package net.ivanvega.miinventorykmpcomposedesktop.cache

import cache.Item
import com.ryinex.kotlin.csv.CsvReadWrite
import com.ryinex.kotlin.csv.builder
import okio.FileSystem
import okio.Path

expect class InventoryExporter {
    suspend fun exportCsv(data: List<InventoryExportRow>)
    suspend fun exportPdf(data: List<Item>)
}

