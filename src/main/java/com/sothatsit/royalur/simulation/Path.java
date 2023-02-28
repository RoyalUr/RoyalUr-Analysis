package com.sothatsit.royalur.simulation;

import java.util.Arrays;

/**
 * Holds information about the path that light and dark tiles can take around the board.
 *
 * @author Paddy Lamont
 */
public final class Path {

    /** The length of the light and dark paths, including the start and end tiles. **/
    public static final int LENGTH = 16;

    /** The light path around the board. **/
    public static final Path LIGHT = new Path(Pos.LIGHT_HOME_COL);
    /** The dark path around the board. **/
    public static final Path DARK = new Path(Pos.DARK_HOME_COL);

    /** This includes the start and end tiles. **/
    public final Pos[] path = new Pos[LENGTH];
    /** The path with each position packed. **/
    public final int[] indexToPos = new int[LENGTH];
    /** Maps from packed positions to the index of that position in the path. **/
    public final int[] posToIndex = new int[Pos.MAX + 1];
    /** Maps from a position on this path to the next position after moving by the given roll. **/
    public final int[][] posToDestByRoll = new int[Roll.MAX + 1][Pos.MAX + 1];

    public Path(int homeCol) {
        // Generate the path.
        for (int index = 0; index < LENGTH; ++index) {
            if (index < 5) {
                path[index]  = new Pos(homeCol, 4 - index);
            } else if (index < 13) {
                path[index]  = new Pos(1, index - 5);
            } else {
                path[index]  = new Pos(homeCol, 20 - index);
            }
            indexToPos[index] = path[index].pack();
        }

        // Generate the pos -> index mapping.
        Arrays.fill(posToIndex, -1);
        for (int index = 0; index < LENGTH; ++index) {
            posToIndex[indexToPos[index]] = index;
        }

        // Generate the dice -> pos -> destination mapping.
        for (int roll = 0; roll <= Roll.MAX; ++roll) {
            int[] posToDest = posToDestByRoll[roll];

            Arrays.fill(posToDest, -1);
            for (int index = 0; index < LENGTH - roll; ++index) {
                posToDest[indexToPos[index]] = indexToPos[index + roll];
            }
        }
    }

    /** @return whether {@param pos} is the start tile. **/
    public boolean isStart(int pos) {
        return pos == indexToPos[0];
    }

    /** @return whether {@param pos} is the end tile. **/
    public boolean isEnd(int pos) {
        return pos == indexToPos[LENGTH - 1];
    }

    /** @return whether {@param pos} falls within this path. **/
    public boolean contains(int pos) {
        for (Pos potential : path) {
            if (potential.pack() == pos)
                return true;
        }
        return false;
    }
}
