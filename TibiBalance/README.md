# Composables a desarrollar

## 1. General Buttons

Botones estándar para diversas acciones.

### `PrimaryButton`
* **Uso**: Acción principal en una pantalla o diálogo.
* **Parámetros clave**: `text: String`, `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`, `isLoading: Boolean = false` (opcional).
* **Recomendación**: Estilo llamativo usando colores primarios del tema.

### `SecondaryButton`
* **Uso**: Acción secundaria.
* **Parámetros clave**: `text: String`, `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`.
* **Recomendación**: Estilo menos prominente (ej. `OutlinedButton` o colores secundarios).

### `DangerButton`
* **Uso**: Acciones destructivas (eliminar, cancelar).
* **Parámetros clave**: `text: String`, `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`.
* **Recomendación**: Estilo distintivo usando colores de error/peligro (rojo).

### `GoogleSignButton`
* **Uso**: Botón específico para iniciar sesión con Google.
* **Parámetros clave**: `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`, `isLoading: Boolean = false` (opcional).
* **Recomendación**: Seguir guías de branding de Google.

### `CloseIconButton` (antes `CloseButton` basado en icono)
* **Uso**: Cerrar modales, diálogos, etc., usando un icono.
* **Parámetros clave**: `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`, `contentDescription: String? = "Cerrar"`.
* **Implementación**: Usar `IconButton` con `Icons.Default.Close`.

### `RoundedIconButton` (antes `RoundedButton -> Icon`)
* **Uso**: Botón de acción redondeado con un icono.
* **Parámetros clave**: `icon: ImageVector` (o `Painter`), `contentDescription: String?`, `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`, `backgroundColor: Color` (del tema).

---

## 2. Form & inputs

Componentes para la entrada de datos del usuario.

### `FormContainer`
* **Uso**: Agrupar campos de formulario, aplicando espaciado/estilos comunes.
* **Parámetros clave**: `modifier: Modifier = Modifier`, `content: @Composable ColumnScope.() -> Unit`.
* **Implementación**: Usualmente un `Column` con padding.

### `InputText`
* **Uso**: Campo de texto genérico (base para otros).
* **Parámetros clave**: `value: String`, `onValueChange: (String) -> Unit`, `modifier: Modifier = Modifier`, `label: String?`, `placeholder: String?`, `leadingIcon: @Composable (() -> Unit)?`, `trailingIcon: @Composable (() -> Unit)?`, `isError: Boolean = false`, `supportingText: @Composable (() -> Unit)?`, `keyboardOptions: KeyboardOptions`, `visualTransformation: VisualTransformation`, `enabled: Boolean = true`.
* **Recomendación**: Usar `OutlinedTextField` o `TextField` de Material.

### `InputDate`
* **Uso**: Seleccionar una fecha.
* **Parámetros clave**: `value: LocalDate?` (o `String`), `onValueChange: (LocalDate?) -> Unit`, `modifier: Modifier = Modifier`, `label: String?`, `placeholder: String?`, `isError: Boolean`, `supportingText: @Composable (() -> Unit)?`, `enabled: Boolean`.
* **Implementación**: `InputText` no editable que abre un `DatePickerDialog` al hacer clic.

### `InputSelect`
* **Uso**: Seleccionar una opción de una lista (Dropdown).
* **Parámetros clave**: `options: List<T>`, `selectedOption: T?`, `onOptionSelected: (T) -> Unit`, `optionToString: (T) -> String`, `modifier: Modifier`, `label: String?`, `placeholder: String?`, `isError: Boolean`, `supportingText: @Composable (() -> Unit)?`, `enabled: Boolean`.
* **Recomendación**: Usar `ExposedDropdownMenuBox` de Material.

### `InputEmail`
* **Uso**: Campo específico para email.
* **Recomendación**: Configurar `InputText` con `keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)` y validación específica.

### `InputPassword`
* **Uso**: Campo específico para contraseña.
* **Recomendación**: Configurar `InputText` con `keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)`, `visualTransformation = PasswordVisualTransformation()`, y `trailingIcon` para visibilidad.

### `Switch`
* **Uso**: Interruptor para opciones booleanas.
* **Parámetros clave**: `checked: Boolean`, `onCheckedChange: (Boolean) -> Unit`, `modifier: Modifier`, `enabled: Boolean`, `thumbContent: (@Composable () -> Unit)?`.
* **Recomendación**: Usar `Switch` de Material 3, a menudo en un `Row` con un `Text` como etiqueta.

---

## 3. Screen Components

Componentes estructurales o complejos para pantallas.

### `Header`
* **Uso**: Barra superior de la aplicación.
* **Parámetros clave**: `title: String`, `modifier: Modifier`, `navigationIcon: @Composable (() -> Unit)?`, `actions: @Composable RowScope.() -> Unit`.
* **Recomendación**: Usar `TopAppBar` de Material.

### `BottomNavBar`
* **Uso**: Barra de navegación inferior.
* **Parámetros clave**: `items: List<NavBarItem>` (`route`, `label`, `icon`), `currentRoute: String?`, `onItemSelected: (String) -> Unit`, `modifier: Modifier`.
* **Recomendación**: Usar `NavigationBar` y `NavigationBarItem` (`NavBarButton`) de Material.

### `Modal`
* **Uso**: Diálogo o Bottom Sheet superpuesto.
* **Parámetros clave**: `showModal: Boolean` (o `isVisible`), `onDismissRequest: () -> Unit`, `modifier: Modifier`, `content: @Composable () -> Unit`.
* **Recomendación**: Usar `Dialog` o `ModalBottomSheetLayout`.
    * **`ModalCloseButton`**: Instancia de `CloseIconButton` dentro del `content`.
    * **`ModalHeader`**: Composable interno con `title: String`, `modifier: Modifier`, `actions: @Composable RowScope.() -> Unit`.

