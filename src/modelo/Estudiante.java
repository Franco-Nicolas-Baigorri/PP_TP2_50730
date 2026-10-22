package modelo;

import java.io.Serializable;

public class Estudiante
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String legajo;
    private String nombre;

    public Estudiante(String legajo, String nombre) {
        this.legajo = legajo;
        this.nombre = nombre;
    }

    public String getLegajo() {
        return this.legajo;
    }
    public String getNombre() {
        return this.nombre;
    }
}
