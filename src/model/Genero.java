package model;

public enum Genero {
    SHONEN("Shonen"),
    SHOJO("Shojo"),
    SEINEN("Seinen"),
    JOSEI("Josei"),
    MECHA("Mecha"),
    ISEKAI("Isekai"),
    SLICE_OF_LIFE("Slice of Life"),
    ACCION("Accion"),
    AVENTURA("Aventura"),
    COMEDIA("Comedia"),
    DRAMA("Drama"),
    ROMANCE("Romance"),
    FANTASIA("Fantasia"),
    CIENCIA_FICCION("Ciencia Ficcion"),
    HORROR("Horror"),
    DEPORTES("Deportes");

    private final String nombre;

    Genero(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
