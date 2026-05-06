package net.ivanvega.miinventorykmpcomposedesktop.cache

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.db.SqlDriver
import cache.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

class ItemDAO(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun getAllItems() : List<Item
            > {
        return dbQuery.getAllItems().executeAsList()
    }

    internal fun getAllItemsStream() : Flow<List<Item>> {
        return dbQuery.getAllItems().asFlow().mapToList(Dispatchers.IO)
    }

    internal fun insertItem(name: String, price: Double, quantity: Long) {
        dbQuery.insertItem(name, price, quantity )
    }

    internal fun updateItem(id: Long, name: String, price: Double, quantity: Long) {
        dbQuery.updateItem(name, price, quantity, id)
    }

    internal fun updateItem(item: Item) {
        dbQuery.updateItem(item.name, item.price, item.quantity, item.id)
    }

    internal fun deleteItem(item: Item) {
        dbQuery.deleteItem(item.id)
    }

    internal fun getItemById(id: Long): Flow <Item?> {
        return dbQuery.getItemById(id).asFlow().mapToOne(Dispatchers.IO)
    }

    fun insertItem(item: Item) {
        dbQuery.insertItem(item.name, item.price, item.quantity)
    }

}