package share.binary;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.datetime.Date;
import share.datetime.Time;
import share.primitive.*;
import share.progressive.Few;
import share.progressive.Lot;

import java.nio.charset.StandardCharsets;

import static share.primitive.Pm.*;


public class Binary {

public static @NotNull Febyte codeInt(int n, boolean little_endian) {
    Febyte bin = makeFebyte(4);
    if (little_endian) {
        for (int i = 0; i < 4; i = i + 1) {
            byte by = (byte) (n >>> (i * 8));
            feSet(bin, i, by);
        }
    } else {
        for (int i = 0; i < 4; i = i + 1) {
            byte by = (byte) (n >>> (24 - i * 8));
            feSet(bin, i, by);
        }
    }
    return bin;
}


@Contract(pure = true)
public static byte @NotNull [] codeInt2(int n, boolean little_endian) {
    byte[] bin = new byte[4];
    int moo = n;
    if (little_endian) {
        for (int i = 0; i < 4; i = i + 1) {
            bin[i] = (byte) moo;
            moo = moo >>> 8;
        }
    } else {
        for (int i = 3; i != -1; i = i - 1) {
            bin[i] = (byte) moo;
            moo = moo >>> 8;
        }
    }
    return bin;
}


/**
 * @return a byte array that applied big-endian by default.
 */
public static @NotNull Febyte codeInt(int n) {
    return codeInt(n, false);
}

public static int deInt(@NotNull Febyte bin, int start, boolean little_endian) {
    int bound = start + 4;
    if (feLength(bin) < bound) {
        throw new IllegalArgumentException
              (String.format("range [%d %d) is out of febyte range [0 %d)",
                             start, bound, feLength(bin)));
    } else {
        int n = 0;
        if (little_endian) {
            for (int i = start, j = 3; i < bound; i = i + 1, j = j - 1) {
                n = n | ((feRef(bin, i) & 0xFF) << (i * 8));
            }
        } else {
            for (int i = start, j = 0; i < bound; i = i + 1, j = j + 1) {
                n = n | ((feRef(bin, i) & 0xFF) << (24 - j * 8));
            }
        }
        return n;
    }
}

/**
 * @return an int that applied big-endian by default.
 */
public static int deInt(@NotNull Febyte bin, int start) {
    return deInt(bin, start, false);
}


public static @NotNull Febyte codeLong(long n, boolean little_endian) {
    Febyte bin = makeFebyte(8);
    if (little_endian) {
        for (int i = 0; i < 8; i = i + 1) {
            byte by = (byte) (n >>> (i * 8));
            feSet(bin, i, by);
        }
    } else {
        for (int i = 0; i < 8; i = i + 1) {
            byte by = (byte) (n >>> (56 - i * 8));
            feSet(bin, i, by);
        }
    }
    return bin;
}

/**
 * @return a byte array that applied big-endian by default.
 */
public static @NotNull Febyte codeLong(long n) {
    return codeLong(n, false);
}

public static long deLong(@NotNull Febyte bin, int start, boolean little_endian) {
    int bound = start + 8;
    if (feLength(bin) < bound) {
        throw new IllegalArgumentException
              (String.format("range [%d %d) is out of febyte range [0 %d)",
                             start, bound, feLength(bin)));
    } else {
        long n = 0;
        if (little_endian) {
            for (int i = start, j = 7; i < bound; i = i + 1, j = j - 1) {
                n = n | ((feRef(bin, i) & 0xFFL) << (i * 8));
            }
        } else {
            for (int i = start, j = 0; i < bound; i = i + 1, j = j + 1) {
                n = n | ((feRef(bin, i) & 0xFFL) << (56 - j * 8));
            }
        }
        return n;
    }
}

/**
 * @return a long that applied big-endian by default.
 */
public static long deLong(@NotNull Febyte bin, int start) {
    return deLong(bin, start, false);
}

public static @NotNull Febyte codeDouble(double n) {
    long bits = Double.doubleToLongBits(n);
    return codeLong(bits);
}

public static double deDouble(@NotNull Febyte bin, int start) {
    int bound = start + 8;
    if (feLength(bin) < bound) {
        throw new IllegalArgumentException
              (String.format("range [%d %d) is out of febyte range [0 %d)",
                             start, bound, feLength(bin)));
    } else {
        long bits = deLong(bin, start);
        return Double.longBitsToDouble(bits);
    }
}

public static @NotNull Febyte codeString(@NotNull String str, byte... endings) {
    if (str.isEmpty() && endings.length == 0) {
        return febyte();
    } else {
        Febyte b_str = febyte(str.getBytes(StandardCharsets.UTF_8));
        Febyte ends = febyte(endings);
        int sz = feLength(b_str) + feLength(ends);
        Febyte bin = makeFebyte(sz);
        feCopyInto(b_str, 0, bin, 0, feLength(b_str));
        feCopyInto(ends, 0, bin, feLength(b_str), feLength(ends));
        return bin;
    }
}

public static Febyte codeDatum(@NotNull Object datum) {
    if (datum instanceof Boolean) {
        return Aid.codeBool((boolean) datum);
    } else if (datum instanceof Integer) {
        return Aid.codeInt((int) datum);
    } else if (datum instanceof Long) {
        return Aid.codeLong((long) datum);
    } else if (datum instanceof Double) {
        return Aid.codeDouble((double) datum);
    } else if (datum instanceof Character) {
        return Aid.codeChar((char) datum);
    } else if (datum instanceof String) {
        return Aid.codeString((String) datum);
    } else if (datum instanceof Febool) {
        return Aid.codeFebool((Febool) datum);
    } else if (datum instanceof Feint) {
        return Aid.codeFeint((Feint) datum);
    } else if (datum instanceof Felong) {
        return Aid.codeFelong((Felong) datum);
    } else if (datum instanceof Fedouble) {
        return Aid.codeFedouble((Fedouble) datum);
    } else if (datum instanceof Time) {
        return Aid.codeTime((Time) datum);
    } else if (datum instanceof Date) {
        return Aid.codeDate((Date) datum);
    } else if (datum instanceof Lot) {
        return Aid.codeLot((Lot) datum);
    } else if (datum instanceof Few) {
        return Aid.codeFew((Few) datum);
    } else {
        throw new IllegalArgumentException(
        String.format("%s is invalid data type", datum));
    }
}

public static Object deDatum(Febyte b_data) {
    Feint pos = makeFeint(1);
    return Aid.deDatum(b_data, pos);
}
}
