package com.example.tibibalance.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tibibalance.ui.theme.TibiBalanceTheme // Asegúrate de importar tu tema

/**
 * Un Composable simple que muestra un indicador de progreso circular.
 * Ideal para mostrar mientras se cargan datos.
 *
 * @param modifier Modificador opcional para personalizar el layout, tamaño, etc.
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    // Simplemente muestra el indicador de progreso circular de Material 3.
    // El llamador puede usar el modifier para posicionarlo, por ejemplo:
    // LoadingIndicator(modifier = Modifier.align(Alignment.Center))
    CircularProgressIndicator(
        modifier = modifier,
        // Puedes personalizar el color si lo deseas:
        // color = MaterialTheme.colorScheme.primary,
        // strokeWidth = 4.dp // Grosor de la línea
    )
}

// Preview para visualizar el componente en Android Studio
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF) // Fondo blanco para ver mejor
@Composable
fun LoadingIndicatorPreview() {
    TibiBalanceTheme {
        // Usamos un Box en el preview para centrarlo y darle tamaño,
        // simulando cómo podría usarse.
        Box(
            modifier = Modifier.size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator()
        }
    }
}
