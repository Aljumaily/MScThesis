package hlcd.operations;

import java.util.Arrays;

/**
 * Creates the weight enumerator of the codewords in the code in the form of:
 * \(\operatorname{w}(x)_{\mathsf{C}} = A_{0} + A_{1}x + A_{2}x^2 + \ldots +
 * A_{n- 2}x^{n-2} + A_{n - 1}x^{n-1} + A_nx^n\), where \(A_{i}\) is the number
 * of codewords in \(\mathsf{C}\) whose Hamming weight is \(i\). This form is
 * used for both binary and quaternary codes.
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public void main(String[] args) {
 *     System.out.println("Running WeightEnumerator...");
 *     byte n = 5;
 *     byte k = 2;
 *     byte d = 3;
 *     byte base = 4;
 *     CodeParameters cp = new CodeParameters(n, k, d, base);
 *     CodeValidatorParameters cvp = new CodeValidatorParameters();
 *     CodeExporterParameters cep = new CodeExporterParameters();
 *     Code code = new Code(cp, cvp);
 *     code.startEngine();
 *     //get the combinations array
 *     LongArray combinations = code.getCombinations();
 *     WeightEnumerator we = new WeightEnumerator(n, k, d, base, combinations);
 *     System.out.println(we);
 *     combinations.print(" ", Style.DECIMAL);
 *     System.out.println();
 *     code.printGeneratorMatrix(true, Style.DECIMAL, true);
 *     System.out.println("WeightEnumerator completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0
 * @since February 11th, 2022
 */
public class WeightEnumerator {

    private byte n;
    private byte k;
    private byte d;
    private byte base;
    private LongArray combinations;

    /**
     * Constructs a weight enumerator based on the \(n\), \(k\) and \(d\)
     * values as well as the linear combinations of codewords specified.
     *
     * @param n            the length of code
     * @param k            the dimension of the code
     * @param d            the minimum distance of the code
     * @param base         the base of the code which should be \(2\) or \(4\)
     * @param combinations the codewords in the code \(\mathsf{C}\)
     */
    public WeightEnumerator(
            byte n,
            byte k,
            byte d,
            byte base,
            LongArray combinations
    ) {
        this.n = n;
        this.k = k;
        this.d = d;
        this.base = base;
        this.combinations = combinations;
    }

    /**
     * Returns the weight enumerator result. The array at index \(i\) contains
     * the number of codewords whose weight is \(i\).
     *
     * @return the weight enumerator result
     */
    public long[] getWeightEnumerator() {
        long[] result = new long[n + 1];
        HammingWeight hammingWeight = new HammingWeight(base);
        for (long i = 0; i < combinations.length(); i++) {
            int index = hammingWeight.getWeight(combinations.get(i));
            result[index]++;
        }
        return result;
        //System.out.println("Enumerator = " + Arrays.toString(enumerator));
    }

    public String toString() {
        String parameter = "(" + n + ", " + k + ", " + d + ") ";
        String label = "Weight Enumerator of " + parameter + "is:\n";
        return label + Arrays.toString(getWeightEnumerator());
    }

    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running WeightEnumerator...");
//        byte n = 5;
//        byte k = 2;
//        byte d = 3;
//        byte base = 4;
//        CodeParameters cp = new CodeParameters(n, k, d, base);
//        CodeValidatorParameters cvp = new CodeValidatorParameters();
//        CodeExporterParameters cep = new CodeExporterParameters();
//        Code code = new Code(cp, cvp);
//        code.startEngine();
//        //get the combinations array
//        LongArray combinations = code.getCombinations();
//        WeightEnumerator we = new WeightEnumerator(n, k, d, base, combinations);
//        System.out.println(we);
//        combinations.print(" ", Style.DECIMAL);
//        System.out.println();
//        code.printGeneratorMatrix(true, Style.DECIMAL, true);
//        System.out.println("WeightEnumerator completed.");
//    }
}
