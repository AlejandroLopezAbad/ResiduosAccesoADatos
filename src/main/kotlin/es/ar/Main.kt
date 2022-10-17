package es.ar

import es.ar.controllers.BasureroController
import es.ar.models.Bitacora
import es.ar.utils.validarDirectorio
import es.ar.utils.validarExtension

import java.io.File
import java.util.*

fun main (args: Array<String>){
    val controller: BasureroController = BasureroController()
    val pathResiduos2: String = System.getProperty("user.dir")+ File.separator+"data"+File.separator
    var exito = true;
    try {
        when(controller.comprobarPrograma(args)) {
            "Parsear" -> controller.programaParser(args[1], args[2])
            "Resumen" -> controller.programaResumen(args[1], args[2])
            "ResumenDistrito" -> controller.programaResumenDistrito(args[2], args[3], args[4])
        }
    }catch (e:Exception) {
        e.printStackTrace()
        exito = false
    }finally {
        Bitacora("parser", exito, System.currentTimeMillis(), pathResiduos2)
    }
}






