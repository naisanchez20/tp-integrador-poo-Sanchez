package repository;

import model.Anime;
import exception.ArchivosPersistencia;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;



public class AnimeRepository implements IAnimeRepository {
    private static final String ARCHIVO= "animes.dat";
    private Map<String, Anime> animes;

    public AnimeRepository() {
        this.animes = new HashMap<>();
        cargarDesdeArchivo();
    }


    @SuppressWarnings("unchecked")
    private void cargarDesdeArchivo() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            animes = (Map<String, Anime>) ois.readObject();
        } catch (FileNotFoundException e) {
            animes = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el anime:  " + e.getMessage(), e);
        }
    }

    private void guardarEnArchivo() throws ArchivosPersistencia {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(animes);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar anime en archivo:  " + e.getMessage(), e);
        }
    }

    private String normalizarTitulo(String titulo) {
        return titulo.toLowerCase().trim();
    }

    @Override
    public void guardar(Anime anime) throws ArchivosPersistencia {
        if (anime == null) {
            throw new ArchivosPersistencia("El anime no puede ser nulo");
        }
        String clave = normalizarTitulo(anime.getTitulo());
        animes.put(clave, anime);
        guardarEnArchivo();
   }

    @Override
    public Anime buscarPorTitulo(String titulo) throws ArchivosPersistencia {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ArchivosPersistencia("El titulo no puede estar vacio");
        }
        String clave = normalizarTitulo(titulo);
        return animes.get(clave);
    }

    @Override
    public List<Anime> obtenerTodas() throws ArchivosPersistencia {
        return new ArrayList<>(animes.values());
    }

    @Override
    public void eliminar(String titulo) throws ArchivosPersistencia {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ArchivosPersistencia("El titulo no puede estar vacio");
        }
        String clave = normalizarTitulo(titulo);
        if (animes.remove(clave) != null) {
            guardarEnArchivo();
        }
    }

    public boolean existe(String titulo) throws ArchivosPersistencia {
        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }
        String clave = normalizarTitulo(titulo);
        return animes.containsKey(clave);
    }

    @Override
    public void actualizar(Anime anime) throws ArchivosPersistencia {
        if (anime == null) {
            throw new ArchivosPersistencia("La actualizacion no se puede hacer en valor vacio");
        }
        String clave = normalizarTitulo(anime.getTitulo());
        if (!animes.containsKey(clave)) {
            throw new ArchivosPersistencia("No existe el titulo del anime: " + anime.getTitulo());
        }
        animes.put(clave, anime);
        guardarEnArchivo();
    }
}