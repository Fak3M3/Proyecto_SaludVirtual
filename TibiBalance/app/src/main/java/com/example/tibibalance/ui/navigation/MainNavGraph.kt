package com.example.tibibalance.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.tibibalance.ui.main.MainAppScaffoldPager // Importa el nuevo Scaffold con Pager

/**
 * Define el grafo de navegación anidado para la sección principal de la app (post-autenticación).
 * Ahora carga el MainAppScaffoldPager que contiene el HorizontalPager.
 *
 * @param navController El NavController raíz.
 */
fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation(
        route = Graph.MAIN,
        startDestination = "main_pager_content" // Ruta interna para el contenido principal
    ) {
        composable(route = "main_pager_content") {
            // Llama al Composable que contiene el Scaffold y el HorizontalPager
            MainAppScaffoldPager(rootNavController = navController)
        }
        // --- Navegación a Detalles (Fuera del Pager) ---
        // Si necesitas navegar a pantallas de detalle DESDE las pantallas del Pager,
        // defines esas rutas aquí, al mismo nivel que "main_pager_content".
        // El rootNavController se usaría para navegar a ellas.
        // Ejemplo:
        /*
        composable(route = Screen.Settings.route) {
             SettingsScreen(onNavigateBack = { navController.navigateUp() })
        }
        composable(
             route = Screen.HabitDetail.route,
             arguments = listOf(navArgument("habitId") { type = NavType.StringType })
        ) { backStackEntry ->
             val habitId = backStackEntry.arguments?.getString("habitId") ?: "ID_INVALIDO"
             HabitDetailScreen(habitId = habitId, onNavigateBack = { navController.navigateUp() })
        }
        */
        // --- Fin Navegación a Detalles ---
    }
}
