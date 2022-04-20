package hlcd.operations;

import hlcd.exceptions.InvalidBaseException;
import hlcd.exceptions.InvalidColIndexException;
import hlcd.exceptions.InvalidIdentityRowIndexException;
import hlcd.exceptions.InvalidMinimumDistanceException;

/**
 * The generator of potential codewords in a sequential order. It uses the
 * method {@code getNextVector()} to return the next <em>valid</em>
 * vector. A valid vector is a vector that satisfies the minimum distance.
 * A vector is of type {@code long}, which is a 64-bit value. In general, the
 * vectors are going to be generated so that the first digit starts
 * far-right, <i>i.e.</i>, \(00...00\), \(00...01\), \(00...10\), \(00...11\),
 * etc.
 * <p>
 * Here, indices are zero based. The <em>important</em> thought is that the
 * zeroth column is the far-right column.
 * <p>
 * A <em>quaternary</em> digit is composed of a left digit and a right digit:
 * <table border= "1" style='text-align:center; border-collapse:collapse'>
 *     <caption>The representation of an quaternary digit</caption>
 *     <tr>
 *         <th style='padding: 4px'>Quaternary Digit</th>
 *         <th style='padding: 4px'>Left Digit</th>
 *         <th style='padding: 4px'>Right Digit</th>
 *     </tr>
 *     <tr>
 *         <td style='padding: 4px'>\(0_4 \, = \, 00_2\)</td>
 *         <td style='padding: 4px'>\(0\)</td>
 *         <td style='padding: 4px'>\(0\)</td>
 *     </tr>
 *     <tr>
 *         <td style='padding: 4px'>\(1_4 \, = \, 01_2\)</td>
 *         <td style='padding: 4px'>\(0\)</td>
 *         <td style='padding: 4px'>\(1\)</td>
 *     </tr>
 *     <tr>
 *         <td style='padding: 4px'>\(\omega_4 \, = \, 10_2\)</td>
 *         <td style='padding: 4px'>\(1\)</td>
 *         <td style='padding: 4px'>\(0\)</td>
 *     </tr>
 *     <tr>
 *         <td style='padding: 4px'>\(\overline{\omega}_4 \, = \, 11_2\)</td>
 *         <td style='padding: 4px'>\(1\)</td>
 *         <td style='padding: 4px'>\(1\)</td>
 *     </tr>
 * </table>
 * <p>
 * The \(i^{th}\) quaternary digit in a {@code long} datatype is given by:
 * {@code leftDigit = i * 2 + 1} and {@code rightDigit = i * 2}.
 * <p>
 * The implementation of this class <b>only</b> works for base \(4\) but can be
 * modified to also include base \(2\).
 * <p>
 * Having minimum distance to be {@code 0} or {@code 1} will generate the
 * exact set of valid vectors.
 * <p>
 * An important property in this class is {@code RESTRICT_GENERATION}. This
 * should always be {@code true} as it will cut down the search space by an
 * exponential factor when the base is \(4\). In addition, this requires to have
 * the property {@code ADD_IDENTITY} to be {@code true} as well. The basic
 * explanation is that the top row will be hardcoded so that it will have a
 * weight of \(d\) consisting of \(1\)'s. The subvector in \(I\) will have a
 * weight if \(1\) and the subvector in \(P\) will have \(d - 1\) as the
 * weight, assuming the generator matrix is in the following form: \(G =
 * \left[\begin{array} {&#64;{}c|c@{}} I &#38; P \end{array}\right]\). For each
 * row, other than the top row in \(P\), the left-most nonzero digit will be
 * \(1\) (not \(\omega\) nor \(\overline{\omega}\)). This construction
 * guarantees that all possible inequivalent matrices will be generated. An
 * explanation of how this is achieved can be found on page
 * <a href="https://arxiv.org/pdf/1908.00244.pdf#page=3&zoom=100,144,600"
 * target="_blank">page 3</a> of <i>Some optimal entanglement-assisted quantum
 * codes constructed from quaternary Hermitian linear complementary dual
 * codes</i> by Masaaki Harada (Dec 2019).
 * <p>
 * TODO: When {@code RESTRICT_GENERATION} is {@code true}, the minimum
 *  distance must be less than or equal to \(3\). Implement \(d = 2\).
 * <p>
 * TODO: test binary implementation (regrading everything)
 * <p>
 * TODO: test quaternary implementation when having both {@code
 *  RESTRICT_GENERATION} and {@code ADD_IDENTITY} as {@code false}.
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running VectorGenerator...");
 *     byte n = 20;
 *     byte k = 9;
 *     byte d = 9;
 *     byte minimumWeight = (byte) (d - 1);
 *     byte base = 4;
 *     boolean addIdentity = true;
 *     boolean restrictGeneration = true;
 *     long counter = 0;
 *     String ns = String.format("%" + 2 + "s", n + "");
 *     String ks = String.format("%" + 2 + "s", k + "");
 *     String ds = String.format("%" + 2 + "s", d + "");
 *     System.out.print("The code (" + ns + ", " + ks + ", " + ds + ") has ");
 *     VectorGenerator vg = new VectorGenerator(
 *         n, k, d, minimumWeight, base, addIdentity, restrictGeneration
 *     );
 *     while (true) {
 *         long vector = vg.getNextFullVector((byte) 0);
 *         if (vg.isCurrentSubvectorValid()) {
 *             counter++;
 *         } else {
 *             break;
 *         }
 *     }
 *     System.out.format("%,d%s\n", counter, " vectors to check.");
 *     System.out.println("VectorGenerator completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 1st, 2022)
 * @see Long
 * @see <a href="https://dr.library.brocku.ca/bitstream/handle/10464/15405/Maysara_Al_Jumaily_MSc_Thesis.pdf#page=91&zoom=100,144,600"
 * target="_blank">Page 91 of the M.Sc. thesis</a>
 * @see <a href="https://dr.library.brocku.ca/bitstream/handle/10464/15405/Maysara_Al_Jumaily_MSc_Thesis.pdf#page=93&zoom=100,144,550"
 * target="_blank">Page 93 of the M.Sc. thesis</a>
 * @see <a href="https://arxiv.org/pdf/1908.00244.pdf#page=3&zoom=100,144,600"
 * target="_blank">Masaaki Harada's paper (Dec 2019)</a>
 * @since 1.8
 */
