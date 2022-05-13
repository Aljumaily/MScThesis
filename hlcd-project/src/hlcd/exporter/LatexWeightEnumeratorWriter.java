package hlcd.exporter;

import hlcd.operations.DirectoryCreator;
import hlcd.parameters.CodeParameters;
import hlcd.operations.WeightEnumerator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exports the weight enumerator of some code \(\mathsf{C}\) as a {@code .tex}
 * file. Similar to {@link CodeExporter}, this will <em>not</em>
 * automatically override the file if it already exists because it does use a
 * timestamp.
 * <p>
 * The {@code .tex} file that will be generated assumes there exist a \(\LaTeX\)
 * environment named {@code weightEnumerator}. Here is a complete but
 * minimal example showing the definition of {@code weightEnumerator}:
 * <pre><code class="language-latex line-numbers">&#92;documentclass[12pt]{article}
 * &#92;usepackage{ifthen}
 * &#92;usepackage{breqn}
 * &#92;usepackage{numprint}
 * &#92;npthousandsep{,}
 *
 * &#92;newcolumntype{:}{%
 *  !{&#92;hspace{2.5pt}&#92;vrule width 1pt&#92;hspace{2.5pt}}
 * }
 * &#92;newcommand{&#92;nMinusK}[2]{&#92;the&#92;numexpr #1 - #2}
 * &#92;newcommand{&#92;code}[1][]{%
 *  &#92;ifthenelse{&#92;equal{#1}{}}
 *  {&#92;ensuremath{[n, k, d]}}
 *  {&#92;ensuremath{[#1]}}%
 * }
 *
 * &#92;DeclareMathOperator{&#92;wtFunctionText}
 * {&#92;ensuremath{&#92;normalfont&#92;textsc{wt}}}
 * &#92;newcommand{&#92;wtFunction}[1]{&#92;ensuremath{&#92;wtFunctionText(#1)}}
 * &#92;newenvironment{weightEnumerator}[4][]{
 * &#92;begin{dmath}
 * 	&#92;ifx&#38;#1&#38;% optional argument has been passed
 * 	    &#92;label{eq:we#2_#3_#4}
 * 	    &#92;wtFunction{x}_{{&#92;code[#2, &#92;, #3, &#92;, #4]}_4} =%
 * 	&#92;else% no optional argument :(
 * 	    &#92;label{eq:we#2_#3_#4_#1}
 * 	    &#92;wtFunction{x}^{#1}_{{&#92;code[#2, &#92;, #3, &#92;, #4]}_4} =%
 * 	&#92;fi
 * }
 * {
 * &#92;end{dmath}
 * }
 *
 * &#92;begin{document}
 * &#92;begin{weightEnumerator}[]{23}{10}{9}
 * 	1&#92;,+&#92;, &#92;numprint{540}x^{9}&#92;,
 * 	+&#92;, &#92;numprint{1398}x^{10}&#92;,
 * 	+&#92;, &#92;numprint{4032}x^{11}&#92;,
 * 	+&#92;, &#92;numprint{11040}x^{12}&#92;,
 * 	+&#92;, &#92;numprint{27237}x^{13}&#92;
 * 	+&#92;, &#92;numprint{57126}x^{14}&#92;,
 * 	+&#92;, &#92;numprint{102417}x^{15}&#92;,
 * 	+&#92;, &#92;numprint{155403}x^{16}&#92;,
 * 	+&#92;, &#92;numprint{193542}x^{17}&#92;,
 * 	+&#92;, &#92;numprint{197562}x^{18}&#92;,
 * 	+&#92;, &#92;numprint{156714}x^{19}&#92;,
 * 	+&#92;, &#92;numprint{92772}x^{20}&#92;,
 * 	+&#92;, &#92;numprint{38169}x^{21}&#92;,
 * 	+&#92;, &#92;numprint{9498}x^{22}&#92;,
 * 	+&#92;,&#92;numprint{1125}x^{23}
 * &#92;end{weightEnumerator}
 * &#92;end{document}</code></pre>
 * <p>
 * The above will render the following:
 * <p>
 * \(
 * \begin{array}{l}
 * {\rm WT}({x})_{\left[23, \, 10, \, 9\right]_4} =
 * 1\,+\, 540x^{9}\,+\, 1,398x^{10}\,+\,
 * 4,032x^{11}\,+\, 11,040x^{12}\,\\%
 * \kern6.5em +\, 27,237x^{13}\,+\, 57,126x^{14}\,+\,
 * 102,417x^{15}\,+\, 155,403x^{16}\,\\%
 * \kern6.5em +\, 193,542x^{17}\,+\, 197,562x^{18}\,+\,
 * 156,714x^{19}\,\\%
 * \kern6.5em +\, 92,772x^{20}\,+\,
 * 38,169x^{21}\,+\, 9,498x^{22}\,+\,
 * 1,125x^{23}
 * \end{array}
 * \)
 *
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running LatexWeightEnumeratorWriter...");
 *     String path = Paths.DEFAULT_PATH;
 *     String directoryName = "LatexWeightEnumeratorWriter";
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
 *     DirectoryCreator dc = new DirectoryCreator(path, directoryName);
 *     dc.createDirectory();
 *     LatexWeightEnumeratorWriter lwew = new LatexWeightEnumeratorWriter(
 *         dc,                             //directory creator
 *         code.getCodeWeightEnumerator(), //the weight enumerator of code
 *         code.getCodeParameters(),       //the code parameters
 *         "0",                            //the id of the generator matrix
 *         printPathToConsole              //should the path be printed
 *     );
 *     lwew.writeWeightEnumeratorAsLatex("x");
 *     System.out.println("LatexWeightEnumeratorWriter completed.");
 * }
 * </code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 25th, 2022)
 * @since 1.8
 */
