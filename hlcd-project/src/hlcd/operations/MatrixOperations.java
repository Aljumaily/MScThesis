package hlcd.operations;

import hlcd.enums.Style;

/**
 * Defines the operations of a matrix. A more in-depth explanation of how
 * matrices are stored in this program can be found in the {@link Matrix} class
 * that implements this interface.
 * <p>
 * TODO: create a method to transform the generator matrix to the  parity
 *  check matrix.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 7th, 2022)
 * @see Long
 * @see Matrix
 * @since 1.8
 */

public interface MatrixOperations {

    /**
     * Returns a new matrix that is the transpose of the current matrix. The
     * current matrix will not be altered.
     *
     * @return a new instance of the matrix transposed
     * @see MatrixOperations#hermitianTranspose()
     */
    public Matrix transpose();

    /**
     * Will return a new matrix that is the transpose of the matrix specified.
     * The matrix specified will not be altered.
     *
     * @param m the matrix to be transposed
     * @return a new instance of the matrix transposed
     * @see MatrixOperations#hermitianTranspose(Matrix)
     */
    public Matrix transpose(Matrix m);

    /**
     * Returns a new matrix that is the transpose of specified matrix
     * array. The matrix array specified will not be altered.
     *
     * @param matrixArray the matrix array to be transposed
     * @param n           the number of columns in the matrix array
     *                    which is the same as the length of code
     *                    \(\mathsf{C}\)
     * @param k           the number of rows in the matrix array which is the
     *                    same as the dimension of code \(\mathsf{C}\)
     * @param base        the base of the code (could be either \(2\) or \(4\))
     * @return a new instance of the matrix array transposed
     * @see MatrixOperations#hermitianTranspose(long[], byte, byte, byte)
     */
    public long[] transpose(long[] matrixArray, byte n, byte k, byte base);

    /**
     * Returns a new matrix that is the Hermitian transpose of the current
     * matrix. The current matrix will not be altered. Essentially, it will
     * apply the transpose of the current matrix and swap cells containing
     * \(\omega\) with \(\overline{\omega}\) and vice-versa.
     *
     * @return a new instance of the Hermitian transpose of the current matrix
     * @see MatrixOperations#transpose()
     */
    public Matrix hermitianTranspose();

    /**
     * Returns a new matrix that is the Hermitian transpose of the
     * specified matrix. The matrix array will not be altered. Essentially,
     * it will apply the transpose of the matrix and swap cells containing
     * \(\omega\) with \(\overline{\omega}\) and vice-versa.
     *
     * @param m the matrix to be Hermitian transposed
     * @return a new instance of the Hermitian transpose of specified matrix
     * @see MatrixOperations#transpose(Matrix)
     */
    public Matrix hermitianTranspose(Matrix m);

    /**
     * Returns a new matrix that is the Hermitian transpose of the matrix
     * array specified. The matrix array will not be altered. Essentially, it
     * will apply the transpose of the matrix array and swap cells containing
     * \(\omega\) with \(\overline{\omega}\) and vice-versa.
     *
     * @param matrixArray the matrix array to be Hermitian transposed
     * @param n           the number of columns in the matrix array
     * @param k           the number of rows in the matrix array
     * @param base        the base of the code (could be either \(2\) or \(4\))
     * @return a new instance of the Hermitian transpose of specified matrix
     * array
     * @see MatrixOperations#transpose(long[], byte, byte, byte)
     */
    public long[] hermitianTranspose(
            long[] matrixArray, byte n, byte k, byte base
    );

    /**
     * Returns the submatrix of \(G^{\prime}_{sub} = G^{\kern0pt}_{x \times n}
     * \overline{G}^{T}_{n \times x}\), where \(x\) is the largest row index in
     * \(G\) that is nonzero. Since the generator matrix might contain zero
     * vectors as it is defined like this by default, only nonzero vectors
     * should be included in order to find \(G^{\prime}\). The single parameter
     * denotes the number of valid codewords in \(G\). We can use this to
     * create a new matrix \(G_{sub}\) with fewer rows, find its transpose
     * \(\overline{G}^{T}_{sub}\), then multiply them together to get the
     * submatrix \(G^{\prime}_{sub}\).
     *
     * @param lastPopulatedRowIndex the index of the last valid row in the
     *                              generator matrix (zero-based). The index
     *                              specified is also included.
     * @return the submatrix of \(G^{\prime}_{sub} = G^{\kern0pt}_{x \times n}
     * \overline{G}^{T}_{n \times x}\)
     * @see MatrixOperations#getGPrime()
     */
    public Matrix getGPrime(byte lastPopulatedRowIndex);

