package com.example.tibibalance.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar // Material 3
import androidx.compose.material3.NavigationBarItem
// import androidx.compose.material3.NavigationBarItemDefaults // Para colores (opcional)
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
// import androidx.compose.runtime.getValue // Ya no se necesita currentBackStackEntryAsState
import androidx.compose.ui.Modifier
// import androidx.compose.ui.graphics.Color // Para colores (opcional)
import androidx.compose.ui.res.stringResource
// Ya no necesita NavController directamente para esta lógica
// import androidx.navigation.NavController
// import androidx.navigation.NavDestination.Companion.hierarchy
// import androidx.navigation.NavGraph.Companion.findStartDestination
// import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Composable reutilizable que muestra la barra de navegación inferior,
 * adaptado para funcionar con un HorizontalPager.
 *
 * @param items La lista de objetos [Screen.MainScreen] a mostrar.
 * @param currentPageIndex El índice de la página actualmente visible en el Pager.
 * @param onItemClick Lambda que se ejecuta cuando se hace clic en un ítem,
 * pasando el índice de la página a la que se debe ir.
 * @param modifier Modificador opcional.
 */
@Composable
fun BottomNavigationBar(
    items: List<Screen.MainScreen>, // <--- CAMBIO: Ahora usa Screen.MainScreen
    currentPageIndex: Int,         // <--- NUEVO: Índice de la página actual del Pager
    onItemClick: (pageIndex: Int) -> Unit, // <--- NUEVO: Lambda para cambiar de página
    modifier: Modifier = Modifier
    // Ya no necesita NavController aquí
) {
    NavigationBar(
        modifier = modifier
    ) {
        // Itera sobre cada pantalla definida para la barra/pager
        items.forEach { screen -> // 'screen' ahora es de tipo Screen.MainScreen

            // Determina si este ítem está seleccionado comparando índices
            val isSelected = screen.pageIndex == currentPageIndex // <--- CAMBIO: Lógica de selección basada en índice

            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = stringResource(screen.titleResId)) },
                label = { Text(stringResource(screen.titleResId)) },
                selected = isSelected,
                onClick = {
                    // Llama a la lambda pasada para manejar el clic,
                    // indicando el índice de la página a la que ir.
                    if (!isSelected) { // Evita acción si ya está seleccionado
                        onItemClick(screen.pageIndex) // <--- CAMBIO: Llama a onItemClick
                    }
                }
                // Opcional: Personalizar colores
                // colors = NavigationBarItemDefaults.colors(...)
            )
        }
    }
}
