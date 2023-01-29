package com.sothatsit.royalur.analysis.targets;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sothatsit.royalur.ai.ExpectimaxAgent;
import com.sothatsit.royalur.ai.RandomAgent;
import com.sothatsit.royalur.ai.utility.CanonicaliseWinsUtilityFn;
import com.sothatsit.royalur.simulation.Agent;
import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.MoveList;
import com.sothatsit.royalur.simulation.Pos;
import com.sothatsit.royalur.simulation.Roll;

/**
 * This target aims to generate data so we can approximate Expectimax depth 7.
 *
 * @author Raphaël Côté
 */
public class ExpectimaxApproxTarget extends Target {

    public static final String NAME = "ExpectimaxApprox";
    public static final String DESC = "This target aims to generate data so we can approximate Expectimax depth 8.";

    private static final class GetExpectimaxOpinionOnMoveTask implements Runnable {

        private ConcurrentLinkedQueue<String> queue;

        public GetExpectimaxOpinionOnMoveTask(ConcurrentLinkedQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            Agent r1 = new RandomAgent();
            Agent r2 = new RandomAgent();
            Game game = new Game();
            ExpectimaxAgent ExpectiMax7 = new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 8, true);
            while (true) {
                game.reset();
                while (!game.state.finished) {
                    String boardStringBeforeMove = game.board.toString();
                    
                    for (int roll = 1; roll <= Roll.MAX; ++roll) {
                        MoveList legalMoves = new MoveList();
                        game.findPossibleMoves(roll, legalMoves);
                        Map<Pos, Float> scoredMoves = ExpectiMax7.scoreMoves(game, roll, legalMoves);
                        if (scoredMoves.size() > 1) {
                            List<Entry<Pos, Float>> list = new ArrayList<>(scoredMoves.entrySet());
                            list.sort(Entry.comparingByValue());
                            Collections.reverse(list);
                            int index = 1;
                            for (Entry<Pos, Float> entry : list) {
                                Pos p = entry.getKey();
                                float utility = entry.getValue();
                                String line = boardStringBeforeMove + ',' + roll + ',' + p.x + ',' + p.y + ',' + game.state.isLightActive + ',' + utility + ',' + game.light.score + ',' + game.dark.score + ',' + game.light.tiles + ',' + game.dark.tiles + ',' + index;
                                queue.add(line);
                                index++;
                            }
                        }
                    }
                    game.simulateOneMove(r1, r2);
                }
                System.out.println("Game done");
            }
        }
    }

    public ExpectimaxApproxTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(new GetExpectimaxOpinionOnMoveTask(queue));
        }
        
        String path = "expectimax_utility_dataset.csv";
        BufferedWriter out = null;
        long lineCounter = 1; // so modulo does not print 0
        long start = System.currentTimeMillis();
        try {
            out = new BufferedWriter(new FileWriter(path));
            out.write("game,roll,x,y,light_turn,utility,light_score,dark_score,light_left,dark_left,rank");
            out.newLine();
            out.flush();
            while (true) {
                String line = queue.poll();
                if (line != null) {
                    lineCounter++;
                    out.write(line);
                    out.newLine();
                    if (lineCounter % 500 == 0) {
                        float sec = (System.currentTimeMillis() - start) / 1000F;
                        System.out.println(lineCounter - 1 + ", " + sec + " seconds, " + (lineCounter - 1) / sec + " per second");
                        out.flush();
                    }
                } else {
                    Thread.sleep(500);
                }
            }
        } catch (IOException | InterruptedException exception) {
            System.out.println(exception.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        

        return new TargetResult(this) {
            @Override
            public void print() {
            }
        };
    }
}
