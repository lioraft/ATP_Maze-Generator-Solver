import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*
    * This class is used to log messages to the server log.
 */
public class ServerLogger {
    private static final Logger LOG = LogManager.getLogger("ServerLogger");

    // log connection of new client
    private static void logServerStarted() {
        try {
            LOG.info("Client connected: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            LOG.error("Unknown host: " + e.getMessage());
        }
    }

    // log disconnection of client
    private static void logServerStopped() {
        try {
            LOG.info("Client disconnected: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            LOG.error("Unknown host: " + e.getMessage());
        }
    }

    // log maze generation
    private static void logMazeGeneration(String host, int rows, int cols) {
        LOG.info("Client " + host + " generated maze of size " + rows + "x" + cols);
    }

    // log error in maze generation
    private static void logMazeGenerationError(String host, int rows, int cols) {
        LOG.error("Client " + host + " failed to generate maze of size " + rows + "x" + cols);
    }

    // log maze solution
    private static void logMazeSolution(String host, int rows, int cols) {
        LOG.info("Client " + host + " requested maze solution of size " + rows + "x" + cols);
    }

    // log error in maze solution
    private static void logMazeSolutionError(String host, int rows, int cols) {
        LOG.error("Client " + host + " request to solve maze of size " + rows + "x" + cols + " failed");
    }

    // log changing character position
    private static void logCharacterPosition(String host, int row, int col) {
        LOG.info("Client " + host + " moved character to position " + row + "," + col);
    }

    // log fatal error of client trying to perform operations on null maze
    private static void logFatalError(String host, String message) {
        LOG.fatal("Client " + host + " tried to perform operation on null maze: " + message);
    }

    // log loading maze from file
    private static void logLoadMaze(String host, String fileName) {
        LOG.info("Client " + host + " loaded maze from file " + fileName);
    }

    // log saving maze to file
    private static void logSaveMaze(String host, String fileName) {
        LOG.info("Client " + host + " saved maze to file " + fileName);
    }

    // log error in saving maze to file
    private static void logSaveMazeError(String host, String fileName) {
        LOG.error("Client " + host + " failed to save maze to file " + fileName);
    }

    // log error in loading maze from file
    private static void logLoadMazeError(String host, String fileName) {
        LOG.error("Client " + host + " failed to load maze from file " + fileName);
    }
}

