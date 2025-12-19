package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Anime implements Serializable {
    private static final long serialVersionUID = 1L;
    private String titulo;
    private int anioLanzamiento;
    private String estudio;
    private int cantidadCapitulos;
    private Estado estado;
    private int calificacion;
    private Set<Genero> generos;

    public Anime(String titulo, int anioLanzamiento, String estudio,
                 int cantidadCapitulos, Estado estado, int calificacion) {
        this.setTitulo(titulo);
        this.setAnioLanzamiento(anioLanzamiento);
        this.setEstudio(estudio);
        this.setCantidadCapitulos(cantidadCapitulos);
        this.setEstado(estado);
        this.setCalificacion(calificacion);
        this.generos = new HashSet<>();
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }
        this.titulo = titulo.trim();
    }
    public int getAnioLanzamiento() {
        return anioLanzamiento;
    }
    public void setAnioLanzamiento(int anioLanzamiento) {
        if (anioLanzamiento < 1900 || anioLanzamiento > 2100) {
            throw new IllegalArgumentException("El año de lanzamiento debe ser entre 1900 y 2100");
        }
        this.anioLanzamiento = anioLanzamiento;
    }

    public String getEstudio() {
        return estudio;
    }

    public void setEstudio(String estudio) {
        if (estudio == null || estudio.trim().isEmpty()) {
            throw new IllegalArgumentException("El estudio no puede estar vacio");
        }
        this.estudio = estudio.trim();
    }

    public int getCantidadCapitulos() {
        return cantidadCapitulos;
    }
    public void setCantidadCapitulos(int cantidadCapitulos) {
        if (cantidadCapitulos <= 0){
            throw new IllegalArgumentException("La cantidad de capitulos debe ser mayor a 0");
        }
        this.cantidadCapitulos = cantidadCapitulos;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        this.estado = estado;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificacion debe estar entre 1 y 5");
        }
        this.calificacion = calificacion;
    }
    public Set<Genero> getGeneros() {
        return new HashSet<>(generos);
    }

    public void agregarGenero(Genero genero) {
        if (genero == null) {
            throw new IllegalArgumentException("El genero no puede ser nulo");
        }
        this.generos.add(genero);
    }

    public void removerGenero(Genero genero) {
        this.generos.remove(genero);
    }

    public boolean tieneGenero(Genero genero) {
        return generos.contains(genero);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Anime anime = (Anime) obj;
        return Objects.equals(titulo.toLowerCase(), anime.titulo.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo.toLowerCase());
    }

    @Override
    public String toString() {
        return String.format("%s (%d) - %s", titulo, anioLanzamiento, estado);
    }

}
