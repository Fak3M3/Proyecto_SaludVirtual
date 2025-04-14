package com.example.tibibalance.ui.feature_register // Asegúrate que el package sea el correcto

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tibibalance.R // Asegúrate que la ruta a R sea la correcta
// Importa tus componentes y modelos reales
// import com.example.tibibalance.ui.components.LoadingIndicator
// import com.example.tibibalance.ui.feature_register.RegisterEvent
// import com.example.tibibalance.ui.feature_register.RegisterUiState

@Composable
fun LoadingIndicator() {
    CircularProgressIndicator() // Implementación básica
}
// --- Fin de Dummy implementations ---


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)
@Composable
fun RegisterContent(
    modifier: Modifier = Modifier,
    uiState: RegisterUiState,
    onEvent: (RegisterEvent) -> Unit
    // Si necesitas la navegación hacia atrás, deberías añadir un parámetro:
    // onNavigateBack: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Recursos de imagen como en RegisterScreen
    val image: Painter = painterResource(id = R.drawable.tibibalance_image3) // Asegúrate que el ID es correcto
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
            Image(
                painter = image,
                contentDescription = "Imagen de registro",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFD5EDFD)) // Fondo imagen
                    .padding(bottom = 0.dp), // Sin padding si el formulario va justo debajo
                contentScale = ContentScale.Crop // O el que prefieras
            )

            Spacer(modifier = Modifier.height(24.dp)) // Espacio entre imagen y formulario

            // 3. Contenedor del formulario estilizado
            Column(
                modifier = Modifier
                    .width(300.dp) // Ancho fijo como en RegisterScreen
                    .background(Color(0xFFD5EDFD), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campos del formulario (funcionales + estilo)
                // Usamos los OutlinedTextField funcionales y les aplicamos el estilo

                // 3.1 Nombre de Usuario
                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = { onEvent(RegisterEvent.UsernameChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre de usuario*") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    singleLine = true,
                    isError = uiState.usernameError != null,
                    supportingText = { uiState.usernameError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp)) // Espacio reducido entre campos

                // 3.2 Fecha de Nacimiento
                OutlinedTextField(
                    value = uiState.dob,
                    onValueChange = { onEvent(RegisterEvent.DobChanged(it)) },
                    // *** CORRECCIÓN: Eliminado Modifier.height(56.dp) ***
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Fecha de Nacimiento*") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri, // Manteniendo tipo original
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    singleLine = true,
                    isError = uiState.dobError != null,
                    supportingText = { uiState.dobError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 3.3 Correo
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = { onEvent(RegisterEvent.EmailChanged(it)) },
                    // *** CORRECCIÓN: Eliminado Modifier.height(56.dp) ***
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Correo*") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    singleLine = true,
                    isError = uiState.emailError != null,
                    supportingText = { uiState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 3.4 Contraseña
                var passwordVisible by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { onEvent(RegisterEvent.PasswordChanged(it)) },
                    // *** CORRECCIÓN: Eliminado Modifier.height(56.dp) ***
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Contraseña*") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, contentDescription = if (passwordVisible) "Ocultar" else "Mostrar")
                        }
                    },
                    isError = uiState.passwordError != null,
                    supportingText = { uiState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // 3.5 Confirmar Contraseña
                var confirmPasswordVisible by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = uiState.confirmPassword,
                    onValueChange = { onEvent(RegisterEvent.ConfirmPasswordChanged(it)) },
                    // *** CORRECCIÓN: Eliminado Modifier.height(56.dp) ***
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Confirmar contraseña*") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        onEvent(RegisterEvent.RegisterClicked) // Disparar evento al pulsar Done
                    }),
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(image, contentDescription = if (confirmPasswordVisible) "Ocultar" else "Mostrar")
                        }
                    },
                    isError = uiState.confirmPasswordError != null,
                    supportingText = { uiState.confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Gray
                    )
                )
            } // Fin del Column del formulario

            Spacer(modifier = Modifier.height(24.dp)) // Espacio antes del botón Registrarse

            // 4. Botón de Registrarse estilizado como en RegisterScreen
            Button(
                onClick = {
                    keyboardController?.hide()
                    onEvent(RegisterEvent.RegisterClicked)
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .height(60.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD5EDFD)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Registrarse",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    style = TextStyle(fontSize = 20.sp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp)) // Espacio después del botón Registrarse

            // 5. Separador "o" estilizado como en RegisterScreen
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(300.dp)
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

            // 6. Botón de Google Sign-In estilizado como en RegisterScreen
            Button(
                onClick = { onEvent(RegisterEvent.GoogleSignInClicked) },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .height(56.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
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

@Preview(showBackground = true)
@Composable
fun RegisterContentPreview() {
    // Estado dummy para la preview
    val dummyState = RegisterUiState(
        username = "",
        usernameError = null,
        dob = "",
        dobError = null,
        email = "",
        emailError = null,
        password = "",
        passwordError = null,
        confirmPassword = "",
        confirmPasswordError = null,
        isLoading = false
    )

    RegisterContent(
        uiState = dummyState,
        onEvent = {} // Acción vacía para la preview
    )
}
