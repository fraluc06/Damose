package it.uniroma.di.mdp.francesco;

import java.time.LocalDateTime;

public class StopTime {

    private String tripId;  // id corsa
    private String stopId;  // id fermata
    private String stopSequence;  // numero progressivo della fermata nel trip
    private String arrivalTime;  // orario di arrivo alla fermata (formato stringa originale)



    private LocalDateTime  arrivalDateTime; // orario di arrivo alla fermata in formato localDateTime
    private String shapeDistTraveled;  // distanza percorsa dal capolinea (ok ?)

    // getter delle variabili
    public String getTripId() {
        return tripId;
    }

    public String getStopId() {
        return stopId;
    }

    public String getStopSequence() {
        return stopSequence;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getShapeDistTraveled() {
        return shapeDistTraveled;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    // costruttore
    public StopTime(String tripId, String stopId, String stopSequence, String arrivalTime, String shapeDistTraveled) {
        this.tripId = tripId;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
        this.arrivalTime = arrivalTime;
        this.shapeDistTraveled = shapeDistTraveled;

        try {
            // converte l'arrivalTime nel tipo LocalDateTime
            //String appArrivalTime = elemento.getArrivalTime();
            String appArrivalTime = this.arrivalTime;
            String h = appArrivalTime.substring(0,2);
            int h_i = Integer.parseInt(h);
            String m = appArrivalTime.substring(3,5);
            int m_i = Integer.parseInt(m);
            String s = appArrivalTime.substring(6,8);
            int s_i = Integer.parseInt(s);
            LocalDateTime adesso = LocalDateTime.now();


            if (h.equals("24"))
            {

                arrivalDateTime =  LocalDateTime.of(adesso.getYear(),adesso.getMonthValue(),adesso.getDayOfMonth(),0,m_i,s_i);
                arrivalDateTime = arrivalDateTime.plusDays(1);


            } else if (h.equals("25"))
            {

                arrivalDateTime =  LocalDateTime.of(adesso.getYear(),adesso.getMonthValue(),adesso.getDayOfMonth(),1,m_i,s_i);
                arrivalDateTime = arrivalDateTime.plusDays(1);

            } else if (h.equals("26"))
            {

                arrivalDateTime =  LocalDateTime.of(adesso.getYear(),adesso.getMonthValue(),adesso.getDayOfMonth(),2,m_i,s_i);
                arrivalDateTime = arrivalDateTime.plusDays(1);

            }
            else if (h.equals("27"))
            {

                arrivalDateTime =  LocalDateTime.of(adesso.getYear(),adesso.getMonthValue(),adesso.getDayOfMonth(),3,m_i,s_i);
                arrivalDateTime = arrivalDateTime.plusDays(1);

            }
            else
            {
                arrivalDateTime =  LocalDateTime.of(adesso.getYear(),adesso.getMonthValue(),adesso.getDayOfMonth(),h_i,m_i,s_i);
            }

        } catch (Exception e) {

            System.out.println("Ora arrivalTime scartato: "+arrivalTime);
        }


    } // fine costruttore
} // fine classe
