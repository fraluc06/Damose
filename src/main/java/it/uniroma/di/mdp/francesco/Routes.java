package it.uniroma.di.mdp.francesco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// classe per rappresentare tutte le linee (routes)
public class Routes
{
    private List<Route> listOfRoutes;

    // costruttore
    public Routes()
    {
        listOfRoutes = new ArrayList<Route>();
    }

    //metodo per aggiungere una nuova linea
    public void addRoute(Route route)
    {
        listOfRoutes.add(route);
    }
    public void print()
    {
        for (Route elemento : listOfRoutes)
        {
            System.out.println(elemento.getRouteId());
        }
    }

    //metodo per caricare dati dai file statici
    public void loadFromFile(String filePath)
    {
        String delimiter = ","; //carattere separatore dei campi
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean primaRiga = true; //la prima riga la scarto perché contiene intestazione dei campi
            while ((line = reader.readLine()) != null) // legge le linee del file
            {
                String[] fields = line.split(delimiter);// split della linea in base al delimitatore
                if (primaRiga)
                {
                    primaRiga = false;
                }
                else
                { // dalla seconda riga in poi

                    // nel primo campo della riga fields[0] c'è il nome della linea
                    Route currentRoute = new Route(fields[0]); // creo una linea statica
                    this.addRoute(currentRoute); // inserisco la linea statica nella lista delle linee
                }
                String routeId = fields[0];
                Route route = new Route(routeId);
                addRoute(route);
            } // fine while
            reader.close();
        }
        catch (IOException e )
        {
            e.printStackTrace();
        }


    } // fine del metodo LoadFromFile

    public Route searchRoute(String searchString)
    {
        for (Route route : listOfRoutes) {
            if (route.getRouteId().equals(searchString)) {
                return route; // restituisce lo stop (fermata) se trovato
            }
        }
        return null; // restituisce null se non trovato
    }



}
