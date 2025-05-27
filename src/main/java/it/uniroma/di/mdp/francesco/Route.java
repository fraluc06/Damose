package it.uniroma.di.mdp.francesco;

    import java.util.HashMap;
    import java.util.Map;

    /**
     * Classe per la rappresentazione dei dati GTFS statici di una linea (route).
     * Contiene informazioni sull'identificativo della linea e sul tipo di mezzo.
     */
    public class Route {
        /** Identificativo della linea (route). */
        private String routeId;

        /** Tipo della linea (0=Tram, 1=Metro, 2=Treno, 3=Bus). */
        private String routeType;

        /** Mappa che associa il tipo di linea alla sua descrizione testuale. */
        private Map<String, String> mappaTypeDesc;

        /**
         * Costruttore della classe Route.
         * @param routeId identificativo della linea
         * @param routeType tipo della linea (0=Tram, 1=Metro, 2=Treno, 3=Bus)
         */
        public Route(String routeId, String routeType) {
            this.routeId = routeId;
            this.routeType = routeType;
            mappaTypeDesc = new HashMap<>();
            mappaTypeDesc.put("0", "Tram");
            mappaTypeDesc.put("1", "Metro");
            mappaTypeDesc.put("2", "Treno");
            mappaTypeDesc.put("3", "Bus");
        }

        /**
         * Restituisce l'identificativo della linea.
         * @return routeId della linea
         */
        public String getRouteId() {
            return routeId;
        }

        /**
         * Restituisce il tipo della linea.
         * @return tipo della linea (0=Tram, 1=Metro, 2=Treno, 3=Bus)
         */
        public String getRouteType() {
            return routeType;
        }

        /**
         * Restituisce la descrizione testuale del tipo di linea.
         * @return descrizione del tipo di linea (es. "Tram", "Metro", "Treno", "Bus")
         */
        public String getRouteTypeDesc() {
            return mappaTypeDesc.get(routeType);
        }
    }