package com.sothatsit.royalur;

import com.sothatsit.royalur.simulation.*;
import org.junit.Assert;
import org.junit.Test;

public class BoardTest {

    public static void assertBoardEquals(Board board, String expected) {
        Assert.assertEquals(expected, board.toString());
    }

    @Test
    public void testDefaultsToZero() {
        Board board = new Board();
        for (int pos = 0; pos < 24; ++pos) {
            Assert.assertEquals(0, board.get(pos));
        }
        assertBoardEquals(board, "--- --- --- --- .-. .-. --- ---");
    }

    @Test
    public void testSetAndGet() {
        Board board = new Board();

        // Test setting every position to every possible state.
        for (int tile = 0; tile < 3; ++tile) {
            for (int pos = 0; pos <= Pos.MAX; ++pos) {
                board.set(pos, tile);
                Assert.assertEquals(tile, board.get(pos));
            }
        }
        for (int pos = 0; pos <= Pos.MAX; ++pos) {
            for (int tile = 0; tile < 3; ++tile) {
                board.set(pos, tile);
                Assert.assertEquals(tile, board.get(pos));
            }
        }

        // Test setting each position to different values.
        for (int offset = 0; offset < 3; ++offset) {
            for (int pos = 0; pos <= Pos.MAX; ++pos) {
                int tile = (pos + offset) % 3;
                board.set(pos, tile);
                Assert.assertEquals(tile, board.get(pos));
            }
        }
    }

    @Test
    public void testFindPossibleMoves() {
        Player lightPlayer = Player.createLight();
        Player darkPlayer = Player.createDark();
        Board board = new Board();
        MoveList moves = new MoveList();

        // Test moves on an empty board with no tiles.
        lightPlayer.tiles = 0;
        darkPlayer.tiles = 0;
        for (int roll = 0; roll <= Roll.MAX; ++roll) {
            board.findPossibleMoves(lightPlayer, roll, moves);
            Assert.assertEquals(0, moves.count);
            board.findPossibleMoves(darkPlayer, roll, moves);
            Assert.assertEquals(0, moves.count);
        }

        // Test moves on an empty board with tiles.
        lightPlayer.tiles = 1;
        darkPlayer.tiles = 1;
        for (int roll = 1; roll <= Roll.MAX; ++roll) {
            board.findPossibleMoves(lightPlayer, roll, moves);
            Assert.assertEquals(1, moves.count);
            Assert.assertEquals(Pos.pack(0, 4), moves.positions[0]);
            Assert.assertEquals(Path.LIGHT.indexToPos[roll], moves.destinations[0]);

            board.findPossibleMoves(darkPlayer, roll, moves);
            Assert.assertEquals(1, moves.count);
            Assert.assertEquals(Pos.pack(2, 4), moves.positions[0]);
            Assert.assertEquals(Path.DARK.indexToPos[roll], moves.destinations[0]);
        }

        // Test moves on a board with one tile next to the end, and no tiles.
        board.clear();
        board.set(Pos.pack(0, 6), Tile.LIGHT);
        lightPlayer.tiles = 0;
        for (int roll = 0; roll <= Roll.MAX; ++roll) {
            for (int darkTiles = 0; darkTiles < (1 << 14); ++darkTiles) {
                // We set an assortment of dark tiles on the board to help test.
                for (int index = 1; index < Path.LENGTH - 1; ++index) {
                    boolean containsTile = ((darkTiles >> (index - 1)) & 0b1) != 0;
                    board.set(Path.DARK.indexToPos[index], (containsTile ? Tile.DARK : Tile.EMPTY));
                }

                board.findPossibleMoves(lightPlayer, roll, moves);
                if (roll == 1) {
                    Assert.assertEquals(1, moves.count);
                    Assert.assertEquals(Pos.pack(0, 6), moves.positions[0]);
                    Assert.assertEquals(Pos.pack(0, 5), moves.destinations[0]);
                } else {
                    Assert.assertEquals(0, moves.count);
                }
            }
        }
        board.clear();
        board.set(Pos.pack(2, 6), Tile.DARK);
        darkPlayer.tiles = 0;
        for (int roll = 0; roll <= Roll.MAX; ++roll) {
            for (int lightTiles = 0; lightTiles < (1 << 14); ++lightTiles) {
                // We set an assortment of dark tiles on the board to help test.
                for (int index = 1; index < Path.LENGTH - 1; ++index) {
                    boolean containsTile = ((lightTiles >> (index - 1)) & 0b1) != 0;
                    board.set(Path.LIGHT.indexToPos[index], (containsTile ? Tile.LIGHT : Tile.EMPTY));
                }

                board.findPossibleMoves(darkPlayer, roll, moves);
                if (roll == 1) {
                    Assert.assertEquals(1, moves.count);
                    Assert.assertEquals(Pos.pack(2, 6), moves.positions[0]);
                    Assert.assertEquals(Pos.pack(2, 5), moves.destinations[0]);
                } else {
                    Assert.assertEquals(0, moves.count);
                }
            }
        }

        // Test moving a tile from every position on the board.
        for (Player player : new Player[] {lightPlayer, darkPlayer}) {
            player.tiles = 0;
            for (int roll = 0; roll <= Roll.MAX; ++roll) {
                for (int pathIndex = 1; pathIndex < Path.LENGTH - 1; ++pathIndex) {
                    board.clear();
                    int from = player.path.indexToPos[pathIndex];
                    int dest = player.path.posToDestByRoll[roll][from];
                    board.set(from, player.tile);
                    board.findPossibleMoves(player, roll, moves);

                    if (roll == 0 || pathIndex + roll >= Path.LENGTH) {
                        Assert.assertEquals(0, moves.count);
                    } else {
                        Assert.assertEquals(1, moves.count);
                        Assert.assertEquals(from, moves.positions[0]);
                        Assert.assertEquals(dest, moves.destinations[0]);
                    }
                }
            }
        }

        // Test specific arrangements of tiles.
        for (Player player : new Player[] {lightPlayer, darkPlayer}) {
            board.clear();
            board.set(Pos.pack(player.homeCol, 0), player.tile);
            board.set(Pos.pack(1, 5), player.tile);
            board.set(Pos.pack(1, 6), player.tile);
            board.set(Pos.pack(1, 7), Tile.getOther(player.tile));

            player.tiles = 1;
            board.findPossibleMoves(player, 1, moves);
            Assert.assertEquals(3, moves.count);
            Assert.assertEquals(Pos.pack(player.homeCol, 4), moves.positions[0]);
            Assert.assertEquals(Pos.pack(player.homeCol, 3), moves.destinations[0]);

            Assert.assertEquals(Pos.pack(player.homeCol, 0), moves.positions[1]);
            Assert.assertEquals(Pos.pack(1, 0), moves.destinations[1]);

            Assert.assertEquals(Pos.pack(1, 6), moves.positions[2]);
            Assert.assertEquals(Pos.pack(1, 7), moves.destinations[2]);


            // Test again with another tile after the start position.
            board.set(Pos.pack(player.homeCol, 3), player.tile);
            board.findPossibleMoves(player, 1, moves);
            Assert.assertEquals(Pos.pack(player.homeCol, 3), moves.positions[0]);
            Assert.assertEquals(Pos.pack(player.homeCol, 2), moves.destinations[0]);

            Assert.assertEquals(Pos.pack(player.homeCol, 0), moves.positions[1]);
            Assert.assertEquals(Pos.pack(1, 0), moves.destinations[1]);

            Assert.assertEquals(Pos.pack(1, 6), moves.positions[2]);
            Assert.assertEquals(Pos.pack(1, 7), moves.destinations[2]);
        }

        // Test that pieces on the rosette are safe.
        for (Player player : new Player[] {lightPlayer, darkPlayer}) {
            Player otherPlayer = (player == lightPlayer ? darkPlayer : lightPlayer);
            player.tiles = 0;

            for (int roll = 1; roll <= 4; ++roll) {
                board.clear();

                // Put a piece of the other player on the rosette.
                board.set(Tile.MIDDLE_ROSETTE, otherPlayer.tile);

                // Put a piece of the player before the rosette.
                int pos = player.path.indexToPos[8 - roll];
                board.set(pos, player.tile);

                // Make sure moving that piece onto the rosette is illegal.
                board.findPossibleMoves(player, roll, moves);
                Assert.assertEquals(0, moves.count);
            }
        }
    }

