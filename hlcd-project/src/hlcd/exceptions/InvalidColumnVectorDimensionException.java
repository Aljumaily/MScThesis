package hlcd.exceptions;

import hlcd.operations.Matrix;

/**
 * An exception that can be thrown when the number of rows in a column vector
 * exceeds the allowable limit. When the base is \(2\), then the limit cannot
 * exceed \(62\) and when the is base \(4\), the limit is \(30\).
 * <p>
 * It is different from {@code InvalidColumnDimensionToSetException} because
 * this deals with the dimension of any column vector, <em>not</em> when
 * setting a column vector in a matrix.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 28th, 2022)
 * @see Matrix
 * @see InvalidColumnDimensionToSetException
 * @since 1.8
 */
public class InvalidColumnVectorDimensionException extends RuntimeException {

    /**
     * Constructs a new runtime exception when a column matrix contain more
     * rows than the allowable limit.
     *
     * @param numberOfRows the number of rows in the column vector
     * @param base         the base of the code
     */
    public InvalidColumnVectorDimensionException(byte numberOfRows, byte base) {
        super("The matrix contains " + numberOfRows + " rows which exceeds " +
                "the allowable limit because the base is " + base + ". The " +
                "limit is 128 / base.");
    }
}
