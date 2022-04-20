package hlcd.exceptions;

import hlcd.operations.Matrix;

/**
 * An exception that can be thrown when trying to override a specific column
 * in the matrix with a column vector that doesn't match the matrix's dimension.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 28th, 2022)
 * @see Matrix
 * @see InvalidColumnVectorDimensionException
 * @since 1.8
 */
public class InvalidColumnDimensionToSetException extends RuntimeException {

    /**
     * Constructs a new runtime exception when a column matrix that doesn't
     * match the matrix's dimension is specified to override some column in
     * the matrix.
     *
     * @param matrixNumberOfRows the number of rows in the matrix
     * @param columnNumberOfRows the number of rows in the column matrix
     */
    public InvalidColumnDimensionToSetException(
            byte matrixNumberOfRows,
            byte columnNumberOfRows
    ) {
        super("The matrix contains " + matrixNumberOfRows + " rows. The column "
                + "that needs to be set in the matrix has " +
                columnNumberOfRows + " rows.");
    }
}