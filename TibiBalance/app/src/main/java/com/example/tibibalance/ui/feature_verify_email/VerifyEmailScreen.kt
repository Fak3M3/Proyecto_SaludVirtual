package com.example.tibibalance.ui.feature_verify_email

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel // Importa hiltViewModel si lo usas
import androidx.lifecycle.compose.collectAsStateWithLifecycle
// import androidx.lifecycle.viewmodel.compose.viewModel // O viewModel si usas esa
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreen(
    // Usa hiltViewModel() o viewModel() según lo que te funcionó
    viewModel: VerifyEmailViewModel = hiltViewModel(),
    // Lambdas para manejar la navegación real
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Efecto para mostrar mensajes (éxito/error al reenviar)
    LaunchedEffect(uiState.message) {
        uiState.message?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            // Informa al ViewModel que el mensaje se mostró
            viewModel.handleEvent(VerifyEmailEvent.MessageShown)
        }
    }

    // Efecto para manejar eventos de navegación
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                VerifyEmailNavigationEvent.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Verificación Requerida") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface, // O el color que prefieras
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
                // Sin botón de navegación para no volver a Registro fácilmente
            )
        }
    ) { paddingValues ->
        VerifyEmailContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onEvent = viewModel::handleEvent // Pasa los eventos de UI al ViewModel
        )
    }
}
