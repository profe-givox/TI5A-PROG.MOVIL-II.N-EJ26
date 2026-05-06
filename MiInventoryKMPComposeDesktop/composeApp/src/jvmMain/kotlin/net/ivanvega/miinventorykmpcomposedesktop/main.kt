package net.ivanvega.miinventorykmpcomposedesktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import net.ivanvega.miinventorykmpcomposedesktop.cache.ExportInventoryUseCase
import net.ivanvega.miinventorykmpcomposedesktop.cache.InventoryExporter
import net.ivanvega.miinventorykmpcomposedesktop.cache.InventoryExporterKlibs
import net.ivanvega.miinventorykmpcomposedesktop.cache.JvmDatabaseDriverFactory
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "miinventorykmpcomposedesktop",
    ) {


        // ✅ FileSystem multiplataforma
        val fileSystem = FileSystem.SYSTEM

        // ✅ Ruta base (por ejemplo, carpeta del usuario)
        val basePath: Path =
            System.getProperty("user.home")
                .toPath()
                .resolve("exports")

        // Crear carpeta si no existe
        fileSystem.createDirectories(basePath)

        // ✅ Inicializar exporter
        val exporterKlibs = InventoryExporterKlibs(
            fileSystem = fileSystem,
            basePath = basePath
        )


        App(JvmDatabaseDriverFactory(),
            InventoryExporter(),
            exporterKlibs

        )
    }
}