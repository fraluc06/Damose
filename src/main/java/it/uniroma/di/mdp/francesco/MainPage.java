package it.uniroma.di.mdp.francesco;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class MainPage {
    private static JXMapViewer mapViewer;
    private static GlobalParameters gp = new GlobalParameters();
    private static JPanel leftPanel;

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
        // Carica i dati GTFS
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
            leftPanel = createLeftPanel();
            leftPanel.setBackground(gp.LEFT_PANEL_COLOR);
            leftPanel.setVisible(false);
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
        JButton ricercaButton = new JButton("Ricerca");
        JButton upButton = createButton("↑");
        JButton downButton = createButton("↓");
        JButton leftButton = createButton("←");
        JButton rightButton = createButton("→");

        upButton.addActionListener(e -> panMap(0, gp.PAN_DELTA));
        downButton.addActionListener(e -> panMap(0, -gp.PAN_DELTA));
        leftButton.addActionListener(e -> panMap(-gp.PAN_DELTA, 0));
        rightButton.addActionListener(e -> panMap(gp.PAN_DELTA, 0));
        ricercaButton.addActionListener(e -> {
            leftPanel.setVisible(!leftPanel.isVisible());
            leftPanel.revalidate();
            leftPanel.repaint();
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

    // metodo per creare il pannello di ricerca
    private static JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JTextField searchField = new JTextField(10);
        searchField.setMaximumSize(new Dimension(200, 30));
        searchField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JButton searchButton = new JButton("Cerca");
        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);           // Disattiva modifica
        resultArea.setFocusable(false);          // Niente focus da tastiera
        resultArea.setHighlighter(null);         // Disattiva selezione del testo
        resultArea.setDragEnabled(false);        // Disattiva il drag del testo
        resultArea.setLineWrap(true);            // Vai a capo automatico
        resultArea.setWrapStyleWord(true);       // A capo solo su parole
        resultArea.setOpaque(false);             // Sfondo trasparente
        resultArea.setBorder(null);              // Nessun bordo
        panel.setPreferredSize(new Dimension(500, 40));

        searchField.setPreferredSize(new Dimension(100, 30));
        panel.add(searchField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(searchButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(resultArea);

        JTable tripTable = new JTable();
        JScrollPane tableScroll = new JScrollPane(tripTable);
        panel.add(tableScroll);

        searchField.addActionListener(e -> {
            if (leftPanel.isVisible()) {
                searchButton.doClick();
            }
        });

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim().toUpperCase();
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Inserisci il nome o il codice di una fermata");
                return;
            }

            Route foundRoute;

            Stop foundStop = allStops.searchStop(searchText);
            if (foundStop != null)
            {
                resultArea.setText("Fermata trovata:\n" + foundStop.getStopName() + "\n" + "ID: " + foundStop.getStopId());
                foundStop.print();
                Set<BusWaypoint> waypoints = new HashSet<>();
                waypoints.add(new BusWaypoint(Double.parseDouble(foundStop.getStopLat()), Double.parseDouble(foundStop.getStopLon())));
                WaypointPainter<BusWaypoint> painter = new WaypointPainter<>();
                painter.setWaypoints(waypoints);
                mapViewer.setAddressLocation(new GeoPosition(Double.parseDouble(foundStop.getStopLat()), Double.parseDouble(foundStop.getStopLon())));
                mapViewer.setOverlayPainter(painter);

                // qui trova tutti gli stoptime che hanno questa fermata e arrivano nei prossimi minutesRange minuti
                List<StopTime> stopTimeListOfStopId = allStopTimes.getStoptimesFromStopId(foundStop.getStopId(), 30);
                // scansiona la lista degli stoptimes restituiti e li stampa in una tabella
                tripTable.setVisible(true);
                tableScroll.setVisible(true);
                String[] columns = {"Linea", "Corsa", "Orario di arrivo","Attesa (minuti)","Tipologia"};
                Object[][] data = new Object[stopTimeListOfStopId.size()][columns.length];

                for (int i = 0; i < stopTimeListOfStopId.size(); i++) {
                    LocalDateTime ora = LocalDateTime.now();
                    // Calcola minuti rimanenti all'arrivo
                    StopTime st = stopTimeListOfStopId.get(i);
                    Duration differenza = Duration.between(ora, st.getArrivalDateTime());
                    long minuti = differenza.toMinutes();
                    String curRouteId = allTrips.getRouteIdFromTripId(st.getTripId());
                    Route curRoute =allRoutes.searchRoute(curRouteId);
                    data[i][0] = curRouteId;
                    data[i][1] = st.getTripId();
                    data[i][2] = st.getArrivalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("d/M/yy HH:mm:ss"));
                    data[i][3] = minuti;
                    data[i][4] = curRoute.getRouteTypeDesc();

                }

                tripTable.setModel(new javax.swing.table.DefaultTableModel(data, columns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                tripTable.setCellSelectionEnabled(false);
                tripTable.setRowSelectionAllowed(false);
                tripTable.setColumnSelectionAllowed(false);
                tripTable.setFocusable(false);
            }
            else if  ((foundRoute = allRoutes.searchRoute(searchText)) != null) // altrimenti ricerca tra le linee
            {
                resultArea.setText("Linea trovata:\n" + foundRoute.getRouteId() + "\n");
                // qui trova tutti i trips della linea trovata
                List<StopTime> stopTimeListOfTripId = new ArrayList<StopTime>();
                // DA QUI - - - - CICLA SU TUTTI I TRIP ID DELLA ROUTE
                List<Trip> tripsOfRoute = allTrips.getTripListFromRouteId(foundRoute.getRouteId());
                for (Trip elemento : tripsOfRoute)
                {
                    //String cur_tripId = "1#952-19"; // scansiona la lista dei trip della linea

                    StopTime appST = allStopTimes.getStoptimesFromTripId( elemento.getTripId(), 1);
                    if (appST != null)
                        stopTimeListOfTripId.add(appST);
                }
                // A QUI  - - - - CICLA SU TUTTI I TRIP ID DELLA ROUTE

                Set<BusWaypoint> stopWaypoints = new HashSet<>(); // ma direttamente questo

                tripTable.setVisible(true);
                tableScroll.setVisible(true);
                String[] columns = {"Corsa", "Direzione","Fermata",  "Orario previsto","Tipologia"};
                // crea la tabella con i dati
                Object[][] data = new Object[stopTimeListOfTripId.size()][columns.length];
                System.out.println("size= "+stopTimeListOfTripId.size());
                for (int i = 0; i < stopTimeListOfTripId.size(); i++)
                {
                    StopTime st =  stopTimeListOfTripId.get(i);
                    //data[i][0] = foundRoute.getRouteId();
                    String curRouteId = allTrips.getRouteIdFromTripId(st.getTripId());
                    Route curRoute = allRoutes.searchRoute(curRouteId);
                    Trip curTrip = allTrips.searchTrip(st.getTripId());
                    Stop curStop = allStops.searchStop(st.getStopId()); // lo deve mettere anche nei waypoint


                    stopWaypoints.add(new BusWaypoint(Double.valueOf(curStop.getStopLat()), Double.valueOf(curStop.getStopLon()), curRouteId+" - "+curTrip.getTripHeadSign(),curRoute.getRouteType()));

                    // **********
                    data[i][0] = st.getTripId();
                    data[i][1] = curTrip.getTripHeadSign();
                    data[i][2] = curStop.getStopName();
                    data[i][3] = st.getArrivalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("d/M/yy HH:mm:ss"));
                    data[i][4] = curRoute.getRouteTypeDesc();

                }
                tripTable.setModel(new javax.swing.table.DefaultTableModel(data, columns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                tripTable.setCellSelectionEnabled(false);
                tripTable.setRowSelectionAllowed(false);
                tripTable.setColumnSelectionAllowed(false);
                tripTable.setFocusable(false);

                displayBusWaypoints(stopWaypoints);

            }


            else {
                JOptionPane.showMessageDialog(panel, "Nessuna corrispondenza trovata");
            }
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
        // displayBusPositions(busPositions);
    }

    // metodo per visualizzare le posizioni dei bus sulla mappa - SOSTITUITA DALLA displayBusWaypoints
    private static void displayBusPositions(List<GeoPosition> positions) {
        Set<BusWaypoint> waypoints = new HashSet<>();
        for (GeoPosition pos : positions) {
            waypoints.add(new BusWaypoint(pos.getLatitude(), pos.getLongitude()));
        }


        // PARTE PRECEDENTE
        WaypointPainter<BusWaypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(painter);
    }

    // metodo per visualizzare le posizioni dei bus sulla mappa
    private static void displayBusWaypoints(Set<BusWaypoint> waypoints) {

        // Carica l'icona personalizzata
        Icon myIcon = new ImageIcon("./icon/iconTRAM.png"); // o usa getResource()

        // Crea e applica il renderer
        WaypointPainter<Waypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(waypoints);
        painter.setRenderer(new CustomWaypointRenderer(myIcon));
        mapViewer.setOverlayPainter(painter);


    }
}