package net.ivanvega.miinventorykmpcomposedesktop.cache

import cache.Item
import com.ryinex.kotlin.csv.CsvReadWrite
import com.ryinex.kotlin.csv.builder
import okio.FileSystem
import okio.Path

class InventoryExporterKlibs(
    private val fileSystem: FileSystem,
    private val basePath: Path
) {

    fun exportCsv(data: List<InventoryExportRow>) {
        val csv = CsvReadWrite.builder(data, isTitled = true)
            .column("Nombre") { _, it -> it.name }
            .column("Cantidad") { _, it -> it.quantity }
            .column("Precio") { _, it -> it.price }
            .build("inventory.csv")

        fileSystem.write(basePath / "inventory.csv") {
            writeUtf8(csv.raw())
        }
    }

    fun exportPdf(data: List<Item>) {
        val pdfBytes = generatePdfBytes(data.map { InventoryExportRow(it.name, it.quantity, it.price) })
        fileSystem.write(basePath / "inventory.pdf") {
            write(pdfBytes)
        }
    }


    fun generatePdfBytes(data: List<InventoryExportRow>): ByteArray {
        val text = buildString {
            appendLine("REPORTE DE INVENTARIO")
            appendLine("====================")
            data.forEach {
                appendLine("${it.name} | ${it.quantity} | $${it.price}")
            }
        }
        return text.encodeToByteArray()
    }
}