package hlcd.testing.optimizationTester;

import hlcd.enums.Style;
import hlcd.exceptions.InvalidBaseException;
import hlcd.operations.Benchmarker;
import hlcd.operations.Functions;
import hlcd.operations.HammingWeight;
import hlcd.operations.MatrixPrinter;

/**
 * Ensures the optimal Hamming weight approach used in this program matched
 * the expected result. The optimal approach doesn't use a loop, only binary
 * manipulation. The test is comparing what the optimal approach yield to the
 * naive approach that uses a loop.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 17th, 2022)
 * @since 1.8
 */
public class HammingWeightTester {

    private byte n;
    private byte base;
    private HammingWeight hw;

    /**
     * Must be true all the time to ensure both implementations yield the
     * same result.
     */
    private boolean validImplementation = true;

    /**
     * Initializes the test of vectors based on the length and base specified.
     *
     * @param n    the length of each vector to test
     * @param base the base of the vectors
     */
    public HammingWeightTester(byte n, byte base) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        this.n = n;
        this.base = base;
        hw = new HammingWeight(base);
    }

    /**
     * The optimized Hamming weight engine used in this program. It
     * <em>doesn't</em> use a loop but a series of binary manipulation to
     * find the Hamming weight of the specified vector.
     *
     * @param vector the vector the find the weight of
     * @return the Hamming weight of the vector specified
     */
    private byte optimalWeightEngine(long vector) {
        return hw.getWeight(vector);
    }

    /**
     * The naive Hamming weight engine that uses a loop to calculate the
     * weight of the specified vector.
     *
     * @param v the vector the find the weight of
     * @return the Hamming weight of the vector specified
     */
    private byte slowWeightEngine(long v) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }

        /* The mask will get the far-right digit which could either be 0b1
        for a binary vector or 0b11 for a quaternary vector.*/
        byte mask = (base == 2) ? (byte) 0b1 : (byte) 0b11;
        byte shift = (base == 2) ? (byte) 1 : (byte) 2;
        byte result = 0;
        while (v != 0) {
            byte farRightDigit = (byte) (v & mask);
            if (farRightDigit != 0) {
                result++;
            }
            v = v >>> shift;
        }
        return result;
    }

    /**
     * Generates x random vectors, where x is the iterations specified and
     * find their Hamming weight using the optimal solution the program uses.
     * Note that generating a random long will take time.
     *
     * @param iterations the number of random vectors to generate and find
     *                   the Hamming weight of
     * @see HammingWeightTester#benchmarkOptimalSolution(long, long)
     */
    public void benchmarkOptimalSolution(long iterations) {
        System.out.println("Running optimal solution using random vectors...");
        Benchmarker mark = new Benchmarker();
        RandomVectorGenerator rvg = new RandomVectorGenerator(n, base);
        mark.start();
        mark.displayStartTime("Started: ");
        for (long i = 0; i < iterations; i++) {
            optimalWeightEngine(rvg.getRandomVector());
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        System.out.println();
    }

    /**
     * Finds the Hamming weight of the vector specified x times, where x is
     * the iterations specified. It uses the optimal solution the program uses.
     * Since the vector is specified, there is no randomness used, hence, the
     * time will be more accurate.
     *
     * @param iterations the number of random vectors to generate and find
     *                   the Hamming weight of
     * @param v          the vector to find the Hamming weight of
     * @see HammingWeightTester#benchmarkOptimalSolution(long)
     */
    public void benchmarkOptimalSolution(long iterations, long v) {
        System.out.println("Running optimal solution using a single vector...");
        Benchmarker mark = new Benchmarker();
        mark.start();
        mark.displayStartTime("Started: ");
        for (long i = 0; i < iterations; i++) {
            optimalWeightEngine(v);
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        System.out.println();
    }

    /**
     * Generates x random vectors, where x is the iterations specified and
     * find their Hamming weight using the slow solution that uses a loop.
     * Note that generating a random long will take time.
     *
     * @param iterations the number of random vectors to generate and find
     *                   the Hamming weight of
     * @see HammingWeightTester#benchmarkSlowSolution(long, long)
     */
    public void benchmarkSlowSolution(long iterations) {
        System.out.println("Running slow solution using random vectors...");
        Benchmarker mark = new Benchmarker();
        RandomVectorGenerator rvg = new RandomVectorGenerator(n, base);
        mark.start();
        mark.displayStartTime("Started: ");
        for (long i = 0; i < iterations; i++) {
            slowWeightEngine(rvg.getRandomVector());
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        System.out.println();
    }

    /**
     * Finds the Hamming weight of the vector specified x times, where x is
     * the iterations specified. It uses the slow solution that uses a loop.
     * Since the vector is specified, there is no randomness used, hence, the
     * time will be more accurate.
     *
     * @param iterations the number of random vectors to generate and find
     *                   the Hamming weight of
     * @param v          the vector to find the Hamming weight of
     * @see HammingWeightTester#benchmarkSlowSolution(long)
     */
    public void benchmarkSlowSolution(long iterations, long v) {
        System.out.println("Running slow solution using a single vector...");
        Benchmarker mark = new Benchmarker();
        mark.start();
        mark.displayStartTime("Started: ");
        for (long i = 0; i < iterations; i++) {
            slowWeightEngine(v);
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        System.out.println();
    }

    /**
     * Generates x random vectors, where x is the iterations specified to
     * ensure both optimal and slow solutions yield the same output. In the
     * case where the weight is not consist between the two methods, then the
     * vector and the weights will be printed on screen. This is benchmarked
     * but the amount of time doesn't have any significance.
     *
     * @param iterations the number of random vectors to generate and find
     *                   the Hamming weight of
     * @see HammingWeightTester#benchmarkSlowSolution(long, long)
     */
    public void initComparison(long iterations) {
        System.out.println("Running the comparison test...");
        Benchmarker mark = new Benchmarker();
        RandomVectorGenerator rvg = new RandomVectorGenerator(n, base);
        mark.start();
        mark.displayStartTime("Started: ");
        for (long i = 0; i < iterations; i++) {
            long v = rvg.getRandomVector();
            if (!areEqual(v)) {
                MatrixPrinter.vectorAsString(v, n, base, " ", Style.DECIMAL);
                System.out.println("Slow    engine: " + slowWeightEngine(v));
                System.out.println("Optimal engine: " + optimalWeightEngine(v));
            }
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        System.out.println();
    }

    /**
     * Returns {@code true} if both methods return the same Hamming weight of
     * the specified vector, {@code false} otherwise.
     *
     * @param v the vector to find the Hamming weight of
     * @return {@code true} if both methods return the same Hamming weight of
     * the specified vector, {@code false} otherwise
     */
    public boolean areEqual(long v) {
        byte result1 = slowWeightEngine(v);
        byte result2 = optimalWeightEngine(v);
        if (result1 != result2) {
            validImplementation = false;
        }
        return result1 == result2;
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
//        System.out.println("Running HammingWeightTester...");
//        byte n = 30;
//        byte base = 4;
//        long iterations = 10_000_000L;
//        long v = new RandomVectorGenerator(n, base).getRandomVector();
//        HammingWeightTester wet = new HammingWeightTester(n, base);
//        wet.benchmarkSlowSolution(iterations);
//        wet.benchmarkSlowSolution(iterations, v);
//        wet.benchmarkOptimalSolution(iterations);
//        wet.benchmarkOptimalSolution(iterations, v);
//        wet.initComparison(iterations);
//        if (wet.isValidImplementation()) {
//            System.out.println("The Hamming weight test was successful. Both " +
//                    "implementations yielded the same results.");
//        } else {
//            System.out.println("The implementation is wrong.");
//        }
//        Benchmarker.beep();
//        System.out.println("HammingWeightTester Completed.");
//    }
}
