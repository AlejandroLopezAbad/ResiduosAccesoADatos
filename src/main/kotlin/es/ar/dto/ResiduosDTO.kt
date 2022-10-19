package es.ar.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
/**
 * @author Alejandro Lopez Abad y Ruben Garcia-Redondo Marin
 */

@DataSchema
@Serializable
@SerialName("Residuos")
data class ResiduosDTO(
    @XmlElement(true)
    val year: Short,
    @XmlElement(true)
    val month: String,
    @XmlElement(true)
    val lote: String,
    @XmlElement(true)
    val residuos: String,
    @XmlElement(true)
  //  val num_distrito: Short,
    val nombre_distrito: String,
    @XmlElement(true)
    val toneladas: Float
){
}