package reduce.binary;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reduce.datetime.Date;
import reduce.datetime.Time;
import reduce.progressive.Few;
import reduce.progressive.Lot;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static reduce.progressive.Pr.*;


class Aid {

//region Share
static byte @NotNull [] integerToBinary(long n, int length, boolean little_endian) {
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

static long binaryToInteger(byte[] bin, int start, int bound, boolean little_endian) {
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

static byte @NotNull [] doubleToBin(double n) {
    long bits = Double.doubleToLongBits(n);
    return Binary.longToBinary(bits, false);
}

static double binToDouble(byte[] bin, int start) {
    long bits = Binary.binaryToLong(bin, start, false);
    return Double.longBitsToDouble(bits);
}
//endregion


//region Coding
@Contract(pure = true)
static byte @NotNull [] codeBoolean(boolean b) {
    if (b) {
        return new byte[]{Label.BOOLEAN_TRUE};
    } else {
        return new byte[]{Label.BOOLEAN_FALSE};
    }
}

static byte @NotNull [] codeInt(int n) {
    byte[] bin = new byte[5];
    bin[0] = Label.INT;
    System.arraycopy(Binary.intToBinary(n, false), 0, bin, 1, 4);
    return bin;
}

static byte @NotNull [] codeLong(long n) {
    byte[] bin = new byte[9];
    bin[0] = Label.LONG;
    System.arraycopy(Binary.longToBinary(n, false), 0, bin, 1, 8);
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
    System.arraycopy(Binary.intToBinary(c, false), 0, bin, 1, 4);
    return bin;
}

static byte @NotNull [] codeString(@NotNull String str) {
    byte[] b_str = str.getBytes(StandardCharsets.UTF_8);
    byte[] bin = new byte[b_str.length + 2];
    bin[0] = Label.STRING;
    System.arraycopy(b_str, 0, bin, 1, b_str.length);
    return bin;
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
        System.arraycopy(Binary.intToBinary(n, false), 0, bin, 1, 4);
        for (int i = 0; i < n; i = i + 1) {
            System.arraycopy(Binary.intToBinary(ins[i], false), 0, bin, 5 + i * 4, 4);
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
        System.arraycopy(Binary.intToBinary(n, false), 0, bin, 1, 4);
        for (int i = 0; i < n; i = i + 1) {
            System.arraycopy(Binary.longToBinary(ls[i], false), 0, bin, 5 + i * 8, 8);
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
        System.arraycopy(Binary.intToBinary(n, false), 0, bin, 1, 4);
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
            moo = cons(Binary.codingDatum(car(xoo)), moo);
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
            moo = cons(Binary.codingDatum(fewRef(fw, i)), moo);
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
    System.arraycopy(Binary.intToBinary(n, false), 0, bin, 1, 4);

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
    byte[] b_sec = Binary.longToBinary(t.second(), false);
    System.arraycopy(b_sec, 0, bin, 1, 8);
    byte[] b_nano = Binary.intToBinary(t.nanosecond(), false);
    System.arraycopy(b_nano, 0, bin, 9, 4);
    return bin;
}

static byte @NotNull [] codeDate(@NotNull Date d) {
    byte[] bin = new byte[19];
    bin[0] = Label.DATE;
    byte[] moo = Binary.intToBinary(d.year(), false);
    System.arraycopy(moo, 0, bin, 1, 4);
    bin[5] = (byte) d.month();
    bin[6] = (byte) d.dayOfMonth();
    bin[7] = (byte) d.dayOfWeek();
    bin[8] = (byte) d.hour();
    bin[9] = (byte) d.minute();
    bin[10] = (byte) d.second();
    moo = Binary.intToBinary(d.nanosecond(), false);
    System.arraycopy(moo, 0, bin, 11, 4);
    moo = Binary.intToBinary(d.offset(), false);
    System.arraycopy(moo, 0, bin, 15, 4);
    return bin;
}
//endregion


//region Decoding
static char decodeChar(byte[] bin, int start) {
    int c = Binary.binaryToInt(bin, start, false);
    return (char) c;
}

@Contract(pure = true)
static int @NotNull [] decodeIntArray(byte[] bin, int start, int sz) {
    int[] ins = new int[sz];
    for (int i = 0, j = start; i < sz; i = i + 1, j = j + 4) {
        ins[i] = Binary.binaryToInt(bin, j, false);
    }
    return ins;
}

@Contract(pure = true)
static long @NotNull [] decodeLongArray(byte[] bin, int start, int sz) {
    long[] ls = new long[sz];
    for (int i = 0, j = start; i < sz; i = i + 1, j = j + 8) {
        ls[i] = Binary.binaryToLong(bin, j, false);
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
    long second = Binary.binaryToLong(bin, start + 1, false);
    int nanosecond = Binary.binaryToInt(bin, start + 9, false);
    return new Time(second, nanosecond);
}

@Contract("_, _ -> new")
static @NotNull Date decodeDate(byte[] bin, int start) {
    int year = Binary.binaryToInt(bin, start + 1, false);
    int nanosecond = Binary.binaryToInt(bin, start + 11, false);
    int offset = Binary.binaryToInt(bin, start + 15, false);
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
            case Label.BOOLEAN_TRUE -> {
                datum = true;
                pos = pos + 1;
            }
            case Label.BOOLEAN_FALSE -> {
                datum = false;
                pos = pos + 1;
            }
            case Label.INT -> {
                pos = pos + 1;
                datum = Binary.binaryToInt(bin, pos, false);
                pos = pos + 4;
            }
            case Label.LONG -> {
                pos = pos + 1;
                datum = Binary.binaryToLong(bin, pos, false);
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
            case Label.INT_ARR -> {
                pos = pos + 1;
                int sz = Binary.binaryToInt(bin, pos, false);
                pos = pos + 4;
                datum = decodeIntArray(bin, pos, sz);
                pos = pos + sz * 4;
            }
            case Label.LONG_ARR -> {
                pos = pos + 1;
                int sz = Binary.binaryToInt(bin, pos, false);
                pos = pos + 4;
                datum = decodeLongArray(bin, pos, sz);
                pos = pos + sz * 8;
            }
            case Label.DOUBLE_ARR -> {
                pos = pos + 1;
                int sz = Binary.binaryToInt(bin, pos, false);
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
                int sz = Binary.binaryToInt(bin, pos, false);
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
