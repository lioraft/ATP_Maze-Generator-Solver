package Server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private int port; // port of the server
    private int listeningIntervalMS; // listening interval of the server
    private IServerStrategy strategy; // strategy of the server
    private volatile boolean stop; // boolean variable to stop the server
    private final Logger LOG = LogManager.getLogger(); // Log4j2 - for logging
    private ThreadPoolExecutor threadPool; // Thread pool


    // constructor - initialize the server
    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port;
        this.listeningIntervalMS = listeningIntervalMS;
        this.strategy = strategy;
        this.stop = false;
        threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(Configurations.getInstance().getThreadPoolSize()); // initialize the thread pool
    }

    // start the server
    public void start() {
        // try to make new connection, as runnable so it won't block the main thread
        Runnable runnable = () -> {
            try {
                // create new server socket
                ServerSocket serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(listeningIntervalMS);
                LOG.info("Starting server at port = " + port);

                // while the server is not stopped
                while (!stop) {
                    try {
                        // accept new client
                        Socket clientSocket = serverSocket.accept();
                        LOG.info("Client accepted: " + clientSocket.toString());

                        // The thread pool will handle the new Client in a new thread
                        threadPool.execute(() -> {
                            handleClient(clientSocket);
                        });

                    } catch (SocketTimeoutException e) { // if the server timed out
                        LOG.debug("Socket timeout");
                    }
                }
            } catch (IOException e) { // if there was an error with the server socket
                LOG.error("IOException", e);
            }
        };
        new Thread(runnable).start(); // Start the server thread
    }

    // this function is for handling the client in a separate thread. the start function will call this function in a new thread
    private void handleClient(Socket clientSocket) {
        try {
            // apply the strategy on the client
            strategy.applyStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            LOG.info("Done handling client: " + clientSocket.toString());
            // close the client socket
            clientSocket.close();
        } catch (IOException e){
            LOG.error("IOException", e);
        }
    }

    // this function is for stopping the server
    public void stop(){
        LOG.info("Stopping server...");
        stop = true; // change the indicator to true
        threadPool.shutdown(); // shut down pool
    }
}
