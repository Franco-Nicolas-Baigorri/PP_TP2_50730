package utils;

import modelo.Estudiante;
import modelo.EventoUniversitario;
import modelo.Sala;

/** Datos de demostración para mantener sencillo el método main. */
public class StubGen {
    public static Estudiante[] crearEstudiantes() {
        return new Estudiante[] {
            new Estudiante("50730", "Franco Nicolás Baigorri"),
            new Estudiante("50502", "Bernardo Beltrán"),
            new Estudiante("50503", "Camila Canales"),
            new Estudiante("50504", "Daniela Daniele"),
            new Estudiante("50505", "Ernestina Erlanga")
        };
    }

    public static EventoUniversitario[] crearEventosUniversitarios() {
        return new EventoUniversitario[] {
            new EventoUniversitario("A87E45", "Jornadas de Arduino", 150000d, false),
            new EventoUniversitario("HK18HZ", "Exposición de proyectos", 250000d, true),
            new EventoUniversitario("PP210M", "Jornadas de programación", 325000d, true)
        };
    }

    public static Sala generarSala(int id) {
        String[] nombresSala = {
            "Sala de Conferencias de Electrónica",
            "Salón de Usos Múltiples",
            "Laboratorio de Informática"
        };
        if (id < 1 || id > nombresSala.length) {
            throw new IllegalArgumentException("No existe una sala de prueba con id " + id);
        }
        return new Sala(id, nombresSala[id - 1]);
    }
}