    /**
     * Returns the matrix \(G^{\prime}_{k \times k} = G^{\kern0pt}_{k \times n}
     * \overline{G}^{T}_{n \times k}\) without altering the current matrix.
     *
     * @return the matrix \(G^{\prime}_{k \times k} = G^{\kern0pt}_{k \times n}
     * \overline{G}^{T}_{n \times k}\)
     * @see MatrixOperations#getGPrime(byte)
     */
    public Matrix getGPrime();

    /**
     * Returns the transpose of the row vector at the index in the current
     * matrix. The current matrix will not be altered. For example, say that
     * the current code is \(\left[n, \, k\right]_{base} = \left[5, \,
     * 1\right]_{4}\). Assume the row specified is
     * \begin{equation}
     * \begin{bmatrix}
     * 01 &amp; 11 &amp; 10 &amp; 01 &amp; 00
     * \end{bmatrix},
     * \end{equation}
     * the transpose will be
     * \begin{equation}
     * \begin{bmatrix}
     * 01 \\ 11 \\ 10 \\ 01 \\ 00
     * \end{bmatrix}.
     * \end{equation}
     * In this example, the row vector is stored as the following in Java:
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	01	&amp;	11	&amp;	10	&amp;
     * 01	&amp;	00
     * \end{bmatrix},
     * \end{equation}
     * and the output, which is a {@code byte} array will be
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	11\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	10\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	00\\
     * \end{bmatrix}.
     * \end{equation}
     *
     * @param index row index of the row in current matrix to be transposed
     *              (zero-based)
     * @return a column vector of size \(n \times 1\)
     */
    public byte[] transposeRowToCol(byte index);

    /**
     * Returns the transpose of the row vector specified. For example, say that
     * the current code is \(\left[n, \, k\right]_{base} = \left[5, \,
     * 1\right]_{4}\). Assume the row specified is
     * \begin{equation}
     * \begin{bmatrix}
     * 01 &amp; 11 &amp; 10 &amp; 01 &amp; 00
     * \end{bmatrix},
     * \end{equation}
     * the transpose will be
     * \begin{equation}
     * \begin{bmatrix}
     * 01 \\ 11 \\ 10 \\ 01 \\ 00
     * \end{bmatrix}.
     * \end{equation}
     * In this example, the row vector is stored as the following in Java:
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	01	&amp;	11	&amp;	10	&amp;
     * 01	&amp;	00
     * \end{bmatrix},
     * \end{equation}
     * and the output, which is a {@code byte} array will be
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	11\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	10\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	00\\
     * \end{bmatrix}.
     * \end{equation}
     *
     * @param rowVector the row vector to be transposed
     * @param x         the number of rows in the result
     * @param base      the base of the code (could be either \(2\) or \(4\))
     * @return a column vector of size \(x \times 1\)
     */
    public byte[] transposeRowToCol(long rowVector, byte x, byte base);

    /**
     * Returns the transpose of a specific row in the matrix specified
     * matrix. The matrix specified will not be altered. For example, say that
     * the current code is \(\left[n, \, k\right]_{base} = \left[5, \,
     * 1\right]_{4}\). Assume the row specified is
     * \begin{equation}
     * \begin{bmatrix}
     * 01 &amp; 11 &amp; 10 &amp; 01 &amp; 00
     * \end{bmatrix},
     * \end{equation}
     * the transpose will be
     * \begin{equation}
     * \begin{bmatrix}
     * 01 \\ 11 \\ 10 \\ 01 \\ 00
     * \end{bmatrix}.
     * \end{equation}
     * In this example, the row vector is stored as the following in Java:
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	01	&amp;	11	&amp;	10	&amp;
     * 01	&amp;	00
     * \end{bmatrix},
     * \end{equation}
     * and the output, which is a {@code byte} array will be
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	11\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	10\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	00\\
     * \end{bmatrix}.
     * \end{equation}
     *
     * @param m     the matrix to obtain the row vector from
     * @param index row index of the row in matrix specified to be transposed
     *              (zero-based)
     * @return a column vector of size \(n \times 1\)
     */
    public byte[] transposeRowToCol(Matrix m, byte index);

