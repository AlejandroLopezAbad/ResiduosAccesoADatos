package es.ar.models

import es.ar.models.enums.Lote
import es.ar.models.enums.TipoContendor


data class Contenedores(
   val codigo_Interno:String,
   val type_Contenedor: TipoContendor,
   val cantidad: Int,
   val lote: Lote,
   val distrito:String
)


