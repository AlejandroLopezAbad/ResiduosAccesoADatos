package es.ar.models.enums

/**
 * Hemos decidido crear este enum para poder clasificar todos los tipos de residuos que hay,
 * ya que para la posterior filtración de datos no será más facil de esta manera
 */
enum class TipoResiduo(tipo:String) {
    RESTO("RESTO"),
    ENVASES("ENVASES"),
    VIDRIO("VIDRIO"),
    ORGANICA("ORGANICA"),
    PAPEL_CARTON("PAPEL Y CARTON"),
    PUNTOS_LIMPIOS("PUNTOS LIMPIOS"),
    CARTON_COMERCIAL("CARTON COMERCIAL"),
    VIDRIO_COMERCIAL("VIDRIO COMERCIAL"),
    PILAS("PILAS"),
    ANIMALES_MUERTOS("ANIMALES MUERTOS"),
    RCD("RCD"),
    CONTENEDORES_ROPA_USADA("CONTENEDORES DE ROPA USADA"),
    //Hemos encontrado en el CSV otro tipo de residuo que es CAMA DE CABALLO
    CAMA_CABALLO("CAMA DE CABALLO")

}