    /**
     * Returns the transpose of the column vector at the index in the current
     * matrix. The current matrix will not be altered. For example, say that
     * the current code is \(\left[n, \, k\right]_{base} = \left[1, \,
     * 5\right]_{4}\). Assume the column specified is
     * \begin{equation}
     * \begin{bmatrix}
     * 01 \\ 11 \\ 10 \\ 01 \\ 00
     * \end{bmatrix},
     * \end{equation}
     * the transpose will be
     * \begin{equation}
     * \begin{bmatrix}
     * 01 &amp; 11 &amp; 10 &amp; 01 &amp; 00
     * \end{bmatrix}.
     * \end{equation}
     * In this example, the column vector is stored as the following in Java:
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	11\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	10\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	00\\
     * \end{bmatrix},
     * \end{equation}
     * and the output stored as
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	01	&amp;	11	&amp;	10	&amp;
     * 01	&amp;	00
     * \end{bmatrix}.
     * \end{equation}
     *
     * @param index column index of the column in current matrix to be
     *              transposed (zero-based)
     * @return a row vector of size \(1 \times k\) with non-used entries as zeros
     */
    public long transposeColToRow(byte index);

    /**
     * Returns the transpose of the column vector specified. For example, say
     * that the current code is \(\left[n, \, k\right]_{base} = \left[1, \,
     * 5\right]_{4}\). Assume the column specified is
     * \begin{equation}
     * \begin{bmatrix}
     * 01 \\ 11 \\ 10 \\ 01 \\ 00
     * \end{bmatrix},
     * \end{equation}
     * the transpose will be
     * \begin{equation}
     * \begin{bmatrix}
     * 01 &amp; 11 &amp; 10 &amp; 01 &amp; 00
     * \end{bmatrix}.
     * \end{equation}
     * In this example, the column vector is stored as the following in Java:
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	11\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	10\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	00\\
     * \end{bmatrix},
     * \end{equation}
     * and the output stored as
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	01	&amp;	11	&amp;	10	&amp;
     * 01	&amp;	00
     * \end{bmatrix}.
     * \end{equation}
     *
     * @param columnVector the column vector to be transposed
     * @param base         the base of the code (could be either \(2\) or \(4\))
     * @return a row vector of size \(1 \times k\) with non-used entries as zeros
     */
    public long transposeColToRow(byte[] columnVector, byte base);

    /**
     * Returns the transpose of a specific column in the matrix specified
     * matrix. The matrix specified will not be altered. For example, say
     * that the current code is \(\left[n, \, k\right]_{base} = \left[1, \,
     * 5\right]_{4}\). Assume the column specified is
     * \begin{equation}
     * \begin{bmatrix}
     * 01 \\ 11 \\ 10 \\ 01 \\ 00
     * \end{bmatrix},
     * \end{equation}
     * the transpose will be
     * \begin{equation}
     * \begin{bmatrix}
     * 01 &amp; 11 &amp; 10 &amp; 01 &amp; 00
     * \end{bmatrix}.
     * \end{equation}
     * In this example, the column vector is stored as the following in Java:
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	11\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	10\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	01\\
     * 0b	&amp;	00	&amp;	00	&amp; 00	&amp;	00\\
     * \end{bmatrix},
     * \end{equation}
     * and the output stored as
     * \begin{equation}
     * \begin{bmatrix}
     * 0b	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;	00	&amp;
     * 00	&amp;	00	&amp;	00	&amp;	01	&amp;	11	&amp;	10	&amp;
     * 01	&amp;	00
     * \end{bmatrix}.
     * \end{equation}
     *
     * @param m     the matrix to obtain the column vector from
     * @param index column index of the column in matrix specified to be
     *              transposed (zero-based)
     * @return a row vector of size \(1 \times k\) with non-used entries as zeros
     */
    public long transposeColToRow(Matrix m, byte index);

