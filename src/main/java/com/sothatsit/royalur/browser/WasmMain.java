package com.sothatsit.royalur.browser;

import com.sothatsit.royalur.ai.ExpectimaxAgent;
import com.sothatsit.royalur.ai.PandaAgent;
import com.sothatsit.royalur.ai.utility.PiecesAdvancedUtilityFn;
import com.sothatsit.royalur.simulation.*;
import de.mirkosertic.bytecoder.api.Export;
import de.mirkosertic.bytecoder.api.Import;

/**
 * This is the main entry-point into the application for use
 * as a WASM library. This entry point allows the use of the
 * algorithms of RoyalUrAnalysis for the RoyalUr.net webgame.
 *
 * @author Paddy Lamont
 */
public class WasmMain {

    /** Just needed for Bytecoder. Not actually used. **/
    public static void main(String[] args) {}

    /**
     * @return the latest request to be handled when {@link #handleRequest} is invoked.
     */
    @Import(module="royalUrAnalysis", name="getLastRequest")
    public static native String getLastRequest();

    /**
     * Handles a request to RoyalUrAnalysis, which is retrieved using {@link #getLastRequest()}.
     * @return the response from RoyalUrAnalysis to the request.
     */
    @Export("handleRequest")
    public static String handleRequest() {
        String request = getLastRequest();
        PacketReader reader = new PacketReader(request);

        // Read information about the AI to use.
        int depth = reader.nextInt(2);
        Agent agent = new PandaAgent(new PiecesAdvancedUtilityFn(), depth, 2);

        // Read the state of the game.
        Game game = new Game();
        game.light.tiles = reader.nextDigit();
        game.light.score = reader.nextDigit();
        game.dark.tiles = reader.nextDigit();
        game.dark.score = reader.nextDigit();
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 3; ++x) {
                int owner = reader.nextDigit();
                int tile = (owner == 1 ? Tile.DARK : (owner == 2 ? Tile.LIGHT : Tile.EMPTY));
                game.board.set(x, y, tile);
            }
        }
        int activePlayerNo = reader.nextDigit();
        game.state = (activePlayerNo == 1 ? GameState.DARK_TURN : GameState.LIGHT_TURN);

        // Read the value of the dice.
        int roll = reader.nextDigit();

        // Use the AI to determine the best move.
        MoveList legalMoves = new MoveList();
        game.findPossibleMoves(roll, legalMoves);
        int move = agent.determineMove(game, roll, legalMoves);
        int moveX = Pos.getX(move);
        int moveY = Pos.getY(move);
        System.out.println("Move " + moveX + "," + moveY + " (" + move + ") on " + game.board);

        // Write the response packet.
        PacketWriter writer = new PacketWriter();
        writer.pushDigit(moveX);
        writer.pushDigit(moveY);
        return writer.toString();
    }
}
