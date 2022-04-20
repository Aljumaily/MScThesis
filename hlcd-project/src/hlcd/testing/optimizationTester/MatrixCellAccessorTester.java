package hlcd.testing.optimizationTester;

import hlcd.operations.Benchmarker;
import hlcd.operations.Matrix;

/**
 * Tests the logic of getting and setting cells in the matrix. A matrix is
 * generated randomly, another matrix will start as a null matrix. The test
 * is to copy from the generated matrix into the null matrix. The objective
 * is to have both matrices become equal. This uses the notion of getting a
 * cell and setting another.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 18th, 2022)
 * @since 1.8
 */
public class MatrixCellAccessorTester {
    private byte n;
    private byte k;
    private byte base = 4;

    /**
     * Must be true all the time to ensure both implementations yield the
     * same result.
     */
    private boolean validImplementation = true;

    /**
     * Chooses a random matrix dimension.
     */
    public MatrixCellAccessorTester() {
        this.n = getRandomValue((byte) 1, (byte) 30);
        this.k = getRandomValue((byte) 1, n);
    }

    /**
     * Creates the matrix based on the {@code n} and {@code k} values specified.
     *
     * @param n the number of columns in the matrix
     * @param k the number of rows in the matrix
     */
    public MatrixCellAccessorTester(byte n, byte k) {
        this.n = n;
        this.k = k;
    }

    /**
     * Will create x random quaternary matrices, where x is the number of
     * iterations specified and copy each matrix cells into a new matrix by
     * using {@code getCell} and {@code setCell}. An error message will be
     * printed if the two matrices don't match. This is benchmarked but the
     * amount of time doesn't have any significance.
     *
     * @param iterations the number of random matrices to be tested
     */
    public void testCellAccessors(long iterations) {
        System.out.println("Running cell accessing test of " +
                String.format("%,d", iterations) + "\n" +
                "randomly generated quaternary matrices...");
        Benchmarker mark = new Benchmarker();
        mark.start();
        mark.displayStartTime("Started: ");
        for (long i = 0; i < iterations; i++) {
            Matrix matrix = new Matrix(n, k);
            testCellAccessMethodEngine(matrix);
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        System.out.println();
    }

    /**
     * Will copy the cells of the specified matrix into a new matrix by using
     * {@code getCell} and {@code setCell}. A check will then occur to ensure
     * both matrices contain the same vectors. An error message will be
     * printed if the two matrices don't match.
     *
     * @param matrix the matrix to copy the elements from
     */
    public void testCellAccessors(Matrix matrix) {
        testCellAccessMethodEngine(matrix);
    }

    /**
     * Will copy the cells of the specified matrix into a new matrix by using
     * {@code getCell} and {@code setCell}. A check will then occur to ensure
     * both matrices contain the same vectors. An error message will be
     * printed if the two matrices don't match.
     *
     * @param matrix the matrix to copy the elements from
     */
    private void testCellAccessMethodEngine(Matrix matrix) {
        Matrix temp = new Matrix(n, k, base);
        //populate temp (which is a null matrix) with the values in the matrix
        for (byte r = 0; r < k; r++) {
            for (byte c = 0; c < n; c++) {
                byte digit = matrix.getCell(r, c);
                temp.setCell(r, c, digit);
            }
        }
        long[] m1 = matrix.getMatrixArray();
        long[] m2 = temp.getMatrixArray();

        if (!areEqual(matrix, temp)) {
            System.out.println("The two matrices didn't match.");
            System.out.println("The original matrix is: ");
            matrix.printMatrix();
            System.out.println("The second matrix is: ");
            temp.printMatrix();
        }
    }

    /**
     * Returns {@code true} if the two specified matrices are equal, {@code
     * false} otherwise.
     *
     * @param m1 the first matrix used for checking
     * @param m2 the second matrix used for checking
     * @return {@code true} if the two specified matrices are equal, {@code
     * false} otherwise
     */
    public boolean areEqual(Matrix m1, Matrix m2) {
        long[] m1Array = m1.getMatrixArray();
        long[] m2Array = m2.getMatrixArray();

        //check if the vectors in the matrix arrays are equal
        for (byte r = 0; r < m1Array.length; r++) {
            if (m1Array[r] != m2Array[r]) {
                System.out.println("Row (zero-based) " + r + " doesn't match");
                validImplementation = false;
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a random {@code byte} value between (and including) the range
     * specified.
     *
     * @param min the minimum value in the range
     * @param max the maximum value in the range
     * @return a random value between (and including) the range specified
     */
    private byte getRandomValue(byte min, byte max) {
        return (byte) ((Math.random() * ((max - min) + 1)) + min);
    }

    /**
     * Returns {@code true} if the test was successful in the sense that both
     * implementations yielded the same results, {@code false} otherwise.
     *
     * @return {@code true} if the test was successful in the sense that both
     * implementations yielded the same results, {@code false} otherwise
     */
    public boolean isValidImplementation() {
        return validImplementation;
    }

    /* A sample execution of this class is commented out*/

//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running MatrixCellAccessorTester...");
//        byte n = 7;
//        byte k = 4;
//        long iterations = 1_000_000;
//        MatrixCellAccessorTester mcat = new MatrixCellAccessorTester();
//        mcat.testCellAccessors(iterations);
//        if (mcat.isValidImplementation()) {
//            System.out.println("The cell accessor test was successful. Both " +
//                    "implementations yielded the same results.");
//        } else {
//            System.out.println("The implementation is wrong.");
//        }
//        Benchmarker.beep();
//        System.out.println("MatrixCellAccessorTester completed.");
//    }
}