package share.binary;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.datetime.Date;
import share.datetime.Time;
import share.progressive.Few;
import share.progressive.Lot;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static share.progressive.Pg.*;


class Aid {

//region Share
@Contract(pure = true)
static byte @NotNull [] intToBin(int n) {
    byte[] bin = new byte[4];
    int moo = n;
    for (int i = 3; i >= 0; i = i - 1) {
        bin[i] = (byte) moo;
        moo = moo >>> 8;
    }
    return bin;
}

static int binToInt(byte[] bin, int start) {
    int bound = start + 4;
    int n = 0;
    for (int i = start; i < bound; i = i + 1) {
        n = n << 8;
        n = n | (bin[i] & 0xFF);
    }
    return n;
}

@Contract(pure = true)
static byte @NotNull [] longToBin(long n) {
    byte[] bin = new byte[8];
    long moo = n;
    for (int i = 7; i >= 0; i = i - 1) {
        bin[i] = (byte) moo;
        moo = moo >>> 8;
    }
    return bin;
}

static long binToLong(byte[] bin, int start) {
    int bound = start + 8;
    long n = 0;
    for (int i = start; i < bound; i = i + 1) {
        n = n << 8;
        n = n | (bin[i] & 0xFF);
    }
    return n;
}

static byte @NotNull [] doubleToBin(double n) {
    long bits = Double.doubleToLongBits(n);
    return longToBin(bits);
}

static double binToDouble(byte[] bin, int start) {
    long bits = binToLong(bin, start);
    return Double.longBitsToDouble(bits);
}
//endregion


//region Coding
@Contract(pure = true)
static byte @NotNull [] codeBoolean(boolean b) {
    byte[] bin = new byte[2];
    bin[0] = Label.BOOLEAN;
    if (b) {
        bin[1] = (byte) 1;
    }
    return bin;
}

static byte @NotNull [] codeInt(int n) {
    byte[] bin = new byte[5];
    bin[0] = Label.INT;
    System.arraycopy(intToBin(n), 0, bin, 1, 4);
    return bin;
}

static byte @NotNull [] codeLong(long n) {
    byte[] bin = new byte[9];
    bin[0] = Label.LONG;
    System.arraycopy(longToBin(n), 0, bin, 1, 8);
    return bin;
}

static byte @NotNull [] codeDouble(double n) {
    byte[] bin = new byte[9];
    bin[0] = Label.DOUBLE;
    System.arraycopy(doubleToBin(n), 0, bin, 1, 8);
    return bin;
}

static byte @NotNull [] codeChar(char c) {
    byte[] bin = new byte[5];
    bin[0] = Label.CHAR;
    System.arraycopy(intToBin(c), 0, bin, 1, 4);
    return bin;
}

static byte @NotNull [] codeString(@NotNull String str) {
    byte[] b_str = str.getBytes(StandardCharsets.UTF_8);
    byte[] bin = new byte[b_str.length + 2];
    bin[0] = Label.STRING;
    System.arraycopy(b_str, 0, bin, 1, b_str.length);
    return bin;
}

static byte @NotNull [] codeBooleanArray(boolean @NotNull [] bs) {
    if (bs.length == 0) {
        byte[] bin = new byte[5];
        bin[0] = Label.BOOLEAN_ARR;
        return bin;
    } else {
        int n = bs.length;
        byte[] bin = new byte[5 + n / 8 + 1];
        bin[0] = Label.BOOLEAN_ARR;
        System.arraycopy(intToBin(n), 0, bin, 1, 4);
        for (int i = 0; i < n; i = i + 1) {
            if (bs[i]) {
                int index_bs = 5 + i / 8;
                bin[index_bs] = (byte) (bin[index_bs] | (1 << (7 - i % 8)));
            }
        }
        return bin;
    }
}

static byte @NotNull [] codeIntArray(int @NotNull [] ins) {
    if (ins.length == 0) {
        byte[] bin = new byte[5];
        bin[0] = Label.INT_ARR;
        return bin;
    } else {
        int n = ins.length;
        byte[] bin = new byte[5 + n * 4];
        bin[0] = Label.INT_ARR;
        System.arraycopy(intToBin(n), 0, bin, 1, 4);
        for (int i = 0; i < n; i = i + 1) {
            System.arraycopy(intToBin(ins[i]), 0, bin, 5 + i * 4, 4);
        }
        return bin;
    }
}

static byte @NotNull [] codeLongArray(long @NotNull [] ls) {
    if (ls.length == 0) {
        byte[] bin = new byte[5];
        bin[0] = Label.LONG_ARR;
        return bin;
    } else {
        int n = ls.length;
        byte[] bin = new byte[5 + n * 8];
        bin[0] = Label.LONG_ARR;
        System.arraycopy(intToBin(n), 0, bin, 1, 4);
        for (int i = 0; i < n; i = i + 1) {
            System.arraycopy(longToBin(ls[i]), 0, bin, 5 + i * 8, 8);
        }
        return bin;
    }
}

static byte @NotNull [] codeDoubleArray(double @NotNull [] ds) {
    if (ds.length == 0) {
        byte[] bin = new byte[5];
        bin[0] = Label.DOUBLE_ARR;
        return bin;
    } else {
        int n = ds.length;
        byte[] bin = new byte[5 + n * 8];
        bin[0] = Label.DOUBLE_ARR;
        System.arraycopy(intToBin(n), 0, bin, 1, 4);
        for (int i = 0; i < n; i = i + 1) {
            System.arraycopy(doubleToBin(ds[i]), 0, bin, 5 + i * 8, 8);
        }
        return bin;
    }
}


static byte @NotNull [] codeLot(Lot lt) {
    if (isNull(lt)) {
        return new byte[]{Label.LOT_BEGIN, Label.LOT_END};
    } else {
        Lot moo = lot();
        Lot xoo = lt;
        while (!isNull(xoo)) {
            moo = cons(Binary.codeDatum(car(xoo)), moo);
            xoo = cdr(xoo);
        }
        moo = reverse(moo);
        return connectLot(moo);
    }
}

static byte @NotNull [] connectLot(Lot lt_bytes) {
    int sz = sizeOf(lt_bytes) + length(lt_bytes) + 1;
    byte[] bin = new byte[sz];
    bin[0] = Label.LOT_BEGIN;
    bin[sz - 1] = Label.LOT_END;

    int i = 1;
    Lot moo = lt_bytes;
    while (!isNull(cdr(moo))) {
        byte[] bs = (byte[]) car(moo);
        System.arraycopy(bs, 0, bin, i, bs.length);
        i = i + bs.length;
        bin[i] = Label.LOT_CONS;
        i = i + 1;
        moo = cdr(moo);
    }
    byte[] bs = (byte[]) car(moo);
    System.arraycopy(bs, 0, bin, i, bs.length);
    return bin;
}

static int sizeOf(Lot lt_bytes) {
    Lot moo = lt_bytes;
    int n = 0;
    while (!isNull(moo)) {
        byte[] bs = (byte[]) car(moo);
        n = n + bs.length;
        moo = cdr(moo);
    }
    return n;
}


static byte @NotNull [] codeFew(Few fw) {
    if (length(fw) == 0) {
        byte[] bin = new byte[5];
        bin[0] = Label.FEW;
        return bin;
    } else {
        Lot moo = lot();
        int n = length(fw);
        for (int i = 0; i < n; i = i + 1) {
            moo = cons(Binary.codeDatum(fewRef(fw, i)), moo);
        }
        moo = reverse(moo);
        return connectFew(moo);
    }
}

static byte @NotNull [] connectFew(Lot lt_bytes) {
    int sz = sizeOf(lt_bytes) + 5;
    byte[] bin = new byte[sz];
    bin[0] = Label.FEW;
    int n = length(lt_bytes);
    System.arraycopy(intToBin(n), 0, bin, 1, 4);

    int i = 5;
    Lot moo = lt_bytes;
    while (!isNull(moo)) {
        byte[] bs = (byte[]) car(moo);
        System.arraycopy(bs, 0, bin, i, bs.length);
        i = i + bs.length;
        moo = cdr(moo);
    }
    return bin;
}


static byte @NotNull [] codeTime(@NotNull Time t) {
    byte[] bin = new byte[13];
    bin[0] = Label.TIME;
    byte[] b_sec = longToBin(t.second());
    System.arraycopy(b_sec, 0, bin, 1, 8);
    byte[] b_nano = intToBin(t.nanosecond());
    System.arraycopy(b_nano, 0, bin, 9, 4);
    return bin;
}

static byte @NotNull [] codeDate(@NotNull Date d) {
    byte[] bin = new byte[19];
    bin[0] = Label.DATE;
    byte[] moo = intToBin(d.year());
    System.arraycopy(moo, 0, bin, 1, 4);
    bin[5] = (byte) d.month();
    bin[6] = (byte) d.dayOfMonth();
    bin[7] = (byte) d.dayOfWeek();
    bin[8] = (byte) d.hour();
    bin[9] = (byte) d.minute();
    bin[10] = (byte) d.second();
    moo = intToBin(d.nanosecond());
    System.arraycopy(moo, 0, bin, 11, 4);
    moo = intToBin(d.offset());
    System.arraycopy(moo, 0, bin, 15, 4);
    return bin;
}
//endregion


//region Decoding
static boolean decodeBoolean(byte by) {
    return by != 0;
}

static char decodeChar(byte[] bin, int start) {
    int c = binToInt(bin, start);
    return (char) c;
}

@Contract(pure = true)
static boolean @NotNull [] decodeBooleanArray(byte[] bin, int start, int sz) {
    boolean[] bs = new boolean[sz];
    for (int i = 0; i < sz; i = i + 1) {
        int j = start + i / 8;
        byte by = (byte) (bin[j] & (1 << (7 - i % 8)));
        bs[i] = by != 0;
    }
    return bs;
}

@Contract(pure = true)
static int @NotNull [] decodeIntArray(byte[] bin, int start, int sz) {
    int[] ins = new int[sz];
    for (int i = 0, j = start; i < sz; i = i + 1, j = j + 4) {
        ins[i] = binToInt(bin, j);
    }
    return ins;
}

@Contract(pure = true)
static long @NotNull [] decodeLongArray(byte[] bin, int start, int sz) {
    long[] ls = new long[sz];
    for (int i = 0, j = start; i < sz; i = i + 1, j = j + 8) {
        ls[i] = binToLong(bin, j);
    }
    return ls;
}

static double @NotNull [] decodeDoubleArray(byte[] bin, int start, int sz) {
    double[] ds = new double[sz];
    for (int i = 0, j = start; i < sz; i = i + 1, j = j + 8) {
        ds[i] = binToDouble(bin, j);
    }
    return ds;
}

@Contract("_, _ -> new")
static @NotNull Time decodeTime(byte[] bin, int start) {
    long second = binToLong(bin, start + 1);
    int nanosecond = binToInt(bin, start + 9);
    return new Time(second, nanosecond);
}

@Contract("_, _ -> new")
static @NotNull Date decodeDate(byte[] bin, int start) {
    int year = binToInt(bin, start + 1);
    int nanosecond = binToInt(bin, start + 11);
    int offset = binToInt(bin, start + 15);
    return new Date(year, bin[start + 5], bin[start + 6], bin[start + 8], bin[start + 9],
                    bin[start + 10], nanosecond, offset);
}

static class Decoding {
    final byte[] bin;
    int pos;

