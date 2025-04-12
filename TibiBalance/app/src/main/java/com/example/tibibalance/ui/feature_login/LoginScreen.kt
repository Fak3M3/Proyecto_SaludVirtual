package com.example.tibibalance.ui.feature_login

// ... otras importaciones de foundation, material, runtime ...
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

// --- Importaciones Corregidas ---
// Importa directamente las clases/interfaces desde el paquete donde están definidas (en LoginContract.kt)
import com.example.tibibalance.ui.feature_login.LoginUiState
import com.example.tibibalance.ui.feature_login.LoginEvent
import com.example.tibibalance.ui.feature_login.LoginNavigationEvent // Asegúrate que esta también esté
// --- Fin Importaciones Corregidas ---

import com.example.tibibalance.ui.feature_login.LoginViewModel
import com.example.tibibalance.ui.components.LoadingIndicator
import com.example.tibibalance.ui.components.StyledButton
import com.example.tibibalance.ui.theme.TibiBalanceTheme
import com.example.tibibalance.R
import kotlinx.coroutines.flow.collectLatest

/**
 * Composable principal para la pantalla de Login. (Refactorizado + Navegación)
 * (...) // KDoc anterior omitido
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    // El resto del código de LoginScreen se mantiene igual que en la versión anterior...
    // (Observar estado, obtener onEvent, LaunchedEffects, Scaffold, llamada a LoginContent)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onEvent = viewModel::handleEvent
    val snackbarHostState = remember { SnackbarHostState() }

    // --- Efecto para mostrar errores ---
    LaunchedEffect(uiState.generalError) {
        uiState.generalError?.let { message ->
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Long)
            onEvent(LoginEvent.GeneralErrorShown)
        }
    }

    // --- ¡NUEVO! Efecto para manejar eventos de navegación ---
    LaunchedEffect(Unit) { // Se lanza una sola vez
        viewModel.navigationEvent.collectLatest { event -> // Escucha el SharedFlow
            when (event) {
                LoginNavigationEvent.NavigateToMainGraph -> {
                    println("LoginScreen: Navegación a Main Graph recibida!")
                    onLoginSuccess() // Llama a la lambda de navegación real
                }
                // Manejar otros posibles eventos de navegación aquí si los hubiera
            }
        }
    }
    // --- Fin Efecto de Navegación ---

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

/**
 * Composable que define la UI del contenido de la pantalla de Login. (Sin cambios)
 * (...) // KDoc anterior omitido
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)
@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
    // Estos callbacks se disparan directamente desde la UI
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
            // 1. Logo o Título
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
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                isError = uiState.emailError != null,
                supportingText = { uiState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
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
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                    onEvent(LoginEvent.LoginClicked)
                }),
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
                supportingText = { uiState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 4. Enlace Olvidé Contraseña: Llama directamente a onForgotPasswordClick
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

            // 8. Enlace a Registro: Llama directamente a onRegisterClick
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
        } // Fin Column

        // Indicador de Carga
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
    } // Fin Box principal
}



// --- Previews ---
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun LoginContentAttractivePreviewUpdated() { // Renombrado para evitar conflicto si mantienes el anterior
    TibiBalanceTheme {
        LoginContent(
            uiState = LoginUiState(email = "test@example.com"),
            onEvent = {},
            onRegisterClick = {},
            onForgotPasswordClick = {}
        )
    }
}

// ... otros previews ...

