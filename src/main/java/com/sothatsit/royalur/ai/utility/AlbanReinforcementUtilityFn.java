package com.sothatsit.royalur.ai.utility;

import com.sothatsit.royalur.simulation.*;

/**
 * This utility function was trained using simulations by Alban
 * from The Royal Game of Ur Discord. Alban's code that was used
 * to train this model is available on GitHub at
 * https://github.com/Cemoixerestre/Ur.
 */
public class AlbanReinforcementUtilityFn extends UtilityFunction {

    /** The value to give to winning states. **/
    private static final float victory_value = 63;

    /** The value of pieces that are yet to be played. **/
    private static final float val_ready = 0;

    /** The value of pieces on the board. **/
    private static final float[] val_cells = {
            2.30852097f,   // A4 & C4
            3.06818316f,   // A3 & C3
            3.887991072f,  // A2 & C2
            2.99065977f,   // A1 & C1
            3.812582214f,  // B1
            3.882839058f,  // B2
            4.937741739f,  // B3
            10.700026911f, // B4
            7.653518775f,  // B5
            8.64846801f,   // B6
            10.060918371f, // B7
            11.82866769f,  // B8
            14.349312495f, // A8 & C8
            12.624119235f  // A7 & C7
    };

    /** The value of pieces that have been taken off the board. **/
    private static final float val_out = 15.17204619f;

    /** The advantage given to the current player. **/
    private static final float player_adv = 1.729259091f;


    public AlbanReinforcementUtilityFn() {
        super("Alban Reinforcement Learning");
    }

    @Override
    public float scoreGameStateForLight(Game game) {
        GameState state = game.state;
        if (state.finished)
            return state.isLightActive ? victory_value : -victory_value;

        Player light = game.light;
        Player dark = game.dark;
        Board board = game.board;

        float val = state.isLightActive ? player_adv : -player_adv;
        val += (light.tiles - dark.tiles) * val_ready;

        int[] lightPath = Path.LIGHT.indexToPos;
        int[] darkPath = Path.DARK.indexToPos;
        for (int pathIndex = 0; pathIndex < 14; ++pathIndex) {
            if (board.get(lightPath[pathIndex + 1]) == Tile.LIGHT) {
                val += val_cells[pathIndex];
            }
            if (board.get(darkPath[pathIndex + 1]) == Tile.DARK) {
                val -= val_cells[pathIndex];
            }
        }

        val += (light.score - dark.score) * val_out;
        return val;
    }
}
