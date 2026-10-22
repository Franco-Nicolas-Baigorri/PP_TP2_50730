# PP_TP2_50730 — Eventos universitarios

**Adaptación y revisión:** Franco Nicolás Baigorri

**Legajo:** 50730

**Materia:** Paradigmas de Programación

**Entorno de trabajo:** IntelliJ IDEA y JDK 21.

Aplicación de consola para gestionar eventos universitarios, sus salas, actividades e inscripciones. Demuestra herencia, polimorfismo, una interfaz de certificación, excepciones chequeadas, serialización, métodos genéricos, clases anidadas miembro e hilos.

## Ejecución

Se necesita un **JDK**, con `java` y `javac` disponibles en la terminal. El proyecto de IntelliJ está configurado para JDK 21; el código también es compatible con Java 17.

### IntelliJ IDEA

1. Descomprimir el ZIP y abrir la carpeta `PP_TP2_50730` como proyecto.
2. Seleccionar el JDK 21 instalado en la configuración del proyecto.
3. Abrir `src/App.java` y ejecutar su método `main`.

La carpeta `events` se crea al guardar el primer evento, incluso si no se usan los scripts.

### Windows

Abrir una terminal dentro de la carpeta del proyecto. En CMD:

```bat
build.bat
```

En PowerShell:

```powershell
.\build.bat
```

### Linux o macOS

```bash
bash build.sh
```

Los scripts compilan en UTF-8 dentro de `cls` y ejecutan `App`. Cada ejecución vuelve a generar los datos de demostración y sobrescribe los tres archivos de eventos.

## Qué demuestra el programa

La versión integra los **ejercicios 1, 2, 3 y 4**. La correspondencia con cada consigna se detalla en [docs/revision_ejercicios.md](docs/revision_ejercicios.md).

| Ejercicio | Implementación |
| --- | --- |
| 1 | Paquetes, excepción chequeada por cupo, serialización y deserialización, capturas específicas y flujo `try-catch-finally` en `App`. Incluye casos exitosos y rechazos controlados. |
| 2 | `Certificable` implementada por `Taller` y `Curso`; las charlas no son certificables. Se muestran certificados y datos de los eventos. |
| 3 | Método genérico acotado y wildcard con las firmas solicitadas; listas `List<Charla>`, `List<Taller>` y `List<Curso>`, cantidades y costos por evento. |
| 4 | Ticket como clase miembro de `Inscripcion`, emisión para confirmadas y envío concurrente mediante `EnvioTicketsThread`. |

1. Crea tres eventos, asigna sus salas y crea siete actividades.
2. Intenta inscribir a cinco estudiantes en cada actividad.
3. Captura `CupoExcedidoException` cuando se completa el cupo.
4. Confirma dos inscripciones por actividad y emite un ticket por cada una.
5. Guarda los eventos, las inscripciones y sus tickets en `events/Event<ID>.pp`.
6. Recupera los eventos y crea un `EnvioTicketsThread` por evento.
7. Inicia los hilos con `start()` y, mientras envían los tickets, `main` muestra eventos, actividades, inscriptos y certificados.
8. Filtra charlas, talleres y cursos mediante un método genérico y calcula sus costos.
9. Espera el cierre de los hilos con `join()` después de mostrar la información e informa el total de tickets enviados.

Con los datos incluidos se aceptan **28 inscripciones** y se rechazan **7 intentos** por falta de cupo. Se confirman **14 inscripciones**, se dejan **14 sin confirmar** y se emiten y envían **14 tickets únicos**. Los rechazos por cupo son una demostración esperada del manejo de excepciones. La salida real de una ejecución se incluye en [docs/ejecucion.txt](docs/ejecucion.txt).

