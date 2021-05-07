package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.ai.ExpectimaxAgent;
import com.sothatsit.royalur.ai.PandaAgent;
import com.sothatsit.royalur.ai.utility.PrioritiseCenterUtilityFn;
import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;
import com.sothatsit.royalur.analysis.reporting.ReportFormatter;
import com.sothatsit.royalur.analysis.reporting.TableGenerator;
import com.sothatsit.royalur.simulation.*;

import java.util.Map;

/**
 * This target aims to let users analyse specific game states.
 *
 * @author Paddy Lamont
 */
public class AnalysisTarget extends Target {

    public static final String NAME = "Analysis";
    public static final String DESC = "Analyses the possible moves from a position.";

    public AnalysisTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        Agent agent = new ExpectimaxAgent(new PrioritiseCenterUtilityFn(4.0f), 7);

        int roll = 2;
        Game game = new Game();
        game.setState(GameState.LIGHT_TURN);
        game.setLightTiles(4);
        game.setDarkTiles(4);
        game.setTile(0, 0, Tile.LIGHT);
        game.setTile(0, 1, Tile.LIGHT);
        game.setTile(2, 0, Tile.DARK);
        game.setTile(2, 2, Tile.DARK);
        game.setTile(1, 1, Tile.DARK);
        game.setTile(1, 7, Tile.LIGHT);

        Map<Integer, Float> scores = agent.scoreMoves(game, roll);

        return new TargetResult(this) {
            @Override
            public void print() {
                System.out.println("\n### Results of the " + NAME + " target");
                System.out.println();

                TableGenerator table = new TableGenerator("X", "Y", "Score");
                for (Map.Entry<Integer, Float> move : scores.entrySet()) {
                    int x = Pos.getX(move.getKey());
                    int y = Pos.getY(move.getKey());
                    table.addRow(x, y, ReportFormatter.format2DP(move.getValue()));
                }
                System.out.println(table.generate());
            }
        };
    }
}
