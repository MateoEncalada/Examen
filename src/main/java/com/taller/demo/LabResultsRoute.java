package com.taller.demo;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class LabResultsRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Obtener la ruta base donde se ejecuta el proyecto
        String basePath = System.getProperty("user.dir");

        // Construir rutas absolutas para entrada y salida
        String inputPath = "file:" + basePath + "/demo/input-labs?include=.*\\.csv&noop=true";
        String outputProcessed = "file:" + basePath + "/demo/output/processed";
        String outputError = "file:" + basePath + "/demo/output/error";
        from("timer:pruebaConexion?repeatCount=1")
        .setBody(constant("SELECT 1"))
        .to("jdbc:dataSource")
        .log("✅ Conexión a la base de datos exitosa: ${body}");

        from(inputPath)
            .log("📂 Archivo detectado: ${file:name}")
            .process(exchange -> {
                String estadoArchivo = "error"; // Por defecto, si algo falla

                try {
                    String body = exchange.getIn().getBody(String.class);
                    String[] lines = body.split("\n");

                    if (lines.length >= 2) {
                        String header = lines[0].toLowerCase().trim();
                        String firstDataLine = lines[1].toLowerCase().trim();

                        if (header.contains("paciente_id") && header.contains("tipo_examen") && header.contains("resultado")) {
                            // Validación básica del contenido mínimo necesario
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
                .otherwise()
                    .log("❌ Archivo inválido. Enviado a 'error'")
                    .to(outputError)
            .end()
            .log("🔄 Transferencia finalizada para: ${file:name}");
    }
}

