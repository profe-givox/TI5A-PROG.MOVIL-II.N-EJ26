This is a Kotlin Multiplatform project targeting Android, Desktop (JVM).

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
      Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
      folder is the appropriate location.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

---
Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

commit 24/03/2026
En los últimos cambios se mejoró la navegación de la aplicación para que, al seleccionar un producto en la lista, se abra una pantalla de detalle enviando su identificador como parámetro. Esto permitió mostrar información específica de cada item de forma dinámica.
También se incorporó un ViewModel para la pantalla de detalle, el cual obtiene el itemId desde la navegación y consulta el producto en la base de datos. Para esto, se actualizó el DAO para trabajar con Flow de SQLDelight, haciendo que la información pueda observarse de manera reactiva.
Además, se creó la interfaz ItemDetailsScreen, donde ya se muestran el nombre, la cantidad disponible y el precio formateado como moneda. Se añadieron botones para vender y eliminar, junto con un diálogo de confirmación para borrado, aunque la lógica final de estas acciones todavía quedó pendiente de completar.
Por último, se dejó preparada la estructura de navegación para una futura pantalla de edición de productos y se agregaron clases auxiliares para manejar mejor el estado de la UI y la conversión entre entidades y datos mostrados en pantalla.
Bitácora técnica
1. Se redireccionó el clic sobre un item de la lista hacia ItemDetailsDestination, enviando el id como argumento de navegación.
2. Se registró una nueva ruta composable para la pantalla de detalle dentro de App.kt.
3. Se integró SavedStateHandle en ItemDetailsViewModel para recuperar el parámetro itemId.
4. Se modificó DatabaseDriverFactory.kt para que getItemById() devuelva un Flow<Item?>.
5. Se añadió la librería app.cash.sqldelight:coroutines-extensions en build.gradle.kts y libs.versions.toml.
6. Se creó ItemDetailsViewModel.kt con un StateFlow<ItemDetailsUiState> derivado del DAO.
7. Se agregaron estructuras de estado y funciones de mapeo en ItemViewModel.kt.
8. Se implementó ItemDetailsScreen.kt con visualización de datos, botón de venta y confirmación de borrado.
9. Se creó ItemEditScreen.kt con la definición de destino de navegación para edición.
10.Se quedó pendiente implementar la lógica real de actualización de stock y eliminación, ya que esos métodos siguen comentados.

commit 24/03/2026 0417
Se ha extendido la funcionalidad de navegación para soportar la edición de ítems existentes. Al hacer clic en un producto de la lista, ahora se navega a una pantalla de edición, pasando el ID del producto como parámetro.

Para lograr esto, se realizaron los siguientes ajustes:
1. Se definió un nuevo destino de navegación, `ItemEditDestination`, con una ruta que acepta el ID del ítem (`item_edit/{itemId}`).
2. Se actualizó el `NavHost` en `App.kt` para incluir un nuevo `composable` que responde a esta ruta. Por el momento, esta ruta reutiliza la pantalla `AddItemScreen` como marcador de posición para la futura pantalla de edición.
3. Se modificó `ItemListScreen` para que cada elemento de la lista sea clickeable, invocando la navegación hacia la ruta de edición con el ID correspondiente.
4. Se ajustó la lógica de la `TopAppBar` para mostrar dinámicamente el título "Editar Ítem" cuando el usuario se encuentra en la pantalla de edición.

El siguiente paso será desarrollar la `EditItemScreen` para cargar los datos del ítem seleccionado, permitir su modificación y guardar los cambios en la base de datos.


