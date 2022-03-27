package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int prt_number;
    private final String id;

    public Server(String id, int prt_number) {
        this.id = id;
        this.prt_number = prt_number;
    }

    public void start() {
        InetSocketAddress socketAddress = new InetSocketAddress(prt_number);
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        try {
            HttpServer httpServer = HttpServer.create(socketAddress, 0);
            System.out.println("Server started on port number : " + prt_number);
            GameStartHandler gameHandler = new GameStartHandler(this.id, this.prt_number);

            httpServer.setExecutor(executorService);

            httpServer.createContext("/ping", new Ping());
            httpServer.createContext("/api/game/start", gameHandler);
            httpServer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
