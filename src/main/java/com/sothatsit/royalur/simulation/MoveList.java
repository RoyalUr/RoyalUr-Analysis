package com.sothatsit.royalur.simulation;

/**
 * A list of possible moves.
 *
 * @author Paddy Lamont
 */
public class MoveList {

    /** The number of moves in this list. **/
    public int count = 0;
    /** The packed positions of the pieces that can be moved. **/
    public final int[] positions = new int[Pos.MAX + 1];
    /** The packed destinations of the pieces that can be moved. **/
    public final int[] destinations = new int[Pos.MAX + 1];

    /** @return whether this move list contains a move from {@param pos}. **/
    public boolean contains(int pos) {
        for (int index = 0; index < count; ++index) {
            if (positions[index] == pos)
                return true;
        }
        return false;
    }
}
