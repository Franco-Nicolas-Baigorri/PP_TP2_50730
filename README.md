# PP_TP2_50730 — Eventos universitarios

**Adaptación y revisión:** Franco Nicolás Baigorri

**Legajo:** 50730

**Materia:** Paradigmas de Programación

**Entorno de trabajo:** IntelliJ IDEA y JDK 21.

Aplicación de consola para gestionar eventos universitarios, sus salas, actividades e inscripciones. Demuestra herencia, polimorfismo, una interfaz de certificación, excepciones chequeadas, serialización y métodos genéricos.

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

1. Crea tres eventos, asigna sus salas y crea siete actividades.
2. Intenta inscribir a cinco estudiantes en cada actividad.
3. Captura `CupoExcedidoException` cuando se completa el cupo.
4. Guarda los eventos en archivos binarios `events/Event<ID>.pp`.
5. Recupera esos archivos y muestra las inscripciones y certificados.
6. Filtra charlas, talleres y cursos mediante un método genérico.
7. Calcula los materiales aceptando listas de distintos subtipos de actividad.

Con los datos incluidos se aceptan **28 inscripciones** y se rechazan **7 intentos** por falta de cupo. Los rechazos son una demostración esperada del manejo de excepciones. La salida real de una ejecución se incluye en [docs/ejecucion.txt](docs/ejecucion.txt).

## Organización del código

| Archivo o carpeta | Responsabilidad |
| --- | --- |
| `src/App.java` | Ejecutar la demostración y capturar las excepciones. |
| `src/modelo/EventoUniversitario.java` | Administrar actividades, costos, persistencia y filtros genéricos. |
| `src/modelo/Estudiante.java` | Representar al estudiante. |
| `src/modelo/Sala.java` | Representar la sala asignada a un evento. |
| `src/modelo/Inscripcion.java` | Vincular una actividad con un estudiante, fecha y estado. |
| `src/modelo/actividades/` | Definir `Actividad`, `Charla`, `Taller` y `Curso`. |
| `src/modelo/certificacion/Certificable.java` | Definir el contrato para generar certificados. |
| `src/excepciones/` | Definir la excepción chequeada por cupo completo. |
| `src/utils/` | Proveer datos de ejemplo y utilidades de archivos. |

`Actividad` es abstracta. `Charla`, `Taller` y `Curso` implementan su propio costo de materiales; `Taller` y `Curso` también implementan `Certificable`.

Se mantienen las reglas de costos del proyecto base: charla sin costo de materiales, curso con costo fijo de 500 y taller con costo de 5000 si requiere notebook o 2000 en caso contrario. El costo estimado de un evento pago es `(costoBase + materiales) * 1.21`; un evento gratuito devuelve 0. Son valores del ejemplo académico.

## Cambios respecto del archivo recibido

- Nombre del proyecto, módulo, identificación y datos de ejemplo adaptados al legajo 50730.
- Configuración de IntelliJ ajustada de JDK 25 a JDK 21.
- `App` organizado con bucles sencillos, sin crear eventos auxiliares que alteren el contador.
- Script de Windows corregido: usa el mismo archivo de fuentes para generar la lista y compilarla.
- Scripts con UTF-8 y ejecución desde la ubicación del proyecto.
- Listas de inscripciones inicializadas desde el constructor; una actividad vacía se puede mostrar sin errores.
- Consulta de inscripciones sin permitir modificaciones externas que eludan el cupo.
- Carpeta de persistencia creada por Java y cierre de streams en el orden correcto mediante `finally`.
- Separadores de archivos independientes del contenido de la variable `PATH`.
- Identificadores `serialVersionUID` en las clases serializables.

## Preparar la subida a GitHub

Nombre previsto del repositorio: **`PP_TP2_50730`**, dentro de la cuenta **`Franco-Nicolas-Baigorri`**.

Descomprimir el archivo y subir **el contenido de la carpeta `PP_TP2_50730`** a la raíz del repositorio. Así `README.md`, `src`, el archivo `.iml` y los scripts quedan directamente en la raíz. También se conserva la configuración compartida de IntelliJ incluida en el paquete.

Los archivos compilados y los eventos generados quedan excluidos mediante `.gitignore`. Al subir por la web, seleccionar los archivos del paquete recién descomprimido; si ya se ejecutó, no seleccionar `cls`, `out` ni `events`, porque la carga web no aplica `.gitignore`.

Mensaje de commit sugerido:

```text
Agregar TP2: eventos, excepciones, persistencia y genericos
```

## Procedencia

Adaptación realizada a partir del ZIP `PP_TP2_50704-master.zip` proporcionado como referencia. Su README identifica como repositorio de origen a [franco-calzetta-campos/PP_TP2_50704](https://github.com/franco-calzetta-campos/PP_TP2_50704). Se conserva esta referencia para distinguir el código base de las modificaciones de esta versión.
