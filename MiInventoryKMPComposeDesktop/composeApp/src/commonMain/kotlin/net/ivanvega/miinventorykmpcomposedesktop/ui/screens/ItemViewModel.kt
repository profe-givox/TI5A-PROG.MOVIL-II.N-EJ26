package net.ivanvega.miinventorykmpcomposedesktop.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cache.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.ivanvega.miinventorykmpcomposedesktop.cache.ItemDAO
import java.text.NumberFormat

class ItemViewModel (val dao: ItemDAO) : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items = _items.asStateFlow()
    init {
        loadItems()
    }
    private fun loadItems() {
        viewModelScope.launch(Dispatchers.IO) {
            _items.value = dao.getAllItems()
        }
    }
    fun insertItem(name: String, price: Double, quantity: Long) {
        viewModelScope.launch {
            dao.insertItem(name, price, quantity)
            loadItems()
        }
    }


    fun insertItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertItem(item.name, item.price, item.quantity)
        }
    }

}

