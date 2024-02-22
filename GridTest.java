import org.junit.Test;
import static org.junit.Assert.*;

public class GridTest {

    @Test
    public void testSymmetry() {
        int[][] matrix = new Grid(10, 10).getGraph().getMatrix();
        assertTrue(GridVerifier.verifySymmetry(matrix));
    }

    @Test
    public void testDegree() {
        int[][] matrix = new Grid(10, 10).getGraph().getMatrix();
        assertTrue(GridVerifier.verifyDegree(matrix, 10, 10));
    }

    @Test
    public void testTotalConnections() {
        int[][] matrix = new Grid(10, 10).getGraph().getMatrix();
        assertEquals(40, GridVerifier.countTotalConnections(matrix));
    }
}