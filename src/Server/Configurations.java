package Server;

import java.io.*;
import java.util.Properties;

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
        // initialize pool size
        int poolSize = 0;
        try (InputStream input = new FileInputStream("C:\\Users\\Lior\\OneDrive\\שולחן העבודה\\שנה ב\\סמסטר ד\\נושאים מתקדמים בתכנות\\ATP-Project-PartA\\resources\\config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // if can retrieve, get the thread pool size and set it
            poolSize = Integer.parseInt(prop.getProperty("threadPoolSize"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // return pool size
        return poolSize;
    }

    // get the maze searching algorithm
    public String getMazeSearchingAlgorithm() {
        // initialize string of maze searcher
        String searchAlg = "";
        try (InputStream input = new FileInputStream("C:\\Users\\Lior\\OneDrive\\שולחן העבודה\\שנה ב\\סמסטר ד\\נושאים מתקדמים בתכנות\\ATP-Project-PartA\\resources\\config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // if can retrieve, get the search algorithm and set it
            searchAlg = prop.getProperty("mazeSearchingAlgorithm");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // return search algorithm
        return searchAlg;
    }

    // get the maze generating algorithm
    public String getMazeGeneratingAlgorithm() {
        // initialize string of maze generator
        String generateAlg = "";
        try (InputStream input = new FileInputStream("C:\\Users\\Lior\\OneDrive\\שולחן העבודה\\שנה ב\\סמסטר ד\\נושאים מתקדמים בתכנות\\ATP-Project-PartA\\resources\\config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // if can retrieve, get the generate algorithm and set it
            generateAlg = prop.getProperty("mazeGeneratingAlgorithm");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // return generate algorithm
        return generateAlg;
    }

    // write the properties to the properties file
    public void writeProperties() {
        // create outputstream with the path (path of the properties file is the local path on my laptop)
        try (OutputStream output = new FileOutputStream("C:\\Users\\Lior\\OneDrive\\שולחן העבודה\\שנה ב\\סמסטר ד\\נושאים מתקדמים בתכנות\\ATP-Project-PartA\\resources\\config.properties")) {

            // set new properties object
            Properties prop = new Properties();

            // set the properties value
            prop.setProperty("threadPoolSize", "5"); // thread pool size
            prop.setProperty("mazeGeneratingAlgorithm", "MyMazeGenerator"); // generate maze algorithm
            prop.setProperty("mazeSearchingAlgorithm", "BreadthFirstSearch"); // solve maze algorithm

            // save properties to project root folder
            prop.store(output, null);

            System.out.println(prop);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    // main that writes the properties to the properties file
    public static void main(String[] args) {
        Configurations.getInstance().writeProperties();
    }
}
