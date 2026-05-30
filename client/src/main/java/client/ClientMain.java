package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        String serverURL = "http://localhost:8080";
        if (args.length == 1) {
            serverURL = args[0];
        }
        try {
            new ChessClient(serverURL).run();
        } catch (Exception ex) {
            System.out.printf("Unable to connect to server : %s%n", ex.getMessage());
        }
    }
}
