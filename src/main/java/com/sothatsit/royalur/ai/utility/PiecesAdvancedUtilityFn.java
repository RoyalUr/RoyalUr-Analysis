package com.sothatsit.royalur.ai.utility;

import com.sothatsit.royalur.simulation.*;

/**
 * Scores game states based on the sum of how far each piece has moved.
 *
 * @author Paddy Lamont
 */
public class PiecesAdvancedUtilityFn implements UtilityFunction {

    @Override
    public int scoreGameStateForLight(Game game) {
        // If the game is over, just go straight for the maximum score.
        if (game.state.finished)
            return (game.state.isLightActive ? 1 : -1) * 16 * Player.MAX_TILES;

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
