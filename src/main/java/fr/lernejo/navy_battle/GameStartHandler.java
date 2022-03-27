package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.IOException;
import java.io.InputStream;
import org.everit.json.schema.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

public class GameStartHandler implements HttpHandler {
    private final String id;
    private final int port;

    public GameStartHandler(String id, int port) {
        this.id = id;
        this.port = port;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (exchange.getRequestMethod().equals("POST")) {
            //JSON VALIDATION
            try (InputStream inputStream = getClass().getResourceAsStream("/valid_schema.json")) {
                JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
                Schema schema = SchemaLoader.load(rawSchema);
                schema.validate(exchange.getRequestBody());
            }
            catch (ValidationException e) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

            String body = "{\"id\":\" " + this.id + "\", \"url\":\"http://localhost:" + this.port + "\", \"message\":\"May the best code win\"}";
            exchange.getResponseHeaders().set("Content-type", "application/json");
            exchange.sendResponseHeaders(202, body.length());

            try (OutputStream os = exchange.getResponseBody()){
                os.write(body.getBytes());
            }
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        }
    }
}
