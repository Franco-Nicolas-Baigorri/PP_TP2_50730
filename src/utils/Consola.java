package utils;

/** Identifica qué hilo produce cada línea de la demostración. */
public final class Consola {
    private Consola() {
    }

    public static void mostrar(String mensaje) {
        String prefijo = "[" + Thread.currentThread().getName() + "] ";
        System.out.println(prefijo + mensaje.replace("\n", "\n" + prefijo));
    }
}
