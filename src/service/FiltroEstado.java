package service;

import model.Anime;
import model.Estado;
import service.IFiltroAnime;

public class FiltroEstado implements IFiltroAnime {
    private final Estado estado;
    public FiltroEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");

        }
        this.estado = estado;
    }


    @Override
    public boolean cumple(Anime anime) {
        return anime.getEstado() == estado;
    }
}
