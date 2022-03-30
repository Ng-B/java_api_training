package fr.lernejo.navy_battle;

import fr.lernejo.navy_battle.game_logic.Cell;
import fr.lernejo.navy_battle.game_logic.GameBoard;

import com.sun.net.httpserver.HttpExchange;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.IOException;
import java.io.InputStream;
import org.everit.json.schema.*;

import com.sun.net.httpserver.HttpHandler;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class PlayerFireHandler implements HttpHandler {
    private final String id;
    private final int port;
    private final GameBoard game;

    public PlayerFireHandler(String id, int port, GameBoard game) {
        this.id = id;
        this.port = port;
        this.game = game;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("At least GET works !!");
        if (exchange.getRequestMethod().equals("GET")) {
            //JSON VALIDATION
            try {
                // transform expected JSON schema into object
                InputStream originalJSON = getClass().getResourceAsStream("/valid_schema_get.json");
                JSONObject inputObj = new JSONObject(new JSONTokener(originalJSON));
                // compare expected JSON to sent JSON
                Schema schema = SchemaLoader.load(inputObj);
                JSONObject requestJSON = new JSONObject(new JSONTokener(exchange.getRequestBody()));
                schema.validate(requestJSON);

            } catch (ValidationException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }

            handleFire(exchange);

        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        }
    }

    public void handleFire(HttpExchange exchange) throws IOException {
        //GET query handle
        String query = exchange.getRequestURI().getQuery();
        String[] entry = query.split("=");
        String target = entry[1];

        int i_coord = new Cell().convertLetterToInt(target);
        int j_coord = target.charAt(1);

        int hit_consequence = this.game.checkHit(i_coord, j_coord);

        exchange.getResponseHeaders().set("Content-type", "application/json");
        String body = "{\"consequence\":\"" + getConsequence(hit_consequence) + "\", \"shipLeft\":\"" + this.game.shipLeft() + "\"}";
        exchange.sendResponseHeaders(200, body.length());

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(body.getBytes());}

        // we respond to attack as long as there are ships left
        if (this.game.shipLeft())
        {
            String url = "http://localhost:" +  exchange.getLocalAddress().getAddress();
            this.game.launchFire(url);
        }

    }

    public String getConsequence(int code)
    {
        if (code == 0)
            return "MISS";
        if (code == 1)
            return "HIT";
        if (code == 99)
            return  "SUNK";

        return "";
    }

}
