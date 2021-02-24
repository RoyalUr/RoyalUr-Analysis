package com.sothatsit.royalur.simulation;

/**
 * Represents a game board for The Royal Game of Ur.
 *
 * @author Paddy Lamont
 */
public final class Board {

    /**
     * We store the whole state of the board in 48 bits:
     *
     *  11 11 11
     *  11 11 11
     *  11 11 11
     *  11 11 11
     *  -- 11 --
     *  -- 11 --
     *  11 11 11
     *  11 11 11
     *
     *  We do this by using 2 bits to represent each tile.
     *  00 - empty
     *  01 - light tile
     *  10 - dark tile
     */
    private long state;

    /** @return the tile at the given position. **/
    public int get(int x, int y) {
        return get(Pos.pack(x, y));
    }

    /** @return the tile at the given packed position. **/
    public int get(int pos) {
        return (int) ((state >> (2 * pos)) & 0b11);
    }

    /** Set the tile at the given position to {@param tile}. **/
    public void set(int x, int y, int tile) {
        set(Pos.pack(x, y), tile);
    }

    /** Set the tile at the given packed position to {@param tile}. **/
    public void set(int pos, int tile) {
        int shift = pos * 2;
        state = (state & ~(0b11L << shift) | ((long) tile << shift));
    }

    /** Clear all of the tiles on the board. **/
    public void clear() {
        state = 0;
    }

    /** Clears and adds all the possible moves to {@param moves}. **/
    public void findPossibleMoves(Player player, int roll, MoveList moves) {
        if (roll == 0) {
            moves.count = 0;
            return;
        }

        int playerTile = player.tile;
        Path path = player.path;
        int[] indexToPos = path.indexToPos;
        int[] posToDest = path.posToDestByRoll[roll];
        int[] outPositions = moves.positions;
        int[] outDestinations = moves.destinations;

        int count = 0;
        for (int index = 0; index < Path.LENGTH - roll; ++index) {
            int pos = indexToPos[index];
            int tile;
            if (index == 0) {
                tile = (player.tiles > 0 ? playerTile : Tile.EMPTY);
            } else {
                tile = get(pos);
            }
            if (tile != playerTile)
                continue;

            int dest = posToDest[pos];
            int destTile = get(dest);
            if (destTile == playerTile || (pos == Tile.MIDDLE_ROSETTE && destTile != Tile.EMPTY))
                continue;

            outPositions[count] = pos;
            outDestinations[count] = dest;
            count += 1;
        }
        moves.count = count;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(state);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Board))
            return false;

        return state == ((Board) obj).state;
    }

    /** @return the game board encoded into a String using spaces as a delimiter. **/
    @Override
    public String toString() {
        return toString(false);
    }

    /** @return the game board encoded into a String. **/
    public String toString(boolean useNewlineDelimiter) {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < 8; ++y) {
            if (y != 0) {
                builder.append(useNewlineDelimiter ? "\n" : " ");
            }
            for (int x = 0; x < 3; ++x) {
                int tile = get(x, y);
                if (tile == Tile.LIGHT) {
                    builder.append("L");
                } else if (tile == Tile.DARK) {
                    builder.append("D");
                } else {
                    builder.append(Tile.isOnBoard(Pos.pack(x, y)) ? "-" : ".");
                }
            }
        }
        return builder.toString();
    }

    /** @return a game board decoded from {@param encoded}. **/
    public static Board fromString(String encoded) {
        if (encoded.length() != 4 * 8 - 1)
            throw new IllegalArgumentException("Invalid encoded board");

        Board board = new Board();

        int index = 0;
        for (int y = 0; y < 8; ++y) {
            if (y != 0) {
                char ch = encoded.charAt(index++);
                if (ch != ' ' && ch != '\n')
                    throw new IllegalArgumentException("Expected space or newline at " + index);
            }
            for (int x = 0; x < 3; ++x) {
                char ch = encoded.charAt(index++);
                if (ch == 'L') {
                    board.set(x, y, Tile.LIGHT);
                } else if (ch == 'D') {
                    board.set(x, y, Tile.DARK);
                } else if (ch != (Tile.isOnBoard(Pos.pack(x, y)) ? '-' : '.')) {
                    throw new IllegalArgumentException("Expected dash or dot at " + index);
                }
            }
        }
        return board;
    }
}
