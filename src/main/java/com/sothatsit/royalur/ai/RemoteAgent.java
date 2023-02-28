package com.sothatsit.royalur.ai;

import java.util.HashMap;
import java.util.Map;

import com.sothatsit.royalur.simulation.Agent;
import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.MoveList;
import com.sothatsit.royalur.simulation.Pos;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import kong.unirest.json.JSONArray;

/**
 * An agent that retrieves its move evaluations from an external server.
 */
public class RemoteAgent extends Agent {

    private final String url;

    public RemoteAgent(String url) {
        super("RemoteAgent");
        this.url = url;
    }

    @Override
    public Agent clone() {
        return new RemoteAgent(url);
    }

    public static RemoteAgent create(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException(
                    "Expected a single argument: the remote URL. " +
                            "Received no arguments"
            );
        }
        if (args.length > 1) {
            throw new IllegalArgumentException(
                    "Expected only a single argument: the remote URL. " +
                            "Received " + args.length + " arguments"
            );
        }
        return new RemoteAgent(args[0]);
    }

    @Override
    public int determineMove(Game game, int roll, MoveList legalMoves) {
        if (legalMoves.count == 0)
            return -1;
        if (legalMoves.count == 1)
            return legalMoves.positions[0];
        
        Map<Pos, Float> scoredMoves = scoreMoves(game, roll, legalMoves);
        Pos maxMove = new Pos(0);
        float maxUtility = Float.NEGATIVE_INFINITY;
        for (Map.Entry<Pos, Float> entry : scoredMoves.entrySet()) {
            float utility = entry.getValue();
            if (utility > maxUtility) {
                maxUtility = utility;
                maxMove = entry.getKey();
            }
        }
        return maxMove.pack();
    }
    

    @Override
    public Map<Pos, Float> scoreMoves(Game game, int roll, MoveList legalMoves) {
        Map<Pos, Float> scores = new HashMap<>();

        JSONArray arr = new JSONArray();

        for (int index = 0; index < legalMoves.count; ++index) {
            JSONObject obj = new JSONObject();
            obj.put("roll", roll);
            obj.put("game", game.board.toString());
            int move = legalMoves.positions[index];
            Pos p = new Pos(move);
            obj.put("x", p.x);
            obj.put("y", p.y);
            obj.put("light_turn", game.state.isLightActive);
            obj.put("light_score", game.light.score);
            obj.put("dark_score", game.dark.score);
            obj.put("light_left", game.light.tiles);
            obj.put("dark_left", game.dark.tiles);
            arr.put(obj);
        }

        HttpResponse<JsonNode> response = Unirest.post(url)
            .header("accept", "application/json")
            .header("Content-Type", "application/json")
            .body(arr.toString())
            .asJson();
        JSONArray utilities = response.getBody().getObject().getJSONArray("utilities");
        
        for (int index = 0; index < legalMoves.count; ++index) {
            int move = legalMoves.positions[index];
            Pos p = new Pos(move);
            scores.put(p, utilities.getFloat(index));
        }
        return scores;
    }
    
}
