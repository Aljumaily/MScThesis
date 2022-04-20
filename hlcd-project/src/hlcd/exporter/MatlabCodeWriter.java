package hlcd.exporter;

import hlcd.operations.DirectoryCreator;
import hlcd.operations.Matrix;
import hlcd.parameters.CodeParameters;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exports the code found to file as a Matlab {@code .m} file. The {@code .m}
 * file contains the generator matrix \(G\), the Hermitian transpose
 * \(\overline{G}^{T}\) and \(G^\prime = G \times \overline{G}^{T}\). Similar
 * to {@link CodeExporter}, this will <em>not</em> automatically override the
 * file if it already exists because it does use a timestamp. Also, ensures
 * that Matlab calculates \(G \times \overline{G}^{T}\), which must match the
 * matrix this program found. <b>Note: A significant bug has been encountered
 * in Matlab. Even in the latest (when writing this document) version R2021b
 * Update 2 (9.11.0.1847648) Dec 31 2021.</b> To see how it can be bypassed,
 * refer to section
 * <a href="https://dr.library.brocku.ca/bitstream/handle/10464/15405/Maysara_Al_Jumaily_MSc_Thesis.pdf?sequence=1&isAllowed=y#page=97&zoom=100,0,350"
 * target="_blank">8.2.6 Verifying Results with MATLAB</a>.
 *
 * <p>
 * A sample output could be:
 * <pre><code class="language-matlab line-numbers"> % The code (7, 4, 3)_4 Version  created at: Jan 24, 2022 08:18:35 PM
 * % It will test the validity of the code found.
 * % N    = 7
 * % K    = 4
 * % D    = 3
 * % BASE = 4
 *
 * clc
 * tic
 * fprintf("Run started %s\n\n", datestr(now,'mmmm dd, yyyy HH:MM:SS.FFF AM'))
 * m = 2;
 * G = gf([
 * 1 0 0 0 0 1 1;
 * 0 1 0 0 0 1 2;
 * 0 0 1 0 1 0 1;
 * 0 0 0 1 1 0 2
 * ], m);
 *
 * GConjugated = gf([
 * 1 0 0 0;
 * 0 1 0 0;
 * 0 0 1 0;
 * 0 0 0 1;
 * 0 0 1 1;
 * 1 1 0 0;
 * 1 3 1 3
 * ], m);
 *
 * GTimesGConj = gf([
 * 1 2 1 3;
 * 3 1 2 1;
 * 1 3 1 2;
 * 2 1 3 1
 * ], m);
 * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 * GMatlab = G;
 * GConjugatedMatlab = transpose(GMatlab).^2;
 * GTimesGConjMatlab = GMatlab * GConjugatedMatlab;
 * detThesis = 1;
 * detMatlab = det(GTimesGConjMatlab);
 * if ~isequal(GTimesGConj, GTimesGConjMatlab)
 * 	disp("Matlab conjugation didn't match the thesis's.")
 * end
 * if ~isequal(detThesis, detMatlab.x)
 * 	disp("Determinant didn't match.")
 * end
 * if isequal(detThesis, 0)
 * 	disp("Thesis determinant is zero.")
 * end
 * if isequal(detMatlab.x, 0)
 * 	disp("Matlab determinant is zero.")
 * end
 * if ~isequal(rank(G), size(G, 1)) % size(G, 1) is number of rows in matrix
 * 	disp("The rank of G is not full.")
 * end
 *
 * fprintf("Run ended %s\n\n", datestr(now,'mmmm dd, yyyy HH:MM:SS.FFF AM'))
 * beep
 * totalTime = seconds(toc);
 * totalTime.Format = 'hh:mm:ss.SSS';
 * fprintf('Total time elapsed: %s\n', char(totalTime));</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 26th, 2022)
 * @since 1.8
 */
public class MatlabCodeWriter {
    private CodeParameters cp;
    private Matrix matrix;
    private String extension = ".m";
    private String id;
    private BufferedWriter pen;
    private boolean printPathToConsole;

    /**
     * Initializes the Matlab code writer based on the parameters specified.
     *
     * @param dc                 the directory creator object to ensure the
     *                           files are stored in the appropriate place
     * @param cp                 the code parameters (<i>i.e.</i>, \(n\), \(k\),
     *                           \(d\), \(base\), etc.)
     * @param matrix             the generator matrix \(G\)
     * @param id                 The id that would be associated with the
     *                           matrix. Used when multiple generator
     *                           matrices of the same \(\left[n, \, k, \,
     *                           d\right]_{base}\) code are generated. Should
     *                           be {@code "0"} if only a single generator
     *                           matrix is found.
     * @param printPathToConsole should the file paths generated be printed
     *                           to console
     */
    public MatlabCodeWriter(
            DirectoryCreator dc,
            CodeParameters cp,
            Matrix matrix,
            String id,
            boolean printPathToConsole
    ) {
        this.cp = cp;
        this.matrix = matrix;
        this.id = id;
        this.printPathToConsole = printPathToConsole;
        //appends leading zeros from the left
        String n = String.format("%02d", cp.getN());
        String k = String.format("%02d", cp.getK());
        String d = String.format("%02d", cp.getD());
        String isHLCD = cp.isHLCD() ? "H" : "";

        //The name of the file
        String filename = "Matlab_" + n + "_" + k + "_" + d + "_" + isHLCD +
                                  "_" + cp.getBase();
        if (!id.equals("")) {
            filename = filename + "_ID_" + id;
        }
        pen = dc.getNewCreatedFile(filename, extension, true, printPathToConsole);
    }

