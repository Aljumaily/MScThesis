package hlcd.operations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The creation of directories and files is taken care of here. All created
 * folder and files will contain a timestamp at the end. There will not be
 * any deletion or overriding of data. In case the main path is empty, then
 * the directory where the project is being executed will be used as the
 * default location. Also, empty folder names or file names will be assigned
 * a random hexadecimal string of eight characters. Using the following:
 * <pre><code class="language-java line-numbers"> DirectoryCreator dc = new DirectoryCreator("path...", "test");
 * dc.createDirectory();//required!</code></pre>
 * to create a directory. The convention is to create a new folder as
 * soon as an instance is created.
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running DirectoryCreator...");
 *     String path = "";//Paths.DEFAULT_PATH;
 *     String directoryName = "";
 *     boolean addTimestamp = false;
 *     boolean override = true;
 *     boolean appendTimestamp = true;
 *     boolean printToConsole = true;
 *     DirectoryCreator dc = new DirectoryCreator(
 *         path, directoryName, printToConsole
 *     );
 *     dc.createDirectory();
 *     BufferedWriter f1 = dc.getNewCreatedFile(
 *         "", ".txt", appendTimestamp, printToConsole
 *     );
 *     BufferedWriter f2 = dc.getNewCreatedFile(
 *         "", ".txt", appendTimestamp, printToConsole
 *     );
 *     try {
 *         //First file
 *         f1.write("Some text in the first file...");
 *         f1.newLine();
 *         f1.flush();
 *         f1.close();
 *         //Second file
 *         f2.write("Some text in the second file...");
 *         f2.newLine();
 *         f2.write("More text :)!");
 *         f2.flush();
 *         f2.close();
 *     } catch (IOException e) {
 *         e.printStackTrace();
 *     }
 *     System.out.println("DirectoryCreator completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0
 * @since February 24th, 2022
 */
public class DirectoryCreator {
    /* Uses the directory where the project is being executed. */
    private final String DEFAULT_DIRECTORY = System.getProperty("user.dir");

    private String completePath;
    private String path;
    private final String DIRECTORY_NAME;
    private boolean printToConsole;

    /**
     * Initializes the object based on the path specified with the passed
     * directory name. It will by default print the directory created on the
     * console.
     *
     * @param path          the path to create the new folder in
     * @param directoryName the name of the new folder to create
     */
    public DirectoryCreator(String path, String directoryName) {
        this.path = path;
        this.DIRECTORY_NAME = directoryName;
        this.printToConsole = true;
        //completePath = getCompletePath(path, true, directoryName, "", true);
    }

    /**
     * Initializes the object based on the path specified with the passed
     * directory name.
     *
     * @param path           the path to create the new folder in
     * @param directoryName  the name of the new folder to create
     * @param printToConsole should the path of the new folder to create be
     *                       displayed on the console
     */
    public DirectoryCreator(
            String path,
            String directoryName,
            boolean printToConsole
    ) {
        this.path = path;
        this.DIRECTORY_NAME = directoryName;
        this.printToConsole = printToConsole;
        // completePath = getCompletePath(path, true, directoryName, "", true);
    }

    /**
     * Creates a new folder.
     */
    public void createDirectory() {
        completePath = getCompletePath(path, true, DIRECTORY_NAME, "", true);
        if (printToConsole) {
            System.out.println("The path of new directory: " + completePath);
        }
        File f = new File(completePath);
        f.mkdirs();
    }

    /**
     * Creates a new file in the path specified based on the other data passed.
     *
     * @param path            the path to create the file in
     * @param filename        the filename of the new file to be created
     * @param extension       the extension of the new file to be created
     *                        (for example {@code .txt})
     * @param appendTimestamp should a timestamp be added to the name
     * @param printToConsole  should the path of the new file to be created be
     *                        displayed on the console
     * @return the newly created file object
     */
    public BufferedWriter getNewCreatedFile(
            String path,
            String filename,
            String extension,
            boolean appendTimestamp,
            boolean printToConsole
    ) {
        return getNewCreatedFileEngine(
                path, filename, extension, appendTimestamp, printToConsole
        );
    }

    /**
     * Creates a new file in the path specified based on the other data passed.
     *
     * @param filename        the filename of the new file to be created
     * @param extension       the extension of the new file to be created
     *                        (for example {@code .txt})
     * @param appendTimestamp should a timestamp be added to the name
     * @param printToConsole  should the path of the new file to be created be
     *                        displayed on the console
     * @return the newly created file object
     */
    public BufferedWriter getNewCreatedFile(
            String filename,
            String extension,
            boolean appendTimestamp,
            boolean printToConsole
    ) {
        return getNewCreatedFileEngine(
                completePath, filename, extension,
                appendTimestamp, printToConsole
        );
    }

    /**
     * Creates a new file in the path specified based on the other data passed.
     * It will display the file path to console by default.
     *
     * @param filename        the filename of the new file to be created
     * @param extension       the extension of the new file to be created
     *                        (for example {@code .txt})
     * @param appendTimestamp should a timestamp be added to the name
     * @return the newly created file object
     */
    public BufferedWriter getNewCreatedFile(
            String filename,
            String extension,
            boolean appendTimestamp) {
        System.out.println(completePath);
        return getNewCreatedFileEngine(
                completePath, filename, extension, appendTimestamp, true
        );
    }

    /**
     * The engine for creating a new file.
     *
     * @param path           the path to create the file in
     * @param filename       the filename of the new file to be created
     * @param extension      the extension of the new file to be created
     *                       (for example {@code .txt})
     * @param printToConsole should the path of the new file to be created be
     *                       displayed on the console
     * @return the newly created file object
     */
    private BufferedWriter getNewCreatedFileEngine(
            String path,
            String filename,
            String extension,
            boolean appendTimestamp,
            boolean printToConsole
    ) {
        String fullPath = getCompletePath(
                path, false, filename, extension, appendTimestamp
        );
        if (printToConsole) {
            System.out.println("The path of new file: " + fullPath);
        }

        BufferedWriter pen = null;
        try {
            pen = new BufferedWriter(new FileWriter(fullPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pen;
    }

    /**
     * Create a new file in the path specified based on the other data passed.
     *
     * @param path        the path to create the file in
     * @param isDirectory whether it is a directory or not
     * @param name        the name of the new directory or file to be created.
     * @param extension   The extension of the file. In case it is a folder
     *                    name, then it should be empty <i>i.e.</i>, {@code ""}.
     * @return the complete path along with the file name and extension
     */
    private String getCompletePath(
            String path,
            boolean isDirectory,
            String name,
            String extension,
            boolean appendTimestamp
    ) {
        String completePath;
        String separation = "";//separation inserted between name & timestamp
        String timestamp = "";
        if (appendTimestamp) {
            timestamp = getCurrentTimestamp();
            separation = "__";
        }

        if (path.isEmpty()) {//use the directory the program was executed from
            completePath = DEFAULT_DIRECTORY + "\\";
        } else {
            completePath = path + "\\";
        }

        /* Empty file name */
        if (name.isEmpty()) {
            name = getRandomName();
        }

        if (!isDirectory) {
            completePath = completePath + name + separation + timestamp +
                                   extension;
        } else {
            completePath = completePath + name + separation + timestamp;
        }

        if (path.isEmpty()) {
            System.out.println("Warning: the original path specified is " +
                                       "empty. The directory used to run this program is used " +
                                       "instead: " + DEFAULT_DIRECTORY
            );
        }
        //a warning message if the name specified is empty
        if (name.isEmpty()) {
            if (isDirectory) {
                System.out.println("Warning: the directory name passed is " +
                                           "empty. The timestamp is used instead which is: " +
                                           timestamp
                );
            } else {
                System.out.println("Warning: The file name passed is empty. " +
                                           "The timestamp is used instead which is: " + timestamp
                );
            }
        }
        return completePath;
    }

    /**
     * Returns the current time using the following format: {@code
     * yyyy_MM_dd_hh_mm_ss_SSSa}.
     *
     * @return the current time using the following format: {@code
     * yyyy_MM_dd_hh_mm_ss_SSSa}
     */
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SSSa");
        return sdf.format(new Date());
    }

    /**
     * Generates a random hexadecimal string of eight characters. It is used
     * when a folder name or file name is empty. A random hexadecimal string
     * will be used as the name instead.
     *
     * @return a random hexadecimal string of eight characters
     */
    private String getRandomName() {
        int value = (int) (Math.random() * Integer.MAX_VALUE);
        return String.format("%08x", value);
    }

    /**
     * Returns the complete path of the directory created.
     *
     * @return the complete path of the directory created
     * @see DirectoryCreator#getDirectoryName()
     */
    public String getCompletePath() {
        return completePath;
    }

    /**
     * Returns the current directory name that was created.
     *
     * @return the current directory name that was created
     * @see DirectoryCreator#getCompletePath()
     */
    public String getDirectoryName() {
        return DIRECTORY_NAME;
    }
    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running DirectoryCreator...");
//        String path = "";//Paths.DEFAULT_PATH;
//        String directoryName = "";
//        boolean addTimestamp = false;
//        boolean override = true;
//        boolean appendTimestamp = true;
//        boolean printToConsole = true;
//        DirectoryCreator dc = new DirectoryCreator(
//                path, directoryName, printToConsole
//        );
//        dc.createDirectory();
//        BufferedWriter f1 = dc.getNewCreatedFile(
//                "", ".txt", appendTimestamp, printToConsole
//        );
//        BufferedWriter f2 = dc.getNewCreatedFile(
//                "", ".txt", appendTimestamp, printToConsole
//        );
//        try {
//            //First file
//            f1.write("Some text in the first file...");
//            f1.newLine();
//            f1.flush();
//            f1.close();
//            //Second file
//            f2.write("Some text in the second file...");
//            f2.newLine();
//            f2.write("More text :)!");
//            f2.flush();
//            f2.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("DirectoryCreator completed.");
//    }
}
