package reduce.share;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


public class Share {

//region Binary
public static byte @NotNull [] integerToBinary(long n, int length, boolean little_endian) {
    byte[] bin = new byte[length];
    if (little_endian) {
        for (int i = 0; i < length; i = i + 1) {
            bin[i] = (byte) n;
            n = n >>> 8;
        }
    } else {
        for (int i = length - 1; i >= 0; i = i - 1) {
            bin[i] = (byte) n;
            n = n >>> 8;
        }
    }
    return bin;
}

public static long binaryToInteger(byte[] bin, int start, int bound, boolean little_endian) {
    long n = 0;
    if (little_endian) {
        for (int i = bound - 1; i >= start; i = i - 1) {
            n = n << 8;
            n = n | (bin[i] & 0xFF);
        }
    } else {
        for (int i = start; i < bound; i = i + 1) {
            n = n << 8;
            n = n | (bin[i] & 0xFF);
        }
    }
    return n;
}

public static byte @NotNull [] codeSize(long size) {
    byte[] bs1 = integerToBinary(size, 8, false);
    bs1 = trim(bs1);
    byte[] bs2 = new byte[bs1.length + 1];
    bs2[0] = (byte) bs1.length;
    System.arraycopy(bs1, 0, bs2, 1, bs1.length);
    return bs2;
}

@Contract(pure = true)
public static long decodeSize(byte @NotNull [] bin, int start) {
    long sz = 0;
    int bound = start + 1 + (bin[start] & 0xFF);
    for (int i = start + 1; i < bound; i += 1) {
        sz = (sz << 4) + (bin[i] & 0xFF);
    }
    return sz;
}
//endregion


public static byte @NotNull [] trim(byte @NotNull [] big) {
    int bound = big.length - 1;
    if (big[0] == 0) {
        int k = 0;
        while (k < bound && big[k] == 0) {
            k += 1;
        }
        if (big[k] < 0) {
            k -= 1;
        }
        byte[] moo = new byte[big.length - k];
        System.arraycopy(big, k, moo, 0, big.length - k);
        return moo;
    } else if (big[0] == -1) {
        int k = 0;
        while (k < bound && big[k] == -1) {
            k += 1;
        }
        if (big[k] >= 0) {
            k -= 1;
        }
        byte[] moo = new byte[big.length - k];
        System.arraycopy(big, k, moo, 0, big.length - k);
        return moo;
    } else {
        return big;
    }
}
}
