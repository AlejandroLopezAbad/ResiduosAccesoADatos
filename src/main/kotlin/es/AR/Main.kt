package es.AR

import es.AR.mappers.ResiduosMapper
import es.AR.models.enums.Lote
import java.io.File

fun main (args: Array<String>){

    val pathResiduos: String = System.getProperty("user.dir")+ File.separator+"data"+File.separator+"modelo_residuos_2021.csv"

    //println(ResiduosMapper().csvReaderToResiduo(path))
    val lista= ResiduosMapper().csvReaderToResiduo(pathResiduos)

    println(lista.filter { it.lote == Lote.UNO })


}