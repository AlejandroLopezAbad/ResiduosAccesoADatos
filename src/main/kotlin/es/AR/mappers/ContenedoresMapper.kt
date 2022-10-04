package es.AR.mappers

import es.AR.dto.ContenedoresDTO
import es.AR.models.Contenedores
import es.AR.models.enums.Lote
import es.AR.models.enums.TipoContendor
import utils.ParseTipo


class ContenedoresMapper {


    fun dtoToContenedores(contenedorDTO: ContenedoresDTO): Contenedores {
        return Contenedores(
            codigo_Interno = contenedorDTO.codigo_Interno,
            type_Contenedor = TipoContendor.valueOf(contenedorDTO.type_Contenedor),
            cantidad = contenedorDTO.cantidad,
            lote = ParseTipo().stringLoteToTypeLote(contenedorDTO.lote),
            distrito = contenedorDTO.distrito
        )
    }


    fun contenedoresToContenedoresDTO(contenedores: Contenedores): ContenedoresDTO {
        return ContenedoresDTO(
            codigo_Interno = contenedores.codigo_Interno,
            type_Contenedor = contenedores.type_Contenedor.name,
            cantidad = contenedores.cantidad,
            lote = contenedores.lote.name,
            distrito = contenedores.distrito
        )
    }

    /**
     * TODO
     *
     * @param campo
     * @return
     */

    private fun stringContenedorToTypeContenedor(campo: String): TipoContendor {
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