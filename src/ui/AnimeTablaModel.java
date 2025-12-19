package ui;

import model.Anime;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;



public class AnimeTablaModel extends AbstractTableModel {
    private static final String[] COLUMNAS = {
            "Titulo", "Año", "Estudio", "Capitulos", "Estado", "Calificacion", "Generos"
    };

    private List<Anime> animes;
    public AnimeTablaModel() {
        this.animes = new ArrayList<>();
    }

    public AnimeTablaModel(List<Anime> animes) {
        this.animes = new ArrayList<>(animes);
    }
    public void setAnimes(List<Anime> animes) {
        this.animes = new ArrayList<>(animes != null ? animes : new ArrayList<>());
        fireTableDataChanged();
    }

    public Anime getAnimeAt(int row) {
        if (row >= 0 && row < animes.size()) {
            return animes.get(row);
        }
        return null;
    }

    @Override
    public int getRowCount() {
        return animes.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNAS[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        Anime anime = animes.get(row);
        switch (column) {
            case 0: return anime.getTitulo();
            case 1: return anime.getAnioLanzamiento();
            case 2: return anime.getEstudio();
            case 3: return anime.getCantidadCapitulos();
            case 4: return anime.getEstado().getDescripcion();
            case 5: return anime.getCalificacion();
            case 6: return anime.getGeneros().toString().replaceAll("[\\[\\]]", "");
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 1:
            case 3:
            case 5:
                return Integer.class;
            default:
                return String.class;
        }
    }



}

