package hlcd.exceptions;

/**
 * An exception that can be thrown when an invalid code parameters (\(n\)
 * and/or \(k\)) of a code \(\mathsf{C}\) is trying to be used. An invalid
 * combination could be when either \(k\) or \(n\) are non-positive integers or
 * when \(k\) is larger than \(n\).
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 28th, 2022)
 * @see hlcd.linearCode.Code
 * @since 1.8
 */
public class InvalidCodeParameters extends RuntimeException {

    /**
     * Constructs a new runtime exception with the specified code parameters
     * \(n\) and \(k\).
     *
     * @param n the length \(n\) of a code \(\mathsf{C}\)
     * @param k the dimension \(k\) of a code \(\mathsf{C}\)
     */
    public InvalidCodeParameters(byte n, byte k) {
        super("The k value must be between [1, n]. The passed k value is " +
                      k + " and the passed n value is " + n + ".");
    }
}