package com.example.tibibalance.ui.feature_verify_email

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tibibalance.ui.components.LoadingIndicator // Asume que tienes este componente

@Composable
fun VerifyEmailContent(
    modifier: Modifier = Modifier,
    uiState: VerifyEmailUiState,
    onEvent: (VerifyEmailEvent) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Verifica tu Correo Electrónico",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            val emailText = uiState.userEmail ?: "tu dirección de correo"
            Text(
                text = "Hemos enviado un enlace de verificación a $emailText. Por favor, revisa tu bandeja de entrada (y la carpeta de spam) y haz clic en el enlace para activar tu cuenta.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Botón para reenviar correo
            Button(
                onClick = { onEvent(VerifyEmailEvent.ResendEmailClicked) },
                enabled = !uiState.isLoading // Deshabilitado mientras carga
            ) {
                Text("Reenviar Correo")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para ir a Login
            OutlinedButton(
                onClick = { onEvent(VerifyEmailEvent.GoToLoginClicked) }
            ) {
                Text("Ir a Inicio de Sesión")
            }
        }

        // Indicador de carga (se muestra encima de todo si isLoading es true)
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
    }
}
