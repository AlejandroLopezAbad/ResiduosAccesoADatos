package es.ar.models

import es.ar.dto.ResiduosDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
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

    private fun leerBitacora(path:String): List<Bitacora> {
            val xmlBitacora = XML {indentString = "  "}
            val fichero = File(path + "Bitacora.xml")
            return xmlBitacora.decodeFromString(fichero.readText())
    }

    private fun crearBitacora() {
        if(File(path + "Bitacora.xml").exists()){
            var lista = leerBitacora(path)
            lista += Bitacora(opcion_elegida, exito, System.currentTimeMillis(), path)

            val xml = XML{
                indentString = " "
            }
            val fichero = File(path + File.separator + "Bitacora.xml")
            fichero.appendText("\n")
            fichero.appendText(xml.encodeToString(lista))
        }else {
            val xml = XML {
                indentString = " "
            }
            val fichero = File(path + File.separator + "Bitacora.xml")
            fichero.appendText("\n")
            fichero.appendText(xml.encodeToString(this))
        }
    }
}