En cada evento, `App` intenta inscribir, confirmar, guardar y recuperar dentro del mismo flujo. El `finally` informa el cierre del intento tanto si la persistencia funciona como si falla. El modelo valida los datos de las actividades y rechaza estudiantes nulos; `App` demuestra además el rechazo controlado de un cupo negativo. Las excepciones de persistencia distinguen archivo ausente o inaccesible, objeto no serializable, clase incompatible, contenido incorrecto, archivo corrupto o incompleto y otros errores de entrada/salida. Los streams se cierran automáticamente mediante `try-with-resources`.

## Tickets de acceso y concurrencia

`TicketDeAcceso` es una clase **miembro no estática** declarada dentro de `Inscripcion`. Cada ticket queda vinculado a su inscripción mediante `Inscripcion.this`. Su constructor es privado: el punto de emisión es `inscripcion.emitirTicket()`.

- El atributo de `Inscripcion` es `ticket: TicketDeAcceso`, consultable con `getTicket()`.
- `confirmar()` cambia el estado a `CONFIRMADA`.
- `emitirTicket()` rechaza una inscripción sin confirmar con `IllegalStateException`.
- El primer llamado válido crea el ticket; los siguientes devuelven la misma instancia. La emisión está sincronizada para evitar duplicados incluso si varios hilos la solicitan.
- Cada ticket tiene `idTicket` de tipo `String` (un UUID) y `fechaEmision` de tipo `LocalDate`, como indica el diagrama. También es serializable, de modo que se conserva al guardar y recuperar el evento.
- `EnvioTicketsThread` es una clase pública e independiente en el paquete `hilos`. Recorre un evento y llama a `ticket.enviarTicket()` únicamente para tickets ya generados de inscripciones confirmadas. La operación de envío pertenece al propio ticket, como indica el diagrama.

**El envío es una simulación por consola.** La consigna aportada no define un servicio de correo ni otro canal de entrega. El hilo representa ese proceso y no realiza comunicaciones externas.

En `App` se inicia un hilo por evento. Después de `start()`, el hilo principal sigue mostrando la información; `join()` se llama al terminar esa tarea. Las pausas breves permiten observar la alternancia. El orden concreto de las líneas y los códigos de ticket cambian entre ejecuciones.

Las líneas llevan prefijos como `[main]` y `[EnvioTickets-A87E45]`. La salida real capturada en `docs/ejecucion.txt` permite observar cómo se alternan. Para la entrega, agregar además una captura de IntelliJ en `docs/captura_hilos.png` que muestre ambos prefijos.

### Correspondencia con el resultado solicitado

| Punto | Implementación |
| --- | --- |
| a. Crear estudiantes | `StubGen.crearEstudiantes()` llamado desde `App`. |
| b. Crear eventos, salas y actividades | Creación y asignación en `App`. |
| c. Inscribir estudiantes | Bucles de inscripción y control de cupo en `App`. |
| d. Confirmar algunas inscripciones | Se llaman `confirmar()` para las dos primeras de cada actividad. |
| e. Emitir un ticket por confirmada | `Inscripcion.emitirTicket()` y su clase miembro `TicketDeAcceso`. |
| f. Iniciar el envío concurrente | `EnvioTicketsThread.start()` por cada evento recuperado. |
| g. Mostrar información durante el envío | `mostrarResumen()` desde `main`, antes de esperar con `join()`. |
| h. Evidenciar los flujos de ejecución | `Consola` agrega el nombre del hilo a cada línea. |

## Organización del código

| Archivo o carpeta | Responsabilidad |
| --- | --- |
| `src/App.java` | Ejecutar la demostración y capturar las excepciones. |
| `src/modelo/EventoUniversitario.java` | Administrar actividades, costos, persistencia y filtros genéricos. |
| `src/modelo/Estudiante.java` | Representar al estudiante. |
| `src/modelo/Sala.java` | Representar la sala asignada a un evento. |
| `src/modelo/Inscripcion.java` | Vincular estudiante y actividad, confirmar la inscripción y emitir su ticket mediante una clase miembro. |
| `src/hilos/EnvioTicketsThread.java` | Simular el envío de tickets de un evento en otro hilo. |
| `src/utils/Consola.java` | Identificar el hilo que produce cada línea. |
| `src/modelo/actividades/` | Definir `Actividad`, `Charla`, `Taller` y `Curso`. |
| `src/modelo/certificacion/Certificable.java` | Definir el contrato para generar certificados. |
| `src/excepciones/` | Definir la excepción chequeada por cupo completo. |
| `src/utils/` | Proveer datos de ejemplo y utilidades de archivos. |

