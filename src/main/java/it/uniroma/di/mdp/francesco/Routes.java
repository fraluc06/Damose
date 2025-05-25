package it.uniroma.di.mdp.francesco;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Routes {
    private Map<String, Route> routeMap;

    public Routes() {
        routeMap = new HashMap<>();
    }

    public void addRoute(Route route) {
        routeMap.put(route.getRouteId(), route);
    }

    public void print() {
        for (String routeId : routeMap.keySet()) {
            System.out.println(routeId);
        }
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
                String routeId = fields[0];
                String routeType = fields[4];
                Route route = new Route(routeId,routeType);
                addRoute(route);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Route searchRoute(String searchString) {
        return routeMap.get(searchString);
    }
}