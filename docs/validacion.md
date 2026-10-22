# Verificación de la versión integrada: ejercicios 1 a 4

- Compilación completa con OpenJDK 17.0.20 en Linux, UTF-8 y `-Xlint:all`: sin errores ni advertencias.
- **32 comprobaciones anteriores**: cupos, inscripciones, costos, filtros genéricos, certificados y persistencia.
- **48 comprobaciones de tickets e hilos**: clase anidada miembro no estática y constructor privado; rechazo antes de confirmar; un solo ticket incluso ante ocho emisores concurrentes; códigos distintos para inscripciones distintas; persistencia de códigos y referencias a la inscripción recuperada; envío en otro hilo; exclusión de inscripciones sin confirmar o sin ticket; respuesta a interrupciones; nombres y tipos de los atributos del diagrama, método `enviarTicket(): void` y firma de tres parámetros de `crearActividad`.
- **22 comprobaciones adicionales de ejercicios 1, 2 y 3**: datos inválidos sin modificar las colecciones, certificados solo en tipos certificables, wildcard con listas del evento y rechazo de actividades ajenas, archivos corruptos o incompletos y archivos serializados con un tipo de objeto incorrecto. Total: **102 comprobaciones**.
- **Ejecución integral de App**: tres eventos, siete actividades, 28 inscripciones aceptadas, siete rechazos por cupo, 14 inscripciones confirmadas y 14 sin confirmar.
- Los 14 códigos emitidos se recuperaron de los archivos y se enviaron una vez por el hilo correspondiente. El conjunto de códigos enviados coincidió con el conjunto de códigos emitidos.
- Se comprobó que hay mensajes de `main` entre envíos de tickets y que el programa espera al final el cierre de todos los hilos.
- La ejecución integral se hizo mediante `build.sh` desde otra carpeta.
- Se comprobaron tres mensajes de `finally`, un rechazo controlado por dato inválido y 13 certificados de talleres o cursos.
- En otra ejecución se hizo inaccesible el archivo del primer evento: `App` capturó el fallo, ejecutó los tres `finally`, recuperó los otros dos eventos y terminó normalmente con sus 10 tickets enviados.

La configuración del proyecto sigue siendo IntelliJ/JDK 21. Esta nueva versión fue verificada aquí con Java 17; corresponde ejecutarla también en el IntelliJ local y subir una captura de sus dos flujos a GitHub.
