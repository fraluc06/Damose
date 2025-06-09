package it.uniroma.di.mdp.francesco;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

import static it.uniroma.di.mdp.francesco.Main.allRoutes;

/**
 * Rappresenta un waypoint di un autobus sulla mappa.
 * Ogni waypoint può essere associato a una posizione geografica, un'etichetta personalizzata,
 * un tipo di mezzo (es. bus, tram) e, opzionalmente, a un oggetto Trip.
 * Permette la personalizzazione dell'etichetta e dell'icona in base al tipo di mezzo.
 */
public class BusWaypoint implements Waypoint {
    /**
     * Posizione geografica del waypoint.
     */
    private final GeoPosition position;
    /**
     * Etichetta personalizzata del waypoint (es. numero linea, destinazione).
     */
    private final String label;
    /**
     * Tipo di mezzo associato (es. bus, tram, ecc.).
     */
    private final String routeType;
    /**
     * Oggetto Trip associato al waypoint, se presente.
     */
    private final Trip trip;

    /**
     * Restituisce l'oggetto Trip associato al waypoint, se presente.
     *
     * @return il Trip associato, oppure null se non presente
     */
    public Trip getTrip() {
        return trip;
    }

    /**
     * Restituisce l'etichetta associata al waypoint.
     *
     * @return l'etichetta del waypoint
     */
    public String getLabel() {
        return label;
    }

    /**
     * Restituisce il tipo di mezzo associato al waypoint.
     *
     * @return il tipo di mezzo (es. bus, tram, ecc.)
     */
    public String getRouteType() {
        return routeType;
    }

    /**
     * Costruttore base. Crea un waypoint senza etichetta, tipo di mezzo o trip associato.
     *
     * @param lat latitudine della posizione
     * @param lon longitudine della posizione
     */
    public BusWaypoint(double lat, double lon) {
        this.position = new GeoPosition(lat, lon);
        this.label = "";
        this.routeType = "";
        this.trip = null;
    }

    /**
     * Costruttore utilizzato in modalità offline.
     * Permette di specificare etichetta e tipo di mezzo.
     *
     * @param lat       latitudine della posizione
     * @param lon       longitudine della posizione
     * @param label     etichetta personalizzata del waypoint
     * @param routeType tipo di mezzo associato
     */
    public BusWaypoint(double lat, double lon, String label, String routeType) {
        this.position = new GeoPosition(lat, lon);
        this.label = label;
        this.routeType = routeType;
        this.trip = null;
    }

    /**
     * Costruttore utilizzato in modalità online.
     * L'etichetta e il tipo di mezzo vengono ricavati dal trip e dalla route associata.
     *
     * @param lat  latitudine della posizione
     * @param lon  longitudine della posizione
     * @param trip oggetto Trip associato al waypoint
     */
    public BusWaypoint(double lat, double lon, Trip trip) {
        this.position = new GeoPosition(lat, lon);
        Route route = allRoutes.searchRoute(trip.getRouteId());
        this.label = route.getRouteId() + " (" + trip.getTripId() + ") -" + trip.getTripHeadSign();
        this.routeType = route.getRouteType();
        this.trip = trip;
    }

    /**
     * Restituisce la posizione geografica del waypoint.
     *
     * @return la posizione geografica come oggetto GeoPosition
     */
    @Override
    public GeoPosition getPosition() {
        return position;
    }
}