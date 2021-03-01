package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;
import com.sothatsit.royalur.analysis.reporting.ReportFormatter;
import com.sothatsit.royalur.analysis.reporting.TableGenerator;

/**
 * This target aims to benchmark our agents to help
 * us to improve their performance.
 *
 * @author Paddy Lamont
 */
public class BenchmarkTarget extends Target {

    public static final String NAME = "Benchmark";
    public static final String DESC = "Measures the performance of our agents.";

    public BenchmarkTarget() {
        super(NAME, DESC);
    }

    @Override
    public void run() {
        AgentStats[] results = runGames(
                "Testing two of our slower agents against one another",
                new AgentType[] { AgentType.PANDA_DEPTH_5, AgentType.EXPECTIMAX_DEPTH_5 },
                1000
        );

        System.out.println("\n### Results of the " + NAME + " target");
        System.out.println();

        TableGenerator table = new TableGenerator("Agent", "Move Duration");
        for (AgentStats stats : results) {
            table.addRow(stats.name, ReportFormatter.formatMSPerMove(stats.getMsPerMove()));
        }
        System.out.println(table.generate());
    }
}
