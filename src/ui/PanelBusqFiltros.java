package ui;
import exception.ArchivosPersistencia;
import model.Anime;
import model.Estado;
import model.Genero;
import service.AnimeService;
import service.IEstrategiaOrden;
import service.IFiltroAnime;
import service.FiltroCalificacionMin;
import service.FiltroEstado;
import service.FiltroGenero;
import service.OrdenAnio;
import service.OrdenCalificacion;
import service.OrdenTitulo;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;



public class PanelBusqFiltros extends JPanel {
    private final AnimeService animeService;
    private AnimeTablaModel tablaModel;
    private JTable tablaAnimes;


    private JTextField txtBuscarTitulo;
    private JSpinner spnAnioInicio;
    private JSpinner spnAnioFin;


    private JComboBox<Genero> cmbFiltroGenero;
    private JComboBox<Estado> cmbFiltroEstado;
    private JSpinner spnCalificacionMinima;
    private JCheckBox chkUsarFiltroGenero;
    private JCheckBox chkUsarFiltroEstado;
    private JCheckBox chkUsarFiltroCalificacion;

    private JComboBox<String> cmbOrdenamiento;


    private JButton btnBuscar;
    private JButton btnLimpiar;

    public PanelBusqFiltros(AnimeService animeService) {
        this.animeService = animeService;
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        cargarTodos();
    }


    private void inicializarComponentes() {
        txtBuscarTitulo = new JTextField(20);
        spnAnioInicio = new JSpinner(new SpinnerNumberModel(1900, 1900, 2100, 1));
        spnAnioFin = new JSpinner(new SpinnerNumberModel(2100, 1900, 2100, 1));

        cmbFiltroGenero = new JComboBox<>(Genero.values());
        cmbFiltroEstado = new JComboBox<>(Estado.values());
        spnCalificacionMinima = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        chkUsarFiltroGenero = new JCheckBox("Filtrar por genero");
        chkUsarFiltroEstado = new JCheckBox("Filtrar por estado");
        chkUsarFiltroCalificacion = new JCheckBox("Filtrar por calificacion Min");

        cmbOrdenamiento = new JComboBox<>(new String[]{
                "Sin ordenar", "Por titulo", "Por calificacion", "Por año"
        });

        btnBuscar = new JButton("Buscar/Filtrar");
        btnLimpiar = new JButton("Limpiar");

        tablaModel = new AnimeTablaModel();
        tablaAnimes = new JTable(tablaModel);
        tablaAnimes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel panelControles = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblBusqueda = new JLabel("BUSQUEDA");
        lblBusqueda.setFont(lblBusqueda.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelControles.add(lblBusqueda, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panelControles.add(new JLabel("Titulo:"), gbc);
        gbc.gridx = 1;
        panelControles.add(txtBuscarTitulo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelControles.add(new JLabel("Año desde:"), gbc);
        gbc.gridx = 1;
        panelControles.add(spnAnioInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelControles.add(new JLabel("Año hasta:"), gbc);
        gbc.gridx = 1;
        panelControles.add(spnAnioFin, gbc);


        JLabel lblFiltros = new JLabel("FILTROS");
        lblFiltros.setFont(lblFiltros.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panelControles.add(lblFiltros, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 5;
        panelControles.add(chkUsarFiltroGenero, gbc);
        gbc.gridx = 1;
        panelControles.add(cmbFiltroGenero, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panelControles.add(chkUsarFiltroEstado, gbc);
        gbc.gridx = 1;
        panelControles.add(cmbFiltroEstado, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        panelControles.add(chkUsarFiltroCalificacion, gbc);
        gbc.gridx = 1;
        panelControles.add(spnCalificacionMinima, gbc);



        JLabel lblOrdenamiento = new JLabel("ORDENANIMES");
        lblOrdenamiento.setFont(lblOrdenamiento.getFont().deriveFont(Font.BOLD));
        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        panelControles.add(lblOrdenamiento, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 9;
        panelControles.add(new JLabel("Ordenar por:"), gbc);
        gbc.gridx = 1;
        panelControles.add(cmbOrdenamiento, gbc);
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnBuscar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panelControles.add(panelBotones, gbc);
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Resultados"));
        panelTabla.add(new JScrollPane(tablaAnimes), BorderLayout.CENTER);
        add(panelControles, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);

    }

    private void configurarEventos() {
        btnBuscar.addActionListener(e -> buscarYFiltrar());
        btnLimpiar.addActionListener(e -> limpiar());
    }

    private void buscarYFiltrar() {
        try {
            List<Anime> resultados = new ArrayList<>();

            String titulo = txtBuscarTitulo.getText().trim();
            if (!titulo.isEmpty()) {
                resultados = animeService.buscarPorTitulo(titulo);
            } else {
                int anioInicio = (Integer) spnAnioInicio.getValue();
                int anioFin = (Integer) spnAnioFin.getValue();
                if (anioInicio <= anioFin) {
                    resultados = animeService.buscarPorAnios(anioInicio, anioFin);
                } else {
                    resultados = animeService.listarTodos();
                }
            }
            List<IFiltroAnime> filtros = new ArrayList<>();

            if (chkUsarFiltroGenero.isSelected()) {
                Genero genero = (Genero) cmbFiltroGenero.getSelectedItem();
                filtros.add(new FiltroGenero(genero));
            }

            if (chkUsarFiltroEstado.isSelected()) {
                Estado estado = (Estado) cmbFiltroEstado.getSelectedItem();
                filtros.add(new FiltroEstado(estado));
            }

            if (chkUsarFiltroCalificacion.isSelected()) {
                int calificacionMin = (Integer) spnCalificacionMinima.getValue();
                filtros.add(new FiltroCalificacionMin(calificacionMin));
            }

            if (!filtros.isEmpty()) {
                resultados = animeService.filtrar(filtros);
            }
            IEstrategiaOrden estrategia = obtenerEstrategiaOrdenamiento();
            if (estrategia != null) {
                resultados = animeService.ordenar(resultados, estrategia);
            }
            tablaModel.setAnimes(resultados);

        } catch (ArchivosPersistencia e) {
            JOptionPane.showMessageDialog(this,
                    "Error al buscar: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private IEstrategiaOrden obtenerEstrategiaOrdenamiento() {
        String seleccion = (String) cmbOrdenamiento.getSelectedItem();
        switch (seleccion) {
            case "Por titulo":
                return new OrdenTitulo();
            case "Por calificacion":
                return new OrdenCalificacion();
            case "Por año":
                return new OrdenAnio();
            default:
                return null;
        }
    }
    private void limpiar() {
        txtBuscarTitulo.setText("");
        spnAnioInicio.setValue(1900);
        spnAnioFin.setValue(2100);
        chkUsarFiltroGenero.setSelected(false);
        chkUsarFiltroEstado.setSelected(false);
        chkUsarFiltroCalificacion.setSelected(false);
        cmbOrdenamiento.setSelectedIndex(0);
        cargarTodos();
    }

    private void cargarTodos() {
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
