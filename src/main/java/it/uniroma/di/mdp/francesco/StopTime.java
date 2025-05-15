package it.uniroma.di.mdp.francesco;

public class StopTime {

    private String tripId;  // id corsa
    private String stopId;  // id fermata
    private String stopSequence;  // numero progressivo della fermata nel trip
    private String arrivalTime;  // orario di arrivo alla fermata
    private String shapeDistTraveled;  // distanza percorsa dal capolinea (ok ?)

    // getter delle variabili
    public String getTripId() {
        return tripId;
    }

    public String getStopId() {
        return stopId;
    }

    public String getStopSequence() {
        return stopSequence;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getShapeDistTraveled() {
        return shapeDistTraveled;
    }



  // costruttore
    public StopTime(String tripId, String stopId, String stopSequence, String arrivalTime, String shapeDistTraveled) {
        this.tripId = tripId;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
        this.arrivalTime = arrivalTime;
        this.shapeDistTraveled = shapeDistTraveled;
    }
}
