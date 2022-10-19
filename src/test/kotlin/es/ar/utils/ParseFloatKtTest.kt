package es.ar.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ParseFloatKtTest {

    @Test
    fun stringToFloat() {
        val valor = "12,34"
        val res = stringToFloat(valor)
        assertEquals(12.34f, res)
        assertAll(
            { assertEquals(12.34f, stringToFloat(valor)) },
            { assertNotEquals(12.12, stringToFloat(valor)) }
        )

    }
}