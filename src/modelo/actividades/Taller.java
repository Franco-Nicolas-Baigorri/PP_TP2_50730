package modelo.actividades;

import modelo.Estudiante;
import modelo.certificacion.Certificable;

public class Taller
        extends Actividad
        implements Certificable
{
    private static final long serialVersionUID = 1L;

    private boolean requiereNotebook;

    public Taller (int id, String titulo, int cupoMaximo, boolean requiereNotebook) {
        super(id, titulo, cupoMaximo);
        this.requiereNotebook = requiereNotebook;
    }

    @Override
    public double calcularCostoMateriales() {
        // $5000 si requieren uso de notebook
        // y $2000 si no requieren uso de notebook.
        return (requiereNotebook ? 5000d : 2000d);
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "Se certifica que el estudiante " + estudiante.getNombre()
                + ", Legajo nº" + estudiante.getLegajo()
                + " asistió al taller \"" + this.getTitulo() + "\"";
    }

    @Override
    public String getTipo() {
        return "Taller";
    }

    public boolean getRequiereNotebook() {
        return this.requiereNotebook;
    }
}
