package com.example.tibibalance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview // Para Previews
import androidx.navigation.NavHostController // Importante
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.tibibalance.ui.navigation.Graph // Importa las rutas de Grafos
import com.example.tibibalance.ui.navigation.authGraph // Importa la función del grafo de Auth
import com.example.tibibalance.ui.navigation.mainGraph // Importa la función del grafo Principal
import com.example.tibibalance.ui.theme.TibiBalanceTheme
import dagger.hilt.android.AndroidEntryPoint

// import dagger.hilt.android.AndroidEntryPoint // Si usas Hilt

/**
 * Actividad principal de la aplicación.
 * Configura el tema y el grafo de navegación raíz.
 */
@AndroidEntryPoint // Descomenta si usas Hilt
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TibiBalanceTheme {
                // Llama al Composable que contiene el NavHost raíz
                RootNavigationGraphSimplified()
            }
        }
    }
}

/**
 * Composable que configura el NavHost raíz de la aplicación.
 * En esta versión simplificada, SIEMPRE inicia en el grafo de autenticación.
 */
@Composable
fun RootNavigationGraphSimplified() {
    // Crea y recuerda el NavController principal
    val navController: NavHostController = rememberNavController()

    // Configura el NavHost raíz
    NavHost(
        navController = navController,
        route = Graph.ROOT, // Identificador para este NavHost raíz
        // ¡IMPORTANTE! Fija el inicio directamente en el grafo de autenticación.
        startDestination = Graph.AUTHENTICATION
    ) {
        // Define los grafos anidados. Aunque empecemos en Auth,
        // necesitamos definir mainGraph para poder navegar hacia él más tarde.
        authGraph(navController = navController) // Define las pantallas de Login, Registro...
        mainGraph(navController = navController) // Define la estructura principal (Scaffold, BottomBar...)
    }
}

// Preview simple para esta versión simplificada
@Preview(showBackground = true)
@Composable
fun DefaultPreviewSimplified() {
    TibiBalanceTheme {
        RootNavigationGraphSimplified() // Mostrará lo que esté definido como inicio en authGraph (LoginScreen)
    }
}

