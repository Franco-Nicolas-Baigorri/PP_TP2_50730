package modelo;

import modelo.actividades.Actividad;
import modelo.actividades.Charla;
import modelo.actividades.Curso;
import modelo.actividades.Taller;
import utils.FileUtils;

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

    public void crearActividad(int id, String titulo, int cupo, String tipo)
    {
        if (cupo < Actividad.CUPO_MINIMO) {
            System.err.println("ERROR: CREANDO ACTIVIDAD CON CUPO MENOR AL"
                    + " MINIMO, ABORTANDO CREACIÓN DE ACTIVIDAD.\n");
            return;
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
                System.err.println("ERROR: INGRESADO TIPO DE ACTIVIDAD INVALIDO."
                        + " ABORTANDO CREACIÓN DE ACTIVIDAD.");
                return;
        }

        actividades.add(nueva_actividad);
    }

    public void mostrarDatos() {
        System.out.println(
                "Evento \"" + this.titulo + "\" (ID#" + this.id + "):\n"
                        +   "Gratuito: " + (gratuito ? "SI" : "NO") + "\n\n"
                        +   "Sala: " + sala.getNombre() + "\n"
                        +   "Costo: ARS " + calcularCostoEstimado() + "\n"
                        +   "Actividades:"
        );

        for (Actividad a : actividades) {
            System.out.println(
                    "Actividad " + a.getId() + ": " + a.getTitulo() + "\nTipo: " + a.getTipo()
            );
            switch (a.getTipo()) {
                case "Charla":
                    System.out.println("Disertante: " + ((Charla)a).getDisertante());
                    break;
                case "Taller":
                    System.out.println(
                            "Requiere notebook: "
                        +   (((Taller)a).getRequiereNotebook() ? "SI" : "NO" )
                    );
                    System.out.println("Certificaciones:");
                    for (Inscripcion i : a.getInscripciones()) {
                        System.out.println(((Taller)a).generarCertificado(i.getEstudiante()));
                    }
                    break;
                case "Curso":
                    System.out.println("Nivel: " + ((Curso)a).getNivel());
                    System.out.println("Certificaciones:");
                    for (Inscripcion i : a.getInscripciones()) {
                        System.out.println(((Curso)a).generarCertificado(i.getEstudiante()));
                    }
                    break;
            }


            // Asumimos en este punto que no hay tipos incorrectos

            a.mostrarInscripciones();
            System.out.println();
        }
        System.out.println();
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
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(
                    FileUtils.PATH_EVENTS
                  + FileUtils.getFormattedNameForEventID(id)
            );
            oos = new ObjectOutputStream(fos);

            oos.writeObject(this);
        }
        finally {
            if (oos != null) {
                oos.close();
            } else if (fos != null) {
                fos.close();
            }
        }
        return true; // Poner esto dentro del finally evita que se largue la excepcion
    }

    public EventoUniversitario recuperarEvento(String id)
        throws FileNotFoundException, IOException, ClassNotFoundException
    {
        EventoUniversitario registro = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            fis = new FileInputStream(
                    FileUtils.PATH_EVENTS
                  + FileUtils.getFormattedNameForEventID(id)
            );
            ois = new ObjectInputStream(fis);

            registro = (EventoUniversitario)(ois.readObject());
        }
        finally {
            if (ois != null) {
                ois.close();
            } else if (fis != null) {
                fis.close();
            }
        }
        return registro; // Poner esto dentro del finally evita que se largue la excepcion
    }

    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> ret = new LinkedList<T>();

        for (Actividad a : actividades) {
            if (tipo.isInstance(a)) {
                ret.add(tipo.cast(a));
            }
        }

        return ret;
    }

    public double calcularCostoMateriales(List<? extends Actividad> actividades) {
        double tot = 0d;

        for (Actividad a : actividades) {
            tot += a.calcularCostoMateriales();
        }

        return tot;
    }

    public String getId() {
        return this.id;
    }
}