public class LatexWeightEnumeratorWriter {
    private String envName = "weightEnumerator";
    private String filename;
    private String extension = ".tex";
    private WeightEnumerator weightEnumerator;
    private CodeParameters cp;
    private String id;
    private BufferedWriter pen;
    private boolean printPathToConsole;

    /**
     * Initializes the LaTeX weight enumerator writer based on the parameters
     * specified.
     *
     * @param dc                 the directory creator object to ensure the
     *                           files are stored in the appropriate place
     * @param weightEnumerator   the weight enumerator of the code
     * @param cp                 the code parameters (<i>i.e.</i>, \(n\), \(k\),
     *                           \(d\), \(base\), etc.)
     * @param id                 The id that would be associated with the
     *                           matrix corresponding to the weight enumerator
     *                           that was passed. Used when multiple generator
     *                           matrices of the same \(\left[n, \, k, \,
     *                           d\right]_{base}\) code are generated. Should be
     *                           {@code "0"} if only a single generator matrix
     *                           is found.
     * @param printPathToConsole should the file paths generated be printed
     *                           to console
     */
    public LatexWeightEnumeratorWriter(
            DirectoryCreator dc,
            WeightEnumerator weightEnumerator,
            CodeParameters cp,
            String id,
            boolean printPathToConsole
    ) {
        this.weightEnumerator = weightEnumerator;
        this.cp = cp;
        this.id = id;
        this.printPathToConsole = printPathToConsole;
        String n = String.format("%02d", cp.getN());
        String k = String.format("%02d", cp.getK());
        String d = String.format("%02d", cp.getD());
        String hlcdStatus = cp.isHLCD() ? "H" : "";
        filename = "WeightEnumerator_" + n + "_" + k + "_" + d + "_" +
                hlcdStatus + "_" + cp.getBase();
        if (!id.equals("")) {
            filename = filename + "_ID_" + id;
        }
        pen = dc.getNewCreatedFile(filename, extension, true, printPathToConsole);
    }