public class VectorGenerator {
    private byte base;
    private byte n;
    private byte k;
    private byte d;
    /* The weight of a subvector in G that is in the matrix P when G is in the
    standard form of G = [I | P]. */
    private byte rhsWeight;//equals to D - 1

    private long subvector;//the current subvector
    private boolean hasStarted = false;//if vector generation has started
    private boolean restrictGeneration;
    private boolean appendIdentity;
    /* Whether the right side of the submatrix P's vector component is valid */
    private boolean isCurrentSubvectorValid = true;

    /* The value the current subvector reaches suggesting a reset is needed. For
     example, a reset means having the following subvector: [00 01 11 11 11 11]
    and a reset will transform it to [01 00 00 00 00 00]. Used in base 4 only */
    private long resetPoint;

    /* The next reset point which a subvector will eventually reach. Once a
    reset point is encountered, the value of nextResetPoint will be assigned
    to resetPoint and nextResetPoint will be shifted appropriately. */
    private long nextResetPoint;
    /* The total number of vectors (valid & invalid) that have been examined.
     When the vector restriction is applied, then some vectors will be
     skipped due to the nature of the "shortcut". These vectors will not be
     counted */
    private long totalVectorsExamined = 0;

    /**
     * Initializes the vector generator for the code. This particular
     * constructor is used with base 4 only.
     *
     * @param n                  the length of the code
     * @param k                  the dimension of the code
     * @param d                  the minimum distance of the code
     * @param rhsWeight          the Hamming weight of the subvector that is
     *                           <em>not</em> a part of the identity
     *                           submatrix. This must be \(d - 1\) assuming the
     *                           identity matrix is appended
     * @param base               the base of the code which could either be
     *                           \(2\) or \(4\)
     * @param appendIdentity     should the identity matrix be appended. This
     *                           should always be {@code true} as it will
     *                           significantly cut down the search space
     * @param restrictGeneration should a shortcut be used so that all the
     *                           inequivalent generator matrices be generated
     *                           This should always be {@code true} as it
     *                           will further cut down the search space
     */
    public VectorGenerator(
            byte n,
            byte k,
            byte d,
            byte rhsWeight,
            byte base,
            boolean appendIdentity,
            boolean restrictGeneration
    ) {

        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        if (base != 4) {
            throw new RuntimeException("Only base 4 is supported when calling" +
                                               " this constructor.");
        }
        if (!Functions.isValidMinimumDistance(d)) {
            throw new InvalidMinimumDistanceException(d);
        }
        this.n = n;
        this.k = k;
        this.d = d;
        this.base = base;
        this.rhsWeight = rhsWeight;
        this.appendIdentity = appendIdentity;
        subvector = 0;
        this.restrictGeneration = restrictGeneration;
        nextResetPoint = 0;

        //if restrict generation is true, ensure appendIdentity is always true
        if (this.restrictGeneration && this.appendIdentity == false) {
            this.appendIdentity = true;
            System.out.println(
                    "Note: appendIdentity is false and restrictGeneration is " +
                            "true. appendIdentity is now true as well."
            );
        }
        if (n - k >= rhsWeight) {
            if (restrictGeneration) {
                long ones = generateOnesOnRight(rhsWeight);
                subvector = ones;
                nextResetPoint = ones << 2;
                resetPoint = 1;
                for (int i = 0; i < rhsWeight - 1; i++) {
                    resetPoint = resetPoint << 2;
                    resetPoint = resetPoint | 0b11;
                }
            } else {
                subvector = 0;
                resetPoint = 0;
            }
        }
        long limit;
        if (appendIdentity) {
            limit = Functions.power(base, n - k);
        } else {
            limit = Functions.power(base, n);
        }

        if (subvector >= limit) {
            subvector = limit + 1;
            isCurrentSubvectorValid = false;
            hasStarted = false;
        }
    }

