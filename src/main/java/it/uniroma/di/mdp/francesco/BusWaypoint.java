package it.uniroma.di.mdp.francesco;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

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

    public BusWaypoint(double lat, double lon,String label,String routeType) { // aggiunto per personalizzazione etichetta

        this.position = new GeoPosition(lat, lon);
        this.label = label;
        this.routeType = routeType;
    }

    @Override
    public GeoPosition getPosition() {
        return position;
    }
}
