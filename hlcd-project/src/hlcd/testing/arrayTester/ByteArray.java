package hlcd.testing.arrayTester;

import hlcd.enums.Style;
import hlcd.exceptions.InvalidBaseException;
import hlcd.operations.Functions;
import hlcd.operations.LongArray;
import hlcd.operations.MatrixPrinter;

/**
 * A tester class that uses {@code byte}s instead of {@code long}s to store
 * vectors of 8-bits. It is created to test the structure implemented in
 * {@link LongArray} without the need to have significant amount of RAM. It
 * uses a 2-D array for storage. The size can be chosen arbitrary without
 * constraints or as \(base^k\). Each 1-D array also referred to as a
 * <i>segment</i>. This class uses
 * {@code Integer.MAX_VALUE - }{@link ByteArray#JVM_OVERHEAD} as the
 * maximum segment size (except possibly for the last segment). The last segment
 * will contain the remaining cells which could be less than or equal to the
 * maximum segment size. A 2-D array is used because Java's maximum 1-D array
 * is {@code Integer.MAX_VALUE - 8} or
 * \(2^{31} - 1 - 8 = 2,147,483,647 - 8 = 2,147,483,639\), which is not
 * sufficient for large values of \(k\) of quaternary codes. The value of
 * {@link ByteArray#JVM_OVERHEAD} is {@code 16} and is needed because VMs
 * reserve some header words in an array which reduces the <em>true</em>
 * maximum value. Java 8's implementation of {@link java.util.Hashtable} in
 * line {@code 397} has {@code MAX_ARRAY_SIZE} as
 * {@code Integer.MAX_VALUE - 8}. We are using {@code - 16} for extra
 * precaution.
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running ByteArray...");
 *     byte n = 20;
 *     byte k = 16;
 *     byte base = 4;
 *     long size = Functions.power(base, k);//or some number like 1L, 2L, etc
 *
 *     //Use the following if testing needs to be performed:
 *     /*
 *     //Make MAX_SEGMENT_LENGTH hold a smaller number, like 10000
 *     long full = (long) (Math.random() * 6);//number of full segments
 *     long extra = (long) (Math.random() * 10000);//last segment size
 *     if (Math.random() &#60; 0.5 &amp;&amp; full != 0) {
 *         extra *= -1;
 *     }
 *     size = MAX_SEGMENT_LENGTH * full + extra;
 *     *&sol;
 *
 *     double sizeInGb = (8 * size) / 8000000000.0;
 *     System.out.println("Total GBs of RAM used: " + sizeInGb);
 *
 *     ByteArray array = new ByteArray(size);
 *
 *     System.out.println("Size: " + array.SIZE);
 *
 *     //print the lengths of each 1-D array
 *     System.out.print("[");
 *     for (int i = 0; i &#60; array.ARRAY.length; i++) {
 *         System.out.print(array.ARRAY[i].length);
 *         if (i != array.ARRAY.length - 1) {
 *             System.out.print(", ");
 *         }
 *     }
 *     System.out.println("]");
 *
 *     //stores the sequence 0, 1, ..., 126, 127, 0, 1, ...
 *     for (long i = 0; i &#60; size; i++) {
 *         array.set(i, (byte) (i % 128L));
 *     }
 *
 *     //check each cell to ensure the above sequence is present
 *     for (long i = 0; i &#60; size; i++) {
 *         if (array.get(i) != i % 128L) {
 *             System.out.println(
 *                 "i = " + i + ", " +
 *                 "value stored = " + array.get(i) + ", " +
 *                 "correct value = " + (byte) (i % 128)
 *             );
 *         }
 *     }
 *     //Should the elements of the array be printed
 *     //array.print();// or use the next line for formatting
 *     //array.print(" ", Style.DECIMAL);
 *     System.out.println("ByteArray completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0
 * @since April 13th, 2022
 */
public class ByteArray {
    /**
     * The structure used to store the codewords of the code.
     */
    public final byte[][] ARRAY;

    /**
     * The total number of cells in the 2-D array.
     */
    private final long SIZE;

    /**
     * The number of 1-D arrays required.
     */
    private int segments;

