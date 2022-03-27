package fr.lernejo.navy_battle;

import java.util.UUID;

public class Launcher {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe incorrecte : ... <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        String id = UUID.randomUUID().toString();

        Server server = new Server(id,port);
        server.start();
    }

}
