package hlcd.enums;

/**
 * An enum for choosing how should a matrix in the program be displayed. We
 * can use the decimal representation (\(0_{10}\), \(1_{10}\), \(2_{10}\) and
 * \(3_{10}\)) or binary representation (\(00_{2}\), \(01_{2}\), \(10_{2}\) and
 * \(11_{2}\)) or quaternary representation (\(0_{4}\), \(1_{4}\),
 * \(\omega_{4}\) and \(\overline{\omega}_{4}\)). Lastly, the \(\LaTeX\)
 * representation writes styles the digits as: {@code 0}, {@code 1}, {@code
 * \omega} and {@code \overline{\omega}}, respectively.
 *
 * @author Maysara Al Jumaily
 * @version 1.0
 * @since February 2nd, 2022
 */
public enum Style {
    /**
     * The decimal presentation (\(0_{10}\), \(1_{10}\), \(2_{10}\) and
     * \(3_{10}\)).
     */
    DECIMAL,
    /**
     * The binary presentation (\(00_{2}\), \(01_{2}\), \(10_{2}\) and
     * \(11_{2}\)).
     */
    BINARY,
    /**
     * The quaternary presentation (\(0_{4}\), \(1_{4}\), \(\omega_{4}\) and
     * \(\overline{\omega}_{4}\)).
     */
    QUATERNARY,

    /**
     * The \(\LaTeX\) representation {@code 0}, {@code 1}, {@code \omega} and
     * {@code \overline{\omega}}).
     */
    LATEX
}