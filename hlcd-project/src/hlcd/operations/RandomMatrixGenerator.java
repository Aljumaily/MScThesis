package hlcd.operations;

import hlcd.enums.Style;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates a random matrix based on the parameters specified.
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running RandomMatrixGenerator...");
 *     RandomMatrixGenerator rmg = new RandomMatrixGenerator((byte) 20);
 *     rmg.printMatrix(" ", true, Style.DECIMAL, true);
 *     System.out.println("RandomMatrixGenerator completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 10th, 2022)
 * @since 1.8
 */
public class RandomMatrixGenerator {
    private long[] matrixArray;
    private byte minK;
    private byte maxK;
    private byte minN;
    private byte maxN;
    private byte n;
    private byte k;
    private byte base;

    /**
     * Initializes a random matrix with a random base, random \(k\) and \(n\)
     * values.
     */
    public RandomMatrixGenerator() {

        this.base = getRandomBase();
        maxN = this.base == 2 ? (byte) 62 : (byte) 30;
        this.minK = 1;
        this.maxK = maxN;
        this.minN = 1;
        n = getRandomValue(minN, maxN);
        k = getRandomValue(minK, maxK);
        populateMatrixArray();
    }

    /**
     * Create a random square matrix with the assumption that the base is \(4\).
     *
     * @param size the size of the random matrix to be created
     */
    public RandomMatrixGenerator(byte size) {
        if (size > 30) {
            System.out.println("Size specified is larger than 30. Since the " +
                                       "base is 4 by default, size is " +
                                       "changed to 30.");
            size = 30;
        }
        this.minK = size;
        this.maxK = size;
        this.minN = size;
        this.maxN = size;
        this.base = 4;
        n = getRandomValue(minN, maxN);
        k = getRandomValue(minK, maxK);
        populateMatrixArray();
    }

    /**
     * Randomly initializes the matrix based on the parameters specified.
     *
     * @param n    the length of each vector in the matrix
     * @param k    the dimension of the matrix
     * @param base the base which could either be \(2\) or \(4\)
     */
    public RandomMatrixGenerator(byte n, byte k, byte base) {
        this.k = k;
        this.n = n;
        this.base = base;
        this.minK = k;
        this.maxK = k;
        this.minN = n;
        this.maxN = n;
        populateMatrixArray();
    }

    /**
     * Initializes the current matrix array to the specified array and the other
     * specified parameters to the current ones.
     *
     * @param matrixArray the matrix array
     * @param n           the length of each vector in the matrix
     * @param k           the dimension of the matrix
     * @param base        the base which could either be \(2\) or \(4\)
     */
    public RandomMatrixGenerator(long[] matrixArray, byte n, byte k, byte base) {
        if (k != matrixArray.length) {
            System.out.println("RandomMatrixGenerator Note: The matrix length " +
                                       "and k don't match. The length is: " +
                                       matrixArray.length + " and k is: " + k +
                                       ". The value k will be changed to " +
                                       matrixArray.length);
            k = (byte) matrixArray.length;
        }
        this.k = k;
        this.n = n;
        this.base = base;
        this.minK = k;
        this.maxK = k;
        this.minN = n;
        this.maxN = n;
        this.matrixArray = new long[matrixArray.length];
        setMatrixArray(matrixArray);
    }

    /**
     * Randomly creates a matrix based on the range of \(n\) and range of \(k\)
     * specified.
     *
     * @param minN the smallest \(n\) value to be generated
     * @param maxN the largest \(n\) value to be generated
     * @param minK the smallest \(k\) value to be generated
     * @param maxK the largest \(k\) value to be generated
     * @param base the base which could either be \(2\) or \(4\)
     */
    public RandomMatrixGenerator(
            byte minN,
            byte maxN,
            byte minK,
            byte maxK,
            byte base
    ) {
        if (minK > maxK) {
            System.out.println("The minimum k value (" + minK + ") is larger" +
                                       " than the maximum k value (" + maxK +
                                       "). They are swapped.");
            byte temp = minK;
            minK = maxK;
            maxK = temp;
        }

        if (minN > maxN) {
            System.out.println("The minimum n value (" + minN + ") is larger" +
                                       " than the maximum n value (" + maxN +
                                       "). They are swapped.");
            byte temp = minN;
            minN = maxN;
            maxN = temp;
        }
        this.minK = minK;
        this.maxK = maxK;
        this.minN = minN;
        this.maxN = maxN;
        this.base = base;

        n = getRandomValue(minN, maxN);
        k = getRandomValue(minK, maxK);
        populateMatrixArray();
    }

    /**
     * Converts the specified byte matrix array to a long matrix array.
     *
     * @param matrixArray the byte matrix array to be converted to long
     *                    matrix array
     * @return the specified matrix array as a long matrix array
     */
    public long[] convertByteArrayToLongArray(byte[] matrixArray) {
        long[] result = new long[matrixArray.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = matrixArray[i];
        }
        return result;
    }

    /**
     * Converts the specified long matrix array to a byte matrix array.
     *
     * @param matrixArray the long matrix array to be converted to byte
     *                    matrix array
     * @return the specified matrix array as a byte matrix array
     */
    public byte[] convertLongArrayToByteArray(long[] matrixArray) {
        byte[] result = new byte[matrixArray.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) matrixArray[i];
        }
        return result;
    }

    /**
     * Populates the matrix array
     */
    private void populateMatrixArray() {
        matrixArray = new long[k];
        byte power = (base == 2) ? n : (byte) (2 * n);
        long min = 0;
        long max = Functions.power(2, power);
        for (int i = 0; i < matrixArray.length; i++) {
            matrixArray[i] = getRandomValue(min, max);
        }
    }

    /**
     * Prints the \(n\), \(k\) and base values.
     */
    public void printParameters() {
        System.out.println("The value of n is between [" + minN + ", "
                                   + maxN + "] which is chosen as " + n);
        System.out.println("The value of k is between [" + minK + ", "
                                   + maxK + "] which is chosen as " + k);
        System.out.println("The base is: " + base);
    }

    /**
     * Prints the matrix on console using the following default properties:
     * the columns are separated by a space, the brackets are added, the
     * style is base 10 and the size is shown.
     */
    public void printMatrix() {
        printMatrixEngine(
                matrixArray, n, base, " ", true, Style.DECIMAL, true
        );
    }

    /**
     * Prints the matrix on console while using the space character as the
     * delimiter between the columns.
     *
     * @param addBrackets should surround the matrix with brackets
     * @param style       the style format which could either be binary,
     *                    quaternary, decimal or \(\LaTeX\)
     * @param showSize    should the dimension of the matrix be displayed in
     *                    the bottom-right corner
     */
    public void printMatrix(
            boolean addBrackets,
            Style style,
            boolean showSize
    ) {
        printMatrixEngine(
                matrixArray, n, base, " ", addBrackets, style, showSize
        );
    }

    /**
     * Prints the matrix array specified on console using the parameters passed.
     *
     * @param delimiter   the delimiter used to separate the column
     * @param addBrackets should surround the matrix array with brackets
     * @param style       the style format which could either be binary,
     *                    quaternary, decimal or \(\LaTeX\)
     * @param showSize    should the dimension of the matrix array be
     *                    displayed in the bottom-right corner
     */
    public void printMatrix(
            String delimiter,
            boolean addBrackets,
            Style style,
            boolean showSize
    ) {
        printMatrixEngine(
                matrixArray, n, base, delimiter, addBrackets, style, showSize
        );
    }

    /**
     * Prints the matrix array specified on console using the parameters passed.
     *
     * @param matrixArray the matrix array to be printed on console
     * @param n           the length of each vector in the matrix
     * @param base        the base of the code which could either be \(2\) or \(4\)
     * @param delimiter   the delimiter used to separate the columns
     * @param addBrackets should surround the matrix with brackets
     * @param style       the style format which could either be binary,
     *                    quaternary, decimal or \(\LaTeX\)
     * @param showSize    should the dimension of the matrix be displayed in
     *                    the bottom-right corner
     */
    public void printMatrix(
            long[] matrixArray,
            byte n,
            byte base,
            String delimiter,
            boolean addBrackets,
            Style style,
            boolean showSize

    ) {
        printMatrixEngine(
                matrixArray, n, base, delimiter, addBrackets, style, showSize
        );
    }

    /**
     * Prints the matrix array specified on console using the parameters passed.
     *
     * @param matrixArray the matrix array to be printed on console
     * @param n           the length of each vector in the matrix
     * @param base        the base of the code which could either be \(2\) or \(4\)
     * @param delimiter   the delimiter used to separate the columns
     * @param addBrackets should surround the matrix with brackets
     * @param style       the style format which could either be binary,
     *                    quaternary, decimal or \(\LaTeX\)
     * @param showSize    should the dimension of the matrix be displayed in
     *                    the bottom-right corner
     */
    private void printMatrixEngine(
            long[] matrixArray,
            byte n,
            byte base,
            String delimiter,
            boolean addBrackets,
            Style style,
            boolean showSize
    ) {
        MatrixPrinter mp = new MatrixPrinter(matrixArray, n, k, base, style);
        mp.printMatrix(delimiter, addBrackets, showSize);
    }

    /**
     * Returns a 2-D array of the current 1-D matrix array.
     *
     * @return a 2-D array of the current 1-D matrix array
     */
    public byte[][] to2DArray() {
        return to2DArrayEngine(matrixArray, n, k, base);
    }

    /**
     * Returns a 2-D array of the 1-D matrix array specified.
     *
     * @param matrixArray the 1-D matrix array to transform to a 2-D matrix
     *                    array
     * @param n           the number of columns in the matrix array specified
     * @param k           the number of rows in the matrix array specified
     * @param base        the base which can either be \(2\) or \(4\)
     * @return a 2-D array equivalent of the matrix array specified
     */
    public byte[][] to2DArray(long[] matrixArray, byte n, byte k, byte base) {
        return to2DArrayEngine(matrixArray, n, k, base);
    }

    /**
     * The engine that transforms the 1-D matrix array specified to a 2-D array.
     *
     * @param matrixArray the 1-D matrix array to transform to a 2-D matrix
     *                    array
     * @param n           the number of columns in the matrix array specified
     * @param k           the number of rows in the matrix array specified
     * @param base        the base which can either be \(2\) or \(4\)
     * @return a 2-D array equivalent of the matrix array specified
     */
    private byte[][] to2DArrayEngine(
            long[] matrixArray,
            byte n,
            byte k,
            byte base
    ) {
        if (k != matrixArray.length) {
            System.out.println("RandomMatrixGenerator Note: The matrix " +
                                       "length and k don't match. The length " +
                                       "is: " + matrixArray.length + " and k " +
                                       "is: " + k + ". The value k will be" +
                                       " changed to " + matrixArray.length
            );
            k = (byte) matrixArray.length;
        }
        byte[][] result = new byte[k][n];
        byte padding = (base == 2) ? n : (byte) (n * 2);
        byte delimiterPosition = (base == 2) ? (byte) 1 : (byte) 2;
        String regexFormat = "(.{" + delimiterPosition + "})(?!$)";//put the
        // delimiter every character (base 2) or every two characters (base 4)

        for (byte i = 0; i < matrixArray.length; i++) {
            String currentRowString = String.format(
                    "%" + (int) padding + "s",
                    Long.toBinaryString(matrixArray[i])
            );

            currentRowString = currentRowString.replace(
                    " ", "0"
            );
            currentRowString = currentRowString.replaceAll(
                    regexFormat, "$1" + ","
            );

            String[] stringDigitsRow = currentRowString.split(",");

            if (stringDigitsRow.length != n) {
                System.out.println("something wrong happened!");
            }
            for (int j = 0; j < n; j++) {
                byte index = (byte) (n - stringDigitsRow.length);
                result[i][j] = Byte.parseByte(
                        stringDigitsRow[j - index], 2
                );
            }
        }
        return result;
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
     * Returns a random {@code long} value between (and including) the range
     * specified.
     *
     * @param min the minimum value in the range
     * @param max the maximum value in the range
     * @return a random value between (and including) the range specified
     */
    private long getRandomValue(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max);
    }

    /**
     * Returns a random base which could either be \(2\) or \(4\).
     *
     * @return a random base which could either be \(2\) or \(4\)
     */
    private byte getRandomBase() {
        return (Math.random() <= 0.5) ? ((byte) 2) : ((byte) 4);
    }

    /**
     * Returns the current matrix array.
     *
     * @return the current matrix array
     */
    public long[] getMatrixArray() {
        return matrixArray;
    }

    /**
     * Returns a clone of the current matrix array.
     *
     * @return a clone of the current matrix array
     */
    public long[] getMatrixArrayClone() {
        long[] result = new long[matrixArray.length];
        for (int i = 0; i < matrixArray.length; i++) {
            result[i] = matrixArray[i];
        }
        return result;
    }

    /**
     * Sets the current matrix array to the specified one.
     *
     * @param matrixArray the matrix to set as
     */
    private void setMatrixArray(long[] matrixArray) {
        for (int i = 0; i < matrixArray.length; i++) {
            this.matrixArray[i] = matrixArray[i];
        }
    }

    /**
     * Returns the number of columns in the matrix array.
     *
     * @return the number of columns in the matrix array
     */
    public byte getN() {
        return n;
    }

    /**
     * Returns the number of row in the matrix array.
     *
     * @return the number of row in the matrix array
     */
    public byte getK() {
        return k;
    }

    /**
     * Returns the base which could either be \(2\) or \(4\).
     *
     * @return the base which could either be \(2\) or \(4\)
     */
    public byte getBase() {
        return base;
    }

    /**
     * Returns the minimum value of the \(k\) range.
     *
     * @return the minimum value of the \(k\) range
     */
    public byte getMinK() {
        return minK;
    }

    /**
     * Returns the maximum value of the \(k\) range.
     *
     * @return the maximum value of the \(k\) range
     */
    public byte getMaxK() {
        return maxK;
    }

    /**
     * Returns the minimum value of the \(n\) range.
     *
     * @return the minimum value of the \(n\) range
     */
    public byte getMinN() {
        return minN;
    }

    /**
     * Returns the maximum value of the \(n\) range.
     *
     * @return the maximum value of the \(n\) range
     */
    public byte getMaxN() {
        return maxN;
    }

    /**
     * Returns the current matrix array as a {@code Matrix} type.
     *
     * @return the current matrix array as a {@code Matrix} type
     */
    public Matrix getMatrix() {
        return new Matrix(this.matrixArray, this.n, this.k, this.base);
    }

    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running RandomMatrixGenerator...");
//        RandomMatrixGenerator rmg = new RandomMatrixGenerator((byte) 20);
//        rmg.printMatrix(" ", true, Style.DECIMAL, true);
//        System.out.println("RandomMatrixGenerator completed.");
//    }

}
