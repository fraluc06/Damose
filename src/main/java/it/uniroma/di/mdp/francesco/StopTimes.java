package it.uniroma.di.mdp.francesco;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class StopTimes {
    private List<StopTime> listOfStoptimes;
    private Map<String, List<StopTime>> stopIdMap;
    private Map<String, List<StopTime>> tripIdMap;

    public StopTimes() {
        listOfStoptimes = new ArrayList<>();
        stopIdMap = new HashMap<>();
        tripIdMap = new HashMap<>();
    }

    public void AddStopTime(StopTime stoptime) {
        listOfStoptimes.add(stoptime);
        stopIdMap.computeIfAbsent(stoptime.getStopId(), k -> new ArrayList<>()).add(stoptime);
        tripIdMap.computeIfAbsent(stoptime.getTripId(), k -> new ArrayList<>()).add(stoptime);
    }

    public void loadFromFile(String filePath) {
        String delimiter = ",";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean primaRiga = true;
            while ((line = reader.readLine()) != null) {
                if (primaRiga) {
                    primaRiga = false;
                    continue;
                }
                String[] fields = line.split(delimiter);
                String currentTripId = fields[0];
                String currentArrivalTime = fields[1];
                String currentStopId = fields[3];
                String currentStopSequence = fields[4];
                String currentShapeDist = fields[8];
                StopTime currentStopTime = new StopTime(currentTripId, currentStopId, currentStopSequence, currentArrivalTime, currentShapeDist);
                this.AddStopTime(currentStopTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<StopTime> getStoptimesFromStopId(String stopId) {
        return stopIdMap.getOrDefault(stopId, Collections.emptyList());
    }

    public List<StopTime> getStoptimesFromStopId(String stopId, long minutesRange) {
        LocalDateTime adesso = LocalDateTime.now();
        List<StopTime> result = new ArrayList<>();
        for (StopTime st : stopIdMap.getOrDefault(stopId, Collections.emptyList())) {
            if (st.getArrivalDateTime().isBefore(adesso.plusMinutes(minutesRange)) &&
                    st.getArrivalDateTime().isAfter(adesso.minusMinutes(1))) {
                result.add(st);
            }
        }
        return result;
    }

    public StopTime getStoptimesFromTripId(String tripId, long minutesRange) {
        LocalDateTime adesso = LocalDateTime.now();
        for (StopTime st : tripIdMap.getOrDefault(tripId, Collections.emptyList())) {
            if (st.getArrivalDateTime().isBefore(adesso.plusMinutes(minutesRange)) &&
                    st.getArrivalDateTime().isAfter(adesso.minusMinutes(1))) {
                return st;
            }
        }
        return null;
    }
}