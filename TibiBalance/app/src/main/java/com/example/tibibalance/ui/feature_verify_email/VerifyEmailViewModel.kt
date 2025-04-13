package com.example.tibibalance.ui.feature_verify_email

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class VerifyEmailViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(VerifyEmailUiState())
    val uiState: StateFlow<VerifyEmailUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<VerifyEmailNavigationEvent>()
    val navigationEvent: SharedFlow<VerifyEmailNavigationEvent> = _navigationEvent.asSharedFlow()

    private val TAG = "VerifyEmailViewModel"

    init {
        // Obtener el correo del usuario actual al iniciar (si existe)
        _uiState.update { it.copy(userEmail = firebaseAuth.currentUser?.email) }
    }

    fun handleEvent(event: VerifyEmailEvent) {
        when (event) {
            VerifyEmailEvent.ResendEmailClicked -> resendVerificationEmail()
            VerifyEmailEvent.GoToLoginClicked -> navigateToLogin()
            VerifyEmailEvent.MessageShown -> _uiState.update { it.copy(message = null) } // Limpia el mensaje
        }
    }

    private fun resendVerificationEmail() {
        _uiState.update { it.copy(isLoading = true, message = null) } // Inicia carga y limpia mensaje previo
        val user = firebaseAuth.currentUser

        if (user == null) {
            Log.w(TAG, "Resend verification email requested but user is null.")
            _uiState.update { it.copy(isLoading = false, message = "Error: No hay usuario activo.") }
            // Considera navegar a Login si no hay usuario
            // navigateToLogin()
            return
        }

        if (user.isEmailVerified) {
            Log.i(TAG, "User email is already verified.")
            _uiState.update { it.copy(isLoading = false, message = "Tu correo ya está verificado.") }
            // Podrías navegar a login directamente si ya está verificado
            // navigateToLogin()
            return
        }


        viewModelScope.launch {
            try {
                Log.d(TAG, "Attempting to resend verification email to ${user.email}")
                user.sendEmailVerification().await()
                Log.i(TAG, "Verification email resent successfully.")
                _uiState.update { it.copy(isLoading = false, message = "Correo de verificación reenviado.") }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to resend verification email", e)
                _uiState.update { it.copy(isLoading = false, message = "Error al reenviar correo: ${e.localizedMessage}") }
            }
        }
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            _navigationEvent.emit(VerifyEmailNavigationEvent.NavigateToLogin)
        }
    }
}
