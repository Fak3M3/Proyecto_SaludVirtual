package com.example.tibibalance.ui.feature_verify_email

/**
 * Define el estado inmutable para la pantalla de verificación de correo.
 */
data class VerifyEmailUiState(
    val isLoading: Boolean = false, // Para indicar si se está reenviando el correo
    val userEmail: String? = null, // Para mostrar el correo al que se envió (opcional)
    val message: String? = null // Para mostrar mensajes de éxito o error (ej. "Correo reenviado", "Error al reenviar")
)

/**
 * Eventos que la UI puede enviar al ViewModel.
 */
sealed interface VerifyEmailEvent {
    object ResendEmailClicked : VerifyEmailEvent // El usuario hizo clic en reenviar
    object GoToLoginClicked : VerifyEmailEvent   // El usuario hizo clic en ir a Login
    object MessageShown : VerifyEmailEvent       // La UI ya mostró el mensaje (para limpiarlo del estado)
}

/**
 * Eventos de navegación de un solo uso emitidos por el ViewModel.
 */
sealed interface VerifyEmailNavigationEvent {
    object NavigateToLogin : VerifyEmailNavigationEvent // Navegar a la pantalla de Login
}
