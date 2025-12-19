package service;

import model.Anime;

import java.util.Comparator;
import java.util.List;

public interface IEstrategiaOrden {
    List<Anime> ordenar(List<Anime> animes);
    Comparator<Anime> getComparator();
}
