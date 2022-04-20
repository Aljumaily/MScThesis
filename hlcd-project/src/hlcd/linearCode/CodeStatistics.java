package hlcd.linearCode;

import hlcd.parameters.CodeParameters;
import hlcd.parameters.CodeValidatorParameters;
import hlcd.operations.Benchmarker;

import java.io.Serializable;

/**
 * The code statistics is responsible for benchmarking the statistics run of a
 * code. It will keep track of the total time taken, the parameters of the
 * code and the parameters of the validator used.
 * <p>
 * TODO: test finding the correct number of recursive calls needed to
 *  arrive to a solution as well as the number of potential vectors generated.
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 2nd, 2022)
 * @see CodeParameters
 * @see CodeValidatorParameters
 * @since 1.8
 */
public class CodeStatistics implements Serializable {

    /**
     * The code parameters which includes \(n\), \(k\), \(d\), base and other
     * properties such as having the generator matrix in standard form, if
     * the code is Hermitian LCD, etc.
     */
    private CodeParameters cp;

    /**
     * The code validator parameters which includes what kind of tests should
     * be executed to ensure the code found is correct and that there aren't
     * bugs present.
     */
    private CodeValidatorParameters cvp;

    /**
     * The number of recursive calls needed to arrive to the solution.
     */
    private long recursiveCallsCount;

    /**
     * The start time of the execution.
     */
    private long startTime;

    /**
     * The end time of the execution.
     */
    private long endTime;

    /**
     * The time duration of the entire execution.
     */
    private long totalTime;

    /**
     * The time duration of the entire execution written in the following
     * human-readable format: {@code 00h:00m:00s.000}.
     */
    private String lapsedTime;

    /**
     * Constructs the code statistics based on the parameters specified.
     *
     * @param cp                  the code parameters of \(\mathsf{C}\)
     * @param cvp                 the validator parameters of the code
     * @param recursiveCallsCount the number of recursive calls needed
     * @param startTime           the start time of the execution of the code
     * @param endTime             the end time of the execution of the code
     */
    public CodeStatistics(
            CodeParameters cp,
            CodeValidatorParameters cvp,
            long recursiveCallsCount,
            long startTime,
            long endTime
    ) {
        this.cp = cp;
        this.cvp = cvp;
        this.recursiveCallsCount = recursiveCallsCount;
        this.startTime = startTime;
        this.endTime = endTime;
        totalTime = calculateLapsedTime(startTime, endTime);
        lapsedTime = formatLapsedTime(startTime, endTime);
    }

    /**
     * Find the time lapsed between the start and end of the execution.
     *
     * @param start the start time of the execution
     * @param end   the end time of the execution
     * @return the time lapsed between the start and end of the execution
     */
    private long calculateLapsedTime(long start, long end) {
        return end - start;
    }

    /**
     * Returns the time lapsed in the following format:
     * {@code 00h:00m:00s.0000}.
     *
     * @param start the start time of the execution
     * @param end   the end time of the execution
     * @return the time lapsed formatted in a human-readable style
     */
    private String formatLapsedTime(long start, long end) {
        return Benchmarker.getLapsedTime(start, end);
    }

    /**
     * Returns the code parameters associated with the code.
     *
     * @return the code parameters associated with the code
     */
    public CodeParameters getCodeParameters() {
        return cp;
    }

    /**
     * Returns the code validator parameters associated with the code.
     *
     * @return the code validator parameters associated with the code
     */
    public CodeValidatorParameters getCodeValidatorParameters() {
        return cvp;
    }

    /**
     * Returns the number of recursive calls carried out during execution.
     *
     * @return the number of recursive calls carried out during execution
     */
    public long getRecursiveCallsCount() {
        return recursiveCallsCount;
    }

    /**
     * Returns the start time of the execution.
     *
     * @return the start time of the execution
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of the execution.
     *
     * @return the end time of the execution
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * Returns the total time of the execution as a {@code long}. It is
     * better to use {@code getLapsedTime()} instead as it will return a
     * human-readable format of the code's time execution.
     *
     * @return the total time of the execution as a {@code long}
     * @see CodeStatistics#getLapsedTime()
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * Returns the total time of the execution as a human-readable format.
     * This method will probably be used a lot more often than {@code
     * getTotalTime()}, which returns the lapsed time as a
     * {@code long}.
     *
     * @return the total time of the execution as a human-readable format
     * @see CodeStatistics#getTotalTime()
     */
    public String getLapsedTime() {
        return lapsedTime;
    }
}
