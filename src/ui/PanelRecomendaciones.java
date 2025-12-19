package ui;
import model.Anime;
import service.AnimeService;
import model.Genero;
import service.IEstrategiaRecomendacion;
import service.RecomendacionCalifi;
import service.RecomendacionGenero;
import exception.ArchivosPersistencia;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelRecomendaciones extends JPanel {
    private final AnimeService animeService;
    private AnimeTablaModel tablaModel;
    private JTable tablaAnime;
    private JComboBox<String> cmbTipoRecomendacion;
    private JComboBox<Genero> cmbGenero;
    private JSpinner spnCantidad;
    private JButton btnGenerar;
    public PanelRecomendaciones(AnimeService animeService) {
        this.animeService = animeService;
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
    }


    private void inicializarComponentes() {
        cmbTipoRecomendacion = new JComboBox<>(new String[]{
                "Los mejores", "Calificados segun genero"
        });
        cmbGenero = new JComboBox<>(Genero.values());
        spnCantidad = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1));
        btnGenerar = new JButton("Generar Recomendaciones");
        tablaModel = new AnimeTablaModel();
        tablaAnime = new JTable(tablaModel);
        tablaAnime.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel panelControles = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;
        panelControles.add(new JLabel("Tipo de Recomendacion:"), gbc);
        gbc.gridx = 1;
        panelControles.add(cmbTipoRecomendacion, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panelControles.add(new JLabel("Genero si corresponde:"), gbc);
        gbc.gridx = 1;
        panelControles.add(cmbGenero, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        panelControles.add(new JLabel("Cuantas?:"), gbc);
        gbc.gridx = 1;
        panelControles.add(spnCantidad, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelControles.add(btnGenerar, gbc);
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Recomendaciones"));
        panelTabla.add(new JScrollPane(tablaAnime), BorderLayout.CENTER);
        add(panelControles, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
    }
    private void configurarEventos() {
        btnGenerar.addActionListener(e -> generarRecomendaciones());
    }
    private void generarRecomendaciones() {
        try {
            String tipo = (String) cmbTipoRecomendacion.getSelectedItem();
            int cantidad = (Integer) spnCantidad.getValue();

            IEstrategiaRecomendacion estrategia;

            if ("Calificados segun genero".equals(tipo)) {
                Genero genero = (Genero) cmbGenero.getSelectedItem();
                estrategia = new RecomendacionGenero(genero);
            } else {
                estrategia = new RecomendacionCalifi();
            }

            List<Anime> recomendaciones = animeService.generarRecomendacion(estrategia, cantidad);
            tablaModel.setAnimes(recomendaciones);

            if (recomendaciones.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron resultados con esos criterios",
                        "INFO", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (ArchivosPersistencia e) {
            JOptionPane.showMessageDialog(this,
                    "Error al generar recomendacion: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
