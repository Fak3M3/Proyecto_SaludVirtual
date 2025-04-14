package com.example.tibibalance.ui.feature_forgotpassword

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ForgotPasswordViewModel(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

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
        val email = currentState.email

        // Validación de formato de email
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.update { it.copy(emailError = "Formato de correo no válido") }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()

                // Si no lanza excepción, fue exitoso
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Correo enviado. Revisa tu bandeja de entrada."
                    )
                }

            } catch (e: Exception) {
                // Manejo de errores específicos
                val errorMessage = when (e) {
                    is FirebaseAuthInvalidUserException -> "Cuenta no encontrada"
                    is FirebaseNetworkException -> "Sin conexión. Verifica tu red e inténtalo nuevamente."
                    else -> "Error inesperado."
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = errorMessage
                    )
                }
            }
        }
    }
}
