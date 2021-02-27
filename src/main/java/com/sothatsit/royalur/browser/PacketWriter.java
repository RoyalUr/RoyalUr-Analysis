package com.sothatsit.royalur.browser;

import java.util.UUID;

/**
 * A helper that allows the writing of packets.
 *
 * @author Paddy Lamont
 */
public class PacketWriter {

    private final StringBuilder dataBuilder;

    /** Create a packet of type {@param type}. **/
    public PacketWriter() {
        this.dataBuilder = new StringBuilder();
    }

    /** @return A String encoded version of this packet. **/
    @Override
    public String toString() {
        return dataBuilder.toString();
    }

    /** Write the boolean value {@param value} to the packet. **/
    public PacketWriter pushBool(boolean value) {
        dataBuilder.append(value ? "t" : "f");
        return this;
    }

    /**
     * Write the single digit {@param digit} to the packet.
     * @param digit must be in the range 0 -> 9 inclusive.
     */
    public PacketWriter pushDigit(int digit) {
        dataBuilder.append(digit);
        return this;
    }

    /**
     * Write the variable length String {@param value} to the packet, where
     * the length of the string can fall anywhere between 0 and 99 inclusive.
     */
    public PacketWriter pushVarString(String value) {
        return pushVarString(value, 2);
    }

    /**
     * Write the variable length String {@param value} to the packet, where the
     * length of the string can have a maximum of {@param lengthDigits} digits.
     */
    public PacketWriter pushVarString(String value, int lengthDigits) {
        pushInt(value.length(), lengthDigits);
        dataBuilder.append(value);
        return this;
    }

    /**
     * Write the integer {@param value} to the packet, encoding it using {@param digits} digits.
     */
    public PacketWriter pushInt(int value, int digits) {
        String string = Integer.toString(value);
        for(int index = string.length(); index < digits; ++index) {
            dataBuilder.append('0');
        }
        dataBuilder.append(string);
        return this;
    }

    /** Write the UUID {@param uuid} to the packet. **/
    public PacketWriter pushUUID(UUID uuid) {
        dataBuilder.append(uuid.toString());
        return this;
    }

    /** Write the String {@param value} to the packet. **/
    public PacketWriter pushRaw(String value) {
        dataBuilder.append(value);
        return this;
    }
}