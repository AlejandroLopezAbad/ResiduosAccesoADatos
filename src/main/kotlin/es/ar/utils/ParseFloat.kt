package es.ar.utils
/**
 * @author Alejandro Lopez Abad y Ruben Garcia-Redondo Marin
 */

/**
 * Convierte una cantidad de tipo String en Float
 *
 * @param valor -> Valor a cambiar
 * @return La cantidad transformada
 */
fun stringToFloat(valor: String): Float{
        val res = valor.replace(",", ".")
        return res.toFloat()
}