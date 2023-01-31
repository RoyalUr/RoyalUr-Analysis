package com.sothatsit.royalur.analysis.targets;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sothatsit.royalur.ai.ExpectimaxAgent;
import com.sothatsit.royalur.ai.RandomAgent;
import com.sothatsit.royalur.ai.utility.CanonicaliseWinsUtilityFn;
import com.sothatsit.royalur.simulation.*;

/**
 * This target aims to generate data so we can approximate Expectimax depth 7.
 *
 * @author Raphaël Côté
 */
public class ExpectimaxApproxTarget extends Target {

    public static final String NAME = "ExpectimaxApprox";
    public static final String DESC = "This target aims to generate data so we can approximate Expectimax depth 8.";

    private static String generateGameStateString(Game game) {
        Board board = game.board;
        Player light = game.light;
        Player dark = game.dark;
        return board + "," + light.tiles + "," + dark.tiles + "," + light.score + "," + dark.score;
    }

    /**
     * Generates positions and their evaluations using Expectimax.
     */
    private static final class GeneratePositionsTask implements Runnable {

        private final Queue<String> outputQueue;
        private final Set<String> seenStates;

        public GeneratePositionsTask(Queue<String> outputQueue, Set<String> seenStates) {
            this.outputQueue = outputQueue;
            this.seenStates = seenStates;
        }

        private void generateMoveEvaluations(ExpectimaxAgent evaluationAgent, Game game) {
            String boardStringBeforeMove = game.board.toString();
            for (int roll = 1; roll <= Roll.MAX; ++roll) {
                MoveList legalMoves = new MoveList();
                game.findPossibleMoves(roll, legalMoves);

                Map<Pos, Float> scoredMoves = evaluationAgent.scoreMoves(game, roll, legalMoves);
                List<Entry<Pos, Float>> list = new ArrayList<>(scoredMoves.entrySet());
                list.sort(Entry.comparingByValue());
                Collections.reverse(list);

                int index = 1;
                for (Entry<Pos, Float> entry : list) {
                    Pos p = entry.getKey();
                    float utility = entry.getValue();
                    String line = boardStringBeforeMove + ',' + roll + ',' + p.x + ',' + p.y + ',' +
                            game.state.isLightActive + ',' + utility + ',' +
                            game.light.score + ',' + game.dark.score + ',' +
                            game.light.tiles + ',' + game.dark.tiles + ',' + index;
                    outputQueue.add(line);
                    index++;
                }
            }
        }

        @Override
        public void run() {
            Agent r1 = new RandomAgent();
            Agent r2 = new RandomAgent();
            Game game = new Game();
            ExpectimaxAgent evaluationAgent = new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 8, true);
            while (true) {
                game.reset();
                while (!game.state.finished) {
                    // We only want to evaluate the position if we haven't seen it before.
                    String state = generateGameStateString(game);
                    if (!seenStates.add(state)) {
                        generateMoveEvaluations(evaluationAgent, game);
                    }

                    // Generate a new position.
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
        Queue<String> outputQueue = new ConcurrentLinkedQueue<>();
        Set<String> seenStates = Collections.synchronizedSet(new HashSet<>());

        System.out.println("Running using " + threadCount + " threads");
        for (int i = 0; i < threadCount; i++) {
            executor.submit(new GeneratePositionsTask(outputQueue, seenStates));
        }
        
        String path = "expectimax8_utility_dataset.csv";
        BufferedWriter out = null;
        long lineCounter = 1; // so modulo does not print 0
        long start = System.currentTimeMillis();
        try {
            out = new BufferedWriter(new FileWriter(path));
            out.write("game,roll,x,y,light_turn,utility,light_score,dark_score,light_left,dark_left,rank");
            out.newLine();
            out.flush();
            while (true) {
                String line = outputQueue.poll();
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
                System.out.println("Output results to " + path);
            }
        };
    }
}
