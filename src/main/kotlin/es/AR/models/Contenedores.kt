package es.AR.models

import es.AR.models.enums.Lote
import es.AR.models.enums.TipoContendor
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import java.io.File

@DataSchema
data class Contenedores(
   val codigointerno:String,
   val type_Contenedor: TipoContendor,
   val cantidad: Int,
   val lote: Lote,
   val distrito:String
)


