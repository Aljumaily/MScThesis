package hlcd.exceptions;

/**
 * An exception that can be thrown when an invalid row index is specified in the
 * identity submatrix of the generator matrix \(G\).
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 28th, 2022)
 * @see hlcd.operations.VectorGenerator
 * @since 1.8
 */
public class InvalidIdentityRowIndexException extends RuntimeException {

    /**
     * Constructs a new runtime exception when an invalid row index is
     * specified in the identity submatrix of the generator matrix \(G\).
     *
     * @param identityRowIndex the invalid row index of the identity matrix
     * @param k                the dimension of the code
     * @param base             the base of the code
     */
    public InvalidIdentityRowIndexException(
            byte identityRowIndex,
            byte k,
            byte base
    ) {
        super("The column " + identityRowIndex + " is an invalid column to " +
                      "insert the value 1 in. The minimum (zero-based) and " +
                      "maximum (zero-based) columns are 0 and " + (k - 1) +
                      " (both inclusive) since the base is " + base + ".");
    }

}
