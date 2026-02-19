package com.example.inventory.data

import ItemsRepository
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.example.inventory.providers.ItemContract
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

class ProviderItemsRepository(private val context: Context) : ItemsRepository {

    override fun getAllItemsStream(): Flow<List<Item>> = callbackFlow {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                trySend(fetchItems())
            }
        }
        context.contentResolver.registerContentObserver(
            ItemContract.ItemEntry.CONTENT_URI,
            true,
            observer
        )

        trySend(fetchItems())

        awaitClose {
            context.contentResolver.unregisterContentObserver(observer)
        }
    }

    private fun fetchItems(): List<Item> {
        val items = mutableListOf<Item>()
        val cursor = context.contentResolver.query(
            ItemContract.ItemEntry.CONTENT_URI,
            null, null, null, null
        )
        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_ID)
            val nameIndex = it.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME)
            val priceIndex = it.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_PRICE)
            val quantityIndex = it.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_QUANTITY)

            while (it.moveToNext()) {
                items.add(
                    Item(
                        id = it.getInt(idIndex),
                        name = it.getString(nameIndex),
                        price = it.getDouble(priceIndex),
                        quantity = it.getInt(quantityIndex)
                    )
                )
            }
        }
        return items
    }

    override fun getItemStream(id: Int): Flow<Item?> = callbackFlow {
        val uri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, id.toLong())
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                trySend(fetchItem(id))
            }
        }
        context.contentResolver.registerContentObserver(uri, false, observer)
        trySend(fetchItem(id))
        awaitClose { context.contentResolver.unregisterContentObserver(observer) }
    }

    private fun fetchItem(id: Int): Item? {
        val uri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, id.toLong())
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_NAME)
                val priceIndex = cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_PRICE)
                val quantityIndex = cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_QUANTITY)
                return Item(
                    id = id,
                    name = cursor.getString(nameIndex),
                    price = cursor.getDouble(priceIndex),
                    quantity = cursor.getInt(quantityIndex)
                )
            }
        }
        return null
    }

    override suspend fun insertItem(item: Item): Long {
        val values = ContentValues().apply {
            put(ItemContract.ItemEntry.COLUMN_NAME, item.name)
            put(ItemContract.ItemEntry.COLUMN_PRICE, item.price)
            put(ItemContract.ItemEntry.COLUMN_QUANTITY, item.quantity)
        }
        val uri = context.contentResolver.insert(ItemContract.ItemEntry.CONTENT_URI, values)
        return ContentUris.parseId(uri!!)
    }

    override suspend fun deleteItem(item: Item): Int {
        val uri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, item.id.toLong())
        return context.contentResolver.delete(uri, null, null)
    }

    override suspend fun updateItem(item: Item): Int {
        val values = ContentValues().apply {
            put(ItemContract.ItemEntry.COLUMN_NAME, item.name)
            put(ItemContract.ItemEntry.COLUMN_PRICE, item.price)
            put(ItemContract.ItemEntry.COLUMN_QUANTITY, item.quantity)
        }
        val uri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, item.id.toLong())
        return context.contentResolver.update(uri, values, null, null)
    }
}
