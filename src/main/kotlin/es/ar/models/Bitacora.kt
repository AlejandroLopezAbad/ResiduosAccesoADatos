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

/**
 * @author Alejandro Lopez Abad y Ruben Garcia-Redondo Marin
 */

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



    private fun leerBitacora(path:String): List<Bitacora> {
            val xmlBitacora = XML {indentString = "  "}
            val fichero = File(path + File.separator + "Bitacora.xml")
            return xmlBitacora.decodeFromString(fichero.readText())
    }

    fun crearBitacora() {
        if(File(path + File.separator + "Bitacora.xml").exists()){
            val lista = leerBitacora(path).toMutableList()
            lista += Bitacora(opcion_elegida, exito, System.currentTimeMillis(), path)

            val xml = XML{
                indentString = " "
            }
            val fichero = File(path + File.separator + "Bitacora.xml")
            fichero.writeText(xml.encodeToString(lista))
        }else {
            val xml = XML {
                indentString = " "
            }
            val listaBitacora = listOf(Bitacora("inicializador", true, System.currentTimeMillis(), "inicializador"))
            val fichero = File(path + File.separator + "Bitacora.xml")
            fichero.writeText(xml.encodeToString(listaBitacora))
            crearBitacora()
        }
    }
}


