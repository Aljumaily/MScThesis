package hlcd.testing.arrayTester;

import java.util.Arrays;

/**
 * Uses a 1-D array of {@code int}s to mimic the splitting of segments. The
 * length of the array represents the number of segments required. The value
 * stored in each cell represents the length the segment.
 *
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running SegmentSplitter...");
 *     //number of complete 1D arrays
 *     long fullSegments = (long) (Math.random() * 100000);
 *     //extra is a value between 0 and maxSegmentLength, including both
 *     long extra = (long) (Math.random() * (MAX_SEGMENT_LENGTH + 1));
 *
 *     long size = fullSegments * MAX_SEGMENT_LENGTH + extra;
 *
 *     double sizeInGb = (64 * size) / 8000000000.0;
 *     //System.out.println("Total GBs of RAM used: " + sizeInGb);
 *
 *     SegmentSplitter data = new SegmentSplitter(size);
 *     System.out.println("The size of each segment in the 2-D array: ");
 *     System.out.println(data);
 *
 *     //count the number of cells allocated
 *     long cells = 0;
 *     for (int i = 0; i &#60; data.array.length; i++) {
 *         cells += data.array[i];
 *     }
 *     //ensure the number of allocated cells matches the size specified
 *     if (size != cells) {
 *         System.out.println("Failed.");
 *     }else{
 *         System.out.println("Success!");
 *     }
 *     System.out.println("SegmentSplitter completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0
 * @since April 13th, 2022
 */
public class SegmentSplitter {
    /**
     * The structure used to store the data.
     */
    private int[] array;

    /**
     * The amount of cells that are excluded from an array because VMs reserve
     * some header words in an array which reduces the <em>true</em> maximum
     * value. It could be at least 8 but 16 is used for extra precaution.
     */
    private final static byte JVM_OVERHEAD = 16;

    /**
     * The number of cells in the array.
     */
    private long size;

    /**
     * The number of 1-D arrays required.
     */
    private int segments;

    /**
     * The maximum size of each 1-D array.
     */
    public static int MAX_SEGMENT_LENGTH = Integer.MAX_VALUE - JVM_OVERHEAD;

    /**
     * The only constructor which uses the specified size to calculates the
     * number of segments required as well as the length of each segment.
     *
     * @param size the number of cells required to be stored
     */
    public SegmentSplitter(long size) {
        if (size <= 0) {
            throw new RuntimeException("Size cannot be less than 1");
        }

        this.size = size;

        //number of 1-D arrays with full capacity
        segments = (int) (this.size / (MAX_SEGMENT_LENGTH));//can be 0 or larger

        //Last 1-D array is not full but needed (assuming the condition is true)
        if (this.size % MAX_SEGMENT_LENGTH != 0) {
            segments++;
        }

        array = new int[segments];

        //initializing the 1-D arrays to the appropriate lengths. It will
        //keep on choosing MAX_SEGMENT_LENGTH as long as sizeLeft is larger. It
        //will eventually store the remaining size left into the last 1-D array.
        long sizeLeft = this.size;
        for (int i = 0; i < array.length; i++) {
            int length = (int) Math.min(MAX_SEGMENT_LENGTH, sizeLeft);
            array[i] = length;
            sizeLeft -= length;
        }
        System.out.println();
        System.out.println("Total Arrays needed: " + segments);
        System.out.println("Size of last array: " + array[array.length - 1]);
    }

    public String toString() {
        return Arrays.toString(array);
    }

    /**
     * Executes the program.
     *
     * @param args the arguments specified but will be ignored
     */
    public static void main(String[] args) {
        System.out.println("Running SegmentSplitter...");
        //number of complete 1D arrays
        long fullSegments = (long) (Math.random() * 100000);
        //extra is a value between 0 and maxSegmentLength, including both
        long extra = (long) (Math.random() * (MAX_SEGMENT_LENGTH + 1));

        long size = fullSegments * MAX_SEGMENT_LENGTH + extra;

        double sizeInGb = (64 * size) / 8000000000.0;
        System.out.println("Total GBs of RAM used: " + sizeInGb);

        SegmentSplitter data = new SegmentSplitter(size);
        System.out.println("The size of each segment in the 2-D array: ");
        System.out.println(data);

        //count the number of cells allocated
        long cells = 0;
        for (int i = 0; i < data.array.length; i++) {
            cells += data.array[i];
        }
        //ensure the number of allocated cells matches the size specified
        if (size != cells) {
            System.out.println("Failed.");
        } else {
            System.out.println("Success!");
        }
        System.out.println("SegmentSplitter completed.");
    }
}