    /**
     * Writes the {@code .m} file to path passed into the constructor
     * <em>without</em> creating a new folder.
     */
    public void writeCodeToFile() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ssa");
            String timestamp = sdf.format(new Date());
            //using String.format("%,d", ...) adds the commas in the number
            //for example: 1,000 and 55,652

            String codeParameter = "(" + cp.getN() + ", " + cp.getK() + ", " +
                                           cp.getD() + ")_" + cp.getBase() +
                                           " Version " +
                                           id;
            pen.write(
                    "% The code " + codeParameter + " created at: " + timestamp
            );
            pen.newLine();
            pen.write("% It will test the validity of the code found.");
            pen.newLine();
            pen.write("% N    = " + cp.getN());
            pen.newLine();
            pen.write("% K    = " + cp.getK());
            pen.newLine();
            pen.write("% D    = " + cp.getD());
            pen.newLine();
            pen.write("% BASE = " + cp.getBase());
            pen.newLine();
            pen.newLine();

            //writing the timestamp of the start of the program execution
            String matlabTime = "datestr(now,'mmmm dd, yyyy HH:MM:SS.FFF AM')";
            pen.write("clc");
            pen.newLine();
            pen.write("tic");
            pen.newLine();

            pen.write("fprintf(\"Run started %s\\n\\n\", " + matlabTime + ")");
            pen.newLine();

            ////////////////////////////////////////////////////////////////////
            String gLabel = "G";
            String ghtLabel = "GHT";//Hermitian transpose of G
            String gPrimeLabel = "GPrime";//G * Hermitian transpose of G
            Matrix g = matrix;
            Matrix ght = matrix.hermitianTranspose();
            Matrix gPrime = g.multiply(ght);
            byte base = cp.getBase();

            if (base == 2) {
                pen.write("m = 1;");
            } else if (base == 4) {
                pen.write("m = 2;");
            } else {
                pen.write("m = 0;% base passed is invalid (not 2 nor 4)");
            }

            writeMatrixToScript(pen, gLabel, g);//Generator matrix
            writeMatrixToScript(pen, ghtLabel, ght);//G Herm. tran.
            writeMatrixToScript(pen, gPrimeLabel, gPrime);//G prime

            byte det = gPrime.getDeterminant();

            writeMatlabValidation(gLabel, ghtLabel, gPrimeLabel, det, pen);

            pen.newLine();
            pen.write("fprintf(\"Run ended at %s\\n\\n\", " + matlabTime + ")");
            pen.newLine();

