package com.example.tibibalance.ui.feature_placeholder2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tibibalance.ui.theme.TibiBalanceTheme

/**
 * Placeholder para una pantalla principal.
 * @param screenName El nombre de esta pantalla para mostrar.
 * @param modifier Modificador opcional.
 */
@Composable
fun PlaceholderScreen2(
    screenName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = screenName,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = "(Contenido de la pantalla pendiente)",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

// Preview para esta pantalla placeholder
@Preview(showBackground = true)
@Composable
private fun PlaceholderScreenPreview() {
    TibiBalanceTheme {
        PlaceholderScreen2(screenName = "Placeholder 2")
    }
}

