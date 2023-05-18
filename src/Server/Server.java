package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * The Server class is responsible for the server's logic.
 * It receives a port, a listening interval and a strategy.
 * The server will listen to the port and accept clients.
 * For each client, the server will apply the strategy.
 * The server will stop when the stop() method is called.
 */

public class Server {
    private int port; // port number
    private int listeningIntervalMS; // listening interval
    private IServerStrategy strategy; // strategy to be applied with each client
    private boolean stop; // boolean to stop the server

    // constructor of server
    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port;
        this.listeningIntervalMS = listeningIntervalMS;
        this.strategy = strategy;
    }

    // start the server
    public void start(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningIntervalMS);
            System.out.println("Starting server at port = " + port);

            while (!stop) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client accepted: " + clientSocket.toString());

                    try {
                        strategy.applyStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
                        clientSocket.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } catch (SocketTimeoutException e){
                    System.out.println("Socket timeout");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // stop the server
    public void stop(){
        stop = true;
    }
}
