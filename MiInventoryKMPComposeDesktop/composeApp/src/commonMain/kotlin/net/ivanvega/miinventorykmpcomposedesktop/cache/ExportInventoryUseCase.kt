package net.ivanvega.miinventorykmpcomposedesktop.cache

class ExportInventoryUseCase(
    private val repository: ItemDAO,
    private val exporter: InventoryExporter
) {
    suspend fun exportToCsv() {
        val items = repository.getAllItems()
        val rows = items.map {
            InventoryExportRow(it.name, it.quantity, it.price)
        }
        exporter.exportCsv(rows)
    }

    suspend fun exportToPdf() {
        val items = repository.getAllItems()
        exporter.exportPdf(items)
    }
}

class ExportInventoryUseCaseKlibs(
    private val repository: ItemDAO ,
    private val exporter: InventoryExporterKlibs
) {
    suspend fun exportToCsv() {
        val items = repository.getAllItems()
        val rows = items.map {
            InventoryExportRow(it.name, it.quantity, it.price)
        }
        exporter.exportCsv(rows)
    }

    suspend fun exportToPdf() {
        val items = repository.getAllItems()
        exporter.exportPdf(items)
    }
}