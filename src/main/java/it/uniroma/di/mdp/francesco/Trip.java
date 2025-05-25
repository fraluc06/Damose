package it.uniroma.di.mdp.francesco;

// classe rappresentante un trip
public class Trip {

    private String tripId; // id della corsa
    private String tripHeadSign; // insegna della corsa es. "VALLE GIULIA"
    private String routeId; // route_id del trip_id ovvero linea servita dalla corsa es. linea tram 3

    public String getTripId() {
        return tripId;
    }

    public String getTripHeadSign() {
        return tripHeadSign;
    }

    public String getRouteId() {
        return routeId;
    }


    public Trip(String tripId,String tripHeadSign,String routeId) {
        this.tripId = tripId;
        this.tripHeadSign = tripHeadSign;
        this.routeId = routeId;
    }
}


