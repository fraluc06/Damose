package it.uniroma.di.mdp.francesco;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestisce le linee e le fermate preferite dell'utente tramite due HashMap distinte.
 * Permette di aggiungere, rimuovere e ottenere linee e fermate preferite.
 */
public class Favourites {
    private final Map<String, String> favouriteLines = new HashMap<>();
    private final Map<String, String> favouriteStops = new HashMap<>();

    /**
     * Aggiunge una linea ai preferiti.
     *
     * @param routeId   l'ID della linea
     * @param routeName il nome della linea
     */
    public void addLine(String routeId, String routeName) {
        favouriteLines.put(routeId, routeName);
    }

    /**
     * Rimuove una linea dai preferiti.
     *
     * @param routeId l'ID della linea da rimuovere
     */
    public void removeLine(String routeId) {
        favouriteLines.remove(routeId);
    }

    /**
     * Restituisce tutte le linee preferite.
     *
     * @return una nuova mappa contenente le linee preferite (ID -> nome)
     */
    public Map<String, String> getFavouriteLines() {
        return new HashMap<>(favouriteLines);
    }

    /**
     * Aggiunge una fermata ai preferiti.
     *
     * @param stopId   l'ID della fermata
     * @param stopName il nome della fermata
     */
    public void addStop(String stopId, String stopName) {
        favouriteStops.put(stopId, stopName);
    }

    /**
     * Rimuove una fermata dai preferiti.
     *
     * @param stopId l'ID della fermata da rimuovere
     */
    public void removeStop(String stopId) {
        favouriteStops.remove(stopId);
    }

    /**
     * Restituisce tutte le fermate preferite.
     *
     * @return una nuova mappa contenente le fermate preferite (ID -> nome)
     */
    public Map<String, String> getFavouriteStops() {
        return new HashMap<>(favouriteStops);
    }
}