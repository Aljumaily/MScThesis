package hlcd.testing.matlabChecker;

import hlcd.Paths;
import hlcd.enums.Style;
import hlcd.operations.*;
import hlcd.operations.Benchmarker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates a Matlab file to ensure the correctness of the Bareiss Algorithm
 * implemented in this program for finding the determinant of quaternary
 * square matrices.
 *
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running MatlabDeterminantTester...");
 *     MatlabDeterminantTester mdt = new MatlabDeterminantTester();
 *     System.out.println("MatlabDeterminantTester completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 15th, 2022)
 * @see MatlabMatrixTester
 * @since 1.8
 */
public class MatlabDeterminantTester {
    private String path = Paths.DEFAULT_PATH;
    /**
     * The number of files to create.
     */
    private long runs = 5;
    /**
     * The number of tests each file will have.
     */
    private long iterations = 10;

    /* The range for the dimension of the randomly generated matrix. Note
    that the matrix generated will be a square because we can only find the
    determinant of square matrices.*/
    private byte minSize = 1;
    private byte maxSize = 30;
    private byte minK = minSize;
    private byte maxK = maxSize;
    private byte minN = minSize;
    private byte maxN = maxSize;

    private byte base = 4;

    /* labels used in naming the matrices in Matlab */
    private String matrixLabel = "G";//the randomly generated matrix g

    /**
     * The order of primitive polynomial which is 1 for binary data and 2 for
     * quaternary. It is given by 2^q which is 2 for binary and 4 for
     * quaternary.
     */
    private byte q = (byte) ((base == 2) ? 1 : 2);//used in matlab

    /**
     * The script name which will run all the created files.
     */
    private String scriptName = "matlab_determinant_script_execution";

    /**
     * The extension of a Matlab file.
     */
    private String extension = ".m";

    /**
     * The directory name to insert the script and the files created.
     */
    private String directoryName = "DeterminantTester_K_" + minK + "_" +
                                           maxK + "_N_" + minN + "_" + maxN + "_Base_" +
                                           base + "_iterations_" + iterations + "_runs_" + runs;

    /**
     * The file to write the data into.
     */
//    private BufferedWriter pen;

    /**
     * Initiates the Matlab file creation for testing the determinant found
     * using Bareiss Algorithm of randomly generated square matrices.
     */
    public MatlabDeterminantTester() {
        Benchmarker mark = new Benchmarker();
        mark.start();
        mark.displayStartTime("Started: ");
        String title = "" +
                               "Tests the determinant finding algorithm, Bareiss Algorithm," +
                               "\n% implemented in this program to find the determinant of" +
                               "\n% quaternary matrices in the Quaternary field.";
        DirectoryCreator dc = new DirectoryCreator(path, directoryName);
        dc.createDirectory();
        TesterParameters tp = new TesterParameters(
                title, minK, maxK, minN, maxN, runs, iterations,
                dc.getCompletePath(), dc.getDirectoryName(), scriptName, extension
        );

        //generate the execution script
        MatlabScriptCreator msm = new MatlabScriptCreator(tp, dc);

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
                                   " the following directory:\n" +
                                   dc.getCompletePath());
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
                byte dim = (byte) (Math.random() * (maxSize - minSize + 1) + 1);
                RandomMatrixGenerator rmg = new RandomMatrixGenerator(dim);
                Matrix matrix = rmg.getMatrix();
                byte detFound = matrix.getDeterminant();

                writeMatrixToFile(id, matrix, matrixLabel, pen);
                writeDeterminantValidator(id, detFound, pen);
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
     * Writes Matlab's script to ensure the determinant found in this program
     * using Bareiss Algorithm matches what Matlab finds.
     *
     * @param id       the id of the matrix which is the \(i^{th}\) matrix
     *                 being tested
     * @param detFound the determinant of the \(i^{th}\) matrix found in this
     *                 program
     */
    private void writeDeterminantValidator(long id, byte detFound, BufferedWriter pen) {
        try {
            pen.write("if det(" + matrixLabel + id + ") ~= " + detFound);
            pen.newLine();
            pen.write("\tdisp(\"" + matrixLabel + id + " is " + detFound +
                              ", should be\" + det(" + matrixLabel + id + ").x " + ")");
            pen.newLine();
            pen.write("end");
            pen.newLine();
        } catch (IOException ex) {
            Logger.getLogger(MatlabDeterminantTester.class.getName()).log(
                    Level.SEVERE, null, ex
            );
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
    private void writeMatrixToFile(
            long id,
            Matrix m,
            String label,
            BufferedWriter pen
    ) {
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
            Logger.getLogger(MatlabDeterminantTester.class.getName()).log(
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
        long padding = (runs - 1 + "").length();
        return String.format("%0" + padding + "d", index);
    }

    /* A sample execution of this class is commented out*/

//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running MatlabDeterminantTester...");
//        MatlabDeterminantTester mdt = new MatlabDeterminantTester();
//        System.out.println("MatlabDeterminantTester completed.");
//    }
}
