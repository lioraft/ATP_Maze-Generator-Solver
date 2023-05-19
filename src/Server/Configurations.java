package Server;

/**
 * The Configurations class is responsible for the server's configurations.
 * It contains getters for thread pool size, the maze generating algorithm and the maze searching algorithm.
 * the values will be read from a properties file.
 * The class is implemented as a singleton.
 */
public class Configurations {
    private static Configurations instance = null; // instance of configurations

    // private constructor
    private Configurations() {
    }

    // get instance of the configurations object
    public static Configurations getInstance() {
        if (instance == null)
            instance = new Configurations();
        return instance;
    }

    // get the thread pool size
    public int getThreadPoolSize() {
        return 0;
    }

    // get the maze generating algorithm
    public String getMazeGeneratingAlgorithm() {
        return null;
    }

    // get the maze searching algorithm
    public String getMazeSearchingAlgorithm() {
        return null;
    }
}
