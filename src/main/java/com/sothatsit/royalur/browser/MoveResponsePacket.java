package com.sothatsit.royalur.browser;

import com.sothatsit.royalur.simulation.Pos;

/**
 * A packet to respond back with the suggested move.
 *
 * @author Paddy Lamont
 */
public class MoveResponsePacket {

    public final int x;
    public final int y;

    public MoveResponsePacket(int move) {
        this(Pos.getX(move), Pos.getY(move));
    }

    public MoveResponsePacket(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        PacketWriter writer = new PacketWriter();
        writer.pushDigit(x);
        writer.pushDigit(y);
        return writer.toString();
    }
}
