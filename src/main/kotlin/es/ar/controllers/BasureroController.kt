package es.ar.controllers

import es.ar.mappers.ContenedoresMapper
import es.ar.mappers.ContenedoresMapper.contenedoresToContenedoresDTO
import es.ar.mappers.ContenedoresMapper.mapToContenedor
import es.ar.mappers.ResiduosMapper
import es.ar.mappers.ResiduosMapper.mapToResiduo
import es.ar.mappers.ResiduosMapper.residuosToResiduosDTO
import es.ar.models.Contenedores
import es.ar.models.Residuos
import es.ar.utils.esCSVContenedores
import es.ar.utils.esCSVResiduos
import es.ar.utils.validarDirectorio
import es.ar.utils.validarExtension
import jetbrains.datalore.base.values.Color
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.dataframe.io.html
import org.jetbrains.letsPlot.Geom
import org.jetbrains.letsPlot.Stat.identity
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomBar
import org.jetbrains.letsPlot.geom.geomMap
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.label.labs
import org.jetbrains.letsPlot.letsPlot
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.exists

/**
 * Basurero controller
 *
 *
 */
class BasureroController {
    private val dir: String = System.getProperty("user.dir")



    /**
     * Función que parsea los archivos csv en csv, json y xml
     *
     * @param pathOrigen Lugar de los ficheros a parsear
     * @param pathFinal Lugar donde se parsearan los ficheros
     */
    fun programaParser(pathOrigen: String, pathFinal: String) {
        if (esCSVResiduos(pathOrigen + File.separator + "modelo_residuos_2021.csv") && esCSVContenedores(pathOrigen + File.separator + "contenedores_varios.csv")) {
            val listaResiduos = ResiduosMapper.csvReaderToResiduo( pathOrigen + File.separator + "modelo_residuos_2021.csv" )
            val listaResiduosDTO = listaResiduos.map { it.residuosToResiduosDTO() }
            ResiduosMapper.residuoToCSV(pathFinal, listaResiduosDTO)
            ResiduosMapper.residuoToJson(pathFinal, listaResiduosDTO)
            ResiduosMapper.residuoToXML(pathFinal, listaResiduosDTO)
            val listaContenedores =
                ContenedoresMapper.csvReaderToContenedores(pathOrigen + File.separator + "contenedores_varios.csv")
            val listaContenedoresDTO = listaContenedores.map { it.contenedoresToContenedoresDTO() }
            ContenedoresMapper.contenedorToCSV(pathFinal, listaContenedoresDTO)
            ContenedoresMapper.contenedorToJson(pathFinal, listaContenedoresDTO)
            ContenedoresMapper.contenedorToXML(pathFinal, listaContenedoresDTO)
        } else {
            throw Exception("Archivo no válido")
        }
    }

    /**
     * Función que realiza todas las consultas pedidas
     *
     * @param pathOrigen path de los archivos
     * @param pathFinal path donde se guardaran los resultados
     */
    fun programaResumen(pathOrigen: String, pathFinal: String) {
        val pathResiduos =  pathOrigen + File.separator + "modelo_residuos_2021.csv"
        val pathContenedores =  pathOrigen + File.separator + "contenedores_varios.csv"
        if(validarExtension(pathResiduos) && validarExtension(pathContenedores)){
            val tiempoInicial = System.currentTimeMillis()
            val listaResiduos = ResiduosMapper.csvReaderToResiduo(pathResiduos).toDataFrame()
            val listaContenedores = ContenedoresMapper.csvReaderToContenedores(pathContenedores).toDataFrame()
            // Número de contenedores de cada tipo que hay en cada distrito.
            numContenedoresByTipoByDistrito(listaContenedores)
            // Media de contenedores de cada tipo que hay en cada distrito.
            mediaContenedoresByTipoByDistrito(listaContenedores)
            //Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por distrito.
            mediaToneladasByTipoByDistrito(listaResiduos)
            //Gráfico de media de toneladas mensuales de recogida de basura por distrito.
            graficoMediaToneladasDistrito(listaResiduos, pathFinal)
            //Máximo, mínimo, media y desviación de toneladas anuales de recogidas por cada tipo de basura agrupadas por distrito.
            estadisticasByTipoByDistrito(listaResiduos)
            //Suma de to do lo recogido en un año por distrito.
            sumaByDistrito(listaResiduos)
            //Por cada distrito obtener para cada tipo de residuo la cantidad recogida.
            cantidadRecogidaByTipoByDistrito(listaResiduos)
            //Gráfico con el total de contenedores por distrito.
            graficoTotalContenedoresDistrito(listaContenedores, pathFinal)
            val tiempoFinal = System.currentTimeMillis()
            println("El tiempo de ejecución es de: ${(tiempoFinal - tiempoInicial)} milisegundos ")
        }else{
            println("La extensión no es válida")
        }

    }

