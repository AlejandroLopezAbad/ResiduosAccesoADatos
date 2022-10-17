package es.ar

import es.ar.controllers.BasureroController
import es.ar.models.Bitacora
import es.ar.utils.validarDirectorio
import es.ar.utils.validarExtension

import java.io.File
import java.util.*

fun main (args: Array<String>){
    val controller: BasureroController = BasureroController()
    val pathResiduos: String = System.getProperty("user.dir")+ File.separator+"data"+File.separator+"modelo_residuos_2021.csv"
    val pathResiduos2: String = System.getProperty("user.dir")+ File.separator+"data"+File.separator
    val pathResiduos3: String = System.getProperty("user.dir")+ File.separator+"data"+File.separator+"residuos.csv"
    val pathContenedores: String = System.getProperty("user.dir")+ File.separator+"data" + File.separator+"contenedores_varios.csv"

    when(comprobarPrograma(args)) {
        "Parsear" -> controller.programaParser(args[1], args[2])
        "Resumen" -> controller.programaResumen(args[1], args[2])
        "ResumenDistrito" -> controller.programaResumenDistrito(args[2], args[3], args[4])
    }
    Bitacora("parser", true, System.currentTimeMillis(), pathResiduos2)

}

fun comprobarPrograma(args: Array<String>): String {
    if (args.size < 2 || args.size > 5) {
        throw Exception("Argumentos no válidos")
    }
    //TODO Decidir si el argumento nos da igual que lo meta en mayuscula o minuscula o solo en minuscula
    //TODO Intentar optimizar este codigo
    if (args[0].lowercase(Locale.getDefault()) == "parser") {
        val pathOrigen = args[1]
        val pathFinal = args[2]
        //TODO Cambiar para que coja directorio
        if ( validarDirectorio(pathOrigen, pathFinal)) { //&&
            return "Parsear"
        }
    }
    else if (args[0].lowercase(Locale.getDefault()) == "resumen" && args.size == 3) {
        val pathOrigen = args[1]
        val pathFinal = args[2]
        if (validarDirectorio(pathOrigen, pathFinal) && validarExtension(pathOrigen)) {
            return "Resumen"
        } else {
            throw Exception("Extensión no válida")
        }
        //TODO Comprobar que el distrito funcione
    } else if (args[0].lowercase(Locale.getDefault()) == "resumen" && args[1].lowercase(Locale.getDefault()) == "distrito") {
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




