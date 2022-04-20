package hlcd.operations;

import hlcd.enums.Style;

/**
 * Prints a matrix <em>nicely</em> on console. There are three different
 * formats to choose from: binary, quaternary and decimal. The binary format
 * will print the binary representation of a vector regardless of whether
 * the code is in base \(2\) or \(4\). The quaternary format prints {@code 0},
 * {@code 1}, {@code 2} and {@code 3} as {@code 0}, {@code 1},
 * <code>&omega;</code> and <code>&epsilon;</code>, respectively. The decimal
 * format will print the digits in base 10 (<i>i.e.</i>, {@code 0}, {@code 1},
 * {@code 2} and {@code 3}). Lastly, the LaTeX option will render the digits
 * in LaTeX format. There are other aesthetics that will be taken place like
 * placing matrix lines around a matrix, printing the dimension of a matrix
 * in the lower-right corner and adding a delimiter between the digits.
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running MatrixPrinter...");
 *     long[] matrixArray = new long[]{
 *         0b11100100,
 *         0b00000000,
 *         0b01010101,
 *         0b10101010,
 *         0b11111111
 *     };
 *     byte n = 4;
 *     byte k = 5;
 *     byte base = 4;
 *     Style style = Style.DECIMAL;
 *     MatrixPrinter mp = new MatrixPrinter(matrixArray, n, k, base, style);
 *     mp.printMatrix(" ", true, Style.DECIMAL, true);
 *     mp.printMatrix(" ", true, Style.BINARY, true);
 *     mp.printMatrix(" ", true, Style.QUATERNARY, true);
 *     mp.printMatrix(" &#38; ", true, Style.LATEX, true);
 *     System.out.println("MatrixPrinter completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0
 * @since February 2nd, 2022
 */
public class MatrixPrinter {
    private long[] matrixArray;
    private byte n;
    private byte k;
    private byte base;
    private Style style;

    /**
     * Constructs a simple printer for the matrix specified.
     *
     * @param matrixArray the matrix to be displayed on console
     * @param n           the length of each vector
     * @param k           the dimension of the matrix
     * @param base        the base of which could either be \(2\) or \(4\)
     * @param style       the style format which could either be binary,
     *                    quaternary, decimal or \(\LaTeX\)
     */
    public MatrixPrinter(
            long[] matrixArray,
            byte n,
            byte k,
            byte base,
            Style style
    ) {
        if (k != matrixArray.length) {
            System.out.println("The value k is " + k + " doesn't match the " +
                                       "number of rows in the specified " +
                                       "matrix which is " + matrixArray.length +
                                       ". The value of k is changed to " +
                                       matrixArray.length + "."
            );
            k = (byte) matrixArray.length;

        }
        this.k = k;
        this.n = n;
        this.base = base;
        this.matrixArray = new long[matrixArray.length];
        this.style = style;
        for (int i = 0; i < matrixArray.length; i++) {
            this.matrixArray[i] = matrixArray[i];
        }
    }

    /**
     * Prints the matrix with the following default values: with a space
     * character as the delimiter between the columns, surrounding the matrix
     * with square brackets as well as showing the dimension.
     */
    public void printMatrix() {
        printMatrixEngine(matrixArray, n, base, " ", true, style, true);
    }

    /**
     * Prints the matrix on console while having the space character as the
     * delimiter between the columns.
     *
     * @param addBrackets should surround the matrix with brackets
     * @param showSize    should the dimension of the matrix be displayed in
     *                    the bottom-right corner
     */
    public void printMatrix(
            boolean addBrackets,
            boolean showSize
    ) {
        printMatrixEngine(
                this.matrixArray,
                this.n,
                this.base,
                " ",
                addBrackets,
                style,
                showSize
        );
    }

    /**
     * Prints the matrix on console while having the specified delimiter
     * between the columns.
     *
     * @param delimiter   the delimiter used to separate the digits
     * @param addBrackets should surround the matrix with brackets
     * @param showSize    should the dimension of the matrix be displayed in
     *                    the bottom-right corner
     */
    public void printMatrix(
            String delimiter,
            boolean addBrackets,
            boolean showSize
    ) {
        printMatrixEngine(
                this.matrixArray,
                this.n,
                this.base,
                delimiter,
                addBrackets,
                style,
                showSize
        );
    }

