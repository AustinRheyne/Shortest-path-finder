/**
 * This class contains methods for the graph objects.
 * Course:	COMP 2100
 * Assignment:	Project 4
 *
 * @author	Austin Rheyne, Joel Justice
 * @version 	1.0, 12/01/2023
 */

import java.util.Arrays;
import java.util.Scanner;

public class Graph {
    private final int NODES;
    private int[][] matrix;


    /**
     * Constructor for graph object. Sets the graphs node count to numbers of nodes, and makes adjacency matrix
     * @param scanner Scanner object to read text file
     */
    public Graph(Scanner scanner){
        this.NODES = scanner.nextInt();
        this.matrix = new int[NODES][NODES];
        for (int i = 0; i < NODES; ++i){
            int edges = scanner.nextInt();
            for (int j = 0; j < edges; ++j){
                int connected = scanner.nextInt();
                int weight = scanner.nextInt();
                matrix[i][connected] = weight;
            }
        }
    }

    public Graph(int[][] matrix) {
        NODES = matrix.length;
        this.matrix = matrix;
    }
    private Graph (int nodes) {
        NODES = nodes;
        matrix = new int[nodes][nodes];
    }

    public boolean isValidNode(int node) {
        // Check to see if the given node is valid before we use it for the shortest path algorithm
        return node >= 0 && node < NODES;
    }
    /**
     * Getter method for the graphs adjacency matrix.
     * @return 2D array representing adjacency matrix
     */
    public int[][] getMatrix() {
        return matrix;
    }

    /**
     * Getter method for graph's number of nodes.
     * @return int number of nodes
     */
    public int getNodes(){
        return NODES;
    }

