package hlcd.linearCode;

import hlcd.operations.*;
import hlcd.parameters.CodeValidatorParameters;

import java.util.LinkedList;

/**
 * The code validator that will ensure the code that was obtained is valid by
 * checking that the generator matrix, orthogonal linear combinations,
 * minimum distance etc. all satisfy the requirements. This is just to make
 * sure the program implementation doesn't contain bugs.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 23rd, 2022)
 * @see CodeValidatorParameters
 * @since 1.8
 */
public class CodeValidator {
    private Code code;
    private CodeValidatorParameters cvp;

    /**
     * The only constructor that accepts the code found as well as the
     * validator parameters.
     *
     * @param code the code found
     * @param cvp  the validator parameters
     */
    public CodeValidator(Code code, CodeValidatorParameters cvp) {
        this.code = code;
        this.cvp = cvp;
    }

    /**
     * Checks if the code satisfies the requirements by checking the
     * appropriate options passed. Instead of using the validator parameters
     * passed in the constructor, they can be passed through this method.
     *
     * @param checkDeterminant                 should the determinant be
     *                                         checked. This is only for
     *                                         checking if the code is
     *                                         quaternary Hermitian LCD
     * @param checkZeroVectors                 check if there exists a zero
     *                                         vector in the linear combinations
     *                                         other than the default
     *                                         all-zero vector
     * @param checkReplicateLinearCombinations should the linear combinations
     *                                         be replicated by using the
     *                                         codewords found in the
     *                                         generator matrix \(G\). The
     *                                         replicated codewords must
     *                                         match what the code contains
     *                                         in terms of linear combinations
     * @param checkValidMinimumDistance        Should the minimum distance
     *                                         for each codeword in the code
     *                                         checked
     * @param checkUniqueness                  Should a check performed for
     *                                         ensuring each codeword in the
     *                                         code is unique and no
     *                                         duplicates exist
     * @param checkHLCDProperty                Should the validator loop
     *                                         through all the possible
     *                                         vectors in the space and
     *                                         ensure the Hermitian LCD
     *                                         property is satisfied. This
     *                                         will take immense amount of
     *                                         time, it should be kept as
     *                                         {@code false}
     * @param printInvalidEntries              should print invalid failed
     *                                         tests on console
     * @param stopWhenFalseEncountered         should the validator stop as
     *                                         soon as a test fails
     * @return {@code true} if the code is valid, {@code false} otherwise
     * @see CodeValidatorParameters
     */
    public boolean isValidCode(
            boolean checkDeterminant,
            boolean checkZeroVectors,
            boolean checkReplicateLinearCombinations,
            boolean checkValidMinimumDistance,
            boolean checkUniqueness,
            boolean checkHLCDProperty,
            boolean printInvalidEntries,
            boolean stopWhenFalseEncountered
    ) {
        return isValidCodeEngine(
                checkDeterminant,
                checkZeroVectors,
                checkReplicateLinearCombinations,
                checkValidMinimumDistance,
                checkUniqueness,
                checkHLCDProperty,
                printInvalidEntries,
                stopWhenFalseEncountered
        );
    }

