package es.ar.models

import es.ar.dto.ResiduosDTO
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
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
*/

