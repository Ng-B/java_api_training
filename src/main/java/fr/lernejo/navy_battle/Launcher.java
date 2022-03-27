package fr.lernejo.navy_battle;

public class Launcher {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe incorrecte : ... <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server server = new Server(port);
        server.start();
    }

}
