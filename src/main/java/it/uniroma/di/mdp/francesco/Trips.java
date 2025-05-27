package it.uniroma.di.mdp.francesco;

    import java.io.BufferedReader;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.ArrayList;
    import java.util.List;

    /**
     * Classe che rappresenta la collezione di tutte le corse (trip) del sistema GTFS.
     * Permette di aggiungere, cercare, stampare e caricare i trip da file.
     */
    public class Trips {

        /** Lista di tutte le corse caricate. */
        private List<Trip> listOfTrips;

        /**
         * Costruttore della classe Trips.
         * Inizializza la lista delle corse.
         */
        public Trips() {
            listOfTrips = new ArrayList<Trip>();
        }

        /**
         * Aggiunge una corsa (trip) alla lista.
         * @param trip oggetto Trip da aggiungere
         */
        public void AddTrip(Trip trip) {
            listOfTrips.add(trip);
        }

        /**
         * Restituisce il numero totale di trip caricati.
         * @return numero di trip
         */
        public int getSize() {
            return listOfTrips.size();
        }

        /**
         * Carica i trip da un file CSV GTFS (trips.txt).
         * @param filePath percorso del file da cui caricare i trip
         */
        public void loadFromFile(String filePath) {
            String delimiter = ","; // carattere separatore campi

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                String line;
                boolean prima_riga = true; // la prima riga la scarto perchè contiene intestazione dei campi

                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(delimiter);

                    if (prima_riga) {
                        prima_riga = false;
                    } else {
                        String currentRouteId = fields[0];
                        String currentServiceId = fields[1];
                        String currentTripId = fields[2];
                        String currentTripHeadsign = fields[3];
                        Trip currentTrip = new Trip(currentTripId, currentTripHeadsign, currentRouteId);
                        this.AddTrip(currentTrip);
                    }
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Stampa su console tutti i trip di tutte le route.
         */
        public void Print() {
            for (Trip elemento : listOfTrips) {
                System.out.println("Trip: " + elemento.getTripId() + " Route: " + elemento.getRouteId() + " Headsign: " + elemento.getTripHeadSign());
            }
        }

        /**
         * Stampa su console tutti i trip relativi a una specifica route.
         * @param routeId identificativo della route
         */
        public void Print(String routeId) {
            for (Trip elemento : listOfTrips) {
                if (elemento.getRouteId().equals(routeId))
                    System.out.println("Trip: " + elemento.getTripId() + " Route: " + elemento.getRouteId() + " Headsign: " + elemento.getTripHeadSign());
            }
        }

        /**
         * Restituisce la routeId corrispondente a un dato tripId.
         * @param tripId identificativo della corsa
         * @return routeId corrispondente, oppure null se non trovato
         */
        public String getRouteIdFromTripId(String tripId) {
            for (Trip elemento : listOfTrips) {
                if (elemento.getTripId().equals(tripId)) {
                    return elemento.getRouteId();
                }
            }
            return null;
        }

        /**
         * Restituisce il trip corrispondente a un dato tripId.
         * @param tripId identificativo della corsa
         * @return oggetto Trip corrispondente, oppure null se non trovato
         */
        public Trip searchTrip(String tripId) {
            for (Trip elemento : listOfTrips) {
                if (elemento.getTripId().equals(tripId)) {
                    return elemento;
                }
            }
            return null;
        }

        /**
         * Restituisce la lista dei trip associati a una specifica route.
         * @param routeId identificativo della route
         * @return lista di Trip per la route specificata
         */
        public List<Trip> getTripListFromRouteId(String routeId) {
            List<Trip> appTripList = new ArrayList<Trip>();
            for (Trip elemento : listOfTrips) {
                if (elemento.getRouteId().equals(routeId)) {
                    appTripList.add(elemento);
                }
            }
            return appTripList;
        }

    } // fine class Trips