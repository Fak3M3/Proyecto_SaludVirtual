package com.example.tibibalance.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tibibalance.ui.theme.TibiBalanceTheme // Asegúrate de importar tu tema

/**
 * Un botón reutilizable con estilo personalizado para la aplicación.
 *
 * @param text El texto a mostrar en el botón.
 * @param onClick La acción a ejecutar cuando se hace clic en el botón.
 * @param modifier Modificador para personalizar el layout.
 * @param enabled Controla si el botón está habilitado o deshabilitado.
 * @param containerColor Color de fondo del botón (opcional, usa el primario por defecto).
 */
@Composable
fun StyledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primary
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth() // Ocupa el ancho disponible por defecto
            .height(50.dp), // Altura estándar
        shape = MaterialTheme.shapes.medium, // Esquinas redondeadas
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onPrimary, // Color del texto sobre el primario
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), // Color cuando está deshabilitado
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f) // Color del texto deshabilitado
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge) // Usa un estilo de texto definido en el tema
    }
}

// Preview para ver cómo se ve el botón en Android Studio
@Preview(showBackground = true)
@Composable
fun StyledButtonPreview() {
    TibiBalanceTheme {
        StyledButton(text = "Botón de Ejemplo", onClick = {}, modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun StyledButtonDisabledPreview() {
    TibiBalanceTheme {
        StyledButton(text = "Botón Deshabilitado", onClick = {}, enabled = false, modifier = Modifier.padding(16.dp))
    }
}
