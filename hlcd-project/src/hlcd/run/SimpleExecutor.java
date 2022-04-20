package hlcd.run;

/**
 * A simple execution class that will search for the \(n\), \(k\), \(d\) and
 * {@code base} values assigned/hardcoded in the constructor. It is assumed
 * that it is a quaternary code and the following options will be used: it is
 * Hermitian LCD, the identity matrix will be appended, the restrictions of
 * codeword generation is also applied and it is <em>not</em> multithreaded.
 * It will create an instance of {@code TestParameter} by using the constructor
 * that accepts four {@code byte}s. There, the default constructor of
 * {@code CodeValidatorParameters} will be used. Furthermore, It will
 * initialize the {@code CodeParameters} object using the \(n\), \(k\), \(d\)
 * and {@code base} values found here. Also, it initiates a code object based
 * on the parameters specified. Lastly, it will use
 * {@code CodeExporterParameters} and {@code CodeDisplayParameters} by
 * calling the constructor that accepts a {@code Code} object (for both
 * classes).
 *
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running SimpleExecutor...");
 *     SimpleExecutor sr = new SimpleExecutor();
 *     System.out.println("SimpleExecutor completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 12th, 2022)
 * @see ComplexExecutor
 * @since 1.8
 */
public class SimpleExecutor {
    /**
     * The only constructor that declares the \(n\), \(k\), \(d\) and
     * {@code base} values to execute.
     */
    public SimpleExecutor() {
        byte n = 8;
        byte k = 4;
        byte d = 4;
        byte base = 4;
        TestParameter tp = new TestParameter(n, k, d, base);
    }

    /**
     * Executes the program.
     *
     * @param args the arguments specified but will be ignored
     */
    public static void main(String[] args) {
        System.out.println("Running SimpleExecutor...");
        SimpleExecutor sr = new SimpleExecutor();
        System.out.println("SimpleExecutor completed.");
    }
}