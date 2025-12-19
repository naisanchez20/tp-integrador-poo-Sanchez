package repository;

import model.Anime;
import exception.ArchivosPersistencia;
import java.util.List;

public interface IAnimeRepository {
    void guardar(Anime anime) throws ArchivosPersistencia;

    Anime buscarPorTitulo (String titulo) throws ArchivosPersistencia;


    List<Anime> obtenerTodas() throws ArchivosPersistencia;

    void eliminar(String titulo) throws ArchivosPersistencia;


    boolean existe(String titulo) throws ArchivosPersistencia;

    void actualizar(Anime anime) throws ArchivosPersistencia;
}
