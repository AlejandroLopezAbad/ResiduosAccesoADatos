package es.AR


import es.AR.mappers.ResiduosMapper.csvReaderToResiduo

import es.AR.mappers.ResiduosMapper.residuosToResiduosDTO

import java.io.File

fun main (args: Array<String>){

    val pathResiduos: String = System.getProperty("user.dir")+ File.separator+"data"+File.separator+"modelo_residuos_2021.csv"

    val pathxmlResiduos:String=System.getProperty("user.dir")+ File.separator+"data"
    //println(ResiduosMapper().csvReaderToResiduo(path))
    val lista= csvReaderToResiduo(pathResiduos)
    val patata= lista.map { it.residuosToResiduosDTO() }
    println(patata)
 //   var patata2= residuoDTOToxml(pathxmlResiduos,patata)
   // println(lista)
    println("ME CAGO EN TU PUTA MADRE" )
    //println(lista.filter { it.lote == Lote.UNO })


}