package com.taller.demo;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResultadoExamenProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String contenido = exchange.getIn().getBody(String.class);
        String[] lineas = contenido.split("\n");

        List<String> inserts = new ArrayList<>();

        // Saltar encabezado (línea 0)
        for (int i = 1; i < lineas.length; i++) {
            String[] columnas = lineas[i].split(",");

            if (columnas.length < 5) continue; // validar estructura mínima

            String laboratorioId = columnas[0].trim();
            String pacienteId = columnas[1].trim();
            String tipoExamen = columnas[2].trim();
            String resultado = columnas[3].trim();
            String fecha = columnas[4].trim();

            String sql = String.format(
                "INSERT IGNORE INTO resultados_examenes (laboratorio_id, paciente_id, tipo_examen, resultado, fecha_examen) " + //La palabra ignonre se utiliza para evitar que se dupliquen los registros
                "VALUES ('%s', '%s', '%s', '%s', '%s')",
                laboratorioId, pacienteId, tipoExamen, resultado, fecha
            );

            inserts.add(sql);
        }

        exchange.getIn().setBody(inserts);
    }
}
