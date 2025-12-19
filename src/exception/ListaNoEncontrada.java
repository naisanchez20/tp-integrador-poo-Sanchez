package exception;

public class ListaNoEncontrada extends Exception {
    public ListaNoEncontrada(String nombre) {
        super("No se encontro la lista: " + nombre);
    }
}
