package hlcd.testing.optimizationTester;

import hlcd.operations.Functions;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Creates a random {@code long} value based on the specified {@code n} and
 * {@code base} values in the constructor.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 18th, 2022)
 * @since 1.8
 */
public class RandomVectorGenerator {
    private long min;
    private long max;

    /**
     * Initializes the generator based on the length of the vector and the
     * base specified.
     *
     * @param n    the length of the vector
     * @param base the base of the vector
     */
    public RandomVectorGenerator(byte n, byte base) {
        byte power = (base == 2) ? n : (byte) (2 * n);
        min = 0;
        max = Functions.power(2, power);
    }

    /**
     * Returns a random vector based on {@code n} and {@code base} values
     * specified in the constructor.
     *
     * @return a random vector
     */
    public long getRandomVector() {
        return ThreadLocalRandom.current().nextLong(min, max);
    }
}
