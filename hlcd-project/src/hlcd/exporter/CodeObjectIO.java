package hlcd.exporter;

import java.io.*;

import hlcd.linearCode.Code;

/**
 * Exports the code found to file as a persistent object ({@code .bin} file).
 * Unlike {@link CodeExporter}, this will automatically override the file if it
 * already exists because it doesn't use a timestamp.
 *
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running CodeObjectIO...");
 *     byte n = 7;
 *     byte k = 4;
 *     byte d = 3;
 *     byte base = 4;
 *     boolean printPathToConsole = true;
 *     CodeParameters cp = new CodeParameters(n, k, d, base);
 *     CodeValidatorParameters cvp = new CodeValidatorParameters();
 *     CodeExporterParameters cep = new CodeExporterParameters();
 *     Code code = new Code(cp, cvp);
 *     code.startEngine();
 *     CodeObjectIO co = new CodeObjectIO(
 *        code,               //the code object to export
 *        Paths.DEFAULT_PATH, //path to write to
 *        "0",                //the id of the generator matrix
 *        printPathToConsole  //should print the path of the file
 *     );
 *     System.out.println("The generator matrix of the code to be written:");
 *     code.printGeneratorMatrix(true, Style.DECIMAL, true);
 *     co.writeCodeObject();
 *     Code c2 = co.readCodeObject();
 *     System.out.println("The generator matrix of the code read:");
 *     code.printGeneratorMatrix(true, Style.DECIMAL, true);
 *     System.out.println("CodeObjectIO completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 24th, 2022)
 * @since 1.8
 */
public class CodeObjectIO {
    private String filepath;
    private String filename;
    private String extension = ".bin";
    private Code code;
    private String id;
    private boolean printPathToConsole;

    /**
     * Initializes the Code object I/O based on the parameters specified.
     * Requires the filepath as well as the code found in order to write it
     * as a persistent object.
     *
     * @param filepath           the filepath to write the {@code .bin} file in
     * @param code               the code to write as a persistent object
     * @param id                 the id of the code instance (can be left as
     *                           {@code "0"})
     * @param printPathToConsole should the file paths generated be printed
     *                           to console
     */
    public CodeObjectIO(
            Code code,
            String filepath,
            String id,
            boolean printPathToConsole
    ) {
        this.code = code;
        this.filepath = filepath;
        this.id = id;
        this.printPathToConsole = printPathToConsole;

        filename = "CodeObject_" + code.getCodeParameters().toString();
        if (!id.equals("")) {
            filename = filename + "_ID_" + id;
        }
    }

    /**
     * Writes the code object that was passed in the constructor.
     */
    public void writeCodeObject() {
        try {
            String route = filepath + "\\" + filename + extension;
            if (printPathToConsole) {
                System.out.println(route);
            }
            FileOutputStream f = new FileOutputStream(route);
            ObjectOutputStream o = new ObjectOutputStream(f);
            // Write object to file
            o.writeObject(code);
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the {@code .bin} file that was previously written.
     * <p>
     * TODO: This is not a complete method to use when running the program.
     *  It needs to be appropriately implemented so that it reads specified
     *  filename in a specific folder. Maybe pass the filepath as well as the
     *  filename here.
     *
     * @return the code object that was read
     */
    public Code readCodeObject() {
        try {
            String route = filepath + "\\" + filename + extension;
            FileInputStream fi = new FileInputStream(route);
            ObjectInputStream oi = new ObjectInputStream(fi);
            // Read object from .bin file
            Code c = (Code) oi.readObject();
            oi.close();
            fi.close();
            return c;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream in readCodeObject");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running CodeObjectIO...");
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
//        CodeObjectIO co = new CodeObjectIO(
//                code,               //the code object to export
//                Paths.DEFAULT_PATH, //path to write to
//                "0",                //the id of the generator matrix
//                printPathToConsole  //should print the path of the file
//        );
//        System.out.println("The generator matrix of the code to be written:");
//        code.printGeneratorMatrix(true, Style.DECIMAL, true);
//        co.writeCodeObject();
//        Code c2 = co.readCodeObject();
//        System.out.println("The generator matrix of the code read:");
//        code.printGeneratorMatrix(true, Style.DECIMAL, true);
//        System.out.println("CodeObjectIO completed.");
//    }
}
