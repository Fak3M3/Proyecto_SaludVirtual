package com.example.tibibalance.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Importa los iconos específicos que necesites
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.annotation.StringRes
import com.example.tibibalance.R // Asegúrate que tu paquete sea correcto y tengas R importable

/**
 * Objeto que contiene las constantes para las rutas de los grafos de navegación.
 * Facilita la referencia a los grafos anidados.
 */
object Graph {
    const val ROOT = "root_graph"          // Grafo raíz que decide entre Auth y Main
    const val AUTHENTICATION = "auth_graph" // Grafo para Login, Registro, etc.
    const val MAIN = "main_graph"          // Grafo para la app principal (post-login)
}

/**
 * Clase sellada que representa todos los posibles destinos de navegación en la app.
 *
 * @param route La cadena de ruta única para este destino.
 */
sealed class Screen(val route: String) {

    // --- Destinos del Grafo de Autenticación ---
    // No necesitan título o icono aquí si no se muestran en barras.
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password") // Ejemplo

    // --- Destinos Principales (con Barra Inferior) ---
    // Subclase sellada específica para las pantallas de la barra inferior.
    sealed class BottomNavScreen(
        route: String,
        @StringRes val titleResId: Int, // ID del recurso de String para el título/label
        val icon: ImageVector          // Icono a mostrar en la barra
    ) : Screen(route) {
        // Define cada pantalla de la barra inferior como un objeto.
        // Asegúrate de tener los R.string correspondientes en res/values/strings.xml
        // y los iconos adecuados.
        object Dashboard : BottomNavScreen(
            route = "dashboard",
            titleResId = R.string.screen_title_dashboard, // Ejemplo: "Inicio"
            icon = Icons.Filled.Settings // O Icons.Default.Home
        )
        object Habits : BottomNavScreen(
            route = "habits",
            titleResId = R.string.screen_title_habits, // Ejemplo: "Hábitos"
            icon = Icons.Filled.Star // O Addchart, etc.
        )
        object Profile : BottomNavScreen(
            route = "profile",
            titleResId = R.string.screen_title_profile, // Ejemplo: "Perfil"
            icon = Icons.Filled.Person
        )
        // Añade más si es necesario...
    }

    // --- Destinos de Detalle/Secundarios ---
    // Pantallas a las que se navega desde las pantallas principales.
    // Ejemplo: Pantalla de detalle de un hábito, que necesita un ID.
    object HabitDetail : Screen("habit_detail/{habitId}") {
        // Función helper para construir la ruta completa con el argumento real.
        fun createRoute(habitId: String) = "habit_detail/$habitId"
    }
    // Pantalla para añadir hábito (ya la tenías definida en tu estructura).
    object AddHabit : Screen("add_habit")

    // Ejemplo de otra pantalla de detalle.
    object Settings : Screen("settings")

    // Añade otras pantallas de detalle según necesites...
}

/**
 * Lista predefinida de las pantallas que aparecerán en la barra inferior.
 * Útil para construir la UI de la barra de navegación.
 */
val bottomNavScreens = listOf(
    Screen.BottomNavScreen.Dashboard,
    Screen.BottomNavScreen.Habits,
    Screen.BottomNavScreen.Profile,
    // Añade los mismos objetos que definiste arriba
)

