package hlcd.linearCode;

import hlcd.enums.Style;
import hlcd.parameters.*;
import hlcd.operations.*;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The definition of a mathematical linear code. It will contain the
 * generator matrix of the code as well as the linear combinations. It is a
 * <b>must</b> to call {@code startEngine()} to start the execution of the
 * search.
 * <p>
 * TODO: ensure the multithreading used here is correct (the program doesn't
 *  support multithreading yet but it has been implemented).
 *
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running the \"Code\" class...");
 *     byte n = 8;
 *     byte k = 4;
 *     byte d = 4;
 *     byte base = 4;
 *
 *     CodeParameters cp = new CodeParameters(n, k, d, base);
 *     CodeValidatorParameters cvp = new CodeValidatorParameters();
 *     CodeExporterParameters cep = new CodeExporterParameters();
 *     Code code = new Code(cp, cvp);
 *     code.startEngine();
 *     code.printGeneratorMatrix(true, Style.DECIMAL, true);
 *     System.out.println("The run of the \"Code\" class completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 10th, 2022)
 * @since 1.8
 */
public class Code implements CodeOperations, Serializable {
    /**
     * The length of each codeword in the code.
     */
    private final byte N;

    /**
     * The dimension of the code (and the number of rows in the generator
     * matrix).
     */
    private final byte K;

    /**
     * The minimum distance between any two codewords in code.
     */
    private final byte D;

    /**
     * The minimum weight of far-right n - k values.
     */
    private final byte RHS_WEIGHT;

    /**
     * The base of the code which can be either 2 or 4.
     */
    private final byte BASE;

    /**
     * The generator matrix that contains G, G transpose, G Hermitian transpose.
     */
    private Matrix matrix;

    /**
     * The linear combinations of the codewords in the code (all codewords in
     * the code) . It is transient because the linear combinations shouldn't
     * be written to disk as they can be generated based on the generator
     * matrix. This will save gigabytes of disk.
     */
    transient private LongArray combinations;

    /**
     * The code parameters which includes n, k, d, base and other properties
     * such as having the generator matrix in standard form, if the code is
     * Hermitian LCD, etc.
     */
    private CodeParameters cp;

    /**
     * The parameters for the validator after the code completes to ensure
     * the code is valid (and the program is bug-free).
     */
    private CodeValidatorParameters cvp;

    /**
     * The number of recursive calls needed to arrive to a solution.
     */
    private long recursiveCallsCount;

    /**
     * The code statistics that benchmarks the run of a code. It contains the
     * duration of the execution, the code parameters and the validator
     * parameters used.
     */
    private CodeStatistics codeStatistics;

    /**
     * The only constructor which accepts the code parameters and the code
     * validator parameters. Both are wrapper classes for primitive type data
     * such as \(n\), \(k\), \(d\), if it is quaternary Hermitian LCD or just
     * quaternary. It also includes what kind of validator tests should be
     * executed.
     *
     * @param cp  the code parameters to execute
     * @param cvp the code validator parameters for ensuring the code
     *            generated doesn't have bugs
     */
    public Code(CodeParameters cp, CodeValidatorParameters cvp) {
        this.cp = cp;
        this.cvp = cvp;
        N = cp.getN();
        K = cp.getK();
        D = cp.getD();
        if (D <= 2) {
            throw new UnsupportedOperationException("Code will not work " +
                                                            "properly " +
                                                            "because D is " +
                                                            "less than 3.");
        }
        RHS_WEIGHT = cp.getRHSWeight();
        BASE = cp.getBase();
        matrix = new Matrix(N, K, BASE);
        combinations = new LongArray(N, K, BASE);
    }

    /**
     * Starts the search of the entire program.
     */
    public void startEngine() {
        long executionStartTime = System.currentTimeMillis();

        VectorGenerator vg = new VectorGenerator(
                N, K, D, RHS_WEIGHT, BASE, cp.isIdentityAppended(),
                cp.isCodewordRestrictionApplied()
        );
        byte row = 0;
        if (cp.isCodewordRestrictionApplied()) {//hardcode the top row with 1's
            long vector = vg.getNextFullVector(row);
            if (!vg.isCurrentSubvectorValid()) {
                return;
            }
            GF4Operations gf4Operations = new GF4Operations();
            matrix.setRow(row, vector);
            combinations.set(1, vector);
            combinations.set(2, gf4Operations.multiplyByScalarTwo(vector));
            combinations.set(3, gf4Operations.multiplyByScalarThree(vector));
            row++;
        }
        //new vector generator is created to ensure the subcodeword of the
        //top row will be tested as well. For example, if the top row is:
        //1 0 0 0 1 1 1, then the next vector that will be examined is:
        //0 1 0 0 1 1 1, not 0 1 0 0 1 1 2.
        VectorGenerator vgNew = new VectorGenerator(
                N, K, D, RHS_WEIGHT, BASE,
                vg.getCurrentSubvector(), vg.getResetPoint(),
                cp.isIdentityAppended(),
                cp.isCodewordRestrictionApplied()
        );
        if (cp.isMultithreaded()) {
            backtrackMultiThreaded(row, vgNew);
        } else {
            backtrack(row, vgNew);
        }

        long end = System.currentTimeMillis();
        codeStatistics = new CodeStatistics(
                cp, cvp, recursiveCallsCount, executionStartTime, end
        );
    }

    /**
     * Populates the linear combinations of the codewords based on the
     * codewords in the generator matrix.
     */
    private void populateCombinations() {
        //populate the codewords from the generator matrix strategically
        // into the appropriate indices in the combination array
        for (byte i = 0; i < K; i++) {
            long index = Functions.power(BASE, i);
            combinations.set(index, matrix.getRow(i));
        }

        //start populating the combinations array
        GF4Operations gf4Operations = new GF4Operations();
        long limit = 1;
        for (byte i = 0; i < K; i++) {
            long offset = Functions.power(BASE, i);
            long vector = combinations.get(limit);
            for (long j = 0; j < limit; j++) {
                long vector2 = gf4Operations.multiplyByScalarTwo(vector);
                long vector3 = gf4Operations.multiplyByScalarThree(vector);

                long result1 = gf4Operations.add(vector, combinations.get(j));
                long result2 = gf4Operations.add(vector2, combinations.get(j));
                long result3 = gf4Operations.add(vector3, combinations.get(j));
                combinations.set(offset + (BASE - 1) * j + 0, result1);
                combinations.set(offset + (BASE - 1) * j + 1, result2);
                combinations.set(offset + (BASE - 1) * j + 2, result3);
            }
            limit = limit * 4;
        }
    }

    /**
     * An important method in the program which will recursively try to
     * populate the generator matrix with valid codewords. It will return
     * {@code true} if a generator matrix is possible to obtain based on the
     * \(n\), \(k\) and \(d\) values, {@code false} otherwise.
     *
     * @param r  the current row in the generator matrix to populate
     * @param vg the vector generator object associated with the current row
     *           being populated
     * @return {@code true} if a generator matrix is possible to obtain based
     * on the \(n\), \(k\) and \(d\) values, {@code false} otherwise
     * @see Code#backtrackMultiThreaded(byte, VectorGenerator)
     * @see Code#isVectorLinearlyOrthogonal(long, long)
     */
    private boolean backtrack(byte r, VectorGenerator vg) {
        recursiveCallsCount++;
        long limit = Functions.power(BASE, r);

        //base case
        if (r >= K) {
            if (matrix.getGPrime().isInvertible()) {
                return true;
            } else {
                matrix.setRow((byte) (r - 1), 0);
                return false;
            }
        }

        while (true) {
            //generate a potential codeword that satisfy the weight requirement
            // (which is having a weight of at least d).
            long vector = vg.getNextFullVector(r);
            if (!vg.isCurrentSubvectorValid()) {
                return false;
            }
            if (isVectorLinearlyOrthogonal(limit, vector)) {
                //current potential codeword is a codeword, place it in the
                // generator matrix and perform a recursive call
                matrix.setRow(r, vector);
                VectorGenerator vgNew = new VectorGenerator(
                        N, K, D, RHS_WEIGHT, BASE,
                        vg.getCurrentSubvector(), vg.getResetPoint(),
                        cp.isIdentityAppended(),
                        cp.isCodewordRestrictionApplied()
                );
                if (backtrack((byte) (r + 1), vgNew)) {
                    return true;
                }
            }
        }
    }

    /**
     * Performs the backtracking using multithreading. It has been implemented
     * but not tested properly. The code will be the exact as {@code
     * backtrack(byte, VectorGenerator)} method but instead of calling {@code
     * isVectorLinearlyOrthogonal(long, long)}, call {@code
     * isVectorLinearlyOrthogonalMulti(long, long)} instead.
     *
     * @param r  the current row in the generator matrix to populate
     * @param vg the vector generator object associated with the current row
     *           being populated
     * @return {@code true} if a generator matrix is possible to obtain based
     * on the \(n\), \(k\) and \(d\) values, {@code false} otherwise
     * @see Code#backtrack(byte, VectorGenerator)
     * @see Code#isVectorLinearlyOrthogonalMulti(long, long)
     */
    private boolean backtrackMultiThreaded(byte r, VectorGenerator vg) {
        if (r < 65) {//always true, to throw the unsupported operation exception
            throw new UnsupportedOperationException(
                    "Multithreading not supported yet..."
            );
        }
        recursiveCallsCount++;
        long limit = Functions.power(BASE, r);

        //base case
        if (r >= K) {
            return matrix.getGPrime().isInvertible();
        }

        while (true) {
            //generate a potential codeword that satisfy the weight requirement
            // (which is having a weight of at least d).
            long vector = vg.getNextFullVector(r);
            if (!vg.isCurrentSubvectorValid()) {
                return false;
            }

            if (isVectorLinearlyOrthogonalMulti(limit, vector)) {
                //current potential codeword is a codeword, place it in the
                // generator matrix and perform a recursive call
                matrix.setRow(r, vector);
                VectorGenerator vgNew = new VectorGenerator(
                        N, K, D, RHS_WEIGHT, BASE,
                        vg.getCurrentSubvector(), vg.getResetPoint(),
                        cp.isIdentityAppended(),
                        cp.isCodewordRestrictionApplied()
                );
                if (backtrack((byte) (r + 1), vgNew)) {
                    return true;
                }
            }
        }
    }

    /**
     * Returns the weight enumerator of the code.
     *
     * @return the weight enumerator of the code
     */
    public WeightEnumerator getCodeWeightEnumerator() {
        return new WeightEnumerator(N, K, D, BASE, combinations);
    }

    /**
     * Finds whether the vector specified is linearly orthogonal to all the
     * vectors in the combination array that have indices less than the limit
     * specified. It uses a single thread.
     *
     * @param limit  the indices to check from 0 to (but <em> not</em>
     *               including) the limit specified
     * @param vector the vector to check whether it is linearly orthogonal to
     *               all the vectors in the combination array
     * @return {@code true} if the vector specified is linearly orthogonal to
     * all vectors in the combination array, {@code false} otherwise
     * @see Code#isVectorLinearlyOrthogonalMulti(long, long)
     */
    private boolean isVectorLinearlyOrthogonal(long limit, long vector) {
        boolean isValid = true;
        GF4Operations gf4Operations = new GF4Operations();
        HammingWeight hammingWeight = new HammingWeight(BASE);
        long multiple2 = gf4Operations.multiplyByScalarTwo(vector);
        long multiple3 = gf4Operations.multiplyByScalarThree(vector);
        for (long i = 0; i < limit && isValid; i++) {
            long v = gf4Operations.add(vector, combinations.get(i));
            if (hammingWeight.getWeight(v) < D) {
                isValid = false;
                break;
            }
            combinations.set(limit + i, v);
        }
        for (long i = 0; i < limit && isValid; i++) {
            long v = gf4Operations.add(multiple2, combinations.get(i));
            if (hammingWeight.getWeight(v) < D) {
                isValid = false;
                break;
            }
            combinations.set((limit * 2) + i, v);
        }

        for (long i = 0; i < limit && isValid; i++) {
            long v = gf4Operations.add(multiple3, combinations.get(i));
            if (hammingWeight.getWeight(v) < D) {
                isValid = false;
                break;
            }
            combinations.set((limit * 3) + i, v);
        }
        return isValid;
    }

    /**
     * Finds whether the v specified is linearly orthogonal to all the
     * vectors in the combination array that have indices less than the limit
     * specified. It uses a multithreading but has not been test properly.
     *
     * @param limit the indices to check from 0 to (but <em> not</em>
     *              including) the limit specified
     * @param v     the vector to check whether it is linearly orthogonal to
     *              all the vectors in the combination array
     * @return {@code true} if the v specified is linearly orthogonal to
     * all vectors in the combination array, {@code false} otherwise
     * @see Code#isVectorLinearlyOrthogonal(long, long)
     */
    private boolean isVectorLinearlyOrthogonalMulti(long limit, long v) {
        GF4Operations gf4Operations = new GF4Operations();
        HammingWeight hammingWeight = new HammingWeight(BASE);
        final AtomicBoolean running = new AtomicBoolean(true);
        Thread t1 = new Thread() {
            public void run() {
                for (long i = 0; i < limit && running.get(); i++) {//check
                    // linear orthogonality
                    long result = gf4Operations.add(v, combinations.get(i));
                    if (hammingWeight.getWeight(v) < D
                                || combinations.get(i) == v
                    ) {//invalid
                        running.set(false);
                        interrupt();
                    }
                    combinations.set(limit + i, result);
                }
            }
        };
        t1.start();

        Thread t2 = new Thread() {
            public void run() {
                for (long i = 0; i < limit && running.get(); i++) {//check
                    long result = gf4Operations.multiplyByScalarTwo(v);
                    result = gf4Operations.add(result, combinations.get(i));
                    if (hammingWeight.getWeight(result) < D
                                || combinations.get(i) == result
                    ) {//invalid
                        running.set(false);
                        interrupt();
                    }
                    combinations.set((limit * 2L) + i, result);
                }
            }
        };
        t2.start();

        Thread t3 = new Thread() {
            public void run() {
                for (long i = 0; i < limit && running.get(); i++) {//check
                    long result = gf4Operations.multiplyByScalarThree(v);
                    result = gf4Operations.add(result, combinations.get(i));
                    if (hammingWeight.getWeight(result) < D
                                || combinations.get(i) == result
                    ) {//invalid
                        running.set(false);
                        interrupt();
                    }
                    combinations.set((limit * 3L) + i, result);
                }
            }
        };
        t3.start();
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return running.get();
    }

    @Override
    public void printGeneratorMatrix() {
        matrix.printMatrix();
    }

    @Override
    public void printGeneratorMatrix(
            boolean addBrackets,
            Style style,
            boolean showSize
    ) {
        matrix.printMatrix(addBrackets, style, showSize);
    }

    @Override
    public void printGeneratorMatrix(
            String delimiter,
            boolean addBrackets,
            Style style,
            boolean showSize
    ) {
        matrix.printMatrix(delimiter, addBrackets, style, showSize);
    }

    @Override
    public void printCombinations(String delimiter, Style style) {
        combinations.print(delimiter, style);
    }

    @Override
    public void printCodeParameters() {
        System.out.println("(" + N + ", " + K + ", " + D + ")_" + BASE);
    }

    /**
     * Returns the code parameters of the code.
     *
     * @return the code parameters of the code
     */
    public CodeParameters getCodeParameters() {
        return cp;
    }

    /**
     * Returns the number of recursive calls required to arrive to the solution.
     *
     * @return the number of recursive calls required to arrive to the solution
     */
    public long getNumberOfRecursiveCalls() {
        return recursiveCallsCount;
    }

    @Override
    public Matrix getGeneratorMatrix() {
        return matrix;
    }

    @Override
    public Matrix getGeneratorMatrixCopy() {
        return matrix.clone();
    }

    @Override
    public LongArray getCombinations() {
        return combinations;
    }

    @Override
    public byte getN() {
        return N;
    }

    @Override
    public byte getK() {
        return K;
    }

    @Override
    public byte getD() {
        return D;
    }

    @Override
    public byte getBase() {
        return BASE;
    }

    @Override
    public byte getRHSWeight() {
        return RHS_WEIGHT;
    }

    @Override
    public boolean isHLCD() {
        return cp.isHLCD();
    }

    @Override
    public boolean isIdentityAppended() {
        return cp.isIdentityAppended();
    }

    @Override
    public boolean isMultithreaded() {
        return cp.isMultithreaded();
    }

    @Override
    public boolean isCodewordRestrictionApplied() {
        return cp.isCodewordRestrictionApplied();
    }

    @Override
    public CodeStatistics getCodeStatistics() {
        return codeStatistics;
    }

    @Override
    public CodeValidatorParameters getCodeValidatorParameters() {
        return cvp;
    }

    /* A sample execution of this class is commented out*/

//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running the \"Code\" class...");
//        byte n = 8;
//        byte k = 4;
//        byte d = 4;
//        byte base = 4;
//
//        CodeParameters cp = new CodeParameters(n, k, d, base);
//        CodeValidatorParameters cvp = new CodeValidatorParameters();
//        CodeExporterParameters cep = new CodeExporterParameters();
//        Code code = new Code(cp, cvp);
//        code.startEngine();
//        code.printGeneratorMatrix(true, Style.DECIMAL, true);
//        System.out.println("The run of the \"Code\" class completed.");
//    }
}