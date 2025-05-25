package it.uniroma.di.mdp.francesco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Stops {

    private List<Stop> listOfStops; //

    // costruttore
    public Stops() {

        listOfStops = new ArrayList<Stop>();

    }

    // metodo che aggiunge uno stop alla list_of_stops
    public void AddStop(Stop stop)
    {
        listOfStops.add(stop);

    }


    public void loadFromFile(String filePath)
    {

        String delimiter = ","; // carattere separatore campi

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean primaRiga = true; // la prima riga la scarto perchè contiene intestazione dei campi

            while ((line = reader.readLine()) != null) // legge le linee dei file
            {
                String[] fields = line.split(delimiter); // Split della linea in base al delimitatore

                if (primaRiga) {
                    primaRiga = false;
                }
                else {       // dalla seconda riga in poi
                    String currentStopId = fields[0]; // stop_id dello stop
                    String current_stop_name = fields[2]; // nome dello stop (fermata)
                    // Rimuove eventuali caratteri di nuova linea dal nome dello stop
                    current_stop_name = current_stop_name.replace("\"", "");
                    String currentStopLat = fields[4]; // latitudine dello stop
                    String currentStopLon = fields[5]; // longitudine dello stop
                    Stop current_stop = new Stop(currentStopId, current_stop_name, currentStopLat, currentStopLon); // creo uno oggetto stop (fermata)
                    this.AddStop(current_stop); // inserisco lo stop (fermata) nella lista di tutti gli stop (fermate)

                }
            } // fine while
            reader.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    } // fine metodo LoadFromFile
    public Stop searchStop(String searchString)
    {
        for (Stop stop : listOfStops) {
            if (stop.getStopId().equals(searchString) || stop.getStopName().equals(searchString)) {
                return stop; // restituisce lo stop (fermata) se trovato
            }
        }
        return null; // restituisce null se non trovato
    }

    // stampa tutti gli stop (tutte le fermate)
    public void Print()
    {
        //System.out.println("entrato Stops.Print()"); // debug
        for (Stop elemento : listOfStops) {
            System.out.println("Stop: "+elemento.getStopId()+" Name: "+elemento.getStopName()+" LAT: "+elemento.getStopLat()+" LON: "+elemento.getStopLon());
        }
    }


} // fine della classe
