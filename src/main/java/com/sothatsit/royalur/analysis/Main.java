package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.ai.*;

/**
 * The main entry-point to this Royal Ur Analysis program.
 *
 * @author Paddy Lamont
 */
public class Main {

    public static void main(String[] args) {
        Analysis analysis = new Analysis(new Agent[] {
                new RandomAgent(),
                new FirstMoveAgent(),
                new LastMoveAgent(),
                new GreedyAgent()
        });
        analysis.simulateGames(10_000);
        analysis.printReport();
    }
}