    /**
     * Multiplies a row from current matrix by a column from specified matrix
     * which yields a single digit. The digit can either be a \(0\) or \(1\) in
     * base \(2\) or \(0\), \(1\), \(2\) or \(3\) in base \(4\). The row will be
     * extracted from the current matrix whereas the column will be extracted
     * from the matrix specified.
     *
     * @param rightMatrix The matrix to extract the column from
     * @param rowIndex    the row index (zero-based) from current matrix to use
     *                    for multiplication
     * @param columnIndex the column index (zero-based) from matrix specified to
     *                    use for multiplication
     * @return the result when the row from current matrix is multiplied by
     * the column from the matrix specified
     */
    public byte multiplyRowByCol(
            Matrix rightMatrix,
            byte rowIndex,
            byte columnIndex
    );

    /**
     * Multiplies a row from matrix specified by a column from the other
     * specified matrix which yields a single digit. The digit can either be
     * a \(0\) or \(1\) in base \(2\) or \(0\), \(1\), \(2\) or \(3\) in base
     * \(4\). The row will be extracted from the left matrix specified whereas
     * the column will be extracted from the right matrix specified.
     *
     * @param leftMatrix  The matrix to extract the row from
     * @param rightMatrix The matrix to extract the column from
     * @param rowIndex    the row index (zero-based) from {@code leftMatrix} to
     *                    use for multiplication
     * @param columnIndex the column index (zero-based) from {@code rightMatrix}
     *                    to use for multiplication
     * @return the result when the row from {@code leftMatrix} is multiplied by
     * the column from {@code rightMatrix}
     */
    public byte multiplyRowByCol(
            Matrix leftMatrix,
            Matrix rightMatrix,
            byte rowIndex,
            byte columnIndex
    );

    /**
     * Multiplies a row from matrix array specified by a column from the other
     * specified matrix array which yields a single digit. The digit can
     * either be a \(0\) or \(1\) in base \(2\) or \(0\), \(1\), \(2\) or \(3\)
     * in base \(4\). The row will be extracted from the left matrix array
     * specified whereas the column will be extracted from the right matrix
     * specified.
     *
     * @param leftMatrixArray      The matrix to extract the row from
     * @param rightMatrixArray     The matrix to extract the column from
     * @param leftMatrixArrayN     the number of columns in
     *                             {@code leftMatrixArray}
     * @param rightMatrixArrayN    the number of columns in
     *                             {@code rightMatrixArray}
     * @param leftMatrixArrayK     the number of rows in
     *                             {@code leftMatrixArray}
     * @param rightMatrixArrayK    the number of rows in
     *                             {@code rightMatrixArray}
     * @param leftMatrixArrayBase  the base of {@code leftMatrixArray} (could
     *                             be either \(2\) or \(4\))
     * @param rightMatrixArrayBase the base of {@code rightMatrixArray} (could
     *                             be either \(2\) or \(4\))
     * @param rowIndex             the row index (zero-based) from {@code
     *                             leftMatrixArray} to use for multiplication
     * @param columnIndex          the column index (zero-based) from {@code
     *                             rightMatrixArray} to use for multiplication
     * @return the result when the row from {@code leftMatrixArray} is
     * multiplied by the column from {@code rightMatrixArray}
     */
    public byte multiplyRowByCol(
            long[] leftMatrixArray,
            long[] rightMatrixArray,
            byte leftMatrixArrayN,
            byte rightMatrixArrayN,
            byte leftMatrixArrayK,
            byte rightMatrixArrayK,
            byte leftMatrixArrayBase,
            byte rightMatrixArrayBase,
            byte rowIndex,
            byte columnIndex
    );

    /**
     * Uses Bareiss Algorithm to find the determinant of \(G^{\prime}\), which
     * is \(G^{\kern0pt}_{k \times n} \overline{G}^{T}_{n \times k}\). It uses
     * the current matrix stored and multiplies it by its complex conjugation
     * and finds the determinant of that resulting matrix after multiplication.
     * This will not alter any matrices in the program and should be used for
     * quaternary codes.
     *
     * @return the determinant of \(G^{\prime} = G^{\kern0pt}_{k \times n}
     * \overline{G}^{T}_{n \times k}\), which could either be \(0\), \(1\),
     * \(2\) or \(3\) in base \(4\)
     */
    public byte getDeterminant();

    /**
     * Uses Bareiss Algorithm to find the determinant of the matrix specified.
     * This will not alter the matrix specified and should be used for
     * quaternary codes.
     *
     * @param m the matrix to find the determinant of
     * @return the determinant of the matrix specified
     */
    public byte getDeterminant(Matrix m);

