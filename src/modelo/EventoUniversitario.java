package modelo;

import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;
import utils.FileUtils;
import utils.Consola;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

public class EventoUniversitario
    implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private static int cantidadEventos = 0;
    private Sala sala;
    /* No es posible respetar la cardinalidad 1..* con las restricciones de
     * constructor y composición. i.e. resulta imposible garantizar la
     * existencia de un mínimo de 1 actividad al inicio del ciclo de vida del
     * objeto
     */
    private List<Actividad> actividades;

    public EventoUniversitario(
            String id,
            String titulo,
            double costoBase,
            boolean gratuito)
    {
        this.id = id;
        this.titulo = titulo;
        this.costoBase = costoBase;
        this.gratuito = gratuito;

        /*
         * Considerando que:
         * - La cardinalidad de composición es de 1 (no 0..1)
         * - No se plantea en el ejercicio que se asigne una sala por constructor
         * - Se asigna una sala solamente con la función asignarSala(sala: Sala)
         *
         * Se opta por un valor por defecto que se sobreescribirá al invocar asignarSala.
         */
        this.sala = new Sala(-1, "SIN SALA");

        this.actividades = new ArrayList<>();

        cantidadEventos++;
    }

    public EventoUniversitario(EventoUniversitario otro) {
        // Se asume por la premisa del resultado esperado que se espera que la
        // cantidad total incluya las copias, ergo se usa el mismo constructor
        // parametrizado
        this(
                otro.id,
                otro.titulo,
                otro.costoBase,
                otro.gratuito);
    }

    public double calcularCostoEstimado() {
        if (gratuito) return 0d;

        double costoEstimado = costoBase;

        if (actividades != null) {
            for (Actividad a : actividades) {
                costoEstimado += a.calcularCostoMateriales();
            }
        }

        return costoEstimado * 1.21d;
    }

    public void asignarSala(Sala sala) {
        this.sala = sala;
    }

    /** Firma del diagrama; se adopta un cupo de demostración de cinco lugares. */
    public void crearActividad(int id, String titulo, String tipo) {
        crearActividad(id, titulo, 5, tipo);
    }

    public void crearActividad(int id, String titulo, int cupo, String tipo)
    {
        if (cupo < Actividad.CUPO_MINIMO) {
            throw new IllegalArgumentException("El cupo de la actividad debe ser como mínimo "
                    + Actividad.CUPO_MINIMO + ".");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Se necesita indicar el tipo de actividad.");
        }

        Actividad nueva_actividad;

        switch (tipo) {
            case "Charla":
                nueva_actividad = new Charla(id, titulo, cupo, "Docente invitado");
                break;
            case "Taller":
                nueva_actividad = new Taller(id, titulo, cupo, true);
                break;
            case "Curso":
                nueva_actividad = new Curso(id, titulo, cupo, 3);
                break;
            default:
                throw new IllegalArgumentException("Tipo de actividad inválido: " + tipo);
        }

        actividades.add(nueva_actividad);
    }

    public void mostrarDatos() {
        Consola.mostrar(
                "Evento \"" + this.titulo + "\" (ID#" + this.id + "):\n"
                        +   "Gratuito: " + (gratuito ? "SI" : "NO") + "\n\n"
                        +   "Sala: " + sala.getNombre() + "\n"
                        +   "Costo: ARS " + calcularCostoEstimado() + "\n"
                        +   "Actividades:"
        );

        for (Actividad a : actividades) {
            Consola.mostrar(
                    "Actividad " + a.getId() + ": " + a.getTitulo() + "\nTipo: " + a.getTipo()
            );
            switch (a.getTipo()) {
                case "Charla":
                    Consola.mostrar("Disertante: " + ((Charla)a).getDisertante());
                    break;
                case "Taller":
                    Consola.mostrar(
                            "Requiere notebook: "
                        +   (((Taller)a).getRequiereNotebook() ? "SI" : "NO" )
                    );
                    Consola.mostrar("Certificaciones:");
                    for (Inscripcion i : a.getInscripciones()) {
                        Consola.mostrar(((Taller)a).generarCertificado(i.getEstudiante()));
                    }
                    break;
                case "Curso":
                    Consola.mostrar("Nivel: " + ((Curso)a).getNivel());
                    Consola.mostrar("Certificaciones:");
                    for (Inscripcion i : a.getInscripciones()) {
                        Consola.mostrar(((Curso)a).generarCertificado(i.getEstudiante()));
                    }
                    break;
            }


            // Asumimos en este punto que no hay tipos incorrectos

            a.mostrarInscripciones();
            Consola.mostrar("");
        }
        Consola.mostrar("");
    }

    public static int getCantidadEventos() {
        return cantidadEventos;
    }

    public List<Actividad> getActividades() {
        return Collections.unmodifiableList(this.actividades);
    }

    public boolean persistirEvento()
        throws FileNotFoundException, IOException
    {
        // También se crea la carpeta al ejecutar App desde IntelliJ.
        Files.createDirectories(Paths.get(FileUtils.PATH_EVENTS));
        try (FileOutputStream fos = new FileOutputStream(
                    FileUtils.PATH_EVENTS
                  + FileUtils.getFormattedNameForEventID(id)
            ); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);
        }
        return true;
    }

    public EventoUniversitario recuperarEvento(String id)
        throws FileNotFoundException, IOException, ClassNotFoundException
    {
        try (FileInputStream fis = new FileInputStream(
                    FileUtils.PATH_EVENTS
                  + FileUtils.getFormattedNameForEventID(id)
            ); ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object registro = ois.readObject();
            if (!(registro instanceof EventoUniversitario)) {
                throw new InvalidObjectException("El archivo no contiene un EventoUniversitario.");
            }
            return (EventoUniversitario) registro;
        }
    }

    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("Se necesita indicar el tipo de actividad a filtrar.");
        }
        List<T> ret = new LinkedList<T>();

        for (Actividad a : actividades) {
            if (tipo.isInstance(a)) {
                ret.add(tipo.cast(a));
            }
        }

        return ret;
    }

    public double calcularCostoMateriales(List<? extends Actividad> actividades) {
        if (actividades == null) {
            throw new IllegalArgumentException("La lista de actividades no puede ser nula.");
        }
        double tot = 0d;

        for (Actividad a : actividades) {
            if (!this.actividades.contains(a)) {
                throw new IllegalArgumentException("Solo se calculan materiales de actividades de este evento.");
            }
            tot += a.calcularCostoMateriales();
        }

        return tot;
    }

    public String getId() {
        return this.id;
    }
}
