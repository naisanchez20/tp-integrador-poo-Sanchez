package service;

import java.util.List;
import java.util.stream.Collectors;
import model.Anime;
import model.Genero;
public class RecomendacionGenero implements IEstrategiaRecomendacion {
    private final Genero genero;


    public RecomendacionGenero(Genero genero) {
        if (genero == null) {
            throw new IllegalArgumentException("El genero no puede ser nulo");
        }
        this.genero = genero;
    }

    @Override
    public List<Anime> recomendar(List<Anime> animes, int cantidad) {
        return animes.stream()
                .filter(anime -> anime.tieneGenero(genero))
                .sorted((a1, a2) -> Integer.compare(a2.getCalificacion(), a1.getCalificacion()))
                .limit(cantidad)
                .collect(Collectors.toList());
    }

}
