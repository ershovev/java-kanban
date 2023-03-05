package ru.yandex.practicum.serverclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {


    private final String apiKey;
    private final URI url;
    private final HttpClient client;

    public KVTaskClient(URI url) throws InterruptedException, IOException {
        this.url = url;
        String registerUrl = url + "/register";
        URI registerUri = URI.create(registerUrl);
        HttpRequest registrationRequest = HttpRequest.newBuilder().uri(registerUri).GET().build();
        client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(registrationRequest, HttpResponse.BodyHandlers.ofString());
        apiKey = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uriSave = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiKey);
        HttpRequest saveRequest = HttpRequest.newBuilder()
                .uri(uriSave)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(saveRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("HTTP код ответа: " + response.statusCode());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uriLoad = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiKey);
        HttpRequest loadRequest = HttpRequest.newBuilder()
                .uri(uriLoad)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(loadRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("HTTP код ответа: " + response.statusCode());
        return response.body();
    }
}
