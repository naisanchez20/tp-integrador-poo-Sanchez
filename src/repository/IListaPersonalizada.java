package repository;

import exception.ArchivosPersistencia;
import model.ListaPersonalizada;

import java.util.List;

public interface IListaPersonalizada {
    void guardar(ListaPersonalizada lista) throws ArchivosPersistencia;

    ListaPersonalizada buscarPorNombre(String nombre) throws ArchivosPersistencia;

    List<ListaPersonalizada> obtenerTodas() throws ArchivosPersistencia;

    void eliminar(String nombre) throws ArchivosPersistencia;

    boolean existe(String nombre) throws ArchivosPersistencia;
    void actualizar(ListaPersonalizada lista) throws ArchivosPersistencia;


}
