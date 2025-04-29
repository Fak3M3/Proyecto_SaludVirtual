# Composables a desarrollar

## A. General Buttons

Botones estándar para diversas acciones.

### `1. PrimaryButton`
* **Uso**: Acción principal en una pantalla o diálogo.
* **Parámetros clave**: `text: String`, `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`, `isLoading: Boolean = false` (opcional).
* **Recomendación**: Estilo llamativo usando colores primarios del tema.

### `2. SecondaryButton`
* **Uso**: Acción secundaria.
* **Parámetros clave**: `text: String`, `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`.
* **Recomendación**: Estilo menos prominente (ej. `OutlinedButton` o colores secundarios).

### `3. DangerButton`
* **Uso**: Acciones destructivas (eliminar, cancelar).
* **Parámetros clave**: `text: String`, `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`.
* **Recomendación**: Estilo distintivo usando colores de error/peligro (rojo).

### `4. GoogleSignButton`
* **Uso**: Botón específico para iniciar sesión con Google.
* **Parámetros clave**: `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`, `isLoading: Boolean = false` (opcional).
* **Recomendación**: Seguir guías de branding de Google.

### `5. CloseIconButton` (antes `CloseButton` basado en icono)
* **Uso**: Cerrar modales, diálogos, etc., usando un icono.
* **Parámetros clave**: `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`, `contentDescription: String? = "Cerrar"`.
* **Implementación**: Usar `IconButton` con `Icons.Default.Close`.

### `6. RoundedIconButton` (antes `RoundedButton -> Icon`)
* **Uso**: Botón de acción redondeado con un icono.
* **Parámetros clave**: `icon: ImageVector` (o `Painter`), `contentDescription: String?`, `onClick: () -> Unit`, `modifier: Modifier = Modifier`, `enabled: Boolean = true`, `backgroundColor: Color` (del tema).

---

## B. Form & inputs

Componentes para la entrada de datos del usuario.

### `7. FormContainer`
* **Uso**: Agrupar campos de formulario, aplicando espaciado/estilos comunes.
* **Parámetros clave**: `modifier: Modifier = Modifier`, `content: @Composable ColumnScope.() -> Unit`.
* **Implementación**: Usualmente un `Column` con padding.

### `8. InputText`
* **Uso**: Campo de texto genérico (base para otros).
* **Parámetros clave**: `value: String`, `onValueChange: (String) -> Unit`, `modifier: Modifier = Modifier`, `label: String?`, `placeholder: String?`, `leadingIcon: @Composable (() -> Unit)?`, `trailingIcon: @Composable (() -> Unit)?`, `isError: Boolean = false`, `supportingText: @Composable (() -> Unit)?`, `keyboardOptions: KeyboardOptions`, `visualTransformation: VisualTransformation`, `enabled: Boolean = true`.
* **Recomendación**: Usar `OutlinedTextField` o `TextField` de Material.

### `9. InputDate`
* **Uso**: Seleccionar una fecha.
* **Parámetros clave**: `value: LocalDate?` (o `String`), `onValueChange: (LocalDate?) -> Unit`, `modifier: Modifier = Modifier`, `label: String?`, `placeholder: String?`, `isError: Boolean`, `supportingText: @Composable (() -> Unit)?`, `enabled: Boolean`.
* **Implementación**: `InputText` no editable que abre un `DatePickerDialog` al hacer clic.

### `10. InputSelect`
* **Uso**: Seleccionar una opción de una lista (Dropdown).
* **Parámetros clave**: `options: List<T>`, `selectedOption: T?`, `onOptionSelected: (T) -> Unit`, `optionToString: (T) -> String`, `modifier: Modifier`, `label: String?`, `placeholder: String?`, `isError: Boolean`, `supportingText: @Composable (() -> Unit)?`, `enabled: Boolean`.
* **Recomendación**: Usar `ExposedDropdownMenuBox` de Material.

### `11. InputEmail`
* **Uso**: Campo específico para email.
* **Recomendación**: Configurar `InputText` con `keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)` y validación específica.

### `12. InputPassword`
* **Uso**: Campo específico para contraseña.
* **Recomendación**: Configurar `InputText` con `keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)`, `visualTransformation = PasswordVisualTransformation()`, y `trailingIcon` para visibilidad.

### `13. Switch`
* **Uso**: Interruptor para opciones booleanas.
* **Parámetros clave**: `checked: Boolean`, `onCheckedChange: (Boolean) -> Unit`, `modifier: Modifier`, `enabled: Boolean`, `thumbContent: (@Composable () -> Unit)?`.
* **Recomendación**: Usar `Switch` de Material 3, a menudo en un `Row` con un `Text` como etiqueta.

