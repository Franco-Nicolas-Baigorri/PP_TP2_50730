import java.io.FileNotFoundException;
import java.io.EOFException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

import excepciones.CupoExcedidoException;
import hilos.EnvioTicketsThread;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;
import utils.StubGen;
import utils.Consola;

/** Demostración de eventos, persistencia, genéricos, tickets e hilos. */
public class App {
    public static void main(String[] args) {
        Consola.mostrar("TP 2 - Paradigmas de Programación");
        Consola.mostrar("Franco Nicolás Baigorri - Legajo 50730\n");

        Estudiante[] estudiantes = StubGen.crearEstudiantes();
        EventoUniversitario[] eventos = StubGen.crearEventosUniversitarios();

        for (int i = 0; i < eventos.length; i++) {
            eventos[i].asignarSala(StubGen.generarSala(i + 1));
        }

        eventos[0].crearActividad(1001, "Introducción a Arduino", 5, "Curso");
        eventos[0].crearActividad(1002, "Servidor casero con Arduino", 7, "Taller");
        eventos[1].crearActividad(2001, "Exposición de proyectos", 4, "Charla");
        eventos[1].crearActividad(2002, "Cierre de la exposición", 5, "Charla");
        eventos[2].crearActividad(3001, "Presentación de la jornada", 3, "Charla");
        eventos[2].crearActividad(3002, "Programación en equipo", 3, "Taller");
        eventos[2].crearActividad(3003, "Presentación de resultados", 3, "Charla");

        // Un mismo flujo intenta inscribir, confirmar, guardar y recuperar cada evento.
        List<EventoUniversitario> eventosRecuperados = new ArrayList<>();
        int confirmadas = 0;
        int sinConfirmar = 0;
        for (EventoUniversitario evento : eventos) {
            Consola.mostrar("\n=== Inscripción y persistencia del evento " + evento.getId() + " ===");
            try {
                inscribirEstudiantes(evento, estudiantes);
                for (Actividad actividad : evento.getActividades()) {
                    List<Inscripcion> inscripciones = actividad.getInscripciones();
                    for (int i = 0; i < inscripciones.size(); i++) {
                        Inscripcion inscripcion = inscripciones.get(i);
                        // Confirmamos algunas y conservamos otras sin confirmar.
                        if (i < 2) {
                            inscripcion.confirmar();
                            Inscripcion.TicketDeAcceso ticket = inscripcion.emitirTicket();
                            confirmadas++;
                            Consola.mostrar("EMITIDO: " + ticket);
                        } else {
                            sinConfirmar++;
                        }
                    }
                }
                evento.persistirEvento();
                // Se usa un objeto existente para llamar al método de recuperación.
                // No se crean eventos auxiliares que alteren el contador estático.
                EventoUniversitario recuperado = evento.recuperarEvento(evento.getId());
                eventosRecuperados.add(recuperado);
                Consola.mostrar("Evento guardado y recuperado: " + recuperado.getId());
            } catch (FileNotFoundException excepcion) {
                Consola.mostrar("Archivo de evento no encontrado o no accesible: " + excepcion.getMessage());
            } catch (NotSerializableException excepcion) {
                Consola.mostrar("Objeto no serializable al guardar el evento: " + excepcion.getMessage());
            } catch (InvalidClassException excepcion) {
                Consola.mostrar("Versión de clase incompatible al recuperar: " + excepcion.getMessage());
            } catch (InvalidObjectException excepcion) {
                Consola.mostrar("Contenido de evento inválido: " + excepcion.getMessage());
            } catch (StreamCorruptedException excepcion) {
                Consola.mostrar("Archivo de evento corrupto: " + excepcion.getMessage());
            } catch (EOFException excepcion) {
                Consola.mostrar("Archivo de evento incompleto: " + excepcion.getMessage());
            } catch (IOException excepcion) {
                Consola.mostrar("Error de entrada/salida al guardar o recuperar: " + excepcion.getMessage());
            } catch (ClassNotFoundException excepcion) {
                Consola.mostrar("No se pudo encontrar una clase del evento guardado: " + excepcion.getMessage());
            } finally {
                Consola.mostrar("FINALLY: finalizó el intento de inscripción y persistencia de "
                        + evento.getId() + ".");
            }
        }
        Consola.mostrar("Confirmadas: " + confirmadas + " | Sin confirmar: " + sinConfirmar);
        Consola.mostrar("Eventos creados: " + EventoUniversitario.getCantidadEventos());

        // La validación pertenece al modelo; App solo demuestra cómo manejar el fallo.
        try {
            eventos[0].crearActividad(9999, "Actividad con cupo inválido", -1, "Taller");
        } catch (IllegalArgumentException excepcion) {
            Consola.mostrar("Dato inválido controlado: " + excepcion.getMessage());
        }

        List<EnvioTicketsThread> hilosEnvio = new ArrayList<>();
        for (EventoUniversitario evento : eventosRecuperados) {
            EnvioTicketsThread hilo = new EnvioTicketsThread(evento);
            hilosEnvio.add(hilo);
            hilo.start(); // start() crea otro hilo; llamar a run() no lo haría.
        }

        try {
            Consola.mostrar("\n=== El hilo principal continúa mostrando información ===");
            for (EventoUniversitario evento : eventosRecuperados) {
                Consola.mostrar("Mostrando el evento " + evento.getId()
                        + " mientras los hilos envían sus tickets.");
                mostrarResumen(evento);
                // Pausa breve de demostración para apreciar la alternancia en consola.
                Thread.sleep(200);
            }

            Consola.mostrar("El hilo principal terminó de mostrar los datos.");
            // Esperamos después de mostrar la información, no antes.
            for (EnvioTicketsThread hilo : hilosEnvio) {
                hilo.join();
            }
        } catch (InterruptedException excepcion) {
            for (EnvioTicketsThread hilo : hilosEnvio) {
                hilo.interrupt();
            }
            Thread.currentThread().interrupt();
            Consola.mostrar("Demostración interrumpida; se solicitó detener los envíos.");
            return;
        }

        int totalEnviados = 0;
        for (EnvioTicketsThread hilo : hilosEnvio) {
            totalEnviados += hilo.getCantidadEnviada();
        }
        Consola.mostrar("Todos los hilos finalizaron. Tickets enviados (simulación): " + totalEnviados);
    }

