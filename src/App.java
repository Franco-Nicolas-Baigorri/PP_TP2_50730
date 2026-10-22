import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;
import utils.StubGen;

/** Demostración de eventos, inscripciones, persistencia y métodos genéricos. */
public class App {
    public static void main(String[] args) {
        System.out.println("TP 2 - Paradigmas de Programación");
        System.out.println("Franco Nicolás Baigorri - Legajo 50730\n");

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

        System.out.println("=== Inscripción de estudiantes ===");
        for (EventoUniversitario evento : eventos) {
            for (Actividad actividad : evento.getActividades()) {
                for (Estudiante estudiante : estudiantes) {
                    try {
                        actividad.inscribir(estudiante);
                    } catch (CupoExcedidoException excepcion) {
                        // La inscripción rechazada no se agrega a la actividad.
                        System.out.println(excepcion.getMessage());
                    }
                }
            }
        }

        System.out.println("\nEventos creados: " + EventoUniversitario.getCantidadEventos());
        System.out.println("\n=== Guardado y recuperación de eventos ===");
        List<EventoUniversitario> eventosRecuperados = new ArrayList<>();

        for (EventoUniversitario evento : eventos) {
            try {
                evento.persistirEvento();
                // Se usa un objeto existente para llamar al método de recuperación.
                // No se crean eventos auxiliares que alteren el contador estático.
                EventoUniversitario recuperado = evento.recuperarEvento(evento.getId());
                eventosRecuperados.add(recuperado);
                System.out.println("Evento guardado y recuperado: " + recuperado.getId());
            } catch (FileNotFoundException excepcion) {
                System.err.println("Archivo no encontrado: " + excepcion.getMessage());
            } catch (IOException excepcion) {
                System.err.println("Error de entrada/salida: " + excepcion.getMessage());
            } catch (ClassNotFoundException excepcion) {
                System.err.println("No se pudo recuperar la clase: " + excepcion.getMessage());
            }
        }

        System.out.println("\n=== Resumen de los eventos recuperados ===\n");
        for (EventoUniversitario evento : eventosRecuperados) {
            evento.mostrarDatos();

            // El método genérico devuelve una lista del subtipo solicitado.
            List<Charla> charlas = evento.filtrarActividadesPorTipo(Charla.class);
            List<Taller> talleres = evento.filtrarActividadesPorTipo(Taller.class);
            List<Curso> cursos = evento.filtrarActividadesPorTipo(Curso.class);

            // El comodín acepta listas de distintos subtipos de Actividad.
            System.out.println("Charlas: " + charlas.size()
                    + " | Costo de materiales: " + evento.calcularCostoMateriales(charlas));
            System.out.println("Talleres: " + talleres.size()
                    + " | Costo de materiales: " + evento.calcularCostoMateriales(talleres));
            System.out.println("Cursos: " + cursos.size()
                    + " | Costo de materiales: " + evento.calcularCostoMateriales(cursos));
            System.out.println();
        }
    }
}
