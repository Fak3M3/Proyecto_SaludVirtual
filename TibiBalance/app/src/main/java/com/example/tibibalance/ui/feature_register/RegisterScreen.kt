package com.example.tibibalance.ui.feature_register

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onRegistrationSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Efecto para mostrar errores o mensajes de éxito mediante Snackbar.
    LaunchedEffect(uiState.generalError, uiState.successMessage) {
        val message = uiState.generalError ?: uiState.successMessage
        message?.let {
            snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Long)
            viewModel.handleEvent(RegisterEvent.MessageShown)
        }
    }

    // Efecto para escuchar los eventos de navegación.
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { event ->
            when (event) {
                RegisterNavigationEvent.NavigateToMainGraph -> onRegistrationSuccess()
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
        RegisterContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onEvent = viewModel::handleEvent
        )
    }
}