    /**
     * Checks if the graph is connected. A graph is connected if you can reach every node from every other node.
     * @return boolean if graph is connected
     */
    public boolean isConnected(){
        int[] visited = dfs(0);
        for (int i = 0; i < visited.length; i++) {
            if(visited[i] == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Method for minimum spanning tree. Graph much be connected ot have a minimum spanning tree. Uses Prim's algorithm
     * to find a minimum spanning tree. This algorithm starts at node and follows the shortest edge from a visited node
     * to a non-visited node.
     * @return Graph object representing the minimum spanning tree
     */
    public Graph mst(){
        if(!isConnected()) {
            System.out.println("Error: Graph is not connected.");
            return null;
        }
        // Get all the edges, put them in order,
        Graph result = new Graph(NODES);
        boolean[] s = new boolean[NODES];
        s[0] = true;
        for (int steps = 0; steps < NODES-1; steps++) {
            int end = 0;
            int start = 0;
            int shortestEdge = Integer.MAX_VALUE;
            for (int i = 0; i < NODES; i++) { // Check each row
                if(s[i]) { // If the node is in s
                    int[] row = matrix[i];
                    for (int j = 0; j < row.length; j++) { // Check Edge columns
                        int edge = row[j];
                        if (!s[j] && edge != 0 && edge < shortestEdge) {
                            shortestEdge = edge;
                            start = i;
                            end = j;
                        }
                    }
                }
            }
            s[end] = true;
            result.matrix[start][end] = shortestEdge;
            result.matrix[end][start] = shortestEdge;
        }
        return result;
    }

    /**
     * Method to find the shortest path in a graph at a user given node. Uses Dijkstra's algorithm.
     * @param start int starting node
     * @return 2D array of 2 arrays. The first representing the distances to a node, and the second representing the
     * node's predecessor
     */
    public int[][] shortestPath(int start){
        int[] distances = new int[NODES]; //distances
        Arrays.fill(distances, Integer.MAX_VALUE); // Set the distance of all nodes in distances to infinity

        distances[start] = 0; // Set starting node distance to 0
        int[] predecessors = new int[NODES]; //predecessors
        Arrays.fill(predecessors, -1);

        boolean[] S = new boolean[NODES]; //nodes seen

        for (int i = 0; i < NODES; i++) {
            int u = 0;
            int closestDist = Integer.MAX_VALUE;

            // Find closest neighbor
            for (int j = 0; j < NODES; j++) {
                if (S[j] == false && distances[j] < closestDist) {
                    closestDist = distances[j];
                    u = j;
                }
            }
            if (closestDist != Integer.MAX_VALUE) {
                // Check the neighbors distances from u
                for (int v = 0; v < NODES; v++) {
                    if (S[v] == false && matrix[u][v] != 0 && distances[v] > distances[u] + matrix[u][v]) {
                        // Update the distance and set the predecessor of v to u
                        distances[v] = distances[u] + matrix[u][v];
                        predecessors[v] = u;
                    }
                }

                // Mark off u
                S[u] = true;
            }
        }
        return new int[][] {distances, predecessors};
    }

    /**
     * Method to check if a graph is metric. A graph is metric if it is completely connected and follows the triangle
     * inequality
     */
    public void isMetric(){
        if(!isCompletelyConnected()) {
            System.out.println("Graph is not metric:  Graph is not completely connected.\n");
            return;
        } else {
            if(!followsTriangleInequality()) {
                System.out.println("Graph is not metric: Edges do not obey the triangle inequality.\n");
            } else {
                System.out.println("Graph is metric.\n");
            }
        }
    }

    /**
     * Method to check if graph is completely connected. A graph is completely connected if you can get from every node
     * directly from every other node.
     * @return boolean if graph is completely connected
     */
    private boolean isCompletelyConnected() {
        for (int i = 0; i < NODES; i++) {
            for (int j = 0; j < NODES; j++) {
                if (j != i && matrix[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check to see if graph follows triangle inequality. A graph follows the triangle inequality if every direct path
     * from one node to another node is the shortest path between the nodes.
     * @return boolean if graph is metric
     */
    private boolean followsTriangleInequality() {
        // 3 loops
        //      loop 1 goes through all nodes
        //      loop 2 goes through all nodes greater than loop 1's current
        //      loop 3 ges through all nodes greater than loop 2's current

        for (int u = 0; u < NODES; u++) {
            for (int w = u+1; w < NODES; w++) {
                for (int v = w+1; v < NODES; v++) {
                    if(matrix[u][v] > matrix[u][w] + matrix[w][v]) {
                        return false;
                    }

                    if(matrix[u][w] > matrix[u][v] + matrix[v][w]) {
                        return false;
                    }

                    if(matrix[w][v] > matrix[w][u] + matrix[u][v]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
    /**
     * Makes the graph metric. This means that the graph will now follow the trangle inequality as well as be completely
     * connected.
     */
    public void makeMetric(){
        if(!isConnected()){
            System.out.println("Error: Graph is not connected.");
        }
        else {
            // For every node set direct path to the shortest path in dijkstras
            for (int i = 0; i < NODES; i++) {
                int [][] path = shortestPath(i);
                int[] distance = path[0];
                for (int j = 0; j < NODES; j++) { // Loop through every other node
                    matrix[i][j] = distance[j]; // Updates shortest path to every other node
                }
            }
            System.out.println(toString());
        }
    }

    /**
     * Performs the Traveling Salesman Problem on the graph.
     */
    public void tsp(){
        if(!isConnected()){
            System.out.println("Error: Graph is not connected.");
        }
        else {
            int[] tour = new int[NODES+1];
            int[] bestTour = new int[NODES+1];
            bestTour[NODES] = Integer.MAX_VALUE;
            boolean[] seen = new boolean[NODES];
            seen[0] = true;

            tsp(0, 1, tour, bestTour, seen);
        }
    }

    /**
     * Recursive method for Traveling Salesman Problem
     * @param node current node
     * @param step next node
     * @param tour current tour
     * @param bestTour best tour seen
     * @param seen seen nodes
     */
    private void tsp(int node, int step, int[] tour, int[] bestTour, boolean[] seen){
        if(step == NODES){
            if(matrix[node][0] != 0){
                if(tour[NODES] + matrix[node][0] < bestTour[NODES]){
                    tour[NODES-1] = node;
                    for (int i = 0; i < NODES+1; i++) {
                        bestTour[i] = tour[i];
                    }
                    bestTour[NODES] += matrix[node][0];
                    displayTsp(bestTour);
                }
            }
        } else {
            // Loop through all nodes, if neighbor of current node update various things
            for (int i = 0; i < NODES; i++) {
                if(matrix[node][i] != 0 && seen[i] == false){
                    // Initialize the values for the next recursive call
                    tour[step-1] = node;
                    tour[NODES] += matrix[node][i];
                    seen[node] = true;

                    // Recursive call
                    tsp(i, step+1, tour, bestTour, seen);

                    // Deinitialize the values for the next iteration of the loop
                    tour[step] = 0;
                    tour[NODES] -= matrix[node][i];

                    seen[node] = false;
                }
            }
        }
    }

    /**
     * toString method for Traveling Salesman Problem.
     * @param tour tour being displayed
     */
    private void displayTsp(int[] tour) {
        StringBuilder result = new StringBuilder(tour[NODES] + ": " + tour[0]);
        for (int i = 1; i < NODES; i++) {
            result.append(" -> " + tour[i]);
        }
        result.append(" -> 0\n");
        System.out.println(result.toString());
    }

    public void approximateTsp(){
        // Check that the graph is metric
        if(isCompletelyConnected() && followsTriangleInequality()) {
            // Grab the mst of the graph
            Graph minimum = mst();

            // Find the shortest path to each node from the origin to the dfs
            int[] search = minimum.dfs(0);

            // Create room for the tour and add 1 spot for the total distance
            int[] tour = new int[NODES+1];

            for (int i = 0; i < NODES; i++) {
                tour[search[i]-1] = i;
            }
            // Place each node's distance from the dfs into the tour
            for (int i = 0; i < NODES-1; i++) {
                // Place the distance into the tour and add the total to the end
                tour[NODES] += matrix[tour[i]][tour[i+1]];
            }

            // Add in the distance from the last node to the 0th node
            tour[NODES] += matrix[tour[0]][tour[NODES-1]];

            displayTsp(tour);

        } else {
            System.out.println("Error: Graph is not metric.\n");
        }
    }

    private int[] dfs(int v) {
        int[] marked = new int[NODES+1];
        marked[NODES] = 1;
        dfs(marked, v);
        return marked;
    }

    private void dfs(int[] marked, int v) {
        marked[v] = marked[NODES];
        marked[NODES]++;
        for (int i = 0; i < NODES; i++) {
            if(matrix[v][i] != 0 && marked[i] == 0) {
                dfs(marked, i);
            }
        }
    }

    public void displayShortestPath(int[] pred, int[] dist, int start) {
        for (int i = 0; i < NODES; i++) {
            if(dist[i] != Integer.MAX_VALUE) {
                String output = i + ": (" + dist[i] + ")" + " ";
                String path = i + "";

                if (i != start) {
                    int current = pred[i];
                    while (current != start && current != -1) {
                        path = current + " -> " + path;
                        current = pred[current];
                    }
                    path = start + " -> " + path;
                }
                output += path;
                System.out.println(output);
            } else {
                System.out.println(i + ": (Infinity)");
            }
        }
        // Add the empty line for spacing
        System.out.println();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int x = 0; x < NODES; x++) {
            for (int y = 0; y < NODES; y++) {
                str.append(matrix[x][y]).append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }

    public String toPrettyString() {
        StringBuilder str = new StringBuilder();
        str.append(NODES + "\n");
        for (int i = 0; i < NODES; i++) {
            StringBuilder suffix = new StringBuilder();
            int sum = 0;
            for (int j = 0; j < NODES; j++) { // Get sum, keep track of nodes
                if(matrix[i][j] != 0) {
                    suffix.append(j + " " + matrix[i][j] + " ");
                    sum++;
                }
            }
            str.append(sum + " " + suffix.toString() + "\n");
        }
        return str.toString();
    }
}