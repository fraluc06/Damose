package it.uniroma.di.mdp.francesco;

import java.awt.Color;
import java.awt.Dimension;
import java.nio.file.Path;

public class GlobalParameters {
    private int mapZoom;

    // Parametri costanti pubblici statici
    public final String FRAME_TITLE = "Rome Bus Tracker";
    public final int FRAME_WIDTH = 1000;
    public final int FRAME_HEIGHT = 600;
    public final Color NAVIGATION_PANEL_COLOR = new Color(200, 200, 200);
    public final Color LEFT_PANEL_COLOR = Color.WHITE;
    public final double ROME_CENTER_LAT = 41.8868;
    public final double ROME_CENTER_LON = 12.5043;
    public final int TIMER_DELAY_MS = 30000;

    // Nuovi parametri aggiunti
    public final double PAN_DELTA = 0.01;
    public final Dimension BUTTON_SIZE = new Dimension(60, 30);

    // URL e path per GTFS (aggiungi qui i tuoi valori reali)
    private final String FILE_URL = "https://romamobilita.it/sites/default/files/rome_static_gtfs.zip";
    private final String MD5_URL = "https://romamobilita.it/sites/default/files/rome_static_gtfs.zip.md5";
    private final Path FOLDER_PATH = java.nio.file.Paths.get("rome_static_gtfs");

    public String getFileURL() {
        return FILE_URL;
    }

    public String getMd5URL() {
        return MD5_URL;
    }

    public Path getFolderPath() {
        return FOLDER_PATH;
    }

    public int getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(int mapZoom) {
        this.mapZoom = mapZoom;
    }

    public GlobalParameters() {
        mapZoom = 4;
    }
}
