package com.sothatsit.royalur;

import com.sothatsit.royalur.simulation.Path;
import com.sothatsit.royalur.simulation.Pos;
import com.sothatsit.royalur.simulation.Roll;
import org.junit.Assert;
import org.junit.Test;

public class PathTest {

    @Test
    public void testLightPath() {
        Pos[] path = Path.LIGHT.path;

        Assert.assertEquals(new Pos(0, 4), path[0]);
        Assert.assertEquals(new Pos(0, 3), path[1]);
        Assert.assertEquals(new Pos(0, 2), path[2]);
        Assert.assertEquals(new Pos(0, 1), path[3]);
        Assert.assertEquals(new Pos(0, 0), path[4]);

        Assert.assertEquals(new Pos(1, 0), path[5]);
        Assert.assertEquals(new Pos(1, 1), path[6]);
        Assert.assertEquals(new Pos(1, 2), path[7]);
        Assert.assertEquals(new Pos(1, 3), path[8]);
        Assert.assertEquals(new Pos(1, 4), path[9]);
        Assert.assertEquals(new Pos(1, 5), path[10]);
        Assert.assertEquals(new Pos(1, 6), path[11]);
        Assert.assertEquals(new Pos(1, 7), path[12]);

        Assert.assertEquals(new Pos(0, 7), path[13]);
        Assert.assertEquals(new Pos(0, 6), path[14]);
        Assert.assertEquals(new Pos(0, 5), path[15]);
    }

    @Test
    public void testDarkPath() {
        Pos[] path = Path.DARK.path;
        
        Assert.assertEquals(new Pos(2, 4), path[0]);
        Assert.assertEquals(new Pos(2, 3), path[1]);
        Assert.assertEquals(new Pos(2, 2), path[2]);
        Assert.assertEquals(new Pos(2, 1), path[3]);
        Assert.assertEquals(new Pos(2, 0), path[4]);

        Assert.assertEquals(new Pos(1, 0), path[5]);
        Assert.assertEquals(new Pos(1, 1), path[6]);
        Assert.assertEquals(new Pos(1, 2), path[7]);
        Assert.assertEquals(new Pos(1, 3), path[8]);
        Assert.assertEquals(new Pos(1, 4), path[9]);
        Assert.assertEquals(new Pos(1, 5), path[10]);
        Assert.assertEquals(new Pos(1, 6), path[11]);
        Assert.assertEquals(new Pos(1, 7), path[12]);

        Assert.assertEquals(new Pos(2, 7), path[13]);
        Assert.assertEquals(new Pos(2, 6), path[14]);
        Assert.assertEquals(new Pos(2, 5), path[15]);
    }

    @Test
    public void testPackedLightPath() {
        int[] path = Path.LIGHT.indexToPos;

        Assert.assertEquals(Pos.pack(0, 4), path[0]);
        Assert.assertEquals(Pos.pack(0, 3), path[1]);
        Assert.assertEquals(Pos.pack(0, 2), path[2]);
        Assert.assertEquals(Pos.pack(0, 1), path[3]);
        Assert.assertEquals(Pos.pack(0, 0), path[4]);

        Assert.assertEquals(Pos.pack(1, 0), path[5]);
        Assert.assertEquals(Pos.pack(1, 1), path[6]);
        Assert.assertEquals(Pos.pack(1, 2), path[7]);
        Assert.assertEquals(Pos.pack(1, 3), path[8]);
        Assert.assertEquals(Pos.pack(1, 4), path[9]);
        Assert.assertEquals(Pos.pack(1, 5), path[10]);
        Assert.assertEquals(Pos.pack(1, 6), path[11]);
        Assert.assertEquals(Pos.pack(1, 7), path[12]);

        Assert.assertEquals(Pos.pack(0, 7), path[13]);
        Assert.assertEquals(Pos.pack(0, 6), path[14]);
        Assert.assertEquals(Pos.pack(0, 5), path[15]);
    }

    @Test
    public void testPackedDarkPath() {
        int[] path = Path.DARK.indexToPos;

        Assert.assertEquals(Pos.pack(2, 4), path[0]);
        Assert.assertEquals(Pos.pack(2, 3), path[1]);
        Assert.assertEquals(Pos.pack(2, 2), path[2]);
        Assert.assertEquals(Pos.pack(2, 1), path[3]);
        Assert.assertEquals(Pos.pack(2, 0), path[4]);

        Assert.assertEquals(Pos.pack(1, 0), path[5]);
        Assert.assertEquals(Pos.pack(1, 1), path[6]);
        Assert.assertEquals(Pos.pack(1, 2), path[7]);
        Assert.assertEquals(Pos.pack(1, 3), path[8]);
        Assert.assertEquals(Pos.pack(1, 4), path[9]);
        Assert.assertEquals(Pos.pack(1, 5), path[10]);
        Assert.assertEquals(Pos.pack(1, 6), path[11]);
        Assert.assertEquals(Pos.pack(1, 7), path[12]);

        Assert.assertEquals(Pos.pack(2, 7), path[13]);
        Assert.assertEquals(Pos.pack(2, 6), path[14]);
        Assert.assertEquals(Pos.pack(2, 5), path[15]);
    }