---

## C. Screen Components

Componentes estructurales o complejos para pantallas.

### `14. Header`
* **Uso**: Barra superior de la aplicación.
* **Parámetros clave**: `title: String`, `modifier: Modifier`, `navigationIcon: @Composable (() -> Unit)?`, `actions: @Composable RowScope.() -> Unit`.
* **Recomendación**: Usar `TopAppBar` de Material.

### `15. BottomNavBar`
* **Uso**: Barra de navegación inferior.
* **Parámetros clave**: `items: List<NavBarItem>` (`route`, `label`, `icon`), `currentRoute: String?`, `onItemSelected: (String) -> Unit`, `modifier: Modifier`.
* **Recomendación**: Usar `NavigationBar` y `NavigationBarItem` (`NavBarButton`) de Material.

### `16. Modal`
* **Uso**: Diálogo o Bottom Sheet superpuesto.
* **Parámetros clave**: `showModal: Boolean` (o `isVisible`), `onDismissRequest: () -> Unit`, `modifier: Modifier`, `content: @Composable () -> Unit`.
* **Recomendación**: Usar `Dialog` o `ModalBottomSheetLayout`.
    * **`ModalCloseButton`**: Instancia de `CloseIconButton` dentro del `content`.
    * **`ModalHeader`**: Composable interno con `title: String`, `modifier: Modifier`, `actions: @Composable RowScope.() -> Unit`.

### `17. ModalTabs`
* **Uso**: Pestañas dentro de un modal o cualquier contenedor.
* **Parámetros clave**: `tabs: List<String>` (o data objects), `selectedTabIndex: Int`, `onTabSelected: (Int) -> Unit`, `modifier: Modifier`, `tabContent: @Composable (index: Int) -> Unit`.
* **Recomendación**: Usar `TabRow` o `ScrollableTabRow`.

### `18. ModalInfoContainer`
* **Uso**: Contenedor estilizado para información dentro de un modal.
* **Parámetros clave**: `modifier: Modifier`, `content: @Composable ColumnScope.() -> Unit`.
* **Implementación**: `Column` o `Box` con estilo (padding, background) del tema.

### `19. ImageContainer`
* **Uso**: Mostrar una imagen (local o remota).
* **Parámetros clave**: `imageUrl: String` (o `data: Any`), `contentDescription: String?`, `modifier: Modifier`, `placeholder: Painter?`, `error: Painter?`, `contentScale: ContentScale`.
* **Recomendación**: Usar librerías como Coil o Glide.

### `20. ProfileContainer`
* **Uso**: Mostrar información de perfil de usuario.
* **Parámetros clave**: `user: UserData`, `modifier: Modifier`, `onEditClick: (() -> Unit)?`.
* **Implementación**: Usa `ImageContainer`, `Text`, y opcionalmente `EditButton`.
    * **`EditButton`**: Botón (`TextButton` o `IconButton` con `Icons.Default.Edit`) para iniciar la edición. Parámetros: `onClick: () -> Unit`, `modifier: Modifier`.

### `21. IconContainer`
* **Uso**: Mostrar un icono vectorial o painter.
* **Parámetros clave**: `icon: ImageVector` (o `Painter`), `contentDescription: String?`, `modifier: Modifier`, `tint: Color`.
* **Recomendación**: Usar el Composable `Icon` estándar.
    * **`EditButton` (en `IconContainer`)**: Si es para editar la entidad representada por el icono, usar el mismo `EditButton` cerca.

### `22. StatContainer`
* **Uso**: Mostrar una estadística simple (valor y etiqueta).
* **Parámetros clave**: `label: String`, `value: String` (o `Int`), `modifier: Modifier`, `icon: @Composable (() -> Unit)?`.
* **Implementación**: `Column { Text(value); Text(label) }` estilizado.

### `23. Graph`
* **Uso**: Mostrar gráficos.
* **Parámetros clave**: `data: ChartData` (depende de la librería), `modifier: Modifier`.
* **Recomendación**: Usar una librería de gráficos especializada (ej. Vico, MPAndroidChart wrappers).

---

## D. Info aux

Componentes para mostrar información auxiliar resumida.

### `24. HabitContainer`
* **Uso**: Mostrar miniatura/resumen de un hábito.
* **Parámetros clave**: `habit: HabitData`, `modifier: Modifier`, `onClick: (() -> Unit)?`.
* **Implementación**: `Card` o `Row` estilizado, usando `IconContainer`, `Text`, etc.

