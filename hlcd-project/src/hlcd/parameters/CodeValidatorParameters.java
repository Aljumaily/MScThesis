package hlcd.parameters;

import java.io.Serializable;

/**
 * Stores the options of a code \(\mathsf{C}\) when being validated to ensure
 * the generator matrix \(G\) does satisfy the necessary requirements of the
 * \(\left[n, \, k, \, d\right]_{base}\) code found.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (January 21st, 2022)
 * @since 1.8
 */
public class CodeValidatorParameters implements Serializable {

    /**
     * Checks if the determinant of the matrix is <em>not</em> zero.
     */
    private boolean checkDeterminant;

    /**
     * Checks if the linear combinations contain a single zero vector, the
     * default zero vector, that is found in the 0th index of the combinations.
     * Note that Java will automatically initialize all the cells in the array
     * to 0's.
     */
    private boolean checkZeroVectors;

    /**
     * Checks if the linear combinations found can be replicated by finding
     * the linear combinations of the codewords in the generator matrix.
     */
    private boolean replicateLinearCombinations;

    /**
     * Checks if the minimum distance of the code is satisfied by examining
     * each codeword and ensuring it has the appropriate weight of larger or
     * equal to \(d\).
     */
    private boolean checkValidMinimumDistance;

    /**
     * Checks if all codewords in the code are unique and that there doesn't
     * exist duplicates in the codewords.
     */
    private boolean checkUniqueness;

    /**
     * Checks if the Hermitian LCD property is satisfied. It will examine all
     * the vectors in the space, seeing which are orthogonal to all the
     * codewords in the code, then, ensures these vectors are <em>not</em> a
     * part of the codewords in \(\mathsf{C}\). This should be left as {@code
     * false} because it will take significant amount of time to complete.
     */
    private boolean checkHLCDProperty;

    /**
     *
     */
    private boolean printInvalidEntries;

    /**
     * Should stop the validator once a single test fails (<i>i.e.</i>,
     * recognizing that the code is invalid).
     */
    private boolean stopWhenFalseEncountered;

    /**
     * The default constructor which will set all parameters to {@code true}
     * <em>except</em> checking for Hermitian LCD property and printing
     * invalid entries.
     */
    public CodeValidatorParameters() {
        this.checkDeterminant = true;
        this.checkZeroVectors = true;
        this.replicateLinearCombinations = true;
        this.checkValidMinimumDistance = true;
        this.checkUniqueness = true;
        this.checkHLCDProperty = false;
        this.printInvalidEntries = false;
        this.stopWhenFalseEncountered = true;
    }

