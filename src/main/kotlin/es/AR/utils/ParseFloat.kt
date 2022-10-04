package es.AR.utils

class ParseFloat() {

    fun stringToFloat(valor: String): Float{
        val res = valor.replace(",", ".")
        return res.toFloat()
    }
}