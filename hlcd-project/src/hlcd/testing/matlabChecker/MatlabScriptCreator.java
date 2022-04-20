package hlcd.testing.matlabChecker;

import hlcd.operations.DirectoryCreator;
import hlcd.operations.Benchmarker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates the main Matlab script to run all the file(s) generated based on the
 * number of run specified in the Tester classes. This script will load and
 * execute each file with the {@code .m} extension in the same directory it is
 * written in. There is a condition in the Matlab script to skip the script
 * itself.
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running MatlabScriptCreator...");
 *     String title = "A temporary test";
 *     byte minK = 1;
 *     byte maxK = 2;
 *     byte minN = 3;
 *     byte maxN = 4;
 *     long runs = 5;
 *     long iterations = 6;
 *     String path = Paths.DEFAULT_PATH;
 *     String newDirectoryName = "Test";
 *     String scriptName = "TempTest";
 *     String extension = ".m";
 *     TesterParameters tp = new TesterParameters(
 *         title, minK, maxK, minN, maxN, runs, iterations,
 *         path, newDirectoryName, scriptName, extension
 *     );
 *     DirectoryCreator dc = new DirectoryCreator(path, newDirectoryName);
 *     dc.createDirectory();
 *     MatlabScriptCreator msm = new MatlabScriptCreator(tp, dc);
 *     Benchmarker.beep();
 *     System.out.println("MatlabScriptCreator completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 15th, 2022)
 * @see MatlabDeterminantTester
 * @see MatlabMatrixTester
 * @since 1.8
 */
public class MatlabScriptCreator {
    private DirectoryCreator dc;

    /**
     * Ensures to create a new directory based on the options given in
     * the {@code TesterParameters} specified. In the case where the
     * directory already exists, it will be deleted first and then created
     * again.
     *
     * @param tp the parameters of the tester options
     */
    public MatlabScriptCreator(TesterParameters tp) {
        Benchmarker mark = new Benchmarker();
        mark.start();
        mark.displayStartTime("Started: ");
        dc = new DirectoryCreator(tp.getPath(), tp.getNewDirectoryName());
        createScriptFile(tp);
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
    }

    /**
     * Ensures to create a new directory based on the options given in
     * the {@code TesterParameters} specified. In the case where the
     * directory already exists, it will be deleted first and then created
     * again.
     *
     * @param dc the directory creator object to ensure the files are stored
     *           in the appropriate place
     * @param tp the parameters of the tester options
     */
    public MatlabScriptCreator(TesterParameters tp, DirectoryCreator dc) {
        this.dc = dc;
        Benchmarker mark = new Benchmarker();
        mark.start();
        mark.displayStartTime("Started: ");
        createScriptFile(tp);
        mark.end();
        mark.displayEndTime("Ended: ");
        mark.displayLapsedTime("The time lapsed: ");
    }

    /**
     * Writes the script based on the options specified in the {@code
     * TesterParameters} and stores everything inside a newly created folder.
     *
     * @param tp the options of the tester parameters
     */
    private void createScriptFile(TesterParameters tp) {
        String scriptName = tp.getScriptName();
        String extension = tp.getExtension();

        //The complete path depends on whether the save path is empty. By
        //default, completePath is initialized to the default location
        //of where this application is running from.

        System.out.println("");
        System.out.println("");
        System.out.println(dc.getCompletePath());
        System.out.println("");
        System.out.println("");
        try {
            BufferedWriter pen = dc.getNewCreatedFile(scriptName, extension, false);

            writeScript(tp, pen);
            pen.flush();//clears anything that is waiting to be written
            pen.close();
        } catch (IOException ex) {
            Logger.getLogger(TesterParameters.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
        System.out.println("Results will be saved in the new directory " +
                                   "created:\n" + dc.getCompletePath());
    }

    private void writeScript(TesterParameters tp, BufferedWriter pen) {
        String scriptName = tp.getScriptName();
        String extension = tp.getExtension();
        SimpleDateFormat sdf = new SimpleDateFormat(
                "MMM dd, yyyy hh:mm:ss a"
        );
        String timestamp = sdf.format(new Date());
        //The start of preamble of the script
        try {
            pen.write("% Script created at: " + timestamp);
            pen.newLine();
            pen.write("% " + tp.getTitle());
            pen.newLine();
            pen.write("% Minimum K size: " + tp.getMinK());
            pen.newLine();
            pen.write("% Maximum K size: " + tp.getMaxK());
            pen.newLine();
            pen.write("% Minimum N size: " + tp.getMinN());
            pen.newLine();
            pen.write("% Maximum N size: " + tp.getMaxN());
            pen.newLine();
            pen.write("% Number of runs: " + commaSeparatedValue(tp.getRuns()));
            pen.newLine();
            pen.write("% Number of iterations in each run: "
                              + commaSeparatedValue(tp.getIterations()));
            pen.newLine();
            pen.write("% Total number of matrices to test: " +
                              commaSeparatedValue(tp.getIterations() * tp.getRuns()));
            pen.newLine();
            pen.write("clc");
            pen.newLine();
            pen.write("beep");
            pen.newLine();
            pen.write("tic");
            pen.newLine();
            //The end of preamble of the script
            pen.write("% Executing all .m files in current folder (other than" +
                              " this script).");
            //The start reading other .m files located in the same directory
            pen.newLine();
            pen.write("% The code is taken from:");
            pen.newLine();
            pen.write("% https://www.mathworks.com/matlabcentral/answers/");
            pen.newLine();
            pen.write("% 122681-is-there-a-code-to-open-and-run-all-m-" +
                              "files-in-a-folder\n");
            pen.write("fullPath\t= '" + tp.getPath() + "\\" + "';");
            pen.newLine();
            pen.write("list\t\t= dir(fullfile(fullPath, '*.m'));");
            pen.newLine();
            pen.write("numOfFiles\t= length(list);");
            pen.newLine();
            pen.write("success\t\t= false(1, numOfFiles);");
            pen.newLine();
            pen.write("for k = 1 : numOfFiles");
            pen.newLine();
            pen.write("\tfile = list(k).name;");
            pen.newLine();
            pen.write("\t% Skips the script file (i.e., this file)");
            pen.newLine();
            //Ensures the current .m file isn't the script itself
            pen.write("\tif strcmp(file, \"" + scriptName + extension +
                              "\") == 0");
            pen.newLine();
            pen.write("\t\ttry");
            pen.newLine();
            pen.write("\t\t\trun(fullfile(fullPath, file));");
            pen.newLine();
            pen.write("\t\t\tsuccess(k) = true;");
            pen.newLine();
            pen.write("\t\tcatch");
            pen.newLine();
            pen.write("\t\t\tfprintf('failed: %s\\n', file);");
            pen.newLine();
            pen.write("\t\tend");
            pen.newLine();
            //clearing Matlab's memory
            pen.write("\t\t% clears variables in memory");
            pen.newLine();
            pen.write("\t\t% Variable from MatlabDeterminantTester:");
            pen.newLine();
            pen.write("\t\tclear -regexp ^G\\d*$;");
            pen.newLine();
            pen.write("\t\t% Variables from MatlabMatrixTester:");
            pen.newLine();
            pen.write("\t\tclear -regexp ^gT\\d*$;");
            pen.newLine();
            pen.write("\t\tclear -regexp ^gHT\\d*$;");
            pen.newLine();
            pen.write("\t\tclear -regexp ^gPrime\\d*$;");
            pen.newLine();
            pen.write("\t\tfclose('all');");
            pen.newLine();
            pen.write("\t\tclose all;");
            pen.newLine();
            pen.write("\t\tclear functions;");
            pen.newLine();
            pen.write("\t\tbdclose('all');");
            pen.newLine();
            pen.write("\tend");
            pen.newLine();
            //Write the conclusion after completing the execution
            pen.write("end");
            pen.newLine();
            pen.write("beep");
            pen.newLine();
            pen.write("s = seconds(toc);");
            pen.newLine();
            pen.write("s.Format = 'hh:mm:ss';");
            pen.newLine();
            pen.write("s");
        } catch (IOException ex) {
            Logger.getLogger(TesterParameters.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    /**
     * Returns the value specified as a String with commas every three
     * column. For example, if the specified value is \(7319241\), then the
     * return value will be \(7,319,241\).
     *
     * @param v the value to be comma separated
     * @return the specified value with commas every three columns
     */
    private String commaSeparatedValue(long v) {
        return String.format("%,d", v);
    }

    /* A sample execution of this class is commented out*/

//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running MatlabScriptCreator...");
//        String title = "A temporary test";
//        byte minK = 1;
//        byte maxK = 2;
//        byte minN = 3;
//        byte maxN = 4;
//        long runs = 5;
//        long iterations = 6;
//        String path = Paths.DEFAULT_PATH;
//        String newDirectoryName = "Test";
//        String scriptName = "TempTest";
//        String extension = ".m";
//        TesterParameters tp = new TesterParameters(
//                title, minK, maxK, minN, maxN, runs, iterations,
//                path, newDirectoryName, scriptName, extension
//        );
//        DirectoryCreator dc = new DirectoryCreator(path, newDirectoryName);
//        dc.createDirectory();
//        MatlabScriptCreator msm = new MatlabScriptCreator(tp, dc);
//        Benchmarker.beep();
//        System.out.println("MatlabScriptCreator completed.");
//    }
}
