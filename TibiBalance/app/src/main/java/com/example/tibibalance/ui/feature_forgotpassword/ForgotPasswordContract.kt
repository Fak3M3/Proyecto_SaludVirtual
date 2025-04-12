package com.example.tibibalance.ui.feature_forgotpassword

/**
 * Estado inmutable para la pantalla de Recuperar Contraseña.
 */
data class ForgotPasswordUiState(
    val email: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val successMessage: String? = null, // Mensaje de éxito, por ejemplo, "Correo enviado..."
    val generalError: String? = null
)

/**
 * Eventos que la UI emitirá para interactuar con el ViewModel.
 */
sealed interface ForgotPasswordEvent {
    data class EmailChanged(val value: String) : ForgotPasswordEvent
    object SendButtonClicked : ForgotPasswordEvent
    object MessageShown : ForgotPasswordEvent // Para limpiar mensajes luego de mostrarlos
}
