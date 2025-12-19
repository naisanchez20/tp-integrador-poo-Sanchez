package ui;

import model.Anime;
import model.Estado;
import model.Genero;
import service.AnimeService;
import exception.AnimeNoEncontrado;
import exception.AnimeYaExistente;
import exception.ArchivosPersistencia;
import exception.ValidacionExcepcion;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelGestionAnime extends JPanel {

    private final AnimeService animeService;
    private AnimeTablaModel tablaModel;
    private JTable tablaAnime;


    private JTextField txtTitulo;
    private JSpinner spnAnio;
    private JTextField txtEstudio;
    private JSpinner spnCapitulos;
    private JComboBox<Estado> cmbEstado;
    private JSpinner spnCalificacion;
    private JList<Genero> lstGeneros;
    private JButton btnAgregar;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public PanelGestionAnime(AnimeService animeService) {
        this.animeService = animeService;
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        cargarAnimes();
    }

    private void inicializarComponentes() {
        txtTitulo = new JTextField(20);
        spnAnio = new JSpinner(new SpinnerNumberModel(2020, 1900, 2100, 1));
        txtEstudio = new JTextField(20);
        spnCapitulos = new JSpinner(new SpinnerNumberModel(12,1,10000, 1));
        cmbEstado = new JComboBox<>(Estado.values());
        spnCalificacion = new JSpinner(new SpinnerNumberModel(3,1,5, 1));
        lstGeneros = new JList<>(Genero.values());
        lstGeneros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        btnAgregar = new JButton("Agregar");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        tablaModel = new AnimeTablaModel();
        tablaAnime = new JTable(tablaModel);
        tablaAnime.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAnime.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tablaAnime.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tablaAnime.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tablaAnime.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);


    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Titulo:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtTitulo, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(spnAnio, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Estudio:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtEstudio, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Capitulos:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(spnCapitulos, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(cmbEstado, gbc);
        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(new JLabel("Calificacion:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(spnCalificacion, gbc);
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panelFormulario.add(new JLabel("Generos:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panelFormulario.add(new JScrollPane(lstGeneros), gbc);



        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(panelBotones, gbc);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Catalogo de Anime"));
        panelTabla.add(new JScrollPane(tablaAnime), BorderLayout.CENTER);
        add(panelFormulario, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);
    }
    private void configurarEventos() {
        btnAgregar.addActionListener(e -> agregarAnime());
        btnModificar.addActionListener(e -> modificarAnime());
        btnEliminar.addActionListener(e -> eliminarAnime());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        tablaAnime.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarAnimeSeleccionado();
            }
        });
    }

    private void agregarAnime() {
        try {
            String titulo = txtTitulo.getText().trim();
            int anio = (Integer) spnAnio.getValue();
            String estudio = txtEstudio.getText().trim();
            int capitulos = (Integer) spnCapitulos.getValue();
            Estado estado = (Estado) cmbEstado.getSelectedItem();
            int calificacion = (Integer) spnCalificacion.getValue();

            animeService.agregarAnime(titulo, anio, estudio, capitulos, estado, calificacion);
            List<Genero> generosSeleccionados = lstGeneros.getSelectedValuesList();
            for (Genero genero : generosSeleccionados) {
                try {
                    animeService.agregarGenero(titulo, genero);
                } catch (AnimeNoEncontrado e) {
                    JOptionPane.showMessageDialog(this,
                            "Error al agregar genero: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            JOptionPane.showMessageDialog(this, "Anime agregado con exito!!",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarAnimes();

        } catch (AnimeYaExistente e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ValidacionExcepcion e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error de validacion", JOptionPane.ERROR_MESSAGE);
        } catch (ArchivosPersistencia e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void modificarAnime() {
        int filaSeleccionada = tablaAnime.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un anime que quiera modificar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Anime animeOriginal = tablaModel.getAnimeAt(filaSeleccionada);
        String tituloOriginal = animeOriginal.getTitulo();

        try {
            String nuevoTitulo = txtTitulo.getText().trim();
            int anio = (Integer) spnAnio.getValue();
            String estudio = txtEstudio.getText().trim();
            int capitulos = (Integer) spnCapitulos.getValue();
            Estado estado = (Estado) cmbEstado.getSelectedItem();
            int calificacion = (Integer) spnCalificacion.getValue();

            animeService.modificarAnime(tituloOriginal, nuevoTitulo, anio, estudio,
                    capitulos, estado, calificacion);

            Anime animeActualizado = animeService.listarTodos().stream()
                    .filter(a -> a.getTitulo().equalsIgnoreCase(nuevoTitulo))
                    .findFirst().orElse(null);

            if (animeActualizado != null) {

                List<Genero> generosSeleccionados = lstGeneros.getSelectedValuesList();
                for (Genero genero : generosSeleccionados) {
                    if (!animeActualizado.tieneGenero(genero)) {
                        try {
                            animeService.agregarGenero(nuevoTitulo, genero);
                        } catch (AnimeNoEncontrado e) {

                            JOptionPane.showMessageDialog(this,
                                    "Error al agregar genero: " + e.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Anime modificado con exito!!",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarAnimes();

        } catch (AnimeNoEncontrado | AnimeYaExistente e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ValidacionExcepcion e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error de validacion", JOptionPane.ERROR_MESSAGE);
        } catch (ArchivosPersistencia e) {
            JOptionPane.showMessageDialog(this,
                    "Error al modificar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarAnime() {
        int filaSeleccionada = tablaAnime.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un anime para eliminar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Anime anime = tablaModel.getAnimeAt(filaSeleccionada);
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Seguro que quiere eliminar \"" + anime.getTitulo() + "\"??",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                animeService.eliminarAnime(anime.getTitulo());
                JOptionPane.showMessageDialog(this, "Anime eliminado con exito!!",
                        "Exito!", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarAnimes();
            } catch (AnimeNoEncontrado e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ArchivosPersistencia e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarAnimeSeleccionado() {
        int filaSeleccionada = tablaAnime.getSelectedRow();
        if (filaSeleccionada >= 0) {
            Anime anime = tablaModel.getAnimeAt(filaSeleccionada);
            txtTitulo.setText(anime.getTitulo());
            spnAnio.setValue(anime.getAnioLanzamiento());
            txtEstudio.setText(anime.getEstudio());
            spnCapitulos.setValue(anime.getCantidadCapitulos());
            cmbEstado.setSelectedItem(anime.getEstado());
            spnCalificacion.setValue(anime.getCalificacion());
            List<Integer> indices = new ArrayList<>();
            Genero[] generos = Genero.values();
            for (Genero genero : anime.getGeneros()) {
                for (int i = 0; i < generos.length; i++) {
                    if (generos[i] == genero) {
                        indices.add(i);
                        break;
                    }
                }
            }
            int[] indicesArray = indices.stream().mapToInt(i -> i).toArray();
            lstGeneros.setSelectedIndices(indicesArray);
        }
    }

    private void limpiarFormulario() {
        txtTitulo.setText("");
        spnAnio.setValue(2020);
        txtEstudio.setText("");
        spnCapitulos.setValue(12);
        cmbEstado.setSelectedIndex(0);
        spnCalificacion.setValue(3);
        lstGeneros.clearSelection();
        tablaAnime.clearSelection();
    }

    public void cargarAnimes() {
        try {
            List<Anime> animes = animeService.listarTodos();
            tablaModel.setAnimes(animes);
        } catch (ArchivosPersistencia e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar anime: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
