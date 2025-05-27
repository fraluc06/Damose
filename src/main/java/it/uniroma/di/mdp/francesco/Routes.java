package it.uniroma.di.mdp.francesco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe che gestisce la collezione delle linee (route) GTFS.
 * Permette di aggiungere, cercare, stampare e caricare le linee da file.
 */
public class Routes {
    /**
     * Mappa che associa l'ID della linea all'oggetto Route corrispondente.
     */
    private final Map<String, Route> routeMap;

    /**
     * Costruttore della classe Routes.
     * Inizializza la mappa delle linee.
     */
    public Routes() {
        routeMap = new HashMap<>();
    }

    /**
     * Aggiunge una linea (Route) alla collezione.
     *
     * @param route oggetto Route da aggiungere
     */
    public void addRoute(Route route) {
        routeMap.put(route.getRouteId(), route);
    }

    /**
     * Stampa su console tutti gli ID delle linee presenti nella collezione.
     */
    public void print() {
        for (String routeId : routeMap.keySet()) {
            System.out.println(routeId);
        }
    }

    /**
     * Carica le linee da un file CSV GTFS (routes.txt).
     *
     * @param filePath percorso del file da cui caricare le linee
     */
    public void loadFromFile(String filePath) {
        String delimiter = ",";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean primaRiga = true;
            while ((line = reader.readLine()) != null) {
                if (primaRiga) {
                    primaRiga = false;
                    continue;
                }
                String[] fields = line.split(delimiter);
                String routeId = fields[0];
                String routeType = fields[4];
                Route route = new Route(routeId, routeType);
                addRoute(route);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cerca una linea tramite il suo identificativo.
     *
     * @param searchString identificativo della linea da cercare
     * @return oggetto Route corrispondente, oppure null se non trovato
     */
    public Route searchRoute(String searchString) {
        return routeMap.get(searchString);
    }
}