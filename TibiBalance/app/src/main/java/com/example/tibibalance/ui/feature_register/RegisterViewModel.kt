package com.example.tibibalance.ui.feature_register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseUser // Importa FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


// Modelo simple para los datos del usuario en Firestore (puede estar en otro archivo)
data class UserProfile(
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    val dob: String = "",
    val photoUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val isEmailVerified: Boolean = false // Añadir campo para estado de verificación
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

    private val TAG = "RegisterViewModel"
    private val USERS_COLLECTION = "users"

    fun handleEvent(event: RegisterEvent) {
        // ... (sin cambios en el when) ...
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
                Log.d(TAG, "Google Sign-In button clicked - UI should launch flow.")
            }
            is RegisterEvent.GoogleIdTokenReceived -> {
                if (event.idToken != null) {
                    signInWithGoogleCredential(event.idToken)
                } else {
                    Log.e(TAG, "Google Sign-In failed: idToken was null.")
                    _uiState.update { it.copy(isLoading = false, generalError = "Fallo al iniciar sesión con Google (token nulo).") }
                }
            }
            RegisterEvent.MessageShown -> {
                _uiState.update { it.copy(generalError = null, successMessage = null) }
            }
        }
    }

    private fun processRegister() {
        if (!validateInput()) return

        _uiState.update { it.copy(isLoading = true, generalError = null) }
        val currentState = _uiState.value
        Log.d(TAG, "Attempting Firebase registration for email: ${currentState.email}")

        viewModelScope.launch {
            var firebaseUser: FirebaseUser? = null // Cambiado para guardar el objeto User
            try {
                // 1. Crear usuario en Firebase Auth
                val authResult = firebaseAuth.createUserWithEmailAndPassword(
                    currentState.email.trim(),
                    currentState.password
                ).await()

                firebaseUser = authResult.user // Guarda el objeto FirebaseUser
                val userId = firebaseUser?.uid

                if (userId == null || firebaseUser == null) { // Doble check
                    throw IllegalStateException("Firebase user was null after successful registration.")
                }
                Log.i(TAG, "Firebase Auth registration successful. User UID: $userId")

                // 2. Crear objeto UserProfile
                val userProfile = UserProfile(
                    uid = userId,
                    username = currentState.username.trim(),
                    email = currentState.email.trim(),
                    dob = currentState.dob.trim(),
                    isEmailVerified = false // Inicialmente no verificado
                )

                // 3. Guardar UserProfile en Firestore
                Log.d(TAG, "Attempting to save user profile to Firestore for UID: $userId")
                firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .set(userProfile)
                    .await()
                Log.i(TAG, "User profile saved successfully to Firestore.")

                // 4. Enviar Correo de Verificación <-- NUEVO PASO
                Log.d(TAG, "Attempting to send verification email to ${firebaseUser.email}")
                firebaseUser.sendEmailVerification().await() // Espera a que se complete el envío
                Log.i(TAG, "Verification email sent successfully.")

                // 5. Éxito Parcial (Esperando Verificación): Actualizar UI y Navegar a pantalla intermedia <-- CAMBIO
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        // Mantenemos mensaje de éxito, pero la navegación cambia
                        successMessage = "¡Registro casi listo! Revisa tu correo para verificar tu cuenta."
                    )
                }
                // Navegar a una pantalla/estado que indique "Verifica tu correo"
                _navigationEvent.emit(RegisterNavigationEvent.NavigateToVerifyEmail) // <-- CAMBIO

            } catch (e: Exception) {
                // --- Manejo de Errores ---
                Log.e(TAG, "Registration process failed", e)

                // Consideración: Si el envío del email falla después de crear el usuario y guardar en Firestore,
                // el usuario existe pero no recibió el correo. Podrías querer reintentar el envío o
                // simplemente informar del error específico del envío de correo.
                // Por ahora, lo tratamos como un error general de registro.

                val errorMessage = when (e) {
                    is FirebaseAuthUserCollisionException -> "Este correo electrónico ya está registrado."
                    // Podrías añadir un caso específico para errores de sendEmailVerification si quieres
                    else -> "Error en el registro: ${e.localizedMessage ?: "Error desconocido"}"
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = errorMessage,
                        password = "",
                        confirmPassword = ""
                    )
                }
                // No borramos el usuario de Auth aquí, ya que podría ser un error recuperable (ej. fallo de red al enviar email)
            }
        }
    }

    // --- Lógica de Google Sign-In (sin cambios aquí, pero notar que no envía email de verificación) ---
    // Nota: Google Sign-In usualmente ya provee correos verificados por Google.
    // Podrías querer marcar isEmailVerified = true en el UserProfile para usuarios de Google.
    private fun signInWithGoogleCredential(idToken: String) {
        _uiState.update { it.copy(isLoading = true, generalError = null) }
        Log.d(TAG, "Attempting Firebase sign-in with Google ID Token.")

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        viewModelScope.launch {
            try {
                val authResult = firebaseAuth.signInWithCredential(credential).await()
                val firebaseUser = authResult.user
                val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

                if (firebaseUser == null) {
                    throw IllegalStateException("Firebase user was null after successful Google sign-in.")
                }
                Log.i(TAG, "Firebase Google Sign-In successful. User UID: ${firebaseUser.uid}, Is new user: $isNewUser")

                // Si es usuario nuevo O si queremos asegurar que el perfil exista/esté actualizado
                if (isNewUser || true) { // Siempre guardar/actualizar perfil en este ejemplo
                    Log.d(TAG, "New/Existing Google user detected. Saving/Updating profile in Firestore.")
                    val userProfile = UserProfile(
                        uid = firebaseUser.uid,
                        username = firebaseUser.displayName ?: "Usuario_${firebaseUser.uid.substring(0, 5)}",
                        email = firebaseUser.email ?: "",
                        dob = "", // No tenemos DoB de Google
                        photoUrl = firebaseUser.photoUrl?.toString(),
                        isEmailVerified = true // Asumimos verificado por Google
                    )
                    firestore.collection(USERS_COLLECTION)
                        .document(firebaseUser.uid)
                        .set(userProfile) // Usar set() para crear o sobrescribir
                        .await()
                    Log.i(TAG, "User profile saved/updated in Firestore for Google user.")
                }

                // Éxito: Actualizar UI y Navegar (Google Sign-In sí va directo al Main Graph)
                _uiState.update { it.copy(isLoading = false, successMessage = "Inicio de sesión con Google exitoso.") }
                _navigationEvent.emit(RegisterNavigationEvent.NavigateToMainGraph) // Google SÍ va al grafo principal

            } catch (e: Exception) {
                Log.e(TAG, "Firebase Google Sign-In failed", e)
                val errorMessage = when (e) {
                    else -> "Error al iniciar sesión con Google: ${e.localizedMessage ?: "Error desconocido"}"
                }
                _uiState.update { it.copy(isLoading = false, generalError = errorMessage) }
            }
        }
    }

    // Función auxiliar para validación (sin cambios)
    private fun validateInput(): Boolean {
        var hasError = false
        val currentState = _uiState.value
        _uiState.update { it.copy(
            usernameError = null, dobError = null, emailError = null,
            passwordError = null, confirmPasswordError = null, generalError = null
        )}
        if (currentState.username.isBlank()) {
            _uiState.update { it.copy(usernameError = "Nombre de usuario requerido") }
            hasError = true
        }
        if (currentState.dob.isBlank()) {
            _uiState.update { it.copy(dobError = "Fecha de nacimiento requerida") }
            hasError = true
        }
        if (currentState.email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _uiState.update { it.copy(emailError = "Correo electrónico inválido") }
            hasError = true
        }
        if (currentState.password.length < 6) {
            _uiState.update { it.copy(passwordError = "La contraseña debe tener al menos 6 caracteres") }
            hasError = true
        }
        if (currentState.confirmPassword != currentState.password) {
            _uiState.update { it.copy(confirmPasswordError = "Las contraseñas no coinciden") }
            hasError = true
        }
        if (hasError) { Log.d(TAG, "Validation failed.") }
        return !hasError
    }
}