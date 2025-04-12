package com.example.tibibalance.ui.feature_register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tibibalance.ui.components.LoadingIndicator
import com.example.tibibalance.ui.components.StyledButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    uiState: RegisterUiState,
    onEvent: (RegisterEvent) -> Unit
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
            OutlinedTextField(
                value = uiState.username,
                onValueChange = { onEvent(RegisterEvent.UsernameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre de Usuario *") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                isError = uiState.usernameError != null,
                supportingText = { uiState.usernameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.dob,
                onValueChange = { onEvent(RegisterEvent.DobChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Fecha de nacimiento (DD/MM/AAAA) *") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri, // Tipo temporal para evitar el teclado estándar
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                isError = uiState.dobError != null,
                supportingText = { uiState.dobError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                isError = uiState.emailError != null,
                supportingText = { uiState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(16.dp))

            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Face else Icons.Filled.Person
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = if (passwordVisible) "Ocultar" else "Mostrar")
                    }
                },
                isError = uiState.passwordError != null,
                supportingText = { uiState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(16.dp))

            var confirmPasswordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirmar contraseña *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    onEvent(RegisterEvent.RegisterClicked)
                }),
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.Face else Icons.Filled.Person
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(image, contentDescription = if (confirmPasswordVisible) "Ocultar" else "Mostrar")
                    }
                },
                isError = uiState.confirmPasswordError != null,
                supportingText = { uiState.confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(32.dp))

            StyledButton(
                text = "Registrarse",
                onClick = {
                    keyboardController?.hide()
                    onEvent(RegisterEvent.RegisterClicked)
                },
                enabled = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                Text(
                    " O ",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
            }
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = { onEvent(RegisterEvent.GoogleSignInClicked) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !uiState.isLoading,
                shape = MaterialTheme.shapes.medium,
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
            ) {
                Text("Continuar con Google", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

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
