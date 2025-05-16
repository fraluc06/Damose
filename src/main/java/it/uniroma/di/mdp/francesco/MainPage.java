package it.uniroma.di.mdp.francesco;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class MainPage {
    private static final String GTFS_FOLDER = "rome_static_gtfs";
    private static final String GTFS_FILE_URL = "https://romamobilita.it/sites/default/files/rome_static_gtfs.zip";
    private static final String GTFS_MD5_URL = "https://romamobilita.it/sites/default/files/rome_static_gtfs.zip.md5";
    private static final String FRAME_TITLE = "Rome Bus Tracker";
    private static final int FRAME_WIDTH = 1000;
    private static final int FRAME_HEIGHT = 600;
    private static final Color NAVIGATION_PANEL_COLOR = new Color(200, 200, 200);
    private static final Color LEFT_PANEL_COLOR = Color.WHITE;
    private static final double ROME_CENTER_LAT = 41.8868;
    private static final double ROME_CENTER_LON = 12.5043;
    private static final int TIMER_DELAY_MS = 30000;

    private JXMapViewer mapViewer;

    public static void main(String[] args) {
        new MainPage().initializeApplication();
    }

    private void initializeApplication() {
        Path folder = Path.of(GTFS_FOLDER);
        StaticGTFSDownloader downloader = new StaticGTFSDownloader(GTFS_FILE_URL, GTFS_MD5_URL, folder);
        try {
            downloader.downloadAndUnzipIfNeeded();
            System.out.println("File scaricato ed estratto se necessario.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        GlobalParam globalParam = new GlobalParam();
        SwingUtilities.invokeLater(() -> initializeFrame(globalParam));
    }

    private void initializeFrame(GlobalParam globalParam) {
        JFrame frame = new JFrame(FRAME_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(new BorderLayout());

        JPanel navigationPanel = createNavigationPanel();
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(LEFT_PANEL_COLOR);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        frame.add(navigationPanel, BorderLayout.SOUTH);
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(mainPanel, BorderLayout.CENTER);

        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer = new JXMapViewer();
        mapViewer.setTileFactory(tileFactory);
        mapViewer.setAddressLocation(new GeoPosition(ROME_CENTER_LAT, ROME_CENTER_LON));
        mapViewer.setZoom(globalParam.getMapZoom());
        frame.add(mapViewer);

        frame.setVisible(true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateBusPositions();
            }
        }, 0, TIMER_DELAY_MS);
    }

    private JPanel createNavigationPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(NAVIGATION_PANEL_COLOR);
        Dimension buttonSize = new Dimension(60, 30);

        JButton upButton = createButton("↑", buttonSize);
        JButton downButton = createButton("↓", buttonSize);
        JButton leftButton = createButton("←", buttonSize);
        JButton rightButton = createButton("→", buttonSize);

        final double PAN_DELTA = 0.01;
        upButton.addActionListener(e -> panMap(0, PAN_DELTA));
        downButton.addActionListener(e -> panMap(0, -PAN_DELTA));
        leftButton.addActionListener(e -> panMap(-PAN_DELTA, 0));
        rightButton.addActionListener(e -> panMap(PAN_DELTA, 0));

        panel.add(upButton);
        panel.add(downButton);
        panel.add(leftButton);
        panel.add(rightButton);

        JButton zoomInButton = createButton("+", buttonSize);
        JButton zoomOutButton = createButton("-", buttonSize);
        zoomInButton.addActionListener(e -> adjustZoom(-1));
        zoomOutButton.addActionListener(e -> adjustZoom(1));
        panel.add(zoomInButton);
        panel.add(zoomOutButton);

        return panel;
    }

    private JButton createButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        return button;
    }

    private void panMap(double deltaLon, double deltaLat) {
        GeoPosition pos = mapViewer.getCenterPosition();
        mapViewer.setCenterPosition(new GeoPosition(pos.getLatitude() + deltaLat, pos.getLongitude() + deltaLon));
    }

    private void adjustZoom(int delta) {
        int zoom = mapViewer.getZoom();
        int newZoom = zoom + delta;
        if (newZoom >= 1 && newZoom <= 9) {
            mapViewer.setZoom(newZoom);
        }
    }

    private void updateBusPositions() {
        List<GeoPosition> busPositions = GTFSFetcher.fetchBusPositions();
        displayBusPositions(busPositions);
    }

    private void displayBusPositions(List<GeoPosition> positions) {
        Set<BusWaypoint> waypoints = new HashSet<>();
        for (GeoPosition pos : positions) {
            waypoints.add(new BusWaypoint(pos.getLatitude(), pos.getLongitude()));
        }
        WaypointPainter<BusWaypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(painter);
    }
}