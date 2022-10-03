package es.AR.dto


data class ResiduosDTO(
    val año: Short,
    val mes: String,
    val lote: Short,
    val residuos: String,
    val num_distrito: Short,
    val nombre_distrito: String,
    val toneladas: Float
){
}