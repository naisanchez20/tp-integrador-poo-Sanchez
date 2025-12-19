package model;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;



public class ListaPersonalizada implements Serializable{
    private static final long serialVersionUID = 1L;
    private String nombre;
    private Set<Anime> animes;

    public ListaPersonalizada(String nombre) {
        this.setNombre(nombre);
        this.animes = new HashSet<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la lista no puede estar vacio");
        }
        this.nombre = nombre.trim();
    }



    public Set<Anime> getAnimes() {
        return new HashSet<>(animes);
    }

    public void agregarAnime(Anime anime) {
        if (anime == null) {
            throw new IllegalArgumentException("El anime no puede ser nulo");
        }
        animes.add(anime);
    }

    public void removerAnime(Anime anime) {
        animes.remove(anime);
    }

    public boolean contieneAnime(Anime anime) {
        return animes.contains(anime);
    }

    public int cantidadAnimes() {
        return animes.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ListaPersonalizada that = (ListaPersonalizada) obj;
        return Objects.equals(nombre.toLowerCase(), that.nombre.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre.toLowerCase());
    }

    @Override
    public String toString() {
        return String.format("%s (%d anime)", nombre, cantidadAnimes());
    }
}

