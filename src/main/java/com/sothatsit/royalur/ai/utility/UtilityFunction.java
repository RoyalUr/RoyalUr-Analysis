package com.sothatsit.royalur.ai.utility;

import com.sothatsit.royalur.simulation.Game;

/**
 *
 *
 * @author Paddy Lamont
 */
public interface UtilityFunction {

    /** @return a utility value to light for the state {@param game}. **/
    int scoreGameStateForLight(Game game);

    /** @return a utility value for the active player for the state {@param game}. **/
    default int scoreGameState(Game game) {
        int utility = scoreGameStateForLight(game);
        return game.state.isLightActive ? utility : -utility;
    }
}