    private static void inscribirEstudiantes(EventoUniversitario evento, Estudiante[] estudiantes) {
        for (Actividad actividad : evento.getActividades()) {
            for (Estudiante estudiante : estudiantes) {
                try {
                    actividad.inscribir(estudiante);
                    Consola.mostrar("Inscripción aceptada: " + estudiante.getLegajo()
                            + " en actividad " + actividad.getId());
                } catch (CupoExcedidoException excepcion) {
                    // La inscripción rechazada no se agrega a la actividad.
                    Consola.mostrar(excepcion.getMessage());
                } catch (IllegalArgumentException excepcion) {
                    Consola.mostrar("Datos de inscripción inválidos: " + excepcion.getMessage());
                }
            }
        }
    }

    private static void mostrarResumen(EventoUniversitario evento) {
        evento.mostrarDatos();

        // El método genérico devuelve una lista del subtipo solicitado.
        List<Charla> charlas = evento.filtrarActividadesPorTipo(Charla.class);
        List<Taller> talleres = evento.filtrarActividadesPorTipo(Taller.class);
        List<Curso> cursos = evento.filtrarActividadesPorTipo(Curso.class);

        // El comodín acepta listas de distintos subtipos de Actividad.
        Consola.mostrar("Charlas: " + charlas.size()
                + " | Costo de materiales: " + evento.calcularCostoMateriales(charlas));
        Consola.mostrar("Talleres: " + talleres.size()
                + " | Costo de materiales: " + evento.calcularCostoMateriales(talleres));
        Consola.mostrar("Cursos: " + cursos.size()
                + " | Costo de materiales: " + evento.calcularCostoMateriales(cursos));
        Consola.mostrar("");
    }
}
