package com.sothatsit.royalur.simulation;

import java.util.Random;

/**
 * Produces rolls for simulated games.
 *
 * @author Paddy Lamont
 */
public class Roll {

    /** Used to generate random rolls. **/
    private static final Random RAND = new Random();

    /** The probability of each of the rolls of the dice happening. **/
    public static final float[] PROBABILITIES = {
            1.0f / 16.0f,
            4.0f / 16.0f,
            6.0f / 16.0f,
            4.0f / 16.0f,
            1.0f / 16.0f,
    };

    /** The maximum possible roll. **/
    public static final int MAX = 4;

    /** @return the resulting value after a roll of The Royal Game of Ur dice. **/
    public static int next() {
        int num = RAND.nextInt(16);
        if (num < 1) return 0;
        if (num < 5) return 1;
        if (num < 11) return 2;
        if (num < 15) return 3;
        else return 4;
    }
}
