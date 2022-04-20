package hlcd.operations;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Used to time the execution duration of a run.
 *
 * <pre><code class="language-java line-numbers"> //minimal example to execute this class:
 * public static void main(String[] args) {
 *     System.out.println("Running Benchmarker...");
 *     Benchmarker b = new Benchmarker();
 *     b.start();
 *     b.displayStartTime("The program started: ");
 *     try {//dummy timelapse
 *         Thread.sleep(1500);
 *     } catch (InterruptedException e) {
 *         e.printStackTrace();
 *     }
 *     b.end();
 *     b.displayEndTime("The program end: ");
 *     b.displayLapsedTime("The total time is: ");
 *     System.out.println("Benchmarker completed.");
 * }</code></pre>
 *
 * @author Maysara Al Jumaily
 * @version 1.0 (February 17th, 2022)
 * @since 1.8
 */
public class Benchmarker {
    private long start;
    private long end;

    /**
     * Initializes the object.
     */
    public Benchmarker() {
        //empty
    }

    /**
     * Assumes the start and end times were already captured.
     *
     * @param start the start time of the execution
     * @param end   the end time of the execution
     */
    public Benchmarker(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Sets the start mark of the execution time.
     */
    public void start() {
        start = System.currentTimeMillis();
    }

    /**
     * Prints the label on specified on screen along with the start time in a
     * human-readable format.
     *
     * @param label the label to display on screen before displaying the start
     *              time.
     */
    public void displayStartTime(String label) {
        displayTime(start, label);
    }

    /**
     * Sets the end mark of the execution time.
     */
    public void end() {
        if (end > start) {
            System.out.println("Warning from Benchmarker class: the time " +
                                       "found is negative...");
        }
        end = System.currentTimeMillis();
    }

    /**
     * Prints the end time on console in a human-readable format.
     */
    public void displayEndTime() {
        displayTime(end, "");
    }

    /**
     * Prints the label on specified on screen along with the end time in a
     * human-readable format.
     *
     * @param label the label to display on screen before displaying the end
     *              time.
     */
    public void displayEndTime(String label) {
        displayTime(end, label);
    }

    /**
     * Prints the time specified on console with an attached label.
     *
     * @param t     the time to display on console
     * @param label the label that will be attached with the time
     */
    public void displayTime(long t, String label) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS a");
        System.out.println(label + f.format(new Date(t)));
    }

    /**
     * Returns the time lapsed as a human-readable format.
     *
     * @return the time lapsed as a human-readable format
     */
    public String getLapsedTime() {
        long t = end - start;
        long millis = t % 1000;
        long x = t / 1000;
        long seconds = x % 60;
        x /= 60;
        long minutes = x % 60;
        x /= 60;
        long hours = x % 24;
        String f = "%02dh:%02dm:%02ds.%03d";
        return String.format(f, hours, minutes, seconds, millis);
    }

    /**
     * Returns the time lapsed as a human-readable format of the start and
     * end time specified.
     *
     * @param start the start time of the execution
     * @param end   the end time of the execution
     * @return the time lapsed as a human-readable format
     */
    public static String getLapsedTime(long start, long end) {
        long t = end - start;
        long millis = t % 1000;
        long x = t / 1000;
        long seconds = x % 60;
        x /= 60;
        long minutes = x % 60;
        x /= 60;
        long hours = x % 24;
        String f = "%02dh:%02dm:%02ds.%03d";
        return String.format(f, hours, minutes, seconds, millis);
    }

    /**
     * Prints the time lapsed on console as a human-readable format.
     */
    public void printLapsedTime() {
        System.out.println("The time lapsed: " + getLapsedTime());
    }

    /**
     * Prints the time lapsed on console as a human-readable format.
     *
     * @param label the label to append before displaying the timelapse
     */
    public void displayLapsedTime(String label) {
        System.out.println(label + getLapsedTime());
    }

    /**
     * Returns the current time/date formatted in the following style:
     * {@code dd/MM/yyyy hh:mm:ss.SSS a}. For example, 02/02/2022 06:02:46 PM
     *
     * @return the current time/date formatted appropriately
     */
    public static String getCurrentDateFormatted() {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS a");
        return f.format(new Date());
    }

    /**
     * Plays a beep audio and usually used when an execution is completed.
     */
    public static void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

    /* A sample execution of this class is commented out*/
//
//    /**
//     * Executes the program.
//     *
//     * @param args the arguments specified but will be ignored
//     */
//    public static void main(String[] args) {
//        System.out.println("Running Benchmarker...");
//        Benchmarker b = new Benchmarker();
//        b.start();
//        b.displayStartTime("The program started: ");
//        try {//dummy timelapse
//            Thread.sleep(1500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        b.end();
//        b.displayEndTime("The program end: ");
//        b.displayLapsedTime("The total time is: ");
//        System.out.println("Benchmarker completed.");
//    }
}