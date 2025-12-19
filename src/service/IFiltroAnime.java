package service;
import model.Anime;

@FunctionalInterface
public interface IFiltroAnime {
    boolean cumple(Anime anime);
}