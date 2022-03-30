package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;
import fr.lernejo.navy_battle.game_logic.GameBoard;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int prt_number;
    private final String id;
    private final String URL;
    private final GameBoard game = new GameBoard();

    public Server(String id, int prt_number, String url) {
        this.id = id;
        this.prt_number = prt_number;
        this.URL = url;
    }

    public void start() {
        game.buildBoard(); //creates game board

        InetSocketAddress socketAddress = new InetSocketAddress(prt_number);
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        try {
            HttpServer httpServer = HttpServer.create(socketAddress, 1);
            System.out.println("Server started on port number : " + prt_number);

            httpServer.setExecutor(executorService);

            httpServer.createContext("/ping", new Ping());
            httpServer.createContext("/api/game/start", new GameStartHandler(this.id, this.prt_number, this.game));
            httpServer.createContext("/api/game/fire", new PlayerFireHandler(this.id, this.prt_number, this.game));
            httpServer.start();

            if (!this.URL.equals("")) {
                System.out.println("URL reception : CHECK");
                Player plyr = new Player();
                plyr.start(this.URL, this.id, this.prt_number);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
