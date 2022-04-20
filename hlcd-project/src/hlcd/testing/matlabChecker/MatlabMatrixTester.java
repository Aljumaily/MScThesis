package hlcd.testing.matlabChecker;

import hlcd.Paths;
import hlcd.enums.Style;
import hlcd.operations.DirectoryCreator;
import hlcd.operations.Matrix;
import hlcd.operations.MatrixPrinter;
import hlcd.operations.RandomMatrixGenerator;
import hlcd.operations.Benchmarker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates a Matlab file to test the logic of transposing a matrix, finding
 * the Hermitian transpose of a matrix and matrix multiplication. A random
 * Quaternary matrix \(G\) will be generated. The transpose will be calculated,
 * the Hermitian transpose will also be calculated and both the transpose and
 * the Hermitian transpose will be multiplied to obtain \(G^\prime\).
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running MatlabMatrixTester...");
 *     MatlabMatrixTester mmt = new MatlabMatrixTester();
 *     System.out.println("MatlabMatrixTester completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 15th, 2022)
 * @see MatlabDeterminantTester
 * @since 1.8
 */
public class MatlabMatrixTester {
    private String path = Paths.DEFAULT_PATH;
    /**
     * The number of files to create.
     */
    private long runs = 10;
    /**
     * The number of tests each file will have.
     */
    private long iterations = 50;

    /* The range for the dimension of the randomly generated matrix. */

    private byte minK = 1;
    private byte maxK = 30;
    private byte minN = 1;
    private byte maxN = 30;

    private byte base = 4;

    /* labels used in naming the matrices in Matlab */
    private String gLabel = "G";//the randomly generated matrix g
    private String gtLabel = "gT";//transpose of g
    private String ghtLabel = "gHT";//Hermitian transpose of g
    private String gPrimeLabel = "gPrime";//g * Hermitian transpose of g

    /**
     * The order of primitive polynomial which is 1 for binary data and 2 for
     * quaternary. It is given by 2^q which is 2 for binary and 4 for
     * quaternary.
     */
    private byte q = (byte) ((base == 2) ? 1 : 2);//used in matlab

    /**
     * The script name which will run all the created files.
     */
    private String scriptName = "matlab_matrix_multiplication_script_execution";

    /**
     * The extension of a Matlab file.
     */
    private String extension = ".m";

    /**
     * The directory name to insert the script and the files created.
     */
    private String directoryName = "MatlabTester_K_" + minK + "_" +
                                           maxK + "_N_" + minN + "_" + maxN + "_Base_" +
                                           base + "_iterations_" + iterations + "_runs_" + runs;

    /**
     * Initiates the Matlab file creation for testing the transpose,
     * Hermitian transpose and matrix multiplication of randomly generated
     * matrices.
     */
    public MatlabMatrixTester() {
        Benchmarker mark = new Benchmarker();
        mark.start();
        mark.displayStartTime("Started: ");

        DirectoryCreator dc = new DirectoryCreator(path, directoryName);
        dc.createDirectory();
        //The title is used to write a sentence or two in the execution
        // script elaborating what is the script is about.
        String title = "" +
                               "Will test the logic of transposing a matrix, finding the" +
                               "\n% Hermitian transpose of a matrix and multiplying matrices.";
        TesterParameters tp = new TesterParameters(
                title, minK, maxK, minN, maxN, runs, iterations,
                dc.getCompletePath(), dc.getDirectoryName(), scriptName,
                extension
        );

        //generate the execution script
        MatlabScriptCreator msm = new MatlabScriptCreator(tp, dc);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        //creating the appropriate number of files
        for (int i = 0; i < runs; i++) {

            System.out.println("Creating file " + (i + 1) + " out of " +
                                       runs + " at: " + Benchmarker.getCurrentDateFormatted()
            );
            String version = "v" + getPaddedFileName(i);
            try {
                //very first run, also write the number of iterations
                BufferedWriter pen = dc.getNewCreatedFile(
                        version, extension, true, false
                );
                if (i == 0) {
                    pen.write("fprintf(\"There will be %d runs and each run " +
                                      "has %d iterations\\n\", " + runs + ", " +
                                      iterations + ")" + "\n");
                }
                //write the data of the file
                writeRun(i, pen);
                pen.flush();//clears anything that is waiting to be written
                pen.close();
            } catch (IOException ex) {
                Logger.getLogger(MatlabDeterminantTester.class.getName()).log(
                        Level.SEVERE, null, ex
                );
            }
        }
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
        mark.beep();
        System.out.println("All " + runs + " have been created under" +
                                   " the following directory:\n" + dc.getCompletePath());
    }

    /**
     * Writes the data of a single run file based on the current run value.
     *
     * @param currentRun the \(i^{th}\) run being written
     */
    private void writeRun(long currentRun, BufferedWriter pen) {
        int id = 0;
        try {
            String fileNamePadded = getPaddedFileName(currentRun);
            //writing the timestamp of the start of the program execution
            String dateFormat = "datestr(now,'mmmm dd, yyyy HH:MM:SS.FFF AM')";
            pen.write("fprintf(\"Run " + fileNamePadded + " started at " +
                              "%s\\n\", " + dateFormat + ")");
            pen.newLine();
            pen.write("q = " + q + ";");//The order of primitive polynomial
            pen.newLine();

            //writes x different tests, where x is the number of iterations
            for (int i = 0; i < iterations; i++) {
                RandomMatrixGenerator rmg = new RandomMatrixGenerator(
                        minN, maxN, minK, maxK, base
                );
                Matrix g = rmg.getMatrix();
                Matrix gTranspose = g.transpose();
                Matrix gHermitianTranspose = g.hermitianTranspose();
                Matrix gPrime = g.multiply(gHermitianTranspose);

                //writes (in order) four matrices:
                //g, transpose of g, Hermitian transpose of g, and g prime
                writeMatrixToFile(id, g, gLabel, pen);
                writeMatrixToFile(id, gTranspose, gtLabel, pen);
                writeMatrixToFile(id, gHermitianTranspose, ghtLabel, pen);
                writeMatrixToFile(id, gPrime, gPrimeLabel, pen);
                id++;
            }
            //writing the timestamp of the end of the program execution
            if (currentRun == runs - 1) {
                pen.write("fprintf(\"The last run completed at %s\\n\\n\", "
                                  + dateFormat + ")");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a matrix to the file.
     *
     * @param id    the id of the matrix which is the \(i^{th}\) matrix being
     *              tested
     * @param m     the matrix to be written
     * @param label the matrix label or the Matlab variable name
     */
    private void writeMatrixToFile(long id, Matrix m, String label,
            BufferedWriter pen) {
        try {
            long[] matrixArray = m.getMatrixArray();
            byte n = m.getN();
            byte k = m.getK();
            pen.write(label + id + " = ");
            pen.write("gf ( ");
            pen.write("[");
            pen.newLine();

            //writing actual matrix cells
            for (byte i = 0; i < matrixArray.length; i++) {
                String row = MatrixPrinter.vectorAsString(
                        matrixArray[i], n, base, " ", Style.DECIMAL
                );
                pen.write("\t" + row);
                pen.newLine();
            }
            pen.write("]");
            pen.write(" , q);");
            pen.newLine();
        } catch (IOException ex) {
            Logger.getLogger(MatlabMatrixTester.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    /**
     * Writes Matlab's script to ensure performing transpose, Hermitian
     * transpose and matrix multiplication yield correct results.
     *
     * @param id the id of the matrix which is the \(i^{th}\) matrix being
     *           tested
     */
    private void writeMatrixValidator(int id, BufferedWriter pen) {
        try {
            //Ensuring the transpose of the random matrix is obtained correctly
            pen.write("if ~isequal(" +
                              gtLabel + id +
                              ", " +
                              "transpose(" + gLabel + id + ")" +
                              ")"
            );
            pen.newLine();
            pen.write("\tdisp(\"Incorrect transpose " + gLabel + id + "\")");
            pen.newLine();
            pen.write("\tmyResult = " + gtLabel + id);
            pen.newLine();
            pen.write("\tcorrect = " + "transpose(" + gLabel + id + ")");
            pen.newLine();
            pen.write("end");
            pen.newLine();

            //Ensuring the hermitian transpose of the random matrix is
            //obtained correctly
            pen.write("if ~isequal(" +
                              ghtLabel + id +
                              ", " +
                              "transpose(" + gLabel + id + ".^2)" +
                              ")"
            );
            pen.newLine();

            pen.write("\tdisp(\"Incorrect Hermitian transpose " + ghtLabel +
                              id + "\")");
            pen.newLine();
            pen.write("\tmyResult = " + ghtLabel + id);
            pen.newLine();
            pen.write("\tcorrect = " + "transpose(" + gLabel + id + ".^2)");
            pen.newLine();
            pen.write("end");
            pen.newLine();

            //Ensuring G prime is obtained correctly
            pen.write("if ~isequal(" +
                              gPrimeLabel + id +
                              ", " +
                              gLabel + id + " * " + "transpose(" + gLabel + id + ".^2)" +
                              ")"
            );
            pen.newLine();
            pen.write("\tdisp(\"Incorrect G prime " + gLabel + id + "\")");
            pen.newLine();
            pen.write("\tmyResult = " + gPrimeLabel + id);
            pen.newLine();
            pen.write("\tcorrect = " + gLabel + id + " * " + "transpose(" +
                              gLabel + id + ".^2)");
            pen.newLine();
            pen.write("end");
            pen.newLine();
        } catch (IOException ex) {
            Logger.getLogger(MatlabMatrixTester.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    /**
     * Pads the current iteration index with left-padded zeros based on the
     * total number of iterations. For example, if we have 100 iterations in
     * total (it <em>is</em> zero-based), then the \(7^{th}\) iteration will be
     * written as \(007\).
     *
     * @param index the index to be padded
     * @return the index left-padded with the appropriate number of zeros
     */
    private String getPaddedFileName(long index) {
        int padding = (runs - 1 + "").length();
        return String.format("%0" + padding + "d", index);
    }

    /* A sample execution of this class is commented out*/

//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running MatlabMatrixTester...");
//        MatlabMatrixTester mmt = new MatlabMatrixTester();
//        System.out.println("MatlabMatrixTester completed.");
//    }
}
