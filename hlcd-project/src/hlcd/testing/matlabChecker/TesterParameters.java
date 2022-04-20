package hlcd.testing.matlabChecker;

/**
 * A wrapper class containing the parameters needed to write a Matlab
 * script for testing the program. This is only used in the testing package.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 18th, 2022)
 * @since 1.8
 */
public class TesterParameters {
    private String title;
    private byte minK;
    private byte maxK;
    private byte minN;
    private byte maxN;
    private long runs;
    private long iterations;
    private String path;
    private String newDirectoryName;
    private String scriptName;
    private String extension;

    /**
     * Initializes the object properties.
     *
     * @param title            the title to include in the Matlab file as a
     *                         comment
     * @param minK             the minimum value \(k\) can be
     * @param maxK             the maximum value \(k\) can be
     * @param minN             the minimum value \(n\) can be
     * @param maxN             the maximum value \(n\) can be
     * @param runs             the number of runs specified (the number of
     *                         test files generated)
     * @param iterations       the number of tests in a single file generates
     * @param path             the path to store the {@code .m} files generated
     * @param newDirectoryName the name of the directory to create that
     *                         will be used to store all the files generated
     * @param scriptName       the name of the script that runs all files
     *                         generated
     * @param extension        the extension of each file generated (it
     *                         should be {@code ".m"})
     */
    public TesterParameters(
            String title,
            byte minK,
            byte maxK,
            byte minN,
            byte maxN,
            long runs,
            long iterations,
            String path,
            String newDirectoryName,
            String scriptName,
            String extension
    ) {
        this.title = title;
        this.minK = minK;
        this.maxK = maxK;
        this.minN = minN;
        this.maxN = maxN;
        this.runs = runs;
        this.iterations = iterations;
        this.path = path;
        this.newDirectoryName = newDirectoryName;
        this.scriptName = scriptName;
        this.extension = extension;
    }

    /**
     * Returns the path to write the files to <em>without</em> appending the
     * directory name that will be used.
     *
     * @return the path to write the files to <em>without</em> appending the
     * directory name that will be used
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the directory name that will be created to store the files
     * generated into.
     *
     * @return the directory name that will be created to store the files
     * generated into
     */
    public String getNewDirectoryName() {
        return newDirectoryName;
    }

    /**
     * Returns the title of the script that will be written as a comment in
     * the Matlab script.
     *
     * @return the title of the script that will be written as a comment
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the minimum value {@code k} can be.
     *
     * @return the minimum value {@code k} can be
     */
    public byte getMinK() {
        return minK;
    }

    /**
     * Returns the maximum value {@code k} can be.
     *
     * @return the maximum value {@code k} can be
     */
    public byte getMaxK() {
        return maxK;
    }

    /**
     * Returns the minimum value {@code n} can be.
     *
     * @return the minimum value {@code n} can be
     */
    public byte getMinN() {
        return minN;
    }

    /**
     * Returns the maximum value {@code n} can be.
     *
     * @return the maximum value {@code n} can be
     */
    public byte getMaxN() {
        return maxN;
    }

    /**
     * Returns the number of runs which is the same as the number of test
     * files created.
     *
     * @return the number of runs which is the same as the number of test
     * files created
     */
    public long getRuns() {
        return runs;
    }

    /**
     * Returns the number of iterations which is the number of tests in a
     * single file.
     *
     * @return number of iterations which is the number of tests in a single
     * file
     */
    public long getIterations() {
        return iterations;
    }

    /**
     * Returns the name of the script file that will execute all the test files.
     *
     * @return the name of the script file
     */
    public String getScriptName() {
        return scriptName;
    }

    /**
     * The extension of the files generated (which should be {@code .m})
     *
     * @return the extension of the files generated
     */
    public String getExtension() {
        return extension;
    }
}
