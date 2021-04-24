package com.sothatsit.royalur.ai.utility;

import com.sothatsit.royalur.simulation.*;

/**
 * Scores game states based on the sum of how far each piece has moved.
 *
 * @author Paddy Lamont
 */
public class PiecesAdvancedUtilityFn extends UtilityFunction {

    /** The utility for light of having a light tile at any position on the board. **/
    private static final int[] LIGHT_POS_UTILITIES = new int[Pos.MAX + 1];
    /** The utility for light of having a dark tile at any position on the board. **/
    private static final int[] DARK_POS_UTILITIES = new int[Pos.MAX + 1];
    static {
        for (int index = 1; index < Path.LENGTH - 1; ++index) {
            LIGHT_POS_UTILITIES[Path.LIGHT.indexToPos[index]] = index;
            DARK_POS_UTILITIES[Path.DARK.indexToPos[index]] = -index;
        }
    }

    public PiecesAdvancedUtilityFn() {
        this("pieces-advanced");
    }

    protected PiecesAdvancedUtilityFn(String name) {
        super(name);
    }

    @Override
    public float scoreGameStateForLight(Game game) {
        return 16 * (game.light.score - game.dark.score) + game.board.piecesAdvancedUtility;
    }

    public int scoreGameStateForLightFromScratch(Game game) {
        Board board = game.board;
        int[] lightIndexToPos = Path.LIGHT.indexToPos;
        int[] darkIndexToPos = Path.DARK.indexToPos;

        int score = 16 * (game.light.score - game.dark.score);
        for (int index = 1; index < Path.LENGTH - 1; ++index) {
            if (board.get(lightIndexToPos[index]) == Tile.LIGHT) {
                score += index;
            }
            if (board.get(darkIndexToPos[index]) == Tile.DARK) {
                score -= index;
            }
        }
        return score;
    }

    /**
     * @return the amount of utility to be given for the tile
     *         {@param tile} at the given position {@param pos}.
     */
    public static int getPieceLightUtility(int pos, int tile) {
        if (tile == Tile.LIGHT)
            return LIGHT_POS_UTILITIES[pos];
        if (tile == Tile.DARK)
            return DARK_POS_UTILITIES[pos];
        return 0;
    }
}
