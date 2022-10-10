package es.ar.models

import es.ar.models.enums.Lote
import es.ar.models.enums.TipoResiduo
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema

@DataSchema
data class Residuos(
    val year: Short,
    val month: String,
    val lote: Lote,
    val residuos: TipoResiduo,
    val nombre_distrito: String,
    val toneladas: Float,
    )