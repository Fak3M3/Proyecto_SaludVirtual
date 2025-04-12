package com.example.tibibalance.ui.main // O donde prefieras colocar este Composable principal

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box // Importa Box si quieres mostrar un texto placeholder
import androidx.compose.foundation.layout.fillMaxSize // Para Box
import androidx.compose.foundation.layout.padding // Necesario para usar innerPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text // Placeholder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment // Para Box
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType // Para argumentos
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument // Para argumentos
import com.example.tibibalance.ui.navigation.BottomNavigationBar
import com.example.tibibalance.ui.navigation.Screen
import com.example.tibibalance.ui.navigation.bottomNavScreens

// --- Importaciones de Pantallas (Puedes mantenerlas comentadas o quitarlas por ahora) ---
// import com.example.tibibalance.ui.dashboard.DashboardScreen
// import com.example.tibibalance.ui.habits.HabitsScreen
// import com.example.tibibalance.ui.profile.ProfileScreen
// import com.example.tibibalance.ui.feature_addhabit.AddHabitScreen
// import com.example.tibibalance.ui.settings.SettingsScreen
// import com.example.tibibalance.ui.habit_detail.HabitDetailScreen
// --- Fin Importaciones ---

/**
 * Composable que define la estructura principal de la aplicación post-autenticación.
 * Incluye el Scaffold con la barra de navegación inferior y el NavHost interno.
 * ¡MODIFICADO TEMPORALMENTE para evitar errores de compilación!
 *
 * @param rootNavController El NavController principal (del grafo raíz).
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainAppScaffold(
    rootNavController: NavHostController
) {
    Scaffold(
        bottomBar = {
            // La barra inferior se mostrará, aunque las pantallas no existan aún
            BottomNavigationBar(navController = rootNavController, items = bottomNavScreens)
        }
        // topBar = { MainTopAppBar(...) }
    ) { innerPadding ->

        // NavHost INTERNO: Gestiona las pantallas DENTRO de la sección principal.
        NavHost(
            navController = rootNavController,
            // Mantenemos una ruta inicial válida, aunque su composable esté comentado
            startDestination = Screen.BottomNavScreen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
            // route = Graph.MAIN_CONTENT // Opcional
        ) {
            // --- ¡BLOQUES COMPOSABLE COMENTADOS TEMPORALMENTE! ---
            // Descomenta y completa cada bloque cuando implementes la pantalla correspondiente.

            composable(route = Screen.BottomNavScreen.Dashboard.route) {
                // DashboardScreen(...)
                // Placeholder temporal para que se vea algo:
                Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center){ Text("Dashboard (Pendiente)") }
            }
            composable(route = Screen.BottomNavScreen.Habits.route) {
                // HabitsScreen(...)
                Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center){ Text("Hábitos (Pendiente)") }
            }
            composable(route = Screen.BottomNavScreen.Profile.route) {
                // ProfileScreen(...)
                Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center){ Text("Perfil (Pendiente)") }
            }
            composable(route = Screen.AddHabit.route) {
                // AddHabitScreen(...)
                Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center){ Text("Añadir Hábito (Pendiente)") }
            }
            composable(route = Screen.Settings.route) {
                // SettingsScreen(...)
                Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center){ Text("Ajustes (Pendiente)") }
            }
            composable(
                route = Screen.HabitDetail.route,
                arguments = listOf(navArgument("habitId") { type = NavType.StringType })
            ) { backStackEntry ->
                val habitId = backStackEntry.arguments?.getString("habitId") ?: "ID_INVALIDO"
                // HabitDetailScreen(habitId = habitId, ...)
                Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center){ Text("Detalle Hábito ID: $habitId (Pendiente)") }
            }

            // --- Fin Bloques Comentados ---
        }
    }
}
/*

**Cambios Realizados:**

1.  **Comentados los `composable` internos:** Las llamadas a `DashboardScreen`, `HabitsScreen`, etc., dentro del `NavHost` interno han sido comentadas o reemplazadas por un simple `Text` dentro de un `Box`.
2.  **Importaciones (Opcional):** Puedes mantener las importaciones comentadas o eliminarlas por ahora, ya que las funciones no se están llamando.
3.  **Placeholders Visuales:** Añadí un `Box` con un `Text` dentro de cada `composable` comentado para que, si navegas a estas rutas, veas un indicador visual de que llegaste ahí, en lugar de una pantalla completamente en blanco.

**Resultado:**

Con esta modificación, el archivo `MainAppScaffold.kt` ya no debería darte errores de compilación por referencias no resueltas. Podrás:

1.  Ejecutar la aplicación.
2.  Ver la pantalla de Login (placeholder o real).
3.  Navegar a Registro y Olvidé Contraseña (placeholders).
4.  Probar la acción "Simular Login Exitoso" o "Simular Registro Exitoso". Esto ejecutará `navController.navigate(Graph.MAIN)` y te llevará a la estructura definida por `MainAppScaffold`. Verás el `Scaffold` con la barra inferior y el texto placeholder correspondiente a la `startDestination` del `NavHost` interno (en este caso, "Dashboard (Pendiente)").

Cuando estés listo para implementar las pantallas principales, simplemente:

1.  Crea los archivos y Composables reales (`DashboardScreen.kt`, etc.).
2.  Vuelve a `MainAppScaffold.kt`.
3.  Descomenta/añade las importaciones correctas.
4.  Descomenta el bloque `composable` correspondiente y reemplaza el `Box` con `Text` por la llamada a tu Composable real (e.g., `DashboardScreen(...)*/