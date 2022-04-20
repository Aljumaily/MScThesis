package hlcd.operations;

/**
 * A class contains static variables and methods to be used globally across
 * the classes in this program. It deals mostly with checking if the values
 * being used for matrix dimension, bases, indices, etc. are valid.
 *
 * @author Maysara Al Jumaily
 * @version 1.0
 * @since February 2nd, 2022
 */
public class Functions {

    /**
     * The decimal value "1" repeated in binary:
     * {@code
     * 0b01010101_01010101_01010101_01010101_01010101_01010101_01010101_01010101
     * }
     */
    public static final long ONE = 0x5555_5555_5555_5555L;

    /**
     * The decimal value "2" repeated in binary:
     * {@code
     * 0b10101010_10101010_10101010_10101010_10101010_10101010_10101010_10101010
     * }
     */
    public static final long TWO = 0xAAAA_AAAA_AAAA_AAAAL;

    /**
     * The decimal value "3" repeated in binary:
     * {@code
     * 0b00110011_00110011_00110011_00110011_00110011_00110011_00110011_00110011
     * }
     */
    public static final long THREE = 0x3333_3333_3333_3333L;

    /**
     * The decimal value "15" repeated in binary:
     * {@code
     * 0b00001111_00001111_00001111_00001111_00001111_00001111_00001111_00001111
     * }
     */
    public static final long F = 0x0F0F_0F0F_0F0F_0F0FL;

    /**
     * Contains 64 1's:
     * {@code
     * 0b11111111_11111111_11111111_11111111_11111111_11111111_11111111_11111111
     * }
     */
    public static final long ALL_ONES = 0xFFFF_FFFF_FFFF_FFFFL;

    /**
     * The multiplication logic in base \(4\). It is given as:
     * <table border= "1" style='text-align:center; border-collapse:collapse'>
     *     <caption>The multiplication table in base \(4\)</caption>
     *     <tr>
     *         <th style='padding: 4px'>\(\times\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(1\)</th>
     *         <th style='padding: 4px'>\(\omega\)</th>
     *         <th style='padding: 4px'>\(\overline{\omega}\)</th>
     *     </tr>
     *     <tr>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *     </tr>
     *     <tr>
     *         <th style='padding: 4px'>\(1\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(1\)</th>
     *         <th style='padding: 4px'>\(\omega\)</th>
     *         <th style='padding: 4px'>\(\overline{\omega}\)</th>
     *     </tr>
     *     <tr>
     *         <th style='padding: 4px'>\(\omega\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(\omega\)</th>
     *         <th style='padding: 4px'>\(\overline{\omega}\)</th>
     *         <th style='padding: 4px'>\(1\)</th>
     *     </tr>
     *     <tr>
     *         <th style='padding: 4px'>\(\overline{\omega}\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(\overline{\omega}\)</th>
     *         <th style='padding: 4px'>\(1\)</th>
     *         <th style='padding: 4px'>\(\omega\)</th>
     *     </tr>
     * </table>
     */
    public static final byte[][] MUL_ARRAY = new byte[][]{
            {0, 0, 0, 0},
            {0, 1, 2, 3},
            {0, 2, 3, 1},
            {0, 3, 1, 2}
    };

    /**
     * The division logic in base \(4\). Note that the far-left column is not
     * defined (division by 0), hence, denoted as "\(-\)" and in code, assigned
     * the number {@code -99} . It is given as:
     * <table border= "1" style='text-align:center; border-collapse:collapse'>
     *     <caption>The division table in base \(4\)</caption>
     *     <tr>
     *         <th style='padding: 4px'>\(\div\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(1\)</th>
     *         <th style='padding: 4px'>\(\omega\)</th>
     *         <th style='padding: 4px'>\(\overline{\omega}\)</th>
     *     </tr>
     *     <tr>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(-\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *         <th style='padding: 4px'>\(0\)</th>
     *     </tr>
     *     <tr>
     *         <th style='padding: 4px'>\(1\)</th>
     *         <th style='padding: 4px'>\(-\)</th>
     *         <th style='padding: 4px'>\(1\)</th>
     *         <th style='padding: 4px'>\(\overline{\omega}\)</th>
     *         <th style='padding: 4px'>\(\omega\)</th>
     *     </tr>
     *     <tr>
     *         <th style='padding: 4px'>\(\omega\)</th>
     *         <th style='padding: 4px'>\(-\)</th>
     *         <th style='padding: 4px'>\(\omega\)</th>
     *         <th style='padding: 4px'>\(1\)</th>
     *         <th style='padding: 4px'>\(\overline{\omega}\)</th>
     *     </tr>
     *     <tr>
     *         <th style='padding: 4px'>\(\overline{\omega}\)</th>
     *         <th style='padding: 4px'>\(-\)</th>
     *         <th style='padding: 4px'>\(\overline{\omega}\)</th>
     *         <th style='padding: 4px'>\(\omega\)</th>
     *         <th style='padding: 4px'>\(1\)</th>
     *     </tr>
     * </table>
     */
    public static final byte[][] DIV_ARRAY = new byte[][]{
            {-99, 0, 0, 0},
            {-99, 1, 3, 2},
            {-99, 2, 1, 3},
            {-99, 3, 2, 1}
    };

