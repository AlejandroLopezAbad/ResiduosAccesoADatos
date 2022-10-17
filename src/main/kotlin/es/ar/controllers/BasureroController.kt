package es.ar.controllers

import es.ar.mappers.ContenedoresMapper
import es.ar.mappers.ContenedoresMapper.contenedoresToContenedoresDTO
import es.ar.mappers.ResiduosMapper
import es.ar.mappers.ResiduosMapper.residuosToResiduosDTO
import es.ar.models.Contenedores
import es.ar.models.Residuos
import es.ar.utils.esCSVContenedores
import es.ar.utils.esCSVResiduos
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.letsPlot.LetsPlot
import org.jetbrains.letsPlot.Stat.identity
import org.jetbrains.letsPlot.export.ggsave
import org.jetbrains.letsPlot.geom.geomBar
import org.jetbrains.letsPlot.ggplot
import org.jetbrains.letsPlot.intern.Plot
import org.jetbrains.letsPlot.label.labs
import org.jetbrains.letsPlot.letsPlot
import java.io.File
import java.util.*
import kotlin.math.nextUp

class BasureroController {
    private val dir: String = System.getProperty("user.dir")
    private val path = dir + File.separator + "data" + File.separator

    //TODO Controlar que el archivo csv es cualquier nombre pero el contenido es igual que en Ficheros.kt
    /*
     * Metodo que se encarga de ejecutar la primera consulta del ejercicio que se trata de PARSER
     * en la cual tenemos que coger la informacion de los contenedores y de la recodiga , idependientemente de la
     * extension que tenga el archivo
     */
    fun programaParser(pathOrigen: String, pathFinal: String) {
        if (esCSVResiduos(pathOrigen)) {
            val listaResiduos = ResiduosMapper.csvReaderToResiduo(path + pathOrigen)
            val listaResiduosDTO = listaResiduos.map { it.residuosToResiduosDTO() }
            ResiduosMapper.residuoToCSV(pathFinal, listaResiduosDTO)
            ResiduosMapper.residuoToJson(pathFinal, listaResiduosDTO)
            ResiduosMapper.residuoToXML(pathFinal, listaResiduosDTO)
        } else if (esCSVContenedores(pathOrigen)) {
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

    //TODO Hacer gráficas
    fun programaResumen(pathOrigen: String, pathFinal: String) {
        val tiempoInicial = System.currentTimeMillis()
        val listaResiduos =
            ResiduosMapper.csvReaderToResiduo(dir + File.separator + pathOrigen + File.separator + "modelo_residuos_2021.csv")
                .toDataFrame()
        val listaContenedores =
            ContenedoresMapper.csvReaderToContenedores(dir + File.separator + pathOrigen + File.separator + "contenedores_varios.csv")
                .toDataFrame()
        listaResiduos.schema().print()
        listaContenedores.schema().print()
        // Número de contenedores de cada tipo que hay en cada distrito.
        listaContenedores
            .groupBy("distrito", "type_Contenedor")
            .count().sortBy("distrito").print()

        //Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por distrito.
        var prueba = listaResiduos
            .groupBy("nombre_distrito", "residuos", "year")
            .aggregate { mean("toneladas") into "Media_Toneladas" }
            .sortBy("nombre_distrito").print()

        //Máximo, mínimo, media y desviación de toneladas anuales de recogidas por cada tipo de basura agrupadas por distrito.
        listaResiduos
            .groupBy("nombre_distrito", "residuos", "year")
            .aggregate {
                min("toneladas") into "Mínimo_Toneladas"
                max("toneladas") into "Máximo_Toneladas"
                mean("toneladas") into "Media_Toneladas"
                std("toneladas") into "Desviación_Toneladas"
            }.sortBy("nombre_distrito").print()

        //Suma de to do lo recogido en un año por distrito.
        listaResiduos
            .groupBy("nombre_distrito")
            .aggregate { sum("toneladas") into "Toneladas_Totales" }
            .sortBy("nombre_distrito").print()


        //Por cada distrito obtener para cada tipo de residuo la cantidad recogida.
        listaResiduos
            .groupBy("nombre_distrito", "residuos")
            .aggregate {
                sum("toneladas") into "Total_Toneladas"
            }.sortBy("nombre_distrito").print()

        val tiempoFinal = System.currentTimeMillis()
        println("El tiempo de ejecución es de: ${(tiempoFinal - tiempoInicial)} milisegundos ")
    }

    fun programaResumenDistrito(pathOrigen: String, pathFinal: String, distrito: String) {
        //TODO Comprobar que el distrito exista
        val tiempoInicial = System.currentTimeMillis()
        val listaResiduos =
            ResiduosMapper.csvReaderToResiduo(dir + File.separator + pathOrigen + File.separator + "modelo_residuos_2021.csv")
                .toDataFrame().cast<Residuos>()
        val listaContenedores =
            ContenedoresMapper.csvReaderToContenedores(dir + File.separator + pathOrigen + File.separator + "contenedores_varios.csv")
                .toDataFrame()
        val distrito2 = distrito[0].uppercaseChar() + distrito.slice(1 until distrito.length).lowercase(Locale.getDefault());
        if(listaResiduos.get("nombre_distrito").toList().toString().contains(distrito2)){
            // Número de contenedores de cada tipo que hay en el distrito dado
            listaContenedores
                .filter { it["distrito"] == distrito.uppercase(Locale.getDefault()) }
                .groupBy("distrito", "type_Contenedor")
                .count().sortBy("distrito").print()

            listaResiduos.schema().print()
            //Total de toneladas recogidas en ese distrito por residuo.
            listaResiduos
                .filter { it["nombre_distrito"] == distrito2 }
                .groupBy("nombre_distrito", "residuos")
                .aggregate { sum("toneladas") into "Toneladas_Totales" }
                .sortBy("nombre_distrito").print()
            //Máximo, mínimo , media y desviación por mes por residuo en dicho distrito.

            listaResiduos
                .filter { it["nombre_distrito"] == distrito2 }
                .groupBy("month", "nombre_distrito", "residuos")
                .aggregate {
                    max("toneladas") into "Máximo_Toneladas"
                    min("toneladas") into "Mínimo_Toneladas"
                    mean("toneladas") into "Media_Toneladas"
                    std("toneladas").toString().replace("NaN", "0") into "Desviación_Toneladas"
                }.print()

        }else{
            println("El distrito no existe")
        }



    }
//TODO no funciona las graficas
    //Número de contenedores de cada tipo que hay en cada distrito.
    /*
    fun crearGraficoContenedoresporDistrito(pathOrigen: String, pathFinal: String) {
        val listaResiduos =
            ResiduosMapper.csvReaderToResiduo(dir + File.separator + pathOrigen + File.separator + "modelo_residuos_2021.csv")
                .toDataFrame().cast<Residuos>()
        val listaContenedores =
            ContenedoresMapper.csvReaderToContenedores(dir + File.separator + pathOrigen + File.separator + "contenedores_varios.csv")
                .toDataFrame()

        var ayuda = listaContenedores
            .groupBy("distrito", "type_Contenedor")
            .count().sortBy("distrito")


        var grafico = ggplot(ayuda.toMap())+
                geomBar{x = "type";fill="type"}+
                labs(
                    x="distrito",
                    y="type_Contenedor",
                    title="Grafico de número de contenedores de cada tipo que hay en cada distrito. "
                )

            ggsave(grafico,"prueba.png")

    }*/




}