    /**
     * Prints the matrix on console while having the specified delimiter
     * between the columns.
     *
     * @param delimiter   the delimiter used to separate the digits
     * @param addBrackets should surround the matrix array with brackets
     * @param style       the style format which could either be binary,
     *                    quaternary, decimal or \(\LaTeX\)
     * @param showSize    should the dimension of the matrix array be
     *                    displayed in the bottom-right corner
     */
    public void printMatrix(
            String delimiter,
            boolean addBrackets,
            Style style,
            boolean showSize
    ) {
        printMatrixEngine(
                this.matrixArray,
                this.n,
                this.base,
                delimiter,
                addBrackets,
                style,
                showSize
        );
    }

    /**
     * Prints the matrix array on console using the other parameters specified.
     *
     * @param matrixArray the matrix array to be printed on console
     * @param n           the length of each vector in the matrix array
     * @param base        the base of the code which could either be \(2\) or \(4\)
     * @param delimiter   the delimiter used to separate the digits
     * @param addBrackets should surround the matrix array with brackets
     * @param style       the style format which could either be binary,
     *                    quaternary, decimal or \(\LaTeX\)
     * @param showSize    should the dimension of the matrix array be
     *                    displayed in the bottom-right corner
     */
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
     * The engine used to print a matrix on console.
     *
     * @param matrixArray the matrix to be printed on console
     * @param n           the length of each vector in the matrix
     * @param base        the base of the code which could either be \(2\) or \(4\)
     * @param delimiter   the delimiter used to separate the digits
     * @param addBrackets should surround the matrix with brackets
     * @param style       the style format which could either be binary,
     *                    quaternary, decimal or \(\LaTeX\)
     * @param showSize    should the dimension of the matrix be displayed in
     *                    the bottom-right corner
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
        for (byte i = 0; i < matrixArray.length; i++) {
            String result = vectorAsString(
                    matrixArray[i], n, base, delimiter, style
            );

            if (addBrackets) {
                String[] braces = surroundRowWithBraces(matrixArray, i);
                //braces[0] is the left brace to add
                //braces[1] is the right brace to add
                result = braces[0] + result + braces[1];
            }

            if (i == matrixArray.length - 1) {
                byte k = (byte) matrixArray.length;
                if (showSize) {
                    result = result + " " + getSubscriptNumber(k) + "ₓ"
                                     + getSubscriptNumber(n);
                }
            }
            System.out.println(result);
        }
    }

    /**
     * Returns a formatted vector based on the properties specified.
     *
     * @param vector    the vector to be formatted
     * @param n         the length of the vector
     * @param base      the base of the code which could either be \(2\) or \(4\)
     * @param delimiter the delimiter used to separate the digits in the vector
     * @param style     the style format which could either be binary,
     *                  quaternary, decimal or \(\LaTeX\)
     * @return a formatted vector based on the properties specified
     */
    public static String vectorAsString(
            long vector,
            byte n,
            byte base,
            String delimiter,
            Style style
    ) {
        byte padding = (base == 2) ? n : (byte) (n * 2);
        byte delimiterPosition = (base == 2) ? (byte) 1 : (byte) 2;
        String regexFormat = "(.{" + delimiterPosition + "})(?!$)";//put the
        // delimiter every character (base 2) or every two characters (base 4)

        String result = String.format(
                "%" + padding + "s", Long.toBinaryString(vector)
        );

        result = result.replace(" ", "0");
        if (delimiter.isEmpty()) {
            result = result.replaceAll(regexFormat, "$1" + " ");
        } else {
            result = result.replaceAll(regexFormat, "$1" + delimiter);
        }
        //the current format of result is in binary. We can keep it like this
        // if the style is binary, otherwise, check other styles.
        if (style != Style.BINARY) {
            String[] digits;
            if (delimiter.isEmpty()) {
                digits = result.split(" ");
            } else {
                digits = result.split(delimiter);
            }
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < digits.length; j++) {
                byte value = Byte.parseByte(digits[j], 2);
                if (style == Style.QUATERNARY) {
                    //readable on screen
                    switch (value) {
                        case 0:
                            sb.append("0");
                            break;
                        case 1:
                            sb.append("1");
                            break;
                        case 2:
                            sb.append("\u03C9");//ω
                            break;
                        case 3:
                            sb.append("\u1FF6");//ῶ
                            //or use ε to not distort the rectangular shape
                            //as ῶ will take more horizontal space than the
                            //other characters.
                            break;
                    }
                } else if (style == Style.LATEX) {
                    switch (value) {
                        case 0:
                            sb.append("0");
                            break;
                        case 1:
                            sb.append("1");
                            break;
                        case 2:
                            sb.append("\\omega");
                            break;
                        case 3:
                            sb.append("\\bar{\\omega}");
                            break;
                    }
                } else if (style == Style.DECIMAL) {
                    sb.append(Byte.parseByte(digits[j], 2));
                } else {
                    //kept as binary, don't do anything because it is already
                    //in binary form.
                }
                if (j != digits.length - 1) {
                    sb.append(delimiter);
                }
            }
            result = sb.toString();
        }

        //The style is quaternary or decimal, it should be printed in base 10
        if (style == Style.QUATERNARY || style == Style.DECIMAL) {

        }
        if (delimiter.isEmpty()) {
            result = result.replace(" ", "");
        }
        return result;
    }

    /**
     * Returns the subscript equivalent of the value specified. For example, if
     * the value specified is 15, then it will return \u2081\u2085.
     *
     * @param value the value to find the subscript equivalent for
     * @return the subscript equivalent of the value specified
     */
    private String getSubscriptNumber(byte value) {
        //\u2080 is subscript 0, \u2081 is subscript 1, ...,  \u2089 is
        //subscript 9.
        String[] subscriptDigits = new String[]{
                "\u2080", "\u2081", "\u2082", "\u2083", "\u2084",
                "\u2085", "\u2086", "\u2087", "\u2088", "\u2089"
        };
        String digits = value + "";
        String result = "";
        for (char c : digits.toCharArray()) {
            result = result + subscriptDigits[(int) c - 48];
        }
        return result;
    }

    /**
     * The logic for surrounding all types of matrices with brackets. It will
     * use \u2308, \u2309, \u230A and \u230B for the corners.
     *
     * @param matrixArray the matrix that contains the row
     * @param currentRow  the row index to be surrounded by brackets
     * @return the specified row surrounded by brackets
     */
    private String[] surroundRowWithBraces(
            long[] matrixArray,
            byte currentRow
    ) {
        //⌈⌉⌊⌋
        String leftBracket;
        String rightBracket;
        if (matrixArray.length == 1) {//1 * 1 matrix
            leftBracket = "[";
            rightBracket = "]";
        } else if (matrixArray.length == 2) {//2 * 2 matrix
            if (currentRow == 0) {//top row
                leftBracket = "⌈";
                rightBracket = "⌉";
            } else {//bottom row
                leftBracket = "⌊";
                rightBracket = "⌋";
            }
        } else {//3 * 3 matrix or higher
            if (currentRow == 0) {//top row
                leftBracket = "⌈";
                rightBracket = "⌉";
            } else if (currentRow == matrixArray.length - 1) {//bottom row
                leftBracket = "⌊";
                rightBracket = "⌋";
            } else {//row somewhere in the middle
                leftBracket = "|";
                rightBracket = "|";
            }
        }
        return new String[]{leftBracket, rightBracket};
    }

    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running MatrixPrinter...");
//        long[] matrixArray = new long[]{
//                0b11100100,
//                0b00000000,
//                0b01010101,
//                0b10101010,
//                0b11111111
//        };
//        byte n = 4;
//        byte k = 5;
//        byte base = 4;
//        Style style = Style.DECIMAL;
//        MatrixPrinter mp = new MatrixPrinter(matrixArray, n, k, base, style);
//        mp.printMatrix(" ", true, Style.DECIMAL, true);
//        mp.printMatrix(" ", true, Style.BINARY, true);
//        mp.printMatrix(" ", true, Style.QUATERNARY, true);
//        mp.printMatrix(" & ", true, Style.LATEX, true);
//        System.out.println("MatrixPrinter completed.");
//    }
}
