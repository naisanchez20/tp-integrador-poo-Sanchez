package exception;

public class ListaYaExistente  extends Exception {
    public ListaYaExistente(String nombre) {
        super("Ya existe la lista: " + nombre);
    }
}
