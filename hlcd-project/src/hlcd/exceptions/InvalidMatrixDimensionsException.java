package hlcd.exceptions;

import hlcd.operations.Matrix;

/**
 * An exception that can be thrown when the dimensions of two matrices don't
 * match for multiplication. This is used when \(G\) is multiplied by
 * \(G^T\) or \(\overline{G}^T\). On average, this exception should not be
 * encountered.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 28th, 2022)
 * @see Matrix
 * @since 1.8
 */
public class InvalidMatrixDimensionsException extends RuntimeException {

    /**
     * Constructs a new runtime exception when two matrices whose dimensions
     * don't match for multiplication.
     *
     * @param m1k the number of rows of the left matrix
     * @param m1n the number of columns of the left matrix
     * @param m2k the number of rows of the right matrix
     * @param m2n the number of columns of the right matrix
     */
    public InvalidMatrixDimensionsException(
            byte m1k,
            byte m1n,
            byte m2k,
            byte m2n
    ) {
        super("The dimension (rows * cols) of left matrix = (" + m1k + "*" +
                      m1n + ") does not match the dimension of the right matrix " +
                      "which is " + "(" + m2k + " * " + m2n + ").");
    }
}
