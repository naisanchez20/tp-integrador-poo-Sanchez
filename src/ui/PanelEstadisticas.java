package ui;


import model.Estado;
import model.Genero;
import service.AnimeService;
import exception.ArchivosPersistencia;
import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class PanelEstadisticas extends JPanel {
    private final AnimeService animeService;
    private JTextArea txtEstadisticas;
    private JButton btnActualizar;

    public PanelEstadisticas(AnimeService animeService) {
        this.animeService = animeService;
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        actualizarEstadisticas();
    }
    private void inicializarComponentes() {
        txtEstadisticas = new JTextArea(20, 50);
        txtEstadisticas.setEditable(false);
        txtEstadisticas.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        btnActualizar = new JButton("Actualizar Estadisticas");
    }
    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel panelSuperior = new JPanel(new FlowLayout());
        panelSuperior.add(btnActualizar);
        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(txtEstadisticas), BorderLayout.CENTER);
    }
    private void configurarEventos() {
        btnActualizar.addActionListener(e -> actualizarEstadisticas());
    }
    private void actualizarEstadisticas() {
        try {
            StringBuilder sb = new StringBuilder();
            DecimalFormat df = new DecimalFormat("#.##");


            double promedioGlobal = animeService.calculoPromedioCalifi();
            sb.append("*** INFORME: ESTADISTICAS GENERALES ***\n\n");
            sb.append("Promedio del Catalogo gral: ").append(df.format(promedioGlobal)).append("\n\n");

            sb.append("*** Anime por Estado ***\n");
            for (Estado estado : Estado.values()) {
                long cantidad = animeService.contarPorEstado(estado);
                sb.append(String.format("%-15s: %d\n", estado.getDescripcion(), cantidad));
            }
            sb.append("\n");
            sb.append("*** Promedio de Calificacion por Genero ***\n");
            for (Genero genero : Genero.values()) {
                double promedio = animeService.calculoPromedioCalifiGenero(genero);
                if (promedio > 0) {
                    sb.append(String.format("%-20s: %s\n", genero.getNombre(), df.format(promedio)));
                }
            }
            sb.append("\n");

            sb.append("*** Top5 Mas Frecuentes: ***\n");
            List<Genero> topGeneros = animeService.obtenerTopGenero(5);
            for (int i = 0; i < topGeneros.size(); i++) {
                sb.append(String.format("%d. %s\n", i + 1, topGeneros.get(i).getNombre()));
            }

            txtEstadisticas.setText(sb.toString());

        } catch (ArchivosPersistencia e) {
            JOptionPane.showMessageDialog(this,
                    "Error al calcular estadisticas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



}
