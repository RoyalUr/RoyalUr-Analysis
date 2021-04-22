package com.sothatsit.royalur.browser;

import com.sothatsit.royalur.RoyalUrAnalysis;
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

    /** Called when instantiated through Bytecoder. **/
    public static void main(String[] args) {
        System.out.println("Loaded RoyalUrAnalysis " + RoyalUrAnalysis.VERSION);
    }

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
        // Read the move request.
        PacketReader requestReader = new PacketReader(getLastRequest());
        MoveRequestPacket request = MoveRequestPacket.read(requestReader);

        // Create the agent to use to fulfill the request.
        Agent agent = new PandaAgent(new PiecesAdvancedUtilityFn(), request.depth, 2);

        // Use the AI to determine the best move.
        int bestMove = agent.determineMove(request.state, request.roll);

        // Write the response packet.
        return new MoveResponsePacket(bestMove).toString();
    }
}
