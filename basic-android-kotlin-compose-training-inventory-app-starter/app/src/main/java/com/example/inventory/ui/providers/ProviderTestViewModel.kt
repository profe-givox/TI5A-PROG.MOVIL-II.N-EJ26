package com.example.inventory.ui.providers

import ItemsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item

import com.example.inventory.ui.home.HomeUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class ProviderTestViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        itemsRepository.getAllItemsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = HomeUiState()
            )

    fun addItem() {
        viewModelScope.launch {
            val randomItem = Item(
                name = "Provider Item ${Random.nextInt(100)}",
                price = Random.nextDouble(10.0, 100.0),
                quantity = Random.nextInt(1, 50)
            )
            itemsRepository.insertItem(randomItem)
        }
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch {
            itemsRepository.deleteItem(item)
        }
    }
}
