package es.AR

import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class Bitacora (
    val opcion_elegida: String,
    val exito: Boolean,
    val tiempo: Long,
    val path: String
) {
    val id: UUID = UUID.randomUUID()
    val instante: LocalDateTime = LocalDateTime.now()

    init {
        crearBitacora()
    }
    private fun crearBitacora() {
        val xml = XML{
            indentString = " "
        }
        val fichero = File(path + File.separator + "Bitacora.xml")
        fichero.writeText(xml.encodeToString(this))
    }
}
