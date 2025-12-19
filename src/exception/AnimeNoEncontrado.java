package exception;

public class AnimeNoEncontrado extends Exception {

    public AnimeNoEncontrado(String titulo) {
        super("No se encontro el anime:" + titulo);
    }
}
