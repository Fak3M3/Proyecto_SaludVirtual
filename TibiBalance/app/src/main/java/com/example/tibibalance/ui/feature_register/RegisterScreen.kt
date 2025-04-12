package com.example.tibibalance.ui.feature_register

import android.util.Patterns // Para validación de email
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Icono de regreso
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
// Importa componentes, tema, etc.
import com.example.tibibalance.ui.components.LoadingIndicator
import com.example.tibibalance.ui.components.StyledButton
import com.example.tibibalance.ui.theme.TibiBalanceTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// --- Placeholders (Mover a Contrato y ViewModel) ---
data class RegisterUiState(
    val username: String = "",
    val dob: String = "", // Fecha de Nacimiento (String por ahora)
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val usernameError: String? = null,
    val dobError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
    val successMessage: String? = null // Mensaje de éxito opcional
)

sealed interface RegisterEvent {
    data class UsernameChanged(val value: String) : RegisterEvent
    data class DobChanged(val value: String) : RegisterEvent // Para el TextField temporal
    // data class DateSelected(val dateMillis: Long) : RegisterEvent // Para DatePicker real
    data class EmailChanged(val value: String) : RegisterEvent
    data class PasswordChanged(val value: String) : RegisterEvent
    data class ConfirmPasswordChanged(val value: String) : RegisterEvent
    object RegisterClicked : RegisterEvent
    object GoogleSignInClicked : RegisterEvent
    object MessageShown : RegisterEvent
}
// --- Fin Placeholders ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    // viewModel: RegisterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onRegistrationSuccess: () -> Unit // Para navegar a Main después del registro
) {
    // --- Estado y Eventos (Placeholders) ---
    var uiState by remember { mutableStateOf(RegisterUiState()) }
    val scope = rememberCoroutineScope()
    val onEvent: (RegisterEvent) -> Unit = { event ->
        when (event) {
            is RegisterEvent.UsernameChanged -> uiState = uiState.copy(username = event.value, usernameError = null, generalError = null)
            is RegisterEvent.DobChanged -> uiState = uiState.copy(dob = event.value, dobError = null, generalError = null) // Para TextField temporal
            is RegisterEvent.EmailChanged -> uiState = uiState.copy(email = event.value, emailError = null, generalError = null)
            is RegisterEvent.PasswordChanged -> uiState = uiState.copy(password = event.value, passwordError = null, generalError = null)
            is RegisterEvent.ConfirmPasswordChanged -> uiState = uiState.copy(confirmPassword = event.value, confirmPasswordError = null, generalError = null)
            RegisterEvent.RegisterClicked -> {
                // --- Lógica de Validación y Registro (Placeholder) ---
                println("Register Clicked")
                var hasError = false
                // Validaciones (deberían estar en el ViewModel)
                if (uiState.username.isBlank()) { uiState = uiState.copy(usernameError = "Nombre de usuario requerido"); hasError = true }
                if (uiState.dob.isBlank()) { uiState = uiState.copy(dobError = "Fecha de nacimiento requerida"); hasError = true } // Validación simple para String
                if (uiState.email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()) { uiState = uiState.copy(emailError = "Correo inválido"); hasError = true }
                if (uiState.password.length < 6) { uiState = uiState.copy(passwordError = "Contraseña muy corta (mín 6)"); hasError = true }
                if (uiState.confirmPassword != uiState.password) { uiState = uiState.copy(confirmPasswordError = "Las contraseñas no coinciden"); hasError = true }

                if (!hasError) {
                    uiState = uiState.copy(isLoading = true)
                    scope.launch {
                        delay(1500) // Simula llamada a backend (Firebase Auth createUserWithEmailAndPassword)
                        uiState = uiState.copy(isLoading = false)
                        // Simulación:
                        // try { firebaseAuth.createUser...; onRegistrationSuccess() } catch { ... }
                        uiState = uiState.copy(generalError = "Simulación: Error al registrar") // O éxito y llamar a onRegistrationSuccess()
                        // onRegistrationSuccess() // Llamar en caso de éxito real
                    }
                }
                // --- Fin Lógica Placeholder ---
            }
            RegisterEvent.GoogleSignInClicked -> {
                println("Google Sign-In Clicked")
                uiState = uiState.copy(generalError = "Google Sign-In no implementado")
            }
            RegisterEvent.MessageShown -> uiState = uiState.copy(generalError = null, successMessage = null)
        }
    }
    // --- Fin Placeholders ---

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.generalError, uiState.successMessage) {
        val message = uiState.generalError ?: uiState.successMessage
        message?.let {
            snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Long)
            onEvent(RegisterEvent.MessageShown)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Crear Cuenta") }, // O "Registro"
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
        RegisterContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onEvent = onEvent
        )
    }
}

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
            // Título opcional dentro del contenido si se prefiere
            // Text("Completa tus datos", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(bottom = 24.dp))

            // --- Campos del Formulario ---
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

            // --- Campo Fecha de Nacimiento (Placeholder con TextField) ---
            // TODO: Reemplazar con DatePickerDialog
            // Al hacer clic en este campo, se debería mostrar un DatePickerDialog.
            // El estado 'dob' guardaría la fecha seleccionada formateada (o un Long con millis).
            OutlinedTextField(
                value = uiState.dob,
                onValueChange = { onEvent(RegisterEvent.DobChanged(it)) }, // Temporal
                // readOnly = true, // Hacerlo readOnly si se usa DatePicker
                modifier = Modifier.fillMaxWidth(),
                // .clickable { /* Aquí mostrarías el DatePickerDialog */ }, // Añadir clickable si es readOnly
                label = { Text("Fecha de nacimiento (DD/MM/AAAA) *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Next), // Temporal, URI para que no abra teclado estándar fácil
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                isError = uiState.dobError != null,
                supportingText = { uiState.dobError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = MaterialTheme.shapes.medium
                // trailingIcon = { Icon(Icons.Default.CalendarToday, "Seleccionar fecha") } // Icono para indicar selección de fecha
            )
            Spacer(modifier = Modifier.height(16.dp))
            // --- Fin Campo Fecha de Nacimiento ---


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
                    IconButton(onClick = { passwordVisible = !passwordVisible }) { Icon(image, if (passwordVisible) "Ocultar" else "Mostrar") }
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
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) { Icon(image, if (confirmPasswordVisible) "Ocultar" else "Mostrar") }
                },
                isError = uiState.confirmPasswordError != null,
                supportingText = { uiState.confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Botón Registrarse
            StyledButton(
                text = "Registrarse",
                onClick = {
                    keyboardController?.hide()
                    onEvent(RegisterEvent.RegisterClicked)
                },
                enabled = !uiState.isLoading
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Separador "O"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
                Text(" O ", modifier = Modifier.padding(horizontal = 8.dp), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Divider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outlineVariant)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Botón Google Sign-In
            OutlinedButton(
                onClick = { onEvent(RegisterEvent.GoogleSignInClicked) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !uiState.isLoading,
                shape = MaterialTheme.shapes.medium,
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp)
            ) {
                // Icon(painter = painterResource(id = R.drawable.ic_google_logo), contentDescription = "Logo Google", modifier = Modifier.size(24.dp), tint=Color.Unspecified)
                // Spacer(modifier = Modifier.width(12.dp))
                Text("Continuar con Google", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(16.dp)) // Espacio al final
        }

        // Indicador de Carga
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize().align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
    } // Fin Box
}


// --- Preview ---
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun RegisterContentPreview() {
    TibiBalanceTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Crear Cuenta") }, navigationIcon = { Icon(Icons.AutoMirrored.Filled.ArrowBack, null)})}
        ) { paddingValues ->
            RegisterContent(
                modifier = Modifier.padding(paddingValues),
                uiState = RegisterUiState(email = "test@example.com"),
                onEvent = {}
            )
        }
    }
}
