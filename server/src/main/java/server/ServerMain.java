package server;

import chess.*;

public class ServerMain {
    public static void main(String[] args) {
        Server myServer = new Server();
        int port = myServer.run(8080);

        System.out.println("♕ 240 Chess Server: ");
    }
}
