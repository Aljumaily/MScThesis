package hlcd.run;

import hlcd.parameters.CodeDisplayParameters;
import hlcd.parameters.CodeExporterParameters;
import hlcd.parameters.CodeParameters;
import hlcd.parameters.CodeValidatorParameters;

/**
 * A complete execution class that initializes all possible parameters in
 * this program from the \(n\), \(k\), \(d\) values to the code parameters, code
 * validator parameters, code export parameters and code display parameters.
 *
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running ComplexExecutor...");
 *     ComplexExecutor sr = new ComplexExecutor();
 *     System.out.println("ComplexExecutor completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 12th, 2022)
 * @see SimpleExecutor
 * @since 1.8
 */
public class ComplexExecutor {

    /**
     * Initializes all the parameters in of the execution.
     */
    public ComplexExecutor() {
        //Basic parameters:
        byte n = 5;
        byte k = 2;
        byte d = 3;
        byte base = 4;

        //Code parameters:
        byte rhsWeight = (byte) (d - 1);
        boolean isHLCD = true;
        boolean isMultithreaded = false;
        boolean appendIdentity = true;
        boolean restrictCodewordGeneration = true;

        //Code validator parameters:
        boolean checkDeterminant = true;
        boolean checkZeroVectors = true;
        boolean replicateLinearCombinations = true;
        boolean checkValidMinimumDistance = true;
        boolean checkUniqueness = true;
        boolean checkHLCDProperty = false;//should always be kept as false.
        boolean printInvalidEntries = true;
        boolean stopWhenFalseEncountered = true;

        //Code export parameters:
        boolean writeCodeObject = true;
        boolean writeMatrixAsMatlab = true;
        boolean writeGMatrixAsLatex = true;
        boolean writeWeightEnumeratorAsLatex = true;

        //Code display parameters:
        boolean printGeneratorMatrix = true;
        boolean printGeneratorGPrimeDet = true;
        boolean printGPrime = false;
        boolean printCombinations = false;
        boolean printCodeStatistics = true;
        boolean printWeightEnumerator = false;
        boolean printFilePath = true;

        CodeParameters cp = new CodeParameters(
                n, k, d, rhsWeight, base, isHLCD,
                isMultithreaded, appendIdentity,
                restrictCodewordGeneration
        );

        CodeValidatorParameters cvp = new CodeValidatorParameters(
                checkDeterminant, checkZeroVectors,
                replicateLinearCombinations, checkValidMinimumDistance,
                checkUniqueness, checkHLCDProperty, printInvalidEntries,
                stopWhenFalseEncountered
        );

        CodeExporterParameters cep = new CodeExporterParameters(
                writeCodeObject, writeMatrixAsMatlab, writeGMatrixAsLatex,
                writeWeightEnumeratorAsLatex
        );

        CodeDisplayParameters cdp = new CodeDisplayParameters(
                printGeneratorMatrix,
                printGeneratorGPrimeDet,
                printGPrime,
                printCombinations,
                printCodeStatistics,
                printWeightEnumerator,
                printFilePath
        );

        boolean beep = true;//beep at once the run completes

        //The tester...
        TestParameter tp = new TestParameter(cp, cvp, cep, cdp, beep);
    }

    /**
     * Executes the program.
     *
     * @param args the arguments specified but will be ignored
     */
    public static void main(String[] args) {
        System.out.println("Running ComplexExecutor...");
        ComplexExecutor sr = new ComplexExecutor();
        System.out.println("ComplexExecutor completed.");
    }
}
