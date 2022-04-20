package hlcd.operations;

import hlcd.exceptions.InvalidBaseException;
import hlcd.exceptions.InvalidDigitException;

/**
 * A way to perform mathematical operations in \(\mathbb{F}_4\). It contains
 * methods for performing basic vector operations such as addition, scalar
 * multiplication, inner product, Hermitian inner product and converting a
 * vector to its Hermitian equivalent.
 * <p>
 * The Hermitian inner product of two vectors \(a\) and \(b\) is defined as
 * \(
 * \langle a, \, b \rangle_H =
 * a_1^\dagger \cdot b_1 +
 * a_2^\dagger \cdot b_2 +
 * \ldots +
 * a_n^\dagger \cdot b_n
 * \), where \(a_i^\dagger\) is the complex conjugation of \(a_i\).
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 24th, 2022)
 * @see GF4Operations#getHermitianVector(long)
 * @since 1.8
 */
public class GF4Operations {
    private final byte BASE = 4;
    private HammingWeight hammingWeight;

    /**
     * Creates a calculator that perform calculations in \(\mathbb{F}_4\).
     */
    public GF4Operations() {
        hammingWeight = new HammingWeight(BASE);
    }

    /**
     * Returns the inner product of two vectors.
     *
     * @param v1 the first operand
     * @param v2 the second operand
     * @return the inner product between two vectors
     */
    public byte innerProduct(long v1, long v2) {
        long v = multiply(v1, v2);
        long a = v & Functions.ONE;
        long b = (v & Functions.TWO) >>> 1;
        long c = a ^ b;
        long ones = v & c;
        long twos = c ^ ones;
        long threes = a & b;
        long resultOne = hammingWeight.getWeight(ones) % 2;
        long resultTwo = hammingWeight.getWeight(twos) % 2 << 1;
        long resultThreeTemp = hammingWeight.getWeight(threes) % 2;
        long resultThree = (resultThreeTemp << 1) | resultThreeTemp;
        long OnePlusTwo = resultOne | resultTwo;
        return (byte) (OnePlusTwo ^ resultThree);
    }

    /**
     * Returns the Hermitian inner product of two vectors. The Hermitian
     * property will be applied on vector {@code v1}.
     *
     * @param v1 the first operand where the Hermitian property is applied on
     * @param v2 the second operand
     * @return the Hermitian inner product between two vectors
     * @see GF4Operations#getHermitianVector
     */
    public byte hermitianInnerProduct(long v1, long v2) {
        long v = multiply(v1, getHermitianVector(v2));
        long a = v & Functions.ONE;
        long b = (v & Functions.TWO) >>> 1;
        long c = a ^ b;
        long ones = v & c;
        long twos = c ^ ones;
        long threes = a & b;
        long resultOne = hammingWeight.getWeight(ones) % 2;
        long resultTwo = hammingWeight.getWeight(twos) % 2 << 1;
        long resultThreeTemp = hammingWeight.getWeight(threes) % 2;
        long resultThree = (resultThreeTemp << 1) | resultThreeTemp;
        long OnePlusTwo = resultOne | resultTwo;
        return (byte) (OnePlusTwo ^ resultThree);
    }

    /**
     * Returns the Hermitian equivalent of the vector passed. For each
     * element \(x \in \mathbb{F}_4\), the complex conjugation (denoted as
     * \(x^\dagger\)) is defined as:
     * <table border= "1" style='text-align:center; border-collapse:collapse'>
     *     <caption>Complex conjugation of \(\mathbb{F}_4\) elements</caption>
     *     <tr>
     *         <th style='padding: 4px'>\(x\)</th>
     *         <th style='padding: 4px'>\(x^\dagger\)</th>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(0\)</td>
     *         <td style='padding: 4px'>\(0\)</td>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(1\)</td>
     *         <td style='padding: 4px'>\(1\)</td>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(\omega\)</td>
     *         <td style='padding: 4px'>\(\overline{\omega}\)</td>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(\overline{\omega}\)</td>
     *         <td style='padding: 4px'>\(\omega\)</td>
     *     </tr>
     * </table>
     *
     * @param v the vector to find its Hermitian equivalent
     * @return the Hermitian equivalent of the vector passed
     */
    public long getHermitianVector(long v) {
        long a = v & Functions.ONE;
        long b = (v & Functions.TWO) >>> 1;
        long c = a ^ b;
        long onesOnly = v & c;
        long twosAndThreesOnly = v ^ onesOnly;
        //the next line coverts all 2's and 3's to 2's.
        long temp = twosAndThreesOnly & Functions.TWO;
        return (twosAndThreesOnly ^ (temp >>> 1)) | onesOnly;
    }

