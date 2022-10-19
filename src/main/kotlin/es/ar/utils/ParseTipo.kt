package es.ar.utils

import es.ar.models.enums.Lote
import es.ar.models.enums.TipoContendor
import es.ar.models.enums.TipoResiduo
/**
 * @author Alejandro Lopez Abad y Ruben Garcia-Redondo Marin
 */

class ParseTipo() {
    /**
     * Convierte una cadena en un tipo de Lote.
     *
     * @param campo -> Campo a transformar
     * @return El tipo de Lote
     */
    fun stringLoteToTypeLote(campo: String): Lote {
        var type: Lote = Lote.UNO
        when (campo) {
            "1" -> type = Lote.UNO
            "2" -> type = Lote.DOS
            "3" -> type = Lote.TRES
        }
        return type
    }

    /**
     * Convierte una cadena en un tipo de residuo.
     *
     * @param campo -> Campo a transformar
     * @return El tipo de Residuo
     */
  fun stringResiduoToTypeResiduo(campo: String): TipoResiduo {
        var type: TipoResiduo = TipoResiduo.ENVASES
        when (campo) {
            "RESTO" -> type = TipoResiduo.RESTO
            "ENVASES" -> type = TipoResiduo.ENVASES
            "VIDRIO" -> type = TipoResiduo.VIDRIO
            "ORGANICA" -> type = TipoResiduo.ORGANICA
            "PAPEL Y CARTON" -> type = TipoResiduo.PAPEL_CARTON
            "PUNTOS LIMPIOS" -> type = TipoResiduo.PUNTOS_LIMPIOS
            "CARTON COMERCIAL" -> type = TipoResiduo.CARTON_COMERCIAL
            "VIDRIO COMERCIAL" -> type = TipoResiduo.VIDRIO_COMERCIAL
            "PILAS" -> type = TipoResiduo.PILAS
            "ANIMALES MUERTOS" -> type = TipoResiduo.ANIMALES_MUERTOS
            "RCD" -> type = TipoResiduo.RCD
            "CONTENEDORES DE ROPA USADA" -> type = TipoResiduo.CONTENEDORES_ROPA_USADA
            "CAMA DE CABALLO" -> type = TipoResiduo.CAMA_CABALLO
        }
        return type
    }

    /**
     * Convierte una cadena en un tipo de contenedor.
     *
     * @param campo -> Campo a transformar
     * @return El tipo de contenedor
     */
    fun stringContenedorToTypeContenedor(campo: String): TipoContendor {
        var type: TipoContendor = TipoContendor.ENVASES
        when (campo) {
            "ORGANICA" -> type = TipoContendor.ORGANICA
            "RESTO" -> type = TipoContendor.RESTO
            "ENVASES" -> type = TipoContendor.ENVASES
            "VIDRIO" -> type = TipoContendor.VIDRIO
            "PAPEL_CARTON" -> type = TipoContendor.PAPEL_CARTON
        }
        return type
    }


}