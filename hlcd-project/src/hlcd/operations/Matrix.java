package hlcd.operations;

import hlcd.enums.Style;
import hlcd.exceptions.*;

import java.io.Serializable;

/**
 * Defines a matrix as a 1-D array where there are 62 columns. The two
 * far-left columns (<i>i.e.</i>, indices {@code 0} and {@code 1}) are not
 * used. The indices {@code 2} and above are used. A vector will start at the
 * appropriate column and span to the right direction \(n\) columns. Using this
 * approach, the columns that will be covered are: {@code x}, {@code x+1},
 * {@code ...}, {@code 62} and {@code 63} (far-right). For example, consider
 * the following randomly matrix of a \(\left[n, \, k\right]_{base} = \left[7,
 * \,4\right]_{4}\) code:
 * \begin{equation}
 * \begin{bmatrix}
 * \omega &amp; \omega &amp; \omega &amp; 0 &amp;
 * \overline{\omega} &amp; 0 &amp; 0\\
 * \omega &amp; \overline{\omega} &amp; \omega &amp; 1 &amp; 0 &amp;
 * \overline{\omega} &amp; \overline{\omega}\\
 * 0 &amp; \omega &amp; \overline{\omega} &amp; 1 &amp; \omega &amp;
 * \overline{\omega} &amp; \omega\\
 * 1 &amp; \overline{\omega} &amp; 1 &amp; 0 &amp;
 * 0 &amp; 1 &amp;\overline{\omega}
 * \end{bmatrix}.
 * \end{equation}
 * In Java, the binary representation of each of the four vectors is written as:
 * \begin{equation}
 * \begin{bmatrix}
 * 0b &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 10 &amp; 10 &amp; 10 &amp; 00 &amp; 11 &amp; 00 &amp; 00\\
 * 0b &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 10 &amp; 11 &amp; 10 &amp; 01 &amp; 00 &amp; 11 &amp; 11\\
 * 0b &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 00 &amp; 10 &amp; 11 &amp; 01 &amp; 10 &amp; 11 &amp; 10\\
 * 0b &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp; 00 &amp;
 * 00 &amp; 01 &amp; 11 &amp; 01 &amp; 00 &amp; 00 &amp; 01 &amp; 11\\
 * \end{bmatrix}.
 * \end{equation}
 * <p>
 * It also has the ability to find the transpose of the current matrix,
 * complex conjugation transpose and the determinant. Finding the determinant
 * uses a modified version of Bareiss's Algorithm. A complete pseudocode can be
 * on <a href=
 * "https://dr.library.brocku.ca/bitstream/handle/10464/15405/Maysara_Al_Jumaily_MSc_Thesis.pdf#page=96&zoom=100,294,150"
 * target="_blank">page 96 of the
 * thesis</a>. The algorithm is implemented in the private method of this
 * class {@code getDeterminantEngine(Matrix)}.
 * <p>
 * TODO: create a method to return the parity check matrix when the generator
 *  matrix is in standard form.
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running the \"Matrix\" class...");
 *     //generate a random matrix array
 *     byte minN = 7;
 *     byte maxN = 7;
 *     byte minK = 4;
 *     byte maxK = 4;
 *     RandomMatrixGenerator rmg = new RandomMatrixGenerator(
 *         minN, maxN, minK, maxK, (byte) 4
 *     );
 *     //convert matrix array to a Matrix object
 *     Matrix g = new Matrix(
 *         rmg.getMatrixArrayClone(), rmg.getN(), rmg.getK(), rmg.getBase()
 *     );
 *     //print the matrix using all possible styles
 *     System.out.println("Printing matrix using binary style:");
 *     g.printMatrix(" ", false, Style.BINARY, false);
 *     System.out.println();
 *     System.out.println("Printing matrix using decimal style:");
 *     g.printMatrix(" ", false, Style.DECIMAL, true);
 *     System.out.println();
 *     System.out.println("Printing matrix using quaternary style:");
 *     g.printMatrix(" ", true, Style.QUATERNARY, false);
 *     System.out.println();
 *     System.out.println("Printing matrix using LaTeX style:");
 *     g.printMatrix(" ", true, Style.LATEX, true);
 *
 *     System.out.println("Transposing the top row of the matrix:");
 *     byte[] col = g.transposeRowToCol((byte) 0);
 *     for (int i = 0; i &#60; col.length; i++) {
 *         System.out.println(col[i]);
 *     }
 *     System.out.println("Transposing the far-left column of the matrix:");
 *     long row = g.transposeColToRow((byte) 0);
 *     System.out.println(MatrixPrinter.vectorAsString(
 *         row, g.getK(), g.getN(), " ", Style.DECIMAL
 *     ));
 *     System.out.println();
 *     System.out.println("Transposing the matrix:");
 *     Matrix transpose = g.transpose();
 *     transpose.printMatrix(" ", true, Style.DECIMAL, true);
 *     System.out.println();
 *     System.out.println("Hermitian transposing the matrix:");
 *     Matrix hermitianTranspose = g.hermitianTranspose();
 *     hermitianTranspose.printMatrix(" ", true, Style.DECIMAL, true);
 *     System.out.println("The \"Matrix\" class completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 8th, 2022)
 * @see Long
 * @see <a href=
 * "https://dr.library.brocku.ca/bitstream/handle/10464/15405/Maysara_Al_Jumaily_MSc_Thesis.pdf"
 * target="_blank">M.Sc. thesis</a>
 * @since 1.8
 */
public class Matrix implements MatrixOperations, Serializable, Cloneable {

    /*
     * Complete Java documentation can be found in the MatrixOperations
     * interface.
     */

    /**
     * The structure used to store a matrix.
     */
    private long[] matrixArray;

    /**
     * The length of each codeword in the code.
     */
    private byte n;

    /**
     * The dimension of the code (and the number of rows in the matrix).
     */
    private byte k;

    /**
     * The base of the code which can be either 2 or 4.
     */
    private byte base;

    /**
     * Creates a random quaternary matrix with \(1 \leq k \leq 30\) and
     * \(1 \leq n \leq 30\). This constructor can be used for testing.
     */
    public Matrix() {
        byte minK = 1;
        byte maxK = 30;
        byte minN = 1;
        byte maxN = 30;
        byte base = 4;

        RandomMatrixGenerator rmg = new RandomMatrixGenerator(
                minK, maxK, minN, maxN, base
        );
        matrixArray = rmg.getMatrixArrayClone();
        n = rmg.getN();
        k = rmg.getK();
        this.base = base;
    }

