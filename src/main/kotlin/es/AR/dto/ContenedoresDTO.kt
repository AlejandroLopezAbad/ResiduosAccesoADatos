package es.AR.dto

import es.AR.models.enums.Lote
import es.AR.models.enums.TipoContendor
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema


data class ContenedoresDTO(
    val codigo_Interno:String,
    val type_Contenedor: String,
    val cantidad: Int,
    val lote: String,
    val distrito: String
) {



}