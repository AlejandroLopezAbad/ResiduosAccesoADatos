package es.ar.models

import es.ar.dto.ResiduosDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import java.io.File
import java.time.LocalDateTime
import java.util.*


@Serializable
@SerialName("Bitácora")
class Bitacora (
    @XmlElement(true)
    val opcion_elegida: String,
    @XmlElement(true)
    val exito: Boolean,
    @XmlElement(true)
    val tiempo: Long,
    @XmlElement(true)
    val path: String
) {
    @XmlElement(true)
    val id: String = UUID.randomUUID().toString()
    @XmlElement(true)
    val instante: String = LocalDateTime.now().toString()

    init {
        crearBitacora()
    }
    private fun crearBitacora() {
        val xml = XML{
            indentString = " "
        }
        val fichero = File(path + File.separator + "Bitacora.xml")
        fichero.appendText("\n")
        fichero.appendText(xml.encodeToString(this))
    }
}


