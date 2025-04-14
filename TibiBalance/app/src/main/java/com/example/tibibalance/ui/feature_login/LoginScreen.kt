package com.example.tibibalance.ui.feature_login

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider


/**
 * Composable principal para la pantalla de Login.
 * Se encarga de recoger el estado del ViewModel, lanzar los efectos (errores y navegación)
 * y dibujar el Scaffold que contiene el contenido real de la pantalla.
 *
 * @param viewModel El ViewModel asociado a la pantalla (por defecto se obtiene usando viewModel()).
 * @param onLoginSuccess Lambda para navegar al grafo principal una vez el login es exitoso.
 * @param onNavigateToRegister Lambda para navegar a la pantalla de registro.
 * @param onNavigateToForgotPassword Lambda para navegar a la pantalla de recuperar contraseña.
 */
@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    // Observa el estado de la UI expuesto por el ViewModel
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Función para disparar eventos
    val onEvent = viewModel::handleEvent
    // SnackbarHost para mostrar errores o mensajes
    val snackbarHostState = SnackbarHostState()
    val context = LocalContext.current as Activity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithGoogleCredential(credential)
        } catch (e: Exception) {
            viewModel.handleEvent(LoginEvent.GeneralErrorShown)
            viewModel.uiState.value.generalError = "Error en Google Sign-In: ${e.localizedMessage}"
        }
    }


    // Efecto para mostrar errores generales mediante Snackbar
    LaunchedEffect(uiState.generalError) {
        uiState.generalError?.let { message ->
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Long)
            onEvent(LoginEvent.GeneralErrorShown)
        }
    }

    // Efecto para escuchar los eventos de navegación emitidos por el ViewModel
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                LoginNavigationEvent.NavigateToMainGraph -> onLoginSuccess()
            }
        }
    }

    // Escuchar evento de Google.
    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is LoginViewModel.UiEvent.LaunchGoogleSignIn -> {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("CLIENT_ID") // Ocupo el Id de Cliente para Google.
                        .requestEmail()
                        .build()

                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    launcher.launch(googleSignInClient.signInIntent)
                }
            }
        }
    }

    // Se muestra el Scaffold con la UI del contenido
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LoginContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onEvent = onEvent,
            onRegisterClick = onNavigateToRegister,
            onForgotPasswordClick = onNavigateToForgotPassword
        )
    }
}
