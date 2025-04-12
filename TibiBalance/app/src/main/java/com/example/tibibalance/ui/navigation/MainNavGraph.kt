package com.example.tibibalance.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.tibibalance.ui.main.MainAppScaffold // Importa el Scaffold principal

/**
 * Define el grafo de navegación anidado para la sección principal de la app (post-autenticación).
 * Su función principal es configurar el entorno (Scaffold con BottomBar) donde se
 * mostrarán las pantallas principales y de detalle.
 *
 * @param navController El NavController raíz.
 */
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    // Define el grafo anidado con su ruta (Graph.MAIN)
    navigation(
        route = Graph.MAIN,
        // Define una ruta interna simple como punto de entrada a este grafo.
        // Podría ser cualquier string, representa el contenido principal.
        startDestination = "main_content_route"
    ) {
        // La única entrada directa en este grafo es el Composable que
        // configura el Scaffold y el NavHost interno.
        composable(route = "main_content_route") {
            // Llama al Composable que contiene el Scaffold y el NavHost interno.
            // Pasa el NavController raíz, ya que el NavHost interno también lo usará.
            MainAppScaffold(rootNavController = navController)
        }
        // NOTA IMPORTANTE: Las definiciones de las pantallas individuales
        // (DashboardScreen, HabitsScreen, AddHabitScreen, SettingsScreen, etc.)
        // NO van aquí directamente. Se definen DENTRO del NavHost que está
        // en MainAppScaffold.kt. Este grafo solo se encarga de "lanzar" ese Scaffold.
    }
}
