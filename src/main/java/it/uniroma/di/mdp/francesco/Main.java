package it.uniroma.di.mdp.francesco;

import com.formdev.flatlaf.FlatLightLaf;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Classe principale dell'applicazione Rome Bus Tracker.
 * Gestisce l'interfaccia grafica, il caricamento dei dati GTFS e l'aggiornamento delle posizioni dei bus.
 */
public class Main {
    /**
     * Visualizzatore della mappa.
     */
    private static JXMapViewer mapViewer;
    /**
     * Parametri globali dell'applicazione.
     */
    private static final GlobalParameters gp = new GlobalParameters();
    /**
     * Pannello laterale sinistro per la ricerca e gestione preferiti.
     */
    private static JPanel leftPanel;
    /**
     * Label di stato (online/offline).
     */
    private static JLabel statusLabel;
    /**
     * Collezione di tutte le fermate caricate.
     */
    private static Stops allStops;
    /**
     * Icona per stato offline.
     */
    private static final Icon redCircleIcon = new ImageIcon("./icon/redCircle.png");
    /**
     * Icona per stato online.
     */
    private static final Icon greenCircleIcon = new ImageIcon("./icon/greenCircle.png");
    /**
     * Collezione di tutte le linee caricate.
     */
    public static Routes allRoutes;
    /**
     * Collezione di tutte le corse caricate.
     */
    public static Trips allTrips;
    /**
     * Collezione di tutti gli orari di fermata caricati.
     */
    private static StopTimes allStopTimes;
    /**
     * RouteId corrente, risultato del filtro di ricerca.
     */
    private static String currentRouteId;
    /**
     * Flag che indica se l'applicazione è in modalità online.
     */
    private static boolean isOnline;
    /**
     * Gestore dei preferiti (linee e fermate).
     */
    private static final Favourites favourites = new Favourites();

