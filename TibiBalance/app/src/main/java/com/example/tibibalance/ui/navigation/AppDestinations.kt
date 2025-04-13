package com.example.tibibalance.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Importa los iconos específicos que necesites
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.annotation.StringRes
import com.example.tibibalance.R // Asegúrate de importar tu clase R

// Objeto para agrupar las rutas de los grafos (sin cambios)
object Graph {
    const val ROOT = "root_graph"
    const val AUTHENTICATION = "auth_graph"
    const val MAIN = "main_graph"
}

// Clase sellada para todas las posibles pantallas/destinos
sealed class Screen(val route: String) {

    // --- Destinos del Grafo de Autenticación (Añadido VerifyEmail) ---
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object VerifyEmail : Screen("verify_email") // <-- NUEVA RUTA AÑADIDA

    // --- Destinos Principales (Sin cambios) ---
    sealed class MainScreen(
        val pageIndex: Int,
        route: String,
        @StringRes val titleResId: Int,
        val icon: ImageVector
    ) : Screen(route) {
        object Placeholder1 : MainScreen(0, "placeholder1", R.string.screen_title_placeholder1, Icons.Filled.Home)
        object Placeholder2 : MainScreen(1, "placeholder2", R.string.screen_title_placeholder2, Icons.Filled.List)
        object Placeholder3 : MainScreen(2, "placeholder3", R.string.screen_title_placeholder3, Icons.Filled.Person)
        object Placeholder4 : MainScreen(3, "placeholder4", R.string.screen_title_placeholder4, Icons.Filled.PlayArrow)
        object Placeholder5 : MainScreen(4, "placeholder5", R.string.screen_title_placeholder5, Icons.Filled.Settings)
    }

    // --- Destinos de Detalle/Secundarios (Sin cambios) ---
    object HabitDetail : Screen("habit_detail/{habitId}") {
        fun createRoute(habitId: String) = "habit_detail/$habitId"
    }
    object AddHabit : Screen("add_habit")
    object Settings : Screen("settings")

}

// Lista para la barra inferior y el Pager (Sin cambios)
val mainPagerScreens = listOf(
    Screen.MainScreen.Placeholder1,
    Screen.MainScreen.Placeholder2,
    Screen.MainScreen.Placeholder3,
    Screen.MainScreen.Placeholder4,
    Screen.MainScreen.Placeholder5,
)
