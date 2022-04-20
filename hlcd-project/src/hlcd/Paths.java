package hlcd;

import hlcd.run.ComplexExecutor;
import hlcd.run.ListExecutor;
import hlcd.run.SimpleExecutor;

/**
 * A class contains the paths of reading/writing from/to directories and files.
 * The {@code PATH} variable contains the directory path of where the output
 * should be written to. The {@code PARAMETERS_PATH} variable contains the
 * directory <em>and</em> the file name of the parameters list to be tested.
 * The class {@link ListExecutor} is dedicated for reading and testing
 * <em>multiple</em> parameters. In the case where a single
 * run is needed, then the classes {@link SimpleExecutor} or
 * {@link ComplexExecutor} can be used instead.
 *
 * @author Maysara Al Jumaily
 * @version 1.0
 * @see ListExecutor
 * @since February 2nd, 2022
 */
public class Paths {

    private Paths() {
        //empty
    }

    /**
     * The path to save the output to.
     */
    public static final String DEFAULT_PATH = "";

    /**
     * The path (with the file name and extension) of the list of optimal
     * parameters.
     */
    public static final String PARAMETERS_PATH = "optimalParameters.txt";
}