    /**
     * The engine that checks if the code satisfies the requirements by
     * executing the appropriate options passed.
     *
     * @param checkDeterminant                 should the determinant be
     *                                         checked. This is only for
     *                                         checking if the code is
     *                                         quaternary Hermitian LCD.
     * @param checkZeroVectors                 check if there exists a zero
     *                                         vector in the linear combinations
     *                                         other than the default
     *                                         all-zero vector.
     * @param checkReplicateLinearCombinations should the linear combinations
     *                                         be replicated by using the
     *                                         codewords found in the
     *                                         generator matrix \(G\). This
     *                                         replicated codewords must
     *                                         match what the code contains
     *                                         in terms of linear combinations.
     * @param checkValidMinimumDistance        Should the minimum distance
     *                                         for each codeword in the code
     *                                         checked
     * @param checkUniqueness                  Should a check performed for
     *                                         ensuring each codeword in the
     *                                         code is unique and no
     *                                         duplicates exists
     * @param checkHLCDProperty                Should the validator loop
     *                                         through all the possible
     *                                         vectors in the space and
     *                                         ensure the Hermitian LCD
     *                                         property is satisfied. This
     *                                         will take immense amount of
     *                                         time, it should be kept as
     *                                         {@code false}.
     * @param printInvalidEntries              should print invalid failed
     *                                         tests on console
     * @param stopWhenFalseEncountered         should the validator stop as
     *                                         soon as a test fail
     * @return {@code true} if the code is valid, {@code false} otherwise
     * @see CodeValidatorParameters
     */
    private boolean isValidCodeEngine(
            boolean checkDeterminant,
            boolean checkZeroVectors,
            boolean checkReplicateLinearCombinations,
            boolean checkValidMinimumDistance,
            boolean checkUniqueness,
            boolean checkHLCDProperty,
            boolean printInvalidEntries,
            boolean stopWhenFalseEncountered
    ) {
        boolean isValidResult = true;

        if (checkDeterminant) {
            byte det = code.getGeneratorMatrix().getDeterminant();
            if (det == 0) {
                isValidResult = false;
            }
        }
        if (!isValidResult && stopWhenFalseEncountered) {
            System.out.println("Determinant is INVALID.");
            return false;
        }

        long total = 0;//temp variable used throughout the validation process
        if (checkZeroVectors) {
            total = getTotalZeroVectors(
                    code.getCombinations(), printInvalidEntries
            );
            if (total != 0) {
                isValidResult = false;
            }
        }

        if (!isValidResult && stopWhenFalseEncountered) {
            System.out.println("There exists " + total + " zero vectors (the " +
                                       "all-zero factor is not included).");
            return false;
        }

        if (checkReplicateLinearCombinations) {
            boolean isMatchedCombinations = isReplicatedLinearCombinationsSame(
                    code.getCombinations(), printInvalidEntries
            );
            if (!isMatchedCombinations) {
                isValidResult = false;
            }
        }

        if (!isValidResult && stopWhenFalseEncountered) {
            System.out.println("Couldn't replicate linear combinations.");
            return false;
        }

        if (checkValidMinimumDistance) {
            total = getTotalInvalidMinimumDistanceVectors(
                    code.getCombinations(), printInvalidEntries
            );
            if (total != 0) {
                isValidResult = false;
            }
        }

        if (!isValidResult && stopWhenFalseEncountered) {
            System.out.println("Total codewords with invalid minimum weight: " +
                                       total + ".");
            return false;
        }

        if (checkUniqueness) {
            boolean isUnique = areAllCombinationsUnique(
                    code.getCombinations(), printInvalidEntries
            );
            if (!isUnique) {
                isValidResult = false;
            }
        }
        if (!isValidResult && stopWhenFalseEncountered) {
            System.out.println("The linear combinations are NOT unique.");
            return false;
        }

        if (checkHLCDProperty) {
            boolean isHLCDSatisfied = isHLCDPropertySatisfied(
                    code.getCombinations(), printInvalidEntries
            );
            if (!isHLCDSatisfied) {
                isValidResult = false;
            }
        }
        if (!isValidResult && stopWhenFalseEncountered) {
            System.out.println("The Hermitian LCD property NOT satisfied.");
            return false;
        }
        return isValidResult;
    }

    /**
     * Starts the validator and uses the properties passed in the constructor.
     *
     * @return {@code true} if the code is valid, {@code false} otherwise
     * @see CodeValidatorParameters
     */
    public boolean isValidCode() {
        return isValidCodeEngine(
                cvp.isCheckDeterminant(),
                cvp.isCheckZeroVectors(),
                cvp.isReplicateCombinationsValid(),
                cvp.isCheckValidMinimumDistance(),
                cvp.isCheckUniqueness(),
                cvp.isCheckHLCDProperty(),
                cvp.isPrintInvalidEntries(),
                cvp.isStopWhenFalseEncountered()
        );
    }

