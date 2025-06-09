package it.uniroma.di.mdp.francesco;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Classe di utilità per verificare lo stato della connessione Internet.
 * Fornisce metodi per controllare se la connessione è attiva effettuando una richiesta HTTP
 * verso un URL specificato o verso un URL di default (Google).
 */
public class OnlineStatusChecker {
    /**
     * URL di default utilizzato per il controllo della connessione (Google).
     */
    private static final String DEFAULT_URL = "https://www.google.com";
    /**
     * Timeout massimo per la connessione e la risposta HTTP.
     */
    private static final Duration TIMEOUT = Duration.ofSeconds(2);

    /**
     * Verifica se la connessione ad Internet è attiva tentando una richiesta HTTP verso l'URL specificato.
     *
     * @param url L'URL da raggiungere (es: "https://www.google.com")
     * @return true se la connessione è attiva e l'host risponde con status 2xx o 3xx, false altrimenti
     */
    public static boolean isOnline(String url) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(TIMEOUT)
                .GET()
                .build();

        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            int code = response.statusCode();
            return code >= 200 && code < 400;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se la connessione ad Internet è attiva usando l'URL di default (Google).
     *
     * @return true se la connessione è attiva, false altrimenti
     */
    public static boolean isOnline() {
        return isOnline(DEFAULT_URL);
    }
}