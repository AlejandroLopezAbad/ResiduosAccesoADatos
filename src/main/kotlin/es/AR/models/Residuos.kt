package es.AR.models

import es.AR.models.enums.Lote
import es.AR.models.enums.TipoResiduo
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import java.time.LocalDate

@DataSchema
data class Residuos(
    val year: Short,
    val month: String,
    val lote: Lote,
    val residuos: TipoResiduo,
  //  val num_distrito: Short = 0,
    val nombre_distrito: String = "Desconocido",
    val toneladas: Float = 0.0f
    ) {


}