package com.example.tibibalance.ui.feature_register

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch // Para lanzar corutinas
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID
// esto deberia estar en un archivo separado como variable de entorno
private const val WEB_CLIENT_ID = "467927540157-tvu0re0msga2o01tsj9t1r1o6kqvek3j.apps.googleusercontent.com"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onRegistrationSuccess: () -> Unit,
    onGoogleSignInSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val TAG = "RegisterScreen"
    // Scope de corutina para lanzar la petición de credenciales
    val scope = rememberCoroutineScope()

    // --- Cliente de Credential Manager ---
    val credentialManager = remember { CredentialManager.create(context) }

    // --- Función para iniciar el flujo de Google Sign-In con Credential Manager ---
    fun launchGoogleSignIn() {
        // Generar un nonce aleatorio para seguridad (opcional pero recomendado)
        val nonce = UUID.randomUUID().toString()
        Log.d(TAG, "Generated nonce for Credential Manager: $nonce")

        // 1. Construir la opción específica para Google ID Token
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(true) // o false, según el comportamiento que desees
            .build()

        // 2. Construir la petición general de credenciales añadiendo la opción de Google
        val credentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption) // Añade la opción de Google
            .build()

        // 3. Lanzar la petición usando una corutina
        scope.launch {
            try {
                Log.d(TAG, "Calling credentialManager.getCredential...")
                // Solicitar la credencial (esto puede mostrar la UI de One Tap)
                val result: GetCredentialResponse = credentialManager.getCredential(
                    context, // Puedes pasar tu Activity aquí
                    credentialRequest
                )


                // Obtener la credencial del resultado
                val credential = result.credential
                Log.d(TAG, "getCredential successful, credential type: ${credential.type}")

                // Verificar si es la credencial de Google que esperamos
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    // Extraer el ID Token del Bundle de datos
                    val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    Log.d(TAG, "Google ID Token extracted: ${idToken != null}")
                    if (idToken != null) {
                        // Enviar el token al ViewModel
                        viewModel.handleEvent(RegisterEvent.GoogleIdTokenReceived(idToken))
                    } else {
                        Log.e(TAG, "Credential Manager success but idToken is null")
                        viewModel.handleEvent(RegisterEvent.GoogleIdTokenReceived(null))
                    }
                } else {
                    // Se recibió otro tipo de credencial inesperado
                    Log.e(TAG, "Received unexpected credential type: ${credential.type}")
                    viewModel.handleEvent(RegisterEvent.GoogleIdTokenReceived(null))
                }

            } catch (e: GetCredentialException) {
                // Manejar errores específicos de Credential Manager
                Log.e(TAG, "getCredential failed", e)
                when (e) {
                    is NoCredentialException -> {
                        Log.w(TAG, "No credentials available.")
                        // Informar al usuario que no se encontraron cuentas o necesita configurar una
                    }
                    else -> {
                        Log.e(TAG, "Unexpected GetCredentialException", e)
                    }
                }
                viewModel.handleEvent(RegisterEvent.GoogleIdTokenReceived(null)) // Informa fallo
            } catch (e: Exception) {
                // Captura otros errores inesperados
                Log.e(TAG, "Unexpected error during getCredential", e)
                viewModel.handleEvent(RegisterEvent.GoogleIdTokenReceived(null))
            }
        }
    }


    // --- Efectos ---

    // Efecto para mostrar Snackbar (sin cambios)
    LaunchedEffect(uiState.generalError, uiState.successMessage) {
        val message = uiState.generalError ?: uiState.successMessage
        message?.let {
            Log.d(TAG, "Mostrando Snackbar: $it")
            snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Long)
            viewModel.handleEvent(RegisterEvent.MessageShown)
        }
    }

    // Efecto para manejar eventos de Navegación (sin cambios)
    LaunchedEffect(Unit) {
        Log.d(TAG, "Iniciando colector de navigationEvent")
        viewModel.navigationEvent.collectLatest { event ->
            Log.d(TAG, "Evento de navegación recibido: $event")
            when (event) {
                RegisterNavigationEvent.NavigateToVerifyEmail -> {
                    Log.d(TAG, "Manejando NavigateToVerifyEmail: llamando a onRegistrationSuccess()")
                    onRegistrationSuccess()
                }
                RegisterNavigationEvent.NavigateToMainGraph -> {
                    Log.d(TAG, "Manejando NavigateToMainGraph: llamando a onGoogleSignInSuccess()")
                    onGoogleSignInSuccess()
                }
            }
        }
    }

    // Efecto para manejar efectos de Vista (lanzar Google Sign-In)
    LaunchedEffect(Unit) {
        Log.d(TAG, "Iniciando colector de viewEffect")
        viewModel.viewEffect.collectLatest { effect ->
            Log.d(TAG, "Efecto de vista recibido: $effect")
            when (effect) {
                RegisterViewEffect.LaunchGoogleSignIn -> {
                    Log.d(TAG, "Recibido LaunchGoogleSignIn effect, llamando a launchGoogleSignIn()...")
                    // Llama a la nueva función que usa Credential Manager
                    launchGoogleSignIn()
                }
            }
        }
    }

    // --- UI ---
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(AutoMirrored.Filled.ArrowBack, "Volver atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, // Fondo de la barra a blanco
                    scrolledContainerColor = Color.White, // Fondo al hacer scroll a blanco
                    titleContentColor = Color.Black, // Color del texto del título a negro
                    navigationIconContentColor = Color.Black // Color del ícono de navegación a negro
                )
            )
        }
    ) { paddingValues ->
        RegisterContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onEvent = viewModel::handleEvent
        )
    }
}

// Ya no se necesita la función auxiliar getGoogleSignInClient ni buildOneTapRequest
