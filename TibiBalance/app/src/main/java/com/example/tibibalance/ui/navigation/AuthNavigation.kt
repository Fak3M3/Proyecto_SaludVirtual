package com.example.tibibalance.ui.navigation

import android.util.Log // Importa Log si quieres mantener los logs de depuración
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
// Importa tus pantallas reales
import com.example.tibibalance.ui.feature_login.LoginScreen
import com.example.tibibalance.ui.feature_register.RegisterScreen
import com.example.tibibalance.ui.feature_forgotpassword.ForgotPasswordScreen
import com.example.tibibalance.ui.feature_verify_email.VerifyEmailScreen // <-- IMPORTA LA NUEVA PANTALLA

/**
 * Define el grafo de navegación anidado para el flujo de autenticación.
 * Incluye la pantalla de verificación de correo.
 *
 * @param navController El NavController raíz para manejar la navegación.
 */
fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Screen.Login.route // Empezamos en Login
    ) {
        // --- Pantalla de Login ---
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // IMPORTANTE: Aquí deberías añadir la lógica para verificar
                    // si firebaseAuth.currentUser?.isEmailVerified == true
                    // antes de navegar a Graph.MAIN. Si no, mostrar mensaje/opción de reenviar.
                    // Por ahora, mantenemos la navegación directa para el ejemplo.
                    Log.d("AuthNavigation", "Login exitoso. Navegando a ${Graph.MAIN}")
                    navController.navigate(Graph.MAIN) {
                        popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    Log.d("AuthNavigation", "Navegando a ${Screen.Register.route}")
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    Log.d("AuthNavigation", "Navegando a ${Screen.ForgotPassword.route}")
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        // --- Pantalla de Registro ---
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = {
                    Log.d("AuthNavigation", "Navegando atrás desde Registro")
                    navController.popBackStack()
                },
                // CAMBIO: Ahora la lambda onRegistrationSuccess debe navegar a VerifyEmail
                onRegistrationSuccess = { // Esta lambda se llama cuando el ViewModel emite NavigateToVerifyEmail
                    Log.d("AuthNavigation", "Registro exitoso (email enviado). Navegando a ${Screen.VerifyEmail.route}")
                    // Navega a la pantalla de verificación en lugar del grafo principal
                    navController.navigate(Screen.VerifyEmail.route) {
                        // Opcional: podrías querer limpiar la pantalla de registro del backstack
                        popUpTo(Screen.Register.route) { inclusive = true }
                        // O limpiar hasta Login si prefieres que "Ir a Login" desde VerifyEmail
                        // no muestre Registro al volver atrás.
                        // popUpTo(Screen.Login.route) { inclusive = false } // No borra Login
                        launchSingleTop = true // Evita múltiples instancias de VerifyEmail
                    }
                }
                // Nota: Asegúrate que el LaunchedEffect en RegisterScreen.kt maneje
                // RegisterNavigationEvent.NavigateToVerifyEmail y llame a esta lambda onRegistrationSuccess.
            )
        }

        // --- Pantalla de Recuperar Contraseña ---
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = {
                    Log.d("AuthNavigation", "Navegando atrás desde Olvidé Contraseña")
                    navController.popBackStack()
                }
                // Aquí podrías tener una lambda onPasswordResetEmailSent para navegar a Login o mostrar mensaje
            )
        }

        // --- NUEVA PANTALLA: Verificar Correo ---
        composable(route = Screen.VerifyEmail.route) {
            Log.d("AuthNavigation", "Entrando a ${Screen.VerifyEmail.route}")
            VerifyEmailScreen(
                onNavigateToLogin = {
                    Log.d("AuthNavigation", "Navegando desde VerifyEmail a ${Screen.Login.route}")
                    // Navega a Login, limpiando todo el grafo de autenticación hasta ese punto
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
