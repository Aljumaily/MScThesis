package hlcd.parameters;

import hlcd.exceptions.InvalidBaseException;
import hlcd.operations.Functions;

import java.io.Serializable;

/**
 * Stores the parameters of a code when being created. This includes the
 * length, dimension, minimum distance, if it is HLCD should the
 * restriction of the code generation be applied, should the identity matrix
 * be appended. The multithreading option is implemented but not tested
 * (hence not supported in this program). The restriction is explained on
 * <a href=
 * "https://dr.library.brocku.ca/bitstream/handle/10464/15405/Maysara_Al_Jumaily_MSc_Thesis.pdf#page=91&zoom=100,144,601"
 * target="_blank">page 91 of the thesis</a>.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 19th, 2022)
 * @see <a href=
 * "https://dr.library.brocku.ca/bitstream/handle/10464/15405/Maysara_Al_Jumaily_MSc_Thesis.pdf"
 * target="_blank">M.Sc. thesis</a>.
 * @see hlcd.operations.VectorGenerator
 * @since 1.8
 */
public class CodeParameters implements Serializable {
    /**
     * The length of each codeword in the code.
     */
    private byte n;

    /**
     * The dimension of the code (and the number of rows in the generator
     * matrix).
     */
    private byte k;

    /**
     * The minimum distance between any two codewords in code.
     */
    private byte d;

    /**
     * The minimum weight of far-right n - k values.
     */
    private byte rhsWeight;

    /**
     * The base of the code which can be either 2 or 4.
     */
    private byte base;

    /**
     * Whether the code is Hermitian LCD.
     */
    private boolean isHlcd;

    /**
     * Should multithreading be used.
     */
    private boolean isMultithreaded;

    /**
     * Should the identity matrix be appended (<i>i.e.</i>, the matrix is in
     * standard form).
     */
    private boolean appendIdentity;

    /**
     * Should the codeword restriction be applied to <em>significantly</em> cut
     * down the search space.
     */
    private boolean restrictCodewordGeneration;

