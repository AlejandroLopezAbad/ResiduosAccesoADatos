package es.AR.mappers

import es.AR.dto.ResiduosDTO
import es.AR.models.Residuos
import es.AR.models.enums.TipoResiduo
import es.AR.utils.ParseFloat
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import utils.ParseTipo
import java.io.File
import java.nio.file.Files
import java.nio.file.Path


/**
 * Clase que se encarga del mapeo de los Residuos
 * Creacion de CSV, JSON y XML
 */
object ResiduosMapper {

    /**
     * Metodo que coje un ResiduoDTO y lo transforma a un objeto de tipo Residuo
     * @param residuosDTO es el objeto de tipo residuosDTO a convertir
     * @return el objeto de TIPO Residuos
     */
    fun ResiduosDTO.dtoToResiduos(): Residuos {
        return Residuos(
            year = this.year,
            month = this.month,
            lote = ParseTipo().stringLoteToTypeLote(this.lote),
            residuos = TipoResiduo.valueOf(this.residuos),
            nombre_distrito = this.nombre_distrito,
            toneladas = this.toneladas
        )
    }


    /**
     * TODO
     *
     * @param residuos
     * @return
     */
    fun Residuos.residuosToResiduosDTO():ResiduosDTO{
        return ResiduosDTO(
            year = this.year,
            month = this.month,
            lote=this.lote.name,
            residuos=this.residuos.name,
            nombre_distrito = this.nombre_distrito,
            toneladas = this.toneladas
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

 /*  fun residuoDTOToxml(path:String,listResiduosDTO:List<ResiduosDTO>){
       val xml= XML {indentString= " "}
       val fichero = File(path + File.separator +  "intercambio.xml")
       fichero.writeText(xml.encodeToString(listResiduosDTO))
   }*/ // si descomentas esto el programa te dice que te vayas a hacer una

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
            lote =ParseTipo().stringLoteToTypeLote(campo[2]),
            residuos =ParseTipo().stringResiduoToTypeResiduo(campo[3]),
            nombre_distrito = campo[5],
            toneladas = ParseFloat().stringToFloat(campo[6]),
        )
    }


}