    /**
     * Write the weight enumerator of the code that was passed in the
     * constructor.
     *
     * @param variable the name of the variable to use. It is suggested to
     *                 pass {@code "x"}.
     */
    public void writeWeightEnumeratorAsLatex(String variable) {
        try {

            //needed to write the timestamp in the file created
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "MMM dd, yyyy hh:mm:ss a"
            );
            String timestamp = sdf.format(new Date());
            String optionalArgument;
            String requiredArguments;
            String beginEnv;//\begin{weightEnumerator}[id]{n}{k}{d}
            String endEnv;//\end{weightEnumerator}

            if (id.equals("")) {
                optionalArgument = "[]";
                pen.write("% The weight enumerator of " + cp + " created " +
                        "at: " + timestamp);

            } else {
                optionalArgument = "[" + id + "]";
                pen.write("% The weight enumerator of " + cp + " with id " +
                        id + " is created at: " + timestamp);
            }
            pen.newLine();
            requiredArguments = "{" + cp.getN() + "}"
                    + "{" + cp.getK() + "}" + "{" + cp.getD() + "}";
            beginEnv = "\\begin{" + envName + "}" +
                    optionalArgument + requiredArguments;
            endEnv = "\\end{" + envName + "}";
            pen.write(beginEnv);
            pen.newLine();
            ////////////////////////////////////////////////////////////////////
            //for example: \begin{weightEnumerator}[1]{G}{15}{6}{7}
            long[] codeWeights = weightEnumerator.getWeightEnumerator();
            StringBuilder result = new StringBuilder();
            for (byte i = 0; i < codeWeights.length; i++) {
                long cellValue = codeWeights[i];
                if (cellValue != 0) {
                    result.append(getTerm(cellValue, variable, i));
                    if (i != codeWeights.length - 1) {
                        result.append("\\,+\\,");
                    }
                }
            }
            //if result ends with the text "\,+\,", remove the last five chars
            String temp = result.toString();
            if (temp.endsWith("\\,+\\,")) {
                temp = temp.substring(0, temp.length() - 5);
                result = new StringBuilder(temp);
            }
            result.append("\n");
            pen.write("\t" + result);
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

    /**
     * Returns a term that is formulated as \(cx^n\), where the values of {@code
     * constant}, {@code variable} and {@code power} are \(c\),\(x\) and
     * \(n\), respectively. The value of variable {@code constant} is
     * formatted so that there will be a comma to separate thousands.
     *
     * @param constant the number of codewords that have weight of {@code power}
     * @param variable the variable letter (suggested to use {@code "x"})
     * @param power    the weight of all the codewords
     * @return a term in the form of \(cx^n\)
     */
    private String getTerm(long constant, String variable, long power) {
        String result;
        if (power == 0) {
            if (constant != 1) {
                System.out.println("Warning: the code seems to have more than" +
                        " 1 vector of weight 0.");
            }
            result = constant + "";
        } else {
            result = " \\numprint{" + constant + "}" + variable + "^{" + power
                    + "}";
        }
        return result;
    }

    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//
//    public static void main(String[] args) {
//        System.out.println("Running LatexWeightEnumeratorWriter...");
//        String path = Paths.DEFAULT_PATH;
//        String directoryName = "LatexWeightEnumeratorWriter";
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
//        DirectoryCreator dc = new DirectoryCreator(path, directoryName);
//        dc.createDirectory();
//        LatexWeightEnumeratorWriter lwew = new LatexWeightEnumeratorWriter(
//                dc,
//                code.getCodeWeightEnumerator(),//the weight enumerator of code
//                code.getCodeParameters(),      //the code parameters
//                "0",                           //the id of the generator matrix
//                printPathToConsole             //should the path be printed
//        );
//        lwew.writeWeightEnumeratorAsLatex("x");
//        System.out.println("LatexWeightEnumeratorWriter completed.");
//    }

}