    @Test
    public void testFromAndToString() {
        String[] boards = {
            "--- --- --- --- .-. .-. --- ---",
            "--- --- L-- --- .-. .-. --- ---",
            "--- --- L-D --- .-. .-. --- ---",
            "L-- --- --D --- .-. .-. --- ---",
            "L-- L-- --D --- .-. .-. --- ---",
            "L-- L-- --D --D .-. .-. --- ---",
            "--- L-- --D -LD .-. .-. --- ---",
            "--- L-- --D --D .-. .-. --- -L-",
            "--- L-D --- --D .-. .-. --- -L-",
            "--- L-D --- --D .-. .-. --- ---",
            "--D L-- --- --D .-. .-. --- ---",
            "--- L-- --- -DD .-. .-. --- ---",
            "--- L-- --- --D .-. .-. --- -D-",
            "--- L-- L-- --D .-. .-. --- -D-",
            "--D L-- L-- --- .-. .-. --- -D-",
            "--- L-- L-- -D- .-. .-. --- -D-",
            "--- L-- L-- -D- .-. .-. --D ---",
            "--- L-- L-- --- .-. .-. -DD ---",
            "--- -L- L-- --- .-. .-. -DD ---",
            "--- -L- L-- --- .-. .-. -D- ---",
            "--- --- L-- -L- .-. .-. -D- ---",
            "--- --- L-- --- .-. .L. -D- ---",
            "--- --- L-- --- .-. .L. --- ---"
        };
        for (String boardString : boards) {
            Board board = Board.fromString(boardString);
            Assert.assertEquals(boardString, board.toString());
        }
    }
}
