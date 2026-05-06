package net.ivanvega.miinventorykmpcomposedesktop.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection


import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cache.Item

import kotlinx.coroutines.launch
import miinventorykmpcomposedesktop.composeapp.generated.resources.Res
import miinventorykmpcomposedesktop.composeapp.generated.resources.attention
import miinventorykmpcomposedesktop.composeapp.generated.resources.delete
import miinventorykmpcomposedesktop.composeapp.generated.resources.delete_question
import miinventorykmpcomposedesktop.composeapp.generated.resources.edit
import miinventorykmpcomposedesktop.composeapp.generated.resources.edit_item_title
import miinventorykmpcomposedesktop.composeapp.generated.resources.item
import miinventorykmpcomposedesktop.composeapp.generated.resources.item_detail_title
import miinventorykmpcomposedesktop.composeapp.generated.resources.no
import miinventorykmpcomposedesktop.composeapp.generated.resources.price
import miinventorykmpcomposedesktop.composeapp.generated.resources.quantity_in_stock
import miinventorykmpcomposedesktop.composeapp.generated.resources.sell
import miinventorykmpcomposedesktop.composeapp.generated.resources.yes
import net.ivanvega.miinventorykmpcomposedesktop.NavigationDestination
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

object ItemDetailsDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = Res.string.item_detail_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailsScreen(
    navigateToEditItem: (Long) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues ,
    viewModel: ItemDetailsViewModel
) {
        val uiState = viewModel.uiState.collectAsState()
        val coroutineScope = rememberCoroutineScope()
    /*Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(ItemDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditItem(uiState.value.itemDetails.id) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_item_title),
                )
            }
        },
        modifier = modifier,
    ) { innerPadding ->*/
        ItemDetailsBody(
            itemDetailsUiState = uiState.value,
            onSellItem = {
                            viewModel.reduceQuantityByOne()
                         },
            onDelete = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be deleted from the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                coroutineScope.launch {
                    viewModel.deleteItem()
                    navigateBack()
                }
            },
            navigateToEditItem= navigateToEditItem,
            modifier = Modifier
                .padding(
                    start =  innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState())
        )
    //}
}

@Composable
private fun ItemDetailsBody(
    itemDetailsUiState: ItemDetailsUiState,
    onSellItem: () -> Unit,
    onDelete: () -> Unit,
    navigateToEditItem: (Long) -> Unit,
    modifier: Modifier = Modifier,

) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }
        ItemDetails(
            item = itemDetailsUiState.itemDetails.toItem(), modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSellItem,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            enabled = !itemDetailsUiState.outOfStock
        ) {
            Text(stringResource(Res.string.sell))
        }
        OutlinedButton(
            onClick = {
                        //deleteConfirmationRequired = true
                         navigateToEditItem(itemDetailsUiState.itemDetails.id)
                      },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.edit_item_title))
        }
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.delete))
        }
        if (deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = { deleteConfirmationRequired = false },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun ItemDetails(
    item: Item, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ItemDetailsRow(
                labelResID = Res.string.item,
                itemDetail = item.name,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            ItemDetailsRow(
                labelResID = Res.string.quantity_in_stock,
                itemDetail = item.quantity.toString(),
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
            ItemDetailsRow(
                labelResID = Res.string.price,
                itemDetail = item.formatedPrice(),
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
        }

    }
}

@Composable
private fun ItemDetailsRow(
     labelResID: StringResource, itemDetail: String, modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = stringResource(labelResID))
        Spacer(modifier = Modifier.weight(1f))
        Text(text = itemDetail, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(Res.string.attention)) },
        text = { Text(stringResource(Res.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(Res.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(Res.string.yes))
            }
        })
}

/*@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview() {
    InventoryTheme {
        ItemDetailsBody(ItemDetailsUiState(
            outOfStock = true, itemDetails = ItemDetails(1, "Pen", "$100", "10")
        ), onSellItem = {}, onDelete = {})
    }
}*/