    private fun graficoTotalContenedoresDistrito(listaContenedores: DataFrame<Contenedores>, pathFinal: String) {
        val consulta = listaContenedores
            .groupBy("distrito", "type_Contenedor")
            .aggregate { sum("cantidad") into "TotalContenedores" }.toMap()

        val fig: Plot = letsPlot(data = consulta) + geomBar(
            stat = identity,
            alpha = 0.6,
            fill = Color.BLUE,
            color = Color.RED
        ) {
            x = "distrito"
            y = "TotalContenedores"
        } + labs(
            x = "Nombre del distrito",
            y = "Número total de contenedores",
            title = "Total de contenedores por distrito"
        )

        val path = pathFinal + File.separator + "images"
        if (!Paths.get(path).exists()) {
            Files.createDirectory(Paths.get(pathFinal + File.separator + "images" + File.separator))
        }

        ggsave(fig, path = path + File.separator, filename = "contenedores_distritos.png")
    }

    private fun graficoMediaToneladasDistrito(listaResiduos: DataFrame<Residuos>, pathDestino: String) {
        val consulta = listaResiduos
            .groupBy("nombre_distrito", "month")
            .aggregate { mean("toneladas") into "media" }.toMap()

        val fig: Plot = letsPlot(data = consulta) + geomBar(
            stat = identity,
            alpha = 0.6,
            fill = Color.ORANGE,
            color = Color.BLACK
        ) {
            x = "nombre_distrito"
            y = "media"
        } + labs(
            x = "Nombre distrito",
            y = "Media de basura recogida",
            title = "Media de toneladas mensuales de basura por distrito."
        )
        val path = pathDestino + File.separator + "images"
        if (!Paths.get(path).exists()) {
            Files.createDirectory(Paths.get(pathDestino + File.separator + "images" + File.separator))
        }
        ggsave(fig, path = path + File.separator, filename = "media_toneladas_distrito.png")
    }

    private fun cantidadRecogidaByTipoByDistrito(listaResiduos: DataFrame<Residuos>): String {
        return listaResiduos
                .groupBy("nombre_distrito", "residuos")
                .aggregate {
                    sum("toneladas") into "Total_Toneladas"
                }.sortBy("nombre_distrito").html()
    }

    private fun sumaByDistrito(listaResiduos: DataFrame<Residuos>): String {
        return listaResiduos
                .groupBy("nombre_distrito")
                .aggregate { sum("toneladas") into "Toneladas_Totales" }
                .sortBy("nombre_distrito").html()
    }

    private fun estadisticasByTipoByDistrito(listaResiduos: DataFrame<Residuos>): String {
        return listaResiduos
                .groupBy("nombre_distrito", "residuos", "year")
                .aggregate {
                    min("toneladas") into "Mínimo_Toneladas"
                    max("toneladas") into "Máximo_Toneladas"
                    mean("toneladas") into "Media_Toneladas"
                    std("toneladas") into "Desviación_Toneladas"
                }.sortBy("nombre_distrito").html()
    }

    private fun mediaToneladasByTipoByDistrito(listaResiduos: DataFrame<Residuos>): String {
        return listaResiduos
                .groupBy("nombre_distrito", "residuos", "year")
                .aggregate { mean("toneladas") into "Media_Toneladas" }
                .sortBy("nombre_distrito").html()
    }

    private fun mediaContenedoresByTipoByDistrito(listaContenedores: DataFrame<Contenedores>): String {
        return listaContenedores
                .groupBy("distrito", "type_Contenedor")
                .mean().sortBy("distrito").html()
    }

