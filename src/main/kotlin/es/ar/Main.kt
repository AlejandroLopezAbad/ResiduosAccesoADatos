package es.ar


import es.ar.utils.validarDirectorio
import es.ar.utils.validarExtension

fun main (args: Array<String>){
    
    /*
    val pathResiduos: String = System.getProperty("user.dir")+ File.separator+"data"+File.separator+"modelo_residuos_2021.csv"
    val pathContenedores: String = System.getProperty("user.dir")+ File.separator+"data" + File.separator+"contenedores_varios.csv"
    val pathResiduos2: String = System.getProperty("user.dir")+ File.separator+"data"+File.separator
    val listaResiduos= csvReaderToResiduo(pathResiduos)
    val listaContenedores = csvReaderToContenedores(pathContenedores)
    val listaResiduosDTO= listaResiduos.map { it.residuosToResiduosDTO() }
    val listaContenedoresDTO = listaContenedores.map { it.contenedoresToContenedoresDTO() }
    residuoToCSV(pathResiduos2, listaResiduosDTO)
    residuoToJson(pathResiduos2,listaResiduosDTO)
    residuoToXML(pathResiduos2, listaResiduosDTO)
    contenedorToCSV(pathResiduos2, listaContenedoresDTO)
    contenedorToJson(pathResiduos2,listaContenedoresDTO)
    contenedorToXML(pathResiduos2, listaContenedoresDTO)
     */

}

fun comprobarPrograma(args: Array<String>): String {
    if (args.size < 2 || args.size >= 5) {
        throw Exception("Argumentos no válidos")
    }
    val argMax = args.size
    //TODO Decidir si el argumento nos da igual que lo meta en mayuscula o minuscula o solo en minuscula
    //TODO Intentar optimizar este codigo
    if (args[0] == "parser") {
        val pathOrigen = args[1]
        val pathFinal = args[2]
        if (validarExtension(pathOrigen)) {
            return "Parsear"
        }
    } else if (args[0] == "resumen" && args.size == 3) {
        val pathOrigen = args[1]
        val pathFinal = args[2]
        if (validarDirectorio(pathOrigen, pathFinal) && validarExtension(pathOrigen)) {
            return "Parsear"
        } else {
            throw Exception("Extensión no válida")
        }
    } else if (args[0] == "resumen" && args[1] == "distrito") {
        val pathOrigen = args[2]
        val pathFinal = args[3]
        if (validarDirectorio(pathOrigen, pathFinal) && validarExtension(pathOrigen)) {
            return "Parsear"
        } else {
            throw Exception("Extensión no válida")
        }
    }
    throw Exception("Argumentos no válidos")
}




