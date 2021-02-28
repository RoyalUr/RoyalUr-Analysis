package com.sothatsit.royalur;

import com.sothatsit.royalur.ai.RandomAgent;
import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.simulation.Board;
import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.GameState;
import com.sothatsit.royalur.simulation.Pos;
import org.junit.Assert;
import org.junit.Test;

public class GameTest {

    public static void assertGameState(
            Game game, GameState state,
            int lightTiles, int lightScore,
            int darkTiles, int darkScore) {

        Assert.assertEquals(state, game.state);
        Assert.assertEquals(lightTiles, game.light.tiles);
        Assert.assertEquals(lightScore, game.light.score);
        Assert.assertEquals(darkTiles, game.dark.tiles);
        Assert.assertEquals(darkScore, game.dark.score);
    }

    @Test
    public void testSimulateRandomGame() {
        Game game = new Game();
        Assert.assertEquals(GameState.LIGHT_TURN, game.state);

        game.simulateGame(new RandomAgent(), new RandomAgent());
        Assert.assertTrue(game.state.finished);
    }

    @Test
    public void testPerformMove() {
        Game game = new Game();
        Board board = game.board;
        assertGameState(game, GameState.LIGHT_TURN, 7, 0, 7, 0);
        BoardTest.assertBoardEquals(board, "--- --- --- --- .-. .-. --- ---");

        game.performMove(Pos.pack(0, 4), 2);
        assertGameState(game, GameState.DARK_TURN, 6, 0, 7, 0);
        BoardTest.assertBoardEquals(board, "--- --- L-- --- .-. .-. --- ---");

        game.performMove(Pos.pack(2, 4), 2);
        assertGameState(game, GameState.LIGHT_TURN, 6, 0, 6, 0);
        BoardTest.assertBoardEquals(board, "--- --- L-D --- .-. .-. --- ---");

        game.performMove(Pos.pack(0, 2), 2);
        assertGameState(game, GameState.LIGHT_TURN, 6, 0, 6, 0);
        BoardTest.assertBoardEquals(board, "L-- --- --D --- .-. .-. --- ---");

        game.performMove(Pos.pack(0, 4), 3);
        assertGameState(game, GameState.DARK_TURN, 5, 0, 6, 0);
        BoardTest.assertBoardEquals(board, "L-- L-- --D --- .-. .-. --- ---");

        game.performMove(Pos.pack(2, 4), 1);
        assertGameState(game, GameState.LIGHT_TURN, 5, 0, 5, 0);
        BoardTest.assertBoardEquals(board, "L-- L-- --D --D .-. .-. --- ---");

        game.performMove(Pos.pack(0, 0), 4);
        assertGameState(game, GameState.LIGHT_TURN, 5, 0, 5, 0);
        BoardTest.assertBoardEquals(board, "--- L-- --D -LD .-. .-. --- ---");

        game.performMove(Pos.pack(1, 3), 4);
        assertGameState(game, GameState.DARK_TURN, 5, 0, 5, 0);
        BoardTest.assertBoardEquals(board, "--- L-- --D --D .-. .-. --- -L-");

        game.performMove(Pos.pack(2, 2), 1);
        assertGameState(game, GameState.LIGHT_TURN, 5, 0, 5, 0);
        BoardTest.assertBoardEquals(board, "--- L-D --- --D .-. .-. --- -L-");

        game.performMove(Pos.pack(1, 7), 3);
        assertGameState(game, GameState.DARK_TURN, 5, 1, 5, 0);
        BoardTest.assertBoardEquals(board, "--- L-D --- --D .-. .-. --- ---");

        // Skip the middle of the game...
        game.dark.tiles = 0;
        game.dark.score = 5;

        game.performMove(Pos.pack(2, 1), 1);
        assertGameState(game, GameState.DARK_TURN, 5, 1, 0, 5);
        BoardTest.assertBoardEquals(board, "--D L-- --- --D .-. .-. --- ---");

        game.performMove(Pos.pack(2, 0), 4);
        assertGameState(game, GameState.DARK_TURN, 5, 1, 0, 5);
        BoardTest.assertBoardEquals(board, "--- L-- --- -DD .-. .-. --- ---");

        game.performMove(Pos.pack(1, 3), 4);
        assertGameState(game, GameState.LIGHT_TURN, 5, 1, 0, 5);
        BoardTest.assertBoardEquals(board, "--- L-- --- --D .-. .-. --- -D-");

        game.performMove(Pos.pack(0, 4), 2);
        assertGameState(game, GameState.DARK_TURN, 4, 1, 0, 5);
        BoardTest.assertBoardEquals(board, "--- L-- L-- --D .-. .-. --- -D-");

        game.performMove(Pos.pack(2, 3), 3);
        assertGameState(game, GameState.DARK_TURN, 4, 1, 0, 5);
        BoardTest.assertBoardEquals(board, "--D L-- L-- --- .-. .-. --- -D-");

        game.performMove(Pos.pack(2, 0), 4);
        assertGameState(game, GameState.DARK_TURN, 4, 1, 0, 5);
        BoardTest.assertBoardEquals(board, "--- L-- L-- -D- .-. .-. --- -D-");

        game.performMove(Pos.pack(1, 7), 2);
        assertGameState(game, GameState.DARK_TURN, 4, 1, 0, 5);
        BoardTest.assertBoardEquals(board, "--- L-- L-- -D- .-. .-. --D ---");

        game.performMove(Pos.pack(1, 3), 3);
        assertGameState(game, GameState.LIGHT_TURN, 4, 1, 0, 5);
        BoardTest.assertBoardEquals(board, "--- L-- L-- --- .-. .-. -DD ---");

        game.performMove(Pos.pack(0, 1), 3);
        assertGameState(game, GameState.DARK_TURN, 4, 1, 0, 5);
        BoardTest.assertBoardEquals(board, "--- -L- L-- --- .-. .-. -DD ---");

        game.performMove(Pos.pack(2, 6), 1);
        assertGameState(game, GameState.LIGHT_TURN, 4, 1, 0, 6);
        BoardTest.assertBoardEquals(board, "--- -L- L-- --- .-. .-. -D- ---");

        game.performMove(Pos.pack(1, 1), 2);
        assertGameState(game, GameState.LIGHT_TURN, 4, 1, 0, 6);
        BoardTest.assertBoardEquals(board, "--- --- L-- -L- .-. .-. -D- ---");

        game.performMove(Pos.pack(1, 3), 2);
        assertGameState(game, GameState.DARK_TURN, 4, 1, 0, 6);
        BoardTest.assertBoardEquals(board, "--- --- L-- --- .-. .L. -D- ---");

        game.performMove(Pos.pack(1, 6), 4);
        assertGameState(game, GameState.DARK_WON, 4, 1, 0, 7);
        BoardTest.assertBoardEquals(board, "--- --- L-- --- .-. .L. --- ---");
    }
}
