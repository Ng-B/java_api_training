package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import fr.lernejo.navy_battle.game_logic.GameBoard;

import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.IOException;
import java.io.InputStream;
import org.everit.json.schema.*;
import com.sun.net.httpserver.HttpHandler;
import java.net.URL;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;
import java.nio.charset.StandardCharsets;

public class GameStartHandler implements HttpHandler {
    private final String id;
    private final int port;
    private final GameBoard game;

    public GameStartHandler(String id_, int port_, GameBoard game_) {
        this.id = id_;
        this.port = port_;
        this.game = game_;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (exchange.getRequestMethod().equals("POST")) {
            //JSON VALIDATION
            try {
                // transform expected JSON schema into object
                InputStream originalJSON = getClass().getResourceAsStream("/valid_schema_post.json");
                JSONObject inputObj = new JSONObject(new JSONTokener(originalJSON));
                // compare expected JSON to sent JSON
                Schema schema = SchemaLoader.load(inputObj);
                JSONObject requestJSON = new JSONObject(new JSONTokener(exchange.getRequestBody()));
                schema.validate(requestJSON);

                String resp = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("MESSAGE RECEIVED : " + resp);

            } catch (ValidationException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
           }

            String body = "{\"id\":\" " + this.id + "\", \"url\":\"http://localhost:" + this.port + "\", \"message\":\"May the best code win\"}";
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_ACCEPTED, body.getBytes().length);
            exchange.getResponseBody().write(body.getBytes());

            try {
                // Our game start request has been accepted, we respond with first fire
                String url = "http://localhost:" + this.port;
                this.game.launchFire(url);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try (OutputStream os = exchange.getResponseBody()){
                os.write(body.getBytes());}

        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        }
    }
}