    /**
     * Initializes the state of a code validator options.
     *
     * @param checkDeterminant            should the determinant of \(G\) be
     *                                    checked so that it is not zero.
     *                                    This is used for quaternary Hermitian
     *                                    LCD codes.
     * @param checkZeroVectors            Checks if there exists zero vectors
     *                                    (other than the all-zero default
     *                                    vector) in the linear combinations
     *                                    found. There shouldn't be any
     *                                    because all the number of slots of
     *                                    the linear combinations stored is
     *                                    \(2^k\) if the code is binary and
     *                                    \(4^k\) if quaternary. At the
     *                                    beginning, all the slots are zeroed
     *                                    out.
     * @param replicateLinearCombinations Uses the codewords in \(G\) and
     *                                    recalculates all the codewords in
     *                                    the code by generating their linear
     *                                    combinations and ensures they match
     *                                    what the code already store as linear
     *                                    combinations.
     * @param checkValidMinimumDistance   Ensures all the codewords in the
     *                                    combinations satisfy the minimum
     *                                    distance constraint.
     * @param checkUniqueness             Ensures all the linear combinations
     *                                    stored are unique and are no
     *                                    duplicates.
     * @param checkHLCDProperty           Ensures to loop through all the
     *                                    possible vectors in the space
     *                                    \(base^n\), stores all the vectors
     *                                    that are orthogonal (using
     *                                    Hermitian dot product) to all
     *                                    codewords of the code in a set \(S\).
     *                                    Then, it ensures all of these
     *                                    vectors found are not
     *                                    codewords of the code \(\mathsf{C}\).
     *                                    In the case where a vector is found
     *                                    in both \(S\) and \(\mathsf{C}\), then
     *                                    the implementation of this program
     *                                    wrong. <b>Note:</b> <em>this
     *                                    should almost always be</em>
     *                                    {@code false}<em>. It is not
     *                                    necessary to check but if checked
     *                                    and all is clear, then the
     *                                    implementation of this program is
     *                                    flawless. Also, this will take
     *                                    enormously long time to complete.
     *                                    Hence, not recommended at all for
     *                                    values of \(k \gt 8\).</em>
     * @param printInvalidEntries         Should messages be printed on
     *                                    console about the invalid aspects
     *                                    found in the code.
     * @param stopWhenFalseEncountered    Should the validator stop as soon
     *                                    as one of the options conclude the
     *                                    code is invalid.
     */
    public CodeValidatorParameters(
            boolean checkDeterminant,
            boolean checkZeroVectors,
            boolean replicateLinearCombinations,
            boolean checkValidMinimumDistance,
            boolean checkUniqueness,
            boolean checkHLCDProperty,
            boolean printInvalidEntries,
            boolean stopWhenFalseEncountered
    ) {
        this.checkDeterminant = checkDeterminant;
        this.checkZeroVectors = checkZeroVectors;
        this.replicateLinearCombinations = replicateLinearCombinations;
        this.checkValidMinimumDistance = checkValidMinimumDistance;
        this.checkUniqueness = checkUniqueness;
        this.checkHLCDProperty = checkHLCDProperty;
        this.printInvalidEntries = printInvalidEntries;
        this.stopWhenFalseEncountered = stopWhenFalseEncountered;
    }

    /**
     * Should the validator check if the generator matrix \(G\) has a non-zero
     * determinant (only used for quaternary Hermitian LCD codes).
     *
     * @return {@code true} if a check is needed, {@code false} otherwise
     */
    public boolean isCheckDeterminant() {
        return checkDeterminant;
    }

    /**
     * Sets the state of whether the validator should perform a determinant
     * check (only used for quaternary Hermitian LCD codes).
     *
     * @param checkDeterminant the new value
     */
    public void setCheckDeterminant(boolean checkDeterminant) {
        this.checkDeterminant = checkDeterminant;
    }

    /**
     * Should the validator perform a check to ensure the code \(\mathsf{C}\)
     * contains the all-zero vector once and all other codewords are non-zero.
     * This is because when the program is initialized, the default value
     * for all the linear combinations is zero.
     *
     * @return {@code true} if a check is needed, {@code false} otherwise
     */
    public boolean isCheckZeroVectors() {
        return checkZeroVectors;
    }

    /**
     * Sets the state of whether the validator should perform a check to
     * ensure the codewords in \(\mathsf{C}\) contain a single nonzero vector.
     *
     * @param checkZeroVectors the new value
     */
    public void setCheckZeroVectors(boolean checkZeroVectors) {
        this.checkZeroVectors = checkZeroVectors;
    }

    /**
     * Should the validator perform a check to ensure the linear combinations
     * of the codewords in the generator matrix \(G\) yield the same linear
     * combination of codewords stored in the {@code Code} object.
     *
     * @return {@code true} if a check is needed, {@code false} otherwise
     * @see hlcd.linearCode.Code
     */
    public boolean isReplicateCombinationsValid() {
        return checkValidMinimumDistance;
    }

    /**
     * Sets the state of whether the validator should perform a check to
     * ensure the codewords in \(G\) generate all the linear combinations found.
     *
     * @param replicateLinearCombinations the new value
     */
    public void setReplicateLinearCombinations(
            boolean replicateLinearCombinations
    ) {
        this.replicateLinearCombinations = replicateLinearCombinations;
    }

