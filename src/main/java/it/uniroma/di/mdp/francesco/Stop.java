package it.uniroma.di.mdp.francesco;

public class Stop {

    private String stopId; // id identificativo dello stop (fermata)
    private String stopName;  // nome fermata
    private String stopLat; // latitudine fermata
    private String stopLon;  // longitudine fermata

    // getter delle variabili
    public String getStopId() {
        return stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public String getStopLat() {
        return stopLat;
    }

    public String getStopLon() {
        return stopLon;
    }


// costruttore
    public Stop(String stopId,String stopName,String stopLat,String stopLon) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.stopLat = stopLat;
        this.stopLon = stopLon;

    }
}
