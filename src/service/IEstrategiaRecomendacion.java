package service;

import model.Anime;

import java.util.List;

public interface IEstrategiaRecomendacion {
    List<Anime> recomendar(List<Anime> animes, int cantidad);
}

