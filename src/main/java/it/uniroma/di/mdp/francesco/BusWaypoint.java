package it.uniroma.di.mdp.francesco;

        import org.jxmapviewer.viewer.GeoPosition;
        import org.jxmapviewer.viewer.Waypoint;

        import static it.uniroma.di.mdp.francesco.Main.allRoutes;

        /**
         * Rappresenta un waypoint di un autobus sulla mappa.
         * Permette la personalizzazione dell'etichetta e del tipo di mezzo.
         */
        public class BusWaypoint implements Waypoint {
            private final GeoPosition position;
            private final String label; // aggiunto per personalizzazione
            private final String routeType; // tipo di mezzo per modificare l'icona

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
             * Costruttore base. Crea un waypoint senza etichetta e senza tipo di mezzo.
             *
             * @param lat latitudine
             * @param lon longitudine
             */
            public BusWaypoint(double lat, double lon) {
                this.position = new GeoPosition(lat, lon);
                this.label = "";
                this.routeType = "";
            }

            /**
             * Costruttore utilizzato in modalità offline.
             * Permette di specificare etichetta e tipo di mezzo.
             *
             * @param lat latitudine
             * @param lon longitudine
             * @param label etichetta personalizzata
             * @param routeType tipo di mezzo
             */
            public BusWaypoint(double lat, double lon, String label, String routeType) {
                this.position = new GeoPosition(lat, lon);
                this.label = label;
                this.routeType = routeType;
            }

            /**
             * Costruttore utilizzato in modalità online.
             * L'etichetta e il tipo di mezzo vengono ricavati dal trip e dalla route associata.
             *
             * @param lat latitudine
             * @param lon longitudine
             * @param trip oggetto Trip associato al waypoint
             */
            public BusWaypoint(double lat, double lon, Trip trip) {
                this.position = new GeoPosition(lat, lon);
                Route route = allRoutes.searchRoute(trip.getRouteId());
                this.label = route.getRouteId() + " (" + trip.getTripId() + ") -" + trip.getTripHeadSign();
                this.routeType = route.getRouteType();
            }

            /**
             * Restituisce la posizione geografica del waypoint.
             *
             * @return la posizione geografica
             */
            @Override
            public GeoPosition getPosition() {
                return position;
            }
        }