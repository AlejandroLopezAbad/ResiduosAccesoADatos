package es.ar.models

import es.ar.models.enums.Lote
import es.ar.models.enums.TipoResiduo
import kotlinx.serialization.Serializable



data class Residuos(
    val year: Short,
    val month: String,
    val lote: Lote,
    val residuos: TipoResiduo,
  //  val num_distrito: Short = 0,
    val nombre_distrito: String,
    val toneladas: Float,
    )