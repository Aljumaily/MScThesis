package hlcd.testing.optimizationTester;

import hlcd.enums.Style;
import hlcd.exceptions.InvalidBaseException;
import hlcd.operations.Benchmarker;
import hlcd.operations.Functions;
import hlcd.operations.GF4Operations;
import hlcd.operations.MatrixPrinter;

/**
 * Test the multiplication logic of two rows vectors being multiplied by each
 * other. The multiplication is done element-wise. There are two approaches:
 * the optimal approach which uses binary manipulation <em>without</em> a
 * loop, and the naive approach which uses a loop. It will ensure both
 * approaches yield the same result. This optimization is the most important
 * optimization in this program.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 18th, 2022)
 * @since 1.8
 */
public class QuaternaryVectorMultiplicationTester {

    private byte base = 4;
    private byte n;
    private GF4Operations gf4Operations;

    /**
     * Must be true all the time to ensure both implementations yield the
     * same result.
     */
    private boolean validImplementation = true;

    /**
     * Initializes the test.
     *
     * @param n the length of the vectors to be tested.
     */
    public QuaternaryVectorMultiplicationTester(byte n) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        this.n = n;
        gf4Operations = new GF4Operations();
    }

    /**
     * The optimized multiplication engine used in this program to find the
     * multiplication of a row vector multiplied by another using
     * element-wise multiplication. It <em>doesn't</em> use a loop but a
     * series of binary manipulation to find the result.
     *
     * @param v1 the first vector to multiply
     * @param v2 the second vector to multiply
     * @return the multiplication of the two specified vectors
     */
    private long optimalQuaternaryMultiplicationEngine(long v1, long v2) {
        return gf4Operations.multiply(v1, v2);
    }

    /**
     * The slow/naive multiplication engine used to check the optimal
     * approach. It finds the multiplication of a row vector multiplied by
     * another row vector using element-wise multiplication. It <em>does</em>
     * use a loop to achieve the result.
     *
     * @param v1 the first vector to multiply
     * @param v2 the second vector to multiply
     * @return the multiplication of the two specified vectors
     */
    private long slowQuaternaryMultiplicationEngine(long v1, long v2) {
        long mask;
        byte shift = 2;

        long result = 0;
        for (byte i = 0; i < n; i++) {
            //get far-right quaternary digit of the two vectors:
            mask = 0b11;
            mask = mask << 2 * (n - i - 1);
            long v1DigitPadded = v1 & mask;//correct digit with zero padding
            long v2DigitPadded = v2 & mask;//correct digit with zero padding
            //shift the digits so that the digit needed is at far-right
            v1DigitPadded = v1DigitPadded >>> 2 * (n - i - 1);
            v2DigitPadded = v2DigitPadded >>> 2 * (n - i - 1);
            //The actual quaternary digit
            byte v1Digit = (byte) v1DigitPadded;
            byte v2Digit = (byte) v2DigitPadded;
            byte product = Functions.MUL_ARRAY[v1Digit][v2Digit];
            long resultDigit = (long) product << 2 * (n - i - 1);
            result = result | resultDigit;
        }
        return result;
    }

    /**
     * Generates 2x random vectors, where x is the iterations specified to
     * test using the optimal solution the program uses. Note that generating
     * a random {@code long} will take time because of randomness, not
     * necessarily the time of the approach.
     *
     * @param iterations the number of random of times to test the
     *                   multiplication of two random row vectors
     */
    public void benchmarkOptimalMultiplication(long iterations) {
        System.out.println("Running optimal multiplication solution using " +
                                   "random vectors...");
        Benchmarker mark = new Benchmarker();
        RandomVectorGenerator rvg = new RandomVectorGenerator(n, base);
        mark.start();
        mark.displayStartTime("Started: ");
        for (long i = 0; i < iterations; i++) {
            long v1 = rvg.getRandomVector();
            long v2 = rvg.getRandomVector();
            optimalQuaternaryMultiplicationEngine(v1, v2);
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        System.out.println();
    }

    /**
     * Tests the multiplication of the two specified vectors x times, where x
     * is the iterations specified. It uses the optimal approach. Since the
     * vector is specified, there is no randomness used, hence, the time will
     * be more accurate.
     *
     * @param iterations the number of random of times to test the
     *                   multiplication of two random row vectors
     * @param v1         the first vector to multiply
     * @param v2         the second vector to multiply
     */
    public void benchmarkOptimalMultiplication(
            long iterations,
            long v1,
            long v2
    ) {
        System.out.println("Running optimal multiplication solution using " +
                                   "the same two vectors...");
        Benchmarker mark = new Benchmarker();
        mark.start();
        mark.displayStartTime("Started: ");
        for (long i = 0; i < iterations; i++) {
            optimalQuaternaryMultiplicationEngine(v1, v2);
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        System.out.println();
    }

    /**
     * Generates 2x random vectors, where x is the iterations specified to
     * test using the naive/slow solution. Note that generating a random
     * {@code long} will take time because of randomness, not necessarily the
     * time of the approach.
     *
     * @param iterations the number of random of times to test the
     *                   multiplication of two random row vectors
     */
    public void benchmarkSlowMultiplication(long iterations) {
        System.out.println("Running slow multiplication solution using " +
                                   "random vectors...");
        Benchmarker mark = new Benchmarker();
        RandomVectorGenerator rvg = new RandomVectorGenerator(n, base);
        mark.start();
        mark.displayStartTime("Started: ");
        for (long i = 0; i < iterations; i++) {
            long v1 = rvg.getRandomVector();
            long v2 = rvg.getRandomVector();
            slowQuaternaryMultiplicationEngine(v1, v2);
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        System.out.println();
    }

    /**
     * Tests the multiplication of the two specified vectors x times, where x
     * is the iterations specified. It uses the slow/naive approach. Since the
     * vector is specified, there is no randomness used, hence, the time will
     * be more accurate.
     *
     * @param iterations the number of random of times to test the
     *                   multiplication of two random row vectors
     * @param v1         the first vector to multiply
     * @param v2         the second vector to multiply
     */
    public void benchmarkSlowMultiplication(long iterations, long v1, long v2) {
        System.out.println("Running slow multiplication solution using " +
                                   "the same two vectors...");
        Benchmarker mark = new Benchmarker();
        mark.start();
        mark.displayStartTime("Started: ");
        for (long i = 0; i < iterations; i++) {
            slowQuaternaryMultiplicationEngine(v1, v2);
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        System.out.println();
    }

    /**
     * Multiplies two randomly generated row vectors x time, where x is the
     * iterations specified to ensure both optimal and slow solutions yield
     * the same output. In the case where the result is not consist between
     * the two methods, then the two vectors and two unequal solutions will
     * be printed on screen. This is benchmarked but the amount of time
     * doesn't have any significance.
     *
     * @param iterations the number of times to perform the multiplication of
     *                   two randomly generated row vectors
     */
    public void initComparison(long iterations) {
        System.out.println("Running the comparison test...");
        Benchmarker mark = new Benchmarker();
        mark.start();
        mark.displayStartTime("Started: ");
        for (long i = 0; i < iterations; i++) {
            RandomVectorGenerator rvg = new RandomVectorGenerator(n, base);
            long v1 = rvg.getRandomVector();
            long v2 = rvg.getRandomVector();
            if (!areEqual(v1, v2)) {
                long a = slowQuaternaryMultiplicationEngine(v1, v2);
                long b = optimalQuaternaryMultiplicationEngine(v1, v2);
                String v1String = MatrixPrinter.vectorAsString(
                        v1, n, base, " ", Style.DECIMAL
                );
                String v2String = MatrixPrinter.vectorAsString(
                        v2, n, base, " ", Style.DECIMAL
                );
                String aString = MatrixPrinter.vectorAsString(
                        a, n, base, " ", Style.DECIMAL
                );
                String bString = MatrixPrinter.vectorAsString(
                        b, n, base, " ", Style.DECIMAL
                );
                System.out.println("v1: " + v1String);
                System.out.println("v2: " + v2String);
                System.out.println("Slow    engine: " + aString);
                System.out.println("Optimal engine: " + bString);
            }
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        System.out.println();
    }

    /**
     * Returns {@code true} if both methods yield the same multiplication
     * result of the two vectors specified, {@code false} otherwise.
     *
     * @param v1 the first vector to multiply
     * @param v2 the second vector to multiply
     * @return {@code true} if both methods return the same result of
     * multiplying the two specified vector, {@code false} otherwise
     */
    public boolean areEqual(long v1, long v2) {
        long a = slowQuaternaryMultiplicationEngine(v1, v2);
        long b = optimalQuaternaryMultiplicationEngine(v1, v2);
        if (a != b) {
            validImplementation = false;
        }
        return a == b;
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
//        System.out.println("Running QuaternaryVectorMultiplicationTester...");
//        byte n = 30;
//        byte base = 4;
//        long iterations = 1_000_000L;
//        RandomVectorGenerator rvg = new RandomVectorGenerator(n, base);
//        long v1 = rvg.getRandomVector();
//        long v2 = rvg.getRandomVector();
//        QuaternaryVectorMultiplicationTester qmt =
//                new QuaternaryVectorMultiplicationTester(n);
//        qmt.benchmarkSlowMultiplication(iterations);
//        qmt.benchmarkSlowMultiplication(iterations, v1, v2);
//        qmt.benchmarkOptimalMultiplication(iterations);
//        qmt.benchmarkOptimalMultiplication(iterations, v1, v2);
//        qmt.initComparison(iterations);
//        if (qmt.isValidImplementation()) {
//            System.out.println("The vector multiplication test was " +
//                                       "successful. Both implementations " +
//                                       "yielded the same results.");
//        } else {
//            System.out.println("The implementation is wrong.");
//        }
//        Benchmarker.beep();
//        System.out.println("QuaternaryVectorMultiplicationTester completed.");
//    }
}