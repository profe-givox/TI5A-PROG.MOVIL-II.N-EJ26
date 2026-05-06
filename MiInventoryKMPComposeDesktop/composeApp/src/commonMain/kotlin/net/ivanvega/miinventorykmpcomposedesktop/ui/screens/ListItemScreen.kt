package net.ivanvega.miinventorykmpcomposedesktop.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import miinventorykmpcomposedesktop.composeapp.generated.resources.Res
import miinventorykmpcomposedesktop.composeapp.generated.resources.app_name
import net.ivanvega.miinventorykmpcomposedesktop.NavigationDestination
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cache.Item
import miinventorykmpcomposedesktop.composeapp.generated.resources.in_stock
import miinventorykmpcomposedesktop.composeapp.generated.resources.no_item_description
import org.jetbrains.compose.resources.stringResource


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = Res.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
                    //viewModel: ItemViewModel,
                    viewModel: HomeViewModel,
                    onItemClick: (Item) -> Unit,
                    navigateToItemUpdate:   (Long) -> Unit,
                   modifier: Modifier,
                    innerPadding: PaddingValues) {

    val homeUiState by viewModel.homeUiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    HomeBody(
        itemList = homeUiState.itemList,
        onItemClick = navigateToItemUpdate,
        onExportCsv = viewModel::onExportCsvKlibs,
        onExportPdf = viewModel::onExportPdfKlibs,
        modifier = modifier.fillMaxSize(),
        contentPadding = innerPadding,
    )
    //    val itemList by viewModel.items.collectAsState(initial = emptyList())
    //
    //    LazyColumn(
    //        modifier = modifier,
    //    ) {
    //        items(items = itemList, key = { it.id }) { item ->
    //            InventoryItem(item = item,
    //                modifier = Modifier
    //                    .padding(horizontal = 16.dp, vertical = 8.dp)
    //                    .clickable { onItemClick(item) })
    //        }
    //    }

}

@Composable
private fun HomeBody(
    itemList: List<Item>,
    onItemClick: (Long) -> Unit,
    onExportCsv: () -> Unit,
    onExportPdf: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(Res.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            Button(onClick = onExportCsv) {
                Text("Export CSV")
            }
            Button(onClick = onExportPdf) {
                Text("Export PDF")
            }
            InventoryList(
                itemList = itemList,
                onItemClick = { onItemClick(it.id) },
                contentPadding = contentPadding,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun InventoryList(
    itemList: List<Item>,
    onItemClick: (Item) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = itemList, key = { it.id }) { item ->
            InventoryItem(item = item,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onItemClick(item) })
        }
    }
}

@Composable
private fun InventoryItem(
    item: Item, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = item.formatedPrice(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = stringResource(Res.string.in_stock, item.quantity),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeBodyPreview() {
//    InventoryTheme {
//        HomeBody(listOf(
//            Item(1, "Game", 100.0, 20), Item(2, "Pen", 200.0, 30), Item(3, "TV", 300.0, 50)
//        ), onItemClick = {})
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun HomeBodyEmptyListPreview() {
//    InventoryTheme {
//        HomeBody(listOf(), onItemClick = {})
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun InventoryItemPreview() {
//    InventoryTheme {
//        InventoryItem(
//            Item(1, "Game", 100.0, 20),
//        )
//    }
//}
//
//
//@Composable
//private fun InventoryItem(
//    item: Item, modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier,
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(20.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = item.name,
//                    style = MaterialTheme.typography.titleLarge,
//                )
//                Spacer(Modifier.weight(1f))
//                Text(
//                    text = item.price.toString(),
//                    style = MaterialTheme.typography.titleMedium
//                )
//            }
//            Text(
//                text = stringResource(Res.string.in_stock, item.quantity),
//                style = MaterialTheme.typography.titleMedium
//            )
//        }
//    }
//}