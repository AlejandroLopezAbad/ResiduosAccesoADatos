package es.AR.models

import es.AR.models.enums.Lote
import es.AR.models.enums.TipoContendor
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema


data class Contenedores(
   val codigo_Interno:String,
   val type_Contenedor: TipoContendor,
   val cantidad: Int,
   val lote: Lote,
   val distrito:String
)


