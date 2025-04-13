package com.example.tibibalance.ui.feature_login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tibibalance.ui.components.LoadingIndicator
import com.example.tibibalance.ui.components.StyledButton
import com.example.tibibalance.ui.theme.TibiBalanceTheme
import com.example.tibibalance.R

/**
 * Composable que define la UI del contenido de la pantalla de Login.
 * Recibe el estado (uiState) y las funciones para disparar eventos y navegación.
 *
 * @param modifier Modificador para personalizar el layout.
 * @param uiState Estado de la UI (correo, contraseña, errores, etc.).
 * @param onEvent Función para disparar eventos del Login (por ejemplo, cambios de texto o clic en login).
 * @param onRegisterClick Función a ejecutar cuando se presione el enlace de registro.
 * @param onForgotPasswordClick Función a ejecutar cuando se presione el enlace de recuperar contraseña.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)
@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Título y subtítulo de bienvenida
            Text(
                text = "Bienvenido a TibiBalance",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // 2. Campo de Email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo Electrónico") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                isError = uiState.emailError != null,
                supportingText = {
                    uiState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 3. Campo de Contraseña
            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        onEvent(LoginEvent.LoginClicked)
                    }
                ),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Face else Icons.Filled.Person
                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = description)
                    }
                },
                isError = uiState.passwordError != null,
                supportingText = {
                    uiState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 4. Enlace Olvidé Contraseña: llama directamente a la lambda onForgotPasswordClick
            TextButton(
                onClick = { onForgotPasswordClick() },
                modifier = Modifier.align(Alignment.End),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                Text("¿Olvidaste tu contraseña? Recupérala aquí")
            }
            Spacer(modifier = Modifier.height(32.dp))

            // 5. Botón de Login
            StyledButton(
                text = "Iniciar Sesión",
                onClick = {
                    keyboardController?.hide()
                    onEvent(LoginEvent.LoginClicked)
                },
                enabled = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(24.dp))

            // 6. Separador "O"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant
                )
                Text(
                    " O ",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // 7. Botón de Google Sign-In
            OutlinedButton(
                onClick = { onEvent(LoginEvent.GoogleSignInClicked) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !uiState.isLoading,
                shape = MaterialTheme.shapes.medium,
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
            ) {
                Text("Continuar con Google", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.weight(1f))

            // 8. Enlace a Registro: llama directamente a la lambda onRegisterClick
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Aún no tienes cuenta?")
                TextButton(onClick = { onRegisterClick() }) {
                    Text("Crea una")
                }
            }
        } // Fin de Column

        // Indicador de carga
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
    } // Fin de Box principal
}
