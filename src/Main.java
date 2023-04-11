import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import test.RunMazeGenerator;
public class Main {
    public static void main(String[] args) {
        //testMaze();
        testSimpleMazeGenerator();
        System.out.println("Maze ");
        //testMyMazeGenerator();
        System.out.println("Maze ");
        //call the runMazeGenerator class
       // RunMazeGenerator.main(args);
        //test my maze generator
        //testMyMazeGenerator();

    }

    //create a new maze and print it
    public static void testMaze() {
        int[][] mazeArray = {{1, 1, 1, 1, 1},
                {0, 0, 0, 0, 1},
                {1, 1, 1, 0, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1}};
        Position p1 = new Position(0, 0);
        Position p2 = new Position(4, 4);

        //create a new maze
        Maze maze = new Maze(p1, p2, mazeArray);
        maze.print();
    }
    //test SimpleMazeGenerator class
    public static void testSimpleMazeGenerator() {
        SimpleMazeGenerator simpleMazeGenerator = new SimpleMazeGenerator();
        Maze maze = simpleMazeGenerator.generate(100, 100);
        maze.print();
    }
    //test MyMazeGenerator class
    public static void testMyMazeGenerator() {
        MyMazeGenerator myMazeGenerator = new MyMazeGenerator();
        Maze maze = myMazeGenerator.generate(10, 10);
        maze.print();
        //prints the maze
    }

}