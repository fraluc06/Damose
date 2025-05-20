package it.uniroma.di.mdp.francesco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StopTimes {
    private List<StopTime> listOfStoptimes; //

    public StopTimes() {
        listOfStoptimes = new ArrayList<StopTime>();
    }


    // metodo che aggiunge uno stop alla list_of_stops
    public void AddStopTime(StopTime stoptime)
    {
        listOfStoptimes.add(stoptime);

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
                else { // dalla seconda riga in poi

                    // nel primo campo della riga fields[0] c'è il nome della linea


                    String currentTripId = fields[0]; // service_id del trip
                    String currentArrivalTime = fields[1]; // orario arrivo alla fermata formato stringa da convertire in LocalTime
                    //LocalTime currentArrivalTime = LocalTime.parse(string_arrival_time);
                    String currentStopId = fields[3]; // stop id
                    String currentStopSequence= fields[4]; // stop sequence
                    String currentShapeDist = fields[8]; // dist traveled stringa da convertire in int
                    //Integer currentShapeDist = Integer.valueOf(string_shape_dist);
                    StopTime currentStopTime = new StopTime(currentTripId,currentStopId,currentStopSequence,currentArrivalTime,currentShapeDist); // creo uno stop time
                    this.AddStopTime(currentStopTime); // inserisco lo stop time nella lista di tutti gli stop times

                }
            } // fine while
            reader.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    } // fine metodo LoadFromFile

    // stampa tutti gli stoptimes
    public void Print()
    {
        for (StopTime elemento : listOfStoptimes) {
            System.out.println("Trip Id: "+elemento.getTripId()+"StopTime: "+elemento.getStopId() +" StopSequence: "+elemento.getStopSequence()+" ArrivalTime: "+elemento.getArrivalTime()+" Dist_traveled: "+elemento.getShapeDistTraveled() );
        }
    }

    public void Print(String tripId)
    {
        Boolean trovatoPrimo = false;

        for (StopTime elemento : listOfStoptimes)

        {
            if (elemento.getTripId().equals(tripId))
            {
                trovatoPrimo = true; // se trovato il trip
                System.out.println("Trip Id: "+elemento.getTripId()+" StopTime: "+elemento.getStopId() +" StopSequence: "+elemento.getStopSequence()+" ArrivalTime: "+elemento.getArrivalTime()+" Dist_traveled: "+elemento.getShapeDistTraveled() );
            }
            else if (trovatoPrimo == true) // se lo aveva trovato ed ora è diventato diverso, esci dal ciclo di scansione
                break; // esce dal for
        }
    }

    // restituisce una lista di stoptimes dato uno stopId (fermata)
    public List<StopTime> getStoptimesFromStopId(String stopId)
    {
        List<StopTime> appStopList = new ArrayList<StopTime>();

        for (StopTime elemento : listOfStoptimes)
        {
            if (elemento.getStopId().equals(stopId))
                appStopList.add(elemento);
        }

        return appStopList;
    } // fine getStopTimesFromStopId

    // restituisce una lista di stoptimes dato uno stopId (fermata) che arriveranno alla fermata nei prossimi minuti (minutesRange)
    public List<StopTime> getStoptimesFromStopId(String stopId,long minutesRange)
    {
        List<StopTime> appStopList = new ArrayList<StopTime>();
        LocalDateTime adesso = LocalDateTime.now();

        for (StopTime elemento : listOfStoptimes)
        {
            if (elemento.getStopId().equals(stopId)) // se è uno stopId che cerco
            {
                if (  (elemento.getArrivalDateTime().isBefore(adesso.plusMinutes(minutesRange))) && (elemento.getArrivalDateTime().isAfter(adesso.minusMinutes(1)))) // se l'ora prevista di arrivo è tra adesso e i prossimi 30 minuti
                {
                    appStopList.add(elemento);
                }



            }
        }

        return appStopList;
    } // fine getStopTimesFromStopId con minutesRange



} // fine della classe