    private Functions() {
        //empty
    }

    /**
     * Checks whether the base of the code is valid. A valid base is either
     * \(2\) or \(4\).
     *
     * @param base the base of the code (could be either \(2\) or \(4\))
     * @return {@code true} if the base is valid (<i>i.e.</i>, \(2\) or \(4\)),
     * {@code false} otherwise
     */
    public static boolean isValidBase(byte base) {
        return base == 2 || base == 4;
    }

    /**
     * Checks whether the \(n\) and \(k\) values being used are valid. They are
     * valid in base \(2\) if \(1 \leq k \leq 62\), \(1 \leq n \leq 62\),
     * \(1 \leq k \leq n\) and valid in base \(4\) if \(1 \leq k \leq 30\),
     * \(1 \leq n \leq 30\) and \(1 \leq k \leq n\).
     *
     * @param n the length of a codeword in the code
     * @param k the dimension of the code
     * @return {@code true} if \(n\) and \(k\) are valid, {@code false}
     * otherwise
     */
    public static boolean isValidNAndKValues(byte n, byte k) {
        return 1 <= k && k <= n;
    }

    /**
     * Checks whether the passed minimum distance is valid. The
     * implementation of this program <em>doesn't</em> support minimum
     * distance of \(d \lt 3\).
     *
     * @param minimumDistance the minimum distance to be checked
     * @return {@code true} if the minimum distance bigger or equal to \(3\),
     * {@code false} otherwise
     */
    public static boolean isValidMinimumDistance(byte minimumDistance) {
        return minimumDistance >= 3;
    }

    /**
     * Checks whether the specified column index is valid. A column index is
     * zero-based.
     *
     * @param index  the column index to check
     * @param maxCol the maximum valid column that is accessible. This is
     *               one-based so the column index must be <em>strictly</em>
     *               less than
     * @return {@code true} if the column index is within the appropriate
     * range, {@code false} otherwise
     */
    public static boolean isValidColIndex(byte index, byte maxCol) {
        return 0 <= index && index < maxCol;
    }

    /**
     * Checks whether the specified row index is valid. A row index is
     * zero-based.
     *
     * @param index  the row index to check
     * @param maxRow the maximum valid row that is accessible. This is
     *               one-based so the row index must be <em>strictly</em>
     *               less than
     * @return {@code true} if the row index is within the appropriate
     * range, {@code false} otherwise
     */
    public static boolean isValidRowIndex(byte index, byte maxRow) {
        return 0 <= index && index < maxRow;
    }

    /**
     * Checks whether the specified row index is valid row in an identity
     * matrix. A row index is zero-based.
     *
     * @param index the row index to check
     * @param k     the maximum valid row that is accessible. This is
     *              one-based so the row index must be <em>strictly</em>
     *              less than
     * @return {@code true} if the row index is within the appropriate
     * range, {@code false} otherwise
     */
    public static boolean isValidIdentityRowIndex(byte index, byte k) {
        return index >= 0 && index < k;
    }

    /**
     * Checks if a column vector is appropriate to be placed inside a matrix.
     * The number of rows in the column vector must match the number of rows
     * in the matrix. It doesn't matter if it is zero-based or one-based as
     * long as both use the same convention. Keep it as one-based if in doubt.
     *
     * @param columnVectorRowCount the number of rows in the column vector
     * @param matrixRowCount       the number of rows in the matrix
     * @return {@code true} if both the column vector and matrix have the same
     * number of rows, {@code false} otherwise
     */
    public static boolean isValidSettingColumnInMatrix(
            byte columnVectorRowCount,
            byte matrixRowCount
    ) {
        return columnVectorRowCount == matrixRowCount;
    }

