package modelo.actividades;

import modelo.Inscripcion;
import modelo.Estudiante;
import excepciones.CupoExcedidoException;
import utils.Consola;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Actividad
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private int cupoMaximo;
    private List<Inscripcion> inscripciones;

    public static final int CUPO_MINIMO = 0;

    public Actividad(int id, String titulo, int cupoMaximo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título de la actividad es obligatorio.");
        }
        if (cupoMaximo < CUPO_MINIMO) {
            throw new IllegalArgumentException("El cupo de la actividad debe ser como mínimo " + CUPO_MINIMO + ".");
        }
        this.id = id;
        this.titulo = titulo;
        this.cupoMaximo = cupoMaximo;
        this.inscripciones = new ArrayList<>();
    }

    // Cada subtipo define su costo de materiales y su tipo.
    public abstract double calcularCostoMateriales();
    public abstract String getTipo();

    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
        if (estudiante == null) {
            throw new IllegalArgumentException("Se necesita un estudiante para realizar la inscripción.");
        }
        // Manejo de excepciones al exceder cupo
        if (inscripciones.size() >= cupoMaximo) {
            throw new CupoExcedidoException(
                "Cupo excedido en actividad " + this.titulo
                + ": No se pudo inscribir al estudiante " + estudiante.getNombre()
                + " (L#" + estudiante.getLegajo() + ")"
            );
        }

        Inscripcion nuevaInscripcion =
                new Inscripcion(LocalDate.now(), "INSCRITO", this, estudiante);
        inscripciones.add(nuevaInscripcion);

        return nuevaInscripcion;
    }

    public void mostrarInscripciones() {
        if (inscripciones.isEmpty()) {
            Consola.mostrar("No hay inscriptos en esta actividad.");
            return;
        }

        Consola.mostrar("Estudiantes inscriptos en actividad \"" + titulo + "\":");

        for (Inscripcion i : inscripciones) {
            Estudiante e = i.getEstudiante();
            Consola.mostrar(e.getNombre() + " (L#" + e.getLegajo() + ")"
                    + " | Estado: " + i.getEstado()
                    + " | Ticket: " + (i.getTicket() != null ? "emitido" : "sin emitir"));
        }
    }

    public final void mostrarIdentificacion() {
        Consola.mostrar(
                "ACTIVIDAD: \"" + this.titulo +   "\" (ID: " + this.id + ")"
        );
    }

    public int getId() {
        return this.id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public int getCupoMaximo() {
        return this.cupoMaximo;
    }

    public List<Inscripcion> getInscripciones() {
        return Collections.unmodifiableList(this.inscripciones);
    }
}
