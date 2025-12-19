package service;

import exception.AnimeNoEncontrado;
import exception.ArchivosPersistencia;
import exception.ListaNoEncontrada;
import exception.ListaYaExistente;
import model.Anime;
import model.ListaPersonalizada;
import repository.AnimeRepository;
import repository.ListaPersonalRepository;

import java.util.List;

public class ListaPersonalizadaService {
    private final ListaPersonalRepository listaRepository;
    private final AnimeRepository animeRepository;

    public ListaPersonalizadaService(ListaPersonalRepository listaRepository,
                                     AnimeRepository animeRepository) {
        if (listaRepository == null || animeRepository == null) {
            throw new IllegalArgumentException("Los repositorios no pueden ser nulos");
        }
        this.listaRepository = listaRepository;
        this.animeRepository = animeRepository;
    }
    public void crearLista(String nombre) throws ListaYaExistente, ArchivosPersistencia {
        if (listaRepository.existe(nombre)) {
            throw new ListaYaExistente(nombre);
        }
        ListaPersonalizada lista = new ListaPersonalizada(nombre);
        listaRepository.guardar(lista);
    }
    public void agregarAnimeALista(String nombreLista, String tituloAnime)
            throws ListaNoEncontrada, AnimeNoEncontrado, ArchivosPersistencia {

        ListaPersonalizada lista = listaRepository.buscarPorNombre(nombreLista);
        if (lista == null) {
            throw new ListaNoEncontrada(nombreLista);
        }

        Anime anime = animeRepository.buscarPorTitulo(tituloAnime);
        if (anime == null) {
            throw new AnimeNoEncontrado(tituloAnime);
        }

        lista.agregarAnime(anime);
        listaRepository.actualizar(lista);
    }
    public void removerAnimeDeLista(String nombreLista, String tituloAnime)
            throws ListaNoEncontrada, AnimeNoEncontrado, ArchivosPersistencia {

        ListaPersonalizada lista = listaRepository.buscarPorNombre(nombreLista);
        if (lista == null) {
            throw new ListaNoEncontrada(nombreLista);
        }

        Anime anime = animeRepository.buscarPorTitulo(tituloAnime);
        if (anime == null) {
            throw new AnimeNoEncontrado(tituloAnime);
        }

        lista.removerAnime(anime);
        listaRepository.actualizar(lista);
    }
    public void eliminarLista(String nombre) throws ListaNoEncontrada, ArchivosPersistencia {
        if (!listaRepository.existe(nombre)) {
            throw new ListaNoEncontrada(nombre);
        }
        listaRepository.eliminar(nombre);
    }
    public List<ListaPersonalizada> listarTodas() throws ArchivosPersistencia {
        return listaRepository.obtenerTodas();
    }
    public ListaPersonalizada obtenerLista(String nombre) throws ListaNoEncontrada, ArchivosPersistencia {
        ListaPersonalizada lista = listaRepository.buscarPorNombre(nombre);
        if (lista == null) {
            throw new ListaNoEncontrada(nombre);
        }
        return lista;
    }
}
