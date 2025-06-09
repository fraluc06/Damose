package it.uniroma.di.mdp.francesco;

import java.time.LocalDateTime;

/**
 * Classe che rappresenta un orario di fermata (stop_time) nel sistema GTFS.
 * <p>
 * Ogni oggetto StopTime contiene informazioni relative a una specifica fermata di una corsa,
 * inclusi l'ID della corsa, l'ID della fermata, la sequenza della fermata, l'orario di arrivo
 * (sia come stringa che come LocalDateTime) e la distanza percorsa dal capolinea.
 */
public class StopTime {

    /**
     * ID della corsa (trip).
     */
    private final String tripId;

    /**
     * ID della fermata (stop).
     */
    private final String stopId;

    /**
     * Numero progressivo della fermata nel trip.
     */
    private final String stopSequence;

    /**
     * Orario di arrivo alla fermata (formato stringa originale, es. "23:45:00").
     */
    private final String arrivalTime;

    /**
     * Orario di arrivo alla fermata in formato LocalDateTime.
     */
    private LocalDateTime arrivalDateTime;

    /**
     * Distanza percorsa dal capolinea (shape_dist_traveled).
     */
    private final String shapeDistTraveled;

    /**
     * Costruttore della classe StopTime.
     * <p>
     * Converte l'orario di arrivo in formato LocalDateTime, gestendo anche orari oltre le 23 (giorno successivo).
     *
     * @param tripId            ID della corsa
     * @param stopId            ID della fermata
     * @param stopSequence      numero progressivo della fermata nel trip
     * @param arrivalTime       orario di arrivo alla fermata (formato stringa "HH:mm:ss")
     * @param shapeDistTraveled distanza percorsa dal capolinea
     */
    public StopTime(String tripId, String stopId, String stopSequence, String arrivalTime, String shapeDistTraveled) {
        this.tripId = tripId;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
        this.arrivalTime = arrivalTime;
        this.shapeDistTraveled = shapeDistTraveled;

        try {
            // Converte l'arrivalTime nel tipo LocalDateTime
            String appArrivalTime = this.arrivalTime;
            String h = appArrivalTime.substring(0, 2);
            int h_i = Integer.parseInt(h);
            String m = appArrivalTime.substring(3, 5);
            int m_i = Integer.parseInt(m);
            String s = appArrivalTime.substring(6, 8);
            int s_i = Integer.parseInt(s);
            LocalDateTime adesso = LocalDateTime.now();

            if (h_i > 23) {
                // Se notazione ora >23 è un'ora del giorno dopo
                int delta_h = h_i - 23;
                arrivalDateTime = LocalDateTime.of(adesso.getYear(), adesso.getMonthValue(), adesso.getDayOfMonth(), delta_h, m_i, s_i).plusDays(1);
            } else {
                arrivalDateTime = LocalDateTime.of(adesso.getYear(), adesso.getMonthValue(), adesso.getDayOfMonth(), h_i, m_i, s_i);
            }
        } catch (Exception e) {
            System.out.println("Ora arrivalTime scartata: " + arrivalTime);
        }
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
     * Restituisce l'ID della fermata.
     *
     * @return ID della fermata
     */
    public String getStopId() {
        return stopId;
    }

    /**
     * Restituisce il numero progressivo della fermata nel trip.
     *
     * @return sequenza della fermata
     */
    public String getStopSequence() {
        return stopSequence;
    }

    /**
     * Restituisce l'orario di arrivo alla fermata (formato stringa).
     *
     * @return orario di arrivo
     */
    public String getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Restituisce la distanza percorsa dal capolinea.
     *
     * @return distanza percorsa
     */
    public String getShapeDistTraveled() {
        return shapeDistTraveled;
    }

    /**
     * Restituisce l'orario di arrivo alla fermata in formato LocalDateTime.
     *
     * @return orario di arrivo come LocalDateTime, oppure null se non parsabile
     */
    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }
}