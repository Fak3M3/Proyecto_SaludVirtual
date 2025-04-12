package com.example.tibibalance.ui.feature_forgotpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Icono de regreso auto-espejado
import androidx.compose.material.icons.filled.Lock // Icono de candado de Material como placeholder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource // Para cargar tu imagen/vector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
// Importa componentes y tema
import com.example.tibibalance.R // Importa tu R
import com.example.tibibalance.ui.components.LoadingIndicator
import com.example.tibibalance.ui.components.StyledButton
import com.example.tibibalance.ui.theme.TibiBalanceTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// --- Placeholders (Mover a Contrato y ViewModel) ---
data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val successMessage: String? = null, // Para mensaje de éxito
    val generalError: String? = null
)

sealed interface ForgotPasswordEvent {
    data class EmailChanged(val value: String) : ForgotPasswordEvent
    object SendButtonClicked : ForgotPasswordEvent
    object MessageShown : ForgotPasswordEvent // Para limpiar mensajes de éxito/error
}
// --- Fin Placeholders ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    // viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit // Lambda para manejar la acción de regreso
) {
    // --- Estado y Eventos (Placeholders) ---
    var uiState by remember { mutableStateOf(ForgotPasswordUiState()) }
    val scope = rememberCoroutineScope()
    val onEvent: (ForgotPasswordEvent) -> Unit = { event ->
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> uiState = uiState.copy(email = event.value, emailError = null, generalError = null, successMessage = null)
            ForgotPasswordEvent.SendButtonClicked -> {
                var hasError = false
                if (uiState.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()) {
                    uiState = uiState.copy(emailError = "Correo inválido"); hasError = true
                }
                if (!hasError) {
                    uiState = uiState.copy(isLoading = true)
                    scope.launch {
                        delay(1500) // Simula llamada a backend (Firebase Auth sendPasswordResetEmail)
                        uiState = uiState.copy(isLoading = false)
                        // Simulación:
                        // try { firebaseAuth.sendPasswordResetEmail(uiState.email).await() ... }
                        // uiState = uiState.copy(successMessage = "Correo enviado. Revisa tu bandeja de entrada.")
                        uiState = uiState.copy(generalError = "Simulación: Error al enviar correo.") // O éxito
                    }
                }
            }
            ForgotPasswordEvent.MessageShown -> uiState = uiState.copy(generalError = null, successMessage = null)
        }
    }
    // --- Fin Placeholders ---

    val snackbarHostState = remember { SnackbarHostState() }
    // Muestra Snackbar para errores o éxito
    LaunchedEffect(uiState.generalError, uiState.successMessage) {
        val message = uiState.generalError ?: uiState.successMessage
        message?.let {
            snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Long)
            onEvent(ForgotPasswordEvent.MessageShown) // Notifica que se mostró
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // Barra superior simple con título y botón de regreso
            TopAppBar(
                title = { Text("Recuperar Contraseña") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { // Llama a la lambda de navegación
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Icono estándar de regreso
                            contentDescription = "Volver atrás"
                        )
                    }
                },
                // Puedes quitarle la sombra si prefieres un look más plano
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface, // O surfaceVariant, etc.
                    scrolledContainerColor = MaterialTheme.colorScheme.surface // Color al hacer scroll
                )
            )
        }
    ) { paddingValues ->
        ForgotPasswordContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordContent(
    modifier: Modifier = Modifier,
    uiState: ForgotPasswordUiState,
    onEvent: (ForgotPasswordEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier.fillMaxSize()) { // Para el LoadingIndicator

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 24.dp), // Padding ajustado
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp)) // Espacio después del TopAppBar

            // --- Placeholder para la Imagen del Candado ---
            // Reemplaza R.drawable.ic_placeholder_lock con tu recurso real
            // o usa un Icono de Material como placeholder inicial.
            Icon(
                imageVector = Icons.Filled.Lock, // Placeholder de Material Icons
                contentDescription = "Candado", // Descripción adecuada
                modifier = Modifier
                    .size(120.dp) // Tamaño deseado para la imagen/icono
                    .padding(bottom = 32.dp),
                tint = MaterialTheme.colorScheme.primary // Tinta el icono con el color primario
            )
            // --- Si usas una imagen de drawable: ---
            // try {
            //     Image(
            //         painter = painterResource(id = R.drawable.ic_candado), // Tu drawable aquí
            //         contentDescription = "Candado",
            //         modifier = Modifier.size(150.dp).padding(bottom = 32.dp)
            //     )
            // } catch (e: Exception) {
            //     // Fallback si la imagen no carga
            //     Icon(imageVector = Icons.Filled.Lock, contentDescription = "Candado", modifier = Modifier.size(120.dp).padding(bottom = 32.dp))
            //     println("Error al cargar imagen de candado: ${e.message}")
            // }
            // --- Fin Placeholder ---

            // Texto explicativo
            Text(
                text = "Ingresa tu correo electrónico y enviaremos un link para recuperar tu contraseña",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campo de Email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { onEvent(ForgotPasswordEvent.EmailChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo Electrónico") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done // Acción "Listo"
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide() // Oculta teclado
                        onEvent(ForgotPasswordEvent.SendButtonClicked) // Intenta enviar
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

// --- Previews ---
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun ForgotPasswordContentPreview() {
    TibiBalanceTheme {
        // Simula el Scaffold para dar padding correcto al preview del contenido
        Scaffold(
            topBar = { TopAppBar(title = { Text("Recuperar Contraseña") }, navigationIcon = { Icon(Icons.AutoMirrored.Filled.ArrowBack, null)})}
        ) { paddingValues ->
            ForgotPasswordContent(
                modifier = Modifier.padding(paddingValues),
                uiState = ForgotPasswordUiState(email = "test@example.com"),
                onEvent = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun ForgotPasswordContentLoadingPreview() {
    TibiBalanceTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Recuperar Contraseña") }, navigationIcon = { Icon(Icons.AutoMirrored.Filled.ArrowBack, null)})}
        ) { paddingValues ->
            ForgotPasswordContent(
                modifier = Modifier.padding(paddingValues),
                uiState = ForgotPasswordUiState(isLoading = true),
                onEvent = {}
            )
        }
    }
}
