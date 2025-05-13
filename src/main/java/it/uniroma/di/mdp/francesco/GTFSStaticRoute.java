package it.uniroma.di.mdp.francesco;
// classe per la rappresentazione dei dati GTFS statici di una linea (route)
public class GTFSStaticRoute {
    private String routeId;
    //costruttore
    public GTFSStaticRoute(String routeId) {
        this.routeId = routeId;

    }
    // getter del routeId ovvero della linea
    public String getRouteId() {
        return routeId;
    }
}
