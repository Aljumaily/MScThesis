package hlcd.exceptions;

/**
 * An exception that can be thrown when an invalid base is trying to be used.
 * Currently, a <em>valid</em> base is either \(2\) or \(4\). The program is
 * designed to allow the inclusion of other bases.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 28th, 2022)
 * @since 1.8
 */
public class InvalidBaseException extends RuntimeException {

    /**
     * Constructs a new runtime exception with the specified invalid base.
     *
     * @param invalidBase the invalid base
     */
    public InvalidBaseException(byte invalidBase) {
        super("The base " + invalidBase + " is an invalid base, please enter " +
                "2 or 4.");
    }
}
