package service;

import model.Anime;
import service.IEstrategiaOrden;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class OrdenAnio implements IEstrategiaOrden{

    @Override
    public List<Anime> ordenar(List<Anime> animes) {
        List<Anime> resultado = new ArrayList<>(animes);
        resultado.sort(getComparator());
        return resultado;
    }
    @Override
    public Comparator<Anime> getComparator() {
        return Comparator.comparingInt(Anime::getAnioLanzamiento).reversed()
                .thenComparing(Anime::getTitulo);
    }

}