    private fun numContenedoresByTipoByDistrito(listaContenedores: DataFrame<Contenedores>): String {
        return listaContenedores
                .groupBy("distrito", "type_Contenedor")
                .count().sortBy("distrito").html()
    }

    /**
     * Programa resumen distrito
     *
     * @param pathOrigen path de donde se sacarán los archivos
     * @param pathFinal path donde se generará el informe
     * @param distrito nombre del distrito a realizar las consultas
     */
    //TODO CAMBIAR FICHEOR A LOS 3 POSIBLES
    fun programaResumenDistrito(pathOrigen: String, pathFinal: String, distrito: String) {
        val tiempoInicial = System.currentTimeMillis()
        val listaResiduos = parserFicherosResiduos(pathOrigen)
        val listaContenedores = parserFicherosContenedores(pathOrigen)
        val distrito2 = distrito[0].uppercaseChar() + distrito.slice(1 until distrito.length).lowercase(Locale.getDefault());
        if(listaResiduos["nombre_distrito"].toList().toString().contains(distrito2)){
            // Número de contenedores de cada tipo que hay en el distrito dado
            numContenedoresByTipoDistrito(listaContenedores, distrito)
            //Total de toneladas recogidas en ese distrito por residuo.
            totalToneladasByResiduoDistrito(listaResiduos, distrito2)
            // Gráfico con el total de toneladas por residuo en ese distrito.
            graficoTotalToneladasResiduoDistrito(listaResiduos, distrito2, pathFinal)
            //Máximo, mínimo, media y desviación por mes por residuo en dicho distrito.
            estadisticaByMesByResiduo(listaResiduos, distrito2)
            // Gráfica del máximo, mínimo y media por meses en dicho distrito.
            graficoMaxMinMediaPorMeses(listaResiduos, distrito2, pathFinal)


        }else{
            println("El distrito no existe")
        }
    }

    private fun parserFicherosContenedores(pathOrigen: String): DataFrame<Contenedores> {
        return if(pathOrigen.endsWith(".csv")){
            ContenedoresMapper.csvReaderToContenedores(pathOrigen + File.separator + "contenedores_varios.csv").toDataFrame()
        }else if(pathOrigen.endsWith(".json")){
            ContenedoresMapper.jsonToContenedor(pathOrigen + File.separator + "contenedor.json").map { mapToContenedor(it.toString())}.toDataFrame()
        }else if(pathOrigen.endsWith(".xml")){
            ContenedoresMapper.xmlToContenedorDTO(pathOrigen + File.separator + "contenedor.xml").map { mapToContenedor(it.toString()) }.toDataFrame()
        }else{
            throw Exception("Archivo no válido")
        }

    }


    private fun parserFicherosResiduos(pathOrigen: String): DataFrame<Residuos> {
        return if(pathOrigen.endsWith(".csv")){
            ResiduosMapper.csvReaderToResiduo(pathOrigen + File.separator + "modelo_residuos_2021.csv").toDataFrame()
        }else if(pathOrigen.endsWith(".json")){
            ResiduosMapper.jsonToResiduoDTO(pathOrigen + File.separator + "residuos.json").map { mapToResiduo(it.toString()) }.toDataFrame()
        }else if(pathOrigen.endsWith(".xml")){
            ResiduosMapper.xmlToResiduoDTO(pathOrigen + File.separator + "residuos.json").map { mapToResiduo(it.toString()) }.toDataFrame()
        }else{
            throw Exception("Archivo no válido")
        }
    }



    private fun graficoMaxMinMediaPorMeses(listaResiduos: DataFrame<Residuos>, distrito2: String, pathFinal: String) {
        val res = listaResiduos.filter { it["nombre_distrito"] == distrito2 }
            .groupBy("nombre_distrito", "month")
            .aggregate {
                max("toneladas") into "Máximo"
                min("toneladas") into "Mínimo"
                mean("toneladas") into "Media"
            }.toMap()

        val fig: Plot = letsPlot(data = res)  + geomBar(
            stat = identity,
            alpha = 0.6,
            fill = Color.DARK_BLUE,
            color = Color.DARK_GREEN
        ) {
            x = "month"
            y = "Mínimo"
        }+ geomBar(
            stat = identity,
            alpha = 0.6,
            fill = Color.YELLOW,
            color = Color.DARK_GREEN
        ) {
            x = "month"
            y = "Media"
        } + geomBar(
            stat = identity,
            alpha = 0.6,
            fill = Color.RED,
            color = Color.DARK_GREEN
        ) {
            x = "month"
            y = "Máximo"
        } + labs(
            x = "Mes",
            y = "Total",
            title = "Máximo, mínimo y media por meses."
        )
        val path = pathFinal + File.separator + "images"
        if (!Paths.get(path).exists()) {
            Files.createDirectory(Paths.get(pathFinal + File.separator + "images" + File.separator))
        }
        ggsave(fig, path = path + File.separator, filename = "media_min_max_mensual_$distrito2.png")
    }

