package es.ar.mappers

import es.ar.dto.ContenedoresDTO
import es.ar.dto.ResiduosDTO
import es.ar.mappers.ContenedoresMapper.dtoToContenedores
import es.ar.models.Contenedores
import es.ar.models.enums.TipoContendor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.serialization.XML
import utils.ParseTipo
import java.io.File
import java.nio.file.Files
import java.nio.file.Path


object ContenedoresMapper {


    fun ContenedoresDTO.dtoToContenedores(): Contenedores {
        return Contenedores(
            codigo_Interno = this.codigo_Interno,
            type_Contenedor = TipoContendor.valueOf(this.type_Contenedor),
            cantidad = this.cantidad,
            lote = ParseTipo().stringLoteToTypeLote(this.lote),
            distrito = this.distrito
        )
    }


    fun Contenedores.contenedoresToContenedoresDTO(): ContenedoresDTO {
        return ContenedoresDTO(
            codigo_Interno = this.codigo_Interno,
            type_Contenedor = this.type_Contenedor.name,
            cantidad = this.cantidad,
            lote = this.lote.name,
            distrito = this.distrito
        )
    }

    fun csvReaderToContenedores(path:String):List<Contenedores>{
        return Files.lines(Path.of(path))
            .skip(1)
            .map { mapToContenedor(it) }.toList()
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

    fun contenedorToCSV(path:String, lista:List<ContenedoresDTO>){
        val file = File(path + "contenedor.csv")
        file.writeText("CodigoInterno;Lote;Tipo_Contendor;Distrito;Cantidad")
        lista.forEach {
            file.appendText("\n${it.codigo_Interno};${it.lote};${it.type_Contenedor};${it.distrito};${it.cantidad};")
        }
    }

    /**
     * TODO
     *
     * @param path
     * @param residuosDto
     */
    fun contenedorToJson(path:String, contenedorDto: List<ContenedoresDTO>) {
        val file = File(path + "contenedor.json")
        val json = Json { prettyPrint = true }
        file.writeText(json.encodeToString(contenedorDto))
    }

    /**
     * TODO
     *
     * @param path
     * @param residuosDto
     */
    fun contenedorToXML(path:String, contenedorDto: List<ContenedoresDTO>) {
        val xml = XML { indentString = "  " }
        val fichero = File(path + "contenedor.xml")
        fichero.writeText(xml.encodeToString(contenedorDto))
    }



}