    @Test
    public void testPackedToLightIndex() {
        int[] toIndex = Path.LIGHT.posToIndex;

        Assert.assertEquals(0, toIndex[Pos.pack(0, 4)]);
        Assert.assertEquals(1, toIndex[Pos.pack(0, 3)]);
        Assert.assertEquals(2, toIndex[Pos.pack(0, 2)]);
        Assert.assertEquals(3, toIndex[Pos.pack(0, 1)]);
        Assert.assertEquals(4, toIndex[Pos.pack(0, 0)]);

        Assert.assertEquals(-1, toIndex[Pos.pack(2, 4)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(2, 3)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(2, 2)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(2, 1)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(2, 0)]);

        Assert.assertEquals(5, toIndex[Pos.pack(1, 0)]);
        Assert.assertEquals(6, toIndex[Pos.pack(1, 1)]);
        Assert.assertEquals(7, toIndex[Pos.pack(1, 2)]);
        Assert.assertEquals(8, toIndex[Pos.pack(1, 3)]);
        Assert.assertEquals(9, toIndex[Pos.pack(1, 4)]);
        Assert.assertEquals(10, toIndex[Pos.pack(1, 5)]);
        Assert.assertEquals(11, toIndex[Pos.pack(1, 6)]);
        Assert.assertEquals(12, toIndex[Pos.pack(1, 7)]);

        Assert.assertEquals(13, toIndex[Pos.pack(0, 7)]);
        Assert.assertEquals(14, toIndex[Pos.pack(0, 6)]);
        Assert.assertEquals(15, toIndex[Pos.pack(0, 5)]);

        Assert.assertEquals(-1, toIndex[Pos.pack(2, 7)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(2, 6)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(2, 5)]);
    }

    @Test
    public void testPackedToDarkIndex() {
        int[] toIndex = Path.DARK.posToIndex;

        Assert.assertEquals(0, toIndex[Pos.pack(2, 4)]);
        Assert.assertEquals(1, toIndex[Pos.pack(2, 3)]);
        Assert.assertEquals(2, toIndex[Pos.pack(2, 2)]);
        Assert.assertEquals(3, toIndex[Pos.pack(2, 1)]);
        Assert.assertEquals(4 , toIndex[Pos.pack(2, 0)]);

        Assert.assertEquals(-1, toIndex[Pos.pack(0, 4)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(0, 3)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(0, 2)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(0, 1)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(0, 0)]);

        Assert.assertEquals(5, toIndex[Pos.pack(1, 0)]);
        Assert.assertEquals(6, toIndex[Pos.pack(1, 1)]);
        Assert.assertEquals(7, toIndex[Pos.pack(1, 2)]);
        Assert.assertEquals(8, toIndex[Pos.pack(1, 3)]);
        Assert.assertEquals(9, toIndex[Pos.pack(1, 4)]);
        Assert.assertEquals(10, toIndex[Pos.pack(1, 5)]);
        Assert.assertEquals(11, toIndex[Pos.pack(1, 6)]);
        Assert.assertEquals(12, toIndex[Pos.pack(1, 7)]);

        Assert.assertEquals(13, toIndex[Pos.pack(2, 7)]);
        Assert.assertEquals(14, toIndex[Pos.pack(2, 6)]);
        Assert.assertEquals(15, toIndex[Pos.pack(2, 5)]);

        Assert.assertEquals(-1, toIndex[Pos.pack(0, 7)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(0, 6)]);
        Assert.assertEquals(-1, toIndex[Pos.pack(0, 5)]);
    }

    @Test
    public void testPosToDestByRoll() {
        for (Path path : new Path[] {Path.LIGHT, Path.DARK}) {
            for (int roll = 0; roll <= Roll.MAX; ++roll) {
                for (int pos = 0; pos <= Pos.MAX; ++pos) {
                    // Manually determine the destination position, and check that it matches.
                    int pathIndex = path.posToIndex[pos];
                    int destIndex = pathIndex + roll;
                    int destPos = (pathIndex >= 0 && destIndex < Path.LENGTH ? path.indexToPos[destIndex] : -1);
                    Assert.assertEquals(destPos, path.posToDestByRoll[roll][pos]);
                }
            }
        }
    }
}
