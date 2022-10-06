package es.ar


import es.ar.mappers.ContenedoresMapper.contenedorToCSV
import es.ar.mappers.ContenedoresMapper.contenedorToJson
import es.ar.mappers.ContenedoresMapper.contenedorToXML
import es.ar.mappers.ContenedoresMapper.contenedoresToContenedoresDTO
import es.ar.mappers.ContenedoresMapper.csvReaderToContenedores
import es.ar.mappers.ResiduosMapper
import es.ar.mappers.ResiduosMapper.csvReaderToResiduo
import es.ar.mappers.ResiduosMapper.residuoToCSV
import es.ar.mappers.ResiduosMapper.residuoToJson
import es.ar.mappers.ResiduosMapper.residuoToXML
import es.ar.mappers.ResiduosMapper.residuosToResiduosDTO
import es.ar.utils.esCSVResiduos


import java.io.File

fun main (args: Array<String>){

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

}