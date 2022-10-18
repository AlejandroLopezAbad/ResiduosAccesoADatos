package es.ar.mappers

import es.ar.dto.ContenedoresDTO
import es.ar.dto.ResiduosDTO
import es.ar.models.Contenedores
import es.ar.models.enums.TipoContendor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML
import es.ar.utils.ParseTipo
import kotlinx.serialization.decodeFromString
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.annotation.processing.FilerException


object ContenedoresMapper {

    /**
     * Metodo coge un contenedorDTO y lo transforma a un objeto de TIPO contenedores
     *
     * @return Contenedor
     */
    fun ContenedoresDTO.dtoToContenedores(): Contenedores {
        return Contenedores(
            codigo_Interno = this.codigo_Interno,
            type_Contenedor = TipoContendor.valueOf(this.type_Contenedor),
            cantidad = this.cantidad,
            lote = ParseTipo().stringLoteToTypeLote(this.lote),
            distrito = this.distrito
        )
    }

    /**
     * Metodo que coge un objeto de TIPO contenedor y lo transfroma en un objeto de TIPO ContenedoresDTO
     *
     * @return ContenedoresDTO
     */
    fun Contenedores.contenedoresToContenedoresDTO(): ContenedoresDTO {
        return ContenedoresDTO(
            codigo_Interno = this.codigo_Interno,
            type_Contenedor = this.type_Contenedor.name,
            cantidad = this.cantidad,
            lote = this.lote.name,
            distrito = this.distrito
        )
    }

    /**
     * Metodo que coge un csv, lee el csv y lo transforma en una lista de Contenedores
     *
     * @param path Directorio de donde lee el csv
     * @return una lista de contenedores
     */
    fun csvReaderToContenedores(path:String):List<Contenedores>{
        return Files.lines(Path.of(path))
            .skip(1)
            .map { mapToContenedor(it) }.toList()
    }

    /**
     * Metodo que tranforma una cadena de string en un objeto de tipo contenedor
     *
     * @param linea cadena a transformar
     * @return un objeto de TIPO contenedor
     */
     fun mapToContenedor(linea:String):Contenedores{
        val campo=linea.split(";")
        return Contenedores(
            codigo_Interno = campo[0],
            type_Contenedor = ParseTipo().stringContenedorToTypeContenedor(campo[1]),
            cantidad = campo[4].toInt(),
            lote = ParseTipo().stringLoteToTypeLote(campo[5]),
            distrito = campo[6]
        )
    }

    /**
     * Metodo que coge una lista de COntenedoresDTO y lo transforma a un csv con los datos
     * que hemos querido guardar
     * @param path localizacion de donde vamos a guardar el csv
     * @param lista la lista de contenedoresDTO que se transformara en el nuevo csv
     */
    fun contenedorToCSV(path:String, lista:List<ContenedoresDTO>){
        val file = File(path + "contenedor.csv")
        file.writeText("CodigoInterno;Lote;Tipo_Contendor;Distrito;Cantidad")
        lista.forEach {
            file.appendText("\n${it.codigo_Interno};${it.lote};${it.type_Contenedor};${it.distrito};${it.cantidad};")
        }
    }

    /**
     * Metodo que coge una lista de ContenedoresDTO y lo transforma a un JSON
     *
     * @param path  localización donde se guardara el JSON
     * @param contenedorDto  la lista de contenedoresDTO que se transformara en un archivo JSON
     */
    fun contenedorToJson(path:String, contenedorDto: List<ContenedoresDTO>) {
        val file = File(path + "contenedor.json")
        val json = Json { prettyPrint = true }
        file.writeText(json.encodeToString(contenedorDto))
    }

    /**
     * Metodo que coge una lista de ContenedoresDTO y lo transforma a un Xml
     *
     * @param path localización donde se guardara el XML
     * @param contenedoresDto la lista de contenedorDto que se transformara en un xml
     */
    fun contenedorToXML(path:String, contenedoresDto: List<ContenedoresDTO>) {
        val xml = XML { indentString = "  " }
        val fichero = File(path + "contenedor.xml")
        fichero.writeText(xml.encodeToString(contenedoresDto))

    }

    /**
     * Metodo que coge un archivo JSON y lo transforma en una lista de ContenedoresDTO
     *
     * @param path el directorio donde se encuntra el archivo que vamos a leer
     * @return una lista de ContenedoresDTO
     */
    fun jsonToContenedor(path:String):List<ContenedoresDTO>{
        var file = File(path)
        if (file.exists() && file.endsWith(".json")){
            val pretty= Json {prettyPrint= true }
            return Json.decodeFromString(File(path).readText())

        }
        throw FilerException("No ha sido posible leer el archivo Json")
    }

    /**
     * Metodo que se encarga de leer un XML y transformarlo en una lista de ContenedoresDTO
     *
     * @param path el directorio donde se encuentra el archivo que vamos a leer
     * @return Devuelve una lista de ContenedoresDTO
     */

    fun xmlToContenedorDTO(path:String):List<ContenedoresDTO>{
        val xmlContenedor = XML {indentString = "  "}
        val fichero = File(path)
        return xmlContenedor.decodeFromString(fichero.readText())
    }












}