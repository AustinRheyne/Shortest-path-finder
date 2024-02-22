public class Grid {
    private int height;
    private int width;
    private Graph graph;

    public Graph getGraph() { return graph; }

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;

        // Create a matrix then build a graph off it
        int[][] matrix = new int[width*height][width*height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int index = y * width + x;

                // Connect with the right neighbor
                if (x < width - 1) {
                    matrix[index][index + 1] = 1;
                    matrix[index + 1][index] = 1;
                }

                // Connect with the bottom neighbor
                if (y < height - 1) {
                    matrix[index][index + width] = 1;
                    matrix[index + width][index] = 1;
                }
            }
        }
        graph = new Graph(matrix);

    }


    @Override
    public String toString() {
        return graph.toString();
    }
}
