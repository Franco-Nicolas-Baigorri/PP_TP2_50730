package excepciones;

// Originalmente extends RuntimeException, por lineamientos
// de la cátedra no se ocupa dado que debe ser una Excepción
// chequeada

/// Excepción al exceder el cupo de una inscripción
public class CupoExcedidoException extends Exception {
    private static final long serialVersionUID = 1L;

    public CupoExcedidoException(String message) {
        super(message);
    }
}
