package com.example.tibibalance.ui.feature_login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.* // Importa SharedFlow y related
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

    sealed class UiEvent {
        object LaunchGoogleSignIn : UiEvent()
    }

    // Para el inicio de sesión utilizando Google.
    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents: SharedFlow<UiEvent> = _uiEvents

    // Flujo de estado mutable privado gestionado por el ViewModel.
    private val _uiState = MutableStateFlow(LoginUiState())
    // Flujo de estado público inmutable expuesto a la UI.
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // --- Canal para Eventos de Navegación ---
    private val _navigationEvent = MutableSharedFlow<LoginNavigationEvent>()
    val navigationEvent: SharedFlow<LoginNavigationEvent> = _navigationEvent.asSharedFlow()
    // --- Fin Canal ---

    // Variables para rastrear intentos fallidos y bloqueos (por email)
    private val failedAttemptsMap = mutableMapOf<String, Int>()
    private val lockoutTimestampsMap = mutableMapOf<String, Long>()

    private val LOCKOUT_DURATION_MS = 5 * 60 * 1000L // 5 minutos de bloqueo.
    private val MAX_FAILED_ATTEMPTS = 3 // Intentos fallidos.

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
                viewModelScope.launch {
                    _uiEvents.emit(UiEvent.LaunchGoogleSignIn)
                }
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
        val email = currentState.email.trim()
        val password = currentState.password

        var emailError: String? = null
        var passwordError: String? = null
        var hasError = false

        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Correo inválido"
            hasError = true
        }

        if (password.length < 8) {
            passwordError = "Contraseña muy corta (mínimo 8 caracteres)"
            hasError = true
        }

        _uiState.update {
            it.copy(emailError = emailError, passwordError = passwordError)
        }

        if (hasError) return

        // Verificar si está bloqueado
        val lockoutTime = lockoutTimestampsMap[email]
        val currentTime = System.currentTimeMillis()

        if (lockoutTime != null && currentTime < lockoutTime) {
            val remaining = ((lockoutTime - currentTime) / 1000)
            _uiState.update {
                it.copy(generalError = "Cuenta bloqueada. Intenta de nuevo en ${remaining}s")
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            try {
                val auth = com.google.firebase.auth.FirebaseAuth.getInstance()
                val result = auth.signInWithEmailAndPassword(email, password).await()

                val user = result.user
                if (user != null && user.isEmailVerified) {
                    // Resetear intentos fallidos en login exitoso
                    failedAttemptsMap[email] = 0
                    lockoutTimestampsMap.remove(email)

                    _navigationEvent.emit(LoginNavigationEvent.NavigateToMainGraph)
                } else {
                    auth.signOut()
                    _uiState.update {
                        it.copy(generalError = "Correo no verificado.")
                    }
                }
            } catch (e: Exception) {
                // --- Incrementar intentos fallidos ---
                val currentFails = failedAttemptsMap.getOrDefault(email, 0) + 1
                failedAttemptsMap[email] = currentFails

                // Si supera el límite, bloquear por 5 minutos
                if (currentFails >= MAX_FAILED_ATTEMPTS) {
                    lockoutTimestampsMap[email] = System.currentTimeMillis() + LOCKOUT_DURATION_MS
                }

                val errorMsg = when (e) {
                    is com.google.firebase.auth.FirebaseAuthInvalidUserException -> "Usuario no registrado"
                    is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "Contraseña incorrecta"
                    else -> "Error al iniciar sesión: ${e.localizedMessage}"
                }

                _uiState.update {
                    it.copy(generalError = errorMsg)
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun signInWithGoogleCredential(credential: AuthCredential) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = FirebaseAuth.getInstance().signInWithCredential(credential).await()
                if (result.user != null) {
                    _navigationEvent.emit(LoginNavigationEvent.NavigateToMainGraph)
                } else {
                    _uiState.update { it.copy(generalError = "No se pudo autenticar con Google") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(generalError = "Error con Google Sign-In: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

}
