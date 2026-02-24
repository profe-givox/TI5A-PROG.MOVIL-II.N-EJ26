package net.ivanvega.clientproviderinventory

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Item(val id: Long, val name: String, val price: Double, val quantity: Int)

@Composable
fun InventoryScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val contentResolver = context.contentResolver
    var itemList by remember { mutableStateOf<List<Item>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var insertedUri by remember { mutableStateOf<Uri?>(null) }

    Column(modifier = modifier.padding(16.dp)) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        TextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
        TextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Quantity") })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                val newUri = insertItem(contentResolver, name, price, quantity)
                insertedUri = newUri
                if (newUri != null) {
                    itemList = queryItems(contentResolver)
                    // Clear fields
                    name = ""
                    price = ""
                    quantity = ""
                }
            }
        }) {
            Text("Insert Item")
        }
        insertedUri?.let {
            Text("Inserted Uri: $it")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            coroutineScope.launch {
                itemList = queryItems(contentResolver)
            }
        }) {
            Text("Query Items")
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(itemList) { item ->
                ListItem(
                    headlineContent = { Text(item.name) },
                    supportingContent = { Text("Price: ${item.price}, Quantity: ${item.quantity}") }
                )
            }
        }
    }
}

private suspend fun queryItems(contentResolver: ContentResolver): List<Item> {
    return withContext(Dispatchers.IO) {
        val cursor: Cursor? = contentResolver.query(
            ItemContract.ItemEntry.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val items = mutableListOf<Item>()
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_ID)
            val nameColumn = it.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME)
            val priceColumn = it.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_PRICE)
            val quantityColumn = it.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_QUANTITY)
            while (it.moveToNext()) {
                items.add(
                    Item(
                        id = it.getLong(idColumn),
                        name = it.getString(nameColumn),
                        price = it.getDouble(priceColumn),
                        quantity = it.getInt(quantityColumn)
                    )
                )
            }
        }
        items
    }
}

private suspend fun insertItem(contentResolver: ContentResolver, name: String, price: String, quantity: String): Uri? {
    return withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put(ItemContract.ItemEntry.COLUMN_NAME, name)
            put(ItemContract.ItemEntry.COLUMN_PRICE, price.toDoubleOrNull() ?: 0.0)
            put(ItemContract.ItemEntry.COLUMN_QUANTITY, quantity.toIntOrNull() ?: 0)
        }
        contentResolver.insert(ItemContract.ItemEntry.CONTENT_URI, values)
    }
}