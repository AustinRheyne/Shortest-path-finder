public class GridVerifier {

    public static boolean verifySymmetry(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (matrix[i][j] != matrix[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean verifyDegree(int[][] matrix, int width, int height) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            int x = i % width;
            int y = i / width;
            int degree = 0;
            // Check right neighbor
            if (x < width - 1) {
                degree++;
            }
            // Check bottom neighbor
            if (y < height - 1) {
                degree++;
            }
            // Check left neighbor
            if (x > 0) {
                degree++;
            }
            // Check top neighbor
            if (y > 0) {
                degree++;
            }
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    degree--;
                }
            }
            if (degree != 0) {
                return false;
            }
        }
        return true;
    }

    public static int countTotalConnections(int[][] matrix) {
        int totalConnections = 0;
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                totalConnections += matrix[i][j];
            }
        }
        return totalConnections;
    }
}