    Decoding(byte[] bin) {
        this.bin = bin;
        pos = 0;
    }

    Object process() {
        Object datum;
        switch (bin[pos]) {
            case Label.BOOLEAN -> {
                pos = pos + 1;
                datum = decodeBoolean(bin[pos]);
                pos = pos + 1;
            }
            case Label.INT -> {
                pos = pos + 1;
                datum = binToInt(bin, pos);
                pos = pos + 4;
            }
            case Label.LONG -> {
                pos = pos + 1;
                datum = binToLong(bin, pos);
                pos = pos + 8;
            }
            case Label.DOUBLE -> {
                pos = pos + 1;
                datum = binToDouble(bin, pos);
                pos = pos + 8;
            }
            case Label.CHAR -> {
                pos = pos + 1;
                datum = decodeChar(bin, pos);
                pos = pos + 4;
            }
            case Label.STRING -> {
                int start = pos + 1;
                while (bin[pos] != 0) {
                    pos = pos + 1;
                }
                byte[] bs_str = Arrays.copyOfRange(bin, start, pos);
                datum = new String(bs_str, StandardCharsets.UTF_8);
                pos = pos + 1;
            }
            case Label.BOOLEAN_ARR -> {
                pos = pos + 1;
                int sz = binToInt(bin, pos);
                pos = pos + 4;
                datum = decodeBooleanArray(bin, pos, sz);
                pos = pos + (sz + 7) / 8;
            }
            case Label.INT_ARR -> {
                pos = pos + 1;
                int sz = binToInt(bin, pos);
                pos = pos + 4;
                datum = decodeIntArray(bin, pos, sz);
                pos = pos + sz * 4;
            }
            case Label.LONG_ARR -> {
                pos = pos + 1;
                int sz = binToInt(bin, pos);
                pos = pos + 4;
                datum = decodeLongArray(bin, pos, sz);
                pos = pos + sz * 8;
            }
            case Label.DOUBLE_ARR -> {
                pos = pos + 1;
                int sz = binToInt(bin, pos);
                pos = pos + 4;
                datum = decodeDoubleArray(bin, pos, sz);
                pos = pos + sz * 8;
            }
            case Label.LOT_BEGIN -> {
                pos = pos + 1;
                if (bin[pos] == Label.LOT_END) {
                    datum = lot();
                    pos = pos + 1;
                } else {
                    Object moo = process();
                    datum = cons(moo, (Lot) process());
                }
            }
            case Label.LOT_CONS -> {
                pos = pos + 1;
                Object moo = process();
                datum = cons(moo, (Lot) process());
            }
            case Label.LOT_END -> {
                pos = pos + 1;
                datum = lot();
            }
            case Label.FEW -> {
                pos = pos + 1;
                int sz = binToInt(bin, pos);
                pos = pos + 4;
                datum = makeFew(sz, 0);
                for (int i = 0; i < sz; i = i + 1) {
                    Object moo = process();
                    fewSet((Few) datum, i, moo);
                }
            }
            case Label.TIME -> {
                datum = decodeTime(bin, pos);
                pos = pos + 13;
            }
            case Label.DATE -> {
                datum = decodeDate(bin, pos);
                pos = pos + 19;
            }
            default -> throw new RuntimeException(String.format(Shop.UNMATCHED, bin[pos]));
        }
        return datum;
    }
}
//endregion
}
