package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ExecutorService executor; // thread pool for the server

    // constructor of server
    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port; // initialize the port
        this.listeningIntervalMS = listeningIntervalMS; // initialize the listening interval
        this.strategy = strategy; // initialize the strategy
        this.executor = Executors.newFixedThreadPool(Configurations.getInstance().getThreadPoolSize()); // initialize the thread pool
    }

    // start the server
    public void start(){
        try {
            // initialize server socket based on the port, and set time out based on the listening interval
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningIntervalMS);
            // let user know the server is starting
            System.out.println("Starting server at port = " + port);

            // while the server is not stopped, accept clients and apply the strategy
            while (!stop) {
                try { // try to accept a client
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client accepted: " + clientSocket.toString());

                    try {
                        // apply the strategy with the client, using the thread pool, and close the client socket
                        Runnable runnable = () -> {
                            try {
                                strategy.applyStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        };
                        executor.execute(runnable); // execute the strategy
                        clientSocket.close(); // close socket when finish
                        executor.shutdown(); // shutdown the thread pool when finish
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                } catch (SocketTimeoutException e){ // if the server times out, let user know
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
