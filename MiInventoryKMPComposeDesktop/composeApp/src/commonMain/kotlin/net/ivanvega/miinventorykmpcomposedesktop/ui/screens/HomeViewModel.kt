package net.ivanvega.miinventorykmpcomposedesktop.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cache.Item

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.ivanvega.miinventorykmpcomposedesktop.cache.ExportInventoryUseCase
import net.ivanvega.miinventorykmpcomposedesktop.cache.ExportInventoryUseCaseKlibs
import net.ivanvega.miinventorykmpcomposedesktop.cache.InventoryExporter
import net.ivanvega.miinventorykmpcomposedesktop.cache.ItemDAO

/**
 * ViewModel to retrieve all items in the Room database.
 */
class HomeViewModel(val itemsRepository: ItemDAO,
                    val exportUseCase: ExportInventoryUseCase,
            val exportUseCaseKlibs: ExportInventoryUseCaseKlibs


                    ) : ViewModel() {

    fun onExportCsv() {
        viewModelScope.launch {
            exportUseCase.exportToCsv()
        }
    }

    fun onExportPdf() {
        viewModelScope.launch {
           exportUseCase.exportToPdf()
        }
    }

    fun onExportCsvKlibs() {
        viewModelScope.launch {
            exportUseCaseKlibs.exportToCsv()
        }
    }

    fun onExportPdfKlibs() {
        viewModelScope.launch {
            exportUseCaseKlibs.exportToPdf()
        }
    }

    /**
     * Holds home ui state. The list of items are retrieved from [ItemsRepository] and mapped to
     * [HomeUiState]
     */
    val homeUiState: StateFlow<HomeUiState> =
        itemsRepository.getAllItemsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }




}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val itemList: List<Item> = listOf())