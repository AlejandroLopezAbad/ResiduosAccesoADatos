package es.AR.mappers

import es.AR.dto.ContenedoresDTO
import es.AR.models.Contenedores
import es.AR.models.enums.Lote
import es.AR.models.enums.TipoContendor
import utils.ParseTipo


object ContenedoresMapper {


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


    private fun mapToContenedor(linea:String):Contenedores{
        val campo=linea.split(";")
        return Contenedores(
            codigo_Interno = campo[0],
            type_Contenedor = ParseTipo().stringContenedorToTypeContenedor(campo[1]),
            cantidad = campo[4].toInt(),
            lote = ParseTipo().stringLoteToTypeLote(campo[5]),
            distrito = campo[6]
        )
    }




}