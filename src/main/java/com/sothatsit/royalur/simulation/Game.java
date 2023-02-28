package com.sothatsit.royalur.simulation;

import com.sothatsit.royalur.analysis.AgentStats;

/**
 * Represents a game of The Royal Game of Ur.
 *
 * @author Paddy Lamont
 */
public class Game {

    /** The board on which the game is played. **/
    public final Board board = new Board();
    /** The light player. **/
    public final Player light = Player.createLight();
    /** The dark player. **/
    public final Player dark = Player.createDark();

    /** The current state of this game. **/
    public GameState state = GameState.LIGHT_TURN;

    /** Resets to the default game state. **/
    public void reset() {
        board.clear();
        light.reset();
        dark.reset();
        state = GameState.LIGHT_TURN;
    }

    /** Copies the state of {@param game} into this game object. **/
    public void copyFrom(Game game) {
        board.copyFrom(game.board);
        light.copyFrom(game.light);
        dark.copyFrom(game.dark);
        state = game.state;
    }

    /** Set the current state of the game. **/
    public void setState(GameState state) {
        this.state = state;
    }

    /** @return the currently active player. **/
    public Player getActivePlayer() {
        return state.isLightActive ? light : dark;
    }

    /** @return the inactive player. **/
    public Player getInactivePlayer() {
        return state.isLightActive ? dark : light;
    }

    /** Populates {@param moves} with all possible moves from the current state. **/
    public void findPossibleMoves(int roll, MoveList moves) {
        board.findPossibleMoves(getActivePlayer(), roll, moves);
    }

    /** Advances this game to the next turn. **/
    public void nextTurn() {
        state = state.nextTurn();
    }

    /**
     * Simulates a whole game between the two agents given.
     */
    public void simulateGame(AgentStats lightStats, AgentStats darkStats, Agent light, Agent dark) {
        MoveList legalMoves = new MoveList();

        while (!state.finished) {
            Agent activeAgent;
            AgentStats activeAgentStats;
            if (state.isLightActive) {
                activeAgent = light;
                activeAgentStats = lightStats;
            } else {
                activeAgent = dark;
                activeAgentStats = darkStats;
            }

            int roll = Roll.next();
            findPossibleMoves(roll, legalMoves);
            if (legalMoves.count == 0) {
                activeAgentStats.recordMoveMade(0, 0);
                nextTurn();
                continue;
            }

            long start = System.nanoTime();
            int move = activeAgent.determineMove(this, roll, legalMoves);
            activeAgentStats.recordMoveMade(System.nanoTime() - start, legalMoves.count);
            if (!legalMoves.contains(move))
                throw new IllegalStateException(activeAgent.name + " made an illegal move");

            performMove(move, roll);
        }
    }

    /**
     * Simulates a whole game between the two agents given.
     */
    public void simulateGame(Agent light, Agent dark) {
        MoveList legalMoves = new MoveList();

        while (!state.finished) {
            Agent activeAgent = (state.isLightActive ? light : dark);

            int roll = Roll.next();
            findPossibleMoves(roll, legalMoves);
            if (legalMoves.count == 0) {
                nextTurn();
                continue;
            }

            int move = activeAgent.determineMove(this, roll, legalMoves);
            if (!legalMoves.contains(move))
                throw new IllegalStateException(activeAgent.name + " made an illegal move");

            performMove(move, roll);
        }
    }

    /**
     * Simulates a single move between the two given agents.
     */
    public void simulateOneMove(Agent light, Agent dark) {
        MoveList legalMoves = new MoveList();
        Agent activeAgent = (state.isLightActive ? light : dark);

        int roll = Roll.next();
        findPossibleMoves(roll, legalMoves);
        if (legalMoves.count == 0) {
            nextTurn();
            return;
        }

        int move = activeAgent.determineMove(this, roll, legalMoves);
        if (!legalMoves.contains(move))
            throw new IllegalStateException(activeAgent.name + " made an illegal move");

        performMove(move, roll);
    }

    /**
     * Move the tile at the given position.
     *
     * This method does _not_ check if the move is legal.
     */
    public void performMove(int pos, int roll) {
        Player player = getActivePlayer();
        int dest = player.path.posToDestByRoll[roll][pos];
        if (dest == -1)
            throw new IllegalArgumentException("Invalid move");

        int destTile = board.get(dest);
        board.set(pos, Tile.EMPTY);

        // Add score when the player gets their pieces to the end.
        if (player.path.isEnd(dest)) {
            player.score += 1;
            if (player.score >= Player.MAX_TILES) {
                state = state.won();
                return;
            }
        } else {
            board.set(dest, player.tile);
        }

        // Add the tile back that the player took.
        if (destTile != Tile.EMPTY) {
            getInactivePlayer().tiles += 1;
        }

        // If a start tile was moved, subtract it from the player's tiles.
        if (player.path.isStart(pos)) {
            player.tiles -= 1;
        }

        // If the destination tile isn't a rosette, advance to the next move.
        if (!Tile.isRosette(dest)) {
            nextTurn();
        }
    }

    @Override
    public String toString() {
        return "Game(" + state + ", " + light +", " + dark + ", \"" + board + "\")";
    }

    /** @return the state of this game encoded into a long. **/
    public long toLong() {
        int bit = 0;
        long value = 0;
        for (int pos = 0; pos <= Pos.MAX; ++pos) {
            if (!Tile.isOnBoard(pos))
                continue;

            int contents = board.get(pos);

            boolean light = Path.LIGHT.contains(pos);
            boolean dark = Path.DARK.contains(pos);
            if (light && dark) {
                value <<= 2;
                value |= contents & 0b11;
                bit += 2;
            } else {
                value <<= 1;
                value |= (contents != 0 ? 1 : 0);
                bit += 1;
            }
        }

        // Add in the tiles.
        value <<= 3;
        value |= light.tiles & 0b111;
        value <<= 3;
        value |= dark.tiles & 0b111;
        bit += 6;

        if (bit > 64)
            throw new RuntimeException("Something went wrong....");

        return value;
    }
}
