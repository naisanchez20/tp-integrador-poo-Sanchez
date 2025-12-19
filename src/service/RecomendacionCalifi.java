package service;
import java.util.List;
import java.util.stream.Collectors;
import model.Anime;


public class RecomendacionCalifi implements IEstrategiaRecomendacion {

    @Override
    public List<Anime> recomendar(List<Anime> animes, int cantidad) {
        return animes.stream()
                .sorted((a1, a2) -> Integer.compare(a2.getCalificacion(), a1.getCalificacion()))
                .limit(cantidad)
                .collect(Collectors.toList());
    }
}
