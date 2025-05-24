package it.uniroma.di.mdp.francesco;

import com.google.transit.realtime.GtfsRealtime;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

import static it.uniroma.di.mdp.francesco.MainPage.allRoutes;

public class GTFSFetcher {
    private static final String GTFS_RT_URL = "https://romamobilita.it/sites/default/files/rome_rtgtfs_vehicle_positions_feed.pb";

    public static Set<BusWaypoint> fetchBusPositions() {
        Set<BusWaypoint> busWaypoints = new HashSet<>();

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GTFS_RT_URL))
                    .build();

            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            byte[] responseBody = response.body();

            GtfsRealtime.FeedMessage feed = GtfsRealtime.FeedMessage.parseFrom(responseBody);

            for (GtfsRealtime.FeedEntity entity : feed.getEntityList()) {
                if (entity.hasVehicle()) {
                    GtfsRealtime.VehiclePosition vehicle = entity.getVehicle();
                    GtfsRealtime.TripDescriptor tripDescriptor = vehicle.getTrip();

                    Route route = allRoutes.searchRoute(tripDescriptor.getRouteId());
                    double lat = vehicle.getPosition().getLatitude();
                    double lon = vehicle.getPosition().getLongitude();
                    busWaypoints.add(new BusWaypoint(lat, lon, route.getRouteId(), route.getRouteType()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return busWaypoints;
    }
}