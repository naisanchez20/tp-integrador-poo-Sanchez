package ui;

import repository.AnimeRepository;
import repository.ListaPersonalRepository;
import service.AnimeService;
import service.ListaPersonalizadaService;

import javax.swing.*;
import java.awt.*;

public class PanelPrincipal  extends JFrame {
    private final AnimeService animeService;
    private final ListaPersonalizadaService listaService;

    public PanelPrincipal(){
        AnimeRepository animeRepository = new AnimeRepository();
        ListaPersonalRepository listaRepository = new ListaPersonalRepository();

        this.animeService = new AnimeService(animeRepository);
        this.listaService = new ListaPersonalizadaService(listaRepository, animeRepository);

        inicializarVentana();
        configurarMenu();
        configurarPaneles();


    }

    private void inicializarVentana() {
        setTitle("Catalogo de Anime---Sistema de Gestion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }
    private void configurarMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo:Animes");
        JMenuItem itemSalir = new JMenuItem("CerrarPrograma");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);
        setJMenuBar(menuBar);
    }


    private void configurarPaneles() {
        JTabbedPane tabbedPane = new JTabbedPane();
        PanelGestionAnime panelGestion = new PanelGestionAnime(animeService);
        tabbedPane.addTab("Gestion de Anime", panelGestion);
        PanelBusqFiltros panelBusqueda = new PanelBusqFiltros(animeService);
        tabbedPane.addTab("Busqueda y Filtrado", panelBusqueda);
        PanelListasPersonal panelListas = new PanelListasPersonal(listaService, animeService);
        tabbedPane.addTab("Listas Personalizadas", panelListas);
        PanelRecomendaciones panelRecomendaciones = new PanelRecomendaciones(animeService);
        tabbedPane.addTab("Recomendaciones", panelRecomendaciones);
        PanelEstadisticas panelEstadisticas = new PanelEstadisticas(animeService);
        tabbedPane.addTab("Estadisticas", panelEstadisticas);
        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            PanelPrincipal ventana = new PanelPrincipal();
            ventana.setVisible(true);
        });
    }
}
