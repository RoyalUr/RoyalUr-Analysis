package com.sothatsit.royalur.simulation;

/**
 * Represents the current state of a game.
 *
 * @author Paddy Lamont
 */
public enum GameState {

    LIGHT_TURN(false, true),
    DARK_TURN(false, false),

    LIGHT_WON(true, true),
    DARK_WON(true, false);

    /** Whether this state represents a finished game. **/
    public final boolean finished;
    /** Whether light is the active player in this state. **/
    public final boolean isLightActive;

    GameState(boolean finished, boolean isLightActive) {
        this.finished = finished;
        this.isLightActive = isLightActive;
    }

    /** @return the state representing the next turn. **/
    public GameState nextTurn() {
        switch (this) {
            case LIGHT_TURN: return DARK_TURN;
            case DARK_TURN: return LIGHT_TURN;
            default: throw new IllegalStateException("The current state (" + this + ") is not a turn state");
        }
    }

    /** @return the state representing that the current player won. **/
    public GameState won() {
        switch (this) {
            case LIGHT_TURN: return LIGHT_WON;
            case DARK_TURN: return DARK_WON;
            default: throw new IllegalStateException("The current state (" + this + ") is not a turn state");
        }
    }
}
