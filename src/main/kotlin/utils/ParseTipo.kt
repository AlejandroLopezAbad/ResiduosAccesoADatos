package utils

import es.AR.models.enums.Lote

class ParseTipo() {

    fun stringLoteToTypeLote(campo: String): Lote {
        var type: Lote = Lote.UNO
        when (campo) {
            "1" -> type = Lote.UNO
            "2" -> type = Lote.DOS
            "3" -> type = Lote.TRES
        }
        return type
    }



}