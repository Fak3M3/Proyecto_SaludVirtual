package com.example.tibibalance.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi // Necesario para Pager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager // Importa el Pager
import androidx.compose.foundation.pager.rememberPagerState // Importa el estado del Pager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.tibibalance.ui.navigation.BottomNavigationBar // Reutilizamos el Composable de la barra
import com.example.tibibalance.ui.navigation.Screen
import com.example.tibibalance.ui.navigation.mainPagerScreens // La lista actualizada
// Importa tus placeholders (asegúrate que los paquetes sean correctos)
import com.example.tibibalance.ui.feature_placeholder1.PlaceholderScreen1 as Placeholder1Screen
import com.example.tibibalance.ui.feature_placeholder2.PlaceholderScreen2 as Placeholder2Screen
import com.example.tibibalance.ui.feature_placeholder3.PlaceholderScreen3 as Placeholder3Screen
import com.example.tibibalance.ui.feature_placeholder4.PlaceholderScreen4 as Placeholder4Screen
import com.example.tibibalance.ui.feature_placeholder5.PlaceholderScreen5 as Placeholder5Screen
import kotlinx.coroutines.launch // Para lanzar la animación del Pager

/**
 * Scaffold principal que utiliza HorizontalPager para la navegación
 * principal con animación de deslizamiento y sincronizado con BottomNavigationBar.
 *
 * @param rootNavController El NavController raíz (puede ser necesario para navegar a detalles fuera del Pager).
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainAppScaffoldPager(
    rootNavController: NavHostController // Podría necesitarse para navegar a pantallas de detalle
) {
    // Estado para el HorizontalPager
    val pagerState = rememberPagerState(pageCount = { mainPagerScreens.size })
    // Coroutine scope para lanzar animaciones del pager
    val scope = rememberCoroutineScope()

    // Deriva el título actual basado en la página del Pager
    val currentScreen = mainPagerScreens[pagerState.currentPage]
    val currentTitle = stringResource(id = currentScreen.titleResId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentTitle) }, // Título dinámico
                // Puedes añadir acciones o un botón de menú aquí si es necesario
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            // Barra inferior para interactuar con el PagerState
            NavigationBar {
                mainPagerScreens.forEachIndexed { index, screen ->
                    val isSelected = pagerState.currentPage == index
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = stringResource(screen.titleResId)) },
                        label = { Text(stringResource(screen.titleResId)) },
                        selected = isSelected,
                        onClick = {
                            // Al hacer clic, anima el Pager a la página correspondiente
                            if (!isSelected) { // Evita animar a la página actual
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // HorizontalPager ocupa el área de contenido del Scaffold
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // Aplica el padding del Scaffold
            // key = { index -> mainPagerScreens[index].route } // Clave opcional para estabilidad
        ) { pageIndex ->
            // Muestra el Composable placeholder correspondiente a la página actual
            val screen = mainPagerScreens[pageIndex]
            when (screen) {
                is Screen.MainScreen.Placeholder1 -> Placeholder1Screen(screenName = stringResource(id = screen.titleResId))
                is Screen.MainScreen.Placeholder2 -> Placeholder2Screen(screenName = stringResource(id = screen.titleResId))
                is Screen.MainScreen.Placeholder3 -> Placeholder3Screen(screenName = stringResource(id = screen.titleResId))
                is Screen.MainScreen.Placeholder4 -> Placeholder4Screen(screenName = stringResource(id = screen.titleResId))
                is Screen.MainScreen.Placeholder5 -> Placeholder5Screen(screenName = stringResource(id = screen.titleResId))
            }
            // NOTA para los de frontend: Si estas pantallas necesitan navegar a pantallas de detalle
            // (que NO están en el Pager), necesitarás pasarles el 'rootNavController'
            // y ellas llamarán a rootNavController.navigate(...)
        }
    }
}