    private fun graficoTotalToneladasResiduoDistrito(listaResiduos: DataFrame<Residuos>, distrito2: String, pathFinal: String) {
        val res = listaResiduos
            .filter { it["nombre_distrito"] == distrito2 }
            .groupBy("residuos", "toneladas")
            .aggregate {
                count() into "totalToneladas"
            }
            .toMap()

        val fig: Plot = letsPlot(data = res) + geomMap(
            stat = identity,
            alpha = 0.8,
            fill = Color.BLACK,
            color = Color.BLACK,
        ) {
            x = "residuos"
            y = "totalToneladas"
        } + labs(
            x = "Tipo de residuo",
            y = "Toneladas",
            title = "Total de toneladas por residuo en $distrito2"
        )

        val path = pathFinal + File.separator + "images"
        if (!Paths.get(path).exists()) {
            Files.createDirectory(Paths.get(pathFinal + File.separator + "images" + File.separator))
        }
        ggsave(fig, path = path + File.separator, filename = "toneladas_tipo_$distrito2.png")
    }
    private fun estadisticaByMesByResiduo(listaResiduos: DataFrame<Residuos>, distrito2: String) {
        listaResiduos
            .filter { it["nombre_distrito"] == distrito2 }
            .groupBy("month", "nombre_distrito", "residuos")
            .aggregate {
                max("toneladas") into "Máximo_Toneladas"
                min("toneladas") into "Mínimo_Toneladas"
                mean("toneladas") into "Media_Toneladas"
                std("toneladas").toString().replace("NaN", "0") into "Desviación_Toneladas"
            }.html()
    }

    private fun totalToneladasByResiduoDistrito(listaResiduos: DataFrame<Residuos>, distrito2: String): String {
        return listaResiduos
            .filter { it["nombre_distrito"] == distrito2 }
            .groupBy("nombre_distrito", "residuos")
            .aggregate { sum("toneladas") into "Toneladas_Totales" }
            .sortBy("nombre_distrito").html()
    }

    private fun numContenedoresByTipoDistrito(listaContenedores: DataFrame<Contenedores>, distrito: String): String {
        return listaContenedores
            .filter { it["distrito"] == distrito.uppercase(Locale.getDefault()) }
            .groupBy("distrito", "type_Contenedor")
            .count().sortBy("distrito").html()

    }

    /**
     * Comprueba segun los argumentos de la consola dados
     *
     * @param args argumentos de la consola
     * @return la opcion elegida
     */
    fun comprobarPrograma(args: Array<String>): String {
        if (args.size < 2 || args.size > 5) {
            throw Exception("Argumentos no válidos")
        }
        if (args[0] == "parser") {
            val pathOrigen = args[1]
            val pathFinal = args[2]
            if ( validarDirectorio(pathOrigen, pathFinal)) { //&&
                return "Parsear"
            }
        }
        else if (args[0].lowercase(Locale.getDefault()) == "resumen" && args.size == 3) {
            val pathOrigen = args[1]
            val pathFinal = args[2]
            if (validarDirectorio(pathOrigen, pathFinal)) {
                return "Resumen"
            } else {
                throw Exception("Extensión no válida")
            }
        } else if (args[0].lowercase(Locale.getDefault()) == "resumen" && args.size == 4) {
            val pathOrigen = args[2]
            val pathFinal = args[3]
            if (validarDirectorio(pathOrigen, pathFinal)) {
                return "ResumenDistrito"
            } else {
                throw Exception("Extensión no válida")
            }
        }
        throw Exception("Argumentos no válidos")
    }



}