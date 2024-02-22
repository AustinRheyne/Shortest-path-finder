/**
 * This class uses the graph class to create a grid and find the shortest path from the bottom left to the top right.
 *
 * @author	Austin Rheyne
 * @version 	1.0, 02/22/2024
 */


import java.awt.*;
import java.awt.event.KeyEvent;

public class Main {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    public static boolean DISPLAY_DEBUG = false;
    public static boolean DEBUG = false;
    public static final int SQUARE_SIZE = 10;

    public static boolean[][] squares = new boolean[WIDTH][HEIGHT];
    public static boolean altDown;

    public static void main(String[] args) {
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering();

        Grid grid = new Grid(WIDTH, HEIGHT);

        displayGrid();


        while (true) {
            StdDraw.clear();
            if(DEBUG) { drawConnections(grid); }
            mouseDown(grid);
            displaySquares();
            displayShortestPath(grid);
            displayGrid();
            StdDraw.show();
        }
    }

    public static void mouseDown(Grid grid) {
        altDown = StdDraw.isKeyPressed(KeyEvent.VK_ALT);

        if (StdDraw.isMousePressed()){
            int x = (int)Math.floor(StdDraw.mouseX());
            int y = (int)Math.floor(StdDraw.mouseY());


            if(!altDown) {
                blockSquare(grid, x, y);
            } else {
                unblockSquare(grid, x, y);
            }
        }
    }
    public static void unblockSquare(Grid grid, int x, int y) {
        if(!squares[x][y]) { return; }

        int index = y * WIDTH + x;

        // Connect with the right neighbor
        if (x < WIDTH - 1 && !squares[x+1][y]) {
            grid.getGraph().getMatrix()[index][index + 1] = 1;
            grid.getGraph().getMatrix()[index + 1][index] = 1;
        }

        // Connect with the left neighbor
        if(x > 0 && !squares[x-1][y]) {
            grid.getGraph().getMatrix()[index][index - 1] = 1;
            grid.getGraph().getMatrix()[index - 1][index] = 1;
        }

        // Connect with the bottom neighbor
        if (y < HEIGHT - 1 && !squares[x][y+1]) {
            grid.getGraph().getMatrix()[index][index + WIDTH] = 1;
            grid.getGraph().getMatrix()[index + WIDTH][index] = 1;
        }

        // Connect with the top neighbor
        if(y > 0 && !squares[x][y-1]) {
            grid.getGraph().getMatrix()[index][index - WIDTH] = 1;
            grid.getGraph().getMatrix()[index - WIDTH][index] = 1;
        }
        squares[x][y] = false;
    }
    public static void blockSquare(Grid grid, int x, int y) {
        if(squares[x][y]) { return; }
        int node = x + (y * WIDTH);
        for (int c = 0; c < WIDTH * HEIGHT; c++) {
            grid.getGraph().getMatrix()[c][node] = 0;
        }

        for (int r = 0; r < WIDTH * HEIGHT; r++) {
            grid.getGraph().getMatrix()[node][r] = 0;
        }

        squares[x][y] = true;
    }
    public static void displayShortestPath(Grid grid) {
        int[][] path = grid.getGraph().shortestPath(0);
        int[] distances = path[0];
        int[] predecessors = path[1];
        // Highlight squares from 0 to WIDTH*HEIGHT-1
        StdDraw.setPenColor(Color.GREEN);
        int index = (WIDTH*HEIGHT)-1;
        while(index != 0) {
            if(predecessors[index] == -1) { return; }
            int x = index % WIDTH;
            int y = Math.floorDiv(index, WIDTH);
            StdDraw.filledSquare(x+0.5, y+0.5, 0.5);
            index = predecessors[index];
        }
        StdDraw.filledSquare(0.5, 0.5, 0.5);
    }
    public static void displayGrid() {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(0.005);
        for (int x = 0; x < WIDTH; x++) {
            StdDraw.line(x, 0, x, HEIGHT);
        }

        for (int y = 0; y < HEIGHT; y++) {
            StdDraw.line(0, y, WIDTH, y);
        }
    }

    public static void displaySquares() {
        StdDraw.setPenColor(Color.DARK_GRAY);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if(squares[x][y]) {
                    StdDraw.filledSquare(x+0.5, y+0.5, 0.5);
                }
            }
        }
    }

    public static void drawConnections(Grid grid) {
        // Loop through each piece and draw connections in from the matrix
        int[][] matrix = grid.getGraph().getMatrix();
        StdDraw.setPenColor(Color.RED);
        StdDraw.setPenRadius(1.0/(WIDTH*10));
        // Go from column to column, connecting based on rows
        int lines_drawn = 0;
        for (int x = 0; x < WIDTH*HEIGHT; x++) {
            for (int y = 0; y < WIDTH*HEIGHT; y++) {
                if(matrix[x][y] != 0) {
                    double startX = (x % WIDTH)+0.5;
                    double startR = Math.floorDiv(x, WIDTH)+0.5;
                    double endX = (y % WIDTH)+0.5;
                    double endY = Math.floorDiv(y, WIDTH)+0.5;

                    StdDraw.line(startX, startR, endX, endY);
                    lines_drawn++;
                    if(DISPLAY_DEBUG) {
                        System.out.println("Connecting (" + startX + " , " + startR + ") - (" + endX + " , " + endY + ")  |  [" + x + " , " + y + "]");
                    }
                }
            }

        }
        if(DISPLAY_DEBUG) {
            System.out.println("Total Lines: " + lines_drawn);
        }
    }

}
