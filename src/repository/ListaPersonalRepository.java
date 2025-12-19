package repository;

import model.ListaPersonalizada;
import repository.IListaPersonalizada;
import exception.ArchivosPersistencia;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ListaPersonalRepository implements IListaPersonalizada {

    private static final String ARCHIVO = "listas.dat";
    private Map<String, ListaPersonalizada> listas;
    public ListaPersonalRepository() {
        this.listas = new HashMap<>();
        cargarDesdeArchivo();
    }

    @SuppressWarnings("unchecked")
    private void cargarDesdeArchivo() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return;
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            listas = (Map<String, ListaPersonalizada>) ois.readObject();
        } catch (FileNotFoundException e) {
            listas = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar las listas "  + e.getMessage(), e);
        }
    }

    private void guardarEnArchivo() throws ArchivosPersistencia  {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(listas);
        } catch (IOException e) {
            throw new ArchivosPersistencia("Error al guardar listas en archivo: " + e.getMessage(), e);
        }

    }

    private String normalizarLista(String nombre) {
        return nombre.toLowerCase().trim();
    }

    @Override
    public void guardar(ListaPersonalizada lista) throws ArchivosPersistencia {
        if (lista == null) {
            throw new ArchivosPersistencia(" No se puede guardar lista nula");
        }
        String clave = normalizarLista(lista.getNombre());
        listas.put(clave, lista);
        guardarEnArchivo();
    }

    @Override
    public ListaPersonalizada buscarPorNombre(String nombre) throws ArchivosPersistencia {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ArchivosPersistencia(" No se puede buscar lista vacia");
        }
        String clave = normalizarLista(nombre);
        return listas.get(clave);
    }

    @Override
    public List<ListaPersonalizada> obtenerTodas() throws ArchivosPersistencia {
        return new ArrayList<>(listas.values());
    }

    @Override
    public void eliminar(String nombre) throws ArchivosPersistencia {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ArchivosPersistencia(" No se puede eliminar lista vacia ");
        }
        String clave = normalizarLista(nombre);
        if (listas.remove(clave) != null) {
            guardarEnArchivo();
        }
    }

    @Override
    public boolean existe(String nombre) throws ArchivosPersistencia {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        String clave = normalizarLista(nombre);
        return listas.containsKey(clave);
    }

    @Override
    public void actualizar(ListaPersonalizada lista) throws ArchivosPersistencia {
        if (lista == null) {
            throw new ArchivosPersistencia(" No se puede actualizar lista vacia");
        }
        String clave = normalizarLista(lista.getNombre());
        if (!listas.containsKey(clave)) {
            throw new ArchivosPersistencia(" No se puede la lista: " + lista.getNombre());
        }
        listas.put(clave, lista);
        guardarEnArchivo();
    }
}
