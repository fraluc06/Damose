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

    public static void main(String[] args) {
        StaticGTFSDownloader downloader = new StaticGTFSDownloader(gp.getFileURL(), gp.getMd5URL(), gp.getFolderPath());
        try {
            downloader.downloadAndUnzipIfNeeded();
            System.out.println("File scaricato ed estratto se necessario.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(gp.FRAME_TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(gp.FRAME_WIDTH, gp.FRAME_HEIGHT);
            frame.setLayout(new BorderLayout());

            JPanel navigationPanel = createNavigationPanel();
            JPanel leftPanel = new JPanel();
            leftPanel.setBackground(gp.LEFT_PANEL_COLOR);
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

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

    private static JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(gp.NAVIGATION_PANEL_COLOR);

        JButton upButton = createButton("↑");
        JButton downButton = createButton("↓");
        JButton leftButton = createButton("←");
        JButton rightButton = createButton("→");

        upButton.addActionListener(e -> panMap(0, gp.PAN_DELTA));
        downButton.addActionListener(e -> panMap(0, -gp.PAN_DELTA));
        leftButton.addActionListener(e -> panMap(-gp.PAN_DELTA, 0));
        rightButton.addActionListener(e -> panMap(gp.PAN_DELTA, 0));

        panel.add(upButton);
        panel.add(downButton);
        panel.add(leftButton);
        panel.add(rightButton);

        JPanel zoomPanel = createZoomPanel();
        panel.add(zoomPanel);

        return panel;
    }

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

    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(gp.BUTTON_SIZE);
        return button;
    }

    private static void panMap(double deltaLon, double deltaLat) {
        GeoPosition pos = mapViewer.getCenterPosition();
        mapViewer.setCenterPosition(new GeoPosition(pos.getLatitude() + deltaLat, pos.getLongitude() + deltaLon));
    }

    private static void updateBusPositions() {
        List<GeoPosition> busPositions = GTFSFetcher.fetchBusPositions();
        displayBusPositions(busPositions);
    }

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
