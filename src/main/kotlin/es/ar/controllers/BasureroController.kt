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
     * en la cual tenemos que coger la informacion de los contenedores y de la recodiga
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

    /**
     *  Metodo que se encarga de ejecutar la primera parte de la segunda consulta del ejercicio que se trata de RESUMEN
     * en la cual tenemos que coger la informacion de los contenedores y de la recodiga, independientemente de la extensión que tenga
     * y la procesamos generando.
     * Este metodo hara todas las consultas que necesitamos para mas tarde generar las graficas y el resumen.html
     *
     * @param pathOrigen Es el directorio origen
     * @param pathFinal Es el directorio donde se guarda los datos conseguidos con el metodo
     */
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

        //TODO  - Media de contenedores de cada tipo que hay en cada distrito. - Gráfico con el total de contenedores por distrito.

        //Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por distrito.
        listaResiduos
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
        println("El tiempo de ejecución es de: ${(tiempoFinal - tiempoInicial) / 1000} segundos ")
    }

    /**
     * Metodo que se encarga de ejecutar la primera parte de la segunda consulta del ejercicio que se trata de RESUMEN DISTRITO
     * en la cual tenemos que coger la informacion de los contenedores y de la recodiga, independientemente de la extensión que tenga
     * y la procesamos generando.
     * Este metodo hara todas las consultas que necesitamos para más tarde generar las gráficas y el resumenDistrito.html
     *
     * @param pathOrigen  Es el directorio origen
     * @param pathFinal  Es el directorio donde se guarda los datos conseguidos con el metodo
     * @param distrito Es la variable que se utilizara para sacar el resumen sobre el distrito que sea pasado
     */
    fun programaResumenDistrito(pathOrigen: String, pathFinal: String, distrito: String) {
        //TODO Realizar las consultas
        //TODO Comprobar que el distrito exista
        val tiempoInicial = System.currentTimeMillis()
        val listaResiduos =
            ResiduosMapper.csvReaderToResiduo(dir + File.separator + pathOrigen + File.separator + "modelo_residuos_2021.csv")
                .toDataFrame()
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
        //Máximo, mínimo , media y desviación por mes por residuo en dicho distrito.

        listaResiduos
            .filter { it["nombre_distrito"] == distrito }
            .groupBy("month", "nombre_distrito", "residuos")
            .aggregate {
                max("toneladas") into "Máximo_Toneladas"
                min("toneladas") into "Mínimo_Toneladas"
                mean("toneladas") into "Media_Toneladas"
                std("toneladas").toString().replace("NaN", "0") into "Desviación_Toneladas"
            }.print()


    }
//TODO no funciona las graficas
    //Número de contenedores de cada tipo que hay en cada distrito.
    /**
     * Metodo que consiste en crear una grafica de contenedores por distrito con ggplot
     * @param pathOrigen  Es el directorio origen
     * @param pathFinal Es el directorio donde se guarda el grafico creado
     */
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


        var grafico = ggplot(ayuda.toMap()) +
                geomBar { x = "type";fill = "type" } +
                labs(
                    x = "distrito",
                    y = "type_Contenedor",
                    title = "Grafico de número de contenedores de cada tipo que hay en cada distrito. "
                )

        ggsave(grafico, "prueba.png")

    }
