package com.example.tibibalance.ui.feature_forgotpassword

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import com.example.tibibalance.ui.theme.TibiBalanceTheme
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.remember

/**
 * Contenedor de la pantalla de Recuperar Contraseña.
 * Se encarga de observar el estado, lanzar efectos (por ejemplo, mostrar Snackbar) y
 * montar el contenido mediante ForgotPasswordContent.
 *
 * @param viewModel Por defecto se obtiene usando viewModel()
 * @param onNavigateBack Lambda para regresar (por ejemplo, volver a la pantalla anterior)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Efecto para mostrar mensajes (error o éxito)
    LaunchedEffect(uiState.generalError, uiState.successMessage) {
        val message = uiState.generalError ?: uiState.successMessage
        message?.let {
            snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Long)
            viewModel.handleEvent(ForgotPasswordEvent.MessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Contraseña") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
        ForgotPasswordContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onEvent = viewModel::handleEvent
        )
    }
}
