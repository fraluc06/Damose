package it.uniroma.di.mdp.francesco;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.time.LocalTime;

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
    // metodo per creare il pannello di ricerca
    private static JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JTextField searchField = new JTextField(10);
        searchField.setMaximumSize(new Dimension(200, 30));
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

        searchField.setPreferredSize(new Dimension(100, 30));
        panel.add(searchField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(searchButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(resultArea);
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim().toUpperCase();
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(panel,"Inserisci il nome o il codice di una fermata");
                return;
            }

            Stop foundStop = allStops.searchStop(searchText);
            if (foundStop!=null)
            {
                resultArea.setText("Fermata trovata:\n" + foundStop.getStopName() + "\n" + "ID: " + foundStop.getStopId());
                foundStop.print();
                Set<BusWaypoint> waypoints = new HashSet<>();
                waypoints.add(new BusWaypoint(Double.parseDouble(foundStop.getStopLat()), Double.parseDouble(foundStop.getStopLon())));
                WaypointPainter<BusWaypoint> painter = new WaypointPainter<>();
                painter.setWaypoints(waypoints);
                mapViewer.setAddressLocation(new GeoPosition(Double.parseDouble(foundStop.getStopLat()), Double.parseDouble(foundStop.getStopLon())));
                mapViewer.setOverlayPainter(painter);

                // qui trova tutti gli stoptime che hanno questa fermata
                List<StopTime> stopTimeListOfStopId = allStopTimes.getStoptimesFromStopId(foundStop.getStopId());
                // scansiona la lista degli stoptims restituiti e li stampa in console per debug
                LocalDateTime adesso = LocalDateTime.now();
                LocalDateTime oraArrivo ;
                for (StopTime elemento : stopTimeListOfStopId)
                {
                    try {
                        String appArrivalTime = elemento.getArrivalTime();
                        String h = appArrivalTime.substring(0,2);
                        int h_i = Integer.parseInt(h);
                        String m = appArrivalTime.substring(3,5);
                        int m_i = Integer.parseInt(m);
                        String s = appArrivalTime.substring(6,8);
                        int s_i = Integer.parseInt(s);
                        // System.out.println("arrival : "+h+":"+m+":"+s);

                        switch (h) {
                            case "24":
                            appArrivalTime = "00"+appArrivalTime.substring(2,8);
                            oraArrivo =  LocalDateTime.of(adesso.getYear(),adesso.getMonthValue(),adesso.getDayOfMonth(),0,m_i,s_i);
                            oraArrivo = oraArrivo.plusDays(1);
                                break;
                            case "25":
                            appArrivalTime = "01"+appArrivalTime.substring(2,8);
                            oraArrivo =  LocalDateTime.of(adesso.getYear(),adesso.getMonthValue(),adesso.getDayOfMonth(),1,m_i,s_i);
                            oraArrivo = oraArrivo.plusDays(1);
                                break;
                            case "26":
                            appArrivalTime = "02"+appArrivalTime.substring(2,8);
                            oraArrivo =  LocalDateTime.of(adesso.getYear(),adesso.getMonthValue(),adesso.getDayOfMonth(),2,m_i,s_i);
                            oraArrivo = oraArrivo.plusDays(1);
                                break;
                            default:
                            oraArrivo =  LocalDateTime.of(adesso.getYear(),adesso.getMonthValue(),adesso.getDayOfMonth(),h_i,m_i,s_i);
                                break;
                        }

                        if (  (oraArrivo.isBefore(adesso.plusMinutes(30))) && (oraArrivo.isAfter(adesso.minusMinutes(1)))) // se l'ora prevista di arrivo è tra adesso e i prossimi 30 minuti
                        {
                            Route route = allTrips.getRouteFromTripId(elemento.getTripId());
                            if (route != null)
                                System.out.print(route.getRouteId());
                            System.out.println(" Trip Id: " + elemento.getTripId() + "StopTime: " + elemento.getStopId() + " StopSequence: " + elemento.getStopSequence() + " ArrivalTime: " + elemento.getArrivalTime() + " Dist_traveled: " + elemento.getShapeDistTraveled());

                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }




                }
            }    // fine trovato uno stop con mome corrispondente
            else {
                JOptionPane.showMessageDialog(panel,"Fermata non trovata");
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
