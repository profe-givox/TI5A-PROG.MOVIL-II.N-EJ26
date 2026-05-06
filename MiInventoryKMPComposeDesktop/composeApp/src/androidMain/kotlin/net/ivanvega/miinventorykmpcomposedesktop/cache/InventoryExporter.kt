package net.ivanvega.miinventorykmpcomposedesktop.cache

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import cache.Item
import java.io.File
import java.io.FileOutputStream

actual open class InventoryExporter(private val context: Context) {

    actual suspend fun exportCsv(data: List<InventoryExportRow>) {
            println("Inicio Export:" + context.filesDir)
        val file = File(context.filesDir, "inventory_export.csv")
        file.writeText(buildString {
            appendLine("Nombre,Cantidad,Precio")
            data.forEach {
                appendLine("${it.name},${it.quantity},${it.price}")
            }
        })
        println("Inicio Export:" + context.filesDir)
    }

    actual suspend fun exportPdf(data: List<Item>) {

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()
        var y = 40

        data.forEach {
            canvas.drawText("${it.name} | ${it.quantity} | $${it.price}", 10f, y.toFloat(), paint)
            y += 20
        }

        document.finishPage(page)

        val file = File(context.filesDir, "inventory_export.pdf")
        document.writeTo(FileOutputStream(file))
        document.close()

    }
}