    /**
     * Multiplies two matrices and returns the result as a new matrix. The
     * current matrix will be the left-hand-side matrix and the matrix
     * specified will be the right-hand-side matrix. Both matrices will not
     * be altered.
     * <p>
     * Note: It is common to have the left-hand-side matrix to be the generator
     * matrix \(G\) of the code \(\mathsf{C}\) whereas the left-hand-side matrix
     * to be \(\overline{G}^{T}_{n \times k}\) or \(G^{T}_{n \times k}\).
     * <p>
     *
     * @param m the right-hand-side matrix
     * @return a new instance of a matrix representing the result when the
     * current matrix (left) multiplied by the matrix specified (right)
     */
    public Matrix multiply(Matrix m);

    /**
     * Multiplies two specified matrices and returns the result as a new
     * matrix. Both matrices will not be altered.
     *
     * @param left  the left-hand-side matrix
     * @param right the right-hand-side matrix
     * @return a new instance of a matrix representing the result when the
     * left matrix specified is multiplied by the right matrix specified
     */
    public Matrix multiply(Matrix left, Matrix right);

    /**
     * Multiplies two specified matrix arrays and returns the result as a new
     * matrix. Both matrix arrays will not be altered.
     *
     * @param leftMatrix      the left-hand-side matrix array
     * @param rightMatrix     the right-hand-side matrix array
     * @param leftMatrixN     the number of columns in the left-hand-side
     *                        matrix array
     * @param rightMatrixN    the number of columns in the right-hand-side
     *                        matrix array
     * @param leftMatrixK     the number of rows in the left-hand-side matrix
     *                        array
     * @param rightMatrixK    the number of rows in the right-hand-side matrix
     *                        array
     * @param leftMatrixBase  the base of {@code leftMatrix} (could be either
     *                        \(2\) or \(4\))
     * @param rightMatrixBase the base of {@code rightMatrix} (could be either
     *                        \(2\) or \(4\))
     * @return a new instance of a matrix representing the result when the
     * left matrix array specified is multiplied by the right matrix array
     * specified
     */
    public Matrix multiply(
            long[] leftMatrix,
            long[] rightMatrix,
            byte leftMatrixN,
            byte rightMatrixN,
            byte leftMatrixK,
            byte rightMatrixK,
            byte leftMatrixBase,
            byte rightMatrixBase
    );

    /**
     * Multiplies the row vector at the index specified in the current matrix
     * by the digit specified.
     *
     * @param index the index of the row vector to be multiplied
     * @param digit the digit the row vector to be multiplied by. When the
     *              base is \(2\), only \(0\) and \(1\) are the valid digits.
     *              For base \(4\), only \(0\), \(1\), \(2\) and \(3\) are the
     *              valid digits
     * @return the result when the specified digit is multiplied by the
     * specified index of the row vector
     */
    public long multiplyRowByDigit(byte index, byte digit);

    /**
     * Multiplies the row vector specified by the digit specified.
     *
     * @param rowVector the row vector to be multiplied
     * @param digit     the digit the row vector to be multiplied by. When the
     *                  base is \(2\), only \(0\) and \(1\) are the valid
     *                  digits. For base \(4\), only \(0\), \(1\), \(2\) and
     *                  \(3\) are the valid digits.
     * @param n         the number of columns in the row vector
     * @param base      the base of {@code rowVector} (could be either \(2\) or
     *                  \(4\))
     * @return the result when the specified digit is multiplied by the
     * specified row vector
     */
    public long multiplyRowByDigit(
            long rowVector,
            byte digit,
            byte n,
            byte base
    );

    /**
     * Returns the current matrix but <b>not</b> as a brand-new copy.
     *
     * @return the current matrix
     * @see Matrix#clone()
     */
    public Matrix getMatrix();

    /**
     * Returns the current matrix array but <b>not</b> as a brand-new instance.
     *
     * @return the current matrix array
     * @see Matrix#getMatrixArrayCopy()
     */
    public long[] getMatrixArray();

    /**
     * Returns a brand-new copy of the current matrix array.
     *
     * @return the current matrix array as a brand-new copy
     * @see Matrix#getMatrixArray()
     */
    public long[] getMatrixArrayCopy();

