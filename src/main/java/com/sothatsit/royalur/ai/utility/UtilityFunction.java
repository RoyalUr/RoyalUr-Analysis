package com.sothatsit.royalur.ai.utility;

import com.sothatsit.royalur.simulation.Game;

/**
 * A function that is used to score game states.
 *
 * @author Paddy Lamont
 */
public abstract class UtilityFunction {

    public final String name;

    public UtilityFunction(String name) {
        this.name = name;
    }

    /** @return a utility value to light for the state {@param game}. **/
    public abstract int scoreGameStateForLight(Game game);

    /** @return a utility value for the active player for the state {@param game}. **/
    public int scoreGameState(Game game) {
        int utility = scoreGameStateForLight(game);
        return game.state.isLightActive ? utility : -utility;
    }
}
