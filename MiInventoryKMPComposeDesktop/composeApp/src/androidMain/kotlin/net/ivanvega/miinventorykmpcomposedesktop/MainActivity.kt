package net.ivanvega.miinventorykmpcomposedesktop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import net.ivanvega.miinventorykmpcomposedesktop.cache.AndroidDatabaseDriverFactory
import net.ivanvega.miinventorykmpcomposedesktop.cache.InventoryExporter
import net.ivanvega.miinventorykmpcomposedesktop.cache.InventoryExporterKlibs
import okio.FileSystem
import okio.Path.Companion.toPath

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // ✅ FileSystem multiplataforma
        val fileSystem = FileSystem.SYSTEM

        // ✅ Ruta base segura en Android
        val basePath = filesDir
            .absolutePath
            .toPath()
            .resolve("exports")

        fileSystem.createDirectories(basePath)

        val exporterKlibs = InventoryExporterKlibs(
            fileSystem = fileSystem,
            basePath = basePath
        )


        setContent {
            App(AndroidDatabaseDriverFactory(this),
                InventoryExporter(this),
                exporterKlibs
                )
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}