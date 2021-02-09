package com.zyrox.util;

import java.math.BigInteger;
import java.util.Random;

public final class ByteBuffer {

    public byte[] buffer;
    public int position;
    public int bitPosition;
    private static final int[] bitMasks = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, '\uffff', 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, 2147483647, -1};

    public static ByteBuffer allocate(int amount) {
        return new ByteBuffer(new byte[amount]);
    }

    public static ByteBuffer wrap(byte[] b) {
        return new ByteBuffer(b);
    }

    public ByteBuffer(byte[] data) {
        this.buffer = data;
        this.position = 0;
    }

    public void skip(int pos) {
        this.position += pos;
    }

    public int size() {
        return this.buffer.length;
    }

    public int getMediumInt() {
        this.position += 3;
        return ((this.buffer[this.position - 3] & 255) << 16) + ((this.buffer[this.position - 2] & 255) << 8) + (this.buffer[this.position - 1] & 255);
    }

    public void putBoolean(boolean bool) {
        this.buffer[this.position++] = (byte)(bool?1:0);
    }

    public void putByte(int val) {
        this.buffer[this.position++] = (byte)val;
    }

    public String getJagString() {
        int off = this.position;

        while(this.buffer[this.position++] != 0) {
            ;
        }

        return new String(this.buffer, off, this.position - off - 1);
    }

    public void putShort(int val) {
        this.buffer[this.position++] = (byte)(val >> 8);
        this.buffer[this.position++] = (byte)val;
    }

    public void putMediumInt(int val) {
        this.buffer[this.position++] = (byte)(val >> 16);
        this.buffer[this.position++] = (byte)(val >> 8);
        this.buffer[this.position++] = (byte)val;
    }

    public void putInt(int val) {
        this.buffer[this.position++] = (byte)(val >> 24);
        this.buffer[this.position++] = (byte)(val >> 16);
        this.buffer[this.position++] = (byte)(val >> 8);
        this.buffer[this.position++] = (byte)val;
    }

    public void putLEInt(int val) {
        this.buffer[this.position++] = (byte)val;
        this.buffer[this.position++] = (byte)(val >> 8);
        this.buffer[this.position++] = (byte)(val >> 16);
        this.buffer[this.position++] = (byte)(val >> 24);
    }

    public void putLong(long val) {
        this.buffer[this.position++] = (byte)((int)(val >> 56));
        this.buffer[this.position++] = (byte)((int)(val >> 48));
        this.buffer[this.position++] = (byte)((int)(val >> 40));
        this.buffer[this.position++] = (byte)((int)(val >> 32));
        this.buffer[this.position++] = (byte)((int)(val >> 24));
        this.buffer[this.position++] = (byte)((int)(val >> 16));
        this.buffer[this.position++] = (byte)((int)(val >> 8));
        this.buffer[this.position++] = (byte)((int)val);
    }

    public void putLine(String string) {
        System.arraycopy(string.getBytes(), 0, this.buffer, this.position, string.length());
        this.position += string.length();
        this.buffer[this.position++] = 10;
    }

    public void putBytes(byte[] data, int length, int off) {
        for(int i = off; i < off + length; ++i) {
            this.buffer[this.position++] = data[i];
        }

    }

    public void putSizeByte(int val) {
        this.buffer[this.position - val - 1] = (byte)val;
    }

    public int getUnsignedByte() {
        return this.buffer[this.position++] & 255;
    }

    public byte getByte() {
        return this.buffer[this.position++];
    }

    public boolean getBoolean() {
        return this.getByte() == 1;
    }

    public int getUnsignedShort() {
        this.position += 2;
        return ((this.buffer[this.position - 2] & 255) << 8) + (this.buffer[this.position - 1] & 255);
    }

    public int getShort() {
        this.position += 2;
        int val = ((this.buffer[this.position - 2] & 255) << 8) + (this.buffer[this.position - 1] & 255);
        if(val > 32767) {
            val -= 65536;
        }

        return (short)val;
    }

    public int getShort2() {
        this.position += 2;
        int val = ((this.buffer[this.position - 2] & 255) << 8) + (this.buffer[this.position - 1] & 255);
        if(val > '\uea60') {
            val += -65536;
        }

        return val;
    }

    public int getInt() {
        this.position += 4;
        return ((this.buffer[this.position - 4] & 255) << 24) + ((this.buffer[this.position - 3] & 255) << 16) + ((this.buffer[this.position - 2] & 255) << 8) + (this.buffer[this.position - 1] & 255);
    }

    public long getLong() {
        long l = (long)this.getInt() & 4294967295L;
        long l1 = (long)this.getInt() & 4294967295L;
        return (l << 32) + l1;
    }

    public void putSmartLong(long val) {
        if(val < 0L) {
            val = -1L;
        }

        if(val < 63L) {
            this.putByte((int)val + 1);
        } else if(val < 16383L) {
            this.putShort((int)val | 16384);
        } else if(val < 1073741823L) {
            this.putInt((int)val | -2147483648);
        } else {
            if(val > 4611686018427387903L) {
                val = 4611686018427387903L;
            }

            this.putLong(val | -4611686018427387904L);
        }

    }

    public long getSmartLong() {
        int size = (this.buffer[this.position] & 192) >>> 6;
        return size == 0?(long)(this.getUnsignedByte() - 1):(size == 1?(long)(this.getUnsignedShort() & 16383):(size == 2?(long)(this.getInt() & 1073741823):this.getLong() & 4611686018427387903L));
    }

    public String getLine() {
        int off = this.position;

        while(this.buffer[this.position++] != 10) {
            ;
        }

        return new String(this.buffer, off, this.position - off - 1);
    }

    public byte[] getLineBytes() {
        int i = this.position;

        while(this.buffer[this.position++] != 10) {
            ;
        }

        byte[] data = new byte[this.position - i - 1];
        System.arraycopy(this.buffer, i, data, i - i, this.position - 1 - i);
        return data;
    }

    public void getBytesFromBuffer(byte[] data, int off, int length) {
        for(int index = off; index < off + length; ++index) {
            data[index - off] = this.buffer[this.position++];
        }

    }

    public void getBytes(byte[] data, int off, int length) {
        System.arraycopy(this.buffer, this.position, data, off, length);
        this.position += length;
    }

    public void startBitAccess() {
        this.bitPosition = this.position * 8;
    }

    public int getBits(int off) {
        int offset = this.bitPosition >> 3;
        int max_bit_len = 8 - (this.bitPosition & 7);
        int dest = 0;

        for(this.bitPosition += off; off > max_bit_len; max_bit_len = 8) {
            dest += (this.buffer[offset++] & bitMasks[max_bit_len]) << off - max_bit_len;
            off -= max_bit_len;
        }

        if(off == max_bit_len) {
            dest += this.buffer[offset] & bitMasks[max_bit_len];
        } else {
            dest += this.buffer[offset] >> max_bit_len - off & bitMasks[off];
        }

        return dest;
    }

    public void endBitAccess() {
        this.position = (this.bitPosition + 7) / 8;
    }

    public void encodeRSA() {
        int off = this.position;
        this.position = 0;
        byte[] data = new byte[off];
        this.getBytes(data, 0, off);
        BigInteger biginteger2 = new BigInteger(data);
        byte[] dest = biginteger2.toByteArray();
        this.position = 0;
        this.putByte(dest.length);
        this.putBytes(dest, dest.length, 0);
    }

    public void encode(Random random) {
        int originalPosition = this.position;
        this.position = 0;
        byte[] data = new byte[originalPosition];
        System.arraycopy(this.buffer, 0, data, 0, originalPosition);

        for(int i = 0; i < originalPosition; ++i) {
            data[i] = (byte)(data[i] ^ random.nextInt());
        }

        this.putShort(data.length);
        this.putBytes(data, data.length, 0);
    }

    public void putNegativeByte(int val) {
        this.buffer[this.position++] = (byte)(-val);
    }

    public void putByteS(int val) {
        this.buffer[this.position++] = (byte)(128 - val);
    }

    public int getUnsignedByteA() {
        return this.buffer[this.position++] - 128 & 255;
    }

    public int getUnsignedNegativeByte() {
        return -this.buffer[this.position++] & 255;
    }

    public int getUnsignedByteS() {
        return 128 - this.buffer[this.position++] & 255;
    }

    public byte getNegativeByte() {
        return (byte)(-this.buffer[this.position++]);
    }

    public byte getByteS() {
        return (byte)(128 - this.buffer[this.position++]);
    }

    public void putLEShort(int val) {
        this.buffer[this.position++] = (byte)val;
        this.buffer[this.position++] = (byte)(val >> 8);
    }

    public void putShortA(int val) {
        this.buffer[this.position++] = (byte)(val >> 8);
        this.buffer[this.position++] = (byte)(val + 128);
    }

    public void putLEShortA(int val) {
        this.buffer[this.position++] = (byte)(val + 128);
        this.buffer[this.position++] = (byte)(val >> 8);
    }

    public int getUnsignedLEShort() {
        this.position += 2;
        return ((this.buffer[this.position - 1] & 255) << 8) + (this.buffer[this.position - 2] & 255);
    }

    public int getUnsignedLEShortA() {
        this.position += 2;
        return ((this.buffer[this.position - 2] & 255) << 8) + (this.buffer[this.position - 1] - 128 & 255);
    }

    public int getUnsignedShortA() {
        this.position += 2;
        return ((this.buffer[this.position - 1] & 255) << 8) + (this.buffer[this.position - 2] - 128 & 255);
    }

    public int getLEShort() {
        this.position += 2;
        int val = ((this.buffer[this.position - 1] & 255) << 8) + (this.buffer[this.position - 2] & 255);
        if(val > 32767) {
            val -= 65536;
        }

        return val;
    }

    public int getLEShortA() {
        this.position += 2;
        int val = ((this.buffer[this.position - 1] & 255) << 8) + (this.buffer[this.position - 2] - 128 & 255);
        if(val > 32767) {
            val -= 65536;
        }

        return val;
    }

    public int getInt_v2() {
        this.position += 4;
        return ((this.buffer[this.position - 2] & 255) << 24) + ((this.buffer[this.position - 1] & 255) << 16) + ((this.buffer[this.position - 4] & 255) << 8) + (this.buffer[this.position - 3] & 255);
    }

    public int getInt_v1() {
        this.position += 4;
        return ((this.buffer[this.position - 3] & 255) << 24) + ((this.buffer[this.position - 4] & 255) << 16) + ((this.buffer[this.position - 1] & 255) << 8) + (this.buffer[this.position - 2] & 255);
    }

    public void putBytesReverseA(byte[] data, int off, int len) {
        for(int index = off + len - 1; index >= off; --index) {
            this.buffer[this.position++] = (byte)(data[index] + 128);
        }

    }

    public void getBytesReverse(byte[] data, int off, int len) {
        for(int index = off + len - 1; index >= off; --index) {
            data[index] = this.buffer[this.position++];
        }

    }
}
