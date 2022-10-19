package es.ar.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement


@Serializable
@SerialName("Contenedores")
data class ContenedoresDTO(
    @XmlElement(true)
    val codigo_Interno:String,
    @XmlElement(true)
    val type_Contenedor: String,
    @XmlElement(true)
    val cantidad: Int,
    @XmlElement(true)
    val lote: String,
    @XmlElement(true)
    val distrito: String
) {



}