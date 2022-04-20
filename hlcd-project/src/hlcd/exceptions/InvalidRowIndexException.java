package hlcd.exceptions;

import hlcd.operations.Matrix;

/**
 * An exception that can be thrown when an invalid row index in some
 * matrix is trying to be accessed.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 28th, 2022)
 * @see Matrix
 * @see InvalidColIndexException
 * @since 1.8
 */

public class InvalidRowIndexException extends RuntimeException {

    /**
     * Constructs a new runtime exception when an invalid index of a row in a
     * matrix is trying to be accessed.
     *
     * @param invalidRowIndex     the invalid row matrix (zero-based)
     * @param maxRowValueOneBased the largest row value that is accessible
     *                            (one-based)
     */
    public InvalidRowIndexException(
            byte invalidRowIndex,
            byte maxRowValueOneBased
    ) {
        super("The row " + invalidRowIndex + " is an invalid row index. "
                      + "The minimum (zero-based) and maximum (zero-based) rows " +
                      "are 0 and " + (maxRowValueOneBased - 1) + " (both " +
                      "inclusive).");
    }
}
