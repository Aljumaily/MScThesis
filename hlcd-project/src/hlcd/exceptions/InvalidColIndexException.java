package hlcd.exceptions;

import hlcd.operations.Matrix;

/**
 * An exception that can be thrown when an invalid column index in some
 * matrix is trying to be accessed.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 28th, 2022)
 * @see Matrix
 * @see InvalidRowIndexException
 * @since 1.8
 */
public class InvalidColIndexException extends RuntimeException {

    /**
     * Constructs a new runtime exception when an invalid index of a column
     * in a matrix is trying to be accessed.
     *
     * @param invalidColIndex     the invalid column matrix (zero-based)
     * @param maxColValueOneBased the largest column value that is accessible
     *                            (one-based)
     * @param base                the base of the code (which could \(2\) or
     *                            \(4\))
     */
    public InvalidColIndexException(
            byte invalidColIndex,
            byte maxColValueOneBased,
            byte base
    ) {
        super("The column " + invalidColIndex + " is an invalid column index. "
                + "The minimum (zero-based) and maximum (zero-based) rows " +
                "are 0 and " + (maxColValueOneBased - 1) + " (both inclusive)" +
                " since the base is " + base + ".");
    }
}
