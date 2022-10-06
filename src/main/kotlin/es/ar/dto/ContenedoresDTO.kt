package es.ar.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Contenedores")
data class ContenedoresDTO(
    val codigo_Interno:String,
    val type_Contenedor: String,
    val cantidad: Int,
    val lote: String,
    val distrito: String
) {



}