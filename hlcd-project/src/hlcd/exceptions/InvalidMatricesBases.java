package hlcd.exceptions;

import hlcd.operations.Matrix;

/**
 * An exception that can be thrown when the bases of two matrices don't match.
 * This is checked when \(G\) is multiplied by \(G^T\) or \(\overline{G}^T\). On
 * average, this exception should not be encountered.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 28th, 2022)
 * @see Matrix
 * @since 1.8
 */
public class InvalidMatricesBases extends RuntimeException {

    /**
     * Constructs a new runtime exception when an invalid digit is used in
     * the code.
     *
     * @param leftMatrixBase  the base of the left-hand-side matrix
     * @param rightMatrixBase the base of the right-hand-side matrix
     */
    public InvalidMatricesBases(byte leftMatrixBase, byte rightMatrixBase) {
        super("The base of matrix left-hand-side matrix is " + leftMatrixBase +
                      "which doesn't match the right-hand-side matrix base " +
                      "which is " + rightMatrixBase + ".");
    }
}
