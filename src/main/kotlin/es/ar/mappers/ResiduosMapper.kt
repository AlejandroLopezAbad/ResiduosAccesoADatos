package es.ar.mappers

import es.ar.dto.ResiduosDTO
import es.ar.models.Residuos
import es.ar.models.enums.TipoResiduo
import es.ar.utils.ParseFloat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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

    /**
     * TODO
     *
     * @param path
     * @param lista
     */
    fun residuoToCSV(path:String, lista:List<ResiduosDTO>){
        val file = File(path + "residuos.csv")
        file.writeText("Año;Mes;Residuo;Lote;Nombre_Distrito;Toneladas")
        lista.forEach {
            file.appendText("\n${it.year};${it.month};${it.residuos};${it.lote};${it.nombre_distrito};${it.toneladas}")
        }
    }

    /**
     * TODO
     *
     * @param path
     * @param residuosDto
     */
    fun residuoToJson(path:String, residuosDto: List<ResiduosDTO>) {
        val file = File(path + "residuos.json")
        val json = Json { prettyPrint = true }
        file.writeText(json.encodeToString(residuosDto))
    }

    /**
     * TODO
     *
     * @param path
     * @param residuosDto
     */
    fun residuoToXML(path:String, residuosDto: List<ResiduosDTO>) {
        val xml = XML { indentString = "  " }
        val fichero = File(path + "residuos.xml")
        fichero.writeText(xml.encodeToString(residuosDto))
    }

}