    /**
     * Returns a brand-new copy of the matrix array specified.
     *
     * @param matrixArray the matrix array to be copy
     * @return a brand-new copy of matrix array specified
     */
    public long[] getMatrixArrayCopy(long[] matrixArray);

    /**
     * Returns the base of the current matrix which is equivalent to the base
     * of the code \(\mathsf{C}\). It is either \(2\) or \(4\).
     *
     * @return the base of the current matrix which is either \(2\) or \(4\)
     */
    public byte getBase();

    /**
     * Returns the number of columns in the current matrix which is
     * equivalent to the length \(n\) of the codeword in the code
     * \(\mathsf{C}\).
     *
     * @return the number of columns in the current matrix which is equivalent
     * to the length of the codeword in the code
     */
    public byte getN();

    /**
     * Returns the number of rows in the current matrix which is equivalent
     * to the dimension \(k\) of the code \(\mathsf{C}\).
     *
     * @return the number of rows in the current matrix which is equivalent
     * to the dimension of the code
     */
    public byte getK();

    /**
     * Returns a row in the current matrix based on the index specified.
     *
     * @param index the index of the row (zero-based) in the matrix
     * @return the row in the current matrix based on the index specified
     */
    public long getRow(byte index);

    /**
     * Returns a row in the matrix array specified based on the index specified.
     *
     * @param matrixArray the matrix array to obtain the row from
     * @param index       the index of the row (zero-based) in the matrix
     * @return the row in the matrix array specified based on the index
     * specified
     */
    public long getRow(long[] matrixArray, byte index);

    /**
     * Returns a row in the matrix specified based on the index specified.
     *
     * @param m     the matrix to obtain the row from
     * @param index the index of the row (zero-based) in the matrix
     * @return the row in the matrix specified based on the index specified
     */
    public long getRow(Matrix m, byte index);

    /**
     * Returns a column in the current matrix based on the index specified.
     *
     * @param index the index of the column (zero-based) in the matrix
     * @return the column in the current matrix based on the index specified
     */
    public byte[] getColumn(byte index);

    /**
     * Returns a column in the matrix array specified based on the index
     * specified.
     *
     * @param matrixArray the matrix array to obtain the column from
     * @param n           the number of columns in the matrix array which is
     *                    the same as the length of code \(\mathsf{C}\)
     * @param index       the index of the column (zero-based) in the matrix
     * @param base        the base of the matrix array (could be either \(2\)
     *                    or \(4\))
     * @return the column in the matrix array specified based on the index
     * specified
     */
    public byte[] getColumn(long[] matrixArray, byte n, byte index, byte base);

    /**
     * Returns a column in the matrix specified based on the index specified.
     *
     * @param m     the matrix to obtain the column from
     * @param index the index of the column (zero-based) in the matrix
     * @return the column in the matrix specified based on the index specified
     */
    public byte[] getColumn(Matrix m, byte index);

    /**
     * Returns the value of a cell based on the row index and column index
     * specified.
     *
     * @param r the row index of the cell in the current matrix to retrieve
     *          from (zero-based)
     * @param c the column index of the cell in the current matrix to
     *          retrieve from (zero-based)
     * @return the value in cell \((r, c)\)
     */
    public byte getCell(byte r, byte c);

    /**
     * Returns the value of a cell based on the specified matrix array, row
     * index and column index.
     *
     * @param matrixArray the matrix array to retrieve the cell from
     * @param r           the row index of the cell in the matrix array to
     *                    retrieve from (zero-based)
     * @param c           the column index of the cell in the matrix array to
     *                    retrieve from (zero-based)
     * @param n           the number of columns in the matrix array
     * @param base        the base of the matrix array (could be either \(2\) or
     *                    \(4\))
     * @return the value in cell \((r, c)\) of the matrix array
     */
    public byte getCell(
            long[] matrixArray,
            byte r,
            byte c,
            byte n,
            byte base
    );

    /**
     * Returns the value of a cell based on the specified matrix, row index
     * and column index.
     *
     * @param m the matrix to retrieve the cell from
     * @param r the row index of the cell in the matrix to retrieve from
     *          (zero-based)
     * @param c the column index of the cell in the matrix to retrieve from
     *          (zero-based)
     * @return the value in cell \((r, c)\) of the matrix
     */
    public byte getCell(Matrix m, byte r, byte c);

