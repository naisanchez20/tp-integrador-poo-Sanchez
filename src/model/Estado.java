package model;

public enum Estado {
    POR_VER("Por ver"),
    VIENDO("Viendo"),
    FINALIZADO("Finalizado"),
    ABANDONADO("Abandonado");
    private final String descripcion;


    Estado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    @Override
    public String toString() {
        return descripcion;
    }
}
