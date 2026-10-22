# Revisión de los ejercicios 1 a 4

Las consignas se implementan de forma integrada en el mismo programa, ejecutable desde `App`.

## Ejercicio 1

| Requisito | Ubicación y comportamiento |
| --- | --- |
| Paquetes del modelo | `modelo`, `modelo.actividades`, `modelo.certificacion` y `excepciones`. |
| `throw` y `throws` con excepción chequeada | `Actividad.inscribir(Estudiante) throws CupoExcedidoException`; la excepción extiende `Exception` y se lanza al completar el cupo. |
| Captura en App | `inscribirEstudiantes()` captura cada rechazo y permite continuar con los siguientes estudiantes. |
| Guardar y recuperar objetos | `EventoUniversitario.persistirEvento()` y `recuperarEvento(String)` serializan el evento y sus relaciones. |
| Manejo granular de persistencia | `App` captura `FileNotFoundException`, `NotSerializableException`, `InvalidClassException`, `InvalidObjectException`, `StreamCorruptedException`, `EOFException`, `IOException` y `ClassNotFoundException`, con mensajes específicos. |
| Flujo `try-catch-finally` | El bucle de eventos en `main` intenta inscribir, confirmar, persistir y recuperar. `finally` informa la finalización del intento incluso si ocurre una excepción. |
| Casos exitosos y fallidos | 28 inscripciones aceptadas, siete rechazos por cupo y tres eventos guardados y recuperados. Se demuestra también un cupo negativo rechazado con `IllegalArgumentException`. |
| Validaciones en el modelo | Actividad rechaza título vacío, cupo inferior a `CUPO_MINIMO` y estudiante nulo. La fábrica rechaza tipos desconocidos; recuperar un archivo con otro tipo de objeto produce `InvalidObjectException`. |

Se conserva el valor `CUPO_MINIMO = 0` del proyecto base; la consigna suministrada no fija otro umbral numérico. Los streams usan `try-with-resources` para garantizar su cierre.

## Ejercicio 2

`App` crea estudiantes, tres eventos, sus salas y actividades de los tipos `Charla`, `Taller` y `Curso`, y realiza las inscripciones. `Taller` y `Curso` implementan `Certificable`; `Charla` no implementa esa interfaz. `EventoUniversitario.mostrarDatos()` genera y muestra los certificados de los estudiantes inscriptos en talleres y cursos, junto con los datos del evento. Con estos datos se muestran 13 certificados.

## Ejercicio 3

En `EventoUniversitario` se implementan exactamente estas firmas:

```java
public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo)
public double calcularCostoMateriales(List<? extends Actividad> actividades)
```

El filtro recorre las actividades del propio evento y usa `Class.isInstance` y `Class.cast`, sin conversiones no comprobadas. El cálculo admite `List<Actividad>` y listas de subtipos, y rechaza actividades ajenas al evento. En `App.mostrarResumen()` se declaran `List<Charla>`, `List<Taller>` y `List<Curso>`; se muestran cantidades y costos de cada lista para cada evento.

| Evento | Charlas / materiales | Talleres / materiales | Cursos / materiales |
| --- | --- | --- | --- |
| A87E45 | 0 / 0 | 1 / 5000 | 1 / 500 |
| HK18HZ | 2 / 0 | 0 / 0 | 0 / 0 |
| PP210M | 2 / 0 | 1 / 5000 | 0 / 0 |

## Ejercicio 4

`Inscripcion.TicketDeAcceso` es una clase miembro no estática con `idTicket: String`, `fechaEmision: LocalDate` y `enviarTicket(): void`. La emisión exige confirmación y conserva un único ticket por inscripción. `hilos.EnvioTicketsThread` es pública, independiente y extiende `Thread`; envía los tickets generados de las inscripciones confirmadas de un evento.

`App` inicia los hilos con `start()`, muestra eventos y sus inscriptos desde el hilo principal y espera con `join()` al terminar esa tarea. Se distinguen los prefijos `[main]` y `[EnvioTickets-...]`. Se generan y envían 14 tickets únicos. El envío es una simulación por consola. La correspondencia detallada con el diagrama se conserva en `revision_diagrama.md`.

## Evidencia de ejecución

`ejecucion.txt` contiene la salida real de la versión integrada. Para cumplir el requisito de captura, subir también una imagen legible de la consola de IntelliJ. Las instrucciones para actualizar el mismo repositorio están en `actualizar_github.md`.
