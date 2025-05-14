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

    public static void main(String[] args) {
        GlobalParam gp = new GlobalParam();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Rome Bus Tracker");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 600);
            frame.setLayout(new BorderLayout());

            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(new Color(200, 200, 200));

            JPanel leftPanel = new JPanel();
            leftPanel.setBackground(new Color(255, 255, 255)); // semi-trasparente
            //leftPanel.setOpaque(false); // altrimenti non si vede l'effetto
            //leftPanel.setPreferredSize(new Dimension(200, 600));// larghezza 200 px

            JButton upButton = new JButton("↑");       // freccia su
            JButton downButton = new JButton("↓");
            JButton leftButton = new JButton("←");
            JButton rightButton = new JButton("→");
            JButton zoomInButton = new JButton("🔍+"); // lente con +
            JButton zoomOutButton = new JButton("🔍−");

            Dimension buttonSize = new Dimension(60, 30); // larghezza, altezza
            zoomInButton.setPreferredSize(buttonSize);
            zoomOutButton.setPreferredSize(buttonSize);
            upButton.setPreferredSize(buttonSize);
            downButton.setPreferredSize(buttonSize);
            leftButton.setPreferredSize(buttonSize);
            rightButton.setPreferredSize(buttonSize);

            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

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
            bottomPanel.add(zoomInButton);
            zoomInButton.setVisible(true);
            bottomPanel.add(zoomOutButton);
            zoomOutButton.setVisible(true);

            final double PAN_DELTA = 0.01; // grado di spostamento (circa 1 km)

            upButton.addActionListener(e -> {
                GeoPosition pos = mapViewer.getCenterPosition();
                mapViewer.setCenterPosition(new GeoPosition(pos.getLatitude() + PAN_DELTA, pos.getLongitude()));
            });

            downButton.addActionListener(e -> {
                GeoPosition pos = mapViewer.getCenterPosition();
                mapViewer.setCenterPosition(new GeoPosition(pos.getLatitude() - PAN_DELTA, pos.getLongitude()));
            });

            leftButton.addActionListener(e -> {
                GeoPosition pos = mapViewer.getCenterPosition();
                mapViewer.setCenterPosition(new GeoPosition(pos.getLatitude(), pos.getLongitude() - PAN_DELTA));
            });

            rightButton.addActionListener(e -> {
                GeoPosition pos = mapViewer.getCenterPosition();
                mapViewer.setCenterPosition(new GeoPosition(pos.getLatitude(), pos.getLongitude() + PAN_DELTA));
            });

            //eftPanel.setLayout(new GridLayout(0, 1)); // verticale
            bottomPanel.add(upButton);
            upButton.setVisible(true);
            bottomPanel.add(downButton);
            downButton.setVisible(true);
            bottomPanel.add(leftButton);
            leftButton.setVisible(true);
            bottomPanel.add(rightButton);
            rightButton.setVisible(true);


            // Contenuto principale al centro
            JPanel mainPanel = new JPanel();
            mainPanel.setBackground(Color.WHITE);
            frame.add(bottomPanel, BorderLayout.SOUTH);
            frame.add(leftPanel, BorderLayout.WEST);
            frame.add(mainPanel, BorderLayout.CENTER);

            TileFactoryInfo info = new OSMTileFactoryInfo();
            DefaultTileFactory tileFactory = new DefaultTileFactory(info);

            mapViewer = new JXMapViewer();
            mapViewer.setTileFactory(tileFactory);
            mapViewer.setAddressLocation(new GeoPosition(41.8868, 12.5043)); // Rome center

            mapViewer.setZoom(gp.getMapZoom());

            frame.add(mapViewer);
            frame.setVisible(true);

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    updateBusPositions();
                }
            }, 0, 30000); // Refresh every 15 sec
        });

        Trips allTrips = new Trips(); // oggetto lista di tutti i trips
        Routes allRoutes = new Routes(); // creo oggetto lista di tutte le linee statiche
        Stops allStops = new Stops();
        allStops.loadFromFile("./rome_static_gtfs/stops.txt");
        allStops.Print();
        allRoutes.loadFromFile("./rome_static_gtfs/routes.txt");
        //allRoutes.print(); // stampa la lista delle Linee
        allTrips.loadFromFile("./rome_static_gtfs/trips.txt");
        //allTrips.Print("3");
    }

    private static void updateBusPositions() {
        List<GeoPosition> busPositions = GTFSFetcher.fetchBusPositions();
        displayBusPositions(busPositions);
    }

    private static void displayBusPositions(List<GeoPosition> positions) {
        Set<BusWaypoint> waypoints = new HashSet<>();
        for (GeoPosition pos : positions) {
            waypoints.add(new BusWaypoint(pos.getLatitude(), pos.getLongitude()));
            BusWaypoint busWaypoint = new BusWaypoint(pos.getLatitude(), pos.getLongitude());
        }

        WaypointPainter<BusWaypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(painter);
    }
}
