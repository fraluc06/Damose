package it.uniroma.di.mdp.francesco;

        import java.io.*;
        import java.time.LocalDateTime;
        import java.util.*;

        /**
         * Classe che gestisce la collezione degli orari di fermata (stop_times) GTFS.
         * Permette di aggiungere, cercare, filtrare e caricare gli orari da file.
         */
        public class StopTimes {

            /** Lista di tutti gli orari di fermata caricati. */
            private List<StopTime> listOfStoptimes;
            /** Mappa che associa uno stop_id alla lista di StopTime corrispondenti. */
            private Map<String, List<StopTime>> stopIdMap;
            /** Mappa che associa un trip_id alla lista di StopTime corrispondenti. */
            private Map<String, List<StopTime>> tripIdMap;

            /**
             * Costruttore della classe StopTimes.
             * Inizializza le strutture dati per la gestione degli orari.
             */
            public StopTimes() {
                listOfStoptimes = new ArrayList<>();
                stopIdMap = new HashMap<>();
                tripIdMap = new HashMap<>();
            }

            /**
             * Aggiunge un orario di fermata alla collezione e aggiorna le mappe di ricerca.
             * @param stoptime oggetto StopTime da aggiungere
             */
            public void AddStopTime(StopTime stoptime) {
                listOfStoptimes.add(stoptime);
                stopIdMap.computeIfAbsent(stoptime.getStopId(), k -> new ArrayList<>()).add(stoptime);
                tripIdMap.computeIfAbsent(stoptime.getTripId(), k -> new ArrayList<>()).add(stoptime);
            }

            /**
             * Carica gli orari di fermata da un file CSV GTFS (stop_times.txt).
             * @param filePath percorso del file da cui caricare gli orari
             */
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

            /**
             * Restituisce la lista di StopTime associati a uno specifico stop_id.
             * @param stopId identificativo della fermata
             * @return lista di StopTime per la fermata specificata
             */
            public List<StopTime> getStoptimesFromStopId(String stopId) {
                return stopIdMap.getOrDefault(stopId, Collections.emptyList());
            }

            /**
             * Restituisce la lista di StopTime per uno stop_id, filtrati per un intervallo di minuti rispetto all'orario attuale.
             * @param stopId identificativo della fermata
             * @param minutesRange intervallo di minuti in avanti rispetto all'orario attuale
             * @return lista di StopTime filtrati per intervallo temporale
             */
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

            /**
             * Restituisce il primo StopTime associato a un trip_id, filtrato per intervallo di minuti rispetto all'orario attuale.
             * @param tripId identificativo della corsa
             * @param minutesRange intervallo di minuti in avanti rispetto all'orario attuale
             * @return oggetto StopTime corrispondente, oppure null se non trovato
             */
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