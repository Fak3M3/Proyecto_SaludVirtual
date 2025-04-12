package com.example.tibibalance.ui.navigation

// Importa Text y Composable solo si necesitaras placeholders aquí, de lo contrario no son necesarios
// import androidx.compose.material3.Text
// import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
// Importa tus pantallas reales (asegúrate que las rutas sean correctas)
import com.example.tibibalance.ui.feature_login.LoginScreen
import com.example.tibibalance.ui.feature_register.RegisterScreen
import com.example.tibibalance.ui.feature_forgotpassword.ForgotPasswordScreen

/**
 * Define el grafo de navegación anidado para el flujo de autenticación.
 * Incluye logs para depurar las llamadas de navegación.
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
            // Llama al Composable LoginScreen real
            LoginScreen(
                // viewModel = hiltViewModel(), // Si usas Hilt
                onLoginSuccess = {
                    println("DEBUG_NAV: onLoginSuccess lambda llamada. Navegando a ${Graph.MAIN}...") // Log
                    navController.navigate(Graph.MAIN) {
                        popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                    }
                    println("DEBUG_NAV: navController.navigate(Graph.MAIN) llamada.") // Log
                },
                onNavigateToRegister = {
                    println("DEBUG_NAV: onNavigateToRegister lambda llamada. Navegando a ${Screen.Register.route}...") // Log
                    navController.navigate(Screen.Register.route)
                    println("DEBUG_NAV: navController.navigate(Register) llamada.") // Log
                },
                onNavigateToForgotPassword = {
                    println("DEBUG_NAV: onNavigateToForgotPassword lambda llamada. Navegando a ${Screen.ForgotPassword.route}...") // Log
                    navController.navigate(Screen.ForgotPassword.route)
                    println("DEBUG_NAV: navController.navigate(ForgotPassword) llamada.") // Log
                }
            )
        }

        // --- Pantalla de Registro ---
        composable(route = Screen.Register.route) {
            // Llama al Composable RegisterScreen real
            RegisterScreen(
                // viewModel = hiltViewModel(), // Si usas Hilt
                onNavigateBack = {
                    println("DEBUG_NAV: onNavigateBack desde Registro llamada.") // Log
                    navController.popBackStack()
                    println("DEBUG_NAV: navController.popBackStack() llamada.") // Log
                },
                onRegistrationSuccess = {
                    println("DEBUG_NAV: onRegistrationSuccess lambda llamada. Navegando a ${Graph.MAIN}...") // Log
                    navController.navigate(Graph.MAIN) {
                        popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                    }
                    println("DEBUG_NAV: navController.navigate(Graph.MAIN) llamada.") // Log
                }
            )
        }

        // --- Pantalla de Recuperar Contraseña ---
        composable(route = Screen.ForgotPassword.route) {
            // Llama al Composable ForgotPasswordScreen real
            ForgotPasswordScreen(
                // viewModel = hiltViewModel(), // Si usas Hilt
                onNavigateBack = {
                    println("DEBUG_NAV: onNavigateBack desde Olvidé Contraseña llamada.") // Log
                    navController.popBackStack()
                    println("DEBUG_NAV: navController.popBackStack() llamada.") // Log
                }
            )
        }
    }
}
