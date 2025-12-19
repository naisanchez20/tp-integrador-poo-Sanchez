package ui;

import model.Anime;
import model.ListaPersonalizada;
import service.AnimeService;
import service.ListaPersonalizadaService;
import exception.AnimeNoEncontrado;
import exception.ListaNoEncontrada;
import exception.ListaYaExistente;
import exception.ArchivosPersistencia;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelListasPersonal extends JPanel {

    private final ListaPersonalizadaService listaService;

    private JList<String> lstListas;
    private DefaultListModel<String> modeloListas;
    private AnimeTablaModel tableModel;
    private JTable tablaAnimes;

    private JTextField txtNombreLista;
    private JTextField txtTituloAnime;
    private JButton btnCrearLista;
    private JButton btnEliminarLista;
    private JButton btnAgregarAnime;
    private JButton btnRemoverAnime;
    private JButton btnRefrescar;

    public PanelListasPersonal(ListaPersonalizadaService listaService,
                                     AnimeService animeService) {
        this.listaService = listaService;
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        cargarListas();
    }

    private void inicializarComponentes() {
        modeloListas = new DefaultListModel<>();
        lstListas = new JList<>(modeloListas);
        lstListas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        txtNombreLista = new JTextField(20);
        txtTituloAnime = new JTextField(20);
        btnCrearLista = new JButton("Crear Lista");
        btnEliminarLista = new JButton("Eliminar Lista");
        btnAgregarAnime = new JButton("Agregar Anime");
        btnRemoverAnime = new JButton("Remover Anime");
        btnRefrescar = new JButton("Refreshh");

        tableModel = new AnimeTablaModel();
        tablaAnimes = new JTable(tableModel);
        tablaAnimes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel panelListas = new JPanel(new BorderLayout());
        panelListas.setBorder(BorderFactory.createTitledBorder("Listas Personal"));

        JPanel panelCrearLista = new JPanel(new FlowLayout());
        panelCrearLista.add(new JLabel("Nombre Lista: :"));
        panelCrearLista.add(txtNombreLista);
        panelCrearLista.add(btnCrearLista);
        panelCrearLista.add(btnEliminarLista);

        panelListas.add(panelCrearLista, BorderLayout.NORTH);
        panelListas.add(new JScrollPane(lstListas), BorderLayout.CENTER);
        JPanel panelAnimes = new JPanel(new BorderLayout());
        panelAnimes.setBorder(BorderFactory.createTitledBorder("Resultado de lisa Seleccionada"));
        JPanel panelAgregarAnime = new JPanel(new FlowLayout());
        panelAgregarAnime.add(new JLabel("Titulo Anime:"));
        panelAgregarAnime.add(txtTituloAnime);
        panelAgregarAnime.add(btnAgregarAnime);
        panelAgregarAnime.add(btnRemoverAnime);
        panelAgregarAnime.add(btnRefrescar);

        panelAnimes.add(panelAgregarAnime, BorderLayout.NORTH);
        panelAnimes.add(new JScrollPane(tablaAnimes), BorderLayout.CENTER);
        add(panelListas, BorderLayout.WEST);
        add(panelAnimes, BorderLayout.CENTER);
    }

    private void configurarEventos() {
        btnCrearLista.addActionListener(e -> crearLista());
        btnEliminarLista.addActionListener(e -> eliminarLista());
        btnAgregarAnime.addActionListener(e -> agregarAnimeALista());
        btnRemoverAnime.addActionListener(e -> removerAnimeDeLista());
        btnRefrescar.addActionListener(e -> cargarAnimesDeLista());

        lstListas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarAnimesDeLista();
            }
        });
    }

    private void crearLista() {
        String nombre = txtNombreLista.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre de lista que desee",
                    "Por favor!!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            listaService.crearLista(nombre);
            JOptionPane.showMessageDialog(this, "Lista creada con exito!!",
                    "Genial!!", JOptionPane.INFORMATION_MESSAGE);
            txtNombreLista.setText("");
            cargarListas();
        } catch (ListaYaExistente e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ArchivosPersistencia e) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear lista: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarLista() {
        String nombreLista = lstListas.getSelectedValue();
        if (nombreLista == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una lista que quiera borrar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "Seguro que queres borrar la lista \"" + nombreLista + "\"?",
                "Confirmarcion", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                listaService.eliminarLista(nombreLista);
                JOptionPane.showMessageDialog(this, "Lista eliminada con exito!!",
                        "EliminadOK", JOptionPane.INFORMATION_MESSAGE);
                cargarListas();
                tableModel.setAnimes(List.of());
            } catch (ListaNoEncontrada e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ArchivosPersistencia e) {
                JOptionPane.showMessageDialog(this,
                        "Error al eliminar lista: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarAnimeALista() {
        String nombreLista = lstListas.getSelectedValue();
        if (nombreLista == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una lista",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tituloAnime = txtTituloAnime.getText().trim();
        if (tituloAnime.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el titulo de anime",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            listaService.agregarAnimeALista(nombreLista, tituloAnime);
            JOptionPane.showMessageDialog(this, "Anime agregado con exito!!",
                    "Agregado", JOptionPane.INFORMATION_MESSAGE);
            txtTituloAnime.setText("");
            cargarAnimesDeLista();
        } catch (ListaNoEncontrada | AnimeNoEncontrado e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ArchivosPersistencia e) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar anime: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerAnimeDeLista() {
        String nombreLista = lstListas.getSelectedValue();
        if (nombreLista == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una lista",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int filaSeleccionada = tablaAnimes.getSelectedRow();
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un Anime para remover",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Anime anime = tableModel.getAnimeAt(filaSeleccionada);

        try {
            listaService.removerAnimeDeLista(nombreLista, anime.getTitulo());
            JOptionPane.showMessageDialog(this, "Anime eliminado de la lista con exito!!",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            cargarAnimesDeLista();
        } catch (ListaNoEncontrada | AnimeNoEncontrado e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ArchivosPersistencia e) {
            JOptionPane.showMessageDialog(this,
                    "Error al remover: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarListas() {
        try {
            List<ListaPersonalizada> listas = listaService.listarTodas();
            modeloListas.clear();
            for (ListaPersonalizada lista : listas) {
                modeloListas.addElement(lista.getNombre());
            }
        } catch (ArchivosPersistencia e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar listas: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarAnimesDeLista() {
        String nombreLista = lstListas.getSelectedValue();
        if (nombreLista == null) {
            tableModel.setAnimes(List.of());
            return;
        }

        try {
            ListaPersonalizada lista = listaService.obtenerLista(nombreLista);
            tableModel.setAnimes(new java.util.ArrayList<>(lista.getAnimes()));
        } catch (ListaNoEncontrada | ArchivosPersistencia e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar anime: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

