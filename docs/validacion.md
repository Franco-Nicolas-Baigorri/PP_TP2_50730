# Verificación de la adaptación

- Compilación completa en Linux con OpenJDK 17.0.20, codificación UTF-8 y `-Xlint:all`: sin errores ni advertencias.
- 32 comprobaciones de comportamiento: listas vacías, cupo completo sin alterar inscripciones, relaciones de inscripción, filtros genéricos, costos, certificados, eventos gratuitos, guardado y recuperación.
- La serialización conservó estudiantes, actividades, inscripciones, referencias compartidas y referencias circulares.
- La recuperación no incrementó el contador de eventos y un archivo inexistente produjo la excepción esperada.
- Ejecución completa de `App`: tres eventos recuperados, 28 inscripciones aceptadas y siete intentos rechazados por cupo.
- `build.sh` ejecutado desde otra carpeta y desde una ruta con espacios.
- Ante un error de compilación, `build.sh` terminó con error y no ejecutó clases anteriores.
- Configuración de IntelliJ revisada como XML y ajustada a JDK 21.

La comprobación ejecutable se hizo con Java 17. El script de Windows y la apertura con IntelliJ/JDK 21 requieren verificación en ese entorno; no se ejecutaron aquí.
