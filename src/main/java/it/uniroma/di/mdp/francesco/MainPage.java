package it.uniroma.di.mdp.francesco;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class MainPage {
    private static JXMapViewer mapViewer;
    private static GlobalParameters gp = new GlobalParameters();

    private static Stops allStops;
    private static Routes allRoutes;
    private static Trips allTrips;
    private static StopTimes allStopTimes;

    public static void main(String[] args) {
        StaticGTFSDownloader downloader = new StaticGTFSDownloader(gp.getFileURL(), gp.getMd5URL(), gp.getFolderPath());
        try {
            boolean downloaded = downloader.downloadAndUnzipIfNeeded();
            if (downloaded) {
                System.out.println("Dati non presenti o non aggiornati, sto scaricando i dati...");
                System.out.println("Dati aggiornati.");
            } else {
                System.out.println("Dati già presenti e aggiornati.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //carica i dati GTFS
        allRoutes = new Routes();
        allRoutes.loadFromFile(gp.getFolderPath() + "/routes.txt");
        allStops = new Stops();
        allStops.loadFromFile(gp.getFolderPath() + "/stops.txt");
        allTrips = new Trips();
        allTrips.loadFromFile(gp.getFolderPath() + "/trips.txt");
        allStopTimes = new StopTimes();
        allStopTimes.loadFromFile(gp.getFolderPath() + "/stop_times.txt");


        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(gp.FRAME_TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(gp.FRAME_WIDTH, gp.FRAME_HEIGHT);
            frame.setLayout(new BorderLayout());

            JPanel navigationPanel = createNavigationPanel();
            JPanel leftPanel = createLeftPanel();
            leftPanel.setBackground(gp.LEFT_PANEL_COLOR);
            //leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

            JPanel mainPanel = new JPanel();
            mainPanel.setBackground(java.awt.Color.WHITE);

            frame.add(navigationPanel, BorderLayout.SOUTH);
            frame.add(leftPanel, BorderLayout.WEST);
            frame.add(mainPanel, BorderLayout.CENTER);

            TileFactoryInfo info = new OSMTileFactoryInfo();
            DefaultTileFactory tileFactory = new DefaultTileFactory(info);

            mapViewer = new JXMapViewer();
            mapViewer.setTileFactory(tileFactory);
            mapViewer.setAddressLocation(new GeoPosition(gp.ROME_CENTER_LAT, gp.ROME_CENTER_LON));
            mapViewer.setZoom(gp.getMapZoom());

            frame.add(mapViewer);
            frame.setVisible(true);

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    updateBusPositions();
                }
            }, 0, gp.TIMER_DELAY_MS);
        });
    }
    // metodi per la creazione dei bottoni e dell' interfaccia grafica
    private static JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(gp.NAVIGATION_PANEL_COLOR);
        JButton ricercaButton = new JButton("Ricerca fermata");
        JButton upButton = createButton("↑");
        JButton downButton = createButton("↓");
        JButton leftButton = createButton("←");
        JButton rightButton = createButton("→");

        upButton.addActionListener(e -> panMap(0, gp.PAN_DELTA));
        downButton.addActionListener(e -> panMap(0, -gp.PAN_DELTA));
        leftButton.addActionListener(e -> panMap(-gp.PAN_DELTA, 0));
        rightButton.addActionListener(e -> panMap(gp.PAN_DELTA, 0));
        ricercaButton.addActionListener(e -> {
            createLeftPanel();
        });
        panel.add(ricercaButton);
        panel.add(upButton);
        panel.add(downButton);
        panel.add(leftButton);
        panel.add(rightButton);

        JPanel zoomPanel = createZoomPanel();
        panel.add(zoomPanel);

        return panel;
    }
    // metodo per creare il pannello di zoom
    private static JPanel createZoomPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(gp.NAVIGATION_PANEL_COLOR);

        JButton zoomInButton = createButton("+");
        JButton zoomOutButton = createButton("−");

        zoomInButton.addActionListener(e -> {
            int zoom = mapViewer.getZoom();
            if (zoom > 1) {
                mapViewer.setZoom(zoom - 1);
            }
        });
        zoomOutButton.addActionListener(e -> {
            int zoom = mapViewer.getZoom();
            if (zoom < 9) {
                mapViewer.setZoom(zoom + 1);
            }
        });

        panel.add(zoomInButton);
        panel.add(zoomOutButton);
        return panel;
    }
    // metodo per creare i bottoni
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(gp.BUTTON_SIZE);
        return button;
    }
    private static JPanel createLeftPanel() {
        // Implementa qui la logica per la ricerca delle fermate
        // Puoi usare un JTextField per l'input dell'utente e un JButton per avviare la ricerca
        // Ad esempio:
        JPanel panel = new JPanel();
        JTextField searchField = new JTextField(10);
        JButton searchButton = new JButton("Cerca");
        searchField.setPreferredSize(new Dimension(100, 30));
        panel.add(searchField);
        panel.add(searchButton);
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            Stop foundStop = allStops.searchStop(searchText);
            if (foundStop == null) {
                JOptionPane.showMessageDialog(panel, "Fermata non trovata");
                return;
            }
            foundStop.print();
            Set<BusWaypoint> waypoints = new HashSet<>();
            waypoints.add(new BusWaypoint(Double.parseDouble(foundStop.getStopLat()), Double.parseDouble(foundStop.getStopLon())));
                    WaypointPainter<BusWaypoint> painter = new WaypointPainter<>();
                    painter.setWaypoints(waypoints);
                    mapViewer.setAddressLocation(new GeoPosition(Double.parseDouble(foundStop.getStopLat()), Double.parseDouble(foundStop.getStopLon())));
                    mapViewer.setOverlayPainter(painter);
            // Implementa questo metodo per cercare lo stop
            // Logica per cercare la fermata e centrare la mappa su di essa
            // Puoi usare GTFSFetcher per ottenere le posizioni delle fermate
        });

        return panel;
    }
    // metodo per spostare la mappa
    private static void panMap(double deltaLon, double deltaLat) {
        GeoPosition pos = mapViewer.getCenterPosition();
        mapViewer.setCenterPosition(new GeoPosition(pos.getLatitude() + deltaLat, pos.getLongitude() + deltaLon));
    }
    // metodo per aggiornare le posizioni dei bus
    private static void updateBusPositions() {
        List<GeoPosition> busPositions = GTFSFetcher.fetchBusPositions();
        //displayBusPositions(busPositions);
    }
    // metodo per visualizzare le posizioni dei bus sulla mappa
    private static void displayBusPositions(List<GeoPosition> positions) {
        Set<BusWaypoint> waypoints = new HashSet<>();
        for (GeoPosition pos : positions) {
            waypoints.add(new BusWaypoint(pos.getLatitude(), pos.getLongitude()));
        }
        WaypointPainter<BusWaypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(painter);
    }
}
