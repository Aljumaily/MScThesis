package hlcd.exceptions;

/**
 * An exception that can be thrown when an invalid digit is being used in the
 * code. For example, using \(\omega_{4}\) or \(3_{10}\) in base \(2\).
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 28th, 2022)
 * @see hlcd.linearCode
 * @since 1.8
 */
public class InvalidDigitException extends RuntimeException {

    /**
     * Constructs a new runtime exception when an invalid digit is used in
     * the code.
     *
     * @param invalidDigit the invalid digit
     * @param base         the base of the code
     */
    public InvalidDigitException(byte invalidDigit, byte base) {
        super("The digit " + invalidDigit + " is an invalid digit in base "
                + base + ".");
    }
}
