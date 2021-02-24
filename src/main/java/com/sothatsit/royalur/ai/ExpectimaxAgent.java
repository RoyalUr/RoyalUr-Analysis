package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.simulation.*;

/**
 * An agent that uses expectimax to determine the best move.
 *
 * @author Paddy Lamont
 */
public class ExpectimaxAgent extends Agent {

    /** The probability of each of the rolls of the dice happening. **/
    private static final float[] MOVE_PROBABILITIES = {
            1.0f / 16.0f,
            4.0f / 16.0f,
            6.0f / 16.0f,
            4.0f / 16.0f,
            1.0f / 16.0f,
    };

    /** The maximum depth to search to. **/
    private final int depth;
    /** Game objects to re-use while exploring. **/
    private final Game[] games;
    /** MoveList objects to re-use while exploring. **/
    private final MoveList[] moveLists;

    public ExpectimaxAgent(int depth) {
        super("Expectimax-" + depth);
        this.depth = depth;
        this.games = new Game[depth + 1];
        this.moveLists = new MoveList[depth + 1];
        for (int index = 0; index <= depth; ++index) {
            games[index] = new Game();
            moveLists[index] = new MoveList();
        }
    }

    @Override
    public ExpectimaxAgent clone() {
        return new ExpectimaxAgent(depth);
    }

    /** @return a utility value to light for the state {@param game}. **/
    public int scoreGameStateForLight(Game game) {
        // If the game is over, just go straight for the maximum score.
        if (game.state.finished)
            return (game.state.isLightActive ? 1 : -1) * 16 * Player.MAX_TILES;

        Board board = game.board;
        int[] lightIndexToPos = Path.LIGHT.indexToPos;
        int[] darkIndexToPos = Path.DARK.indexToPos;

        int score = 16 * (game.light.score - game.dark.score);
        for (int index = 1; index < Path.LENGTH - 1; ++index) {
            if (board.get(lightIndexToPos[index]) == Tile.LIGHT) {
                score += index;
            } else if (board.get(darkIndexToPos[index]) == Tile.DARK) {
                score -= index;
            }
        }
        return score;
    }

    /** @return a utility value for the active player for the state {@param game}. **/
    public int scoreGameState(Game game) {
        int utility = scoreGameStateForLight(game);
        return game.state.isLightActive ? utility : -utility;
    }

    public float calculateBestMoveUtility(Game precedingGame, int roll, int depth) {
        Game game = games[depth];

        MoveList legalMoves = moveLists[depth];
        precedingGame.findPossibleMoves(roll, legalMoves);

        // If there are no possible moves, we need to swap players.
        if (legalMoves.count == 0 || legalMoves.count == 1) {
            game.copyFrom(precedingGame);

            if (legalMoves.count == 0) {
                game.nextTurn();
            } else {
                game.performMove(legalMoves.positions[0], roll);
            }

            float utility = calculateProbabilityWeightedUtility(game, depth + 1);
            // Correct for if the utility is the utility of the other player.
            utility *= (precedingGame.state.isLightActive == game.state.isLightActive ? 1 : -1);
            return utility;
        }

        float maxUtility = Float.NEGATIVE_INFINITY;
        for (int move = 0; move < legalMoves.count; ++move) {
            game.copyFrom(precedingGame);
            game.performMove(legalMoves.positions[move], roll);

            float utility = calculateProbabilityWeightedUtility(game, depth + 1);
            // Correct for if the utility is the utility of the other player.
            utility *= (precedingGame.state.isLightActive == game.state.isLightActive ? 1 : -1);

            maxUtility = Math.max(maxUtility, utility);
        }
        return maxUtility;
    }

    public float calculateProbabilityWeightedUtility(Game game, int depth) {
        if (game.state.finished || depth >= this.depth)
            return scoreGameState(game);

        float utility = 0;
        for (int roll = 0; roll <= Roll.MAX; ++roll) {
            utility += MOVE_PROBABILITIES[roll] * calculateBestMoveUtility(game, roll, depth);
        }
        return utility;
    }

    @Override
    public int determineMove(Game originalGame, int roll, MoveList legalMoves) {
        Game game = games[0];

        float maxUtility = Float.NEGATIVE_INFINITY;
        int maxMove = legalMoves.positions[0];

        for (int index = 0; index < legalMoves.count; ++index) {
            int move = legalMoves.positions[index];
            game.copyFrom(originalGame);
            game.performMove(move, roll);
            float utility = calculateProbabilityWeightedUtility(game, 1);
            // Correct for if the utility is the utility of the other player.
            utility *= (originalGame.state.isLightActive == game.state.isLightActive ? 1 : -1);
            if (utility > maxUtility) {
                maxUtility = utility;
                maxMove = move;
            }
        }
        return maxMove;
    }
}
