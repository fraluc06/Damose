package it.uniroma.di.mdp.francesco;

/**
 * Classe che rappresenta una corsa (trip) nel sistema GTFS.
 * Contiene informazioni sull'ID della corsa, l'insegna e la linea servita.
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

    public String getCurrentStopId() {
        return currentStopId;
    }

    public void setCurrentStopId(String currentStopId) {
        this.currentStopId = currentStopId;
    }

    // fermata corrente della corsa
    private  String currentStopId;

    public int getCurrentStopSequence() {
        return currentStopSequence;
    }

    public void setCurrentStopSequence(int currentStopSequence) {
        this.currentStopSequence = currentStopSequence;
    }

    // stop sequence corrente
    private int currentStopSequence;

    public int getTargetStopSequence() {
        return targetStopSequence;
    }

    public void setTargetStopSequence(int targetStopSequence) {
        this.targetStopSequence = targetStopSequence;
    }

    // target stop sequence della fermata di riferimento
    private int targetStopSequence;

    /**
     * Costruttore della classe Trip.
     *
     * @param tripId       ID della corsa
     * @param tripHeadSign insegna della corsa
     * @param routeId      ID della linea servita dalla corsa
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
}