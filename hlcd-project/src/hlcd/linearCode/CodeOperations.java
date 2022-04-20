package hlcd.linearCode;

import hlcd.enums.Style;
import hlcd.operations.LongArray;
import hlcd.operations.Matrix;
import hlcd.parameters.CodeValidatorParameters;

/**
 * The operations a mathematical linear code.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 10th, 2022)
 * @since 1.8
 */
public interface CodeOperations {

    /**
     * Prints the current code parameters on screen. More precisely, it will
     * print the generator \(n\), \(k\), \(d\), base values, number of linear
     * combinations, the combinations themselves and the vectors used in the
     * generator matrix and other properties.
     *
     * @see hlcd.parameters.CodeParameters
     */
    public void printCodeParameters();

    /**
     * Returns the generator matrix \(G\).
     *
     * @return the generator matrix \(G\)
     * @see CodeOperations#getGeneratorMatrixCopy()
     */
    public Matrix getGeneratorMatrix();

    /**
     * Returns the matrix \(G\) as a brand-new copy (deep copy).
     * <p>
     * NOT TESTED YET!
     *
     * @return the matrix \(G\) as a brand-new copy (deep copy)
     * @see CodeOperations#getGeneratorMatrix()
     */
    public Matrix getGeneratorMatrixCopy();

    /**
     * Returns the length of each codeword in a \(\left[n, \, k, \, d\right]_{
     * base}\) code. For a binary code, \(1 \lt n \leq 62\) and for a
     * quaternary code, \(1 \lt n \leq 30\).
     *
     * @return the length of each codeword in the
     * \(\left[n, \, k, \, d\right]_{base}\) code
     */
    public byte getN();

    /**
     * Returns the dimension of the \(\left[n, \, k, \, d\right]_{base}\) code.
     * For a binary code, \(1 \lt k \leq 62\) and for a quaternary code,
     * \(1 \lt k \leq 30\).
     *
     * @return the dimension of the \(\left[n, \, k, \, d\right]_{base}\) code
     */
    public byte getK();

    /**
     * Returns the minimum distance of the \(\left[n, \, k, \, d\right]_{base}\)
     * code. It <em>should</em> be less than \(n\).
     *
     * @return the minimum distance of the \(\left[n, \, k, \, d\right]_{base}\)
     * code
     */
    public byte getD();

    /**
     * Returns the base of the \(\left[n, \, k, \, d\right]_{base}\) code. It
     * could either be \(2\) or \(4\).
     *
     * @return the base of the \(\left[n, \, k, \, d\right]_{base}\) code which
     * could either be \(2\) or \(4\)
     */
    public byte getBase();

    /**
     * Returns the minimum weight of the right-side of the generator matrix
     * \(G\) that is in standard form, which is \(d - 1\). In the case
     * where it is not in standard form, it should return \(d\).
     *
     * @return returns the minimum weight of the right-side of the generator
     * matrix \(G\) that is in standard form, which is \(d - 1\). In the case
     * where it is not in standard form, it should return \(d\).
     */
    public byte getRHSWeight();

    /**
     * Prints the elements in the combination array on screen with the style
     * type specified.
     *
     * @param delimiter the delimiter between each digit
     * @param style     the style format which could either be binary,
     *                  quaternary, decimal or \(\LaTeX\)
     */
    public void printCombinations(String delimiter, Style style);

    /**
     * Prints the current matrix on console with brackets surrounding the
     * matrix, a single space between columns, each digit is written in base
     * 10 as well as the size of the matrix.
     */
    public void printGeneratorMatrix();

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
    public void printGeneratorMatrix(
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
    public void printGeneratorMatrix(
            String delimiter,
            boolean addBrackets,
            Style style,
            boolean showSize
    );

    /**
     * The linear combinations of the codewords in the code.
     *
     * @return the linear combinations of the codewords in the code
     */
    public LongArray getCombinations();

    /**
     * Returns {@code true} if the code is Hermitian linear complementary
     * code, {@code false} otherwise. This property can be true when the code
     * is quaternary. Note that this program dealt with only Hermitian
     * quaternary codes but can be easily extended to traditional quaternary
     * codes.
     *
     * @return returns {@code true} if the code is Hermitian linear
     * complementary code, {@code false} otherwise
     */
    public boolean isHLCD();

    /**
     * Returns {@code true} if the identity matrix is appended to the
     * left-side of the generator matrix, {@code false} otherwise.
     *
     * @return returns {@code true} if the identity matrix is appended
     * to the left-side of the generator matrix, {@code false} otherwise
     */
    public boolean isIdentityAppended();

    /**
     * Returns {@code true} if multithreading is used when calculating
     * if some vector \(v\) is an appropriate codeword to be added to the
     * generator matrix \(G\), {@code false} otherwise. This deals with
     * checking and ensuring that \(v\), \(\omega v\) and
     * \(\overline{\omega} v\) are linearly independent with all linear
     * combinations that are currently stored. This is coded but not tested.
     *
     * @return returns {@code true} if multithreading is used when
     * calculating if some vector \(v\) is an appropriate codeword to be added
     * to the generator matrix \(G\), {@code false} otherwise
     */
    public boolean isMultithreaded();

    /**
     * Returns {@code true} if a specific form of cut down in the space search
     * has been applied, {@code false} otherwise. This should always be
     * {@code true}. The explanation of this form is found on <a href=
     * "https://dr.library.brocku.ca/bitstream/handle/10464/15405/Maysara_Al_Jumaily_MSc_Thesis.pdf#page=91&zoom=100,144,601"
     * target="_blank">page 91 of the thesis</a>.
     *
     * @return returns {@code true} if a specific form of cut down in the space
     * search has been applied, {@code false} otherwise
     */
    public boolean isCodewordRestrictionApplied();

    /**
     * Returns the code statistics which includes the total time taken,
     * parameters of the code and parameters of the validator used.
     *
     * @return the statistics of the code
     */
    public CodeStatistics getCodeStatistics();

    /**
     * Returns the code validator parameters of the code.
     *
     * @return the code validator parameters of the code
     */
    public CodeValidatorParameters getCodeValidatorParameters();
}
