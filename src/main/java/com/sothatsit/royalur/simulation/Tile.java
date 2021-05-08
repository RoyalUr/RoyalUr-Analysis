package com.sothatsit.royalur.simulation;

/**
 * Holds information about the tiles on The Royal Game of Ur board.
 *
 * @author Paddy Lamont
 */
public class Tile {

    /** The packed position of the middle rosette on the board. **/
    public static final int MIDDLE_ROSETTE = Pos.pack(1, 3);
    /** The packed position of the rosette at the top of the light side of the board. **/
    public static final int LIGHT_TOP_ROSETTE = Pos.pack(0, 0);
    /** The packed position of the rosette at the bottom of the light side of the board. **/
    public static final int LIGHT_BOTTOM_ROSETTE = Pos.pack(0, 6);
    /** The packed position of the rosette at the top of the dark side of the board. **/
    public static final int DARK_TOP_ROSETTE = Pos.pack(2, 0);
    /** The packed position of the rosette at the bottom of the dark side of the board. **/
    public static final int DARK_BOTTOM_ROSETTE = Pos.pack(2, 6);

    /** The x coordinate of light's safe tiles. **/
    public static final int LIGHT_SAFE_X = 0;
    /** The x coordinate of dark's safe tiles. **/
    public static final int DARK_SAFE_X = 2;

    /** Represents a tile that is empty. **/
    public static final int EMPTY = 0;
    /** Represents a tile that contains a light piece. **/
    public static final int LIGHT = 1;
    /** Represents a tile that contains a dark piece. **/
    public static final int DARK = 2;

    /** @return the opposite coloured tile to {@param tile}. Light if dark, dark if light. **/
    public static int getOther(int tile) {
        return (tile == LIGHT ? DARK : (tile == DARK ? LIGHT : EMPTY));
    }

    /** @return whether the given position is on the board. **/
    public static boolean isOnBoard(int pos) {
        return isOnBoard(Pos.getX(pos), Pos.getY(pos));
    }

    /** @return whether the given position is on the board. **/
    public static boolean isOnBoard(int x, int y) {
        return x == 1 || (y != 4 && y != 5);
    }

    /** @return whether the given packed position is a rosette tile. **/
    public static boolean isRosette(int x, int y) {
        return isRosette(Pos.pack(x, y));
    }

    /** @return whether the given packed position is a rosette tile. **/
    public static boolean isRosette(int pos) {
        return pos == Tile.MIDDLE_ROSETTE
                || pos == Tile.LIGHT_TOP_ROSETTE || pos == Tile.LIGHT_BOTTOM_ROSETTE
                || pos == Tile.DARK_TOP_ROSETTE || pos == Tile.DARK_BOTTOM_ROSETTE;
    }
}
