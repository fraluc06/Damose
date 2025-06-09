package it.uniroma.di.mdp.francesco;

/**
 * Classe che rappresenta una corsa (trip) nel sistema GTFS.
 * <p>
 * Ogni oggetto Trip contiene informazioni identificative sulla corsa,
 * l'insegna (headsign), la linea servita (route_id) e lo stato corrente
 * relativo alla fermata attuale e a quella di destinazione.
 */
public class Trip {

    /**
     * ID della corsa.
     */
    private final String tripId;

    /**
     * Insegna della corsa (es. "VALLE GIULIA").
     */
    private final String tripHeadSign;

    /**
     * ID della linea servita dalla corsa (route_id).
     */
    private final String routeId;

    /**
     * ID della fermata corrente della corsa.
     */
    private String currentStopId;

    /**
     * Sequenza della fermata corrente della corsa.
     */
    private int currentStopSequence;

    /**
     * Sequenza della fermata di destinazione (target).
     * La differenza con currentStopSequence permette di calcolare
     * quante fermate mancano alla destinazione.
     */
    private int targetStopSequence;

    /**
     * Costruttore della classe Trip.
     *
     * @param tripId        ID della corsa
     * @param tripHeadSign  insegna della corsa
     * @param routeId       ID della linea servita dalla corsa
     * @param currentStopId ID della fermata corrente
     */
    public Trip(String tripId, String tripHeadSign, String routeId, String currentStopId) {
        this.tripId = tripId;
        this.tripHeadSign = tripHeadSign;
        this.routeId = routeId;
        this.currentStopId = currentStopId;
        this.currentStopSequence = 0;
        this.targetStopSequence = 0;
    }

    /**
     * Restituisce l'ID della corsa.
     *
     * @return ID della corsa
     */
    public String getTripId() {
        return tripId;
    }

    /**
     * Restituisce l'insegna della corsa.
     *
     * @return insegna della corsa
     */
    public String getTripHeadSign() {
        return tripHeadSign;
    }

    /**
     * Restituisce l'ID della linea servita dalla corsa.
     *
     * @return ID della linea
     */
    public String getRouteId() {
        return routeId;
    }

    /**
     * Restituisce l'ID della fermata corrente della corsa.
     *
     * @return ID della fermata corrente
     */
    public String getCurrentStopId() {
        return currentStopId;
    }

    /**
     * Imposta l'ID della fermata corrente della corsa.
     *
     * @param currentStopId nuovo ID della fermata corrente
     */
    public void setCurrentStopId(String currentStopId) {
        this.currentStopId = currentStopId;
    }

    /**
     * Restituisce la sequenza della fermata corrente.
     *
     * @return sequenza della fermata corrente
     */
    public int getCurrentStopSequence() {
        return currentStopSequence;
    }

    /**
     * Imposta la sequenza della fermata corrente.
     *
     * @param currentStopSequence nuova sequenza della fermata corrente
     */
    public void setCurrentStopSequence(int currentStopSequence) {
        this.currentStopSequence = currentStopSequence;
    }

    /**
     * Restituisce la sequenza della fermata di destinazione.
     *
     * @return sequenza della fermata di destinazione
     */
    public int getTargetStopSequence() {
        return targetStopSequence;
    }

    /**
     * Imposta la sequenza della fermata di destinazione.
     *
     * @param targetStopSequence nuova sequenza della fermata di destinazione
     */
    public void setTargetStopSequence(int targetStopSequence) {
        this.targetStopSequence = targetStopSequence;
    }
}