package it.uniroma.di.mdp.francesco;
import java.util.HashMap;
import java.util.Map;
// classe per la rappresentazione dei dati GTFS statici di una linea (route)
public class Route {
    private String routeId;
    private String routeType;
    private Map<String, String> mappaTypeDesc;
    //costruttore
    public Route(String routeId,String routeType) {
        this.routeId = routeId;
        this.routeType = routeType;
        mappaTypeDesc = new HashMap<>();
        mappaTypeDesc.put("0", "Tram");
        mappaTypeDesc.put("1", "Metro");
        mappaTypeDesc.put("2", "Treno");
        mappaTypeDesc.put("3", "Bus");

    }
    // getter del routeId ovvero della linea
    public String getRouteId() {
        return routeId;
    }

    public String getRouteType() {
        return routeType;
    }
    public String getRouteTypeDesc() {
        return mappaTypeDesc.get(routeType);
    }
}