    /**
     * Sets the value of a cell based on its row index and column index in the
     * current matrix.
     *
     * @param r     the row index of the cell in the current matrix to set
     *              (zero-based)
     * @param c     the column index of the cell in the current matrix to set
     *              (zero-based)
     * @param value the value to be set to which can be a \(0\) or \(1\) when the
     *              base is \(2\) or \(0\), \(1\), \(2\), or \(3\) in base \(4\)
     */
    public void setCell(byte r, byte c, byte value);

    /**
     * Sets the value of a cell based on the specified matrix, row index,
     * column index and value.
     *
     * @param m     the matrix to set the cell in
     * @param r     the row index of the cell in the matrix to set (zero-based)
     * @param c     the column index of the cell in the matrix to set
     *              (zero-based)
     * @param value the value to be set to which can be a \(0\) or \(1\) when the
     *              base is \(2\) or \(0\), \(1\), \(2\), or \(3\) in base \(4\)
     */
    public void setCell(Matrix m, byte r, byte c, byte value);

    /**
     * Sets the value of a cell based on its row index and column index in the
     * current matrix.
     *
     * @param matrixArray the matrix array to set the cell in
     * @param r           the row index of the cell in the current matrix to set
     *                    (zero-based)
     * @param c           the column index of the cell in the current matrix
     *                    to set (zero-based)
     * @param value       the value to be set to which can be a \(0\) or \(1\)
     *                    when the base is \(2\) or \(0\), \(1\), \(2\), or
     *                    \(3\) in base \(4\)
     * @param n           the number of columns in the matrix array
     * @param base        the base of matrix array (could be either \(2\) or \(4\))
     */
    public void setCell(
            long[] matrixArray,
            byte r,
            byte c,
            byte value,
            byte n,
            byte base
    );

    /**
     * Sets the matrix array of the current matrix to the specified matrix
     * array. There are two ways to achieve that: either assign the array passed
     * (non-deep copy) or reinitialized the array in the matrix, loop through
     * the specified matrix array and copy each cell individually. The
     * dimension of the specified matrix array must match the current
     * one. Otherwise, the operation will not be applied.
     *
     * @param matrixArray the new matrix array to set to
     * @param n           the number of columns in the matrix array
     * @param k           the number of rows in the matrix array
     * @param deepCopy    whether a deep copy is desired
     */
    public void setMatrixArray(
            long[] matrixArray,
            byte n,
            byte k,
            boolean deepCopy
    );

    /**
     * Sets a specific row in the current matrix based on the specified row
     * index and new row vector.
     *
     * @param index        the index of the row in current matrix to be
     *                     altered (zero-based)
     * @param newRowVector the value of the new row to set as
     */
    public void setRow(byte index, long newRowVector);

    /**
     * Sets a specific row based on the specified matrix array, row index and
     * new row vector.
     *
     * @param matrixArray  the matrix array to set the row in
     * @param index        the index of the row in the matrix array to be
     *                     altered (zero-based)
     * @param newRowVector the value of the new row to set as
     */
    public void setRow(long[] matrixArray, byte index, long newRowVector);

    /**
     * Sets a specific row based on the specified matrix, row index and new
     * row vector.
     *
     * @param m            the matrix to set the row in
     * @param index        the index of the row in matrix to be altered
     *                     (zero-based)
     * @param newRowVector the value of the new row to set as
     */
    public void setRow(Matrix m, byte index, long newRowVector);

    /**
     * Sets a specific column in the current matrix based on the specified
     * column index and new column vector.
     *
     * @param index           the index of the column in current matrix to be
     *                        altered (zero-based)
     * @param newColumnVector the value of the new column to set as
     */
    public void setColumn(byte index, byte[] newColumnVector);

    /**
     * Sets a specific column based on the specified matrix array, column index
     * and new column vector.
     *
     * @param matrixArray     the matrix array to set the column in
     * @param index           the index of the column in the matrix array to be
     *                        altered (zero-based)
     * @param newColumnVector the value of the new column to set as
     * @param base            the base of the matrix array (could be either \(2\)
     *                        or \(4\))
     */
    public void setColumn(
            long[] matrixArray,
            byte index,
            byte[] newColumnVector,
            byte base
    );

