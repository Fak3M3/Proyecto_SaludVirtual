package com.example.tibibalance.ui.feature_register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Eventos de entrada para el ViewModel.
 *
 */
sealed interface RegisterViewEffect {
    object LaunchGoogleSignIn : RegisterViewEffect // Señal para que la UI lance el flujo
}

// --- Modelo UserProfile ---
data class UserProfile(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val dob: String = "",
    val photoUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isEmailVerified: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<RegisterNavigationEvent>()
    val navigationEvent: SharedFlow<RegisterNavigationEvent> = _navigationEvent.asSharedFlow()

    private val _viewEffect = MutableSharedFlow<RegisterViewEffect>()
    val viewEffect: SharedFlow<RegisterViewEffect> = _viewEffect.asSharedFlow()

    private val TAG = "RegisterViewModel"
    private val USERS_COLLECTION = "users"

    fun handleEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UsernameChanged -> _uiState.update { it.copy(username = event.value, usernameError = null, generalError = null) }
            is RegisterEvent.DobChanged -> _uiState.update { it.copy(dob = event.value, dobError = null, generalError = null) }
            is RegisterEvent.EmailChanged -> _uiState.update { it.copy(email = event.value, emailError = null, generalError = null) }
            is RegisterEvent.PasswordChanged -> _uiState.update { it.copy(password = event.value, passwordError = null, generalError = null) }
            is RegisterEvent.ConfirmPasswordChanged -> _uiState.update { it.copy(confirmPassword = event.value, confirmPasswordError = null, generalError = null) }
            RegisterEvent.RegisterClicked -> processRegister()
            RegisterEvent.GoogleSignInClicked -> {
                Log.d(TAG, "Google Sign-In button clicked - Emitting LaunchGoogleSignIn effect.")
                viewModelScope.launch {
                    _viewEffect.emit(RegisterViewEffect.LaunchGoogleSignIn)
                }
            }
            is RegisterEvent.GoogleIdTokenReceived -> {
                if (event.idToken != null) {
                    signInWithGoogleCredential(event.idToken)
                } else {
                    Log.e(TAG, "Google Sign-In failed: idToken was null.")
                    _uiState.update { it.copy(isLoading = false, generalError = "Fallo al iniciar sesión con Google (token nulo).") }
                }
            }
            RegisterEvent.MessageShown -> _uiState.update { it.copy(generalError = null, successMessage = null) }
        }
    }

    private fun processRegister() {
        if (!validateInput()) return
        _uiState.update { it.copy(isLoading = true, generalError = null) }
        val currentState = _uiState.value
        Log.d(TAG, "Attempting Firebase registration for email: ${currentState.email}")
        viewModelScope.launch {
            var firebaseUser: FirebaseUser? = null
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(currentState.email.trim(), currentState.password).await()
                firebaseUser = authResult.user
                val userId = firebaseUser?.uid
                if (userId == null || firebaseUser == null) { throw IllegalStateException("Firebase user was null after successful registration.") }
                Log.i(TAG, "Firebase Auth registration successful. User UID: $userId")
                val userProfile = UserProfile(uid = userId, username = currentState.username.trim(), email = currentState.email.trim(), dob = currentState.dob.trim(), isEmailVerified = false)
                Log.d(TAG, "Attempting to save user profile to Firestore for UID: $userId")
                firestore.collection(USERS_COLLECTION).document(userId).set(userProfile).await()
                Log.i(TAG, "User profile saved successfully to Firestore.")
                Log.d(TAG, "Attempting to send verification email to ${firebaseUser.email}")
                firebaseUser.sendEmailVerification().await()
                Log.i(TAG, "Verification email sent successfully.")
                _uiState.update { it.copy(isLoading = false, successMessage = "¡Registro casi listo! Revisa tu correo para verificar tu cuenta.") }
                _navigationEvent.emit(RegisterNavigationEvent.NavigateToVerifyEmail)
            } catch (e: Exception) {
                Log.e(TAG, "Registration process failed", e)
                val errorMessage = when (e) {
                    is FirebaseAuthUserCollisionException -> "Este correo electrónico ya está registrado."
                    else -> "Error en el registro: ${e.localizedMessage ?: "Error desconocido"}"
                }
                _uiState.update { it.copy(isLoading = false, generalError = errorMessage, password = "", confirmPassword = "") }
            }
        }
    }

    private fun signInWithGoogleCredential(idToken: String) {
        _uiState.update { it.copy(isLoading = true, generalError = null) }
        Log.d(TAG, "Attempting Firebase sign-in with Google ID Token.")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            try {
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                val firebaseUser = authResult.user
                val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
                if (firebaseUser == null) { throw IllegalStateException("Firebase user was null after successful Google sign-in.") }
                Log.i(TAG, "Firebase Google Sign-In successful. User UID: ${firebaseUser.uid}, Is new user: $isNewUser")
                if (isNewUser || true) { // Siempre guardar/actualizar perfil en este ejemplo
                    Log.d(TAG, "New/Existing Google user detected. Saving/Updating profile in Firestore.")
                    val userProfile = UserProfile(uid = firebaseUser.uid, username = firebaseUser.displayName ?: "Usuario_${firebaseUser.uid.substring(0, 5)}", email = firebaseUser.email ?: "", dob = "", photoUrl = firebaseUser.photoUrl?.toString(), isEmailVerified = true)
                    firestore.collection(USERS_COLLECTION).document(firebaseUser.uid).set(userProfile).await()
                    Log.i(TAG, "User profile saved/updated in Firestore for Google user.")
                }
                _uiState.update { it.copy(isLoading = false, successMessage = "Inicio de sesión con Google exitoso.") }
                // Google Sign-In navega directo al grafo principal
                _navigationEvent.emit(RegisterNavigationEvent.NavigateToMainGraph)
            } catch (e: Exception) {
                Log.e(TAG, "Firebase Google Sign-In failed", e)
                val errorMessage = when (e) { else -> "Error al iniciar sesión con Google: ${e.localizedMessage ?: "Error desconocido"}" }
                _uiState.update { it.copy(isLoading = false, generalError = errorMessage) }
            }
        }
    }

    private fun validateInput(): Boolean {
        var hasError = false
        val currentState = _uiState.value
        _uiState.update { it.copy(usernameError = null, dobError = null, emailError = null, passwordError = null, confirmPasswordError = null, generalError = null) }
        if (currentState.username.isBlank()) { _uiState.update { it.copy(usernameError = "Nombre de usuario requerido") }; hasError = true }
        if (currentState.dob.isBlank()) { _uiState.update { it.copy(dobError = "Fecha de nacimiento requerida") }; hasError = true }
        if (currentState.email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) { _uiState.update { it.copy(emailError = "Correo electrónico inválido") }; hasError = true }
        if (currentState.password.length < 6) { _uiState.update { it.copy(passwordError = "La contraseña debe tener al menos 6 caracteres") }; hasError = true }
        if (currentState.confirmPassword != currentState.password) { _uiState.update { it.copy(confirmPasswordError = "Las contraseñas no coinciden") }; hasError = true }
        if (hasError) { Log.d(TAG, "Validation failed.") }
        return !hasError
    }
}
