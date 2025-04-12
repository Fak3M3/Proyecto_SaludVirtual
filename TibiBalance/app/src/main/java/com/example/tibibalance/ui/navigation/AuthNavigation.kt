package com.example.tibibalance.ui.navigation

import androidx.compose.material3.Text // Para los placeholders
import androidx.compose.runtime.Composable // Para los placeholders
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
// Importa los composables placeholder que crearemos a continuación
import com.example.tibibalance.ui.feature_login.LoginScreen
import com.example.tibibalance.ui.feature_register.RegisterScreen
import com.example.tibibalance.ui.feature_forgotpassword.ForgotPasswordScreen // Asumiendo esta ubicación

/**
 * Define el grafo de navegación anidado para el flujo de autenticación.
 * Llama a Composables placeholder para cada pantalla.
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
            // Llama al Composable placeholder de Login
            LoginScreen(
                // Define las acciones de navegación desde Login
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                // Esta navegación se definirá aquí, pero no hará nada visible
                // hasta que implementemos mainGraph y MainAppScaffold
                onLoginSuccess = {
                    navController.navigate(Graph.MAIN) {
                        popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                    }
                }
            )
        }

        // --- Pantalla de Registro ---
        composable(route = Screen.Register.route) {
            // Llama al Composable placeholder de Registro
            RegisterScreen(
                // Define las acciones de navegación desde Registro
                onNavigateBack = {
                    navController.popBackStack() // Vuelve a la pantalla anterior (Login)
                },
                onRegistrationSuccess = {
                    // Navega al grafo principal (igual que en login)
                    navController.navigate(Graph.MAIN) {
                        popUpTo(Graph.AUTHENTICATION) { inclusive = true }
                    }
                }
            )
        }

        // --- Pantalla de Recuperar Contraseña ---
        composable(route = Screen.ForgotPassword.route) {
            // Llama al Composable placeholder de Recuperar Contraseña
            ForgotPasswordScreen(
                // Define las acciones de navegación desde Recuperar Contraseña
                onNavigateBack = {
                    navController.popBackStack() // Vuelve a la pantalla anterior (Login)
                }
            )
        }
    }
}
