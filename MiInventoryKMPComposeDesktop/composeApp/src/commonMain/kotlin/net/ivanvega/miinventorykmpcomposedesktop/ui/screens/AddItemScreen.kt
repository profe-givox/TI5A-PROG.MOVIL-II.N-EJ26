package net.ivanvega.miinventorykmpcomposedesktop.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import miinventorykmpcomposedesktop.composeapp.generated.resources.Res
import miinventorykmpcomposedesktop.composeapp.generated.resources.item_entry_title
import miinventorykmpcomposedesktop.composeapp.generated.resources.item_name_req
import miinventorykmpcomposedesktop.composeapp.generated.resources.item_price_req
import miinventorykmpcomposedesktop.composeapp.generated.resources.quantity_req
import miinventorykmpcomposedesktop.composeapp.generated.resources.required_fields
import miinventorykmpcomposedesktop.composeapp.generated.resources.save_action
import net.ivanvega.miinventorykmpcomposedesktop.NavigationDestination
import org.jetbrains.compose.resources.stringResource
import java.util.Currency
import java.util.Locale

//object ItemEntryDestination : NavigationDestination {
//    override val route = "item_entry"
//    override val titleRes = Res.string.item_entry_title
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(viewModel: ItemViewModel, onItemAdded: () -> Unit, modifier: Modifier) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }


        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    viewModel.insertItem(
                        name = name,
                        price = price.toDoubleOrNull() ?: 0.0,
                        quantity = quantity.toLongOrNull() ?: 0L
                    )
                    onItemAdded()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add")
            }
        }

}