`Actividad` es abstracta. `Charla`, `Taller` y `Curso` implementan su propio costo de materiales; `Taller` y `Curso` también implementan `Certificable`.

Se mantienen las reglas de costos del proyecto base: charla sin costo de materiales, curso con costo fijo de 500 y taller con costo de 5000 si requiere notebook o 2000 en caso contrario. El costo estimado de un evento pago es `(costoBase + materiales) * 1.21`; un evento gratuito devuelve 0. Son valores del ejemplo académico. El cálculo de una lista verifica que sus actividades pertenezcan al propio evento.

## Correspondencia con el diagrama del punto 4

La revisión de los nombres, tipos y responsabilidades se documenta en [docs/revision_diagrama.md](docs/revision_diagrama.md). La clase miembro incluye la operación pública `enviarTicket(): void`, ejecutada desde `EnvioTicketsThread.run()`.

También se ofrece `crearActividad(int id, String titulo, String tipo)`, la firma de tres parámetros mostrada en el diagrama. Como el diagrama no fija el cupo que debe asignar esa firma, se adopta un cupo de demostración de cinco lugares. La sobrecarga que recibe el cupo explícito se conserva para las actividades de `App`.

## Cambios respecto del archivo recibido

- Nombre del proyecto, módulo, identificación y datos de ejemplo adaptados al legajo 50730.
- Configuración de IntelliJ ajustada de JDK 25 a JDK 21.
- `App` organizado con bucles sencillos, sin crear eventos auxiliares que alteren el contador.
- Script de Windows corregido: usa el mismo archivo de fuentes para generar la lista y compilarla.
- Scripts con UTF-8 y ejecución desde la ubicación del proyecto.
- Listas de inscripciones inicializadas desde el constructor; una actividad vacía se puede mostrar sin errores.
- Consulta de inscripciones sin permitir modificaciones externas que eludan el cupo.
- Carpeta de persistencia creada por Java y cierre automático de streams mediante `try-with-resources`; `finally` explícito en el flujo de `App`.
- Separadores de archivos independientes del contenido de la variable `PATH`.
- Identificadores `serialVersionUID` en las clases serializables.
- Confirmación de inscripciones, emisión de tickets mediante una clase anidada miembro y envío concurrente por evento.

## Preparar la subida a GitHub

Nombre previsto del repositorio: **`PP_TP2_50730`**, dentro de la cuenta **`Franco-Nicolas-Baigorri`**.

Descomprimir el archivo y subir **el contenido de la carpeta `PP_TP2_50730`** a la raíz del repositorio. Así `README.md`, `src`, el archivo `.iml` y los scripts quedan directamente en la raíz. También se conserva la configuración compartida de IntelliJ incluida en el paquete.

Los archivos compilados y los eventos generados quedan excluidos mediante `.gitignore`. Al subir por la web, seleccionar los archivos del paquete recién descomprimido; si ya se ejecutó, no seleccionar `cls`, `out` ni `events`, porque la carga web no aplica `.gitignore`.

Mensaje de commit sugerido:

```text
Completar ejercicios 1 a 4
```

Para actualizar el repositorio que ya está creado, seguir [docs/actualizar_github.md](docs/actualizar_github.md).

## Procedencia

Adaptación realizada a partir del ZIP `PP_TP2_50704-master.zip` proporcionado como referencia. Su README identifica como repositorio de origen a [franco-calzetta-campos/PP_TP2_50704](https://github.com/franco-calzetta-campos/PP_TP2_50704). Se conserva esta referencia para distinguir el código base de las modificaciones de esta versión.
