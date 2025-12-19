package service;


import model.Anime;
import model.Estado;
import model.Genero;
import repository.AnimeRepository;

import exception.ArchivosPersistencia;
import exception.AnimeNoEncontrado;
import exception.AnimeYaExistente;
import exception.ValidacionExcepcion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class AnimeService {

    private final AnimeRepository repository;

    public AnimeService(AnimeRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("No puede ser nulo");
        }
        this.repository = repository;
    }
    public void agregarAnime(String titulo, int anioLanzamiento, String estudio,
                             int cantidadCapitulos, Estado estado, int calificacion)
            throws AnimeYaExistente, ValidacionExcepcion, ArchivosPersistencia {
        try{
            if (repository.existe(titulo)) {
                throw new AnimeYaExistente(titulo);
            }
            Anime anime = new Anime(titulo,anioLanzamiento, estudio, cantidadCapitulos, estado, calificacion);
            repository.guardar(anime);
        } catch (IllegalArgumentException e) {
            throw new ValidacionExcepcion("Error en validar" + e.getMessage());
        }
    }


    public void modificarAnime(String tituloOriginal, String nuevoTitulo, int anioLanzamiento,
                               String estudio, int cantidadCapitulos, Estado estado, int calificacion)
            throws AnimeNoEncontrado, AnimeYaExistente, ValidacionExcepcion, ArchivosPersistencia {

        Anime anime = repository.buscarPorTitulo(tituloOriginal);
        if (anime == null) {
            throw new AnimeNoEncontrado(tituloOriginal);
        }
        if (!tituloOriginal.equalsIgnoreCase(nuevoTitulo) && repository.existe(nuevoTitulo)) {
            throw new AnimeYaExistente(nuevoTitulo);
        }

        try {
            anime.setTitulo(nuevoTitulo);
            anime.setAnioLanzamiento(anioLanzamiento);
            anime.setEstudio(estudio);
            anime.setCantidadCapitulos(cantidadCapitulos);
            anime.setEstado(estado);
            anime.setCalificacion(calificacion);

            if (!tituloOriginal.equalsIgnoreCase(nuevoTitulo)) {
                repository.eliminar(tituloOriginal);
            }
            repository.actualizar(anime);
        } catch (IllegalArgumentException e) {
            throw new ValidacionExcepcion("Error en validar" + e.getMessage());
        }
    }


    public void eliminarAnime(String titulo) throws AnimeNoEncontrado, ArchivosPersistencia {
        if (!repository.existe(titulo)) {
            throw new AnimeNoEncontrado(titulo);
        }
        repository.eliminar(titulo);
    }

    public List<Anime> listarTodos() throws ArchivosPersistencia{
        return repository.obtenerTodas();
    }

    public List<Anime> buscarPorTitulo(String titulo) throws ArchivosPersistencia{
        if (titulo == null || titulo.trim().isEmpty()) {
            return  listarTodos();
        }

        String tituloLower = titulo.toLowerCase().trim();
        return repository.obtenerTodas().stream()
                .filter(anime -> anime.getTitulo().toLowerCase().contains(tituloLower))
                .collect(Collectors.toList());
    }


    public List<Anime> buscarPorAnios(int anioInicio, int anioFin) throws ArchivosPersistencia{
        return repository.obtenerTodas().stream()
                .filter(anime -> anime.getAnioLanzamiento() >= anioInicio
                        && anime.getAnioLanzamiento() <= anioFin)
                .collect(Collectors.toList());
    }


    public List<Anime> filtrar(List<IFiltroAnime> filtros) throws ArchivosPersistencia{
        List<Anime> todos = repository.obtenerTodas();
        if (filtros == null || filtros.isEmpty()) {
            return todos;
        }
        return todos.stream()
                .filter(anime -> filtros.stream().allMatch(filtro -> filtro.cumple(anime)))
                .collect((Collectors.toList()));
    }

    public List<Anime> ordenar(List<Anime> animes, IEstrategiaOrden estrategia){
        if (estrategia == null) {
            return new ArrayList<>(animes);
        }
        return estrategia.ordenar(animes);
    }

    public List<Anime> generarRecomendacion(IEstrategiaRecomendacion estrategia, int cantidad)
            throws ArchivosPersistencia {
        if (estrategia == null) {
            return new ArrayList<>();
        }
        List<Anime> todos = repository.obtenerTodas();
        return estrategia.recomendar(todos, cantidad);
    }


    public double calculoPromedioCalifi() throws ArchivosPersistencia {
        List<Anime> todos = repository.obtenerTodas();
        if (todos.isEmpty()){
            return 0.0;
        }
        return todos.stream()
                .mapToInt(Anime::getCalificacion)
                .average()
                .orElse(0.0);
    }

    public double calculoPromedioCalifiGenero(Genero genero) throws ArchivosPersistencia {
        List<Anime> todos = repository.obtenerTodas();
        List<Anime> delGenero = todos.stream()
                .filter(anime -> anime.tieneGenero(genero))
                .collect(Collectors.toList());

        if (delGenero.isEmpty()) {
            return 0.0;
        }

        return delGenero.stream()
                .mapToInt(Anime::getCalificacion)
                .average()
                .orElse(0.0);
    }

    public long contarPorEstado(Estado estado) throws ArchivosPersistencia {
        return repository.obtenerTodas().stream()
                .filter(anime -> anime.getEstado() == estado)
                .count();
    }

    public List<Genero> obtenerTopGenero(int cantidad) throws ArchivosPersistencia {
        List<Anime> todos = repository.obtenerTodas();

        java.util.Map<Genero, Long> frecuencia = new java.util.HashMap<>();
        for (Anime anime : todos) {
            for (Genero genero : anime.getGeneros()) {
                frecuencia.put(genero, frecuencia.getOrDefault(genero, 0L) + 1);
            }
        }
        return frecuencia.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(cantidad)
                .map(java.util.Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    public void agregarGenero(String titulo, Genero genero)
            throws AnimeNoEncontrado, ArchivosPersistencia {
        Anime anime = repository.buscarPorTitulo(titulo);
        if (anime == null) {
            throw new AnimeNoEncontrado(titulo);
        }
        anime.agregarGenero(genero);
        repository.actualizar(anime);
    }


}
