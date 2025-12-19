package exception;

public class AnimeYaExistente extends Exception {

    public AnimeYaExistente(String titulo) {
        super("Ya existe un anime con el titulo: " + titulo);
    }

    public AnimeYaExistente(String mensaje, Throwable causa ) {
        super(mensaje, causa);
    }
}
