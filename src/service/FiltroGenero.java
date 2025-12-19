package service;

import model.Anime;
import model.Genero;

public class FiltroGenero implements IFiltroAnime {
    private final Genero genero;

    public FiltroGenero(Genero genero) {
        if(genero == null) {
            throw new IllegalArgumentException("El genero debe ser nulo");
        }
        this.genero = genero;
    }

    @Override
    public boolean cumple(Anime anime) {
        return anime.tieneGenero(genero);
    }
}
