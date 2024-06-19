package reduce.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reduce.share.Share;
import reduce.share.BinaryLabel;
import reduce.datetime.Date;
import reduce.datetime.Time;
import reduce.numerus.*;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static reduce.progressive.Pr.*;


class Mate {

//region Lot
@Contract("null -> new")
static @NotNull Pair toPairCons(Pair pair) {
    if (pair instanceof PairTail) {
        return new PairTail();
    } else {
        PairOn on = (PairOn) pair;
        return new PairCons(on.data, toPairCons(on.next));
    }
}

static boolean isBelong(Object datum, @NotNull Lot lt) {
    while (!lt.isEmpty() &&
           !equal(datum, car(lt))) {
        lt = cdr(lt);
    }
    return !lt.isEmpty();
}

static Lot remove(Object datum, @NotNull Lot lt) {
    Lot ooo = new Lot();
    while (!lt.isEmpty()) {
        if (equal(datum, car(lt))) {
            lt = cdr(lt);
            break;
        } else {
            ooo = cons(car(lt), ooo);
            lt = cdr(lt);
        }
    }
    while (!ooo.isEmpty()) {
        lt = cons(car(ooo), lt);
        ooo = cdr(ooo);
    }
    return lt;
}

static @NotNull Lot removeAll(Object datum, @NotNull Lot lt) {
    Lot ooo = new Lot();
    while (!lt.isEmpty()) {
        if (!equal(datum, car(lt))) {
            ooo = cons(car(lt), ooo);
        }
        lt = cdr(lt);
    }
    return reverse(ooo);
}

static @NotNull Lot removeDup(@NotNull Lot lt) {
    Lot ooo = new Lot();
    while (!lt.isEmpty()) {
        if (!isBelong(car(lt), cdr(lt))) {
            ooo = cons(car(lt), ooo);
        }
        lt = cdr(lt);
    }
    return reverse(ooo);
}
//endregion


//region Symbol
static final char[] occupant1 = "+-.@".toCharArray();
static final char[] occupant2 = "\"#'(),;[\\]{|}".toCharArray();

static boolean isCharPresent(char c, char @NotNull [] charset) {
    int sz = charset.length;
    int i = 0;
    while (i < sz && c != charset[i]) {
        i = i + 1;
    }
    return i < sz;
}

static boolean isScalar(char c) {
    return c <= 0x1F || Character.isWhitespace(c) || isCharPresent(c, occupant2);
}
//endregion


//region String Of
private static final Few char_code = new Few(0, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0x1B, 0x20, 0x7F);
private static final String[] char_name = {"nul", "alarm", "backspace", "tab", "newline", "vtab",
                                           "page", "return", "esc", "space", "delete"};
@SuppressWarnings("SpellCheckingInspection")
private static final char[] HEX_STR = "0123456789ABCDEF".toCharArray();

static @NotNull String stringOfChar(char c) {
    int i = fewIndex(Pr::eq, (int) c, char_code);
    if (0 <= i) {
        return String.format("#\\%s", char_name[i]);
    } else if (Character.isISOControl(c)) {
        return String.format("#\\u%X", (int) c);
    } else {
        return String.format("#\\%c", c);
    }
}

static @NotNull String originalString(@NotNull String str) {
    int sz = str.length();
    StringBuilder builder = new StringBuilder();
    builder.append('"');
    for (int i = 0; i < sz; i += 1) {
        char c = str.charAt(i);
        switch (c) {
        case '\b' -> {
            builder.append("\\");
            builder.append('b');
        }
        case '\t' -> {
            builder.append('\\');
            builder.append('t');
        }
        case '\n' -> {
            builder.append('\\');
            builder.append('n');
        }
        case '\f' -> {
            builder.append('\\');
            builder.append('f');
        }
        case '\r' -> {
            builder.append('\\');
            builder.append('r');
        }
        case '"' -> {
            builder.append('\\');
            builder.append('"');
        }
        case '\\' -> {
            builder.append(c);
            builder.append(c);
        }
        default -> {
            builder.append(c);
        }
        }
    }
    builder.append('"');
    return builder.toString();
}

static @NotNull String stringOfArray(@NotNull Object array) {
    if (array instanceof boolean[] bs) {
        return String.format("#1(%s)", consArray(bs, bs.length));
    } else if (array instanceof byte[] bs) {
        return String.format("#i8(%s)", consArray(bs, bs.length));
    } else if (array instanceof int[] ins) {
        return String.format("#i32(%s)", consArray(ins, ins.length));
    } else if (array instanceof long[] ls) {
        return String.format("#i64(%s)", consArray(ls, ls.length));
    } else if (array instanceof double[] ds) {
        return String.format("#r64(%s)", consArray(ds, ds.length));
    } else {
        throw new RuntimeException(String.format("unsupported array type %s for printing", array));
    }
}

static @NotNull String consArray(Object arr, int sz) {
    if (sz == 0) {
        return "";
    } else {
        StringBuilder str = new StringBuilder();
        sz = sz - 1;
        for (int i = 0; i < sz; i = i + 1) {
            str.append(stringOf(Array.get(arr, i)));
            str.append(" ");
        }
        str.append(stringOf(Array.get(arr, sz)));
        return str.toString();
    }
}

@Contract(pure = true)
static @NotNull String stringOfHex(byte by) {
    return new String(new char[]{HEX_STR[(by >> 4) & 0xF], HEX_STR[by & 0xF]});
}
//endregion


//region Comparison
static boolean numberEq(Number n1, Number n2) {
    if (n1 instanceof Byte b1 &&
        n2 instanceof Byte b2) {
        return (byte) b1 == b2;
    } else if (n1 instanceof Short s1 &&
               n2 instanceof Short s2) {
        return (short) s1 == s2;
    } else if (n1 instanceof Integer i1 &&
               n2 instanceof Integer i2) {
        return (int) i1 == i2;
    } else if (n1 instanceof Long l1 &&
               n2 instanceof Long l2) {
        return (long) l1 == l2;
    } else if (n1 instanceof Float f1 &&
               n2 instanceof Float f2) {
        return (float) f1 == f2;
    } else if (n1 instanceof Double d1 &&
               n2 instanceof Double d2) {
        return (double) d1 == d2;
    } else {
        return false;
    }
}

static boolean numberLess(Number n1, Number n2) {
    if (n1 instanceof Byte b1 &&
        n2 instanceof Byte b2) {
        return (byte) b1 < b2;
    } else if (n1 instanceof Short s1 &&
               n2 instanceof Short s2) {
        return (short) s1 < s2;
    } else if (n1 instanceof Integer i1 &&
               n2 instanceof Integer i2) {
        return (int) i1 < i2;
    } else if (n1 instanceof Long l1 &&
               n2 instanceof Long l2) {
        return (long) l1 < l2;
    } else if (n1 instanceof Float f1 &&
               n2 instanceof Float f2) {
        return (float) f1 < f2;
    } else if (n1 instanceof Double d1 &&
               n2 instanceof Double d2) {
        return (double) d1 < d2;
    } else {
        return false;
    }
}

static boolean objectArrayEqual(Object @NotNull [] arr1, Object @NotNull [] arr2) {
    if (arr1.length == arr2.length) {
        int i = 0;
        while (i < arr1.length &&
               equal(arr1[i], arr2[i])) {
            i = i + 1;
        }
        return i == arr1.length;
    } else {
        return false;
    }
}

static boolean timeLessThan(@NotNull Time t1, @NotNull Time t2) {
    if (t1.second() < t2.second()) {
        return true;
    } else if (t1.second() == t2.second()) {
        return t1.nanosecond() < t2.nanosecond();
    } else {
        return false;
    }
}
//endregion


//region Binary Code
@Contract(pure = true)
static byte @NotNull [] codeBoolean(boolean b) {
    if (b) {
        return new byte[]{BinaryLabel.BOOLEAN_TRUE};
    } else {
        return new byte[]{BinaryLabel.BOOLEAN_FALSE};
    }
}

static byte @NotNull [] codeInt(int n) {
    byte[] ooo = Share.integerToBinary(n, 4, false);
    byte[] bin = new byte[5];
    bin[0] = BinaryLabel.INT;
    System.arraycopy(ooo, 0, bin, 1, 4);
    return bin;
}

static byte @NotNull [] codeLong(long n) {
    byte[] ooo = Share.integerToBinary(n, 8, false);
    byte[] bin = new byte[9];
    bin[0] = BinaryLabel.LONG;
    System.arraycopy(ooo, 0, bin, 1, 8);
    return bin;
}

static byte @NotNull [] codeDouble(double n) {
    long bits = Double.doubleToLongBits(n);
    byte[] ooo = Share.integerToBinary(bits, 8, false);
    byte[] bin = new byte[9];
    bin[0] = BinaryLabel.DOUBLE;
    System.arraycopy(ooo, 0, bin, 1, 8);
    return bin;
}

static byte @NotNull [] codeChar(char c) {
    byte[] ooo = Share.integerToBinary(c, 3, false);
    byte[] bin = new byte[4];
    bin[0] = BinaryLabel.CHAR;
    System.arraycopy(ooo, 0, bin, 1, 3);
    return bin;
}

static byte @NotNull [] codeString(@NotNull String str) {
    byte[] b_str = str.getBytes(StandardCharsets.UTF_8);
    byte[] bin = new byte[b_str.length + 2];
    bin[0] = BinaryLabel.STRING;
    System.arraycopy(b_str, 0, bin, 1, b_str.length);
    return bin;
}

static byte @NotNull [] codeInts(int @NotNull [] ins) {
    byte[] size = Share.codeSize(ins.length);
    byte[] bin = new byte[1 + size.length + 4 * ins.length];
    bin[0] = BinaryLabel.INTS;
    System.arraycopy(size, 0, bin, 1, size.length);
    for (int i = 0; i < ins.length; i += 1) {
        byte[] ooo = Share.integerToBinary(ins[i], 4, false);
        System.arraycopy(ooo, 0, bin, 1 + size.length + 4 * i, 4);
    }
    return bin;
}

static byte @NotNull [] codeLongs(long @NotNull [] ls) {
    byte[] size = Share.codeSize(ls.length);
    byte[] bin = new byte[1 + size.length + 8 * ls.length];
    bin[0] = BinaryLabel.LONGS;
    System.arraycopy(size, 0, bin, 1, size.length);
    for (int i = 0; i < ls.length; i += 1) {
        byte[] ooo = Share.integerToBinary(ls[i], 8, false);
        System.arraycopy(ooo, 0, bin, 1 + size.length + 8 * i, 8);
    }
    return bin;
}

static byte @NotNull [] codeDoubles(double @NotNull [] ds) {
    byte[] size = Share.codeSize(ds.length);
    byte[] bin = new byte[1 + size.length + 8 * ds.length];
    bin[0] = BinaryLabel.DOUBLES;
    System.arraycopy(size, 0, bin, 1, size.length);
    for (int i = 0; i < ds.length; i += 1) {
        long bits = Double.doubleToLongBits(ds[i]);
        byte[] ooo = Share.integerToBinary(bits, 8, false);
        System.arraycopy(ooo, 0, bin, 1 + size.length + 8 * i, 8);
    }
    return bin;
}

static byte @NotNull [] codeSymbol(@NotNull Symbol sym) {
    byte[] b_str = (sym.toString()).getBytes(StandardCharsets.UTF_8);
    byte[] bin = new byte[b_str.length + 2];
    bin[0] = BinaryLabel.SYMBOL;
    System.arraycopy(b_str, 0, bin, 1, b_str.length);
    return bin;
}

static byte @NotNull [] codeLot(@NotNull Lot lt) {
    if (lt.isEmpty()) {
        return new byte[]{BinaryLabel.LOT_BEGIN, BinaryLabel.LOT_END};
    } else {
        Lot ooo = lt;
        Lot eee = new Lot();
        while (!ooo.isEmpty()) {
            eee = cons(code(car(ooo)), eee);
            ooo = cdr(ooo);
        }
        eee = reverse(eee);
        return connectLot(eee);
    }
}

static byte @NotNull [] connectLot(Lot lt) {
    int sz = sizeOf(lt) + length(lt) + 1;
    byte[] bin = new byte[sz];
    bin[0] = BinaryLabel.LOT_BEGIN;
    bin[sz - 1] = BinaryLabel.LOT_END;

    int i = 1;
    Lot ooo = lt;
    while (!cdr(ooo).isEmpty()) {
        byte[] bs = (byte[]) car(ooo);
        System.arraycopy(bs, 0, bin, i, bs.length);
        i += bs.length;
        bin[i] = BinaryLabel.LOT_CONS;
        i += 1;
        ooo = cdr(ooo);
    }
    byte[] bs = (byte[]) car(ooo);
    System.arraycopy(bs, 0, bin, i, bs.length);
    return bin;
}

static int sizeOf(Lot bins) {
    int n = 0;
    Lot ooo = bins;
    while (!ooo.isEmpty()) {
        byte[] bs = (byte[]) car(ooo);
        n += bs.length;
        ooo = cdr(ooo);
    }
    return n;
}

static byte @NotNull [] codeFew(Few fw) {
    if (length(fw) == 0) {
        byte[] size = Share.codeSize(0);
        byte[] bin = new byte[1 + size.length];
        bin[0] = BinaryLabel.FEW;
        System.arraycopy(size, 0, bin, 1, size.length);
        return bin;
    } else {
        Lot ooo = new Lot();
        int n = length(fw);
        for (int i = 0; i < n; i = i + 1) {
            ooo = cons(code(fewRef(fw, i)), ooo);
        }
        ooo = reverse(ooo);
        return connectFew(ooo);
    }
}

static byte @NotNull [] connectFew(Lot lt) {
    byte[] size = Share.codeSize(length(lt));
    byte[] bin = new byte[1 + size.length + sizeOf(lt)];
    bin[0] = BinaryLabel.FEW;
    System.arraycopy(size, 0, bin, 1, size.length);

    int i = 1 + size.length;
    Lot ooo = lt;
    while (!ooo.isEmpty()) {
        byte[] bs = (byte[]) car(ooo);
        System.arraycopy(bs, 0, bin, i, bs.length);
        i = i + bs.length;
        ooo = cdr(ooo);
    }
    return bin;
}


static byte @NotNull [] codeTime(@NotNull Time t) {
    byte[] bin = new byte[13];
    bin[0] = BinaryLabel.TIME;
    byte[] b_sec = Share.integerToBinary(t.second(), 8, false);
    System.arraycopy(b_sec, 0, bin, 1, 8);
    byte[] b_nano = Share.integerToBinary(t.nanosecond(), 4, false);
    System.arraycopy(b_nano, 0, bin, 9, 4);
    return bin;
}

static byte @NotNull [] codeDate(@NotNull Date d) {
    byte[] bin = new byte[19];
    bin[0] = BinaryLabel.DATE;
    byte[] ooo = Share.integerToBinary(d.year(), 4, false);
    System.arraycopy(ooo, 0, bin, 1, 4);
    bin[5] = (byte) d.month();
    bin[6] = (byte) d.dayOfMonth();
    bin[7] = (byte) d.dayOfWeek();
    bin[8] = (byte) d.hour();
    bin[9] = (byte) d.minute();
    bin[10] = (byte) d.second();
    ooo = Share.integerToBinary(d.nanosecond(), 4, false);
    System.arraycopy(ooo, 0, bin, 11, 4);
    ooo = Share.integerToBinary(d.offset(), 4, false);
    System.arraycopy(ooo, 0, bin, 15, 4);
    return bin;
}
//endregion


//region Binary Decode
@Contract(pure = true)
static int @NotNull [] decodeInts(byte[] bin, int start, int size) {
    int[] ins = new int[size];
    for (int i = 0, j = start; i < size; i += 1, j += 4) {
        ins[i] = (int) Share.binaryToInteger(bin, j, j + 4, false);
    }
    return ins;
}

@Contract(pure = true)
static long @NotNull [] decodeLongs(byte[] bin, int start, int sz) {
    long[] ls = new long[sz];
    for (int i = 0, j = start; i < sz; i += 1, j += 8) {
        ls[i] = Share.binaryToInteger(bin, j, j + 8, false);
    }
    return ls;
}

static double @NotNull [] decodeDoubles(byte[] bin, int start, int sz) {
    double[] ds = new double[sz];
    for (int i = 0, j = start; i < sz; i += 1, j += 8) {
        long bits = Share.binaryToInteger(bin, j, j + 8, false);
        ds[i] = Double.longBitsToDouble(bits);
    }
    return ds;
}

@Contract("_, _ -> new")
static @NotNull Time decodeTime(byte[] bin, int start) {
    long second = Share.binaryToInteger(bin, start, start + 8, false);
    int nanosecond = (int) Share.binaryToInteger(bin, start + 8, start + 12, false);
    return new Time(second, nanosecond);
}

@Contract("_, _ -> new")
static @NotNull Date decodeDate(byte[] bin, int start) {
    int year = (int) Share.binaryToInteger(bin, start, start + 4, false);
    int nanosecond = (int) Share.binaryToInteger(bin, start + 10, start + 14, false);
    int offset = (int) Share.binaryToInteger(bin, start + 14, start + 18, false);
    return new Date(year, bin[start + 4], bin[start + 5], bin[start + 7], bin[start + 8],
                    bin[start + 9], nanosecond, offset);
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
        int label = bin[pos];
        pos += 1;
        switch (label) {
        case BinaryLabel.BOOLEAN_FALSE -> {
            datum = false;
        }
        case BinaryLabel.BOOLEAN_TRUE -> {
            datum = true;
        }
        case BinaryLabel.INT -> {
            datum = (int) Share.binaryToInteger(bin, pos, pos + 4, false);
            pos = pos + 4;
        }
        case BinaryLabel.LONG -> {
            datum = Share.binaryToInteger(bin, pos, pos + 8, false);
            pos = pos + 8;
        }
        case BinaryLabel.DOUBLE -> {
            long bits = Share.binaryToInteger(bin, pos, pos + 8, false);
            datum = Double.longBitsToDouble(bits);
            pos = pos + 8;
        }
        case BinaryLabel.CHAR -> {
            datum = (char) Share.binaryToInteger(bin, pos, pos + 3, false);
            pos = pos + 3;
        }
        case BinaryLabel.STRING -> {
            int start = pos;
            while (bin[pos] != 0) {
                pos = pos + 1;
            }
            byte[] bs_str = Arrays.copyOfRange(bin, start, pos);
            datum = new String(bs_str, StandardCharsets.UTF_8);
            pos = pos + 1;
        }
        case BinaryLabel.INTS -> {
            int size = (int) Share.decodeSize(bin, pos);
            pos = pos + 1 + (bin[pos] & 0xFF);
            datum = decodeInts(bin, pos, size);
            pos = pos + 4 * size;
        }
        case BinaryLabel.LONGS -> {
            int size = (int) Share.decodeSize(bin, pos);
            pos = pos + 1 + (bin[pos] & 0xFF);
            datum = decodeLongs(bin, pos, size);
            pos = pos + 8 * size;
        }
        case BinaryLabel.DOUBLES -> {
            int size = (int) Share.decodeSize(bin, pos);
            pos = pos + 1 + (bin[pos] & 0xFF);
            datum = decodeDoubles(bin, pos, size);
            pos = pos + 8 * size;
        }
        case BinaryLabel.INTACT -> {
            int size = (int) Share.decodeSize(bin, pos);
            pos = pos + 1 + (bin[pos] & 0xFF);
            byte[] big = new byte[size];
            System.arraycopy(bin, pos, big, 0, size);
            datum = new Intact(big);
            pos = pos + size;
        }
        case BinaryLabel.FRACTION -> {
            int sz_num = (int) Share.decodeSize(bin, pos);
            pos = pos + 1 + (bin[pos] & 0xFF);
            byte[] num = new byte[sz_num];
            System.arraycopy(bin, pos, num, 0, sz_num);
            pos = pos + sz_num;

            int sz_den = (int) Share.decodeSize(bin, pos);
            pos = pos + 1 + (bin[pos] & 0xFF);
            byte[] den = new byte[sz_den];
            System.arraycopy(bin, pos, den, 0, sz_den);
            pos = pos + sz_den;
            datum = new Fraction(num, den);
        }
        case BinaryLabel.FLOAT64 -> {
            long bits = Share.binaryToInteger(bin, pos, pos + 8, false);
            double d = Double.longBitsToDouble(bits);
            datum = new Float64(d);
            pos = pos + 8;
        }
        case BinaryLabel.COMPLEX -> {
            Real real = (Real) process();
            Real img = (Real) process();
            datum = new Complex(real, img);
        }
        case BinaryLabel.SYMBOL -> {
            int start = pos;
            while (bin[pos] != 0) {
                pos = pos + 1;
            }
            byte[] bs_str = Arrays.copyOfRange(bin, start, pos);
            datum = new Symbol(new String(bs_str, StandardCharsets.UTF_8));
            pos = pos + 1;
        }
        case BinaryLabel.LOT_BEGIN -> {
            if (bin[pos] == BinaryLabel.LOT_END) {
                datum = new Lot();
                pos = pos + 1;
            } else {
                Object ooo = process();
                datum = cons(ooo, (Lot) process());
            }
        }
        case BinaryLabel.LOT_CONS -> {
            Object ooo = process();
            datum = cons(ooo, (Lot) process());
        }
        case BinaryLabel.LOT_END -> {
            datum = new Lot();
        }
        case BinaryLabel.FEW -> {
            int size = (int) Share.decodeSize(bin, pos);
            pos = pos + 1 + (bin[pos] & 0xFF);
            datum = makeFew(size, 0);
            for (int i = 0; i < size; i = i + 1) {
                Object ooo = process();
                fewSet((Few) datum, i, ooo);
            }
        }
        case BinaryLabel.TIME -> {
            datum = decodeTime(bin, pos);
            pos = pos + 12;
        }
        case BinaryLabel.DATE -> {
            datum = decodeDate(bin, pos);
            pos = pos + 18;
        }
        default -> throw new RuntimeException(String.format(Msg.UNMATCHED, bin[pos]));
        }
        return datum;
    }
}
//endregion
}
