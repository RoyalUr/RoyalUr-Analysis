package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;
import com.sothatsit.royalur.analysis.reporting.ReportFormatter;
import com.sothatsit.royalur.analysis.reporting.TableGenerator;

/**
 * A target aimed at measuring the advantage
 * that you get if you play first.
 *
 * @author Paddy Lamont
 */
public class FirstMoveAdvantageTarget extends Target {

    public static final String NAME = "First-Move-Advantage";
    public static final String DESC = "Measures the advantage that you get by playing first.";

    public FirstMoveAdvantageTarget() {
        super(NAME, DESC);
    }

    @Override
    public void run() {
        AgentStats[] results = runGames(
                "Testing two good players against one another",
                new AgentType[] { AgentType.PANDA_DEPTH_5, AgentType.PANDA_DEPTH_5 },
                10_000
        );

        double lightWinPercentage = 0;
        double darkWinPercentage = 0;
        for (AgentStats stats : results) {
            lightWinPercentage += stats.getWinPercentageAsLight();
            darkWinPercentage += stats.getWinPercentageAsDark();
        }
        lightWinPercentage /= results.length;
        darkWinPercentage /= results.length;

        System.out.println("\n### Results of the " + NAME + " target");
        System.out.println();

        TableGenerator table = new TableGenerator("Colour", "Win Percentage");
        table.addRow("Light", ReportFormatter.formatWinPercentage(lightWinPercentage));
        table.addRow("Dark", ReportFormatter.formatWinPercentage(darkWinPercentage));
        System.out.println(table.generate());
    }
}
