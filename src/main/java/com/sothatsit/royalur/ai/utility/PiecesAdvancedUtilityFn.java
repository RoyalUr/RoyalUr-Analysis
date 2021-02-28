package com.sothatsit.royalur.ai.utility;

import com.sothatsit.royalur.simulation.*;

/**
 * Scores game states based on the sum of how far each piece has moved.
 *
 * @author Paddy Lamont
 */
public class PiecesAdvancedUtilityFn extends UtilityFunction {

    public PiecesAdvancedUtilityFn() {
        this("pieces-advanced");
    }

    protected PiecesAdvancedUtilityFn(String name) {
        super(name);
    }

    @Override
    public int scoreGameStateForLight(Game game) {
        Board board = game.board;
        int[] lightIndexToPos = Path.LIGHT.indexToPos;
        int[] darkIndexToPos = Path.DARK.indexToPos;

        int score = 16 * (game.light.score - game.dark.score);
        for (int index = 1; index < Path.LENGTH - 1; ++index) {
            if (board.get(lightIndexToPos[index]) == Tile.LIGHT) {
                score += index;
            } else if (board.get(darkIndexToPos[index]) == Tile.DARK) {
                score -= index;
            }
        }
        return score;
    }
}
