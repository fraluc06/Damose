package it.uniroma.di.mdp.francesco;

/**
 * Rappresenta una fermata (stop) del sistema GTFS.
 * <p>
 * Ogni oggetto Stop contiene informazioni identificative e geografiche
 * relative a una fermata del trasporto pubblico, come l'ID, il nome,
 * la latitudine e la longitudine.
 */
public class Stop {

    /**
     * Identificativo univoco della fermata.
     */
    private final String stopId;

    /**
     * Nome descrittivo della fermata.
     */
    private final String stopName;

    /**
     * Latitudine della fermata (in formato stringa).
     */
    private final String stopLat;

    /**
     * Longitudine della fermata (in formato stringa).
     */
    private final String stopLon;

    /**
     * Costruisce una nuova fermata con i dati specificati.
     *
     * @param stopId   identificativo della fermata
     * @param stopName nome della fermata
     * @param stopLat  latitudine della fermata
     * @param stopLon  longitudine della fermata
     */
    public Stop(String stopId, String stopName, String stopLat, String stopLon) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.stopLat = stopLat;
        this.stopLon = stopLon;
    }

    /**
     * Restituisce l'identificativo della fermata.
     *
     * @return ID della fermata
     */
    public String getStopId() {
        return stopId;
    }

    /**
     * Restituisce il nome della fermata.
     *
     * @return nome della fermata
     */
    public String getStopName() {
        return stopName;
    }

    /**
     * Restituisce la latitudine della fermata come stringa.
     *
     * @return latitudine della fermata
     */
    public String getStopLat() {
        return stopLat;
    }

    /**
     * Restituisce la longitudine della fermata come stringa.
     *
     * @return longitudine della fermata
     */
    public String getStopLon() {
        return stopLon;
    }

    /**
     * Restituisce la latitudine della fermata come valore double.
     *
     * @return latitudine in formato double
     */
    public double getDoubleStopLat() {
        return Double.valueOf(stopLat);
    }

    /**
     * Restituisce la longitudine della fermata come valore double.
     *
     * @return longitudine in formato double
     */
    public double getDoubleStopLon() {
        return Double.valueOf(stopLon);
    }

    /**
     * Stampa su console le informazioni della fermata.
     */
    public void print() {
        System.out.println("Stop ID: " + stopId);
        System.out.println("Stop Name: " + stopName);
        System.out.println("Stop Latitude: " + stopLat);
        System.out.println("Stop Longitude: " + stopLon);
    }
}