    /**
     * The amount of cells that are excluded from an array because VMs reserve
     * some header words in an array which reduces the <em>true</em> maximum
     * value. It could be at least 8 but 16 is used for extra precaution.
     */
    public static final int JVM_OVERHEAD = 16;

    /**
     * The maximum size of each 1-D array.
     */
    public static int MAX_SEGMENT_LENGTH = Integer.MAX_VALUE - JVM_OVERHEAD;

    /**
     * The length of each codeword in the code.
     */
    private byte n;

    /**
     * The dimension of the code which is the same as the number of rows in
     * the generator matrix.
     */
    private byte k;

    /**
     * The base of the code (which can either be \(2\) or \(4\)).
     */
    private byte base;

    /**
     * Creates a combination array filled with zeros based on the parameters
     * specified.
     *
     * @param n    the length of the code
     * @param k    the dimension of the code
     * @param base the base of the code which could either be \(2\) or \(4\)
     */
    public ByteArray(byte n, byte k, byte base) {
        this.n = n;
        this.k = k;
        this.base = base;

        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        //TODO: test the other constructor and other values such as n, k, etc.

        SIZE = Functions.power(base, k);

        //number of 1-D arrays with full capacity
        segments = (int) (SIZE / (MAX_SEGMENT_LENGTH));//can be zero or larger

        //Last 1-D array is not full but needed (assuming the condition is true)
        if (SIZE % MAX_SEGMENT_LENGTH != 0) {
            segments++;
        }

        ARRAY = new byte[segments][];

        //initializing the 1-D arrays to the appropriate lengths. It will
        //keep on choosing MAX_SEGMENT_LENGTH as long as sizeLeft is larger. It
        //will eventually store the remaining size left into the last 1-D array.
        long sizeLeft = SIZE;
        for (int i = 0; i < ARRAY.length; i++) {
            int length = (int) Math.min(MAX_SEGMENT_LENGTH, sizeLeft);
            ARRAY[i] = new byte[length];
            sizeLeft -= length;
        }
    }

    /**
     * Creates a combination array filled with zeros based on the size
     * specified. This should only be used for testing purposes to ensure
     * there doesn't exist a bug in dividing the arrays and/or
     * accessing/setting specific indices.
     *
     * @param size the number of cells to create
     */
    public ByteArray(long size) {
        this.SIZE = size;
        //the values of n and base will be hardcoded so that we are able to
        //print the elements without encountering an exception.
        n = 30;
        base = 4;

        //number of 1-D arrays with full capacity
        segments = (int) (SIZE / (MAX_SEGMENT_LENGTH));//can be zero or larger

        //Last 1-D array is not full but needed (assuming the condition is true)
        if (SIZE % MAX_SEGMENT_LENGTH != 0) {
            segments++;
        }

        ARRAY = new byte[segments][];

        //initializing the 1-D arrays to the appropriate lengths. It will
        //keep on choosing MAX_SEGMENT_LENGTH as long as sizeLeft is larger. It
        //will eventually store the remaining size left into the last 1-D array.
        long sizeLeft = SIZE;
        for (int i = 0; i < ARRAY.length; i++) {
            int length = (int) Math.min(MAX_SEGMENT_LENGTH, sizeLeft);
            ARRAY[i] = new byte[length];
            sizeLeft -= length;
        }
    }

    /**
     * Returns a specific element in the combination array.
     *
     * @param index the index of the element
     * @return the element in the index specified
     */
    public final long get(final long index) {
        int s = (int) (index / MAX_SEGMENT_LENGTH);
        int i = (int) (index % MAX_SEGMENT_LENGTH);
        return ARRAY[s][i];
    }

    /**
     * Sets the value specified at the appropriate index.
     *
     * @param index the index of the element to set
     * @param value the value to be assigned to
     */
    public final void set(final long index, final byte value) {
        int s = (int) (index / MAX_SEGMENT_LENGTH);
        int i = (int) (index % MAX_SEGMENT_LENGTH);
        ARRAY[s][i] = value;
    }

    /**
     * Returns the number of elements.
     *
     * @return the number of elements
     */
    public long length() {
        return SIZE;
    }