    /**
     * Initializes the basic parameters state of a code.
     *
     * @param n    the length of code
     * @param k    the dimension of the code
     * @param d    the minimum distance of the code
     * @param base the base of the code which should be
     *             \(2\) or \(4\)
     */
    public CodeParameters(byte n, byte k, byte d, byte base) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        //TODO: test the other constructor and other values such as n, k, etc.
        this.n = n;
        this.k = k;
        this.d = d;
        rhsWeight = (byte) (d - 1);
        this.base = base;
        isHlcd = true;
        isMultithreaded = false;
        appendIdentity = true;
        restrictCodewordGeneration = true;
    }

    /**
     * Initializes the basic state of a code.
     *
     * @param n                          the length of code
     * @param k                          the dimension of the code
     * @param d                          the minimum distance of the code
     * @param rhsWeight                  the weight of the right-hand-side of
     *                                   the matrix. In the case where the
     *                                   identity matrix is not appended,
     *                                   then it should be \(d\).
     *                                   Otherwise, it should be \(d - 1\).
     * @param base                       the base of the code which should be
     *                                   \(2\) or \(4\)
     * @param isHLCD                     if the quaternary code should be of
     *                                   type Hermitian linear complementary
     *                                   code
     * @param isMultithreaded            should multithreading be used for
     *                                   faster computation time
     * @param appendIdentity             should the identity matrix be added
     *                                   on the left-hand-side to cut down on
     *                                   the search space
     * @param restrictCodewordGeneration Should a specific form of cut down in
     *                                   of search space be applied. This
     *                                   should always be {@code true}.
     *                                   The explanation of this form is
     *                                   found on <a href=
     *                                   "https://dr.library.brocku.ca/bitstream/handle/10464/15405/Maysara_Al_Jumaily_MSc_Thesis.pdf#page=91&zoom=100,144,601"
     *                                   target="_blank">page 91 of the
     *                                   thesis</a>.
     */
    public CodeParameters(
            byte n,
            byte k,
            byte d,
            byte rhsWeight,
            byte base,
            boolean isHLCD,
            boolean isMultithreaded,
            boolean appendIdentity,
            boolean restrictCodewordGeneration
    ) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        this.n = n;
        this.k = k;
        this.d = d;
        this.rhsWeight = rhsWeight;
        this.base = base;
        isHlcd = isHLCD;
        this.isMultithreaded = isMultithreaded;
        this.appendIdentity = appendIdentity;
        this.restrictCodewordGeneration = restrictCodewordGeneration;
    }

    /**
     * Returns the length of each codeword in a \(\left[n, \, k, \, d\right]_{
     * base}\) code. For a binary code, \(0 \lt n \leq 62\) and for a
     * quaternary code, \(0 \lt n \leq 30\).
     *
     * @return the length of each codeword in a \(\left[n, \, k, \, d\right]_{
     * base}\) code.
     */
    public byte getN() {
        return n;
    }

    /**
     * Returns the dimension of the \(\left[n, \, k, \, d\right]_{base}\) code.
     * For a binary code, \(0 \lt k \leq 62\) and for a quaternary code, \(0 \lt
     * k \leq 30\).
     *
     * @return the dimension of the \(\left[n, \, k, \, d\right]_{base}\) code
     */
    public byte getK() {
        return k;
    }

    /**
     * Returns the minimum distance of the \(\left[n, \, k, \, d\right]_{base}\)
     * code. It <em>should</em> be less than \(n\).
     *
     * @return the minimum distance of the \(\left[n, \, k, \, d\right]_{base}\)
     * code
     */
    public byte getD() {
        return d;
    }

    /**
     * Returns the base of the \(\left[n, \, k, \, d\right]_{base}\) code. It
     * could either be \(2\) or \(4\).
     *
     * @return the base of the \(\left[n, \, k, \, d\right]_{base}\) code. It
     * could either be \(2\) or \(4\).
     */
    public byte getBase() {
        return base;
    }

    /**
     * Returns the minimum weight of the right-side of the generator matrix \(G\)
     * that is in standard form which should be \(d - 1\). In the case where it is
     * not in standard form, it should return \(d\).
     *
     * @return returns the minimum weight of the right-side of the generator
     * matrix \(G\) that is in standard form which should be \(d - 1\). In the case
     * where it is not in standard form, it should return \(d\).
     */
    public byte getRHSWeight() {
        return rhsWeight;
    }

    /**
     * Returns {@code true} if the code is Hermitian linear complementary
     * code, {@code false} otherwise. This property <em>should</em> be true
     * only if the code is quaternary. A traditional non-Hermitian quaternary
     * code is not implemented here but can be easily implemented.
     *
     * @return returns {@code true} if the code is Hermitian linear
     * complementary code, {@code false} otherwise
     */
    public boolean isHLCD() {
        return isHlcd;
    }

    /**
     * Returns {@code true} if the identity matrix is appended to the
     * left-side of the generator matrix, {@code false} otherwise.
     *
     * @return returns {@code true} if the identity matrix is appended
     * to the left-side of the generator matrix, {@code false} otherwise
     */
    public boolean isIdentityAppended() {
        return appendIdentity;
    }

    /**
     * Returns {@code true} if multithreading is used when calculating
     * if some vector \(v\) is an appropriate codeword to be added to the
     * generator matrix \(G\), {@code false} otherwise. This deals with
     * checking and ensuring that \(v\), \(\omega v\) and
     * \(\overline{\omega} v\) are linearly independent with all linear
     * combinations that are currently stored. It has been implemented but
     * not test, hence, not supported yet.
     *
     * @return returns {@code true} if multithreading is used when
     * calculating if some vector \(v\) is an appropriate codeword to be added
     * to the generator matrix \(G\), {@code false} otherwise
     */
    public boolean isMultithreaded() {
        return isMultithreaded;
    }

    /**
     * Returns {@code true} if a specific form of cut down in of search
     * space be applied, {@code false} otherwise. This should always be
     * {@code true}. The explanation of this form is found on
     * <a href="https://dr.library.brocku.ca/bitstream/handle/10464/15405/Maysara_Al_Jumaily_MSc_Thesis.pdf#page=91&zoom=100,144,600"
     * target="_blank">Page 91 of the M.Sc. thesis</a>
     *
     * @return returns {@code true} if a specific form of cut down in of
     * search space be applied, {@code false} otherwise
     */
    public boolean isCodewordRestrictionApplied() {
        return restrictCodewordGeneration;
    }

    /**
     * The text representation of the parameters used in the form of <em>
     * (n, k , d)_H_base</em> (the <em>H</em> denotes it is Hermitian,
     * ignored otherwise). The other parameters, such is generating all
     * matrices, is code restriction applied, is the identity added, is
     * multithreading used and the right-hand side weight of vectors in \(G\)
     * are not a part of this.
     *
     * @return The text representation of the parameters used.
     */
    public String toString() {
        String isHLCD = (isHlcd) ? "H" : "";
        //String generateAllMatrices = (GENERATE_ALL_MATRICES) ? "_All" : "";
        String ns = String.format("%02d", n);
        String ks = String.format("%02d", k);
        String ds = String.format("%02d", d);
        return ns + "_" + ks + "_" + ds + isHLCD + "_" + base;
    }
}
