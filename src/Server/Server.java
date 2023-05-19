package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
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


    // thread pool is static variable of the class. each time a server is being initialized, it will use the same thread pool
    private static ExecutorService executor = Executors.newFixedThreadPool(Configurations.getInstance().getThreadPoolSize());

    // constructor of server
    public Server(int port, int listeningIntervalMS, IServerStrategy strategy) {
        this.port = port; // initialize the port
        this.listeningIntervalMS = listeningIntervalMS; // initialize the listening interval
        this.strategy = strategy; // initialize the strategy
    }

    // start the server
    public void start(){
        try {
            // try to make new connection, as runnable
                        Runnable runnable = () -> {
                            try {
                                ServerSocket serverSocket = new ServerSocket(port);
                                serverSocket.setSoTimeout(listeningIntervalMS);
                                LOG.info("Starting server at port = " + port);

                                while (!stop) {
                                    try {
                                        Socket clientSocket = serverSocket.accept();
                                        LOG.info("Client accepted: " + clientSocket.toString());
                                        try {
                                                strategy.applyStrategy(clientSocket.getInputStream(), clientSocket.getOutputStream());
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        finally {
                                            clientSocket.close();
                                        }
                                    } catch (SocketTimeoutException e){
                                        LOG.debug("Socket timeout");
                                    }

                                }
                            } catch (IOException e) {
                                LOG.error("IOException", e);
                            }
                        };
                        executor.execute(runnable); // execute the strategy
        } catch (Exception e) {
            LOG.error("Failed to execute connection", e);
        }
        finally {
            // check if there are active threads in the thread pool
            if (executor instanceof ThreadPoolExecutor) {
                ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                if (threadPoolExecutor.getActiveCount() == 0) // if there are no active threads in the thread pool, shut it down
                    executor.shutdown();
            }
        }
     }


    // stop the server
    public void stop(){
        LOG.info("Stopping server...");
        stop = true;
    }
}
