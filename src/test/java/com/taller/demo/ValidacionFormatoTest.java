package com.taller.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidacionFormatoTest {

    @Test
    public void testSeparadorCorrecto() {
        // Valida que los campos estén separados por punto y coma
        String linea = "1001;GLUCOSA;88.2";
        assertEquals(3, linea.split(";").length);
    }

    @Test
    public void testFormatoIncorrecto() {
        // Valida un error común (coma como separador)
        String linea = "1001,GLUCOSA,88.2";
        assertNotEquals(3, linea.split(";").length);
    }
}

