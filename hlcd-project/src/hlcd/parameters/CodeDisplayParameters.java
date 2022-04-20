package hlcd.parameters;

/**
 * A wrapper class containing the information of what should be displayed on
 * console as output.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 24th, 2022)
 * @since 1.8
 */
public class CodeDisplayParameters {
    private boolean printGeneratorMatrix;
    private boolean printGeneratorGPrimeDet;
    private boolean printGPrime;
    private boolean printCodeStatistics;
    private boolean printCombinations;
    private boolean printWeightEnumerator;
    private boolean printFilePath;

    /**
     * Uses the default values which are to print the generator matrix, the
     * determinant of the generator matrix, the statistics of the code and
     * the file path of where the data will be written to.
     */
    public CodeDisplayParameters() {
        printGeneratorMatrix = true;
        printGeneratorGPrimeDet = true;
        printGPrime = false;
        printCombinations = false;
        printCodeStatistics = true;
        printWeightEnumerator = false;
        printFilePath = false;
    }

    /**
     * Allows to choose the data to be displayed to console.
     *
     * @param printGeneratorMatrix    should the generator matrix be printed
     * @param printGeneratorGPrimeDet should the determinant of the G prime
     *                                which is \(G^{\prime} = G^{\kern0pt}_{k
     *                                \times n} \overline{G}^{T}_{n \times k}\)
     * @param printGPrime             print \(G^{\prime} = G^{\kern0pt}_{k
     *                                \times n} \overline{G}^{T}_{n \times k}\)
     * @param printCombinations       print all the codewords in the code
     * @param printWeightEnumerator   print the weight enumerator of the code
     * @param printCodeStatistics     print the statistics of the code
     * @param printFilePath           print the filepath used to write the
     *                                files to
     */
    public CodeDisplayParameters(
            boolean printGeneratorMatrix,
            boolean printGeneratorGPrimeDet,
            boolean printGPrime,
            boolean printCombinations,
            boolean printWeightEnumerator,
            boolean printCodeStatistics,
            boolean printFilePath
    ) {
        this.printGeneratorMatrix = printGeneratorMatrix;
        this.printGeneratorGPrimeDet = printGeneratorGPrimeDet;
        this.printGPrime = printGPrime;
        this.printCombinations = printCombinations;
        this.printWeightEnumerator = printWeightEnumerator;
        this.printCodeStatistics = printCodeStatistics;
        this.printFilePath = printFilePath;
    }

    /**
     * Whether the generator matrix should be printed on console.
     *
     * @return {@code true} if the generator matrix should be printed on
     * console, {@code false} otherwise
     */
    public boolean shouldPrintGeneratorMatrix() {
        return printGeneratorMatrix;
    }

    /**
     * Whether the determinant of \(G^{\prime} = G^{\kern0pt}_{k \times n}
     * \overline{G}^{T}_{n \times k}\) should be printed on console.
     *
     * @return {@code true} if the determinant of \(G^{\prime} = G^{\kern0pt}_{k
     * \times n} \overline{G}^{T}_{n \times k}\) should be printed on console,
     * {@code false} otherwise
     */
    public boolean shouldPrintGPrimeDet() {
        return printGeneratorGPrimeDet;
    }

    /**
     * Whether \(G^{\prime} = G^{\kern0pt}_{k \times n} \overline{G}^{T}_{n
     * \times k}\) should be printed on console.
     *
     * @return {@code true} if \(G^{\prime} = G^{\kern0pt}_{k \times n}
     * \overline{G}^{T}_{n \times k}\) should be printed on console,
     * {@code false} otherwise
     */
    public boolean shouldPrintGPrime() {
        return printGPrime;
    }

    /**
     * Whether the codewords in the code should be printed on console.
     *
     * @return {@code true} if the codewords in the code should be printed on
     * console, {@code false} otherwise
     */
    public boolean shouldPrintCombinations() {
        return printCombinations;
    }

    /**
     * Whether the weight enumerator of the code should be printed on console.
     *
     * @return {@code true} if the weight enumerator of the code should be
     * printed on console, {@code false} otherwise
     */
    public boolean shouldPrintWeightEnumerator() {
        return printWeightEnumerator;
    }

    /**
     * Whether the code statistics should be printed on console.
     *
     * @return {@code true} if the code statistics of the code should be
     * printed on console, {@code false} otherwise
     */
    public boolean shouldPrintCodeStatistics() {
        return printCodeStatistics;
    }

    /**
     * Whether the directory used to write the files to should be printed on
     * console.
     *
     * @return {@code true} if the directory used to write the files to
     * should be printed on console, {@code false} otherwise
     */
    public boolean shouldPrintFilePath() {
        return printFilePath;
    }
}
