package com.sothatsit.royalur.simulation;

/**
 * Represents the state of a player in the game.
 *
 * @author Paddy Lamont
 */
public class Player {

    /** The number of tiles the player starts with. **/
    public static final int MAX_TILES = 7;

    /** This player's name. **/
    public final String name;
    /** The x position of this player's home column. **/
    public final int homeCol;
    /** The tile value representing this player. **/
    public final int tile;
    /** The path to take by this player. **/
    public final Path path;

    /** The number of tiles the player has left to be played. **/
    public int tiles;
    /** The number of tiles the player has scored. **/
    public int score;

    private Player(String name, int homeCol, int tile, Path path) {
        this.name = name;
        this.homeCol = homeCol;
        this.tile = tile;
        this.path = path;
        this.tiles = MAX_TILES;
        this.score = 0;
    }

    /** Resets this player back to its default state. **/
    public void reset() {
        this.tiles = MAX_TILES;
        this.score = 0;
    }

    @Override
    public String toString() {
        return name + "(" + tiles + ", " + score + ")";
    }

    public static Player createLight() {
        return new Player("Light", Pos.LIGHT_HOME_COL, Tile.LIGHT, Path.LIGHT);
    }

    public static Player createDark() {
        return new Player("Dark", Pos.DARK_HOME_COL, Tile.DARK, Path.DARK);
    }
}