    /**
     * The engine that tallies the number of zero vectors in the code. Note
     * that the all-zero vector is <em>not</em> a part of this. In other
     * words, if the code is valid, then this should return {@code 0}.
     *
     * @param combinationsArray   the linear combinations (or codewords) of the
     *                            code.
     * @param printInvalidEntries should the indices of the invalid codewords
     *                            (<i>i.e.</i>, codewords with value {@code 0}
     *                            be printed
     * @return the number of codewords that are {@code 0} and this doesn't
     * include the all-zero vector that is found be default
     */
    private long getTotalZeroVectors(
            LongArray combinationsArray, boolean printInvalidEntries
    ) {
        long counter = 0;
        //start at 1 because the all-zero vector is at index 0 and is not
        //counted here.
        for (long i = 1; i < combinationsArray.length(); i++) {
            if (combinationsArray.get(i) == 0) {
                if (printInvalidEntries) {
                    System.out.println("Zero vector at index: " + i);
                }
                counter++;
            }
        }
        return counter;
    }

    /**
     * The engine for replicating the codewords of the code from the
     * generator matrix \(G\) and ensuring the ones replicated matches the
     * codewords stored in the code.
     *
     * @param combinationsArray   the linear combinations (or codewords) of the
     *                            code.
     * @param printInvalidEntries should the indices of the invalid codewords
     *                            (<i>i.e.</i>, codewords that don't match
     *                            when replicated be printed
     * @return {@code true} if the replication matches what the code holds,
     * {@code false} otherwise
     */
    private boolean isReplicatedLinearCombinationsSame(
            LongArray combinationsArray,
            boolean printInvalidEntries

    ) {
        boolean areReplicatedCombinationsSame = true;
        //LongArray a = new LongArray(code.getN(), code.getK(), code.getBase());
        GF4Operations gf4Operations = new GF4Operations();
        for (byte i = 0; i < code.getK(); i++) {
            long limit = Functions.power(code.getBase(), i);
            long row = combinationsArray.get(limit);
            if (code.getGeneratorMatrix().getRow(i) != row) {
                if (printInvalidEntries) {
                    System.out.println("code.getMatrix().getRow(i) != matrixRow");
                }
            }
            for (long j = 0; j < limit; j++) {
                long v = combinationsArray.get(j);
                long multiple2 = gf4Operations.multiplyByScalarTwo(row);
                long multiple3 = gf4Operations.multiplyByScalarThree(row);
                long result1 = gf4Operations.add(row, v);
                long result2 = gf4Operations.add(multiple2, v);
                long result3 = gf4Operations.add(multiple3, v);
                if (combinationsArray.get(limit + j) != result1 ||
                            combinationsArray.get(limit * 2 + j) != result2 ||
                            combinationsArray.get(limit * 3 + j) != result3
                ) {
                    if (printInvalidEntries) {
                        areReplicatedCombinationsSame = false;
                        System.out.println(
                                "Invalid linear combination at matrix row and" +
                                        " linear combination: " + i + ", " + j
                        );
                    }
                }
            }
        }
        return areReplicatedCombinationsSame;
    }

    /**
     * The engine that goes through the linear combination array and ensures
     * that the minimum distance for each vector in the combinations satisfies
     * the minimum distance requirement. Note that the all-zero vector is
     * assumed to satisfy the minimum distance constraint.
     *
     * @param combinationsArray   the linear combinations (or codewords) of the
     *                            code
     * @param printInvalidEntries should the indices of the invalid codewords
     *                            (<i>i.e.</i>, codewords that don't satisfy
     *                            the minimum distance requirement be printed
     * @return the number of codewords that don't satisfy the minimum distance
     * \(d\) of the code
     */
    public long getTotalInvalidMinimumDistanceVectors(
            LongArray combinationsArray,
            boolean printInvalidEntries
    ) {
        //loop through all the full codewords
        //start at 1 because the all-zero vector is at index 0
        long counter = 0;
        HammingWeight hWeight = new HammingWeight(code.getBase());
        for (long i = 1; i < combinationsArray.length(); i++) {
            /* print the vector if needed
            System.out.println(
                    MatrixPrinter.vectorAsString(
                            combinationsArray.get(i),
                            code.getN(), code.getBase(), " ", Style.DECIMAL)
            );
             */
            if (hWeight.getWeight(combinationsArray.get(i)) < code.getD()) {
                if (printInvalidEntries) {
                    System.out.println(
                            "Invalid minimum distance at index: " + i
                    );
                }
                counter++;
            }
        }
        return counter;
    }

