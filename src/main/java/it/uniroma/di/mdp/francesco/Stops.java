package it.uniroma.di.mdp.francesco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe che gestisce la collezione delle fermate (stop) GTFS.
 * <p>
 * Permette di aggiungere, cercare, stampare e caricare le fermate da file.
 */
public class Stops {

    /**
     * Lista di tutte le fermate caricate.
     */
    private final List<Stop> listOfStops;

    /**
     * Costruttore della classe Stops.
     * Inizializza la lista delle fermate.
     */
    public Stops() {
        listOfStops = new ArrayList<Stop>();
    }

    /**
     * Aggiunge una fermata alla lista delle fermate.
     *
     * @param stop oggetto Stop da aggiungere
     */
    public void AddStop(Stop stop) {
        listOfStops.add(stop);
    }

    /**
     * Carica le fermate da un file CSV GTFS (stops.txt).
     * Il file deve avere la prima riga come intestazione e i campi separati da virgola.
     * Vengono letti solo l'ID della fermata (campo 0), il nome (campo 2),
     * la latitudine (campo 4) e la longitudine (campo 5).
     *
     * @param filePath percorso del file da cui caricare le fermate
     */
    public void loadFromFile(String filePath) {
        String delimiter = ","; // carattere separatore campi

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean primaRiga = true; // la prima riga la scarto perchè contiene intestazione dei campi

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(delimiter);

                if (primaRiga) {
                    primaRiga = false;
                } else {
                    String currentStopId = fields[0];
                    String current_stop_name = fields[2];
                    current_stop_name = current_stop_name.replace("\"", "");
                    String currentStopLat = fields[4];
                    String currentStopLon = fields[5];
                    Stop current_stop = new Stop(currentStopId, current_stop_name, currentStopLat, currentStopLon);
                    this.AddStop(current_stop);
                }
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cerca una fermata tramite il suo ID o nome.
     *
     * @param searchString ID o nome della fermata da cercare
     * @return oggetto Stop corrispondente, oppure null se non trovato
     */
    public Stop searchStop(String searchString) {
        for (Stop stop : listOfStops) {
            if (stop.getStopId().equals(searchString) || stop.getStopName().equals(searchString)) {
                return stop;
            }
        }
        return null;
    }

    /**
     * Cerca tutte le fermate che corrispondono all'ID esatto o che contengono la stringa di ricerca nel nome.
     *
     * @param searchString stringa da cercare (ID o parte del nome)
     * @return lista di oggetti Stop che corrispondono alla ricerca
     */
    public List<Stop> searchStopList(String searchString) {
        List<Stop> result = new ArrayList<Stop>();
        for (Stop stop : listOfStops) {
            if (stop.getStopId().equals(searchString)) { // se trova l'occorrenza esatta con lo stopID
                result.add(stop);
            } else { // altrimenti prova a cercare come substring nella descrizione
                int indice = stop.getStopName().indexOf(searchString);
                if (indice != -1) // se trovata la substring
                    result.add(stop); // aggiunge la fermata alla lista
            }
        }
        return result;
    }

    /**
     * Stampa su console tutte le fermate presenti nella lista.
     */
    public void Print() {
        for (Stop elemento : listOfStops) {
            System.out.println("Stop: " + elemento.getStopId() + " Name: " + elemento.getStopName() +
                    " LAT: " + elemento.getStopLat() + " LON: " + elemento.getStopLon());
        }
    }
}