package share.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.binary.Binary;
import share.progressive.Few;
import share.progressive.Lot;
import share.refmethod.Eqv;

import java.util.Random;

import static share.progressive.Pr.*;


public class Ut {

@SuppressWarnings("SpellCheckingInspection")
private static final char[] CHARS_SET =
        "_-ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

public static @NotNull String randomString(int length) {
    Random rd = new Random();
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < length; i = i + 1) {
        int index = rd.nextInt(CHARS_SET.length);
        char c = CHARS_SET[index];
        str.append(c);
    }
    return str.toString();
}


//region Lot
public static boolean isBelong(Object datum, Lot lt) {
    Lot moo = lt;
    while (!isNull(moo) &&
           !eq(datum, car(moo))) {
        moo = cdr(moo);
    }
    return !isNull(moo);
}

public static boolean isBelong(Eqv pred, Object datum, Lot lt) {
    Lot moo = lt;
    while (!isNull(moo) &&
           !pred.apply(datum, car(moo))) {
        moo = cdr(moo);
    }
    return !isNull(moo);
}

public static Lot removeDup(Lot lt) {
    Lot moo = lt;
    Lot xoo = lot();
    while (!isNull(moo)) {
        if (!isBelong(car(moo), cdr(moo))) {
            xoo = cons(car(moo), xoo);
        }
        moo = cdr(moo);
    }
    return reverse(xoo);
}
//endregion


//region Few
public static int fewIndex(Eqv pred, Object datum, Few fw) {
    int sz = length(fw);
    int i = 0;
    while (i < sz &&
           !pred.apply(datum, fewRef(fw, i))) {
        i = i + 1;
    }
    if (i == sz) {
        return -1;
    } else {
        return i;
    }
}
//endregion


//region Checksum
private static final int FLETCHER_MOD = 65535;
private static final int ADLER_MOD = 65521;

@Contract(pure = true)
public static int fletcher32(byte @NotNull [] bs) {
    int len = bs.length;
    int block_sz = 360;       // 0 < n and n * (n+1)/2 * (2^16 - 1) < (2^32 - 1)

    int sum0 = 0;
    int sum1 = 0;
    int i = 0;
    int bound = block_sz;
    while (bound < len) {
        for (; i < bound; i = i + 2) {
            sum0 = sum0 + (int) Binary.binToInteger(bs, i, i + 2, false);
            sum1 = sum1 + sum0;
        }
        sum0 = sum0 % FLETCHER_MOD;
        sum1 = sum1 % FLETCHER_MOD;
        bound = bound + block_sz;
    }

    bound = len & 0xFFFFFFFE;
    for (; i < bound; i = i + 2) {
        sum0 = sum0 + (int) Binary.binToInteger(bs, i, i + 2, false);
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


@Contract(pure = true)
public static int adler32(byte @NotNull [] bs) {
    int sum0 = 1;
    int sum1 = 0;
    for (byte b : bs) {
        sum0 = (sum0 + (b & 0xFF)) % ADLER_MOD;
        sum1 = (sum1 + sum0) % ADLER_MOD;
    }
    return (sum1 << 16) | sum0;
}
//endregion
}
