package es.ar.mappers

import es.ar.dto.ResiduosDTO
import es.ar.models.Residuos
import es.ar.models.enums.TipoResiduo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML
import es.ar.utils.ParseTipo
import es.ar.utils.stringToFloat
import kotlinx.serialization.decodeFromString
import org.apache.poi.UnsupportedFileFormatException
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.annotation.processing.FilerException


/**
 * Clase que se encarga del mapeo de los Residuos
 * Creacion de CSV, JSON y XML
 */
object ResiduosMapper {

    /**
     * Metodo que coge un ResiduoDTO y lo transforma a un objeto de tipo Residuo
     *
     * @param residuosDTO es el objeto de tipo residuosDTO a convertir
     * @return el objeto de tipo Residuos
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
     * Metodo que coge un ResiduoDTO y lo transforma a un objeto de tipo Residuo
     *
     * @param residuos es el objeto de tipo Residuo a convertir
     * @return El objeto de tipo ResiduoDTO
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
     * Método que lee un archivo CSV y lo convierte a una lista de tipo Residuos
     *
     * @param path Ruta del archivo
     * @return Una lista de tipo Residuos
     */
    fun csvReaderToResiduo(path:String):List<Residuos>{
        return Files.lines(Path.of(path))
            .skip(1)
            .map { mapToResiduo(it) }.toList()
    }

    /**
     * Metodo que tranforma una cadena de string en un objeto de tipo residuo
     *
     * @param linea cadena a transformar
     * @return El objeto de tipo residuo
     */
    fun mapToResiduo(linea:String):Residuos{
        val campo=linea.split(";")
        return Residuos(
            year = campo[0].toShort(),
            month = campo[1],
            lote = ParseTipo().stringLoteToTypeLote(campo[2]),
            residuos = ParseTipo().stringResiduoToTypeResiduo(campo[3]),
            nombre_distrito = campo[5],
            toneladas = stringToFloat(campo[6]),
        )
    }

    /**
     *Metodo que coge una lista de ResiduosDTO y lo transforma a un csv con los datos
     * que hemos querido guardar
     *
     * @param path localizacion de donde vamos a guardar el csv
     * @param lista  la lista de residuosDTO que se transformara en el nuevo csv
     */
    fun residuoToCSV(path:String, lista:List<ResiduosDTO>){
        val file = File(path + "residuos.csv")
        file.writeText("year;month;residuo;lote;nombreDistrito;toneladas")
        lista.forEach {
            file.appendText("\n${it.year};${it.month};${it.residuos};${it.lote};${it.nombre_distrito};${it.toneladas}")
        }
    }

    /**
     * Metodo que coge una lista de ResiduosDTO y lo transforma a un JSON con formato pretty
     *
     * @param path localización donde se guardara el JSON
     * @param residuosDto  la lista de residuosDTO que se transformara en un archivo JSON
     */
    fun residuoToJson(path:String, residuosDto: List<ResiduosDTO>) {
        val file = File(path + "residuos.json")
        val json = Json { prettyPrint = true }
        file.writeText(json.encodeToString(residuosDto))
    }

    /**
     *Metodo que coge una lista de ResiduosDTO y lo transforma a un Xml
     *
     * @param path localización donde se guardara el XML
     * @param residuosDto la lista de residuosDTO que se transformara en un xml
     */
    fun residuoToXML(path:String, residuosDto: List<ResiduosDTO>) {
        val xml = XML { indentString = "  " }
        val fichero = File(path + "residuos.xml")
        fichero.writeText(xml.encodeToString(residuosDto))
    }

    /**
     * Metodo que coge un archivo JSON y lo transforma en una lista de ResiduosDTO
     *
     * @param path el directorio donde se encuntra el archivo que vamos a leer
     * @return una lista de ResiduosDTO
     */
    fun jsonToResiduoDTO(path:String):List<ResiduosDTO>{
        var file = File(path)

        if (file.exists() && file.endsWith(".json")){
            val pretty= Json {prettyPrint= true }
            return Json.decodeFromString(File(path).readText())

        }
        throw FilerException("No ha sido posible leer el archivo Json")
    }

    /**
     * Metodo que se encarga de leer un XML y transformarlo en una lista de ResiduosDTO
     *
     * @param path el directorio donde se encuentra el archivo que vamos a leer
     * @return Devuelve una lista de ResiduosDTO
     */
        //TODO mirar si hay que meter filtros para comprobar que no pete tipo
    //meter uno vacio otro nulo y tal
    fun xmlToResiduoDTO(path:String):List<ResiduosDTO>{
        val xmlResiduo = XML {indentString = "  "}
        val fichero = File(path)
        return xmlResiduo.decodeFromString(fichero.readText())
    }







}