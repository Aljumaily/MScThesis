package hlcd.exporter;

import hlcd.linearCode.Code;
import hlcd.operations.DirectoryCreator;
import hlcd.operations.Functions;
import hlcd.parameters.CodeExporterParameters;
import hlcd.enums.Style;
import hlcd.operations.LongArray;

/**
 * Exports the code found to file as a persistent object, \(\LaTeX\) and Matlab
 * files. It will not override any files/folders because timestamps will be
 * added in the names. The options are given using {@code
 * CodeExporterParameters}. It can write a code as a {@code .bin}, {@code
 * .tex} and {@code .m} (Matlab) file. The {@code .tex} files will contain the
 * generator matrix \(G\) along with the weight enumerator written as two
 * separate files. The {@code .m} file contains the generator matrix \(G\), the
 * Hermitian transpose \(\overline{G}^{T}\) and \(G^\prime = G \times
 * \overline{G}^{T}\). It will ensure that Matlab calculates \(G \times
 * \overline{G}^{T}\) which must match the matrix this program found.
 * <pre><code class="language-java line-numbers"> //minimal but complete example
 * public static void main(String[] args) {
 *     System.out.println("Running CodeExporter...");
 *     String path = Paths.DEFAULT_PATH;
 *     byte n = 7;
 *     byte k = 4;
 *     byte d = 3;
 *     byte base = 4;
 *     boolean printPathToConsole = true;
 *     CodeParameters cp = new CodeParameters(n, k, d, base);
 *     CodeValidatorParameters cvp = new CodeValidatorParameters();
 *     CodeExporterParameters cep = new CodeExporterParameters();
 *     Code code = new Code(cp, cvp);
 *     code.startEngine();
 *     code.printGeneratorMatrix(true, Style.DECIMAL, true);
 *     CodeExporter ce = new CodeExporter(path, cep, code, printPathToConsole);
 *     ce.export(
 *         "G",          //label of the generator matrix
 *         " ",          //delimiter between generator matrix's columns
 *         "0",          //the id of the generator matrix
 *         true,         //surround delimiter by space
 *         Style.DECIMAL //the style of digits
 *     );
 *     System.out.println("CodeExporter completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 24th, 2022)
 * @see CodeExporterParameters
 * @see CodeObjectIO
 * @see LatexMatrixWriter
 * @see LatexWeightEnumeratorWriter
 * @see MatlabCodeWriter
 * @since 1.8
 */
public class CodeExporter {
    private CodeExporterParameters cep;
    private Code code;
    private LongArray combination;
    private DirectoryCreator dc;
    private boolean printPathToConsole;

    /**
     * Initializes the code exporter by accepting the parameters of the files
     * that need to be exported.
     *
     * @param path               the directory path to store the files
     * @param cep                the parameters of the code exporter
     * @param code               the code to export
     * @param printPathToConsole should the file paths generated be printed
     *                           to console
     * @see CodeExporterParameters
     */
    public CodeExporter(String path, CodeExporterParameters cep, Code code,
            boolean printPathToConsole) {
        this.cep = cep;
        this.code = code;
        this.printPathToConsole = printPathToConsole;
        String directoryName = code.getCodeParameters().toString();
        dc = new DirectoryCreator(path, directoryName);
        dc.createDirectory();
    }

    /**
     * Writes the desired files to disk. It will use the {@code
     * CodeExporterParameters} object that was passed in the constructor
     * which specifies which type of file that needs to be written to disk.
     *
     * @param matrixLabel           the generator matrix label (should be
     *                              {@code "G"})
     * @param delimiter             the delimiter used when {@code .tex} file
     *                              is written (should be {@code "&"})
     * @param id                    The id that would be associated with the
     *                              matrix. Used when multiple generator
     *                              matrices of the same \(\left[n, \, k, \,
     *                              d\right]_{base}\) code are generated.
     *                              Should be {@code "0"} if only a single
     *                              generator matrix is found.
     * @param spaceBetweenDelimiter Should there be a space between the
     *                              delimiter used. Regardless of the value,
     *                              this doesn't affect the output, it is
     *                              there for aesthetic purposes
     * @param style                 should the {@code .tex} file write a
     *                              matrix in binary, decimal or quaternary.
     *                              For a binary code, this doesn't matter. For
     *                              a quaternary code, a <em>binary</em>
     *                              style will write the digits as \(00\),
     *                              \(01\), \(10\) and \(11\). A
     *                              <em>decimal</em> style will use \(0\),
     *                              \(1\), \(2\) and \(3\). A
     *                              <em>quaternary</em> style uses \(0\), \(1\),
     *                              \(\omega\) and \(\overline{\omega}\).
     *                              Lastly, the LaTeX style will write
     *                              everything in \(\LaTeX\) syntax.
     */
    public void export(String matrixLabel,
            String delimiter,
            String id,
            boolean spaceBetweenDelimiter,
            Style style) {
        if (cep.isCodeObjectWritable()) {
            CodeObjectIO co = new CodeObjectIO(
                    code, dc.getCompletePath(), id, printPathToConsole
            );
            co.writeCodeObject();
        }
        if (cep.isGMatrixLatexWritable()) {
            LatexMatrixWriter lmw = new LatexMatrixWriter(
                    dc, code.getCodeParameters(),
                    code.getGeneratorMatrix(), matrixLabel, delimiter,
                    id, spaceBetweenDelimiter, style, printPathToConsole
            );
            lmw.writeMatrixAsLatex();
        }

        if (cep.isWeightEnumeratorAsLatexWritable()) {
            LatexWeightEnumeratorWriter lwel = new LatexWeightEnumeratorWriter(
                    dc, code.getCodeWeightEnumerator(),
                    code.getCodeParameters(), id, printPathToConsole
            );
            lwel.writeWeightEnumeratorAsLatex("x");
        }
        if (cep.isGMatrixMatlabWritable()) {
            MatlabCodeWriter mcw = new MatlabCodeWriter(
                    dc,
                    code.getCodeParameters(),
                    code.getGeneratorMatrix(),
                    id,
                    printPathToConsole
            );
            mcw.writeCodeToFile();
        }
        System.out.println(Functions.writeConsoleLineSeparator());
    }

    /**
     * Returns the complete path of the directory to output to.
     *
     * @return the complete path of the directory to output to
     */
    public String getCompletePath() {
        return dc.getCompletePath();
    }

    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running CodeExporter...");
//        String path = Paths.DEFAULT_PATH;
//        byte n = 7;
//        byte k = 4;
//        byte d = 3;
//        byte base = 4;
//        boolean printPathToConsole = true;
//        CodeParameters cp = new CodeParameters(n, k, d, base);
//        CodeValidatorParameters cvp = new CodeValidatorParameters();
//        CodeExporterParameters cep = new CodeExporterParameters();
//        Code code = new Code(cp, cvp);
//        code.startEngine();
//        code.printGeneratorMatrix(true, Style.DECIMAL, true);
//        CodeExporter ce = new CodeExporter(path, cep, code, printPathToConsole);
//        ce.export(
//                "G",            //label of the generator matrix
//                " ",            //delimiter between generator matrix's columns
//                "0",            //the id of the generator matrix
//                true,           //surround delimiter by space
//                Style.DECIMAL   //the style of digits
//        );
//        System.out.println("CodeExporter completed.");
//    }
}