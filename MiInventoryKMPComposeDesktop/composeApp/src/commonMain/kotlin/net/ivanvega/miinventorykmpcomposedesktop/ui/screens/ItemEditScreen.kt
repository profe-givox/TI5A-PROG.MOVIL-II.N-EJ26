package net.ivanvega.miinventorykmpcomposedesktop.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import miinventorykmpcomposedesktop.composeapp.generated.resources.Res
import miinventorykmpcomposedesktop.composeapp.generated.resources.edit_item_title
import net.ivanvega.miinventorykmpcomposedesktop.NavigationDestination

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection

import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch


object ItemEditDestination : NavigationDestination {
    override val route = "item_edit"
    override val titleRes = Res.string.edit_item_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues ,
    viewModel: ItemEditViewModel
) {
    val coroutineScope = rememberCoroutineScope()
//    Scaffold(
//        topBar = {
//            InventoryTopAppBar(
//                title = stringResource(ItemEditDestination.titleRes),
//                canNavigateBack = true,
//                navigateUp = onNavigateUp
//            )
//        },
//        modifier = modifier
//    ) { innerPadding ->
        ItemEntryBody(
            itemUiState = viewModel.itemUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateItem()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding()
                )
                .verticalScroll(rememberScrollState())
        )
//    }
}

//@Preview(showBackground = true)
//@Composable
//fun ItemEditScreenPreview() {
//    InventoryTheme {
//        ItemEditScreen(navigateBack = { /*Do nothing*/ }, onNavigateUp = { /*Do nothing*/ })
//    }
//}