    /**
     * Sets a specific column based on the specified matrix, column index and
     * new column vector.
     *
     * @param m               the matrix to set the column in
     * @param index           the index of the column in the matrix to be
     *                        altered (zero-based)
     * @param newColumnVector the value of the new column to set as
     */
    public void setColumn(Matrix m, byte index, byte[] newColumnVector);

    /**
     * Returns {@code true} if the current matrix is invertible (<i>i.e.,
     * </i> the determinant of the current matrix is <em>not</em> \(0\)),
     * {@code false} if the determinant <em>is</em> \(0\).
     *
     * @return {@code true} if the current matrix is invertible (<i>i.e.,</i>
     * the determinant of is not \(0\)), {@code false} otherwise
     */
    public boolean isInvertible();

    /**
     * To check if the matrix contains at least a single row of {@code 0}.
     *
     * @return {@code true} if there exists a row that is {@code 0}, {@code
     * false} otherwise
     */
    public boolean containsZeroRow();

    /**
     * Returns {@code true} if the specified matrix is invertible (<i>i.e.,
     * </i> the determinant of the matrix is <em>not</em> \(0\)), {@code false}
     * if the determinant <em>is</em> \(0\).
     *
     * @param m the matrix to find the determinant of
     * @return {@code true} if the specified matrix is invertible (<i>i.e.,
     * </i> the determinant of is not \(0\)), {@code false} otherwise
     */
    public boolean isInvertible(Matrix m);

    /**
     * Prints the parameters (\(n\), \(k\) and \(base\)) of the code on
     * console.
     */
    public void printParameters();

    /**
     * Prints the current matrix on console with brackets surrounding the
     * matrix, a single space between columns, each digit is written in base
     * 10 as well as the size of the matrix.
     */
    public void printMatrix();

    /**
     * Prints the current matrix on console with columns separated by a
     * single space. The brackets surrounding the matrix will be displayed if
     * {@code addBrackets} is {@code true}. Each digit is displayed based on
     * the style specified. It will show the size of the matrix in the
     * bottom-right corner if {@code showSize} is {@code true}.
     *
     * @param addBrackets should brackets around the matrix be displayed
     * @param style       the style format which could either be binary,
     *                    quaternary, decimal or \(\LaTeX\)
     * @param showSize    should the dimension of the matrix be shown at
     *                    the bottom-right
     */
    public void printMatrix(
            boolean addBrackets,
            Style style,
            boolean showSize
    );

    /**
     * Prints the current matrix on console. The columns will be separated
     * by the delimiter specified. The brackets surrounding the matrix will
     * be displayed if {@code addBrackets} is {@code true}. Each digit is
     * displayed based on the style specified. It will show the size of the
     * matrix in the bottom-right corner if {@code showSize} is {@code true}.
     *
     * @param delimiter   the delimiter between columns of current matrix
     * @param addBrackets should brackets around the matrix be displayed
     * @param style       the style format which could either be binary,
     *                    quaternary, decimal or \(\LaTeX\)
     * @param showSize    should the dimension of the matrix be shown at
     *                    the bottom-right
     */
    public void printMatrix(
            String delimiter,
            boolean addBrackets,
            Style style,
            boolean showSize
    );

    /**
     * Prints the specified matrix array on console. The columns will be
     * separated by the delimiter specified. The brackets surrounding the
     * matrix will be displayed if {@code addBrackets} is {@code true}. Each
     * digit is displayed based on the style specified. It will show the size
     * of the matrix in the bottom-right corner if {@code showSize} is {@code
     * true}.
     *
     * @param matrixArray the matrix array to be displayed on console
     * @param n           the number of columns in the matrix array
     * @param base        the base of the matrix array (could be either \(2\)
     *                    or \(4\))
     * @param delimiter   the delimiter between columns of current matrix
     * @param addBrackets should brackets around the matrix be displayed
     * @param style       the style format which could either be binary,
     *                    quaternary, decimal or \(\LaTeX\)
     * @param showSize    should the dimension of the matrix be shown at the
     *                    bottom-right
     */
    public void printMatrix(
            long[] matrixArray,
            byte n,
            byte base,
            String delimiter,
            boolean addBrackets,
            Style style,
            boolean showSize
    );
}