/*
    /**
     * Metodo que se encarga de hacer el template html de Resumen
     *
     * @return un html con el resumen
     */
    fun resumenTemplate(): String {
        return """ <!doctype html>
        <html lang="en">
        <head>
             <meta charset="utf-8">
             <title>Resumen de recogidas de basura y reciclaje de Madrid</title>
             <meta name="viewport" content="width=device-width, initial-scale=1">
             <style type="text/css" href="/css/main.css">
             </style>
        </head>

    <body>
        <h1>Resumen de recogidas de basura y reciclaje de Madrid </h1></n>
        <hr/><hr/>
        <p>Este resumen se ha generado a las $time<br/>
           Autores:Alejandro López Abad y Ruben </p>
        <h3>Este es el resumen de Contenedores y Residuos</h3>  <!--hay que hacer contenedores-->

        <h4>Consultas</h4>
        <p>Se van a resolver las siguientes consultas: </p>
           <ol>
             <li>Número de contenedores de cada tipo que hay en cada distrito</li>
          
             <li> Media de contenedores de cada tipo que hay en cada distrito</li>
             <li> Media de toneladas anuales de recogidas por cada tipo de basura agrupadas por
                 distrito.</li>
             <li> Máximo, mínimo , media y desviación de toneladas anuales de recogidas por cada tipo
                 de basura agrupadas por distrito.
             </li>
             <li>Suma de todo lo recogido</li>
             <li> Por cada distrito obtener para cada tipo de residuo la cantidad recogida.</li>
           </ol>

            <hr/>
            <hr/>

        <table style="text-align: center;width: 100%;" border="1" cellpadding="2" cellspacing="2">

            <tr>
                <th>Consulta</th> <!--1-->
                <th>Resultado</th>
                <th>Grafica</th>
            </tr>

            <tr>
               <td><h5>1</h5></td><!--1-->
               <td>$numcontenedoresTipoByDistrito</td><!--2-->
               <td>$grafica1</td><!--3-->
            </tr>

            <tr>
               <td><h5>2</h5></td><!--2-->
               <td>$mediacontenedoresPorTipo</td><!--2-->
               <td>$grafica2</td><!--3-->
            </tr>
            <tr>
               <td><h5>3</h5></td><!--3-->
               <td>$mediaTonPorTipoByDistrito</td><!--2-->
               <td>$grafica3</td><!--3-->
            </tr>
             <tr>
                <td><h5>4</h5></td><!--4-->
                <td>
                    Max $max</br>Min $min</br>Media $medi</br>Desviacion $std
                </td><!--2-->
                <td>$grafica4</td><!--3-->
             </tr>
             <tr>
                <td><h5>5</h5></td><!--3-->
                <td>$sumaTotal </td><!--2-->
                <td>$grafica5</td><!--3-->
            </tr>
            <tr>
               <td><h5>6</h5></td><!--3-->
               <td>$toneladasResiduosByDIstritoByResiduo</td><!--2-->
               <td>$grafica6</td><!--3-->
            </tr>
        </table>

    </body>

</html>"""

    }
    */

/*
    /**
     *
     * Metodo que se encarga de hacer el template html de un resumen por distrito
     *
     * @return un html con el resumen por distrito
     */
    fun distritoResumentemplate(): String {
        return """  <!doctype html>
        <html lang="en">
        <head>
             <meta charset="utf-8">
             <title>Resumen de recogidas de basura y reciclaje de Madrid</title>
             <meta name="viewport" content="width=device-width, initial-scale=1">
             <style type="text/css" href="/css/main.css">
             </style>
        </head>

    <body>
        <h1>Resumen de recogidas de basura y reciclaje de Madrid </h1></n>
        <hr/><hr/>
        <p>Este resumen se ha generado a las $time<br/>
           Autores:Alejandro López Abad y Ruben </p>
        <h3>Este es el resumen de $Distrito</h3>

        <h4>Consultas</h4>
        <p>Se van a resolver las siguientes consultas: </p>
           <ol>
             <li>Media de contenedores de cada tipo que hay en cada distrito</li>
             <li> Total de toneladas recogidas en ese distrito por residuo</li>
             <li> Máximo, mínimo , media y desviación por mes por residuo </li>
           </ol>

            <hr/>
            <hr/>

        <table style="text-align: center;width: 100%;" border="1" cellpadding="2" cellspacing="2">

            <tr>
              <th>Consulta</th> 
              <th>Resultado</th>
              <th>Grafica</th>
            </tr>

            <tr>
                <td><h5>1</h5></td>
                <td>>$numContenedoresByTipoByDistrito</td>
                <td>$grafica1</td>
            </tr>
            <tr>
                <td><h5>2</h5></td><!--2-->
                <td>$numtotaltoneladasByDistritoByresiduo</td><!--2-->
                <td>$grafica2</td><!--3-->
            </tr>
            <tr>
                <td><h5>3</h5></td><!--3-->
                <td>  Max $max</br>Min $min</br>Media $medi</br>Desviacion $std

                    <p>por mes por residuos en dicho distrito</p>
                </td>     
                <td>$grafica3</td><!--3-->
            </tr>
          
           
       </table>

    </body>

</html>"""

    }
*/

}