package com.example.inventory.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.coroutineContext

class InventoryProvider : ContentProvider() {

    companion object {
        private const val ITEMS = 100
        private const val ITEM_ID = 101
        private const val ITEM_FILTER = 102

        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(ItemContract.AUTHORITY, ItemContract.PATH_ITEMS, ITEMS)
            addURI(ItemContract.AUTHORITY, "${ItemContract.PATH_ITEMS}/#", ITEM_ID)
            addURI(ItemContract.AUTHORITY, "${ItemContract.PATH_ITEMS}/*", ITEM_FILTER)
        }
    }

    private lateinit var database: InventoryDatabase

    override fun onCreate(): Boolean {
        database = InventoryDatabase.getDatabase(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val result = GlobalScope.launch {
             database.itemDao().selectAllCursor()
        }

        val match = sUriMatcher.match(uri)
        val cursor: Cursor? =
            when (match) {
                ITEMS -> database.itemDao().selectAllCursor()
                ITEM_ID -> {
                    val id = ContentUris.parseId(uri)
                    database.itemDao().selectByIdCursor(id)
                }
                else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        cursor?.setNotificationUri(context?.contentResolver, uri)

        return cursor
    }

    override fun getType(uri: Uri): String? {
        return when (sUriMatcher.match(uri)) {
            ITEMS -> ItemContract.ItemEntry.CONTENT_LIST_TYPE
            ITEM_ID -> ItemContract.ItemEntry.CONTENT_ITEM_TYPE
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val match = sUriMatcher.match(uri)
        return when (match) {
            ITEMS -> {
                val name = values?.getAsString(ItemContract.ItemEntry.COLUMN_NAME) ?: ""
                val price = values?.getAsDouble(ItemContract.ItemEntry.COLUMN_PRICE) ?: 0.0
                val quantity = values?.getAsInteger(ItemContract.ItemEntry.COLUMN_QUANTITY) ?: 0
                
                val item = Item(name = name, price = price, quantity = quantity)
                runBlocking { database.itemDao().insert(item) }
                
                context?.contentResolver?.notifyChange(uri, null)
                // Note: Since insert returns Unit, we don't have the new ID.
                // In a real app, you'd update the DAO to return Long.
                uri
            }
            else -> throw IllegalArgumentException("Insertion is not supported for $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val match = sUriMatcher.match(uri)
        return when (match) {
            ITEM_ID -> {
                val id = ContentUris.parseId(uri).toInt()
                // We need to fetch the item first because delete(item) takes an object
                // This is inefficient but necessary with the current DAO signature
                runBlocking {
                    // This is a bit tricky since getItem returns a Flow
                    // For a Provider, direct SQL delete via openHelper is often better 
                    // if the DAO isn't optimized for it.
                }



                // For simplicity and to avoid complex Flow handling in runBlocking:
                val count = database.openHelper.writableDatabase.delete(
                    ItemContract.ItemEntry.TABLE_NAME,
                    "${ItemContract.ItemEntry.COLUMN_ID} = ?",
                    arrayOf(id.toString())
                )
                if (count > 0) context?.contentResolver?.notifyChange(uri, null)
                count
            }
            else -> {
                 val count = database.openHelper.writableDatabase.delete(
                    ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs
                )
                if (count > 0) context?.contentResolver?.notifyChange(uri, null)
                count
            }
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        val match = sUriMatcher.match(uri)
        val count = when (match) {
            ITEMS -> database.openHelper.writableDatabase.update(
                ItemContract.ItemEntry.TABLE_NAME, 0, values!!, selection, selectionArgs
            )
            ITEM_ID -> {
                val id = ContentUris.parseId(uri)
                database.openHelper.writableDatabase.update(
                    ItemContract.ItemEntry.TABLE_NAME, 0, values!!,
                    "${ItemContract.ItemEntry.COLUMN_ID} = ?", arrayOf(id.toString())
                )
            }
            else -> throw IllegalArgumentException("Update is not supported for $uri")
        }
        if (count > 0) context?.contentResolver?.notifyChange(uri, null)
        return count
    }
}
