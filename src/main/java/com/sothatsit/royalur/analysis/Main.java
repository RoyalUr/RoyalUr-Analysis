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
                new GreedyAgent(),
                new ExpectimaxAgent(3),
                new ExpectimaxAgent(5)
        });
        analysis.simulateGames(1000, 5);
        System.out.println();
        analysis.printReport();
        analysis.shutdown();
    }
}