    /**
     * Should the validator perform a check to ensure the codewords in the code
     * satisfy the minimum distance \(d\) constraint.
     *
     * @return {@code true} if a check is needed, {@code false} otherwise
     */
    public boolean isCheckValidMinimumDistance() {
        return checkValidMinimumDistance;
    }

    /**
     * Sets the state of whether the validator should perform a check to
     * ensure all the codewords in the code satisfy the minimum distance \(d\)
     * constraint.
     *
     * @param checkValidMinimumDistance the new value
     */
    public void setCheckValidMinimumDistance(
            boolean checkValidMinimumDistance
    ) {
        this.checkValidMinimumDistance = checkValidMinimumDistance;
    }

    /**
     * Should the validator perform a check to ensure all the codewords are
     * unique such that there are no duplicates in the codewords of \(\mathsf{
     * C}\).
     *
     * @return {@code true} if a check is needed, {@code false} otherwise
     */
    public boolean isCheckUniqueness() {
        return checkUniqueness;
    }

    /**
     * Sets the state of whether the validator should perform a check to
     * ensure all the codewords are unique such that there are no duplicates
     * in the codewords of \(\mathsf{C}\).
     *
     * @param checkUniqueness the new value
     */
    public void setCheckUniqueness(boolean checkUniqueness) {
        this.checkUniqueness = checkUniqueness;
    }

    /**
     * Should the validator perform a check to ensure that the code \(\mathsf{
     * C}\) satisfies the Hermitian LCD property (for quaternary codes). It will
     * loop through all the possible vectors in the space \(base^n\), stores
     * all the vectors that are orthogonal (using Hermitian dot product) to all
     * codewords of the code in a set \(S\). Then, it ensures all of these
     * vectors found are not codewords of the code \(\mathsf{C}\). In the case
     * where a vector is found in both \(S\) and \(\mathsf{C}\), then the
     * implementation of this program wrong. <b>Note:</b> <em>this should
     * almost always be</em>{@code false}<em>. It is not necessary to check but
     * if checked and all is clear, then the implementation of this program is
     * flawless. Also, this will take enormously long time to complete.
     * Hence, not recommended at all for values of \(k \gt 8\).</em>
     *
     * @return {@code true} if a check is needed, {@code false} otherwise
     */
    public boolean isCheckHLCDProperty() {
        return checkHLCDProperty;
    }

    /**
     * Sets the state of whether the validator should perform a check to
     * ensure all the code \(\mathsf{C}\) satisfies the Hermitian LCD property
     * (for quaternary codes). It should be always {@code false} because it
     * takes a significantly long time to complete.
     *
     * @param checkHLCDProperty the new value
     */
    public void setCheckLCDProperty(boolean checkHLCDProperty) {
        this.checkHLCDProperty = checkHLCDProperty;
    }

    /**
     * Should the program print invalid tests when encountered. This is used
     * for debugging purposes. Nonetheless, it should be {@code true} to
     * ensure the correctness of the implementation of the program from bugs.
     *
     * @return {@code true} if printing will occur, {@code false} otherwise
     */
    public boolean isPrintInvalidEntries() {
        return printInvalidEntries;
    }

    /**
     * Sets the state of whether the validator should print out messages to
     * the console of invalid tests encountered.
     *
     * @param printInvalidEntries the new value
     */
    public void setPrintInvalidEntries(boolean printInvalidEntries) {
        this.printInvalidEntries = printInvalidEntries;
    }

    /**
     * Should the validator stop further testing once a test failed. This
     * should be {@code true} to stop the validator from continuing.
     *
     * @return {@code true} if the validator will stop once a single test
     * failed, {@code false} otherwise
     */
    public boolean isStopWhenFalseEncountered() {
        return stopWhenFalseEncountered;
    }

    /**
     * Sets the state of whether the validator should stop testing once a
     * single test has failed.
     *
     * @param stopWhenFalseEncountered the new value
     */
    public void setStopWhenFalseEncountered(boolean stopWhenFalseEncountered) {
        this.stopWhenFalseEncountered = stopWhenFalseEncountered;
    }
}
