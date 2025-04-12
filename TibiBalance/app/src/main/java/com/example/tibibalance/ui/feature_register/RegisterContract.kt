package com.example.tibibalance.ui.feature_register

/**
 * Estado inmutable para el proceso de registro.
 */
data class RegisterUiState(
    val username: String = "",
    val dob: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val usernameError: String? = null,
    val dobError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
    val successMessage: String? = null
)

/**
 * Eventos que la UI envía para interactuar en el proceso de registro.
 */
sealed interface RegisterEvent {
    data class UsernameChanged(val value: String) : RegisterEvent
    data class DobChanged(val value: String) : RegisterEvent
    data class EmailChanged(val value: String) : RegisterEvent
    data class PasswordChanged(val value: String) : RegisterEvent
    data class ConfirmPasswordChanged(val value: String) : RegisterEvent
    object RegisterClicked : RegisterEvent
    object GoogleSignInClicked : RegisterEvent
    object MessageShown : RegisterEvent
}

/**
 * Eventos de navegación emitidos por el ViewModel.
 */
sealed interface RegisterNavigationEvent {
    object NavigateToMainGraph : RegisterNavigationEvent
}