    /**
     * Initializes the vector generator for the code. This particular
     * constructor is used with base 4 only. This is used when a recursive
     * call has been made to find the next codeword to be placed in the
     * generator matrix.
     *
     * @param n                  the length of the code
     * @param k                  the dimension of the code
     * @param d                  the minimum distance of the code
     * @param rhsWeight          the Hamming weight of the subvector that is
     *                           <em>not</em> a part of the identity
     *                           submatrix. This must be \(d - 1\) assuming the
     *                           identity matrix is appended.
     * @param base               the base of the code which could either be
     *                           \(2\) or \(4\)
     * @param startingSubvector  the starting position of the subvector that
     *                           is <em>not</em> a part of the identity matrix
     * @param resetPoint         a binary value that will have a weight of
     *                           \(1\) where once reached, it denotes that
     *                           some vectors can be skipped (assuming that
     *                           the restriction generation property is on)
     * @param appendIdentity     should the identity matrix be appended. This
     *                           should always be {@code true} as it will
     *                           significantly cut down the search space
     * @param restrictGeneration should a shortcut be used so that all the
     *                           inequivalent generator matrices be generated
     *                           This should always be {@code true} as it
     *                           will further cut down the search space
     */
    public VectorGenerator(
            byte n,
            byte k,
            byte d,
            byte rhsWeight,
            byte base,
            long startingSubvector,
            long resetPoint,
            boolean appendIdentity,
            boolean restrictGeneration
    ) {
        //TODO: check if n, k and maybe startingSubvector are valid values
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }

        if (!Functions.isValidMinimumDistance(d)) {
            throw new InvalidMinimumDistanceException(d);
        }
        this.n = n;
        this.k = k;
        this.d = d;
        this.base = base;
        this.rhsWeight = rhsWeight;
        this.appendIdentity = appendIdentity;
        this.restrictGeneration = restrictGeneration;
        this.resetPoint = resetPoint;
        hasStarted = true;
        subvector = startingSubvector;
        if (subvector < 0) {
            subvector = 0;
        }
        nextResetPoint = (resetPoint + 1) >> 1;

