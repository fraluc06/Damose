package it.uniroma.di.mdp.francesco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// classe per la rappresentazione di tutti i trip
public class Trips
{

    private List<Trip> listOfTrips; //

    // costruttore
    public Trips() {

        listOfTrips = new ArrayList<Trip>();

    }

    // metodo che aggiunge un trip alla list_of_trips
    public void AddTrip(Trip trip)
    {
        listOfTrips.add(trip);

    }

    // ritorna il numero di trips
    public int getSize()
    {
        return  listOfTrips.size();
    }

    public void loadFromFile(String filePath)
    {

        String delimiter = ","; // carattere separatore campi

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean prima_riga = true; // la prima riga la scarto perchè contiene intestazione dei campi

            while ((line = reader.readLine()) != null) // legge le linee dei file
            {
                String[] fields = line.split(delimiter); // Split della linea in base al delimitatore

                if (prima_riga) {
                    prima_riga = false;
                }
                else { // dalla seconda riga in poi

                    // nel primo campo della riga fields[0] c'è il nome della linea
                    String currentRouteId = fields[0]; // routeId del trip
                    String currentServiceId = fields[1]; // serviceId del trip
                    String currentTripId = fields[2]; // serviceId del trip
                    String currentTripHeadsign = fields[3]; // trip headsign
                    Trip currentTrip = new Trip(currentTripId, currentTripHeadsign, currentRouteId); // creo una corsa Trip
                    this.AddTrip(currentTrip); // inserisco ìl trip nella lista di tutti i trips

                }
            } // fine while
            reader.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    } // fine metodo LoadFromFile

    // stampa tutti i trip di tutti le routes
    public void Print()
    {
        for (Trip elemento : listOfTrips) {
            System.out.println("Trip: "+elemento.getTripId()+" Route: "+elemento.getRouteId()+" Headsign: "+elemento.getTripHeadSign());
        }
    }

    // stampa tutti i trip del singolo routeId
    public void Print(String routeId)
    {
        for (Trip elemento : listOfTrips) {
            if (elemento.getRouteId().equals(routeId))
                System.out.println("Trip: "+elemento.getTripId()+" Route: "+elemento.getRouteId()+" Headsign: "+elemento.getTripHeadSign());

        }
    }

    // restituisce la route corrispondente al tripId
    public Route getRouteFromTripId (String tripId)
    {

        for (Trip elemento : listOfTrips)
        {
            if (elemento.getTripId().equals(tripId))
            {
                Route r = new Route(elemento.getRouteId());
                return r;
            }
        }
        return null;
    }



} // fine class Trips
