package service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.Anime;



public class OrdenTitulo implements IEstrategiaOrden {

    @Override
    public List<Anime> ordenar(List<Anime> animes) {
        List<Anime> resultado = new ArrayList<>(animes);
        resultado.sort(getComparator());
        return resultado;
    }

    @Override
    public Comparator<Anime> getComparator() {
        return Comparator.comparing(Anime::getTitulo, String.CASE_INSENSITIVE_ORDER);
    }
}