            pen.write("beep");
            pen.newLine();
            pen.write("totalTime = seconds(toc);");
            pen.newLine();
            pen.write("totalTime.Format = 'hh:mm:ss.SSS';");
            pen.newLine();
            pen.write("fprintf('Total time elapsed: %s\\n', char(totalTime));");
            pen.flush();//clears anything that is waiting to be written
            pen.close();
        } catch (IOException ex) {
            Logger.getLogger(MatlabCodeWriter.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    /**
     * Writes the matrix passed in Matlab's syntax to the file passed.
     *
     * @param pen    the file to write to
     * @param label  the label of the matrix such as {@code G}, {@code
     *               gTranspose}, etc.
     * @param matrix the matrix to be written
     */
    private void writeMatrixToScript(
            BufferedWriter pen,
            String label,
            Matrix matrix
    ) {
        byte n = matrix.getN();
        byte k = matrix.getK();
        byte base = matrix.getBase();
        try {
            pen.newLine();
            pen.write(label + " = ");
            pen.write("gf(");
            pen.write("[");
            pen.newLine();
            byte padding = (cp.getBase() == 2) ? n : (byte) (n * 2);
            byte delimiterPost = (base == 2) ? (byte) 1 : (byte) 2;
            String regexFormat = "(.{" + delimiterPost + "})(?!$)";//put the
            // delimiter every digit (base 2) or every two digit (base 4)

            for (byte i = 0; i < matrix.getMatrixArray().length; i++) {
                String result = String.format(
                        "%" + (int) padding + "s",
                        Long.toBinaryString(matrix.getMatrixArray()[i])
                );
                result = result.replace(" ", "0");//pad with 0's
                result = result.replaceAll(regexFormat, "$1" + " ");
                result = result.replaceAll("00", "0");
                result = result.replaceAll("01", "1");
                result = result.replaceAll("10", "2");
                result = result.replaceAll("11", "3");
                pen.write(result);
                if (i != matrix.getMatrixArray().length - 1) {
                    pen.write("; ");
                }
                pen.newLine();
            }
            pen.write("]");
            pen.write(", m);");
            pen.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ensures that the matrices found in this thesis matches what Matlab
     * outputs. It will use Matlab to obtain \(G^{\prime}_{k \times k} = G^{
     * \kern0pt}_{k \times n} \overline{G}^{T}_{n \times k}\) and ensures its
     * determinant matches the determinant found in this program. Also, it
     * will ensure that \(G\) has full rank.
     *
     * @param gLabel      label of the generator matrix
     * @param ghtLabel    label of the generator matrix Hermitian transposed
     * @param gPrimeLabel label of G prime
     * @param gPrimeDet   the determinant of G prime
     * @param pen         the file to write to
     */
    private void writeMatlabValidation(
            String gLabel,
            String ghtLabel,
            String gPrimeLabel,
            byte gPrimeDet,
            BufferedWriter pen
    ) {
        try {
            pen.write(new String(new char[80]).replace("\0", "%"));
            pen.newLine();
            String gMatlab = gLabel + "Matlab";
            String ghtMatlab = ghtLabel + "Matlab";
            String gPrimeMatlab = gPrimeLabel + "Matlab";
            String detThesis = "detThesis";
            String detMatlab = "detMatlab";

            pen.write(gMatlab + " = " + gLabel + ";");
            pen.newLine();
            pen.write(ghtMatlab + " = transpose(" + gMatlab + ").^2;");
            pen.newLine();
            pen.write(gPrimeMatlab + " = " + gMatlab + " * " +
                              ghtMatlab + ";");
            pen.newLine();

            pen.write(detThesis + " = " + gPrimeDet + ";");
            pen.newLine();
            pen.write(detMatlab + " = det(" + gPrimeMatlab + ");");
            pen.newLine();
            pen.write("if ~isequal(" + gPrimeLabel + ", " +
                              gPrimeMatlab + ")");
            pen.newLine();
            pen.write("\tdisp(\"Matlab Hermitian transpose didn't match the " +
                              "Java program.\")"
            );
            pen.newLine();
            pen.write("end");
            pen.newLine();

            pen.write("if ~isequal(" + detThesis + ", " + detMatlab + ".x)");
            pen.newLine();
            pen.write("\tdisp(\"Determinant didn't match.\")");
            pen.newLine();
            pen.write("end");
            pen.newLine();

            pen.write("if isequal(" + detThesis + ", " + 0 + ")");
            pen.newLine();
            pen.write("\tdisp(\"Thesis gPrimeDet is zero.\")");
            pen.newLine();
            pen.write("end");
            pen.newLine();

            pen.write("if isequal(" + detMatlab + ".x, " + 0 + ")");
            pen.newLine();
            pen.write("\tdisp(\"Matlab gPrimeDet is zero.\")");
            pen.newLine();
            pen.write("end");
            pen.newLine();

            pen.write(
                    "if ~isequal(rank(" + gLabel + "), size(" + gLabel + ", 1))"
            );
            pen.write("% size(" + gLabel + ", 1) is number of rows in matrix");
            pen.newLine();
            pen.write("\tdisp(\"The rank of G is not full.\")");
            pen.newLine();
            pen.write("end");
            pen.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//
//    public static void main(String[] args) {
//        System.out.println("Running MatlabCodeWriter...");
//        String path = Paths.DEFAULT_PATH;
//        String directoryName = "MatlabCodeWriter";
//        byte n = 7;
//        byte k = 4;
//        byte d = 3;
//        byte base = 4;
//        boolean printPathToConsole = true;
//        CodeParameters cp = new CodeParameters(n, k, d, base);
//        CodeValidatorParameters cvp = new CodeValidatorParameters();
//        CodeExporterParameters cep = new CodeExporterParameters();
//        Code code = new Code(cp, cvp);
//        code.startEngine();
//        code.printGeneratorMatrix(true, Style.DECIMAL, true);
//        DirectoryCreator dc = new DirectoryCreator(path, directoryName);
//        MatlabCodeWriter cw = new MatlabCodeWriter(
//                dc,
//                code.getCodeParameters(),   //the code parameters
//                code.getGeneratorMatrix(),  //the generator matrix of the code
//                "0",                        //the id of the generator matrix
//                printPathToConsole          //should the path be printed
//        );
//        cw.writeCodeToFile();
//        System.out.println("MatlabCodeWriter completed.");
//    }
}
