package es.AR.mappers

import es.AR.dto.ResiduosDTO
import es.AR.models.Residuos
import es.AR.models.enums.Lote
import es.AR.models.enums.TipoResiduo
import java.nio.file.Files
import java.nio.file.Path

/**
 * Clase que se encarga del mapeo de los Residuos
 * Creacion de CSV, JSON y XML
 */
class ResiduosMapper {

    /**
     * Metodo que coje un ResiduoDTO y lo transforma a un objeto de tipo Residuo
     * @param residuosDTO es el objeto de tipo residuosDTO a convertir
     * @return el objeto de TIPO Residuos
     */
    fun dtoToResiduos(residuosDTO: ResiduosDTO): Residuos {
        return Residuos(
            year = residuosDTO.year,
            month = residuosDTO.month,
            lote = stringLoteToTypeLote(residuosDTO.lote),
            residuos = TipoResiduo.valueOf(residuosDTO.residuos),
            nombre_distrito = residuosDTO.nombre_distrito,
            toneladas = residuosDTO.toneladas
        )
    }


    /**
     * TODO
     *
     * @param residuos
     * @return
     */
    fun residuosToResiduosDTO(residuos:Residuos):ResiduosDTO{
        return ResiduosDTO(
            year = residuos.year,
            month = residuos.month,
            lote=residuos.lote.name,
            residuos=residuos.residuos.name,
            nombre_distrito = residuos.nombre_distrito,
            toneladas = residuos.toneladas
        )
    }

    /**
     * TODO
     *
     * @param path
     * @return
     */
    fun csvReaderToResiduo(path:String):List<Residuos>{
        return Files.lines(Path.of(path))
            .skip(1)
            .map { mapToResiduo(it) }.toList()
    }

    fun residuoDTOToxml(path:String,listResiduosDTO:List<ResiduosDTO>){


    }



    /**
     * TODO
     *
     * @param linea
     * @return
     */
    private fun mapToResiduo(linea:String):Residuos{
        val campo=linea.split(";")
        return Residuos(
            year = campo[0].toShort(),
            month = campo[1],
            lote = stringLoteToTypeLote(campo[2]),
            residuos = stringResiduoToTypeResiduo(campo[3]),
            nombre_distrito = campo[5],
            toneladas = 0F
            //TODO //hay que cambiar las comas del csv por
        )
    }



    /**
     * TODO
     *
     * @param campo
     * @return
     */
    private fun stringResiduoToTypeResiduo(campo: String): TipoResiduo {
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
    protected fun stringLoteToTypeLote(campo: String): Lote {
        var type: Lote = Lote.UNO
        when (campo) {
            "1" -> type = Lote.UNO
            "2" -> type = Lote.DOS
            "3" -> type = Lote.TRES
        }
        return type
    }

}