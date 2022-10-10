package es.ar.controllers

import es.ar.mappers.ContenedoresMapper
import es.ar.mappers.ContenedoresMapper.contenedoresToContenedoresDTO
import es.ar.mappers.ResiduosMapper
import es.ar.mappers.ResiduosMapper.residuosToResiduosDTO
import es.ar.models.enums.TipoContendor
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import java.io.File
import java.util.*

class BasureroController {
    private val dir: String = System.getProperty("user.dir")
    private val path = dir + File.separator + "data" + File.separator
    fun programaParser(pathOrigen: String, pathFinal: String){
        if(pathOrigen.lowercase(Locale.getDefault()).contains("residuo")){
            val listaResiduos= ResiduosMapper.csvReaderToResiduo(path + pathOrigen )
            val listaResiduosDTO= listaResiduos.map { it.residuosToResiduosDTO() }
            ResiduosMapper.residuoToCSV(pathFinal, listaResiduosDTO)
            ResiduosMapper.residuoToJson(pathFinal, listaResiduosDTO)
            ResiduosMapper.residuoToXML(pathFinal, listaResiduosDTO)
        }else if(pathOrigen.lowercase(Locale.getDefault()).contains("contenedor")){
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
            val listaResiduos = ResiduosMapper.csvReaderToResiduo(dir + File.separator + pathOrigen  + File.separator +"modelo_residuos_2021.csv")
            val listaContenedores = ContenedoresMapper.csvReaderToContenedores(dir + File.separator + pathOrigen  + File.separator + "contenedores_varios.csv")
            //TODO Realizar las consultas

    }

    fun programaResumenDistrito(pathOrigen: String, pathFinal: String, distrito: String) {
        //TODO Realizar las consultas y comprobar distrito
    }

}