package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;
import com.sothatsit.royalur.analysis.Analyser;
import com.sothatsit.royalur.analysis.reporting.ReportFormatter;
import com.sothatsit.royalur.analysis.reporting.TableGenerator;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * An analysis target. Each target is designed to gain
 * specific insights about the game.
 *
 * @author Paddy Lamont
 */
public abstract class Target {

    private static final int GAME_PROGRESS_REPORT_INTERVAL_SECS = 15;

    /** The name of this target. **/
    public final String name;
    /** The name of this target in lowercase with all punctuation removed. **/
    public final String canonicalisedName;
    /** A description of what this target measures. **/
    public final String description;

    public Target(String name, String description) {
        this.name = name;
        this.canonicalisedName = canonicaliseTargetName(name);
        this.description = description;
    }

    public abstract void run();

    /** @return the statistics after running {@param iterations} iterations of games between all agents. **/
    protected AgentStats[] runGames(String desc, AgentType[] agentTypes, int iterations) {
        System.out.println("Running " + iterations + " iterations of games for: " + desc);
        Analyser analyser = new Analyser(agentTypes);
        analyser.simulateGames(iterations, GAME_PROGRESS_REPORT_INTERVAL_SECS);
        System.out.println("\nFinished " + iterations + " iterations of games for: " + desc);
        analyser.printReport();
        System.out.println();
        AgentStats[] stats = analyser.getAgentStats();
        analyser.shutdown();
        return stats;
    }

    /** @return a table containing the win percentage of each agent. **/
    protected String generateAgentWinPercentageTable(AgentStats[] agentStats) {
        TableGenerator table = new TableGenerator("Agent", "Win Percentage");
        for (AgentStats stats : agentStats) {
            table.addRow(stats.name, ReportFormatter.formatWinPercentage(stats.getWinPercentage()));
        }
        return table.generate();
    }

    /** @return find the target with the name {@param name} from the list of targets in {@param targets}. **/
    public static Target find(String name, Target[] targets) {
        name = canonicaliseTargetName(name);
        for (Target target : targets) {
            if (name.equals(target.canonicalisedName))
                return target;
        }
        return null;
    }

    /** @return the given target name converted to lowercase, with all punctuation removed. **/
    private static String canonicaliseTargetName(String name) {
        StringBuilder builder = new StringBuilder();
        for (char ch : name.toCharArray()) {
            if (Character.isAlphabetic(ch)) {
                builder.append(Character.toLowerCase(ch));
            }
        }
        return builder.toString();
    }
}