    /**
     * Checks if a column vector is appropriate in the sense that its length
     * should be between \(1\) and \(k\) (one-based).
     *
     * @param columnVectorRowCount the number of rows in the column vector
     * @param k                    the length of the code
     * @return {@code true} if the column vector has a valid size, {@code
     * false} otherwise
     */
    public static boolean isValidColumnVectorDimension(
            byte columnVectorRowCount,
            byte k
    ) {
        return 1 <= columnVectorRowCount && columnVectorRowCount <= k;
    }

    /**
     * Checks whether the digit specified is a valid digit in the base passed.
     *
     * @param digit the digit to check
     * @param base  the base of the code
     * @return {@code true} if the digit is valid in the base passed, {@code
     * false} otherwise
     */
    public static boolean isValidDigit(byte digit, byte base) {
        if (base == 2) {
            return digit == 0b0 || digit == 0b1;
        } else if (base == 4) {
            return digit == 0b00 || digit == 0b01 ||
                           digit == 0b10 || digit == 0b11;
        }
        return false;
    }

    /**
     * Checks if the dimension of two matrices {@code leftMatrixArray} and
     * {@code rightMatrixArray} is valid for performing the multiplication.
     *
     * @param leftMatrixArray   the left matrix to be multiplied
     * @param leftMatrixArrayN  the number of columns in the left matrix
     * @param leftMatrixArrayK  the number of rows in the left matrix
     * @param rightMatrixArray  the right matrix to be multiplied
     * @param rightMatrixArrayN the number of columns in the right matrix
     * @param rightMatrixArrayK the number of rows in the right matrix
     * @return {@code true} if the multiplication can be performed, {@code
     * false} otherwise
     */
    public static boolean isValidMatrixMultiplicationDimension(
            long[] leftMatrixArray,
            byte leftMatrixArrayN,
            byte leftMatrixArrayK,
            long[] rightMatrixArray,
            byte rightMatrixArrayN,
            byte rightMatrixArrayK
    ) {
        //dimension of leftMatrixArray doesn't match its number of rows
        if (leftMatrixArray.length != leftMatrixArrayK) {
            return false;
        }
        //dimension of rightMatrixArray doesn't match its number of rows
        if (rightMatrixArray.length != rightMatrixArrayK) {
            return false;
        }

        //leftMatrixArray and rightMatrixArray cannot be multiplied because
        //their dimension doesn't match
        return (leftMatrixArrayK == rightMatrixArrayN) &&
                       (leftMatrixArrayN == rightMatrixArrayK);
    }

    /**
     * An aesthetic way to draw a solid (non-dashed) line on console by using
     * the character {@code \u2500}. It will automatically draw a horizontal
     * line consisting of 80 characters.
     *
     * @return A string containing 80 characters of {@code \u2500}.
     */
    public static String writeConsoleLineSeparator() {
        return writeConsoleLineSeparator((byte) 80);
    }

    /**
     * An aesthetic way to draw a solid (non-dashed) line on console by using
     * the character {@code \u2500}. It will create a horizontal line
     * consisting of the specified length.
     *
     * @param length the length of the line
     * @return A string of the length specified containing characters of {@code
     * \u2500}.
     */
    public static String writeConsoleLineSeparator(byte length) {
        return new String(new char[length]).replace("\0", "\u2500");
    }

    /**
     * Efficiently finds the result of \(x^n\). In the case where \(n=63\) or
     * \(n=64\), then the maximum value that will be return is
     * \(2^{63} - 1 = 9223372036854775807\). Since this only return positive
     * values, we cannot have a binary vector of length \(63\) nor \(64\) or a
     * quaternary vector of length \(31\) nor \(32\). In the case where an
     * invalid length is passed, then a warning message will be displayed on
     * console. All in all, the valid lengths for a binary code is between
     * \(1\) and \(62\) (including both) and for a quaternary code is between
     * \(1\) and \(30\) (including both).
     *
     * @param x the base of the power
     * @param n the number to raise to
     * @return the result of \(x^n\)
     */
    public static long power(long x, long n) {
        if (n == 63 || n == 64) {
            System.out.println("Warning from the power method in the " +
                                       "Functions class: the maximum long " +
                                       "value has been returned.");
            return Long.MAX_VALUE;
        }
        long result = 1;
        for (byte i = 0; i < n; i++) {
            result = result * x;
            //System.out.println(result);
        }
        return result;
    }
}
