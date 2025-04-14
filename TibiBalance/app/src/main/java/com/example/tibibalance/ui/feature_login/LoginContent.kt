package com.example.tibibalance.ui.feature_login // Asegúrate que el package sea el correcto

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility // Icono correcto
import androidx.compose.material.icons.filled.VisibilityOff // Icono correcto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale // Importa si es necesario para la imagen
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tibibalance.R // Asegúrate que la ruta a R sea la correcta
// Importa tus componentes y modelos reales
// import com.example.tibibalance.ui.components.LoadingIndicator
// import com.example.tibibalance.ui.feature_login.LoginEvent
// import com.example.tibibalance.ui.feature_login.LoginUiState

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator() // Implementación básica
}
// --- Fin de Dummy implementations ---


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)
@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    uiState: LoginUiState,
    onEvent: (LoginEvent) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Recursos de imagen como en LoginScreen
    val image: Painter = painterResource(id = R.drawable.tibibalance_image2) // Asegúrate que el ID es correcto
    val googleLogo: Painter = painterResource(id = R.drawable.google) // Asegúrate que el ID es correcto

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White) // Fondo blanco general
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 16.dp), // Padding inferior general
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 2. Imagen superior como en LoginScreen
            Image(
                painter = image,
                contentDescription = "Imagen de bienvenida",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // Altura de LoginScreen
                    .background(Color(0xFFD5EDFD)) // Fondo imagen
                    .padding(bottom = 0.dp), // Sin padding si el formulario va justo debajo
                contentScale = ContentScale.Crop // O el que prefieras
            )

            // Omitir icono de flecha atrás

            Spacer(modifier = Modifier.height(24.dp)) // Espacio entre imagen y formulario

            // 3. Contenedor del formulario estilizado
            Column(
                modifier = Modifier
                    .width(300.dp) // Ancho fijo como en LoginScreen
                    .background(Color(0xFFD5EDFD), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 3.1 Campo de Email (funcional + estilo)
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { onEvent(LoginEvent.EmailChanged(it)) },
                    // *** NO añadir altura fija ***
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre de usuario o correo*") }, // Texto de LoginScreen
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    isError = uiState.emailError != null,
                    supportingText = {
                        uiState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    shape = RoundedCornerShape(8.dp), // Estilo LoginScreen
                    colors = OutlinedTextFieldDefaults.colors( // Estilo LoginScreen
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(16.dp)) // Espacio entre campos

                // 3.2 Campo de Contraseña (funcional + estilo)
                var passwordVisible by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                    // *** NO añadir altura fija ***
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contraseña*") }, // Texto de LoginScreen
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            onEvent(LoginEvent.LoginClicked) // Disparar evento al pulsar Done
                        }
                    ),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector, contentDescription = description)
                        }
                    },
                    isError = uiState.passwordError != null,
                    supportingText = {
                        uiState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    shape = RoundedCornerShape(8.dp), // Estilo LoginScreen
                    colors = OutlinedTextFieldDefaults.colors( // Estilo LoginScreen
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )
            } // Fin del Column del formulario

            Spacer(modifier = Modifier.height(24.dp)) // Espacio antes del botón Login

            // 4. Botón de Login estilizado como en LoginScreen
            Button(
                onClick = {
                    keyboardController?.hide()
                    onEvent(LoginEvent.LoginClicked)
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    // .width(300.dp) // Opcional: mismo ancho que form
                    .height(60.dp) // Altura ajustada (80dp era mucho)
                    .shadow(4.dp, RoundedCornerShape(16.dp)), // Sombra
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD5EDFD)), // Color de fondo
                shape = RoundedCornerShape(16.dp) // Bordes redondeados
            ) {
                Text(
                    text = "Iniciar sesión",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = TextStyle(fontSize = 20.sp) // Estilo de texto
                )
            }
            Spacer(modifier = Modifier.height(24.dp)) // Espacio después del botón Login

            // 5. Separador "o" estilizado como en LoginScreen
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(300.dp) // Mismo ancho que el form
                    .padding(horizontal = 16.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color.Gray,
                    thickness = 1.dp
                )
                Text(
                    text = "o",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = Color.Gray,
                    thickness = 1.dp
                )
            }
            Spacer(modifier = Modifier.height(24.dp)) // Espacio después del separador

            // 6. Botón de Google Sign-In estilizado como en LoginScreen
            Button(
                onClick = { onEvent(LoginEvent.GoogleSignInClicked) },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    // .width(300.dp) // Opcional: mismo ancho que form
                    .height(56.dp) // Altura estándar
                    .shadow(4.dp, RoundedCornerShape(16.dp)), // Sombra
                colors = ButtonDefaults.buttonColors(containerColor = Color.White), // Fondo blanco
                shape = RoundedCornerShape(16.dp) // Bordes redondeados
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = googleLogo,
                        contentDescription = "Logo de Google",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Continuar con Google",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        style = TextStyle(fontSize = 18.sp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp)) // Espacio antes de los enlaces inferiores

            // 7. Enlace Olvidé Contraseña estilizado y posicionado como en LoginScreen (al final)
            TextButton( // Se mantiene TextButton por funcionalidad onClick
                onClick = { onForgotPasswordClick() },
                contentPadding = PaddingValues(vertical = 4.dp) // Padding reducido
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña? Clic aquí", // Texto de LoginScreen
                    style = TextStyle(color = Color.Black, fontSize = 14.sp) // Estilo de LoginScreen
                )
            }
            // Spacer(modifier = Modifier.height(8.dp)) // Espacio entre enlaces (opcional)

            // 8. Enlace a Registro (funcionalidad original, estilo adaptado)
            // Colocado debajo de "Olvidé contraseña" para agrupar enlaces al final
            Row(
                modifier = Modifier.fillMaxWidth(), // Centrado horizontalmente
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Aún no tienes cuenta?",
                    style = TextStyle(color = Color.Gray, fontSize = 14.sp) // Estilo sutil
                )
                TextButton(onClick = { onRegisterClick() }) {
                    Text(
                        text = "Crea una",
                        fontWeight = FontWeight.Bold, // Destacar la acción
                        color = MaterialTheme.colorScheme.primary // Usar color primario
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp)) // Espacio final

        } // Fin de Column principal

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
    } // Fin de Box principal
}
