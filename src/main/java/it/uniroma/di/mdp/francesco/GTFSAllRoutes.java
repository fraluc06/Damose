package it.uniroma.di.mdp.francesco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// classe per rappresentare tutte le linee (routes)
public class GTFSAllRoutes
{
    private List<GTFSStaticRoute> listOfRoute;

    // costruttore
    public GTFSAllRoutes()
    {
        listOfRoute = new ArrayList<GTFSStaticRoute>();
    }

    //metodo per aggiungere una nuova linea
    public void addRoute(GTFSStaticRoute route)
    {
        listOfRoute.add(route);
    }
    public void printRoutes()
    {
        for (GTFSStaticRoute elemento : listOfRoute)
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
            boolean firstLine = true; //la prima riga la scarto perché contiene intestazione dei campi
            while ((line = reader.readLine()) != null) // legge le linee del file
            {
                String[] fields = line.split(delimiter);// split della linea in base al delimitatore
                if (firstLine)
                {
                    firstLine = false;
                }
                else
                { // dalla seconda riga in poi

                    // nel primo campo della riga fields[0] c'è il nome della linea
                    GTFSStaticRoute currentRoute = new GTFSStaticRoute(fields[0]); // creo una linea statica
                    this.addRoute(currentRoute); // inserisco la linea statica nella lista delle linee
                }
                String routeId = fields[0];
                GTFSStaticRoute route = new GTFSStaticRoute(routeId);
                addRoute(route);
            } // fine while
            reader.close();
        }
        catch (IOException e )
        {
            e.printStackTrace();
        }


    } // fine del metodo LoadFromFile
}
