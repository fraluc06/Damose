package it.uniroma.di.mdp.francesco;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce le linee e le fermate preferite dell'utente.
 * Permette di aggiungere, rimuovere, caricare e salvare le linee e le fermate preferite su file.
 * Le preferenze sono memorizzate tramite due liste distinte: una per le linee e una per le fermate.
 */
public class Favourites {
    /**
     * Lista degli ID delle linee preferite dall'utente.
     */
    private final List<String> favouriteRoutes = new ArrayList<>();
    /**
     * Lista degli ID delle fermate preferite dall'utente.
     */
    private final List<String> favouriteStops = new ArrayList<>();

    /**
     * Carica le linee preferite da un file.
     * Ogni riga del file deve contenere un ID di linea, eventualmente seguito da un punto e virgola.
     *
     * @param filePath percorso del file da cui caricare le linee preferite
     */
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

    /**
     * Aggiunge una linea ai preferiti, se non già presente.
     *
     * @param routeId l'ID della linea da aggiungere ai preferiti
     */
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
     * Restituisce la lista delle linee preferite.
     *
     * @return una nuova lista contenente gli ID delle linee preferite
     */
    public List<String> getFavouriteRoutes() {
        return new ArrayList<String>(favouriteRoutes);
    }

    /**
     * Salva le linee preferite su un file.
     * Ogni ID viene scritto su una riga, seguito da un punto e virgola.
     *
     * @param filePath percorso del file su cui salvare le linee preferite
     */
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

    /**
     * Carica le fermate preferite da un file.
     * Ogni riga del file deve contenere un ID di fermata, eventualmente seguito da un punto e virgola.
     *
     * @param filePath percorso del file da cui caricare le fermate preferite
     */
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

    /**
     * Aggiunge una fermata ai preferiti, se non già presente.
     *
     * @param stopId l'ID della fermata da aggiungere ai preferiti
     */
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
     * @param stopId l'ID della fermata da rimuovere
     */
    public void removeStop(String stopId) {
        favouriteStops.remove(stopId);
    }

    /**
     * Restituisce la lista delle fermate preferite.
     *
     * @return una nuova lista contenente gli ID delle fermate preferite
     */
    public List<String> getFavouriteStops() {
        return new ArrayList<String>(favouriteStops);
    }

    /**
     * Salva le fermate preferite su un file.
     * Ogni ID viene scritto su una riga, seguito da un punto e virgola.
     *
     * @param filePath percorso del file su cui salvare le fermate preferite
     */
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
}