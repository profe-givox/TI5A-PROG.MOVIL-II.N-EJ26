package net.ivanvega.miinventorykmpcomposedesktop.cache

import cache.Item
import org.openpdf.text.Document
import org.openpdf.text.Paragraph
import org.openpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream

actual open class InventoryExporter {

    actual suspend fun exportCsv(data: List<InventoryExportRow>) {
        val file = File("inventory_export.csv")
        println("Inicio Export:" + file.absolutePath)
        file.writeText(
            buildString {
                appendLine("Nombre,Cantidad,Precio")
                data.forEach {
                    appendLine("${it.name},${it.quantity},${it.price}")
                }
            }
        )
        println("Fin Export")
    }

    actual suspend fun exportPdf(data: List<Item>) {

        val document = Document()
        PdfWriter.getInstance(document, FileOutputStream("inventory_export.pdf"))

        document.open()
        document.add(Paragraph("Reporte de Inventario"))

        data.forEach {
            document.add(
                Paragraph("${it.name} | ${it.quantity} | $${it.price}")
            )
        }

        document.close()
        println("$document")
    }
}