package com.example.tibibalance.ui.feature_forgotpassword

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Archivo: ForgotPasswordViewModel.kt
 * Clase: ForgotPasswordViewModel
 * Descripción: ViewModel para la pantalla de Recuperar Contraseña.
 */
class ForgotPasswordViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun handleEvent(event: ForgotPasswordEvent) {
        when (event) {
            is ForgotPasswordEvent.EmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = event.value,
                        emailError = null,
                        generalError = null,
                        successMessage = null
                    )
                }
            }
            ForgotPasswordEvent.SendButtonClicked -> processSendEmail()
            ForgotPasswordEvent.MessageShown -> {
                _uiState.update {
                    it.copy(generalError = null, successMessage = null)
                }
            }
        }
    }

    private fun processSendEmail() {
        val currentState = _uiState.value
        var hasError = false

        if (currentState.email.isBlank() ||
            !Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()
        ) {
            _uiState.update { it.copy(emailError = "Correo inválido") }
            hasError = true
        }
        if (hasError) return

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(1500) // Simula la llamada al backend
            _uiState.update { it.copy(isLoading = false) }
            // Para simular, podemos decidir:
            if (currentState.email.contains("error", ignoreCase = true)) {
                _uiState.update { it.copy(generalError = "Simulación: Error al enviar correo.") }
            } else {
                _uiState.update { it.copy(successMessage = "Correo enviado. Revisa tu bandeja de entrada.") }
            }
        }
    }
}
