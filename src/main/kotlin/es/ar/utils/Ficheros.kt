package es.ar.utils

import java.io.File
/**
 * @author Alejandro Lopez Abad y Ruben Garcia-Redondo Marin
 */


/**
 * Comprueba si el path del archivo de tipo csv es del modelo correspondiente a residuos
 *
 * @param path Ruta del archivo
 * @return Devuelve True si el contenido es correcto y false si el contenido es incorrecto
 */
fun esCSVResiduos(path: String): Boolean{
    val cabeceraOriginal = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"
    val cabeceraModificada = "Año;Mes;Residuo;Lote;Nombre_Distrito;Toneladas"
    var linea = File(path + File.separator + "modelo_residuos_2021.csv").readLines().first()
    val re = Regex("[^A-Za-z0-9;._ñ ]")
    linea = re.replace(linea, "")
    return linea.equals(cabeceraOriginal)  || linea.equals(cabeceraModificada)
}

/**
 * Comprueba si el path del archivo de tipo csv es del modelo correspondiente a contenedores
 *
 * @param path Ruta del archivo
 * @return Devuelve True si el contenido es correcto y False si el contenido es incorrecto
 */
fun esCSVContenedores(path: String): Boolean {
    val cabeceraOriginal = "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;COORDENADA Y;LONGITUD;LATITUD;DIRECCION"
    val cabeceraModificada = "CodigoInterno;Lote;Tipo_Contendor;Distrito;Cantidad"
    var linea = File(path + File.separator + "contenedores_varios.csv").readLines().first().toString()
    val re = Regex("[^A-Za-z0-9;._ñáéíóú ]")
    linea = re.replace(linea, "")
    return linea.equals(cabeceraOriginal)  || linea.equals(cabeceraModificada)

}

/**
 * Comprueba si la ruta del archivo es del tipo ".csv", ".json" o ".xml"
 *
 * @param path_origen La ruta del archivo
 * @return Devuelve True si es el tipo de archivo correcto o False si es el tipo de archivo incorrecto
 */
fun validarExtension(path_origen: String): Boolean {
return path_origen.endsWith(".csv") || path_origen.endsWith(".json") || path_origen.endsWith(".xml")
}


/**
 * Comprueba que la ruta dada existe
 *
 * @param path_origen Ruta del archivo origen
 * @param path_final Ruta del archivo final
 * @return True si la ruta existe y False si la ruta no existe
 */
fun validarDirectorio(path_origen: String, path_final: String): Boolean {
    return File(path_origen).exists() && File(path_final).exists()
}
