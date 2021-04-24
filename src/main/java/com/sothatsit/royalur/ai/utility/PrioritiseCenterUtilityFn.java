package com.sothatsit.royalur.ai.utility;

import com.sothatsit.royalur.simulation.*;

/**
 * Scores game states based on the sum of how far each piece has moved,
 * a bonus for having a piece on the center rosette, and canonicalises
 * winning or losing states into the maximum or minimum scores.
 *
 * @author Paddy Lamont
 */
public class PrioritiseCenterUtilityFn extends CanonicaliseWinsUtilityFn {

    private final float bonus;

    public PrioritiseCenterUtilityFn(float bonus) {
        super("prioritise-center");
        this.bonus = bonus;
    }

    @Override
    public float scoreGameStateForLight(Game game) {
        float score = super.scoreGameStateForLight(game);
        if (game.state.finished)
            return score;

        int center = game.board.get(1, 4);
        if (center == Tile.EMPTY)
            return score;

        return score + (center == Tile.LIGHT ? bonus : -bonus);
    }
}
