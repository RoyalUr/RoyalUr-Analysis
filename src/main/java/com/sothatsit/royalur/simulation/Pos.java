package com.sothatsit.royalur.simulation;

/**
 * Handles representations of positions of pieces on the game board.
 *
 * @author Paddy Lamont
 */
public final class Pos {

    /** One above the maximum possible packed position. **/
    public static final int MAX = pack(2, 7);

    /** The x of the light player's home column. **/
    public static final int LIGHT_HOME_COL = 0;
    /** The x of the dark player's home column. **/
    public static final int DARK_HOME_COL = 2;

    public final int x;
    public final int y;

    public Pos(int pos) {
        this(getX(pos), getY(pos));
    }

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** @return this Pos converted to a packed int. **/
    public int pack() {
        return pack(x, y);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(pack());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pos))
            return false;
        return pack() == ((Pos) obj).pack();
    }

    @Override
    public String toString() {
        return (char) ('A' + x) + Integer.toString(y + 1);
    }

    /**
     * We use the two least-significant bits for x,
     * and the next three least-significant bits for y.
     */
    public static int pack(int x, int y) {
        return x | (y << 2);
    }

    /** @return the X position of {@param pos} on the board. **/
    public static int getX(int pos) {
        return pos & 0b11;
    }

    /** @return the Y position of {@param pos} on the board. **/
    public static int getY(int pos) {
        return pos >> 2;
    }
}
