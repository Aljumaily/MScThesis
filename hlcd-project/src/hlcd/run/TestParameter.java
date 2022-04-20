package hlcd.run;

import hlcd.Paths;
import hlcd.enums.Style;
import hlcd.exporter.CodeExporter;
import hlcd.linearCode.Code;
import hlcd.linearCode.CodeValidator;
import hlcd.operations.DirectoryCreator;
import hlcd.operations.Functions;
import hlcd.parameters.*;
import hlcd.operations.Benchmarker;

import java.awt.*;

/**
 * Starts the execution of finding if the parameters specified of a code are
 * valid <i>automatically</i> once a constructor is called.
 *
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running TestParameter...");
 *     byte n = 8;
 *     byte k = 4;
 *     byte d = 4;
 *     byte base = 4;
 *     TestParameter tp = new TestParameter(n, k, d, base);
 *     System.out.println("TestParameter completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 12th, 2022)
 * @since 1.8
 */
public class TestParameter {
    private Code code;
    private CodeParameters cp;
    private CodeValidatorParameters cvp;
    private CodeExporterParameters cep;
    private CodeDisplayParameters cdp;
    private boolean isValidCode;
    private boolean beep;
    private String path;

    /**
     * Initiates the run of execution of the code based on the {@code
     * CodeParameters} specified. It will also run the validation of the code
     * which is assigned to run all testes <em>except</em> checking for the
     * Hermitian LCD property. Furthermore, it will export the code object, the
     * generator matrix as a \(\LaTeX\) file and a Matlab file as well as the
     * weight enumerator as a \(\LaTeX\) file. Lastly, a beep sound is played
     * once the search is completed.
     *
     * @param path the directory path to write the files to
     * @param p    the parameters of the code to be executed
     */
    public TestParameter(String path, CodeParameters p) {
        this.path = path;
        if (p.getD() >= 3) {
            cp = new CodeParameters(p.getN(), p.getK(), p.getD(), p.getBase());
            cvp = new CodeValidatorParameters();
            code = new Code(cp, cvp);
            cep = new CodeExporterParameters();
            cdp = new CodeDisplayParameters();
            beep = true;
            initEngine();
        } else {

            System.out.println("The parameter is skipped because d is less " +
                    "than 3.");
            System.out.println(Functions.writeConsoleLineSeparator());
        }
    }

    /**
     * Initiates the run of execution of the code based on the specified
     * parameters. It will also run the validation of the code which is
     * assigned to run all testes <em>except</em> checking for the Hermitian
     * LCD property. Furthermore, it will export the code object, the
     * generator matrix as a \(\LaTeX\) file and a Matlab file as well as the
     * weight enumerator as a \(\LaTeX\) file. Lastly, a beep sound is played
     * once the search is completed.
     *
     * @param n    the length of each codeword in the code
     * @param k    the dimension of the code
     * @param d    the minimum distance of the code
     * @param base the base of the code which could either be \(2\) or \(4\)
     */
    public TestParameter(byte n, byte k, byte d, byte base) {
        if (d >= 3) {
            cp = new CodeParameters(n, k, d, base);
            cvp = new CodeValidatorParameters();
            code = new Code(cp, cvp);
            cep = new CodeExporterParameters();
            cdp = new CodeDisplayParameters();
            beep = true;
            initEngine();
        } else {
            System.out.println("The parameter is skipped because d is less" +
                    " than 3.");
            System.out.println(Functions.writeConsoleLineSeparator());
        }
    }

    /**
     * Initiates the run of execution of the code based on the specified
     * parameters. It will also run the validation of the code which is
     * assigned to run all testes <em>except</em> checking for the Hermitian
     * LCD property. Furthermore, it will export the code object, the
     * generator matrix as a \(\LaTeX\) file and a Matlab file as well as the
     * weight enumerator as a \(\LaTeX\) file.
     *
     * @param n    the length of each codeword in the code
     * @param k    the dimension of the code
     * @param d    the minimum distance of the code
     * @param base the base of the code which could either be \(2\) or \(4\)
     * @param beep whether a beep sound should be played after the completion
     *             of the execution of the program
     */
    public TestParameter(byte n, byte k, byte d, byte base, boolean beep) {
        cp = new CodeParameters(n, k, d, base);
        cvp = new CodeValidatorParameters();
        cep = new CodeExporterParameters();
        cdp = new CodeDisplayParameters();
        this.beep = beep;
        code = new Code(cp, cvp);
        initEngine();
    }

