package hlcd.parameters;

/**
 * Stores the options of a code when being exported. The default constructor
 * will set all options to true and write the code object, the generator
 * matrix as \(\LaTeX\) and Matlab files, as well as the weight enumerator of
 * the code as a \(\LaTeX\) file.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 20th, 2022)
 * @since 1.8
 */
public class CodeExporterParameters {

    private boolean writeCodeObject;
    private boolean writeGMatrixAsLatex;
    private boolean writeWeightEnumeratorAsLatex;
    private boolean writeMatrixAsMatlab;

    /**
     * Writes all data to file: the code object, the generator matrix as
     * \(\LaTeX\) and Matlab files, as well as the weight enumerator of the code
     * as a \(\LaTeX\) file.
     */
    public CodeExporterParameters() {
        writeCodeObject = true;
        writeMatrixAsMatlab = true;
        writeGMatrixAsLatex = true;
        writeWeightEnumeratorAsLatex = true;
    }

    /**
     * Accepts the code being exported as well as the options associated with
     * it.
     *
     * @param writeCodeObject              should write the code as {@code .bin}
     *                                     file
     * @param writeMatrixAsMatlab          should write the code as a {@code .m}
     *                                     Matlab executable file
     * @param writeGMatrixAsLatex          should typeset the generator matrix
     *                                     as a {@code .tex} \(\LaTeX\) file
     * @param writeWeightEnumeratorAsLatex should typeset the weight
     *                                     enumerator of the code as a {@code
     *                                     .tex} \(\LaTeX\) file
     */
    public CodeExporterParameters(
            boolean writeCodeObject,
            boolean writeMatrixAsMatlab,
            boolean writeGMatrixAsLatex,
            boolean writeWeightEnumeratorAsLatex
    ) {
        this.writeCodeObject = writeCodeObject;
        this.writeMatrixAsMatlab = writeMatrixAsMatlab;
        this.writeGMatrixAsLatex = writeGMatrixAsLatex;
        this.writeWeightEnumeratorAsLatex = writeWeightEnumeratorAsLatex;
    }

    /**
     * Should the code object be written to file.
     *
     * @return {@code true} if the code object should be written to
     * file, {@code false} otherwise
     */
    public boolean isCodeObjectWritable() {
        return writeCodeObject;
    }

    /**
     * Sets the value of whether the code object should be written to file.
     *
     * @param status the new value to set as
     */
    public void setCodeObjectWritableStatus(boolean status) {
        this.writeCodeObject = status;
    }

    /**
     * Should write the generator matrix of the code as a {@code .tex} file.
     *
     * @return {@code true} if the generator matrix should be written
     * as a {@code .tex} file, {@code false} otherwise
     */
    public boolean isGMatrixLatexWritable() {
        return writeGMatrixAsLatex;
    }

    /**
     * Sets the value of whether the generator matrix should be written as a
     * {@code .tex} file.
     *
     * @param status the new value to set as
     */
    public void setGMatrixLatexWritableStatus(boolean status) {
        this.writeGMatrixAsLatex = status;
    }

    /**
     * Should write the weight enumerator of the code be written as a {@code
     * .tex} file.
     *
     * @return {@code true} if the weight enumerator should be written
     * as a {@code .tex} file, {@code false} otherwise
     */
    public boolean isWeightEnumeratorAsLatexWritable() {
        return writeWeightEnumeratorAsLatex;
    }

    /**
     * Sets the value of whether the weight enumerator should be written as a
     * {@code .tex} file.
     *
     * @param status the new value to set as
     */
    public void setWeightEnumeratorAsLatexStatus(boolean status) {
        this.writeWeightEnumeratorAsLatex = status;
    }

    /**
     * Should write the generator matrix of the code as a {@code .m} (Matlab)
     * file.
     *
     * @return {@code true} if the generator matrix should be written
     * as a {@code .tex} file, {@code false} otherwise
     */
    public boolean isGMatrixMatlabWritable() {
        return writeMatrixAsMatlab;
    }

    /**
     * Sets the value of whether the generator matrix should be written as a
     * {@code .m} (Matlab) file.
     *
     * @param status the new value to set as
     */
    public void setGMatrixMatlabWritableStatus(boolean status) {
        this.writeMatrixAsMatlab = status;
    }
}
