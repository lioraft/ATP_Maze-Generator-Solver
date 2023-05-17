package test;

import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import IO.SimpleCompressorOutputStream;
import IO.SimpleDecompressorInputStream;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.*;
import java.util.Arrays;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;

public class compareObjectSize {
    public static void main(String[] args) {

        // testing simpleCompressorOutputStream and simpleDecompressorInputStream
        String mazeFileName = "savedMaze.maze";
        AMazeGenerator mazeGenerator = new MyMazeGenerator();
            Maze maze = mazeGenerator.generate(100, 100); // Generate new maze
        try {
            // save maze to a file
            OutputStream simpleOut = new SimpleCompressorOutputStream(new FileOutputStream(mazeFileName));
            simpleOut.write(maze.toByteArray())
            ;
            simpleOut.flush();
            simpleOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte simpleSavedMazeBytes[] = new byte[0];
        try {
            //read maze from file
            InputStream simpleIn = new SimpleDecompressorInputStream(new FileInputStream(mazeFileName));
            simpleSavedMazeBytes = new byte[maze.toByteArray().length];
            simpleIn.read(simpleSavedMazeBytes);
            simpleIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Maze simpleLoadedMaze = new Maze(simpleSavedMazeBytes);
        boolean areMazesEquals =
                Arrays.equals(simpleLoadedMaze.toByteArray(), maze.toByteArray());
        System.out.println(String.format("SimpleCompressor: Mazes equal: %s", areMazesEquals));
        //maze should be equal to simpleLoadedMaze
        if (!areMazesEquals) {
            int[][] originalMazeArr = maze.getMaze();
            int[][] loadedMazeArr = simpleLoadedMaze.getMaze();
            for (int i = 0; i < originalMazeArr.length; i++) {
                for (int j = 0; j < originalMazeArr[i].length; j++) {
                    if (originalMazeArr[i][j] != loadedMazeArr[i][j])
                        System.out.println("i: " + i + " j: " + j + " original: " + originalMazeArr[i][j] + ", loaded: " + loadedMazeArr[i][j]);
                }
            }
        }

        // testing sizes of objects
        long size = ObjectSizeCalculator.getObjectSize(maze);
        System.out.println("Size of the actual maze: " + size + " bytes");

        // get size of maze in file
        Path pathOfSimpleCompressedMaze = Paths.get("savedMaze.maze");

        try {

            // get size of a maze file in bytes
            long bytes = Files.size(pathOfSimpleCompressedMaze);
            System.out.println(String.format("Size of the saved maze in bytes using simplecompressor: %,d bytes", bytes));

        } catch (IOException e) {
            e.printStackTrace();
        }


        // testing MyCompressorOutputStream and MyDecompressorInputStream
        try {
            // save maze to a file
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
            out.write(maze.toByteArray())
            ;
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte savedMazeBytes[] = new byte[0];
        try {
            //read maze from file
            InputStream in = new MyDecompressorInputStream(new FileInputStream(mazeFileName));
            savedMazeBytes = new byte[maze.toByteArray().length];
            in.read(savedMazeBytes);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Maze loadedMaze = new Maze(savedMazeBytes);
        areMazesEquals =
                Arrays.equals(loadedMaze.toByteArray(), maze.toByteArray());
        System.out.println(String.format("MyCompressor: Mazes equal: %s", areMazesEquals));
        //maze should be equal to loadedMaze


        if (!areMazesEquals) {
            int[][] originalMazeArr = maze.getMaze();
            int[][] loadedMazeArr = loadedMaze.getMaze();
            for (int i = 0; i < originalMazeArr.length; i++) {
                for (int j = 0; j < originalMazeArr[i].length; j++) {
                    if (originalMazeArr[i][j] != loadedMazeArr[i][j])
                        System.out.println("i: " + i + " j: " + j + " original: " + originalMazeArr[i][j] + ", loaded: " + loadedMazeArr[i][j]);
                }
            }
        }

        // testing sizes of objects
        // get size of maze in file
        Path pathOfMyCompressedMaze = Paths.get("savedMaze.maze");

        try {

            // get size of a maze file in bytes
            long bytes = Files.size(pathOfMyCompressedMaze);
            System.out.println(String.format("Size of the saved maze in bytes using mycompressor: %,d bytes", bytes));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
