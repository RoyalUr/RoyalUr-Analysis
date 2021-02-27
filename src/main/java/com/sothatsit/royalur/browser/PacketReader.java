package com.sothatsit.royalur.browser;

import java.util.UUID;

/**
 * A helper to read values from a packet.
 *
 * @author Paddy Lamont
 */
public class PacketReader {

    private final String data;
    private int index;

    /** Construct a packet to read in the packet data {@param data}. **/
    public PacketReader(String data) {
        this.data = data;
        this.index = 0;
    }

    /** @return A copy of this packet, with its position reset to the beginning of the packet. **/
    public PacketReader copy() {
        return new PacketReader(data);
    }

    /** @return The next character in this packet. **/
    public char nextChar() {
        return data.charAt(index++);
    }

    /** @return The next String of length {@param length} in this packet. **/
    public String nextString(int length) {
        int from = index;
        index += length;
        return data.substring(from, index);
    }

    /** @return The next digit in this packet. **/
    public int nextDigit() {
        return nextInt(1);
    }

    /** @return The next integer of length {@param digits} digits in this packet. **/
    public int nextInt(int digits) {
        String string = nextString(digits);
        return Integer.parseInt(string);
    }

    /** @return The next String with length encoded in the next two digits. **/
    public String nextVarString() {
        return nextVarString(2);
    }

    /** @return The next String with length encoded in the next {@param lengthDigits} digits. **/
    public String nextVarString(int lengthDigits) {
        int length = nextInt(lengthDigits);
        return nextString(length);
    }

    /** @return The next UUID in the packet. **/
    public UUID nextUUID() {
        String string = nextString(36);
        return UUID.fromString(string);
    }
}