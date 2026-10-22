package modelo.actividades;

import modelo.Estudiante;
import modelo.certificacion.Certificable;

public class Curso
        extends Actividad
        implements Certificable
{
    private static final long serialVersionUID = 1L;

    private int nivel;

    public Curso(int id, String titulo, int cupoMaximo, int nivel) {
        super(id, titulo, cupoMaximo);
        this.nivel = nivel;
    }

    @Override
    public double calcularCostoMateriales() {
        // Costo fijo utilizado en el modelo de demostración.
        return 500d;
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "Se certifica que el estudiante " + estudiante.getNombre()
                + ", Legajo nº" + estudiante.getLegajo()
                + " asistió al curso \"" + this.getTitulo() + "\"";
    }

    @Override
    public String getTipo() {
        return "Curso";
    }

    public int getNivel() {
        return nivel;
    }
}
