package com.taller.demo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class LabResultsRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Ruta base del sistema
        String basePath = System.getProperty("user.dir");
        String inputPath = "file:" + basePath + "/demo/input-labs?include=.*\\.csv&noop=true";
        String outputProcessed = "file:" + basePath + "/demo/output/processed";
        String outputError = "file:" + basePath + "/demo/output/error";

        // 🔍 Ruta de prueba de conexión a la base
        from("timer:pruebaConexion?repeatCount=1")
            .setBody(constant("SELECT 1"))
            .to("jdbc:dataSource")
            .log("✅ Conexión a la base de datos exitosa: ${body}");

        // 📥 Ruta principal para procesar archivos CSV
        from(inputPath)
            .log("📂 Archivo detectado: ${file:name}")
            .process(exchange -> {
                String estadoArchivo = "error"; // Por defecto, si algo falla

                try {
                    String body = exchange.getIn().getBody(String.class);
                    String[] lines = body.split("\n");

                    if (lines.length >= 2) {
                        String header = lines[0].toLowerCase().trim();

                        if (header.contains("paciente_id") && header.contains("tipo_examen") && header.contains("resultado")) {
                            estadoArchivo = "processed";
                        }
                    }
                } catch (Exception e) {
                    // estadoArchivo se mantiene como "error"
                }

                exchange.setProperty("estadoArchivo", estadoArchivo);
            })
            .choice()
    .when(simple("${exchangeProperty.estadoArchivo} == 'processed'"))
        .log("✅ Archivo válido. Enviado a 'processed'")
        .to(outputProcessed)
        .process(new ResultadoExamenProcessor())
        .split(body())
        .to("jdbc:dataSource")
        .log("📝 Registro insertado en BD.")
    .endChoice()  
    .otherwise()
        .log("❌ Archivo inválido. Enviado a 'error'")
        .to(outputError)
.end()
            .log("🔄 Transferencia finalizada para: ${file:name}");
    }
}
