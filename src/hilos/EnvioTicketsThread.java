package hilos;

import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.Inscripcion.TicketDeAcceso;
import modelo.actividades.Actividad;
import utils.Consola;

/** Simula en un hilo independiente el envío de los tickets de un evento. */
public class EnvioTicketsThread extends Thread {
    private static final long PAUSA_ENVIO_MS = 150;

    private final EventoUniversitario evento;
    private int cantidadEnviada;

    public EnvioTicketsThread(EventoUniversitario evento) {
        super("EnvioTickets-" + evento.getId());
        this.evento = evento;
    }

    @Override
    public void run() {
        Consola.mostrar("Comienza el envío simulado del evento " + evento.getId());
        try {
            for (Actividad actividad : evento.getActividades()) {
                for (Inscripcion inscripcion : actividad.getInscripciones()) {
                    TicketDeAcceso ticket = inscripcion.getTicket();
                    if (inscripcion.estaConfirmada() && ticket != null) {
                        // La pausa representa el tiempo que demandaría un envío.
                        Thread.sleep(PAUSA_ENVIO_MS);
                        ticket.enviarTicket();
                        cantidadEnviada++;
                    }
                }
            }
        } catch (InterruptedException excepcion) {
            Thread.currentThread().interrupt();
            Consola.mostrar("Envío interrumpido. Tickets enviados: " + cantidadEnviada);
            return;
        }
        Consola.mostrar("Finalizó el envío del evento " + evento.getId()
                + ". Tickets enviados: " + cantidadEnviada);
    }

    /** Consultar después de join(), cuando el hilo haya terminado. */
    public int getCantidadEnviada() {
        return cantidadEnviada;
    }
}
