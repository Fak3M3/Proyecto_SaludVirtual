package com.example.tibibalance.ui.feature_login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager // Para mover el foco
import androidx.compose.ui.platform.LocalSoftwareKeyboardController // Para ocultar teclado
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tibibalance.ui.components.LoadingIndicator
import com.example.tibibalance.ui.components.StyledButton // Botón primario
import com.example.tibibalance.ui.theme.TibiBalanceTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch // Para corutinas en placeholders

// --- Placeholders (Mover a LoginContract.kt y LoginViewModel.kt) ---
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null
)

sealed interface LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    object LoginClicked : LoginEvent
    object GoogleSignInClicked : LoginEvent
    object RegisterLinkClicked : LoginEvent
    object ForgotPasswordLinkClicked : LoginEvent
    object GeneralErrorShown : LoginEvent
}
// --- Fin Placeholders ---

@Composable
fun LoginScreen(
    // viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit
) {
    // --- ViewModel y Estado (Usando placeholders por ahora) ---
    var uiState by remember { mutableStateOf(LoginUiState()) }
    val scope = rememberCoroutineScope()
    val onEvent: (LoginEvent) -> Unit = { event ->
        when(event) {
            is LoginEvent.EmailChanged -> uiState = uiState.copy(email = event.value, emailError = null, generalError = null)
            is LoginEvent.PasswordChanged -> uiState = uiState.copy(password = event.value, passwordError = null, generalError = null)
            LoginEvent.LoginClicked -> {
                println("Login Clicked: ${uiState.email}, ${uiState.password}")
                var hasError = false
                if (uiState.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()) { // Validación simple de email
                    uiState = uiState.copy(emailError = "Correo inválido"); hasError = true
                }
                if (uiState.password.length < 6) { // Validación simple de contraseña
                    uiState = uiState.copy(passwordError = "Contraseña muy corta (mín 6)"); hasError = true
                }

                if (!hasError) {
                    uiState = uiState.copy(isLoading = true)
                    scope.launch {
                        delay(1500)
                        uiState = uiState.copy(isLoading = false)
                        // Simulación:
                        // if (uiState.email == "test@test.com") { onLoginSuccess() }
                        // else { uiState = uiState.copy(generalError = "Credenciales inválidas") }
                        uiState = uiState.copy(generalError = "Simulación: Login fallido")
                    }
                }
            }
            LoginEvent.GoogleSignInClicked -> {
                println("Google Sign-In Clicked")
                uiState = uiState.copy(generalError = "Google Sign-In no implementado")
            }
            LoginEvent.RegisterLinkClicked -> onNavigateToRegister()
            LoginEvent.ForgotPasswordLinkClicked -> onNavigateToForgotPassword()
            LoginEvent.GeneralErrorShown -> uiState = uiState.copy(generalError = null)
        }
    }
    // --- Fin Sustitutos ---

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.generalError) {
        uiState.generalError?.let { message ->
            snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Long) // Duración más larga para errores
            onEvent(LoginEvent.GeneralErrorShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LoginContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)
@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEvent: (LoginEvent) -> Unit
) {
    // Controladores para teclado y foco
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 48.dp), // Más padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Logo o Título más prominente
            // Puedes usar un Image si tienes un logo:
            // Image(painter = painterResource(id = R.drawable.ic_logo), contentDescription = "Logo App", modifier = Modifier.size(100.dp))
            Text(
                text = "Bienvenido a TibiBalance",
                style = MaterialTheme.typography.headlineMedium, // Un poco más pequeño que Large
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant, // Color más suave
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
                    imeAction = ImeAction.Next // Acción "Siguiente" en el teclado
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) } // Mueve el foco al siguiente campo
                ),
                singleLine = true,
                isError = uiState.emailError != null,
                supportingText = {
                    uiState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                shape = MaterialTheme.shapes.medium // Bordes redondeados
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
                    imeAction = ImeAction.Done // Acción "Listo" en el teclado
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide() // Oculta el teclado
                        onEvent(LoginEvent.LoginClicked) // Intenta hacer login
                    }
                ),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Face else Icons.Filled.Info
                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                },
                isError = uiState.passwordError != null,
                supportingText = {
                    uiState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                shape = MaterialTheme.shapes.medium // Bordes redondeados
            )
            Spacer(modifier = Modifier.height(8.dp))

            // 4. Enlace Olvidé Contraseña (TextButton es más accesible)
            TextButton(
                onClick = { onEvent(LoginEvent.ForgotPasswordLinkClicked) },
                modifier = Modifier.align(Alignment.End),
                contentPadding = PaddingValues(vertical = 4.dp) // Menos padding vertical
            ) {
                Text("¿Olvidaste tu contraseña? Recupérala aquí")
            }
            Spacer(modifier = Modifier.height(32.dp)) // Más espacio antes del botón principal

            // 5. Botón de Login
            StyledButton(
                text = "Iniciar Sesión",
                onClick = {
                    keyboardController?.hide() // Ocultar teclado al hacer clic
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
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Text(" O ", modifier = Modifier.padding(horizontal = 8.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
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
                // Ícono de Google (requiere añadir el drawable ic_google_logo a res/drawable)
                // try {
                //     Icon(
                //         painter = painterResource(id = R.drawable.ic_google_logo), // Asegúrate que este drawable exista
                //         contentDescription = "Logo de Google",
                //         modifier = Modifier.size(24.dp),
                //         tint = Color.Unspecified // Para que no aplique el contentColor del botón
                //     )
                //     Spacer(modifier = Modifier.width(12.dp))
                // } catch (e: Exception) {
                //     // Manejar el caso si el drawable no existe
                //     println("Error al cargar logo de Google: ${e.message}")
                // }
                Text("Continuar con Google", style = MaterialTheme.typography.labelLarge) // Texto un poco más grande
            }
            Spacer(modifier = Modifier.weight(1f)) // Empuja el siguiente elemento hacia abajo

            // 8. Enlace a Registro (Usando TextButton para claridad)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Aún no tienes cuenta?")
                TextButton(onClick = { onEvent(LoginEvent.RegisterLinkClicked) }) {
                    Text("Crea una") // El TextButton ya le da estilo de enlace
                }
            }
        } // Fin Column

        // Indicador de Carga superpuesto
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center) // Asegura que el Box esté centrado
                // Fondo semi-transparente opcional
                // .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
                ,
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
fun LoginContentAttractivePreview() {
    TibiBalanceTheme {
        LoginContent(
            uiState = LoginUiState(email = "test@example.com"),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun LoginContentAttractiveLoadingPreview() {
    TibiBalanceTheme {
        LoginContent(
            uiState = LoginUiState(isLoading = true),
            onEvent = {}
        )
    }
}