    /**
     * Checks if the value specified is exists in the combination array.
     *
     * @param value the value to look for
     * @return {@code true} if the value exists, {@code false} otherwise
     */
    public boolean contains(final long value) {
        for (int i = 0; i < ARRAY.length; i++) {
            for (int j = 0; j < ARRAY[i].length; j++) {
                if (ARRAY[i][j] == value) return true;
            }
        }
        return false;
    }

    /**
     * Prints the elements in the combination array on screen. The values
     * will be printed in base 10.
     */
    public void print() {
        long counter = 0;
        for (int i = 0; i < ARRAY.length; i++) {
            for (int j = 0; j < ARRAY[i].length; j++) {
                System.out.format("(%d, %d) = %d \n", i, j, get(counter));
                counter++;
            }
            System.out.println();
        }
    }

    /**
     * Prints the elements in the combination array on screen as binary
     * values or decimal values.
     *
     * @param delimiter the delimiter between each digit
     * @param style     the style format which could either be binary,
     *                  quaternary, decimal or \(\LaTeX\)
     * @see ByteArray#print()
     */
    public void print(String delimiter, Style style) {
        System.out.println();
        int pad = (length() + "").length();
        for (int i = 0; i < length(); i++) {
            String v = MatrixPrinter.vectorAsString(
                    get(i), n, base, delimiter, style
            );
            String labelledVector = String.format("%" + pad + "s\t%s", i, v);
            System.out.println(labelledVector);
        }
    }

    /**
     * Compares whether the specified combination array is the same as the
     * current one. By <em>same</em>, it means both arrays have the same
     * values stored in the <em>same</em> order. Having the same values but
     * in shuffled order will return {@code false}.
     *
     * @param passed the combination array to be compared to
     * @return {@code true} if both combination arrays are the same, {@code
     * false} otherwise
     */
    public boolean equalsTo(ByteArray passed) {
        if (length() != passed.length()) {
            System.out.println("Current array length is " + length() +
                    " and passed array length is " + passed.length()
            );
            return false;
        }

        for (long i = 0; i < length(); i++) {
            if (get(i) != passed.get(i)) {
//                System.out.println("ARRAY 1");
//                print(" ", Style.DECIMAL);
//                System.out.println("\nARRAY 2");
//                print(" ", Style.DECIMAL);
//                System.out.println("First index not equal to: " + i);
                return false;
            }
        }
        return true;
    }

    /**
     * Executes the program.
     *
     * @param args the arguments specified but will be ignored
     */
    public static void main(String[] args) {
        System.out.println("Running ByteArray...");
        byte n = 20;
        byte k = 16;
        byte base = 4;
        long size = Functions.power(base, k);//or some number like 1L, 2L, etc

        //Use the following if testing needs to be performed:
        /*
        //Make MAX_SEGMENT_LENGTH hold a smaller number, like 10000
        long full = (long) (Math.random() * 6);//number of full segments
        long extra = (long) (Math.random() * 10000);//last segment size
        if (Math.random() < 0.5 && full != 0) {
            extra *= -1;
        }
        size = MAX_SEGMENT_LENGTH * full + extra;
        */
        double sizeInGb = (8 * size) / 8000000000.0;
        System.out.println("Total GBs of RAM used: " + sizeInGb);

        ByteArray array = new ByteArray(size);

        //System.out.println("Size: " + array.SIZE);

        //print the lengths of each 1-D array
        System.out.print("[");
        for (int i = 0; i < array.ARRAY.length; i++) {
            System.out.print(array.ARRAY[i].length);
            if (i != array.ARRAY.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");

        //stores the sequence 0, 1, ..., 126, 127, 0, 1, ...
        for (long i = 0; i < size; i++) {
            array.set(i, (byte) (i % 128L));
        }

        //check each cell to ensure the above sequence is present
        for (long i = 0; i < size; i++) {
            if (array.get(i) != i % 128L) {
                System.out.println(
                        "i = " + i + ", " +
                                "value stored = " + array.get(i) + ", " +
                                "correct value = " + (byte) (i % 128)
                );
            }
            //Should the elements of the array be printed
            //array.print();// or use the next line for formatting
            //array.print(" ", Style.DECIMAL);
        }

        System.out.println("ByteArray completed.");
    }
}
