package it.uniroma.di.mdp.francesco;

import com.google.transit.realtime.GtfsRealtime;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Set;

import static it.uniroma.di.mdp.francesco.Main.allTrips;

/**
 * Classe che si occupa di recuperare le posizioni in tempo reale dei bus tramite il feed GTFS-RT.
 */
public class GTFSFetcher {
    /**
     * URL del feed GTFS-RT per le posizioni dei veicoli.
     */
    private static final String GTFS_RT_URL = "https://romamobilita.it/sites/default/files/rome_rtgtfs_vehicle_positions_feed.pb";

    /**
     * Recupera le posizioni dei bus in tempo reale dal feed GTFS-RT, filtrando per routeId.
     *
     * @param filterRouteId l'identificativo della linea da filtrare
     * @return un insieme di {@link BusWaypoint} corrispondenti ai bus trovati
     */
    public static Set<BusWaypoint> fetchBusPositions(String filterRouteId) {
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
                    String curRouteId = tripDescriptor.getRouteId();
                    String curTripId = tripDescriptor.getTripId();
                    System.out.println("entity online " + entity);
                    if (curRouteId.equals(filterRouteId)) {
                        Trip trip = allTrips.searchTrip(curTripId);
                        double lat = vehicle.getPosition().getLatitude();
                        double lon = vehicle.getPosition().getLongitude();
                        busWaypoints.add(new BusWaypoint(lat, lon, trip));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return busWaypoints;
    }
}