    /**
     * Metodo principale di avvio dell'applicazione.
     *
     * @param args argomenti da linea di comando (non usati)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        // Carica i dati GTFS STATICI
        allRoutes = new Routes();
        allRoutes.loadFromFile(gp.getFolderPath() + "/routes.txt");
        allStops = new Stops();
        allStops.loadFromFile(gp.getFolderPath() + "/stops.txt");
        allTrips = new Trips();
        allTrips.loadFromFile(gp.getFolderPath() + "/trips.txt");
        allStopTimes = new StopTimes();
        allStopTimes.loadFromFile(gp.getFolderPath() + "/stop_times.txt");
        currentRouteId = "";
        statusLabel = new JLabel(); // label stato online/offline
        isOnline = OnlineStatusChecker.isOnline();
        SwingUtilities.invokeLater(() -> {
            if (isOnline) {
                statusLabel.setText("Online");
                statusLabel.setIcon(greenCircleIcon);
            } else {
                statusLabel.setText("Offline");
                statusLabel.setIcon(redCircleIcon);
            }
            JFrame frame = new JFrame(gp.FRAME_TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(gp.FRAME_WIDTH, gp.FRAME_HEIGHT);
            frame.setLayout(new BorderLayout());

            JPanel navigationPanel = createNavigationPanel();
            leftPanel = createLeftPanel();
            leftPanel.setBackground(gp.LEFT_PANEL_COLOR);
            leftPanel.setVisible(false);

            JPanel mainPanel = new JPanel();
            mainPanel.setBackground(Color.WHITE);

            frame.add(navigationPanel, BorderLayout.SOUTH);
            frame.add(leftPanel, BorderLayout.WEST);
            frame.add(mainPanel, BorderLayout.CENTER);

            // Inizializzazione della mappa e della cache
            File cacheDir = new File("./tileCache");
            TileFactoryInfo info = new OSMTileFactoryInfo();
            DefaultTileFactory tileFactory = new DefaultTileFactory(info);
            tileFactory.setLocalCache(new FileBasedLocalCache(cacheDir, false));

            mapViewer = new JXMapViewer();
            mapViewer.setTileFactory(tileFactory);
            mapViewer.setAddressLocation(new GeoPosition(gp.ROME_CENTER_LAT, gp.ROME_CENTER_LON));
            mapViewer.setZoom(gp.getMapZoom());

            frame.add(mapViewer);
            frame.setVisible(true);

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                /**
                 * Aggiorna periodicamente le posizioni dei bus e lo stato online/offline.
                 */
                @Override
                public void run() {
                    updateBusPositions(); // aggiorna le posizioni dei bus nella mappa
                    updateStatus(); // verifica se è connesso online
                }
            }, 0, gp.TIMER_DELAY_MS);
        });
    }

    /**
     * Crea il pannello di navigazione con i bottoni di ricerca, pan e zoom.
     *
     * @return il pannello di navigazione
     */
    private static JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(gp.NAVIGATION_PANEL_COLOR);
        JButton searchButton = new JButton("Ricerca");
        searchButton.setPreferredSize(new Dimension(gp.SEARCH_BUTTON_SIZE));
        JButton upButton = createButton("↑");
        JButton downButton = createButton("↓");
        JButton leftButton = createButton("←");
        JButton rightButton = createButton("→");

        upButton.addActionListener(e -> panMap(0, gp.PAN_DELTA));
        downButton.addActionListener(e -> panMap(0, -gp.PAN_DELTA));
        leftButton.addActionListener(e -> panMap(-gp.PAN_DELTA, 0));
        rightButton.addActionListener(e -> panMap(gp.PAN_DELTA, 0));
        searchButton.addActionListener(e -> {
            leftPanel.setVisible(!leftPanel.isVisible());
            leftPanel.revalidate();
            leftPanel.repaint();
            if (leftPanel.isVisible()) {
                searchButton.setText("Chiudi ricerca");
            } else {
                searchButton.setText("Ricerca");
            }
        });

        panel.add(searchButton);
        panel.add(upButton);
        panel.add(downButton);
        panel.add(leftButton);
        panel.add(rightButton);

        JPanel zoomPanel = createZoomPanel();
        panel.add(zoomPanel);
        panel.add(statusLabel);
        return panel;
    }

    /**
     * Crea il pannello per il controllo dello zoom della mappa.
     *
     * @return il pannello di zoom
     */
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

    /**
     * Crea un bottone con testo e dimensione standard.
     *
     * @param text testo del bottone
     * @return il bottone creato
     */
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(gp.BUTTON_SIZE);
        return button;
    }

    /**
     * Crea il pannello laterale per la ricerca di fermate e linee e la gestione dei preferiti.
     *
     * @return il pannello di ricerca e preferiti
     */
    private static JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JTextField searchField = new JTextField(10);
        searchField.setMaximumSize(new Dimension(200, 30));
        searchField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JButton searchButton = new JButton("Cerca");
        searchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFocusable(false);
        resultArea.setHighlighter(null);
        resultArea.setDragEnabled(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setOpaque(false);
        resultArea.setBorder(null);
        panel.setPreferredSize(new Dimension(500, 40));
        JLabel searchLabel = new JLabel("Inserire nome o codice fermata/linea:");
        searchLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        searchField.setPreferredSize(new Dimension(100, 30));
        panel.add(Box.createVerticalStrut(20));
        panel.add(searchLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(searchField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(searchButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(resultArea);

        // Bottoni per i preferiti
        JButton addFavButton = new JButton("Aggiungi ai preferiti");
        JButton removeFavButton = new JButton("Rimuovi dai preferiti");
        JButton showFavButton = new JButton("Mostra preferiti");
        addFavButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeFavButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        showFavButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(addFavButton);
        panel.add(removeFavButton);
        panel.add(showFavButton);

        // Lista preferiti
        DefaultListModel<String> favListModel = new DefaultListModel<>();
        JList<String> favList = new JList<>(favListModel);
        favList.setVisibleRowCount(5);
        JScrollPane favScroll = new JScrollPane(favList);
        favScroll.setVisible(false);
        panel.add(favScroll);

        JTable tripTable = new JTable();
        JScrollPane tableScroll = new JScrollPane(tripTable);
        panel.add(tableScroll);

        // Listener per invio su campo ricerca
        searchField.addActionListener(e -> {
            if (leftPanel.isVisible()) {
                searchButton.doClick();
            }
        });

        // Listener per ricerca di fermata o linea
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim().toUpperCase();
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Inserisci il nome o il codice di una fermata");
                return;
            }

            Route foundRoute;
            Stop foundStop = allStops.searchStop(searchText);
            if (foundStop != null) {
                resultArea.setText("Fermata trovata:\n" + foundStop.getStopName() + "\n" + "ID: " + foundStop.getStopId());
                currentRouteId = "";
                foundStop.print();
                Set<BusWaypoint> waypoints = new HashSet<>();
                waypoints.add(new BusWaypoint(Double.parseDouble(foundStop.getStopLat()), Double.parseDouble(foundStop.getStopLon())));
                WaypointPainter<BusWaypoint> painter = new WaypointPainter<>();
                painter.setWaypoints(waypoints);
                mapViewer.setAddressLocation(new GeoPosition(Double.parseDouble(foundStop.getStopLat()), Double.parseDouble(foundStop.getStopLon())));
                mapViewer.setOverlayPainter(painter);

                List<StopTime> stopTimeListOfStopId = allStopTimes.getStoptimesFromStopId(foundStop.getStopId(), 30);
                tripTable.setVisible(true);
                tableScroll.setVisible(true);
                String[] columns = {"Linea", "Corsa", "Orario di arrivo", "Attesa (minuti)", "Tipologia"};
                Object[][] data = new Object[stopTimeListOfStopId.size()][columns.length];

                for (int i = 0; i < stopTimeListOfStopId.size(); i++) {
                    LocalDateTime ora = LocalDateTime.now();
                    StopTime st = stopTimeListOfStopId.get(i);
                    Duration differenza = Duration.between(ora, st.getArrivalDateTime());
                    long minuti = differenza.toMinutes();
                    String curRouteId = allTrips.getRouteIdFromTripId(st.getTripId());
                    Route curRoute = allRoutes.searchRoute(curRouteId);
                    data[i][0] = curRouteId;
                    data[i][1] = st.getTripId();
                    data[i][2] = st.getArrivalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("d/M/yy HH:mm:ss"));
                    data[i][3] = minuti;
                    data[i][4] = curRoute.getRouteTypeDesc();
                }

                tripTable.setModel(new DefaultTableModel(data, columns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                tripTable.setCellSelectionEnabled(false);
                tripTable.setRowSelectionAllowed(false);
                tripTable.setColumnSelectionAllowed(false);
                tripTable.setFocusable(false);
            } else if ((foundRoute = allRoutes.searchRoute(searchText)) != null) {
                resultArea.setText("Linea trovata:\n" + foundRoute.getRouteId() + "\n");
                currentRouteId = foundRoute.getRouteId();
                List<StopTime> stopTimeListOfTripId = new ArrayList<StopTime>();
                List<Trip> tripsOfRoute = allTrips.getTripListFromRouteId(foundRoute.getRouteId());
                for (Trip elemento : tripsOfRoute) {
                    StopTime appST = allStopTimes.getStoptimesFromTripId(elemento.getTripId(), 1);
                    if (appST != null)
                        stopTimeListOfTripId.add(appST);
                }

                Set<BusWaypoint> stopWaypoints = new HashSet<>();

                tripTable.setVisible(true);
                tableScroll.setVisible(true);
                String[] columns = {"Corsa", "Direzione", "Fermata", "Orario previsto", "Tipologia"};
                Object[][] data = new Object[stopTimeListOfTripId.size()][columns.length];
                for (int i = 0; i < stopTimeListOfTripId.size(); i++) {
                    StopTime st = stopTimeListOfTripId.get(i);
                    String curRouteId = allTrips.getRouteIdFromTripId(st.getTripId());
                    Route curRoute = allRoutes.searchRoute(curRouteId);
                    Trip curTrip = allTrips.searchTrip(st.getTripId());
                    Stop curStop = allStops.searchStop(st.getStopId());

                    stopWaypoints.add(new BusWaypoint(Double.valueOf(curStop.getStopLat()), Double.valueOf(curStop.getStopLon()), curRouteId + " - " + curTrip.getTripHeadSign(), curRoute.getRouteType()));

                    data[i][0] = st.getTripId();
                    data[i][1] = curTrip.getTripHeadSign();
                    data[i][2] = curStop.getStopName();
                    data[i][3] = st.getArrivalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("d/M/yy HH:mm:ss"));
                    data[i][4] = curRoute.getRouteTypeDesc();
                }
                tripTable.setModel(new DefaultTableModel(data, columns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                tripTable.setCellSelectionEnabled(false);
                tripTable.setRowSelectionAllowed(false);
                tripTable.setColumnSelectionAllowed(false);
                tripTable.setFocusable(false);

                if (!isOnline)
                    displayBusWaypoints(stopWaypoints);

            } else {
                JOptionPane.showMessageDialog(panel, "Nessuna corrispondenza trovata");
            }
        });

        // Aggiorna lista preferiti
        Runnable updateFavList = () -> {
            favListModel.clear();
            favourites.getFavouriteLines().forEach((id, name) -> favListModel.addElement("Linea: " + id + " - " + name));
            favourites.getFavouriteStops().forEach((id, name) -> favListModel.addElement("Fermata: " + id + " - " + name));
        };

        // Mostra/nascondi preferiti
        showFavButton.addActionListener(e -> {
            boolean isVisible = !favScroll.isVisible();
            favScroll.setVisible(isVisible);
            if (isVisible) {
                updateFavList.run(); // aggiorna la lista solo quando la si mostra
                showFavButton.setText("Chiudi preferiti");
            } else {
                showFavButton.setText("Mostra preferiti");
            }
            panel.revalidate();
            panel.repaint();
        });

        // Click su preferito: simula ricerca
        favList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && favList.getSelectedValue() != null) {
                String selected = favList.getSelectedValue();
                if (selected.startsWith("Linea: ")) {
                    String id = selected.split(" ")[1];
                    searchField.setText(id);
                    searchButton.doClick();
                } else if (selected.startsWith("Fermata: ")) {
                    String id = selected.split(" ")[1];
                    searchField.setText(id);
                    searchButton.doClick();
                }
            }
        });

        // Aggiungi ai preferiti
        addFavButton.addActionListener(e -> {
            String searchText = searchField.getText().trim().toUpperCase();
            if (searchText.isEmpty()) return;
            Stop foundStop = allStops.searchStop(searchText);
            Route foundRoute = allRoutes.searchRoute(searchText);
            if (foundStop != null) {
                favourites.addStop(foundStop.getStopId(), foundStop.getStopName());
                JOptionPane.showMessageDialog(panel, "Fermata aggiunta ai preferiti!");
            } else if (foundRoute != null) {
                favourites.addLine(foundRoute.getRouteId(), foundRoute.getRouteId());
                JOptionPane.showMessageDialog(panel, "Linea aggiunta ai preferiti!");
            } else {
                JOptionPane.showMessageDialog(panel, "Nessuna fermata o linea trovata.");
            }
            updateFavList.run();
        });

        // Rimuovi dai preferiti
        removeFavButton.addActionListener(e -> {
            String searchText = searchField.getText().trim().toUpperCase();
            if (searchText.isEmpty()) return;
            favourites.removeLine(searchText);
            favourites.removeStop(searchText);
            JOptionPane.showMessageDialog(panel, "Rimosso dai preferiti (se presente).");
            updateFavList.run();
        });

        return panel;
    }

    /**
     * Sposta la mappa di una certa quantità in latitudine e longitudine.
     *
     * @param deltaLon spostamento longitudine
     * @param deltaLat spostamento latitudine
     */
    private static void panMap(double deltaLon, double deltaLat) {
        GeoPosition pos = mapViewer.getCenterPosition();
        mapViewer.setCenterPosition(new GeoPosition(pos.getLatitude() + deltaLat, pos.getLongitude() + deltaLon));
    }

    /**
     * Aggiorna le posizioni dei bus sulla mappa tramite il feed GTFS-RT.
     */
    private static void updateBusPositions() {
        Set<BusWaypoint> busWaypoints = GTFSFetcher.fetchBusPositions(currentRouteId);
        displayBusWaypoints(busWaypoints);
    }

    /**
     * Aggiorna lo stato online/offline dell'applicazione.
     */
    private static void updateStatus() {
        isOnline = OnlineStatusChecker.isOnline();
        if (isOnline) {
            statusLabel.setText("Online");
            statusLabel.setIcon(greenCircleIcon);
        } else {
            statusLabel.setText("Offline");
            statusLabel.setIcon(redCircleIcon);
        }
    }

    /**
     * Visualizza i waypoint dei bus sulla mappa.
     *
     * @param waypoints insieme di waypoint da visualizzare
     */
    private static void displayBusWaypoints(Set<BusWaypoint> waypoints) {
        WaypointPainter<Waypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(waypoints);
        painter.setRenderer(new CustomWaypointRenderer());
        mapViewer.setOverlayPainter(painter);
    }
}