package com.example.tibibalance.ui.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.* // Importa SharedFlow y related
import kotlinx.coroutines.launch
// import dagger.hilt.android.lifecycle.HiltViewModel // Descomentar si se usa Hilt
// import javax.inject.Inject // Descomentar si se usa Hilt

/**
 * ViewModel para la pantalla de Login.
 * Responsable de:
 * - Mantener y exponer el estado de la UI ([LoginUiState]).
 * - Manejar los eventos de la UI ([LoginEvent]).
 * - Ejecutar la lógica de negocio/autenticación (actualmente simulada).
 * - Interactuar con capas inferiores (Repositories/UseCases) - (Pendiente).
 */
// @HiltViewModel // Descomentar si se usa Hilt
class LoginViewModel  : ViewModel() {

    // Flujo de estado mutable privado gestionado por el ViewModel.
    private val _uiState = MutableStateFlow(LoginUiState())
    // Flujo de estado público inmutable expuesto a la UI.
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // --- Canal para Eventos de Navegación ---
    private val _navigationEvent = MutableSharedFlow<LoginNavigationEvent>()
    val navigationEvent: SharedFlow<LoginNavigationEvent> = _navigationEvent.asSharedFlow()
    // --- Fin Canal ---

    init {
        println("DEBUG_VM: LoginViewModel CREADO") // Log de inicialización
    }

    /**
     * Punto de entrada central para procesar todos los eventos provenientes de la UI.
     * Actualiza el estado según el evento recibido.
     *
     * @param event El [LoginEvent] disparado por la UI.
     */
    fun handleEvent(event: LoginEvent) {
        println("DEBUG_VM: Evento Recibido: $event") // Log de evento recibido
        when (event) {
            is LoginEvent.EmailChanged -> {
                // Actualiza el estado con el nuevo valor del email y limpia errores relacionados
                _uiState.update { currentState ->
                    currentState.copy(email = event.value, emailError = null, generalError = null)
                }
                println("DEBUG_VM: Estado Email Actualizado: ${_uiState.value.email}") // Log de estado
            }
            is LoginEvent.PasswordChanged -> {
                // Actualiza el estado con el nuevo valor de la contraseña y limpia errores relacionados
                _uiState.update { currentState ->
                    currentState.copy(password = event.value, passwordError = null, generalError = null)
                }
                println("DEBUG_VM: Estado Password Actualizado: ${_uiState.value.password}") // Log de estado
            }
            LoginEvent.LoginClicked -> {
                // Llama a la función que procesa el intento de login
                processLogin()
            }
            LoginEvent.GoogleSignInClicked -> {
                // TODO: Implementar lógica de inicio de sesión con Google.
                println("ViewModel: Google Sign-In Clicked (Lógica pendiente)")
                // Actualiza el estado para mostrar un mensaje temporal o manejar el inicio del flujo
                _uiState.update { it.copy(generalError = "Google Sign-In no implementado") }
            }
            // Los eventos de navegación pura (clic en enlaces) son manejados directamente por la UI
            // llamando a las lambdas de navegación (onNavigateToRegister, etc.).
            // El ViewModel no necesita manejarlos directamente aquí, a menos que se requiera
            // alguna lógica previa (poco común para estos casos).
            LoginEvent.RegisterLinkClicked -> { /* No necesita acción en ViewModel */ }
            LoginEvent.ForgotPasswordLinkClicked -> { /* No necesita acción en ViewModel */ }

            LoginEvent.GeneralErrorShown -> {
                // Limpia el error general una vez que la UI lo ha mostrado (e.g., en un Snackbar).
                _uiState.update { it.copy(generalError = null) }
                println("DEBUG_VM: Error general limpiado") // Log de estado
            }
        }
    }

    /**
     * Procesa el intento de inicio de sesión con correo y contraseña.
     * Realiza validaciones y simula la llamada al backend.
     */
    private fun processLogin() {
        val currentState = _uiState.value
        var emailError: String? = null
        var passwordError: String? = null
        var hasError = false

        if (currentState.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            emailError = "Correo inválido"
            hasError = true
        }
        if (currentState.password.length < 6) { // FALTA MODIFICAR LA REGEX PARA QUE SEA MÁS FUERTE LA CONTRASEÑA
            passwordError = "Contraseña muy corta (mín 6)"
            hasError = true
        }
        // --- Fin Validaciones ---

        // Actualiza el estado con los errores de validación (si los hay) ANTES de continuar
        _uiState.update { it.copy(emailError = emailError, passwordError = passwordError) }

        // Si no hay errores de validación, procede con la simulación de login
        if (!hasError) {
            _uiState.update { it.copy(isLoading = true, generalError = null) } // Inicia carga
            println("DEBUG_VM: Iniciando proceso de login simulado...") // Log

            viewModelScope.launch {
                delay(1500) // Simula la llamada de red/backend

                // --- Lógica de Autenticación Simulada ---
                val loginExitosoSimulado = true // <-- Puesto en true para probar
                // --- Fin Simulación ---

                println("DEBUG_VM: Simulación de backend terminada. Éxito: $loginExitosoSimulado") // Log

                _uiState.update { it.copy(isLoading = false) } // Quita el loading en ambos casos

                if (loginExitosoSimulado) {
                    // --- ¡ACCIÓN CLAVE! Emitir evento de navegación ---
                    println("DEBUG_VM: Emitiendo evento NavigateToMainGraph") // Log
                    _navigationEvent.emit(LoginNavigationEvent.NavigateToMainGraph)
                    // --- Fin Acción Clave ---
                } else {
                    // Simula error de credenciales
                    _uiState.update {
                        it.copy(generalError = "Simulación: Credenciales inválidas o error de red.")
                    }
                    println("DEBUG_VM: Estado actualizado con error general") // Log
                }
            }
        } else {
            println("DEBUG_VM: Login no procesado debido a errores de validación.") // Log
        }
    }
}
