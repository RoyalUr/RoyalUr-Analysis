package com.sothatsit.royalur.browser;

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

    public static void main(String[] args) {
        // We don't actually need to do anything here.
        // We export other methods to be accessible from JavaScript.
    }

    /**
     * @return the latest request to be handled when {@link #handleRequest} is invoked.
     */
    @Import(module="royalUrAnalysis", name="getRequest")
    public static native String getRequest();

    /**
     * Handles a request to RoyalUrAnalysis, which is retrieved using {@link #getRequest()}.
     * @return the response from RoyalUrAnalysis to the request.
     */
    @Export("handleRequest")
    public static String handleRequest() {
        return "Pong: " + getRequest();
    }
}
