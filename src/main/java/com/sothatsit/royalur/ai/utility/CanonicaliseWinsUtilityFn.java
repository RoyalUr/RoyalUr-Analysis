package com.sothatsit.royalur.ai.utility;

import com.sothatsit.royalur.simulation.*;

/**
 * Scores game states based on the sum of how far each piece has moved,
 * and canonicalises winning or losing states into the maximum or
 * minimum scores.
 *
 * @author Paddy Lamont
 */
public class CanonicaliseWinsUtilityFn extends PiecesAdvancedUtilityFn {

    public CanonicaliseWinsUtilityFn() {
        this("Canonicalise Wins");
    }

    protected CanonicaliseWinsUtilityFn(String name) {
        super(name);
    }

    @Override
    public float scoreGameStateForLight(Game game) {
        // If the game is over, just go straight for the maximum score.
        if (game.state.finished)
            return (game.state.isLightActive ? 1 : -1) * 16 * Player.MAX_TILES;

        return super.scoreGameStateForLight(game);
    }
}
