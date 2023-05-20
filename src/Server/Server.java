package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



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
    private volatile boolean stop;
    private final Logger LOG = LogManager.getLogger(); //Log4j2

    // thread pool. each time a server is being initialized, it will use a thread pool
    private ThreadPoolExecutor threadPool;

    // constructor of server
    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port; // initialize the port
        this.listeningIntervalMS = listeningIntervalMS; // initialize the listening interval
        this.strategy = strategy; // initialize the strategy
        threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool(); // initialize the thread pool
    }

    // start the server
    public void start(){
        try {
            stop = false; // initialize stop to false
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(listeningIntervalMS);
            LOG.info("Starting server at port = " + port);
            while (!stop) {
                // accept new clients
                Socket clientSocket = serverSocket.accept();
                LOG.info("Client accepted: " + clientSocket.toString());
                // create new client thread
                Thread clientThread = new Thread(() -> {
                    handleClient(clientSocket);
                });
                threadPool.execute(clientThread); // execute the client handler in the thread pool
            }
        } catch (Exception e) {
            LOG.error("Failed to execute connection");
        }
        finally {
            // check if there are active threads in the thread pool
                if (threadPool.getActiveCount() == 0) // if there are no active threads in the thread pool, shut it down
                    threadPool.shutdownNow();
            }
        }

    private void handleClient(Socket clientSocket) {
        try {
            strategy.applyStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
            LOG.info("Done handling client: " + clientSocket.toString());
            clientSocket.close();
        } catch (IOException e){
            LOG.error("IOException", e);
        }
    }


    // stop the server
    public void stop(){
        LOG.info("Stopping server...");
        stop = true; // stop the server
        threadPool.shutdown(); // shut down the thread pool
    }
}