    /**
     * Creates a random quaternary matrix with the specified dimension. This
     * specific constructor can be used for testing.
     *
     * @param n the number of columns in the matrix (which should match
     *          the length of codewords in the code)
     * @param k the number of rows in the matrix (which should match
     *          the dimension of the code)
     */
    public Matrix(byte n, byte k) {
        this.n = n;
        this.k = k;
        this.base = 4;

        RandomMatrixGenerator rmg = new RandomMatrixGenerator(
                n, n, k, k, base
        );
        matrixArray = rmg.getMatrixArrayClone();

    }

    /**
     * Creates a matrix based on the specified values of \(n\), \(k\), \(d\)
     * and \(base\). A null matrix will be created.
     *
     * @param n    the number of columns in the matrix (which should match
     *             the length of codewords in the code)
     * @param k    the number of rows in the matrix (which should match
     *             the dimension of the code)
     * @param base the base of the code (could be either \(2\) or \(4\))
     */
    public Matrix(byte n, byte k, byte base) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        if (!Functions.isValidNAndKValues(n, k)) {
            throw new InvalidCodeParameters(n, k);
        }
        this.n = n;
        this.k = k;
        this.base = base;
        matrixArray = new long[this.k];
        //initializes to a zero matrix by default
        for (int i = 0; i < matrixArray.length; i++) {
            matrixArray[i] = 0L;
        }
    }

    /**
     * Creates a matrix based on the specified values of \(n\), \(k\), \(d\),
     * \(base\) and the matrix array.
     *
     * @param matrixArray the default values to assign the cells of the matrix
     * @param n           the number of columns in the matrix (which should
     *                    match the length of codewords in the code)
     * @param k           the number of rows in the matrix (which should match
     *                    the dimension of the code)
     * @param base        the base of the code (could be either \(2\) or \(4\))
     */
    public Matrix(long[] matrixArray, byte n, byte k, byte base) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        this.n = n;
        this.k = k;
        this.base = base;
        this.matrixArray = getMatrixArrayCopy(matrixArray);
    }

    @Override
    public byte[] transposeRowToCol(byte index) {
        if (!Functions.isValidRowIndex(index, (byte) matrixArray.length)) {
            throw new InvalidRowIndexException(
                    index, (byte) matrixArray.length
            );
        }
        long rowVector = matrixArray[index];
        return transposeRowToColEngine(rowVector, n, base);
    }

    @Override
    public byte[] transposeRowToCol(long rowVector, byte x, byte base) {
        return transposeRowToColEngine(rowVector, x, base);
    }

    @Override
    public byte[] transposeRowToCol(Matrix m, byte row) {
        if (!Functions.isValidBase(m.getBase())) {
            throw new InvalidBaseException(m.getBase());
        }
        if (!Functions.isValidRowIndex(
                row,
                (byte) m.getMatrixArrayCopy().length
        )) {
            throw new InvalidRowIndexException(
                    row,
                    (byte) m.getMatrixArrayCopy().length
            );
        }
        return transposeRowToColEngine(
                m.getMatrixArrayCopy()[row],
                m.getN(),
                m.getBase()
        );
    }

    /**
     * Will return the transpose of the specified row vector. For example,
     * say that the current code is \(\left[n, \, k\right]_{base} = \left[5,
     * \, 1\right]_{4}\). Assume the column specified is
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
     * @param rowVector the row vector to be transposed
     * @param n         the number of columns in the row vector (one-based)
     * @param base      the base of the code (could be either \(2\) or \(4\))
     * @return the row vector transposed as a size of \(n \times 1\)
     */
    private static byte[] transposeRowToColEngine(
            long rowVector,
            byte n,
            byte base
    ) {
        byte[] result = new byte[n];
        long mask = 0;
        byte shift = 0;

        //find the maximum number of columns (62 for base 2 or 30 for base 4)
        byte maxCol = 0;
        if (base == 2) {
            maxCol = 62;
            mask = 0b1;
            shift = 1;
        } else if (base == 4) {
            maxCol = 30;
            mask = 0b11;
            shift = 2;
        }
        //shift the mask so that it starts at the far-left rather than far-right
        mask = mask << shift * (n - 1);

        //check if the number of the columns is valid
        if (!Functions.isValidColIndex((byte) (n - 1), maxCol)) {
            throw new InvalidColIndexException((byte) (n - 1), maxCol, base);
        }

        for (int c = 0; c < result.length; c++) {
            //rowVector	: aabbccddeeffgg		//the input
            //mask		: 11000000000000		//the mask
            //temp		: (aa & 11)000000000000	//the result of rowVector & mask
            //cell		: 000000000000(aa & 11)	//shift result to the far-right
            long temp = rowVector & mask;//result with leading 0's from right
            byte cell = (byte) (temp >>> (n * shift) - ((c + 1) * (shift)));
            result[c] = cell;
            mask = mask >>> shift;
        }
        return result;
    }

    @Override
    public long transposeColToRow(byte columnIndex) {
        byte[] columnVector = getColumn(this, columnIndex);
        return transposeColToRowEngine(columnVector, k, base);
    }

    @Override
    public long transposeColToRow(byte[] columnVector, byte base) {
        return transposeColToRowEngine(columnVector, k, base);
    }

    @Override
    public long transposeColToRow(Matrix m, byte columnIndex) {
        byte[] columnVector = getColumn(m, columnIndex);
        return transposeColToRowEngine(columnVector, m.getK(), m.getBase());
    }

    /**
     * Returns the transpose the column vector. For example, say that the
     * current code is
     * \(\left[n, \, k\right]_{base} = \left[5, \, 1\right]_{4}\).
     * Assume the column specified is
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
     * @param colVector the column vector to be transposed
     * @param k         the dimension of the matrix (this must equal the
     *                  length of the column vector specified)
     * @param base      the base of the code (could be either \(2\) or \(4\))
     * @return the column vector transposed
     */
    private long transposeColToRowEngine(
            byte[] colVector,
            byte k,
            byte base
    ) {
        long result = 0;
        byte shift = 0;

        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        if (!Functions.isValidColumnVectorDimension(
                (byte) colVector.length, k
        )) {
            throw new InvalidColumnVectorDimensionException(
                    (byte) colVector.length, base
            );
        }
        if (colVector.length != k) {
            System.out.println("The column vector length passed (" +
                                       colVector.length + ") doesn't match k (" + k + "). " +
                                       "The column vector value will be used instead."
            );
        }

        //ensure column vector contains valid digits (0,1) for base 2, (0,1,2,3)
        // for base 4
        for (int i = 0; i < colVector.length; i++) {
            if (!Functions.isValidDigit(colVector[i], base)) {
                throw new InvalidDigitException(colVector[i], base);
            }
        }

        if (base == 2) {
            shift = 1;
        } else if (base == 4) {
            shift = 2;
        }
        int n = colVector.length;
        //shift the mask so that it starts at the far-left rather than far-right
        //shift = (byte) (shift * (n - 1));
        for (int i = 0; i < colVector.length; i++) {
            long digit = (long) colVector[i] << shift * (n - 1) - (i * shift);
            result = result | digit;
        }
        return result;
    }

    @Override
    public Matrix transpose() {
        return transposeEngine(getMatrixArrayCopy(), getK(), getN(), getBase());
    }

    @Override
    public Matrix transpose(Matrix m) {
        return transposeEngine(
                m.getMatrixArrayCopy(),
                m.getK(),
                m.getN(),
                m.getBase()
        );
    }

    @Override
    public long[] transpose(long[] matrixArray, byte n, byte k, byte base) {
        return transposeEngine(matrixArray, k, n, base).getMatrixArrayCopy();
    }

    /**
     * Returns a new matrix that is the transpose of specified matrix
     * array. The matrix specified will not be altered.
     *
     * @param matrixArray the matrix to be transposed
     * @param n           the number of columns in the matrix array
     *                    which is the same as the length of code \(\mathsf{C}\)
     * @param k           the number of rows in the matrix array which is the
     *                    same as the dimension of code \(\mathsf{C}\)
     * @param base        the base of the code (could be either \(2\) or \(4\))
     * @return a new instance of the matrix array transposed
     * @see MatrixOperations#hermitianTranspose(long[], byte, byte, byte)
     */
    private Matrix transposeEngine(
            long[] matrixArray,
            byte k,
            byte n,
            byte base
    ) {
        byte shift = 0;
        long[] result = new long[n];
        if (base == 2) {
            shift = 1;
        } else if (base == 4) {
            shift = 2;
        }

        //use k and n to your advantage since a k*n matrix transposed is n*k.
        for (byte r = 0; r < k; r++) {
            byte[] currentRowTransposed = transposeRowToCol(
                    matrixArray[r], n, base
            );
            //append the current row transposed to the result
            for (byte j = 0; j < n; j++) {
                result[j] = result[j] << shift;
                result[j] = result[j] | currentRowTransposed[j];
            }
        }

        //say k is the dimension of the current matrix to be transposed.
        //input is n * k, output is k * n.

        return new Matrix(result, k, n, base);
    }

    @Override
    public Matrix hermitianTranspose() {
        return hermitianTransposeEngine(
                getMatrixArrayCopy(),
                getK(),
                getN(),
                getBase()
        );
    }

    @Override
    public Matrix hermitianTranspose(Matrix m) {
        return hermitianTransposeEngine(
                m.getMatrixArrayCopy(),
                m.getK(),
                m.getN(),
                m.getBase()
        );
    }

    @Override
    public long[] hermitianTranspose(
            long[] matrixArray, byte n, byte k, byte base
    ) {
        Matrix m = hermitianTransposeEngine(matrixArray, k, n, base);
        return m.getMatrixArrayCopy();
    }

    /**
     * Returns a new matrix that is the Hermitian transpose of the matrix
     * array specified in base \(4\). The matrix array will not be altered.
     * Essentially, it will apply the transpose of the matrix array and swap
     * cells containing \(\omega\) with \(\overline{\omega}\) and vice-versa.
     *
     * @param matrixArray the matrix array to be Hermitian transposed
     * @param n           the number of columns in the matrix array
     * @param k           the number of rows in the matrix array
     * @param base        the base of the code (could be either \(2\) or \(4\))
     * @return a new instance of the Hermitian transpose of specified matrix
     * array
     * @see MatrixOperations#transpose(long[], byte, byte, byte)
     */
    private Matrix hermitianTransposeEngine(
            long[] matrixArray, byte k, byte n, byte base
    ) {
        byte shift = 0;
        long[] resultArray = new long[n];
        if (base == 2) {
            shift = 1;
        } else if (base == 4) {
            shift = 2;
        }

        //use k and n to your advantage since a k*n matrix transposed is n*k.
        for (byte r = 0; r < k; r++) {
            byte[] currentRowTransposed = transposeRowToCol(
                    matrixArray[r], n, base
            );
            //append the current row transposed to the result
            for (byte j = 0; j < n; j++) {
                resultArray[j] = resultArray[j] << shift;
                resultArray[j] = resultArray[j] | currentRowTransposed[j];
            }
        }
        Matrix resultMatrix = new Matrix(resultArray, k, n, base);
        for (byte row = 0; row < n; row++) {
            for (byte col = 0; col < k; col++) {

                if (resultMatrix.getCell(row, col) == 2) {
                    resultMatrix.setCell(row, col, (byte) 3);
                } else if (resultMatrix.getCell(row, col) == 3) {
                    resultMatrix.setCell(row, col, (byte) 2);
                }
            }
        }
        return resultMatrix;
    }

    /**
     * Returns the current matrix as a brand-new copy.
     *
     * @return the current matrix as a brand-new copy
     */
    @Override
    public Matrix clone() {
        return new Matrix(getMatrixArrayCopy(), n, k, base);
    }

    @Override
    public long[] getMatrixArrayCopy() {
        return getMatrixCopyEngine(matrixArray);
    }

    @Override
    public long[] getMatrixArrayCopy(long[] matrixArray) {
        return getMatrixCopyEngine(matrixArray);
    }

    /**
     * The engine of returning a brand-new copy of the matrix array passed.
     *
     * @param matrixArray the matrix array to be cloned
     * @return a brand-new copy of the matrix array
     */
    private long[] getMatrixCopyEngine(long[] matrixArray) {
        long[] result = new long[matrixArray.length];
        for (byte i = 0; i < matrixArray.length; i++) {
            result[i] = matrixArray[i];
        }
        return result;
    }

    @Override
    public byte multiplyRowByCol(Matrix rightMatrix, byte row, byte column) {
        return multiplyRowByColEngine(
                matrixArray,
                rightMatrix.getMatrixArrayCopy(),
                n,
                rightMatrix.getN(),
                k,
                rightMatrix.getK(),
                base,
                rightMatrix.getBase(),
                row,
                column
        );
    }

    @Override
    public byte multiplyRowByCol(
            Matrix leftMatrix,
            Matrix rightMatrix,
            byte row,
            byte column
    ) {
        return multiplyRowByColEngine(
                leftMatrix.getMatrixArrayCopy(),
                rightMatrix.getMatrixArrayCopy(),
                leftMatrix.n,
                rightMatrix.getN(),
                leftMatrix.k,
                rightMatrix.getK(),
                leftMatrix.base,
                rightMatrix.getBase(),
                row,
                column
        );
    }

    @Override
    public byte multiplyRowByCol(
            long[] leftMatrix,
            long[] rightMatrix,
            byte leftMatrixArrayN,
            byte rightMatrixArrayN,
            byte leftMatrixArrayK,
            byte rightMatrixArrayK,
            byte leftMatrixArrayBase,
            byte rightMatrixArrayBase,
            byte row,
            byte column
    ) {
        return multiplyRowByColEngine(
                leftMatrix,
                rightMatrix,
                leftMatrixArrayN,
                rightMatrixArrayN,
                leftMatrixArrayK,
                rightMatrixArrayK,
                leftMatrixArrayBase,
                rightMatrixArrayBase,
                row,
                column
        );
    }

    /**
     * Returns a single digit as the result of multiplying a row by a column.
     * Everything is zero-based.
     *
     * @param leftMatrix      the left matrix array to obtain the row from
     * @param rightMatrix     the right matrix array to obtain the column from
     * @param n               the number of columns in the left matrix array
     * @param rightMatrixN    the number of columns in the right matrix array
     * @param k               the number of rows in the left matrix array
     * @param rightMatrixK    the number of rows in the right matrix array
     * @param gBase           the base of the left matrix array which is
     *                        either \(2\) or \(4\)
     * @param rightMatrixBase the base of the right matrix array which is
     *                        either \(2\) or \(4\)
     * @param row             the row index in the left matrix to use
     * @param column          the column index in the right matrix to use
     * @return the result (a single digit) when the row specified is multiplied
     * by the column specified
     */
    private byte multiplyRowByColEngine(
            long[] leftMatrix,
            long[] rightMatrix,
            byte n,
            byte rightMatrixN,
            byte k,
            byte rightMatrixK,
            byte gBase,
            byte rightMatrixBase,
            byte row,
            byte column
    ) {
        byte shift = 0;
        if (!Functions.isValidBase(gBase)) {
            throw new InvalidBaseException(gBase);
        }

        if (!Functions.isValidBase(rightMatrixBase)) {
            throw new InvalidBaseException(rightMatrixBase);
        }

        if (gBase != rightMatrixBase) {//must match!
            throw new InvalidMatricesBases(gBase, rightMatrixBase);
        }

        //find the maximum number of columns (62 for base 2 or 30 for base 4)
        byte maxCol = 0;
        if (gBase == 2) {
            maxCol = 62;
            shift = 1;
        } else if (gBase == 4) {
            maxCol = 30;
            shift = 2;
        }
        //check if the index of the column is valid
        if (!Functions.isValidColIndex(column, maxCol)) {
            throw new InvalidColIndexException(column, maxCol, gBase);
        }

        //check if the index of the column is valid with respect to n
        if (!Functions.isValidColIndex(column, rightMatrixN)) {
            System.out.println("Col = " + column + "\t n = " + rightMatrixN +
                                       "\t k = " + rightMatrixK);
            throw new InvalidColIndexException(column, rightMatrixN, gBase);
        }

        //check if index of the row is valid
        if (!Functions.isValidRowIndex(row, (byte) leftMatrix.length)) {
            throw new InvalidRowIndexException(row, (byte) leftMatrix.length);
        }

        //check if the index of the column is valid with respect to k
        if (!Functions.isValidRowIndex(row, k)) {
            throw new InvalidRowIndexException(row, k);
        }

        //check if the dimensions of the two matrices match to perform matrix
        // multiplication correctly.
        if (!Functions.isValidMatrixMultiplicationDimension(
                leftMatrix, n, k, rightMatrix, rightMatrixN, rightMatrixK
        )) {
            throw new InvalidMatrixDimensionsException(
                    k, n, rightMatrixN, rightMatrixK
            );
        }

        if (leftMatrix.length != k) {
            //TODO
            System.out.println("The column vector length passed (" +
                                       leftMatrix.length + ") doesn't match k (" + k + "). " +
                                       "The column vector length will be used instead."
            );
        }
        if (rightMatrix.length != n) {
            //TODO
            System.out.println("Error! The dimensions don't match!");
        }

        //Start the logic
        long rowVector = getRow(leftMatrix, row);
        byte[] columnVector = getColumn(
                rightMatrix, rightMatrixN, column, gBase
        );
        byte tempDigit = 0;
        long colVectorAsRowVector = 0;
        for (int i = 0; i < columnVector.length; i++) {
            colVectorAsRowVector = colVectorAsRowVector << shift;
            colVectorAsRowVector = colVectorAsRowVector | columnVector[i];
        }
        return new GF4Operations().innerProduct(
                rowVector, colVectorAsRowVector
        );
    }

    @Override
    public byte getDeterminant() {
        return getDeterminantEngine(this);
    }

    @Override
    public byte getDeterminant(Matrix m) {
        return getDeterminantEngine(m);
    }

    /**
     * Uses a modified version of Bareiss's Algorithm to compute the
     * determinant. There will be no division by zero encountered in this
     * version of the algorithm.
     *
     * @param m the matrix to find the determinant of
     * @return the determinant of the matrix. In the case where the matrix is
     * not invertible, {@code 0} will be returned. This is used for
     * quaternary matrices which means the possible return values are: {@code
     * 0}, {@code 1}, {@code 2} or {@code 3}.
     */
    private byte getDeterminantEngine(Matrix m) {
        Matrix copy = new Matrix(
                m.getMatrixArrayCopy(),
                m.getN(),
                m.getK(),
                m.getBase()
        );
        byte n = (byte) copy.getMatrixArrayCopy().length;
        byte pivot = 1;

        for (byte k = 0; k < n - 1; k++) {//note, k is NOT the dimension ;)
            if (copy.getCell(k, k) == 0) {//switching rows due to division by 0
                boolean isFixed = false;
                for (
                        byte rowIndexToSwap = (byte) (k + 1);
                        rowIndexToSwap < n;
                        rowIndexToSwap++
                ) {
                    if (copy.getCell(rowIndexToSwap, k) != 0) {
                        long tmpRow = copy.getRow(k);
                        long rowToSwap = copy.getRow(rowIndexToSwap);

                        copy.setRow(k, rowToSwap);
                        copy.setRow(rowIndexToSwap, tmpRow);
                        isFixed = true;
                        break;
                    }
                }
                if (!isFixed) {
                    return 0;
                }
                if (copy.getCell(k, k) == 0) {//must NEVER enter here
                    System.out.println("Even after the swapping, " +
                                               "we still have division by 0.");
                }
            }
            for (byte i = (byte) (k + 1); i < n; i++) {
                for (byte j = (byte) (k + 1); j < n; j++) {
                    byte current = copy.getCell(i, j);
                    byte topRight = copy.getCell(k, k);
                    byte right = copy.getCell(i, k);
                    byte top = copy.getCell(k, j);
                    byte value = copy.sub(
                            copy.mul(current, topRight),
                            copy.mul(right, top)
                    );
                    copy.setCell(i, j, value);
                    value = copy.div(copy.getCell(i, j), pivot);
                    copy.setCell(i, j, value);
                }
            }
            if (pivot == 0) {
                System.out.println("Pivot is zero");
            }
            pivot = copy.getCell(k, k);
        }
        return copy.getCell((byte) (n - 1), (byte) (n - 1));
    }

    /**
     * Performs binary and/or quaternary addition based on the two digits
     * specified using the xor operator. This is the same as subtraction.
     *
     * @param x the left digit
     * @param y the right digit
     * @return the result of both digits xor-ed
     * @see Matrix#sub(byte, byte)
     */
    private byte add(byte x, byte y) {
        return (byte) (x ^ y);
    }

    /**
     * Performs binary and/or quaternary subtraction based on the two digits
     * specified using the xor operator. This is the same as addition.
     *
     * @param x the left digit
     * @param y the right digit
     * @return the result of both digits xor-ed
     * @see Matrix#add(byte, byte)
     */
    private byte sub(byte x, byte y) {
        return (byte) (x ^ y);
    }

    /**
     * Performs binary and/or quaternary multiplication based on the two digits
     * specified in base \(2\) or base \(4\).
     *
     * @param x the left digit
     * @param y the right digit
     * @return the result of both digits multiplied in base \(2\) or base \(4\)
     */
    private byte mul(byte x, byte y) {
        return Functions.MUL_ARRAY[x][y];
    }

    /**
     * Performs binary and/or quaternary division based on the two digits
     * specified in base \(2\) or base \(4\).
     *
     * @param x the left digit
     * @param y the right digit
     * @return the result of both digits divided in base \(2\) or base \(4\)
     */
    private byte div(byte x, byte y) {
        if (Functions.DIV_ARRAY[x][y] == -99) {
            System.out.println("row " + x);
            System.out.println("col " + y);
        }
        return Functions.DIV_ARRAY[x][y];
    }

    @Override
    public Matrix multiply(Matrix m) {
        return multiply(
                matrixArray, m.getMatrixArrayCopy(), n, m.getN(),
                k, m.getK(), base, m.getBase()
        );
    }

    @Override
    public Matrix multiply(Matrix left, Matrix right) {
        return multiply(
                left.getMatrixArrayCopy(),
                right.getMatrixArrayCopy(),
                left.getN(),
                right.getN(),
                left.getK(),
                right.getK(),
                left.getBase(),
                right.getBase()
        );
    }

    @Override
    public Matrix multiply(
            long[] leftMatrixArray,
            long[] rightMatrixArray,
            byte leftMatrixArrayN,
            byte rightMatrixArrayN,
            byte leftMatrixArrayK,
            byte rightMatrixArrayK,
            byte leftMatrixArrayBase,
            byte rightMatrixArrayBase
    ) {
        if (!Functions.isValidBase(leftMatrixArrayBase)) {
            throw new InvalidBaseException(leftMatrixArrayBase);
        }

        if (!Functions.isValidBase(rightMatrixArrayBase)) {
            throw new InvalidBaseException(rightMatrixArrayBase);
        }

        if (leftMatrixArrayBase != rightMatrixArrayBase) {//must match!
            throw new InvalidMatricesBases(
                    leftMatrixArrayBase, rightMatrixArrayBase
            );
        }

        //check if the dimensions of the two matrices match to perform matrix
        // multiplication correctly.
        if (!Functions.isValidMatrixMultiplicationDimension(
                leftMatrixArray, leftMatrixArrayN, leftMatrixArrayK,
                rightMatrixArray, rightMatrixArrayN, rightMatrixArrayK
        )) {
            throw new InvalidMatrixDimensionsException(
                    leftMatrixArrayK, leftMatrixArrayN,
                    rightMatrixArrayN, rightMatrixArrayK
            );
        }

        if (leftMatrixArray.length != leftMatrixArrayK) {
            //TODO create an exception or use on already created
            System.out.println("The column vector length passed (" +
                                       leftMatrixArray.length + ") doesn't " +
                                       "match k (" + leftMatrixArrayK + "). " +
                                       "The column vector length will be used " +
                                       "instead."
            );
        }
        if ((rightMatrixArray.length != leftMatrixArrayN)) {
            //TODO create an exception or use on already created
            System.out.println("Error! The dimensions don't match!");
        }

        //logic starts here:
        long[] result = new long[leftMatrixArrayK];
        for (byte r = 0; r < leftMatrixArrayK; r++) {
            for (byte c = 0; c < rightMatrixArrayN; c++) {
                byte cellValue = multiplyRowByColEngine(
                        leftMatrixArray,
                        rightMatrixArray,
                        leftMatrixArrayN,
                        rightMatrixArrayN,
                        leftMatrixArrayK,
                        rightMatrixArrayK,
                        leftMatrixArrayBase,
                        rightMatrixArrayBase,
                        r,
                        c
                );
                setCellEngine(
                        result, r, c, cellValue,
                        leftMatrixArrayK, leftMatrixArrayBase
                );
            }
        }
        return new Matrix(
                result, rightMatrixArrayN, leftMatrixArrayK, leftMatrixArrayBase
        );
    }

    @Override
    public long multiplyRowByDigit(byte index, byte digit) {
        //check if index of the row index is valid
        if (!Functions.isValidRowIndex(index, (byte) matrixArray.length)) {
            throw new InvalidRowIndexException(
                    index,
                    (byte) matrixArray.length
            );
        }
        return multiplyRowByDigitEngine(matrixArray[index], digit, n, base);
    }

    @Override
    public long multiplyRowByDigit(
            long rowVector,
            byte digit,
            byte n,
            byte base
    ) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        return multiplyRowByDigitEngine(rowVector, digit, n, base);
    }

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
    private long multiplyRowByDigitEngine(
            long rowVector,
            byte digit,
            byte n,
            byte base
    ) {
        //ensure the column contains valid digits according to the base.
        if (!Functions.isValidDigit(digit, base)) {
            throw new InvalidDigitException(digit, base);
        }
        return new GF4Operations().multiplyByScalar(rowVector, digit, base);
    }

    @Override
    public Matrix getGPrime(byte examiningRow) {
        return getSubGPrimeEngine(examiningRow);
    }

    @Override
    public Matrix getGPrime() {
        return getSubGPrimeEngine((byte) (k - 1));
    }

    private Matrix getSubGPrimeEngine(byte examiningRow) {
        /* Ensures a one-based row index (needed because if the examiningRow
        is 0, it doesn't make sense to create an array of length 0).*/
        examiningRow = (byte) (examiningRow + 1);
        Matrix subMatrix = createSubMatrix(examiningRow);
        Matrix subMatrixConjugated = subMatrix.hermitianTranspose();
        return subMatrix.multiply(subMatrixConjugated);
    }

    /**
     * Returns a submatrix of \(G\) with fewer or equal rows.
     *
     * @param length the number of rows to have in the submatrix (one-based)
     * @return a submatrix of \(G\) with fewer or equal rows
     */
    private Matrix createSubMatrix(byte length) {
        //TODO, ensure matrix that will be generated doesn't have length of 0
        long[] duplicate = getMatrixArrayCopy();//current matrix cloned
        long[] subMatrixArray = new long[length];
        for (int i = 0; i < length; i++) {
            subMatrixArray[i] = duplicate[i];
        }
        return new Matrix(
                subMatrixArray, n, (byte) subMatrixArray.length, base
        );
    }

    @Override
    public void printParameters() {
        System.out.println("The value of k (rows) is:\t" + k);
        System.out.println("The value of n (cols) is:\t" + n);
        System.out.println("The value of base \t  is:\t" + base);
    }

    @Override
    public void printMatrix() {
        printMatrixEngine(
                matrixArray, n, base, " ", false, Style.DECIMAL, true
        );
    }

    @Override
    public void printMatrix(
            boolean addBrackets,
            Style style,
            boolean showSize
    ) {
        printMatrixEngine(
                matrixArray, n, base, " ", addBrackets, style, showSize
        );
    }

    @Override
    public void printMatrix(
            String delimiter,
            boolean addBrackets,
            Style style,
            boolean showSize
    ) {
        printMatrixEngine(
                matrixArray,
                n,
                base,
                delimiter,
                addBrackets,
                style,
                showSize
        );
    }

    @Override
    public void printMatrix(
            long[] matrixArray,
            byte n,
            byte base,
            String delimiter,
            boolean addBrackets,
            Style style,
            boolean showSize

    ) {
        printMatrixEngine(
                matrixArray,
                n,
                base,
                delimiter,
                addBrackets,
                style,
                showSize
        );
    }

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
    private void printMatrixEngine(
            long[] matrixArray,
            byte n,
            byte base,
            String delimiter,
            boolean addBrackets,
            Style style,
            boolean showSize
    ) {
        MatrixPrinter mp = new MatrixPrinter(
                getMatrixArrayCopy(), this.n, k, this.base, style
        );
        mp.printMatrix(" ", addBrackets, showSize);
    }

    @Override
    public long[] getMatrixArray() {
        return matrixArray;
    }

    @Override
    public boolean isInvertible(Matrix m) {
        return isInvertibleEngine(m);
    }

    @Override
    public boolean containsZeroRow() {
        for (int i = 0; i < matrixArray.length; i++) {
            if (matrixArray[i] == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInvertible() {
        return isInvertibleEngine(this);
    }

    /**
     * Returns {@code true} if the specified matrix is invertible (<i>i.e.,
     * </i> the determinant of the matrix is <em>not</em> \(0\)), {@code false}
     * if the determinant <em>is</em> \(0\).
     *
     * @param m the matrix to find the determinant of
     * @return {@code true} if the specified matrix is invertible (<i>i.e.,
     * </i> the determinant of is not \(0\)), {@code false} otherwise
     */
    private boolean isInvertibleEngine(Matrix m) {
        return getDeterminant(m) != 0;
    }

    @Override
    public byte getBase() {
        return base;
    }

    @Override
    public byte getN() {
        return n;
    }

    @Override
    public byte getK() {
        return k;
    }

    @Override
    public Matrix getMatrix() {
        return this;
    }

    @Override
    public byte getCell(byte r, byte c) {
        return getCellEngine(matrixArray, r, c, n, base);
    }

    @Override
    public byte getCell(long[] matrixArray, byte r, byte c, byte n, byte base) {
        return getCellEngine(matrixArray, r, c, n, base);
    }

    @Override
    public byte getCell(Matrix m, byte r, byte c) {
        return getCellEngine(
                m.getMatrixArrayCopy(),
                r,
                c,
                m.getN(),
                m.getBase()
        );
    }

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
    private byte getCellEngine(
            long[] matrixArray,
            byte r,
            byte c,
            byte n,
            byte base
    ) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        //find the maximum number of columns (62 for base 2 or 30 for base 4)
        byte maxCol = 0;
        if (base == 2) {
            maxCol = 62;
        } else if (base == 4) {
            maxCol = 30;
        }

        //check if the index of the column is valid
        if (!Functions.isValidColIndex(c, maxCol)) {
            throw new InvalidColIndexException(c, maxCol, base);
        }
        //check if index of the row is valid
        if (!Functions.isValidRowIndex(r, (byte) matrixArray.length)) {
            throw new InvalidRowIndexException(
                    r,
                    (byte) matrixArray.length
            );
        }
        if (n < c) {
            System.out.println("Warning from getCellEngine method, the " +
                                       "column which is " + c + " we are trying to access " +
                                       "is larger than the " + "word length n which is " + n +
                                       ".");
        }
//        long result = matrixArray[r];
//        long mask;
//        if (base == 2) {
//            result = (byte) ((matrixArray[r] >>> c) & 0b1);
//        } else if (base == 4) {
//            result = (result >>> 2 * (n - c - 1)) & 0b11;
//        }
//        return (byte) result;
        long result = 0;
        long mask;
        if (base == 2) {
            mask = 0b1;
            mask = mask << c;
            result = matrixArray[r] & mask;//cell with the right-padding zeros
            result = result >> c;//actual result to be returned
            //result = (byte) ((matrixArray[row] >>> column) & 0b1);
        } else if (base == 4) {
            mask = 0b11;
            mask = mask << 2 * (n - c - 1);
            result = matrixArray[r] & mask;//cell with the right-padding zeros
            result = result >>> 2 * (n - c - 1);//actual result to return
        }
        return (byte) result;
    }

    @Override
    public long getRow(byte index) {
        return getRowEngine(matrixArray, index);
    }

    @Override
    public long getRow(long[] arrayMatrix, byte index) {
        return getRowEngine(arrayMatrix, index);
    }

    @Override
    public long getRow(Matrix m, byte index) {
        return getRowEngine(m.getMatrixArrayCopy(), index);
    }

    /**
     * Returns a row in the matrix array specified based on the index specified.
     *
     * @param matrixArray the matrix array to obtain the row from
     * @param index       the index of the row (zero-based) in the matrix
     * @return the row in the matrix array specified based on the index
     * specified
     */
    private long getRowEngine(long[] matrixArray, byte index) {
        if (!Functions.isValidRowIndex(index, (byte) matrixArray.length)) {
            throw new InvalidRowIndexException(
                    index,
                    (byte) matrixArray.length
            );
        }
        return matrixArray[index];
    }

    @Override
    public byte[] getColumn(byte index) {
        return getColumnEngine(matrixArray, n, index, base);
    }

    @Override
    public byte[] getColumn(long[] matrixArray, byte n, byte index, byte base) {
        return getColumnEngine(matrixArray, n, index, base);
    }

    @Override
    public byte[] getColumn(Matrix m, byte index) {
        return getColumnEngine(
                m.getMatrixArrayCopy(),
                m.getN(),
                index,
                m.getBase()
        );
    }

    /**
     * Returns a column in the matrix array specified based on the index
     * specified.
     *
     * @param matrixArray the matrix array to obtain the column from
     * @param n           the number of columns in the matrix array which is
     *                    the same as the length of code \(\mathsf{C}\)
     * @param column      the index of the column (zero-based) in the matrix
     * @param base        the base of the matrix array (could be either \(2\)
     *                    or \(4\))
     * @return the column in the matrix array specified based on the index
     * specified
     */
    private byte[] getColumnEngine(
            long[] matrixArray,
            byte n,
            byte column,
            byte base
    ) {
        byte maxCol = 0;
        if (base == 2) {
            maxCol = 62;
        } else if (base == 4) {
            maxCol = 30;
        }
        //check if index of the column is valid which is (both inclusive)
        //between 0 and 61 for base 2 or 0 and 29 for base 4
        if (!Functions.isValidColIndex(column, maxCol)) {
            throw new InvalidColIndexException(column, maxCol, base);
        }
        byte[] result = new byte[matrixArray.length];
        for (byte i = 0; i < matrixArray.length; i++) {
            result[i] = getCell(matrixArray, i, column, n, base);
        }
        return result;
    }

    @Override
    public void setMatrixArray(
            long[] matrixArray,
            byte n,
            byte k,
            boolean deepCopy
    ) {
        if (n != this.n || k != this.k) {
            System.out.println("Matrix.setMatrixArray: the array passed " +
                                       "doesn't match the current array's dimension in terms of" +
                                       " n and k. The required n value is " + this.n + " but " +
                                       "received passed as " + n + ". The required k value is " +
                                       this.k + "but received " + k + ". No changes have been " +
                                       "taken place.");
        } else {
            if (!deepCopy) {
                this.matrixArray = matrixArray;
            } else {
                long[] clonedMatrix = new long[matrixArray.length];
                for (byte i = 0; i < matrixArray.length; i++) {
                    clonedMatrix[i] = matrixArray[i];
                }
                this.matrixArray = clonedMatrix;
            }
        }
    }

    @Override
    public void setRow(byte index, long newRow) {
        setRowEngine(matrixArray, index, newRow);
    }

    @Override
    public void setRow(long[] matrixArray, byte index, long newRow) {
        setRowEngine(matrixArray, index, newRow);

    }

    @Override
    public void setRow(Matrix m, byte index, long newRow) {
        setRowEngine(m.getMatrixArrayCopy(), index, newRow);
    }

    /**
     * Sets a specific row based on the specified matrix array, row index and
     * new row vector.
     *
     * @param matrixArray the matrix array to set the row in
     * @param index       the index of the row in the matrix array to be
     *                    altered (zero-based)
     * @param newRow      the value of the new row to set as
     */
    private void setRowEngine(long[] matrixArray, byte index, long newRow) {
        //check if index of the row is valid
        if (!Functions.isValidRowIndex(index, (byte) matrixArray.length)) {
            throw new InvalidRowIndexException(
                    index, (byte) matrixArray.length
            );
        }
        matrixArray[index] = newRow;
    }

    @Override
    public void setCell(byte row, byte column, byte value) {
        setCellEngine(matrixArray, row, column, value, n, base);
    }

    @Override
    public void setCell(
            long[] matrixArray,
            byte r,
            byte c,
            byte value,
            byte n,
            byte base
    ) {
        setCellEngine(matrixArray, r, c, value, n, base);
    }

    @Override
    public void setCell(Matrix m, byte row, byte column, byte value) {
        setCellEngine(
                m.getMatrixArrayCopy(),
                row,
                column,
                value,
                m.getN(),
                m.getBase()
        );
    }

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
     * @param base        the base of matrix array (could be either \(2\) or
     *                    \(4\))
     */
    private void setCellEngine(
            long[] matrixArray,
            byte r,
            byte c,
            byte value,
            byte n,
            byte base
    ) {
        byte maxCol = 0;
        byte shift = 0;
        long mask = 0;
        //ensure the column contains valid digits according to the base.
        if (!Functions.isValidDigit(value, base)) {
            throw new InvalidDigitException(value, base);
        }

        if (base == 2) {
            maxCol = 62;
            shift = c;
            mask = 0b1L << shift;
        } else if (base == 4) {
            maxCol = 30;
            shift = (byte) (2 * (n - c - 1));
            mask = 0b11L << shift;
        }
        //check if index of the column is valid which is (both inclusive)
        //between 0 and 61 for base 2 or 0 and 29 for base 4
        if (!Functions.isValidColIndex(c, maxCol)) {
            throw new InvalidColIndexException(c, maxCol, base);
        }

        matrixArray[r] &= ~(mask);//reset column
        long digit = value;//the new digit to be placed
        digit = digit << shift;//shift digit to the correct position
        matrixArray[r] = matrixArray[r] | digit;//insert it in the row.
    }

    @Override
    public void setColumn(byte columnIndex, byte[] newColumn) {
        setColumnEngine(matrixArray, newColumn, columnIndex, base);
    }

    @Override
    public void setColumn(
            long[] matrixArray,
            byte columnIndex,
            byte[] newColumn,
            byte base
    ) {
        setColumnEngine(matrixArray, newColumn, columnIndex, base);
    }

    @Override
    public void setColumn(Matrix m, byte index, byte[] newColumn) {
        setColumnEngine(
                m.getMatrixArrayCopy(),
                newColumn,
                index,
                m.getBase()
        );
    }

    /**
     * Sets a specific column based on the specified matrix array, column index
     * and new column vector.
     *
     * @param matrixArray the matrix array to set the column in
     * @param index       the index of the column in the matrix array to be
     *                    altered (zero-based)
     * @param newColumn   the value of the new column to set as
     * @param base        the base of the matrix array (could be either \(2\)
     *                    or \(4\))
     */
    private void setColumnEngine(
            long[] matrixArray,
            byte[] newColumn,
            byte index,
            byte base
    ) {
        byte maxCol = 0;
        //ensure the column contains valid digits according to the base.
        for (int i = 0; i < newColumn.length; i++) {
            if (!Functions.isValidDigit(newColumn[i], base)) {
                throw new InvalidDigitException(newColumn[i], base);
            }
        }
        if (base == 2) {
            maxCol = 61;
        } else if (base == 4) {
            maxCol = 29;
        }
        //check if index of the column is valid which is (both inclusive)
        //between 0 and 61 for base 2 or 0 and 29 for base 4
        if (!Functions.isValidColIndex(index, maxCol)) {
            throw new InvalidColIndexException(index, maxCol, base);
        }

        //check if the column passed matches the dimension of the
        //matrix (i.e., matches the number of rows in the matrix).
        if (!Functions.isValidSettingColumnInMatrix(
                (byte) newColumn.length,
                (byte) matrixArray.length
        )) {
            throw new InvalidColumnDimensionToSetException(
                    (byte) matrixArray.length,
                    (byte) newColumn.length
            );
        }
        for (byte i = 0; i < matrixArray.length; i++) {
            setCell(i, index, newColumn[i]);
        }
    }

    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running the \"Matrix\" class...");
//        //generate a random matrix array
//        byte minN = 7;
//        byte maxN = 7;
//        byte minK = 4;
//        byte maxK = 4;
//        RandomMatrixGenerator rmg = new RandomMatrixGenerator(
//                minN, maxN, minK, maxK, (byte) 4
//        );
//        //convert matrix array to a Matrix object
//        Matrix g = new Matrix(
//                rmg.getMatrixArrayClone(), rmg.getN(), rmg.getK(), rmg.getBase()
//        );
//        //print the matrix using all possible styles
//        System.out.println("Printing matrix using binary style:");
//        g.printMatrix(" ", false, Style.BINARY, false);
//        System.out.println();
//        System.out.println("Printing matrix using decimal style:");
//        g.printMatrix(" ", false, Style.DECIMAL, true);
//        System.out.println();
//        System.out.println("Printing matrix using quaternary style:");
//        g.printMatrix(" ", true, Style.QUATERNARY, false);
//        System.out.println();
//        System.out.println("Printing matrix using LaTeX style:");
//        g.printMatrix(" ", true, Style.LATEX, true);
//
//        System.out.println("Transposing the top row of the matrix:");
//        byte[] col = g.transposeRowToCol((byte) 0);
//        for (int i = 0; i < col.length; i++) {
//            System.out.println(col[i]);
//        }
//
//        System.out.println("Transposing the far-left column of the matrix:");
//        long row = g.transposeColToRow((byte) 0);
//        System.out.println(MatrixPrinter.vectorAsString(
//                row, g.getK(), g.getN(), " ", Style.DECIMAL
//        ));
//        System.out.println();
//        System.out.println("Transposing the matrix:");
//        Matrix transpose = g.transpose();
//        transpose.printMatrix(" ", true, Style.DECIMAL, true);
//        System.out.println();
//        System.out.println("Hermitian transposing the matrix:");
//        Matrix hermitianTranspose = g.hermitianTranspose();
//        hermitianTranspose.printMatrix(" ", true, Style.DECIMAL, true);
//        System.out.println("The \"Matrix\" class completed.");
//    }
}