package it.uniroma.di.mdp.francesco;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestisce le linee e le fermate preferite dell'utente tramite due HashMap distinte.
 * Permette di aggiungere, rimuovere e ottenere linee e fermate preferite.
 */
public class Favourites {
    private final List<String> favouriteRoutes = new ArrayList<>();
    private final List<String> favouriteStops = new ArrayList<>();

    public void loadFavouriteRoutesFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String id = line.replace(";", "").trim();
                if (!id.isEmpty()) favouriteRoutes.add(id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRoute(String routeId) {
        boolean trovato = false;
        for (String curRouteId : favouriteRoutes) {
            if (curRouteId.equals(routeId)) {
                trovato = true;
                break;
            }
        }
        if (!trovato)
            favouriteRoutes.add(routeId);
    }

    /**
     * Rimuove una linea dai preferiti.
     *
     * @param routeId l'ID della linea da rimuovere
     */
    public void removeRoute(String routeId) {
        favouriteRoutes.remove(routeId);
    }

    /**
     * Restituisce tutte le linee preferite.
     *
     * @return una nuova mappa contenente le linee preferite (ID -> nome)
     */
    public List<String> getFavouriteRoutes() {
        return new ArrayList<String>(favouriteRoutes);
    }

    public void saveFavouriteRoutesToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String routeId : favouriteRoutes) {
                writer.write(routeId + ";");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void loadFavouriteStopsFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String id = line.replace(";", "").trim();
                if (!id.isEmpty()) favouriteStops.add(id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addStop(String stopId) {
        boolean trovato = false;
        for (String curStopId : favouriteStops) {
            if (curStopId.equals(stopId)) {
                trovato = true;
                break;
            }
        }
        if (!trovato)
            favouriteStops.add(stopId);
    }

    /**
     * Rimuove una fermata dai preferiti.
     *
     * @param stopId l'ID della linea da rimuovere
     */
    public void removeStop(String stopId) {
        favouriteStops.remove(stopId);
    }

    /**
     * Restituisce tutti le fermate preferite.
     *
     * @return una nuova mappa contenente le linee preferite (ID -> nome)
     */
    public List<String> getFavouriteStops() {
        return new ArrayList<String>(favouriteStops);
    }

    public void saveFavouriteStopsToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String stopId : favouriteStops) {
                writer.write(stopId + ";");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

} //fine classe Favourites






