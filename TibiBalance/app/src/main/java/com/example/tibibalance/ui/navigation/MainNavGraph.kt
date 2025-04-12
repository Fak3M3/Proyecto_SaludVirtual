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
    }
}
