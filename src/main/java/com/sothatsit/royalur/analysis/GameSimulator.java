package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.simulation.Agent;
import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class handles the simulating of lots of games
 * using multi-threading to speed up the process.
 *
 * @author Paddy Lamont
 */
public class GameSimulator {

    private final ExecutorService executor;
    private final List<SimulateGameTask> outstandingTasks = new ArrayList<>();

    public GameSimulator() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        System.out.println("GameSimulator is using " + threadCount + " threads\n");
        this.executor = Executors.newFixedThreadPool(threadCount);
    }

    public void addGame(AgentStats lightStats, AgentStats darkStats) {
        Agent light = lightStats.agent.clone();
        Agent dark = darkStats.agent.clone();
        outstandingTasks.add(new SimulateGameTask(lightStats, darkStats, light, dark));
    }

    /**
     * @return the number of outstanding games that are yet to be submitted for simulation.
     */
    public int countOutstandingGames() {
        return outstandingTasks.size();
    }

    /**
     * Submits all of the outstanding games to be simulated.
     *
     * @return a latch that can be used to monitor the number of games remaining to be simulated.
     */
    public CountDownLatch simulateGames() {
        CountDownLatch latch = new CountDownLatch(outstandingTasks.size());
        for (SimulateGameTask task : outstandingTasks) {
            task.latch = latch;
            executor.submit(task);
        }
        outstandingTasks.clear();
        return latch;
    }

    /**
     * Shuts down this game simulator, stopping all of its threads.
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * Represents a game that is to be simulated.
     */
    private static final class SimulateGameTask implements Runnable {

        private final Game game;
        private final AgentStats lightStats;
        private final AgentStats darkStats;
        private final Agent light;
        private final Agent dark;
        private CountDownLatch latch;

        public SimulateGameTask(AgentStats lightStats, AgentStats darkStats, Agent light, Agent dark) {
            this.game = new Game();
            this.lightStats = lightStats;
            this.darkStats = darkStats;
            this.light = light;
            this.dark = dark;
        }

        @Override
        public void run() {
            try {
                game.reset();
                game.simulateGame(lightStats, darkStats, light, dark);
                if (game.state == GameState.LIGHT_WON) {
                    lightStats.markWin(true);
                    darkStats.markLoss(false);
                } else {
                    darkStats.markWin(false);
                    lightStats.markLoss(true);
                }
            } finally {
                latch.countDown();
            }
        }
    }
}
