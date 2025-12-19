package exception;

public class ArchivosPersistencia extends Exception {
    public ArchivosPersistencia(String mensaje) {
        super(mensaje);
    }

    public ArchivosPersistencia(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
