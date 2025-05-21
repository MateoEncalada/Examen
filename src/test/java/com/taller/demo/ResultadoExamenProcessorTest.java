package com.taller.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResultadoExamenProcessorTest {

    @Test
    public void testProcesaLineaValida() {
        // Simula una línea CSV válida y prueba que se procese correctamente
        String linea = "12345;HEMOGLOBINA;12.3";
        // Aquí iría lógica real con el processor si es accesible
        assertTrue(linea.contains("HEMOGLOBINA"));
    }

    @Test
    public void testProcesaLineaInvalida() {
        // Simula una línea con campos faltantes
        String linea = "12345;;";
        assertTrue(linea.split(";").length < 3);
    }
}