    /**
     * The engine that ensures all the codewords in the code <em>are</em>
     * unique.
     *
     * @param combinationsArray   the linear combinations (or codewords) of the
     *                            code
     * @param printInvalidEntries should the indices of the invalid codewords
     *                            (<i>i.e.</i>, codewords that are <em>not</em>
     *                            unique be printed)
     * @return {@code true} if all the codewords in code are unique, {@code
     * false} otherwise
     */
    public boolean areAllCombinationsUnique(
            LongArray combinationsArray,
            boolean printInvalidEntries
    ) {
        boolean isUnique = true;
        for (long i = 0; i < combinationsArray.length() - 1; i++) {
            for (long j = i + 1; j < combinationsArray.length(); j++) {
                if (combinationsArray.get(i) == combinationsArray.get(j)) {
                    isUnique = false;
                    if (printInvalidEntries) {
                        System.out.println(
                                "Not unique at index: " + i + " and " + j
                        );
                    }
                }
            }
        }
        return isUnique;
    }

    /**
     * The engine that ensures all the codewords satisfies the Hermitian LCD
     * property. This is used only for quaternary codes. It will examine all
     * the vectors in the space, see which are orthogonal to all the
     * codewords in the code and ensures these vectors are <em>not</em> a
     * part of the codewords in \(\mathsf{C}\).
     *
     * @param combinationsArray   the linear combinations (or codewords) of the
     *                            code
     * @param printInvalidEntries should the indices of the invalid codewords
     *                            (<i>i.e.</i>, codewords that <em>don't</em>
     *                            satisfy the Hermitian LCD property
     * @return {@code true} if all the code is Hermitian LCD, {@code false}
     * otherwise
     */
    public boolean isHLCDPropertySatisfied(
            LongArray combinationsArray,
            boolean printInvalidEntries
    ) {
        /*
         * linkedList stores all the vectors in \(BASE^N\) that are orthogonal
         * to all the codewords and linear combinations in the
         * combinationsArray.
         */
        LinkedList<Long> arbitraryOrthogonalVectors = new LinkedList<>();

        //loop through all the vectors in the space
        long limit = Functions.power(code.getBase(), code.getN());
        for (long v = 0; v < limit; v++) {
            boolean isValid = true;

            //loop through all the codewords found
            for (long c = 0; c < combinationsArray.length(); c++) {
                long codeword = combinationsArray.get(c);

                //convert the codeword to a (n * 1) matrix/vector
                Matrix codewordMatrix = new Matrix(
                        new long[]{combinationsArray.get(c)},
                        code.getN(), (byte) 1, code.getBase()
                );
                GF4Operations gf4Operations = new GF4Operations();
                byte result = gf4Operations.hermitianInnerProduct(codeword, v);
                //System.out.println(result);
                if (result != 0) {
                    isValid = false;
                    break;
                }
            }
            //if isValid is true, it means that the current v is orthogonal to
            //all vectors in the combinations.
            if (isValid) {
                arbitraryOrthogonalVectors.add(v);
            }
        }
        boolean isHLCDPropertySatisfied = true;
        //Ensure that the vectors in arbitraryOrthogonalVectors linked list are
        //not found in the combinations array. In the case where they are
        //found, then the implementation of the thesis is wrong.
        for (long j = 0; j < combinationsArray.length(); j++) {
            for (long value : arbitraryOrthogonalVectors) {
                if (combinationsArray.get(j) == value &&
                            combinationsArray.get(j) != 0 && value != 0) {
                    isHLCDPropertySatisfied = false;
                    if (printInvalidEntries) {
                        System.out.println("HLCD is satisfied: " +
                                                   combinationsArray.get(j) +
                                                   " and " + value
                        );
                    }
                }
            }
        }
        return isHLCDPropertySatisfied;
    }
}
