package modelo.certificacion;

import modelo.Estudiante;

public interface Certificable {
    // Las constantes de una interfaz son public static final.
    public static final String ENTIDAD_EMISORA = "UTN";

    public String generarCertificado(Estudiante estudiante);
}
