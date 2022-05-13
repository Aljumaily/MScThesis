package hlcd.run;

import hlcd.Paths;
import hlcd.parameters.CodeParameters;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

/**
 * Runs the list of parameters and test all the parameters read (parameters
 * with \(d \ge 3\)). It has the ability to increase or decrease the minimum
 * distance read by some offset. The {@code offset} is used when the minimum
 * distance needs to be incremented/decremented, otherwise, keep {@code
 * offset} as {@code 0} to find the true minimum distance specified in the
 * file. The offset is used when checking if there exists a more optimal
 * value than the current optimal value found or a suboptimal value that is
 * less than the best minimum distance found. It will run each parameter read
 * and use the default properties that can be found in the
 * {@link TestParameter} class. There will a {@code .txt} file summary generated
 * stating whether a code is valid or not for each parameter. A typical list
 * that can be read is found in the documentation of {@link ParametersLoader}.
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running ListExecutor...");
 *     String savePath = Paths.DEFAULT_PATH;
 *     String parametersPath = Paths.PARAMETERS_PATH;
 *     byte offset = 0;
 *     boolean beep = true;
 *     ListExecutor pes = new ListExecutor(
 *         savePath, parametersPath, offset, beep
 *     );
 *     System.out.println("ListExecutor completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 19th, 2022)
 * @see TestParameter
 * @since 1.8
 */
public class ListExecutor {
    //todo: add the date as well!
    private String filename = "summary";
    private String extension = ".txt";

    /**
     * Initializes the test of running optimal parameters read from a file.
     *
     * @param savePath      the path to save the summary of the run as well
     *                      as the data that will be exported
     * @param parameterPath the path of the parameters file
     * @param offset        the value for incrementing/decrementing the optimal
     *                      distance found. Keep it as {@code 0} if the true
     *                      optimal distance needs to be tested
     * @param beep          plays a beep sound to alter a single test has
     *                      completed
     */
    public ListExecutor(
            String savePath,
            String parameterPath,
            byte offset,
            boolean beep
    ) {
        //create new file
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd_hh-mm-ss-SSSa"
            );
            String timestamp = sdf.format(new Date());

            //The complete path depends on whether the save path is empty. By
            //default, completePath is initialized to the default location
            //of where this application is running from.
            String completePath = filename + timestamp + extension;
            if (!savePath.isEmpty()) {
                completePath = savePath + "\\" + completePath;
            }
            //the summary txt file to create
            FileWriter file = new FileWriter(completePath);

            PrintWriter writer = new PrintWriter(file, true);

            ParametersLoader pl = new ParametersLoader(parameterPath, offset);
            LinkedList<CodeParameters> parameters = pl.getParameters();

            for (int i = 0; i < parameters.size(); i++) {
                CodeParameters p = parameters.get(i);
                String summary;
                System.out.println(p);
                TestParameter tp = new TestParameter(savePath, p);

                if (tp.isValidCode()) {
                    summary = "Valid";
                } else {
                    summary = "INVALID!";
                }

                if (p.getD() < 3) {
                    summary = "Parameter is skipped because d is less than 3";
                }
                writeLine(writer, p, summary);

                if (beep) {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
            //close the file
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLine(PrintWriter writer, CodeParameters p, String summary) {
        //System.out.println(summary);
        writer.write(p.toString());
        writer.write(" //" + summary);
        writer.write("\n");
        writer.flush();//writes what is already stored in the buffer. That way,
        //we are able to write the data without the need to apply the close()
        //method.
    }

    /**
     * Executes the program.
     *
     * @param args the arguments specified but will be ignored
     */
    public static void main(String[] args) {
        System.out.println("Running ListExecutor...");
        String savePath = Paths.DEFAULT_PATH;
        String parametersPath = Paths.PARAMETERS_PATH;
        byte offset = 0;
        boolean beep = true;
        ListExecutor pes = new ListExecutor(
                savePath, parametersPath, offset, beep
        );
        System.out.println("ListExecutor completed.");
    }
}
