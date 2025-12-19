package service;

import model.Anime;

public class FiltroCalificacionMin implements IFiltroAnime {
    private final int calificacionMin;

    public FiltroCalificacionMin(int calificacionMin) {
        if(calificacionMin <= 1 || calificacionMin > 5) {
            throw new IllegalArgumentException("El calificacion debe ser entre 1 y 5");
        }
        this.calificacionMin = calificacionMin;
    }

    @Override
    public boolean cumple(Anime anime) {
        return anime.getCalificacion() >= calificacionMin;
    }

}
