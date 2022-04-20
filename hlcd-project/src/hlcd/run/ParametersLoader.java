package hlcd.run;

import hlcd.Paths;
import hlcd.parameters.CodeParameters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * A way to load a list of parameters of quaternary codes from a {@code .txt}
 * file. It could be used for two main reasons: the first is testing known
 * optimal parameters and the second testing <em>potential</em> optimal
 * parameters that are not known yet. It will load the parameters from a text
 * file by reading the \(n\), \(k\) and \(d\) values (the base is assumed to be
 * {@code 4}). A linkedlist of {@code Parameter}s is created and can be
 * obtained to test the parameters read. The {@code offset} that is specified
 * will increase/decrease the \(d\) value read. It can be set to {@code 0} if
 * the same {@code d} value read needs to be tested. The objective of
 * {@code offset} is to have the ability to test whether there exists a more
 * optimal minimum distance than the currently known minimum distance or find
 * suboptimal values. A typical parameter list could be:
 * <pre><code class="language-java line-numbers"> //Write a comment starting the line with two slashes
 * //OR, write the parameters and two slashes, as such: n, k, d // some text...
 * //Note that invalid entries will crash the program
 * 3, 1, 3
 * 3, 2, 2
 * //Length 4
 * 4, 1, 3
 * 4, 2, 2
 * 4, 3, 1
 * //Length 5
 * 5, 1, 5
 * 5, 2, 3
 * 5, 3, 2
 * 5, 4, 2</code></pre>
 * TODO: Check for invalid entries and skip them
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running ParametersLoader...");
 *     String path = Paths.PARAMETERS_PATH;
 *     byte offset = 0;
 *     ParametersLoader parameters = new ParametersLoader(path, offset);
 *     System.out.println("The parameters read from file are:");
 *     LinkedList&#60;CodeParameters&#62; data = parameters.getParameters();
 *     for (int i = 0; i &#60; data.size(); i++) {
 *         System.out.println(data.get(i));
 *     }
 *     System.out.println("ParametersLoader completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 19th, 2022)
 * @since 1.8
 */
public class ParametersLoader {
    private byte base = 4;
    private LinkedList<CodeParameters> parameters = new LinkedList<>();

    /**
     * Starts to read the parameters based on the path specified. In the case
     * where the minimum distance needs to be incremented/decremented, {@code
     * offset} is used for this, otherwise, keep {@code offset} as {@code 0}
     * to find the true minimum distance specified in the file. The offset is
     * used when checking if there exists a more optimal value than the
     * current optimal value found.
     *
     * @param path   the path of the text file that contains the parameters
     *               to test
     * @param offset the value to increment/decrement the minimum distance
     *               read from the file
     */
    public ParametersLoader(String path, byte offset) {
        read(path, offset);
    }

    /**
     * Reads the parameters based on the specified path and offset. In the
     * case where the exact \(d\) value is needed to be read, ensure the
     * value of {@code offset} to be {@code 0}.
     *
     * @param path   the path of the text file that contains the parameters
     * @param offset the value to increment/decrement the minimum distance
     */
    private void read(String path, byte offset) {
        try {
            System.out.println(path);
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("//")) {//not a comment line
                    String[] values = line.split(",");
                    byte n = toByteValue(values[0]);
                    byte k = toByteValue(values[1]);
                    byte d = (byte) (toByteValue(values[2]) + offset);
                    //System.out.println(n + ", " + k + ", " + d);
                    parameters.add(new CodeParameters(n, k, d, base));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while opening the file.");

        }
    }

    /**
     * Converts the splitted {@code String} entry to a {@code byte}.
     *
     * @param value the splitted text
     * @return the splitted text as a byte
     */
    private byte toByteValue(String value) {
        String result = value.trim();
        int i = result.indexOf("/");
        if (i != -1) {//contains two slashes//as well
            result = result.substring(0, i);
        }
        return Byte.parseByte(result);
    }

    /**
     * Returns the parameters read from the file.
     *
     * @return the parameters read from the file
     */
    public LinkedList<CodeParameters> getParameters() {
        return parameters;
    }

    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running ParametersLoader...");
//        String path = Paths.PARAMETERS_PATH;
//        byte offset = 0;
//        ParametersLoader parameters = new ParametersLoader(path, offset);
//        System.out.println("The parameters read from file are:");
//        LinkedList<CodeParameters> data = parameters.getParameters();
//        for (int i = 0; i < data.size(); i++) {
//            System.out.println(data.get(i));
//        }
//        System.out.println("ParametersLoader completed.");
//    }
}