### `ModalTabs`
* **Uso**: Pestañas dentro de un modal o cualquier contenedor.
* **Parámetros clave**: `tabs: List<String>` (o data objects), `selectedTabIndex: Int`, `onTabSelected: (Int) -> Unit`, `modifier: Modifier`, `tabContent: @Composable (index: Int) -> Unit`.
* **Recomendación**: Usar `TabRow` o `ScrollableTabRow`.

### `ModalInfoContainer`
* **Uso**: Contenedor estilizado para información dentro de un modal.
* **Parámetros clave**: `modifier: Modifier`, `content: @Composable ColumnScope.() -> Unit`.
* **Implementación**: `Column` o `Box` con estilo (padding, background) del tema.

### `ImageContainer`
* **Uso**: Mostrar una imagen (local o remota).
* **Parámetros clave**: `imageUrl: String` (o `data: Any`), `contentDescription: String?`, `modifier: Modifier`, `placeholder: Painter?`, `error: Painter?`, `contentScale: ContentScale`.
* **Recomendación**: Usar librerías como Coil o Glide.

### `ProfileContainer`
* **Uso**: Mostrar información de perfil de usuario.
* **Parámetros clave**: `user: UserData`, `modifier: Modifier`, `onEditClick: (() -> Unit)?`.
* **Implementación**: Usa `ImageContainer`, `Text`, y opcionalmente `EditButton`.
    * **`EditButton`**: Botón (`TextButton` o `IconButton` con `Icons.Default.Edit`) para iniciar la edición. Parámetros: `onClick: () -> Unit`, `modifier: Modifier`.

### `IconContainer`
* **Uso**: Mostrar un icono vectorial o painter.
* **Parámetros clave**: `icon: ImageVector` (o `Painter`), `contentDescription: String?`, `modifier: Modifier`, `tint: Color`.
* **Recomendación**: Usar el Composable `Icon` estándar.
    * **`EditButton` (en `IconContainer`)**: Si es para editar la entidad representada por el icono, usar el mismo `EditButton` cerca.

### `StatContainer`
* **Uso**: Mostrar una estadística simple (valor y etiqueta).
* **Parámetros clave**: `label: String`, `value: String` (o `Int`), `modifier: Modifier`, `icon: @Composable (() -> Unit)?`.
* **Implementación**: `Column { Text(value); Text(label) }` estilizado.

### `Graph`
* **Uso**: Mostrar gráficos (líneas, barras, etc.).
* **Parámetros clave**: `data: ChartData` (depende de la librería), `modifier: Modifier`.
* **Recomendación**: Usar una librería de gráficos especializada (ej. Vico, MPAndroidChart wrappers).

---

## 4. Info aux

Componentes para mostrar información auxiliar resumida.

### `HabitContainer`
* **Uso**: Mostrar miniatura/resumen de un hábito.
* **Parámetros clave**: `habit: HabitData`, `modifier: Modifier`, `onClick: (() -> Unit)?`.
* **Implementación**: `Card` o `Row` estilizado, usando `IconContainer`, `Text`, etc.

### `AchievementContainer`
* **Uso**: Mostrar información de un logro (bloqueado/desbloqueado, progreso).
* **Parámetros clave**: `achievement: AchievementData`, `modifier: Modifier`.
* **Implementación**: `Card` o `Row` estilizado, usando `IconContainer`, `Text`, y opcionalmente `ProgressBar`. Estilo puede variar según estado (`isUnlocked`).
    * **`ProgressBar`**: Ver descripción abajo.

### `ProgressBar`
* **Uso**: Indicador de progreso lineal.
* **Parámetros clave**: `progress: Float` (0.0f a 1.0f), `modifier: Modifier`, `color: Color`, `trackColor: Color`.
* **Recomendación**: Usar `LinearProgressIndicator` de Material 3. Para circular, `CircularProgressIndicator`.

---

## 5. Text

Componentes para mostrar texto con estilos consistentes.

### `Title`, `Subtitle`, `Caption`, `Description`
* **Uso**: Mostrar texto con estilos tipográficos predefinidos.
* **Parámetros clave**: `text: String`, `modifier: Modifier`, `color: Color`, `textAlign: TextAlign?`, `maxLines: Int`, `overflow: TextOverflow`.
* **Recomendación**: Mapear a estilos de `MaterialTheme.typography` (ej. `h4`, `h6`, `caption`, `body1`) en lugar de definir estilos manualmente.

### `Alert`
* **Uso**: Mostrar mensajes de estado o error (ej. en formularios).
* **Parámetros clave**: `text: String`, `modifier: Modifier`, `severity: AlertSeverity` (Enum: Error, Warning, Info, Success), `icon: @Composable (() -> Unit)?`.
* **Recomendación**: Estilo (color, icono) debe depender del `severity`.

## Recomendaciones Generales

* **`Modifier`**: Incluir `modifier: Modifier = Modifier` en todos los Composables.
* **Estado (State Hoisting)**: Elevar el estado a los Composables padres. Pasar `value` y `onValueChange` a inputs, y `onClick` a botones.
* **Tema (Theme)**: Usar `MaterialTheme` para colores, tipografía y formas. Evitar estilos hardcodeados.
* **Accesibilidad**: Proveer `contentDescription` para elementos no textuales (iconos, imágenes).
* **Layouts**: Utilizar `Column`, `Row`, `Box`, `LazyColumn`, etc., para estructurar los componentes.