        //The next line decrements the subvector because the generator will
        //increment the current subvector before checking if it is valid or not
        //(it uses a do-while loop). Since we want to also test the subvector
        //of the above row, we need to decrement it by one and the generator
        //will increment it by one, check its validity (which is valid) and
        //the logic continues. For example, if the top row is: 1 0 0 0 1 1 1,
        //then the next vector that will be examined is: 0 1 0 0 1 1 1, not
        //0 1 0 0 1 1 2.
        subvector--;
    }

    /**
     * Generates a quaternary vector with the specified number of 1's from
     * the right side.
     *
     * @param amount the number of 1's to be generated
     * @return a quaternary vector with the appropriate number of 1's
     */
    private long generateOnesOnRight(byte amount) {
        long result = 1;
        for (int i = 0; i < amount - 1; i++) {
            result = result << 2;
            result = result | 0b01;
        }
        return result;
    }

    /**
     * returns the current subvector without the identity subvector.
     *
     * @return the current subvector without the identity subvector
     */
    public long getCurrentSubvector() {
        return subvector;
    }

    /**
     * Returns the base of the code which could either be \(2\) or \(4\).
     *
     * @return the base of the code which could either be \(2\) or \(4\)
     */
    public long getBase() {
        return base;
    }

    /**
     * Returns the minimum weight of the right-hand-side of a valid vector.
     *
     * @return the minimum weight of the right-hand-side of a valid vector.
     */
    public long getMinimumRHSWeight() {
        return rhsWeight;
    }

    /**
     * Returns the current subvector with the identity row attached.
     *
     * @param currentRow the current row to place \(1\) in the valid position
     *                   of the identity row portion
     * @return The current subvector with the identity row attached
     */
    public long getCurrentFullVector(byte currentRow) {
        return getIdentityRow(currentRow) | subvector;
    }

    /**
     * Returns the next subvector with the identity row attached.
     *
     * @param currentRow the current row to place \(1\) in the valid position
     *                   of the identity row portion
     * @return The next vector with the identity row attached
     */
    public long getNextFullVector(byte currentRow) {

        return getIdentityRow(currentRow) | getNextSubVector();
    }

    /**
     * Returns a vector with weight of \(1\) where the digit one is placed in
     * the specified index.
     *
     * @param index the index of the \(1\) to place
     * @return a vector with weight of \(1\) where the digit one is placed in
     * the specified index
     */
    private long getIdentityRow(byte index) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }

        if (!appendIdentity) {//return 0 if we don't need to add identity
            return 0;
        }

        byte maxCol = 0;
        if (base == 2) {
            maxCol = 61;
        } else if (base == 4) {
            maxCol = 29;
        }

        //ensure the column is between [0, 61] for base 2 or [0, 29] for base 4
        if (!Functions.isValidColIndex(index, maxCol)) {
            throw new InvalidColIndexException(index, maxCol, base);
        }

        //ensure the column index desired is on the left side of the matrix.
        //It is somewhere between the first k (between [0, k - 1]) digits from
        //far-left of the matrix.
        if (!Functions.isValidIdentityRowIndex(index, k)) {
            throw new InvalidIdentityRowIndexException(index, k, base);
        }

        long result = 1L;
        if (base == 2) {
            result = result << n - 1;
            result = result >> index;
        }
        if (base == 4) {
            result = result << 2 * (n - 1);
            result = result >> 2 * index;
        }
        return result;
    }

    /**
     * Returns the next subvector that satisfy the weight {@code RHS_WEIGHT},
     * which is \(d - 1\).
     *
     * @return the next subvector that satisfy the weight {@code RHS_WEIGHT},
     * which is \(d - 1\)
     */
    public long getNextSubVector() {
        return getNextSubVectorEngine(rhsWeight, base);
    }

    /**
     * The engine that will return the next subvector that satisfy the weight
     * {@code RHS_WEIGHT}, which is \(d - 1\).
     *
     * @param rhsWeight the Hamming weight of the subvector
     * @param base      the base of the code which could either be \(2\) or \(4\)
     * @return the next subvector that satisfy the weight {@code RHS_WEIGHT},
     * which is \(d - 1\)
     */
    private long getNextSubVectorEngine(byte rhsWeight, byte base) {
        if (!Functions.isValidBase(base)) {//validity of the base
            throw new InvalidBaseException(base);
        }
        long limit;
        if (appendIdentity) {
            limit = Functions.power(base, n - k);
        } else {
            limit = Functions.power(base, n);
        }
        if (!hasStarted) {
            hasStarted = true;
            if (getWeight(subvector, base) >= rhsWeight) {
                return subvector;
            }
        }
        if (base == 2) {
            do {
                subvector = subvector + 1;
            } while (getWeight(subvector, base) < rhsWeight &&
                             subvector < limit - 1);
        } else if (base == 4) {
            if (restrictGeneration) {
                //find the next vector with appropriate weight
                do {
                    totalVectorsExamined++;
                    if (resetPoint == subvector) {
                        nextResetPoint = (resetPoint + 1) << 1;
                        subvector = nextResetPoint;
                        nextResetPoint = nextResetPoint << 2;
                        resetPoint = resetPoint << 2;
                        resetPoint = resetPoint | 0b11;
                    } else {
                        subvector = subvector + 1;
                    }
                } while (getWeight(subvector, base) < rhsWeight);
            } else {
                do {
                    subvector = subvector + 1;
                } while (getWeight(subvector, base) < rhsWeight &&
                                 subvector < limit - 1);
            }
        }
        if (subvector >= limit) {
            //No more subvectors available
            isCurrentSubvectorValid = false;
        }
        return subvector;
    }

    /**
     * Returns whether the current subvector is valid Hamming weight wise.
     *
     * @return {@code true} if the current subvector satisfy the Hamming
     * weight, {@code false} otherwise
     */
    public boolean isCurrentSubvectorValid() {
        return isCurrentSubvectorValid;
    }

    /**
     * Returns the weight of non-zero digits in the value passed. It will
     * achieve that by going the binary representation of the value and
     * increment a counter every time a digit (in base \(2\) or base \(4\)
     * depending on the base passed) is not the digit \(0\).
     *
     * @param vector the vector to find the weight for
     * @param base   the base the value should be treated as
     * @return the number of non-zero digits in the vector passed
     */
    public byte getWeight(long vector, byte base) {
        return HammingWeight.getWeight(vector, base);
    }

    /**
     * The current reset point which is a vector of Hamming weight of \(1\). It
     * is used to generate vectors whose left-most on-zero digit is \(1\)
     * (<i>i.e.</i>, not {@code 2} nor {@code 3}).
     *
     * @return the current reset point which is a vector of Hamming weight of
     * \(1\)
     */
    public long getResetPoint() {
        return resetPoint;
    }

    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running VectorGenerator...");
//        byte n = 20;
//        byte k = 9;
//        byte d = 9;
//        byte minimumWeight = (byte) (d - 1);
//        byte base = 4;
//        boolean addIdentity = true;
//        boolean restrictGeneration = true;
//        long counter = 0;
//        String ns = String.format("%" + 2 + "s", n + "");
//        String ks = String.format("%" + 2 + "s", k + "");
//        String ds = String.format("%" + 2 + "s", d + "");
//        System.out.print("The code (" + ns + ", " + ks + ", " + ds + ") has ");
//        VectorGenerator vg = new VectorGenerator(
//                n, k, d, minimumWeight, base, addIdentity, restrictGeneration
//        );
//        while (true) {
//            long vector = vg.getNextFullVector((byte) 0);
//            if (vg.isCurrentSubvectorValid()) {
//                counter++;
//            } else {
//                break;
//            }
//        }
//        System.out.format("%,d%s\n", counter, " vectors to check.");
//        System.out.println("VectorGenerator completed.");
//    }

}