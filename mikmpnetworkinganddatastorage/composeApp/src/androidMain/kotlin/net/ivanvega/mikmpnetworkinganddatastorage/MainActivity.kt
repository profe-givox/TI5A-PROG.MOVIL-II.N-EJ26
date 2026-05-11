package net.ivanvega.mikmpnetworkinganddatastorage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import net.ivanvega.mikmpnetworkinganddatastorage.cache.AndroidDatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(AndroidDatabaseDriverFactory(applicationContext))
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    //App()
}