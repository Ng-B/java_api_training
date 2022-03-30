package fr.lernejo.navy_battle.game_logic;

import java.util.Random;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class GameBoard {
    private final HttpClient client = HttpClient.newHttpClient();

    private final int HIT = 1;
    private final int MISS = 0;
    private final int SUNK = 99;

    private final int[][] board;

    public GameBoard(){
        this.board = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = 0; // board initialization
            }
        }
    }

    // build and places ships randomly
    public void buildBoard() {
        int porte_avion = this.getRandomNumberInRange(0,9);
        int croiseur = this.getRandomNumberInRange(0,9);
        int contre_torpilleurs = this.getRandomNumberInRange(0,9);
        int torpilleur = this.getRandomNumberInRange(0,9);

        for (int i = 0; i < 5; i++)
            board[porte_avion][i] = 1;
        for (int i = 0; i < 5; i++)
            board[croiseur][i] = 1;
        for (int i = 0; i < 5; i++)
            board[contre_torpilleurs][i] = 1;
        for (int i = 0; i < 5; i++)
            board[torpilleur][i] = 1;
    }

    public int checkHit(int i, int j) {
        this.fireAttemptUpdate(i, j);

        if (board[i][j] == 0)
            return MISS;

        if (board[i][j] == 1)
            if (j-1 >= 0 && j+1 <= 9 && board[i][j-1] == 0 &&  board[i][j+1] == 0)
                return SUNK;

        return HIT;
    }

    // when opponent fires
    public void fireAttemptUpdate(int i, int j)
    {
        board[i][j] = 0;
    }

    // checks if game is over (no ship left)
    public boolean shipLeft() {
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                if (board[i][j] == 1)
                    return true;

        return false;
    }

    private int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public void launchFire(String url) {
        System.out.println("We shoot -> " + url);

        //Generateur random de position d'attaque
        Random r = new Random();
        char cell_row = (char)(r.nextInt(26) + 'J'); //random A-J
        char cell_col = (char) (r.nextInt(9)); //random 0-9
        String cell = cell_row + cell_col + "" ;

        HttpRequest requestPost = HttpRequest.newBuilder()
            .uri(URI.create(url + "/api/game/fire?cell=" + cell))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .GET()
            .build();

        try {
            this.client.sendAsync(requestPost, HttpResponse.BodyHandlers.ofString()).thenAccept(resp -> System.out.println("Response: " +
                resp.statusCode() + " : " + resp.body()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