### `25. AchievementContainer`
* **Uso**: Mostrar información de un logro (bloqueado/desbloqueado, progreso).
* **Parámetros clave**: `achievement: AchievementData`, `modifier: Modifier`.
* **Implementación**: `Card` o `Row` estilizado, usando `IconContainer`, `Text`, y `ProgressBar`. Estilo puede variar según estado (`isUnlocked`).
    * **`ProgressBar`**: Ver descripción abajo.

### `26. ProgressBar`
* **Uso**: Indicador de progreso lineal.
* **Parámetros clave**: `progress: Float` (0.0f a 1.0f), `modifier: Modifier`, `color: Color`, `trackColor: Color`.
* **Recomendación**: Usar `LinearProgressIndicator` de Material 3. Para circular, `CircularProgressIndicator`.

---

## E. Text

Componentes para mostrar texto con estilos consistentes.

### `27. Title`, `Subtitle`, `Caption`, `Description`
* **Uso**: Mostrar texto con estilos tipográficos predefinidos.
* **Parámetros clave**: `text: String`, `modifier: Modifier`, `color: Color`, `textAlign: TextAlign?`, `maxLines: Int`, `overflow: TextOverflow`.
* **Recomendación**: Mapear a estilos de `MaterialTheme.typography` (ej. `h4`, `h6`, `caption`, `body1`) en lugar de definir estilos manualmente.

### `28. Alert`
* **Uso**: Mostrar mensajes de estado o error (ej. en formularios).
* **Parámetros clave**: `text: String`, `modifier: Modifier`, `severity: AlertSeverity` (Enum: Error, Warning, Info, Success), `icon: @Composable (() -> Unit)?`.
* **Recomendación**: Estilo (color, icono) debe depender del `severity`.

## F. Orden de Desarrollo Sugerido (NUEVA SECCIÓN)

Priorización para la implementación de los componentes.

### Primeros a Desarrollar (Fundamentales y Bloques Básicos)

1.  **Text (`Title`, `Subtitle`, `Caption`, `Description`)**: Base para casi todo. Implementar usando `MaterialTheme.typography`.
2.  **`IconContainer`**: Necesario para botones y otros componentes.
3.  **Botones Básicos (`PrimaryButton`, `SecondaryButton`, `CloseIconButton`, `GoogleSignButton`, `RoundedIconButton`)**: Acciones fundamentales.
4.  **`InputText`**: Base para la mayoría de los campos de formulario.
5.  **`ImageContainer`**: Esencial si se manejan imágenes de perfil o iconos personalizados pronto.
6.  **`FormContainer`**: Para empezar a estructurar formularios.
7.  **`Switch`**: Control de formulario común.
8.  **`ProgressBar`**: Indicador visual útil en varias partes.
9.  **`Alert`**: Para mostrar retroalimentación básica al usuario.

### Desarrollo Intermedio (Componentes de Estructura y Formularios Específicos)

10. **`Header`**: Estructura básica de pantalla.
11. **`BottomNavBar`**: Navegación principal (si aplica).
12. **`InputEmail`, `InputPassword`**: Especializaciones de `InputText`.
13. **`InputDate`**: Requiere integración con `DatePickerDialog`.
14. **`InputSelect`**: Requiere manejo de menú desplegable.
15. **`DangerButton`, `GoogleSignButton`**: Botones con usos más específicos.
16. **`StatContainer`**: Presentación simple de datos.
17. **`Modal` (con `ModalCloseButton`, `ModalHeader`)**: Para diálogos y hojas inferiores.

### Últimos a Desarrollar (Componentes Complejos, Específicos o Dependientes de Librerías)

18. **`ProfileContainer`**: Agrupa varios componentes ya hechos.
19. **`HabitContainer`**: Específico de la lógica de hábitos, usa otros componentes.
20. **`AchievementContainer`**: Específico de la lógica de logros, usa otros componentes.
21. **`ModalInfoContainer`**: Contenedor estilizado, menos crítico inicialmente.
22. **`ModalTabs`**: Funcionalidad de pestañas, a menudo para modales complejos.
23. **`Graph`**: Requiere investigación e integración de una librería externa, alta complejidad.

## Recomendaciones Generales

* **`Modifier`**: Incluir `modifier: Modifier = Modifier` en todos los Composables.
* **Estado (State Hoisting)**: Elevar el estado a los Composables padres. Pasar `value` y `onValueChange` a inputs, y `onClick` a botones.
* **Tema (Theme)**: Usar `MaterialTheme` para colores, tipografía y formas. Evitar estilos hardcodeados.
* **Accesibilidad**: Proveer `contentDescription` para elementos no textuales (iconos, imágenes).
* **Layouts**: Utilizar `Column`, `Row`, `Box`, `LazyColumn`, etc., para estructurar los componentes.


