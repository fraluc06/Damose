package it.uniroma.di.mdp.francesco;

import java.awt.*;
import java.nio.file.Path;

/**
 * Classe che gestisce i parametri globali e le costanti di configurazione
 * per l'applicazione Damose.
 * <p>
 * Fornisce valori predefiniti per dimensioni, colori, coordinate, URL e altri parametri
 * utilizzati in tutta l'applicazione.
 * Permette inoltre di gestire dinamicamente il livello di zoom della mappa.
 */
public class GlobalParameters {
    /**
     * Livello di zoom della mappa.
     */
    private int mapZoom;

    /**
     * Titolo della finestra principale dell'applicazione.
     */
    public final String FRAME_TITLE = "Rome Bus Tracker";
    /**
     * Larghezza della finestra principale.
     */
    public final int FRAME_WIDTH = 1300;
    /**
     * Altezza della finestra principale.
     */
    public final int FRAME_HEIGHT = 800;
    /**
     * Colore del pannello di navigazione.
     */
    public final Color NAVIGATION_PANEL_COLOR = new Color(200, 200, 200);
    /**
     * Colore del pannello sinistro.
     */
    public final Color LEFT_PANEL_COLOR = Color.WHITE;
    /**
     * Latitudine del centro di Roma.
     */
    public final double ROME_CENTER_LAT = 41.8868;
    /**
     * Longitudine del centro di Roma.
     */
    public final double ROME_CENTER_LON = 12.5043;
    /**
     * Delay del timer in millisecondi per l'aggiornamento automatico.
     */
    public final int TIMER_DELAY_MS = 15000;
    /**
     * Delta di spostamento per il pan della mappa.
     */
    public final double PAN_DELTA = 0.01;
    /**
     * Dimensione standard dei bottoni.
     */
    public final Dimension BUTTON_SIZE = new Dimension(60, 30);
    /**
     * Dimensione del bottone di ricerca.
     */
    public final Dimension SEARCH_BUTTON_SIZE = new Dimension(120, 30);

    /**
     * URL del file GTFS da scaricare.
     */
    private final String FILE_URL = "https://romamobilita.it/sites/default/files/rome_static_gtfs.zip";
    /**
     * URL del file MD5 per il GTFS.
     */
    private final String MD5_URL = "https://romamobilita.it/sites/default/files/rome_static_gtfs.zip.md5";
    /**
     * Percorso della cartella dove vengono estratti i dati GTFS.
     */
    private final Path FOLDER_PATH = java.nio.file.Paths.get("rome_static_gtfs");

    /**
     * Restituisce l'URL del file GTFS.
     *
     * @return URL del file GTFS
     */
    public String getFileURL() {
        return FILE_URL;
    }

    /**
     * Restituisce l'URL del file MD5 per il GTFS.
     *
     * @return URL del file MD5
     */
    public String getMd5URL() {
        return MD5_URL;
    }

    /**
     * Restituisce il percorso della cartella dei dati GTFS.
     *
     * @return percorso della cartella
     */
    public Path getFolderPath() {
        return FOLDER_PATH;
    }

    /**
     * Restituisce il livello di zoom della mappa.
     *
     * @return livello di zoom attuale
     */
    public int getMapZoom() {
        return mapZoom;
    }

    /**
     * Imposta il livello di zoom della mappa.
     *
     * @param mapZoom livello di zoom da impostare
     */
    public void setMapZoom(int mapZoom) {
        this.mapZoom = mapZoom;
    }

    /**
     * Costruttore della classe.
     * Inizializza il livello di zoom a 3.
     */
    public GlobalParameters() {
        mapZoom = 4;
    }
}