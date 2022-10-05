package es.AR.dto

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement


data class ResiduosDTO(

    val year: Short,
    val month: String,
    val lote: String,
    val residuos: String,
  //  val num_distrito: Short,
    val nombre_distrito: String,
    val toneladas: Float
){
}