    /**
     * Initializes the execution to the specified parameters (the most
     * customized executor).
     *
     * @param cp   the code parameters
     * @param cvp  the code validator parameters
     * @param cep  the code exporter parameters
     * @param cdp  the display code parameters
     * @param beep should the program play a beep sound once it completes
     *             running
     */
    public TestParameter(
            CodeParameters cp,
            CodeValidatorParameters cvp,
            CodeExporterParameters cep,
            CodeDisplayParameters cdp,
            boolean beep
    ) {
        this.cp = cp;
        this.cvp = cvp;
        this.cep = cep;
        this.cdp = cdp;
        this.beep = beep;
        code = new Code(cp, cvp);
        initEngine();
    }

    /**
     * Starts the search of finding a generator matrix that satisfy the
     * \(n\), \(k\) and \(d\) parameters of the code.
     */
    private void initEngine() {
        Benchmarker mark = new Benchmarker();
        mark.start();
        mark.displayStartTime("Execution started at: ");
        code.startEngine();//start the execution of program

        //getting the code engine started...
        displayerEngine();//display info on console

        //Ensure the generator matrix obtains doesn't have zero row(s)
        if (!code.getGeneratorMatrix().containsZeroRow()) {
            System.out.println("Engine timelapse: " +
                    code.getCodeStatistics().getLapsedTime()
            );
            //getting the validator and exporter started...
            Benchmarker m = new Benchmarker();
            m.start();
            codeValidatorEngine();
            codeExporterEngine();
            m.end();
            m.displayLapsedTime("Validator and exporter timelapse: ");
        } else {
            System.out.println("The code doesn't exist.");
        }
        mark.end();
        mark.displayEndTime("Execution completed at: ");
        mark.displayLapsedTime("Total duration of execution: ");
        System.out.println(Functions.writeConsoleLineSeparator());

        if (beep) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /**
     * Prints on the console the options (generator matrix, g prime's
     * determinant, etc.) that should be printed after completing the search.
     */
    private void displayerEngine() {
        System.out.println("The generator matrix of the code:");
        if (cdp.shouldPrintGeneratorMatrix()) {
            code.printGeneratorMatrix(true, Style.DECIMAL, true);
        }
        if (cdp.shouldPrintGPrimeDet()) {
            byte det = code.getGeneratorMatrix().getGPrime().getDeterminant();
            System.out.println("G prime determinant is: " + det);
        }
        if (cdp.shouldPrintCombinations()) {
            code.printCombinations(" ", Style.DECIMAL);
        }
    }

    /**
     * Executes the validator of the code found.
     */
    private void codeValidatorEngine() {
        CodeValidator cv = new CodeValidator(code, cvp);
        isValidCode = cv.isValidCode();
        if (isValidCode) {
            System.out.println("The code is valid!");
        } else {
            System.out.println("The code is INVALID!");
        }
    }

    /**
     * Exports the code data to file.
     */
    private void codeExporterEngine() {
        String path = Paths.DEFAULT_PATH;
        String directoryName = code.getCodeParameters().toString();
        DirectoryCreator dc = new DirectoryCreator(path, directoryName);
        CodeExporter ce = new CodeExporter(
                path, cep, code, cdp.shouldPrintFilePath()
        );
        ce.export("G", " ", "0", true, Style.QUATERNARY);
        System.out.println(ce.getCompletePath());
    }

    /**
     * Returns the code found.
     *
     * @return the code found
     */
    public Code getCode() {
        return code;
    }

    /**
     * Returns whether the code is valid after the tests have been completed.
     *
     * @return {@code true} if the code is valid, {@code false} otherwise
     */
    public boolean isValidCode() {
        return isValidCode;
    }

    /**
     * Executes the program.
     *
     * @param args the arguments specified but will be ignored
     */
    public static void main(String[] args) {
        System.out.println("Running TestParameter...");
        byte n = 8;
        byte k = 4;
        byte d = 4;
        byte base = 4;
        TestParameter tp = new TestParameter(n, k, d, base);
        System.out.println("TestParameter completed.");
    }
}