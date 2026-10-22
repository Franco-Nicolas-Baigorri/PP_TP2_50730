package modelo;

import modelo.actividades.Actividad;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import utils.Consola;

public class Inscripcion
    implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final String CONFIRMADA = "CONFIRMADA";

    private LocalDate fecha;
    private String estado;
    private Actividad actividad;
    private Estudiante estudiante;
    private TicketDeAcceso ticket;

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

    public synchronized String getEstado() {
        return this.estado;
    }

    public synchronized void confirmar() {
        this.estado = CONFIRMADA;
    }

    public synchronized boolean estaConfirmada() {
        return CONFIRMADA.equals(this.estado);
    }

    /** Emite un único ticket para esta inscripción, una vez confirmada. */
    public synchronized TicketDeAcceso emitirTicket() {
        if (!estaConfirmada()) {
            throw new IllegalStateException("Solo se puede emitir un ticket para una inscripción confirmada.");
        }
        if (ticket == null) {
            ticket = new TicketDeAcceso();
        }
        return ticket;
    }

    public synchronized TicketDeAcceso getTicket() {
        return ticket;
    }

    public Actividad getActividad() {
        return this.actividad;
    }

    public Estudiante getEstudiante() {
        return this.estudiante;
    }

    /** Clase anidada miembro: cada ticket pertenece a una Inscripcion concreta. */
    public class TicketDeAcceso implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String idTicket;
        private final LocalDate fechaEmision;

        // Solo Inscripcion puede construirlo, después de comprobar su estado.
        private TicketDeAcceso() {
            this.idTicket = UUID.randomUUID().toString();
            this.fechaEmision = LocalDate.now();
        }

        public String getIdTicket() {
            return idTicket;
        }

        public LocalDate getFechaEmision() {
            return fechaEmision;
        }

        /** Operación de envío del ticket definida en el diagrama de clases. */
        public void enviarTicket() {
            Consola.mostrar("ENVIADO (simulación): " + this);
        }

        public Inscripcion getInscripcion() {
            return Inscripcion.this;
        }

        @Override
        public String toString() {
            return "Ticket " + idTicket + " | L#" + estudiante.getLegajo()
                    + " | Actividad#" + actividad.getId();
        }
    }
}
