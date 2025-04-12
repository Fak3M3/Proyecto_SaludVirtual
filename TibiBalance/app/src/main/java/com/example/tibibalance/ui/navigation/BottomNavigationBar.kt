package com.example.tibibalance.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar // Material 3
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults // Para colores personalizados (opcional)
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Para colores personalizados (opcional)
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Composable reutilizable que muestra la barra de navegación inferior.
 *
 * @param navController El NavController que gestiona la navegación principal.
 * @param items La lista de objetos [Screen.BottomNavScreen] a mostrar en la barra.
 * @param modifier Modificador opcional para aplicar a la barra.
 */
@Composable
fun BottomNavigationBar(
    navController: NavController,
    items: List<Screen.BottomNavScreen>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
        // Puedes añadir modificadores como background color aquí si quieres:
        // .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Obtiene la entrada actual de la pila de navegación para saber qué ruta está activa
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        // Itera sobre cada pantalla definida para la barra inferior
        items.forEach { screen ->
            // Determina si esta pantalla está seleccionada actualmente,
            // comprobando si su ruta está en la jerarquía de la ruta activa.
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            // Crea un item en la barra de navegación para esta pantalla
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = stringResource(screen.titleResId)) },
                label = { Text(stringResource(screen.titleResId)) },
                selected = isSelected,
                onClick = {
                    // Navega a la ruta de la pantalla seleccionada
                    navController.navigate(screen.route) {
                        // Pop up hasta el destino inicial del grafo para evitar acumular
                        // pantallas en la pila al seleccionar items repetidamente.
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true // Guarda el estado de la pantalla de origen
                        }
                        // Evita lanzar múltiples copias del mismo destino si se re-selecciona
                        launchSingleTop = true
                        // Restaura el estado si se vuelve a una pantalla previamente seleccionada
                        restoreState = true
                    }
                },
                // Opcional: Personalizar colores
                // colors = NavigationBarItemDefaults.colors(
                //     selectedIconColor = MaterialTheme.colorScheme.primary,
                //     unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                //     selectedTextColor = MaterialTheme.colorScheme.primary,
                //     unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                //     indicatorColor = MaterialTheme.colorScheme.surfaceVariant // Color del "círculo" indicador
                // )
            )
        }
    }
}
