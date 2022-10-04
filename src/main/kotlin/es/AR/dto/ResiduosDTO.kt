package es.AR.dto


data class ResiduosDTO(
    val year: Short,
    val month: String,
    val lote: String,
    val residuos: String,
  //  val num_distrito: Short,
    val nombre_distrito: String,
    val toneladas: Float
){
}