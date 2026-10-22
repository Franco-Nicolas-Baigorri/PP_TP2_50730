package modelo;

import modelo.actividades.Actividad;

import java.io.Serializable;
import java.time.LocalDate;

public class Inscripcion
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    private LocalDate fecha;
    private String estado;
    private Actividad actividad;
    private Estudiante estudiante;

    public Inscripcion(
            LocalDate fecha, String estado,
            Actividad actividad, Estudiante estudiante) {
        this.fecha = fecha;
        this.estado = estado;
        this.actividad = actividad;
        this.estudiante = estudiante;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public String getEstado() {
        return this.estado;
    }

    public Actividad getActividad() {
        return this.actividad;
    }

    public Estudiante getEstudiante() {
        return this.estudiante;
    }
}
