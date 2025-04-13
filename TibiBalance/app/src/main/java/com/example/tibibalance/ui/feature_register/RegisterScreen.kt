package com.example.tibibalance.ui.feature_register

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
// import androidx.compose.material.icons.filled.ArrowBack // No necesario si usas AutoMirrored
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel // Importa hiltViewModel si es el que te funcionó
import androidx.lifecycle.compose.collectAsStateWithLifecycle
// import androidx.lifecycle.viewmodel.compose.viewModel // O viewModel si usas esa
import kotlinx.coroutines.flow.collectLatest
import android.util.Log // Para depuración

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    // Asegúrate de usar hiltViewModel() o viewModel() según lo que te funcionó antes
    viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    // Esta lambda ahora será llamada cuando el ViewModel emita NavigateToVerifyEmail
    onRegistrationSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val TAG = "RegisterScreen" // Tag para logs

    // Efecto para mostrar errores o mensajes de éxito mediante Snackbar.
    LaunchedEffect(uiState.generalError, uiState.successMessage) {
        // Usamos uiState.message si lo centralizas, o mantenemos la lógica actual
        val message = uiState.generalError ?: uiState.successMessage
        message?.let {
            Log.d(TAG, "Mostrando Snackbar: $it")
            snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Long)
            viewModel.handleEvent(RegisterEvent.MessageShown) // Limpia el mensaje después de mostrarlo
        }
    }

    // Efecto para escuchar los eventos de navegación.
    // ¡AQUÍ ESTABA EL PROBLEMA!
    LaunchedEffect(Unit) {
        Log.d(TAG, "Iniciando colector de navigationEvent")
        viewModel.navigationEvent.collectLatest { event ->
            Log.d(TAG, "Evento de navegación recibido: $event")
            when (event) {
                // --- CORRECCIÓN ---
                // Cuando el ViewModel indica éxito y que se envió el correo,
                // llamamos a la lambda onRegistrationSuccess que nos pasaron.
                // Esta lambda (definida en AuthNavigation.kt) se encargará
                // de realizar la navegación real a Screen.VerifyEmail.route.
                RegisterNavigationEvent.NavigateToVerifyEmail -> {
                    Log.d(TAG, "Manejando NavigateToVerifyEmail: llamando a onRegistrationSuccess()")
                    onRegistrationSuccess() // <-- REEMPLAZA EL TODO() CON ESTO
                }
                // Este caso podría ya no ser necesario si Google Sign-In también
                // usa su propia lambda o si se maneja de otra forma.
                // Por ahora lo dejamos por si acaso.
                RegisterNavigationEvent.NavigateToMainGraph -> {
                    Log.w(TAG,"Recibido NavigateToMainGraph, pero el flujo normal va a VerifyEmail. ¿Es de Google Sign-In?")
                    // Si Google Sign-In usa la misma lambda onRegistrationSuccess, necesitarás
                    // diferenciar o pasar lambdas separadas. Asumamos que onRegistrationSuccess
                    // es solo para el registro estándar por ahora.
                    // Considera añadir una lambda separada para Google Sign-In si es necesario.
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        // Asume que RegisterContent existe y funciona como antes
        RegisterContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onEvent = viewModel::handleEvent
        )
    }
}
