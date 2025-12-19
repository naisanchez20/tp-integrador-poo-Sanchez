package exception;

public class ValidacionExcepcion extends Exception {
    public ValidacionExcepcion(String mensaje) {
        super(mensaje);
    }
    public ValidacionExcepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
