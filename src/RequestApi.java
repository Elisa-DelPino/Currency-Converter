import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RequestApi {

    /**
     * Récupère le JSON brut de l’API pour les devises spécifiées.
     * @param from La devise de base (ex : "EUR", "JPY", etc.)
     * @param to La devise cible (ex : "USD", "GBP", etc.)
     * @return la chaîne JSON renvoyée par l’API, ou null si erreur
     */
    public String fetchJson(String from, String to) {
        StringBuilder sb = new StringBuilder();
        String apiUrl = String.format("https://api.frankfurter.dev/v1/latest?base=%s&symbols=%s", from, to);
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int status = conn.getResponseCode();
            InputStream is;
            if (status >= 200 && status < 300) {
                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    /**
     * Récupère le taux de conversion pour “from → to” pour 1 unité de “from”.
     * Méthode publique, qu’on peut appeler depuis une autre classe.
     *
     * @param from La devise de base
     * @param to La devise cible
     * @return le taux de conversion (double) ou null en cas d’erreur
     */
    public Double getRate(String from, String to) {
        String json = fetchJson(from, to);
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonObject rates = root.getAsJsonObject("rates");
            if (rates != null && rates.has(to)) {
                return rates.get(to).getAsDouble();
            } else {
                System.err.println("La devise cible " + to + " n’a pas été trouvée dans le JSON.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convertit un montant donné de la devise “from” vers la devise “to”.
     *
     * @param amount Le montant à convertir
     * @param from La devise de base
     * @param to La devise cible
     * @return le montant converti (amount × taux) ou null si erreur
     */
    public Double convertAmount(double amount, String from, String to) {
        Double rate = getRate(from, to);
        if (rate != null) {
            return amount * rate;
        } else {
            return null;
        }
    }
}
