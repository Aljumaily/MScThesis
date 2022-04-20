package hlcd.operations;

import hlcd.exceptions.InvalidBaseException;

/**
 * A way to find the Hamming weight for vectors in base \(2\) or \(4\). The
 * approach uses the least amount of operations. The complexity is
 * \(\mathcal{O}(C)\) and <em>without</em> looping through the vector.
 *
 * @author Maysara Al Jumaily
 * @version 1.0
 * @since February 1st, 2022
 */
public class HammingWeight {
    private final byte BASE;

    /**
     * Creates a Hamming weight calculator ready in the base specified.
     *
     * @param base the base of the code which could either be \(2\) or \(4\)
     */
    public HammingWeight(byte base) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        this.BASE = base;
    }

    /**
     * Finds the weight of the vector in the base specified.
     *
     * @param v    the vector to find the weight of
     * @param base the base of the code which could either be \(2\) or \(4\)
     * @return the Hamming weight of the vector
     */
    private static byte getWeightEngine(long v, byte base) {
        if (base == 2) {
            //from Java's official documentation
            v = v - ((v >>> 1) & Functions.ONE);
            v = (v & Functions.THREE) + ((v >>> 2) & Functions.THREE);
            v = (v + (v >>> 4)) & Functions.F;
            v = v + (v >>> 8);
            v = v + (v >>> 16);
            v = v + (v >>> 32);
            return (byte) (v & 0x7f);
        } else if (base == 4) {
            //from https://stackoverflow.com/a/65262283/10082415
            v = (v & Functions.ONE) | ((v >>> 1) & Functions.ONE);
            v -= (v >>> 1) & Functions.ONE;
            v = (v & Functions.THREE) + ((v >>> 2) & Functions.THREE);
            v = (v + (v >>> 4)) & Functions.F;
            v += v >>> 8;
            v += v >>> 16;
            v += v >>> 32;
            return (byte) (v & 0x7f);
        }
        return (byte) v;
    }

    /**
     * Returns the weight of the vector specified.
     *
     * @param vector the vector to find the weight of
     * @return the Hamming weight of the vector
     */
    public byte getWeight(long vector) {
        return getWeightEngine(vector, BASE);
    }

    /**
     * Finds the weight of the vector in the base specified.
     *
     * @param vector the vector to find the weight of
     * @param base   the base of the code which could either be \(2\) or \(4\)
     * @return the Hamming weight of the vector
     */
    public static byte getWeight(long vector, byte base) {
        if (!Functions.isValidBase(base)) {
            throw new InvalidBaseException(base);
        }
        return getWeightEngine(vector, base);
    }

    /**
     * Returns the base the Hamming weight calculator is operating in.
     *
     * @return the base of the code
     */
    public byte getBase() {
        return BASE;
    }
}
