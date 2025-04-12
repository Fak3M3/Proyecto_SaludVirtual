package com.example.tibibalance.ui.feature_register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<RegisterNavigationEvent>()
    val navigationEvent: SharedFlow<RegisterNavigationEvent> = _navigationEvent.asSharedFlow()

    fun handleEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UsernameChanged -> {
                _uiState.update { it.copy(username = event.value, usernameError = null, generalError = null) }
            }
            is RegisterEvent.DobChanged -> {
                _uiState.update { it.copy(dob = event.value, dobError = null, generalError = null) }
            }
            is RegisterEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.value, emailError = null, generalError = null) }
            }
            is RegisterEvent.PasswordChanged -> {
                _uiState.update { it.copy(password = event.value, passwordError = null, generalError = null) }
            }
            is RegisterEvent.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = event.value, confirmPasswordError = null, generalError = null) }
            }
            RegisterEvent.RegisterClicked -> {
                processRegister()
            }
            RegisterEvent.GoogleSignInClicked -> {
                _uiState.update { it.copy(generalError = "Google Sign-In no implementado") }
            }
            RegisterEvent.MessageShown -> {
                _uiState.update { it.copy(generalError = null, successMessage = null) }
            }
        }
    }

    private fun processRegister() {
        var hasError = false
        val currentState = _uiState.value

        // Validación de cada campo y actualización de errores en el estado.
        if (currentState.username.isBlank()) {
            _uiState.update { it.copy(usernameError = "Nombre de usuario requerido") }
            hasError = true
        }
        if (currentState.dob.isBlank()) {
            _uiState.update { it.copy(dobError = "Fecha de nacimiento requerida") }
            hasError = true
        }
        if (currentState.email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _uiState.update { it.copy(emailError = "Correo inválido") }
            hasError = true
        }
        if (currentState.password.length < 6) {
            _uiState.update { it.copy(passwordError = "Contraseña muy corta (mín 6)") }
            hasError = true
        }
        if (currentState.confirmPassword != currentState.password) {
            _uiState.update { it.copy(confirmPasswordError = "Las contraseñas no coinciden") }
            hasError = true
        }

        if (hasError) return

        // Si no hay errores, se simula la llamada al backend.
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(1500) // Simula tiempo de respuesta
            _uiState.update { it.copy(isLoading = false) }
            // Aquí se simula el resultado del registro:
            val registrationSuccess = true // Cambia a false para simular error
            if (registrationSuccess) {
                _uiState.update { it.copy(successMessage = "Registro exitoso") }
                _navigationEvent.emit(RegisterNavigationEvent.NavigateToMainGraph)
            } else {
                _uiState.update { it.copy(generalError = "Simulación: Error al registrar") }
            }
        }
    }
}