    /**
     * Returns the multiplication of two quaternary row vectors. The
     * multiplication is done element-wise.
     *
     * @param v1 the first operand to be multiplied
     * @param v2 the second operand to be multiplied
     * @return the two vectors multiplied
     */
    public long multiply(long v1, long v2) {
        long a = (v1 >>> 1) & Functions.ONE;
        long b = (v2 >>> 1) & Functions.ONE;
        return (((v1 & b) ^ (v2 & a)) << 1) ^ (a & b) ^ (v1 & v2);
    }

    /**
     * Multiplies a vector by \(0\) which yields \(0\).
     *
     * @param v the vector that will be multiplied
     * @return the vector multiplied by \(0\)
     */
    public long multiplyByScalarZero(long v) {
        return 0;
    }

    /**
     * Multiplies a vector by \(1\) which yields the vector itself.
     *
     * @param v the vector that will be multiplied
     * @return the vector multiplied by \(1\)
     */
    public long multiplyByScalarOne(long v) {
        return v;
    }

    /**
     * Multiplies each element \(x \in \mathbb{F}_4\) in the vector specified
     * by \(\omega\). It will transform each digit to the following:
     * <table border= "1" style='text-align:center; border-collapse:collapse'>
     *     <caption>Multiplying \(\mathbb{F}_4\) elements by \(\omega\)</caption>
     *     <tr>
     *         <th style='padding: 4px'>\(x\)</th>
     *         <th style='padding: 4px'>\(x \cdot \omega\)</th>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(0\)</td>
     *         <td style='padding: 4px'>\(0\)</td>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(1\)</td>
     *         <td style='padding: 4px'>\(\omega\)</td>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(\omega\)</td>
     *         <td style='padding: 4px'>\(\overline{\omega}\)</td>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(\overline{\omega}\)</td>
     *         <td style='padding: 4px'>\(1\)</td>
     *     </tr>
     * </table>
     *
     * @param v the vector that will be multiplied
     * @return the vector multiplied by \(\omega\)
     */
    public long multiplyByScalarTwo(long v) {
        return multiply(v, Functions.TWO);
    }

    /**
     * Multiplies each element \(x \in \mathbb{F}_4\) in the vector specified by
     * \(\overline{\omega}\). It will transform each digit to the following:
     * <table border= "1" style='text-align:center; border-collapse:collapse'>
     *     <caption>
     *         Multiplying \(\mathbb{F}_4\) elements by \(\overline{\omega}\)
     *     </caption>
     *     <tr>
     *         <th style='padding: 4px'>\(x\)</th>
     *         <th style='padding: 4px'>\(x \cdot \overline{\omega}\)</th>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(0\)</td>
     *         <td style='padding: 4px'>\(0\)</td>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(1\)</td>
     *         <td style='padding: 4px'>\(\overline{\omega}\)</td>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(\omega\)</td>
     *         <td style='padding: 4px'>\(1\)</td>
     *     </tr>
     *     <tr>
     *         <td style='padding: 4px'>\(\overline{\omega}\)</td>
     *         <td style='padding: 4px'>\(\omega\)</td>
     *     </tr>
     * </table>
     *
     * @param v the vector that will be multiplied
     * @return the vector multiplied by \(\omega\)
     */
    public long multiplyByScalarThree(long v) {
        return multiply(v, Functions.ALL_ONES);
    }

    /**
     * Adds two vectors in \(\mathbb{F}_4\).
     *
     * @param v1 the first operand to be added
     * @param v2 the second operand to be added
     * @return the result of adding two vectors in \(\mathbb{F}_4\)
     */
    public long add(long v1, long v2) {
        return v1 ^ v2;
    }

    /**
     * Multiplies the vector specified by the digit specified. It is
     * suggested to use {@link GF4Operations#multiplyByScalarZero(long)},
     * {@link GF4Operations#multiplyByScalarOne(long)},
     * {@link GF4Operations#multiplyByScalarTwo(long)} and
     * {@link GF4Operations#multiplyByScalarThree(long)} instead of this method.
     *
     * @param v     the vector to multiply
     * @param digit the digit to multiply with
     * @param base  the base of the vector which could either by \(2\) or \(4\)
     * @return the vector multiplied by the digit
     */
    public long multiplyByScalar(long v, byte digit, byte base) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }

        //ensure the digit passed is a valid digit corresponding to the base.
        if (!Functions.isValidDigit(digit, base)) {
            throw new InvalidDigitException(digit, base);
        }

        switch (digit) {
            case 3:
                return multiply(v, Functions.ALL_ONES);
            case 2:
                return multiply(v, Functions.TWO);
            case 1:
                return v;
            default:
                return 0;
        }
    }
}
