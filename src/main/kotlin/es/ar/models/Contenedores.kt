package es.ar.models

import es.ar.models.enums.Lote
import es.ar.models.enums.TipoContendor

/**
 * @author Alejandro Lopez Abad y Ruben Garcia-Redondo Marin
 */

data class Contenedores(
   val codigo_Interno:String,
   val type_Contenedor: TipoContendor,
   val cantidad: Int,
   val lote: Lote,
   val distrito:String
)


