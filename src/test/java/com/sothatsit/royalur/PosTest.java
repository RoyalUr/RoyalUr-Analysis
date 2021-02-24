package com.sothatsit.royalur;

import com.sothatsit.royalur.simulation.Pos;
import org.junit.Assert;
import org.junit.Test;

public class PosTest {

    private void assertPackAndUnpack(int x, int y) {
        int packed = Pos.pack(x, y);
        Assert.assertEquals(x, Pos.getX(packed), Pos.getY(packed));
    }

    @Test
    public void testPackAndUnpack() {
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 8; ++y) {
                assertPackAndUnpack(x, y);
            }
        }
    }

    @Test
    public void testPack() {
        Assert.assertEquals(0b00000, Pos.pack(0, 0));
        Assert.assertEquals(0b00001, Pos.pack(1, 0));
        Assert.assertEquals(0b00010, Pos.pack(2, 0));

        Assert.assertEquals(0b00100, Pos.pack(0, 1));
        Assert.assertEquals(0b00101, Pos.pack(1, 1));
        Assert.assertEquals(0b00110, Pos.pack(2, 1));

        Assert.assertEquals(0b01000, Pos.pack(0, 2));
        Assert.assertEquals(0b01001, Pos.pack(1, 2));
        Assert.assertEquals(0b01010, Pos.pack(2, 2));

        Assert.assertEquals(0b01100, Pos.pack(0, 3));
        Assert.assertEquals(0b01101, Pos.pack(1, 3));
        Assert.assertEquals(0b01110, Pos.pack(2, 3));

        Assert.assertEquals(0b10000, Pos.pack(0, 4));
        Assert.assertEquals(0b10001, Pos.pack(1, 4));
        Assert.assertEquals(0b10010, Pos.pack(2, 4));

        Assert.assertEquals(0b10100, Pos.pack(0, 5));
        Assert.assertEquals(0b10101, Pos.pack(1, 5));
        Assert.assertEquals(0b10110, Pos.pack(2, 5));

        Assert.assertEquals(0b11000, Pos.pack(0, 6));
        Assert.assertEquals(0b11001, Pos.pack(1, 6));
        Assert.assertEquals(0b11010, Pos.pack(2, 6));

        Assert.assertEquals(0b11100, Pos.pack(0, 7));
        Assert.assertEquals(0b11101, Pos.pack(1, 7));
        Assert.assertEquals(0b11110, Pos.pack(2, 7));
    }

    private void assertPosUnpacksTo(int pos, int x, int y) {
        Assert.assertEquals(x, Pos.getX(pos));
        Assert.assertEquals(y, Pos.getY(pos));
    }

    @Test
    public void testUnpack() {
        assertPosUnpacksTo(0b00000, 0, 0);
        assertPosUnpacksTo(0b00001, 1, 0);
        assertPosUnpacksTo(0b00010, 2, 0);

        assertPosUnpacksTo(0b00100, 0, 1);
        assertPosUnpacksTo(0b00101, 1, 1);
        assertPosUnpacksTo(0b00110, 2, 1);

        assertPosUnpacksTo(0b01000, 0, 2);
        assertPosUnpacksTo(0b01001, 1, 2);
        assertPosUnpacksTo(0b01010, 2, 2);

        assertPosUnpacksTo(0b01100, 0, 3);
        assertPosUnpacksTo(0b01101, 1, 3);
        assertPosUnpacksTo(0b01110, 2, 3);

        assertPosUnpacksTo(0b10000, 0, 4);
        assertPosUnpacksTo(0b10001, 1, 4);
        assertPosUnpacksTo(0b10010, 2, 4);

        assertPosUnpacksTo(0b10100, 0, 5);
        assertPosUnpacksTo(0b10101, 1, 5);
        assertPosUnpacksTo(0b10110, 2, 5);

        assertPosUnpacksTo(0b11000, 0, 6);
        assertPosUnpacksTo(0b11001, 1, 6);
        assertPosUnpacksTo(0b11010, 2, 6);

        assertPosUnpacksTo(0b11100, 0, 7);
        assertPosUnpacksTo(0b11101, 1, 7);
        assertPosUnpacksTo(0b11110, 2, 7);
    }
}
