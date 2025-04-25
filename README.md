# Proyecto de Integración de Resultados de Exámenes - BioNet

Este repositorio contiene la solución desarrollada para el caso práctico de la empresa **BioNet**, donde se integran resultados de exámenes provenientes de diferentes laboratorios en una base de datos central mediante Apache Camel y MySQL.

---

## 🔗 Estructura del Proyecto

```
BioNet-Integracion/
├── demo/                         # Proyecto Spring Boot con Apache Camel
│   ├── input-labs/              # Carpeta de entrada con archivos CSV
│   ├── output/
│   │   ├── processed/           # Archivos válidos procesados
│   │   └── error/               # Archivos con errores de formato
│   └── src/                     # Código fuente Java
├── sql/                         # Scripts SQL de creación de base y tablas
├── doc/                         # Documentación del análisis
└── capturas/                    # Evidencias de ejecución funcional
```

---

## 👨‍💻 Tecnologías utilizadas
- Java 17
- Spring Boot 3.4.4
- Apache Camel 4.10.2
- MySQL 8.0
- VS Code

---

## 📄 Contenido

### 🔬 1. Código fuente
Ruta: `demo/src/main/java/com/taller/demo/`

Contiene:
- `LabResultsRoute.java` → Ruta Camel que procesa y clasifica archivos.
- `ResultadoExamenProcessor.java` → Procesador que inserta datos en la base de datos.

### 📃 2. Scripts SQL
Ruta: `sql/`

- `bionet_schema.sql` → Crea la base de datos `bionet`, las tablas `resultados_examenes` y `log_cambios_resultados`, y su trigger de auditoría.

### 📜 3. Documento PDF de análisis
Ruta: `doc/`

- `Informe_BioNet_Integracion.pdf` → Documento formal con análisis del problema, patrones usados y diseño de la solución.

### 📷 4. Capturas de ejecución funcional
Ruta: `capturas/`

- Evidencias de procesamiento de archivos.
- Estructura de carpetas.
- Resultados en base de datos.
- Logs en consola Camel.

---

## ⚙️ Instrucciones de uso

1. Ejecutar los scripts de `sql/` en MySQL Workbench.
2. Colocar archivos `.csv` en `input-labs/`.
3. Ejecutar el proyecto con `./mvnw spring-boot:run` dentro de la carpeta `demo/`.
4. Observar los resultados en la base de datos y en consola.

---

## 🎉 Estado del proyecto
✅ Completado y funcional. Listo para presentación o despliegue.

---

## 📍 Autor
Mateo Encalada
