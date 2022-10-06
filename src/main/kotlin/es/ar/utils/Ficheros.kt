package es.ar.utils

import java.io.File


fun esCSVResiduos(path: String): Boolean{
    val cabeceraOriginal = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"
    val cabeceraModificada = "Año;Mes;Residuo;Lote;Nombre_Distrito;Toneladas"
    val linea = File(path + "residuos.csv").readLines().take(1).toString().replace("[", "").replace("]", "")
    return linea == cabeceraOriginal || linea == cabeceraModificada
}


fun esCSVContenedores(file: File): Boolean {
return false
}

fun validarExtension(path_origen: String): Boolean {
return path_origen.endsWith(".csv") || path_origen.endsWith(".json") || path_origen.endsWith(".xml")
}

fun validarDirectorio(path_origen: String, path_final: String): Boolean {
    return File(path_origen).exists() && File(path_final).exists()
}
