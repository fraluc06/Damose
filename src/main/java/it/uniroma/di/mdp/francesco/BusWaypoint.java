package it.uniroma.di.mdp.francesco;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import static it.uniroma.di.mdp.francesco.MainPage.allRoutes;

public class BusWaypoint implements   Waypoint { // messo extends al posto di implements ?
    private final GeoPosition position;

    public String getLabel() {
        return label;
    }

    private final String label; // aggiunto per personalizzazione
    private final String routeType; // tipo di mezzo per modificare l'icona

    public String getRouteType() {
        return routeType;
    }

    public BusWaypoint(double lat, double lon) {
        this.position = new GeoPosition(lat, lon);
        this.label = "";
        this.routeType = "";
    }

    // utilizzato in modalità offline
    public BusWaypoint(double lat, double lon,String label,String routeType) { // aggiunto per personalizzazione etichetta

        this.position = new GeoPosition(lat, lon);
        this.label = label;
        this.routeType = routeType;
    }

    // utilizzato in modalità online
    public BusWaypoint(double lat, double lon, Trip trip) { // aggiunto per personalizzazione etichetta

        this.position = new GeoPosition(lat, lon);
        Route route = allRoutes.searchRoute(trip.getRouteId());
        this.label = route.getRouteId()+" ("+trip.getTripId()+") -"+trip.getTripHeadSign();
        this.routeType = route.getRouteType();

    }

    @Override
    public GeoPosition getPosition() {
        return position;
    }
}
