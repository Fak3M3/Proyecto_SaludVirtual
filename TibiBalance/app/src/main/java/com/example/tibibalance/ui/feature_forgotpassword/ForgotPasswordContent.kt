package com.example.tibibalance.ui.feature_forgotpassword

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tibibalance.ui.components.LoadingIndicator
import com.example.tibibalance.ui.components.StyledButton

/**
 * Composable que define la UI del contenido para recuperar la contraseña.
 * Se limita a renderizar el formulario.
 *
 * @param modifier Modificador para personalizar el layout.
 * @param uiState Estado de la UI (correo, errores, indicador de carga, etc.).
 * @param onEvent Función para disparar eventos (por ejemplo, cambios de email, clic en enviar).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordContent(
    modifier: Modifier = Modifier,
    uiState: ForgotPasswordUiState,
    onEvent: (ForgotPasswordEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Icono de candado como placeholder
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Candado",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            // Texto explicativo
            Text(
                text = "Ingresa tu correo electrónico y enviaremos un link para recuperar tu contraseña",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campo de correo electrónico
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { onEvent(ForgotPasswordEvent.EmailChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo Electrónico") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onEvent(ForgotPasswordEvent.SendButtonClicked)
                    }
                ),
                singleLine = true,
                isError = uiState.emailError != null,
                supportingText = {
                    uiState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Botón Enviar
            StyledButton(
                text = "Enviar",
                onClick = {
                    keyboardController?.hide()
                    onEvent(ForgotPasswordEvent.SendButtonClicked)
                },
                enabled = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Indicador de carga central si isLoading es true
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
    }
}
