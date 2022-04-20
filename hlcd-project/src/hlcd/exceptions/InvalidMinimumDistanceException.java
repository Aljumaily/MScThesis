package hlcd.exceptions;

import hlcd.operations.Matrix;

/**
 * An exception that can be thrown when the minimum distance specified is
 * less than \(3\). Note that this program doesn't support codes of minimum
 * distance less than \(3\).
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 28th, 2022)
 * @see Matrix
 * @see InvalidColIndexException
 * @see hlcd.operations.Functions#isValidMinimumDistance(byte)
 * @since 1.8
 */
public class InvalidMinimumDistanceException extends RuntimeException {

    /**
     * Constructs a new runtime exception when an invalid minimum distance is
     * specified for a code.
     *
     * @param invalidMinimumDistance the invalid minimum distance
     * @see hlcd.operations.Functions#isValidMinimumDistance(byte)
     */
    public InvalidMinimumDistanceException(byte invalidMinimumDistance) {
        super("The minimum distance " + invalidMinimumDistance + " is an " +
                "invalid. Please enter a minimum distance ≥ 3."
        );
    }
}
