package es.ar

import es.ar.controllers.BasureroController
import es.ar.models.Bitacora
import es.ar.utils.validarDirectorio
import es.ar.utils.validarExtension

import java.io.File
import java.util.*
/**
 * @author Alejandro Lopez Abad y Ruben Garcia-Redondo Marin
 */

fun main (args: Array<String>){
    val controller = BasureroController()
    var exito = true;
    try {
        when(controller.comprobarPrograma(args)) {
            "Parsear" -> controller.programaParser(args[1], args[2])
            "Resumen" -> controller.programaResumen(args[1], args[2])
            "ResumenDistrito" -> controller.programaResumenDistrito(args[2], args[3], args[1])
        }
    }catch (e:Exception) {
        println(e.printStackTrace())
        exito = false
    }finally {
        Bitacora(controller.comprobarPrograma(args), exito, System.currentTimeMillis(), args[2]).crearBitacora()
    }
}






