import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConvertirMoneda {
    private String monedaOrigen;
    private String monedaDestino;
    private double tasaCambio;

    public ConvertirMoneda(String monedaOrigen, String monedaDestino) {
        this.monedaDestino = monedaDestino;
        this.monedaOrigen = monedaOrigen;
        this.tasaCambio = obtenerTasaCambio();
    }

    private double obtenerTasaCambio() {
        String apiKey = "85e9c3058b541e903bf659a7";
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/pair/" + monedaOrigen + "/" + monedaDestino;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();

            // Verifica si la respuesta contiene la tasa de cambio
            if (jsonResponse.has("conversion_rate")) {
                return jsonResponse.get("conversion_rate").getAsDouble();
            } else {
                System.out.println("Error al obtener la tasa de cambio.");
                return 0.0;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    // Método para convertir la cantidad especificada a la moneda de destino
    public double convertir(double cantidad) {
        return cantidad * tasaCambio;
    }
}
