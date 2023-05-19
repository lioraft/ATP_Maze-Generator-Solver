package Client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * The Client class is responsible for the client's strategy.
 * It receives an input stream and an output stream.
 */
public class Client {
    private InetAddress serverIP; // IP address of the server
    private int serverPort; // port of the server
    private IClientStrategy strategy; // strategy of the client

    // constructor
    public Client(InetAddress serverIP, int serverPort, IClientStrategy strategy) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.strategy = strategy;
    }

    // start client
    public void start(){
        try(Socket serverSocket = new Socket(serverIP, serverPort)){
            System.out.println("connected to server - IP = " + serverIP + ", Port = " + serverPort);
            strategy.clientStrategy(serverSocket.getInputStream(), serverSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
