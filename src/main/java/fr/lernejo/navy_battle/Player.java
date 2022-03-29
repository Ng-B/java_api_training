package fr.lernejo.navy_battle;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Player {
    private final HttpClient client = HttpClient.newHttpClient();

    public void start(String url, String id, int port) {
        System.out.println("URL = " + url);

        HttpRequest requestPost = HttpRequest.newBuilder()
            .uri(URI.create(url + "api/game/start"))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"1\", \"url\":\"http://localhost:" + port + "\", \"message\":\"Lets play\"}"))
            .build();
        try {
            client.send(requestPost,  HttpResponse.BodyHandlers.ofString());
            System.out.println("Start request send : CHECK");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
