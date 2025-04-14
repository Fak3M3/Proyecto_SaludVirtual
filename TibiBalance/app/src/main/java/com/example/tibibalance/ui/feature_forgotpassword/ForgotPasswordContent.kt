package com.example.tibibalance.ui.feature_forgotpassword

import androidx.compose.foundation.Image // Importar Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
// import androidx.compose.material.icons.Icons // Ya no se usa Lock Icon
// import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter // Importar Painter
import androidx.compose.ui.layout.ContentScale // Importar ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource // Importar painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tibibalance.R // Asegúrate que la ruta a R sea la correcta
// Importa tus componentes y modelos reales
// import com.example.tibibalance.ui.components.LoadingIndicator
// import com.example.tibibalance.ui.feature_forgotpassword.ForgotPasswordEvent
// import com.example.tibibalance.ui.feature_forgotpassword.ForgotPasswordUiState

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator() // Implementación básica
}
// --- Fin de Dummy implementations ---


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordContent(
    modifier: Modifier = Modifier,
    uiState: ForgotPasswordUiState,
    onEvent: (ForgotPasswordEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // *** CAMBIO: Cargar recurso de imagen ***
    // Usamos la imagen 2 como placeholder, cámbiala si tienes una específica para esta pantalla
    val image: Painter = painterResource(id = R.drawable.tibibalance_image2)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White) // Fondo blanco general
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                // Quitar padding horizontal aquí si la imagen ocupa todo el ancho
                .padding(vertical = 0.dp, horizontal = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // No hay título superior según solicitud

            // *** CAMBIO: Reemplazar Icono con Imagen ***
            Image(
                painter = image,
                contentDescription = "Recuperar Contraseña",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp) // Altura similar a otras pantallas
                    .background(Color(0xFFD5EDFD)) // Fondo azul claro para la imagen
                    .padding(bottom = 0.dp), // Sin padding inferior si el texto va pegado
                contentScale = ContentScale.Crop // O el que prefieras
            )

            // Spacer(modifier = Modifier.height(24.dp)) // Espacio entre imagen y texto explicativo

            // 2. Texto explicativo (se mantiene, ajustar padding)
            Text(
                text = "Ingresa tu correo electrónico y enviaremos un link para recuperar tu contraseña",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 32.dp) // Padding horizontal para limitar ancho
                    .padding(top = 32.dp, bottom = 32.dp) // Espacio vertical alrededor del texto
            )

            // 3. Contenedor del formulario estilizado (solo email y botón)
            Column(
                modifier = Modifier
                    .width(300.dp) // Ancho fijo como en Login/Register
                    .background(Color(0xFFD5EDFD), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 3.1 Campo de correo electrónico (sin cambios funcionales)
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { onEvent(ForgotPasswordEvent.EmailChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Correo Electrónico") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            onEvent(ForgotPasswordEvent.SendButtonClicked)
                        }
                    ),
                    singleLine = true,
                    isError = uiState.emailError != null,
                    supportingText = {
                        uiState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.height(24.dp)) // Espacio antes del botón

                // 3.2 Botón Enviar (estilo Login/Register con fondo blanco)
                Button(
                    onClick = {
                        keyboardController?.hide()
                        onEvent(ForgotPasswordEvent.SendButtonClicked)
                    },
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        // *** CAMBIO: Añadida sombra de nuevo ***
                        .shadow(4.dp, RoundedCornerShape(16.dp)),
                    // *** CAMBIO: Color de fondo a Blanco ***
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Enviar",
                        fontWeight = FontWeight.Bold,
                        // El color negro funciona bien sobre blanco
                        color = Color.Black,
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
            } // Fin del Column del formulario

            Spacer(modifier = Modifier.height(24.dp)) // Espacio final opcional

        } // Fin Column principal

        // Indicador de carga (se mantiene igual)
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
    } // Fin Box principal
}
