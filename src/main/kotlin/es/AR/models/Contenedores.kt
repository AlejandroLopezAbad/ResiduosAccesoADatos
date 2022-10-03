package es.AR.models


 data class Contenedores(
    val codigointerno:Int,
    val type_Contenedor:TipoContendor,
    val cantidad: Int,
    val lote: Lote,
    val distrito:String
) {
}