package es.ar.utils

import org.junit.jupiter.api.Assertions.*
import java.io.File

internal class FicherosKtTest {

    @org.junit.jupiter.api.Test
    fun esCSVResiduos() {
        val cabeceraOriginal = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"
        val cabeceraModificada = "Año;Mes;Residuo;Lote;Nombre_Distrito;Toneladas"
        val cabeceraBien = "Año;Mes;Lote;Residuo;Distrito;Nombre Distrito;Toneladas"
        val cabeceraBien2 = "Año;Mes;Residuo;Lote;Nombre_Distrito;Toneladas"
        val cabeceraMal = ""
        val cabeceraMal2 = "Year;mes;abc"
        val cabeceraMal3 = "Año;Mes;Residuo;Lote;Nombre Distrito;Toneladas"

        assertAll(
            { assertEquals(cabeceraOriginal, cabeceraBien) },
            { assertEquals(cabeceraModificada, cabeceraBien2) },
            { assertNotEquals(cabeceraOriginal, cabeceraMal) },
            { assertNotEquals(cabeceraOriginal, cabeceraMal2) },
            { assertNotEquals(cabeceraOriginal, cabeceraMal3) }
        )
    }

    @org.junit.jupiter.api.Test
    fun esCSVContenedores() {
        val cabeceraOriginal = "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;COORDENADA Y;LONGITUD;LATITUD;DIRECCION"
        val cabeceraModificada = "CodigoInterno;Lote;Tipo_Contendor;Distrito;Cantidad"
        val cabeceraBien1 = "Código Interno del Situad;Tipo Contenedor;Modelo;Descripcion Modelo;Cantidad;Lote;Distrito;Barrio;Tipo Vía;Nombre;Número;COORDENADA X;COORDENADA Y;LONGITUD;LATITUD;DIRECCION"
        val cabeceraBien2 = "CodigoInterno;Lote;Tipo_Contendor;Distrito;Cantidad"
        val cabeceraMal = ""
        val cabeceraMal2 = "Código Interno del Situad"
        val cabeceraMal3 = "CodigoInterno;Lote;Tipo Contendor;Distrito;Cantidad"
        val cabeceraMal4 = ";a;a;a;a;a;a"

        assertAll(
            { assertEquals(cabeceraOriginal, cabeceraBien1) },
            { assertEquals(cabeceraModificada, cabeceraBien2) },
            { assertNotEquals(cabeceraOriginal, cabeceraMal) },
            { assertNotEquals(cabeceraOriginal, cabeceraMal2) },
            { assertNotEquals(cabeceraOriginal, cabeceraMal3) },
            { assertNotEquals(cabeceraOriginal, cabeceraMal4) }
        )
    }

    @org.junit.jupiter.api.Test
    fun validarExtension() {
        val extensionCSV = ".csv"
        val extensionJSON = ".json"
        val extensionXML = ".xml"
        val archivoBueno = "prueba.csv"
        val archivoBueno2 = "prueba2.json"
        val archivoBueno3 = "prueba3.xml"
        val archivoMalo1 = "prueba1.cvs"
        val archivoMalo2 = "prueba2.xlm"
        val archivoMalo3 = "prueba3.josn"
        val archivoMalo4 = "prueba4"
        val archivoMalo5 = ""
        assertAll(
            { assertTrue(archivoBueno.endsWith(extensionCSV))},
            { assertTrue(archivoBueno2.endsWith(extensionJSON))},
            { assertTrue(archivoBueno3.endsWith(extensionXML))},
            { assertFalse(archivoMalo1.endsWith(extensionCSV))},
            { assertFalse(archivoMalo2.endsWith(extensionXML))},
            { assertFalse(archivoMalo3.endsWith(extensionJSON))},
            { assertFalse(archivoMalo4.endsWith(extensionCSV))},
            { assertFalse(archivoMalo5.endsWith(extensionJSON))},
        )

    }

    @org.junit.jupiter.api.Test
    fun validarDirectorio() {
        val directorioExiste = System.getProperty("user.dir") + File.separator + "data"
        val directorioExiste2 = System.getProperty("user.dir") + File.separator + "metadata"
        val directorioNoExiste = "/prueba/datos"

        assertAll(
            { assertTrue(File(directorioExiste).exists())},
            { assertTrue(File(directorioExiste2).exists())},
            { assertFalse(File(directorioNoExiste).exists())},


        )

    }
}