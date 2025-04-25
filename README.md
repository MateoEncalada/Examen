# Proyecto de Integración de Resultados de Exámenes - BioNet

Este repositorio contiene la solución desarrollada para el caso práctico de la empresa **BioNet**, donde se integran resultados de exámenes provenientes de diferentes laboratorios en una base de datos central mediante Apache Camel y MySQL.

---

## Estructura del Proyecto

```
BioNet-Integracion/
├── demo/                         # Proyecto Spring Boot con Apache Camel
│   ├── input-labs/              # Carpeta de entrada con archivos CSV
│   ├── output/
│   │   ├── processed/           # Archivos válidos procesados
│   │   └── error/               # Archivos con errores de formato
│   └── src/                     # Código fuente Java
├── doc/                         # Documentación del análisis
```

---

## Tecnologías utilizadas
- Java 17
- Spring Boot 3.4.4
- Apache Camel 4.10.2
- MySQL 8.0
- VS Code

---

## Contenido

###  Código fuente
Ruta: `demo/src/main/java/com/taller/demo/`

Contiene:
- `LabResultsRoute.java` → Ruta Camel que procesa y clasifica archivos.
- `ResultadoExamenProcessor.java` → Procesador que inserta datos en la base de datos.

### Scripts SQL
```
   USE bionet;

CREATE TABLE IF NOT EXISTS resultados_examenes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    laboratorio_id VARCHAR(50),
    paciente_id VARCHAR(50),
    tipo_examen VARCHAR(100),
    resultado TEXT,
    fecha_examen DATE,
    UNIQUE KEY unique_resultado (paciente_id, tipo_examen, fecha_examen)
);
log_cambios_resultadosCREATE TABLE IF NOT EXISTS log_cambios_resultados (
    id INT AUTO_INCREMENT PRIMARY KEY,
    operacion VARCHAR(10),
    paciente_id VARCHAR(50),
    tipo_examen VARCHAR(100),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DELIMITER $$

CREATE TRIGGER trigger_log_inserttrigger_log_insert
AFTER INSERT ON resultados_examenes
FOR EACH ROW
BEGIN
    INSERT INTO log_cambios_resultados (operacion, paciente_id, tipo_examen)
    VALUES ('INSERT', NEW.paciente_id, NEW.tipo_examen);
END$$

DELIMITER ;
```
### ¿Qué hace el trigger `trigger_log_insert`?

El trigger `trigger_log_insert` se ejecuta automáticamente después de cada inserción en la tabla `resultados_examenes`. Su función es **registrar en la tabla `log_cambios_resultados`** información clave de cada nuevo examen ingresado, incluyendo:

- El tipo de operación (`INSERT`)
- El ID del paciente
- El tipo de examen
- La fecha y hora del registro

Esto permite **mantener un historial de auditoría**, esencial para la trazabilidad y control de cambios en entornos clínicos donde la integridad de los datos es crítica.

### Documento PDF de análisis
Puedes consultar el informe formal aquí:

[📎 Informe_BioNet_Integracion.pdf](./doc/Informe_BioNet_Integracion.pdf)

### Capturas de ejecución funcional
### Carpetas de entrada y salida

#### input-labs/
Esta carpeta contiene los archivos `.csv` que representan resultados de exámenes generados por los laboratorios. Aquí se colocan todos los archivos a procesar, ya sea válidos o con errores. El sistema Apache Camel monitorea esta carpeta continuamente.

![image](https://github.com/user-attachments/assets/1d5c8483-c5e9-4023-9793-b7221f2b9c94)
#### output/
Una vez procesados, los archivos se clasifican automáticamente:

- `output/processed/`: aquí se mueven los archivos válidos que fueron insertados exitosamente en la base de datos.
- `output/error/`: aquí se almacenan los archivos que tienen errores de formato, encabezado incompleto o problemas estructurales.

Esta organización permite trazabilidad y control de calidad en la integración.

![image](https://github.com/user-attachments/assets/4abfe54d-15b4-4d02-a4a9-f750bcbfb5ac)

### Antes de iniciar el programa
![image](https://github.com/user-attachments/assets/e1338f78-49b4-4082-9631-f3c269de355b)
### Despues
![image](https://github.com/user-attachments/assets/5932de60-cdd2-4e02-8128-36591515adee)
## Estructura de carpetas
![image](https://github.com/user-attachments/assets/c5972142-7151-4a27-8dc2-8c7427905e4d)
### Resultados en la Base de Datos

A continuación se muestran las evidencias de cómo los archivos `.csv` procesados impactan directamente en la base de datos MySQL:
#### Inserciones válidas en `resultados_examenes`

Registros insertados correctamente a partir de archivos con datos completos y válidos.
#### Contenido del csv
![image](https://github.com/user-attachments/assets/2b7c1b1e-820a-40e6-8548-b7c3bf1d61e3)
#### Base de Datos
![image](https://github.com/user-attachments/assets/7463c7ca-d078-4260-b101-35844ec55a91)
![image](https://github.com/user-attachments/assets/f7ec150a-5f11-4b9e-805f-8f2f408b97cd)
#### Manejo de archivos duplicados (por `INSERT IGNORE`)

Los registros duplicados son ignorados sin causar errores ni interrupciones. Esto evita inserciones innecesarias y garantiza integridad.

#### Contenido del csv
![image](https://github.com/user-attachments/assets/31075e6e-6696-48e8-b768-845a3f3c7600)

#### Base de Datos
![image](https://github.com/user-attachments/assets/7da2a4b4-e3fb-44f1-8d5e-c7685167a707)
![image](https://github.com/user-attachments/assets/b32652a5-01a6-49cb-a0ee-3ed972b6367c)

#### Archivos con contenidos incompletos

Al procesar un archivo con una línea incompleta, el sistema sigue procesando las líneas válidas y omite las que están mal.
#### Contenido del csv
![image](https://github.com/user-attachments/assets/b6495d14-0cdb-43a3-a898-a0cbc5a11579)
#### Base de Datos
![image](https://github.com/user-attachments/assets/b714652e-7696-4cf4-ad28-8e61918e85a7)
![image](https://github.com/user-attachments/assets/7c9bbed3-c5e3-46e8-a7e0-30b427bd03c8)

#### Archivo con errores
Al procesar una rchivo con erroes no entrara a la base de datos y pasara a la carpeta output/errores
![image](https://github.com/user-attachments/assets/737aad2a-9788-4724-b7d9-255e22031105)

### Logs de Consola
![image](https://github.com/user-attachments/assets/4606c435-fe02-4ebf-ae1a-9bc68a29a16c)


---

## Instrucciones de uso

1. Ejecutar los scripts de `sql/` en MySQL Workbench.
2. Colocar archivos `.csv` en `input-labs/`.
3. Ejecutar el proyecto con `./mvnw spring-boot:run` dentro de la carpeta `demo/`.
4. Observar los resultados en la base de datos y en consola.

---

## Estado del proyecto
Completado y funcional. Listo para presentación o despliegue.

---

## Autor
Mateo Encalada
