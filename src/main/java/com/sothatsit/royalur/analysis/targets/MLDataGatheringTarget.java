package com.sothatsit.royalur.analysis.targets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sothatsit.royalur.ai.CachedAgent;
import com.sothatsit.royalur.ai.ExpectimaxAgent;
import com.sothatsit.royalur.ai.RandomAgent;
import com.sothatsit.royalur.ai.utility.CanonicaliseWinsUtilityFn;
import com.sothatsit.royalur.simulation.*;

/**
 * This target aims to generate data so that we can train machine
 * learning models to play the Royal Game of Ur.
 *
 * @author Raphaël Côté, Padraig X. Lamont
 */
public class MLDataGatheringTarget extends Target {

    public static final String NAME = "MLDataGathering";
    public static final String DESC = "This target aims to generate data so that we can train machine learning models to play the Royal Game of Ur.";

    /**
     * Generates positions and their evaluations.
     */
    private static final class GenerateDataTask implements Runnable {

        private final Agent evaluationAgent;
        private final Queue<String> outputQueue;

        public GenerateDataTask(Agent evaluationAgent, Queue<String> outputQueue) {
            this.evaluationAgent = evaluationAgent;
            this.outputQueue = outputQueue;
        }

        private void generateMoveEvaluations(Game game) {
            Game testGame = new Game();
            MoveList legalMoves = new MoveList();

            for (int roll = 1; roll <= Roll.MAX; ++roll) {
                game.findPossibleMoves(roll, legalMoves);
                Map<Pos, Float> scoredMoves = evaluationAgent.scoreMoves(game, roll, legalMoves);

                for (Entry<Pos, Float> entry : scoredMoves.entrySet()) {
                    int pos = entry.getKey().pack();
                    float utility = entry.getValue();
                    if (!game.state.isLightActive) {
                        utility *= -1;
                    }

                    testGame.copyFrom(game);
                    testGame.performMove(pos, roll);

                    String line = testGame.board + "," +
                            testGame.light.tiles + "," +
                            testGame.dark.tiles + "," +
                            utility;
                    outputQueue.add(line);
                }
            }
        }

        @Override
        public void run() {
            Agent r1 = new RandomAgent();
            Agent r2 = new RandomAgent();
            Game game = new Game();
            while (!Thread.interrupted()) {
                game.reset();

                int moveNumber = 0;
                while (!game.state.finished) {
                    game.simulateOneMove(r1, r2);
                    moveNumber += 1;

                    // Skip the first few moves, as we don't want
                    // to record the same moves thousands of times.
                    if (moveNumber >= 5) {
                        generateMoveEvaluations(game);
                    }
                }
                System.out.println("(Game done)");
            }
        }
    }

    public MLDataGatheringTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        Queue<String> outputQueue = new ConcurrentLinkedQueue<>();
        Agent evaluationAgent = new CachedAgent<>(new ExpectimaxAgent(
                new CanonicaliseWinsUtilityFn(), 8
        ));

        System.out.println("Running using " + threadCount + " threads");
        for (int i = 0; i < threadCount; i++) {
            executor.submit(new GenerateDataTask(evaluationAgent.clone(), outputQueue));
        }

        File outputFile = new File("expectimax8_evaluations_dataset.csv");
        if (outputFile.exists())
            throw new IllegalStateException("The output file, " + outputFile + ", already exists.");

        BufferedWriter out = null;
        long lineCounter = 0;
        long start = System.currentTimeMillis();
        try {
            out = new BufferedWriter(new FileWriter(outputFile));
            out.write("board,light_tiles,dark_tiles,evaluation");
            out.newLine();
            out.flush();
            while (!Thread.interrupted()) {
                String line = outputQueue.poll();
                if (line != null) {
                    lineCounter++;
                    out.write(line);
                    out.newLine();
                    if (lineCounter % 500 == 0) {
                        float sec = (System.currentTimeMillis() - start) / 1000F;
                        System.out.println(
                                lineCounter + ", " + sec + " seconds, " +
                                        (lineCounter - 1) / sec + " per second"
                        );
                        out.flush();
                    }
                } else {
                    Thread.sleep(500);
                }
            }
        } catch (IOException | InterruptedException exception) {
            exception.printStackTrace();
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
                System.out.println("Output results to " + outputFile);
            }
        };
    }
}
