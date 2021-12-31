package com.sothatsit.royalur.analysis.targets;

/**
 * This target aims to rank all the different
 * agents that make up the RoyalUrAnalysis lineup.
 *
 * @author Paddy Lamont
 */
public class LeaderboardTarget extends Target {

    public static final String NAME = "Leaderboard";
    public static final String DESC = "Measures the skill of our agents.";

    public LeaderboardTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        // TODO
        throw new UnsupportedOperationException("The leaderboard target has not been implemented yet");
    }
}
