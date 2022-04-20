package hlcd.exporter;

import hlcd.operations.DirectoryCreator;
import hlcd.operations.Matrix;
import hlcd.operations.MatrixPrinter;
import hlcd.parameters.CodeParameters;
import hlcd.enums.Style;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exports the generator matrix \(G\) of the code \(\mathsf{C}\) as a {@code
 * .tex} file. Similar to {@link CodeExporter}, this will <em>not</em>
 * automatically override the file if it already exists because it does use a
 * timestamp.
 * <p>
 * The {@code .tex} file that will be generated assumes there exist a \(\LaTeX\)
 * environment named {@code optimalCodeMatrix}. Here is a complete but
 * minimal example showing the definition of {@code optimalCodeMatrix}:
 * <pre><code class="language-latex line-numbers">&#92;documentclass[12pt]{article}
 * &#92;usepackage{array}
 * &#92;usepackage{ifthen}
 *
 * &#92;newcolumntype{:}{%
 *  !{&#92;hspace{2.5pt}&#92;vrule width 1pt&#92;hspace{2.5pt}}
 * }
 * &#92;newcommand{&#92;nMinusK}[2]{&#92;the&#92;numexpr #1 - #2}
 * &#92;newcommand{&#92;code}[1][]{%
 *  &#92;ifthenelse{&#92;equal{#1}{}}%
 *  {&#92;ensuremath{[n, k, d]}}%
 *  {&#92;ensuremath{[#1]}}%
 * }
 * &#92;newenvironment{optimalCodeMatrix}[5][]
 * {
 * &#92;begin{equation}
 * &#92;arraycolsep=3pt
 * &#92;ifx&#38;#1&#38;% optional argument has been passed
 *     &#92;label{eq:#2_#3_#4_#5}
 *     #2_{{&#92;code[#3, &#92;, #4, &#92;, #5]}_4} =%
 * &#92;else% no optional argument :(
 *     &#92;label{eq:#2_#3_#4_#5_#1}
 *     #2^{#1}_{{&#92;code[#3, &#92;, #4, &#92;, #5]}_4} =%
 * &#92;fi
 * &#92;left[
 * &#92;begin{array}{&#64;{}*{#4}{c}:*{&#92;nMinusK{#3}{#4}}{c}&#64;{}}
 * }
 * {
 * &#92;end{array}
 * &#92;right]
 * &#92;end{equation}
 * }
 *
 * &#92;begin{document}
 * &#92;begin{optimalCodeMatrix}[]{G}{7}{4}{3}
 * 	1 &#38; 0 &#38; 0 &#38; 0 &#38; 0 &#38; 1 &#38; 1       &#92;&#92;
 * 	0 &#38; 1 &#38; 0 &#38; 0 &#38; 0 &#38; 1 &#38; &#92;omega  &#92;&#92;
 * 	0 &#38; 0 &#38; 1 &#38; 0 &#38; 1 &#38; 0 &#38; 1       &#92;&#92;
 * 	0 &#38; 0 &#38; 0 &#38; 1 &#38; 1 &#38; 0 &#38; &#92;omega  &#92;&#92;
 * &#92;end{optimalCodeMatrix}
 * &#92;end{document}</code></pre>
 * <p>
 * The above will render the following matrix:
 * <p>
 * \(
 * G_{\left[ 7, \, 4, \, 3\right]_4} =
 * \left[\begin{array}{cccc|ccc}
 * 1 &#38; 0 &#38; 0 &#38; 0 &#38; 0 &#38; 1 &#38; 1	\\
 * 0 &#38; 1 &#38; 0 &#38; 0 &#38; 0 &#38; 1 &#38; \omega	\\
 * 0 &#38; 0 &#38; 1 &#38; 0 &#38; 1 &#38; 0 &#38; 1	\\
 * 0 &#38; 0 &#38; 0 &#38; 1 &#38; 1 &#38; 0 &#38; \omega	\\
 * \end{array}\right]
 * \)
 *
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running LatexMatrixWriter...");
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
 *     DirectoryCreator dc = new DirectoryCreator( path, "LatexMatrixWriter");
 *     dc.createDirectory();
 *     LatexMatrixWriter lcw = new LatexMatrixWriter(
 *         dc,                        //directory creator
 *         code.getCodeParameters(),  //code parameters
 *         code.getGeneratorMatrix(), //generator matrix to write
 *         "G",                       //label of the generator matrix
 *         "&#38;",                       //delimiter between matrix columns
 *         "0",                       //the id of the generator matrix
 *         true,                      //surround delimiter by space
 *         Style.QUATERNARY,          //the style of digits
 *         printPathToConsole         //should print path to console
 *     );
 *     lcw.writeMatrixAsLatex();
 *     System.out.println("LatexMatrixWriter completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 24th, 2022)
 * @since 1.8
 */
public class LatexMatrixWriter {
    private String envName = "optimalCodeMatrix";
    private String extension = ".tex";
    private CodeParameters cp;
    private Matrix matrix;
    private String matrixLabel;
    private String delimiter;
    private String id;
    private boolean writeWeightEnumerator;
    private Style style;
    private BufferedWriter pen;
    private boolean printPathToConsole;

    /**
     * Initializes the LaTeX matrix writer based on the parameters specified.
     *
     * @param dc                    the directory creator object to ensure
     *                              the files are stored in the appropriate
     *                              place
     * @param cp                    the code parameters (<i>i.e.</i>, \(n\),
     *                              \(k\), \(d\), \(base\), etc.)
     * @param matrix                the generator matrix \(G\)
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
     * @param printPathToConsole    should the file paths generated be printed
     *                              to console
     */
    public LatexMatrixWriter(
            DirectoryCreator dc,
            CodeParameters cp,
            Matrix matrix,
            String matrixLabel,
            String delimiter,
            String id,
            boolean spaceBetweenDelimiter,
            Style style,
            boolean printPathToConsole
    ) {
        this.cp = cp;
        this.matrix = matrix;
        this.printPathToConsole = printPathToConsole;
        String n = String.format("%02d", cp.getN());
        String k = String.format("%02d", cp.getK());
        String d = String.format("%02d", cp.getD());
        String hlcdStatus = cp.isHLCD() ? "H" : "";
        String filename = "GeneratorMatrix_" + n + "_" + k + "_" + d + "_" +
                                  hlcdStatus + "_" + cp.getBase();
        if (!id.equals("")) {
            filename = filename + "_ID_" + id;
        }

        pen = dc.getNewCreatedFile(filename, extension, true, printPathToConsole);

        this.matrixLabel = matrixLabel;
        this.delimiter = delimiter;
        this.id = id;
        this.style = style;
    }

    /**
     * Writes the {@code .tex} to the directory passed in the constructor.
     */
    public void writeMatrixAsLatex() {
        try {
            byte n = cp.getN();
            byte k = cp.getK();
            byte d = cp.getD();
            byte base = cp.getBase();
            long[] matrixArray = matrix.getMatrixArrayCopy();

//            File file = new File(filepath + "\\" + filename + extension);
//            BufferedWriter pen = new BufferedWriter(new FileWriter(file));

            SimpleDateFormat sdf = new SimpleDateFormat(
                    "MMM dd, yyyy hh:mm:ss a"
            );
            String timestamp = sdf.format(new Date());
            String optionalArgument;
            String requiredArguments;
            String beginEnv;//\begin{optimalCodeMatrix}[id]{matrixName}{n}{k}{d}
            String endEnv;//\end{optimalCodeMatrix}

            if (id.equals("")) {
                optionalArgument = "[]";
                pen.write("% The code " + cp + " created at: " + timestamp);

            } else {
                optionalArgument = "[" + id + "]";
                pen.write("% The code " + cp + " with id " +
                                  id + " is created at: " + timestamp);
            }
            pen.newLine();
            requiredArguments = "{" + matrixLabel + "}" + "{" + n + "}"
                                        + "{" + k + "}" + "{" + d + "}";
            beginEnv = "\\begin{" + envName + "}" +
                               optionalArgument + requiredArguments;
            endEnv = "\\end{" + envName + "}";
            pen.write(beginEnv);
            pen.newLine();
            //writing actual matrix cells
            for (byte i = 0; i < matrixArray.length; i++) {
                String row = MatrixPrinter.vectorAsString(
                        matrixArray[i], n, base, " & ", Style.LATEX
                );
                pen.write("\t" + row);
                pen.newLine();
            }

            pen.write(endEnv);
            pen.newLine();
            pen.flush();//clears anything that is waiting to be written
            pen.close();
        } catch (IOException ex) {
            Logger.getLogger(LatexMatrixWriter.class.getName()).log(
                    Level.SEVERE, null, ex
            );
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
//        System.out.println("Running LatexMatrixWriter...");
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
//        DirectoryCreator dc = new DirectoryCreator(
//                path, "LatexMatrixWriter"
//        );
//        dc.createDirectory();
//
//        LatexMatrixWriter lcw = new LatexMatrixWriter(
//                dc,
//                code.getCodeParameters(),   //code parameters
//                code.getGeneratorMatrix(),  //generator matrix to write
//                "G",                        //label of the generator matrix
//                "&",                        //delimiter between matrix columns
//                "0",                        //the id of the generator matrix
//                true,                       //surround delimiter by space
//                Style.QUATERNARY,           //the style of digits
//                printPathToConsole
//        );
//        lcw.writeMatrixAsLatex();
//        System.out.println("LatexMatrixWriter completed.");
//    }
}
