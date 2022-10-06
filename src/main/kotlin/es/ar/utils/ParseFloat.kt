package es.ar.utils
fun stringToFloat(valor: String): Float{
        val res = valor.replace(",", ".")
        return res.toFloat()
}