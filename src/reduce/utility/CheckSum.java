package reduce.utility;

import org.jetbrains.annotations.NotNull;


public class CheckSum {

private static final int FLETCHER_MOD = 65535;
private static final int ADLER_MOD = 65521;


public static int fletcher32(byte @NotNull [] bs) {
    int len = bs.length;
    int block_sz = 360;       // 0 < n and n * (n+1)/2 * (2^16 - 1) < (2^32 - 1)

    int sum0 = 0;
    int sum1 = 0;
    int i = 0;
    int bound = block_sz;
    while (bound < len) {
        for (; i < bound; i = i + 2) {
            sum0 = (int) (sum0 + Binary.binaryToInteger(bs, i, i + 2, false));
            sum1 = sum1 + sum0;
        }
        sum0 = sum0 % FLETCHER_MOD;
        sum1 = sum1 % FLETCHER_MOD;
        bound = bound + block_sz;
    }

    bound = len & 0xFFFFFFFE;
    for (; i < bound; i = i + 2) {
        sum0 = (int) (sum0 + Binary.binaryToInteger(bs, i, i + 2, false));
        sum1 = sum1 + sum0;
    }
    if ((len & 1) == 1) {
        sum0 = sum0 + ((bs[i] & 0xFF) << 8);
        sum1 = sum1 + sum0;
    }
    sum0 = sum0 % FLETCHER_MOD;
    sum1 = sum1 % FLETCHER_MOD;

    return (sum1 << 16) | sum0;
}


public static int adler32(byte @NotNull [] bs) {
    int sum0 = 1;
    int sum1 = 0;
    for (byte b : bs) {
        sum0 = (sum0 + (b & 0xFF)) % ADLER_MOD;
        sum1 = (sum1 + sum0) % ADLER_MOD;
    }
    return (sum1 << 16) | sum0;
}
}
