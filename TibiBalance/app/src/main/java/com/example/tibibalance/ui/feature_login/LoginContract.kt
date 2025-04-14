package com.example.tibibalance.ui.feature_login

/**
 * Define el estado inmutable necesario para renderizar la UI de Login.
 * El ViewModel expondrá este estado y la UI lo observará.
 * (...) // KDoc anterior omitido por brevedad
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    var generalError: String? = null
    // Podrías añadir aquí 'val loginSuccess: Boolean = false' (Opción 1 comentada en VM)
)

/**
 * Define todos los posibles eventos/acciones que la UI de Login
 * puede enviar al LoginViewModel para ser procesados.
 */
sealed interface LoginEvent {
    data class EmailChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    object LoginClicked : LoginEvent
    object GoogleSignInClicked : LoginEvent
    object RegisterLinkClicked : LoginEvent
    object ForgotPasswordLinkClicked : LoginEvent
    object GeneralErrorShown : LoginEvent
    // object NavigationCompleted : LoginEvent
}

/**
 * Define los eventos de navegación que el ViewModel puede enviar a la UI.
 * Son eventos de "una sola vez".
 */
sealed interface LoginNavigationEvent {
    object NavigateToMainGraph : LoginNavigationEvent // Evento para indicar navegación exitosa
}

