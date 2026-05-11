package net.ivanvega.mikmpnetworkinganddatastorage

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import net.ivanvega.mikmpnetworkinganddatastorage.cache.JvmDatabaseDriverFactory

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "Mikmpnetworkinganddatastorage",
    ) {
        App(JvmDatabaseDriverFactory() )
    }
}