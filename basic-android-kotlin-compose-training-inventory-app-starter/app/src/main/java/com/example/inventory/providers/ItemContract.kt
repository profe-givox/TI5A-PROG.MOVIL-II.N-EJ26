    package com.example.inventory.providers

import android.net.Uri
import android.provider.BaseColumns

/**
 * Clase contrato para el Content Provider de Items.
 */
object ItemContract {
    const val AUTHORITY = "com.example.inventory.provider"
    val BASE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY")
    const val PATH_ITEMS = "items"

    object ItemEntry : BaseColumns {
        val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEMS).build()

        const val TABLE_NAME = "items"

        const val COLUMN_ID = BaseColumns._ID
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_QUANTITY = "quantity"

        const val CONTENT_LIST_TYPE = "vnd.android.cursor.dir/$AUTHORITY/$PATH_ITEMS"
        const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/$AUTHORITY/$PATH_ITEMS"
    }
}
