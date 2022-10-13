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
import org.jetbrains.letsPlot.intern.Plot
import java.io.File
import java.util.*
import kotlin.math.nextUp

class BasureroController {
    private val dir: String = System.getProperty("user.dir")
    private val path = dir + File.separator + "data" + File.separator

    //TODO Controlar que el archivo csv es cualquier nombre pero el contenido es igual que en Ficheros.kt
    fun programaParser(pathOrigen: String, pathFinal: String){
        if(esCSVResiduos(pathOrigen)){
            val listaResiduos= ResiduosMapper.csvReaderToResiduo(path + pathOrigen )
            val listaResiduosDTO= listaResiduos.map { it.residuosToResiduosDTO() }
            ResiduosMapper.residuoToCSV(pathFinal, listaResiduosDTO)
            ResiduosMapper.residuoToJson(pathFinal, listaResiduosDTO)
            ResiduosMapper.residuoToXML(pathFinal, listaResiduosDTO)
        }else if(esCSVContenedores(pathOrigen)){
            val listaContenedores = ContenedoresMapper.csvReaderToContenedores(pathOrigen + File.separator + "contenedores_varios.csv")
            val listaContenedoresDTO = listaContenedores.map { it.contenedoresToContenedoresDTO() }
            ContenedoresMapper.contenedorToCSV(pathFinal, listaContenedoresDTO)
            ContenedoresMapper.contenedorToJson(pathFinal, listaContenedoresDTO)
            ContenedoresMapper.contenedorToXML(pathFinal, listaContenedoresDTO)
        }else{
            throw Exception("Archivo no válido")
        }
    }

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
        //TODO Realizar las consultas
        // Número de contenedores de cada tipo que hay en cada distrito.
        listaContenedores
            .groupBy("distrito", "type_Contenedor")
            .count().sortBy("distrito").print()

        //Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por distrito.
        listaResiduos
            .groupBy("nombre_distrito", "residuos", "year")
            .aggregate {mean("toneladas") into "Media_Toneladas"}
            .sortBy("nombre_distrito").print()

        //Máximo, mínimo, media y desviación de toneladas anuales de recogidas por cada tipo de basura agrupadas por distrito.
        listaResiduos
            .groupBy("nombre_distrito", "residuos", "year")
            .aggregate {
                min("toneladas") into "Mínimo_Toneladas"
                max("toneladas")into "Máximo_Toneladas"
                mean("toneladas") into "Media_Toneladas"
                std("toneladas") into "Desviación_Toneladas"
            }.sortBy("nombre_distrito").print()

        //Suma de todo lo recogido en un año por distrito.
        listaResiduos
            .groupBy("nombre_distrito")
            .aggregate { sum("toneladas") into "Toneladas_Totales" }
            .sortBy("nombre_distrito").print()

        //Por cada distrito obtener para cada tipo de residuo la cantidad recogida.
        listaResiduos
            .groupBy("nombre_distrito", "residuos")
            .aggregate{
                sum("toneladas") into "Total_Toneladas"
            }.sortBy("nombre_distrito").print()

        val tiempoFinal = System.currentTimeMillis()
        println("El tiempo de ejecución es de: ${(tiempoFinal - tiempoInicial)/1000} segundos ")
    }

    fun programaResumenDistrito(pathOrigen: String, pathFinal: String, distrito: String) {
        //TODO Realizar las consultas
        //TODO Comprobar que el distrito exista
        val tiempoInicial = System.currentTimeMillis()
        val listaResiduos =
            ResiduosMapper.csvReaderToResiduo(dir + File.separator + pathOrigen + File.separator + "modelo_residuos_2021.csv")
                .toDataFrame().cast<Residuos>()
        val listaContenedores =
            ContenedoresMapper.csvReaderToContenedores(dir + File.separator + pathOrigen + File.separator + "contenedores_varios.csv")
                .toDataFrame()
        // Número de contenedores de cada tipo que hay en el distrito dado
        listaContenedores
            .filter { it["distrito"] == distrito.uppercase(Locale.getDefault()) }
            .groupBy("distrito", "type_Contenedor")
            .count().sortBy("distrito").print()

        listaResiduos.schema().print()
        //Total de toneladas recogidas en ese distrito por residuo.
        listaResiduos
            .filter { it["nombre_distrito"] == distrito }
            .groupBy("nombre_distrito", "residuos")
            .aggregate { sum("toneladas") into "Toneladas_Totales" }
            .sortBy("nombre_distrito").print()


    }

}