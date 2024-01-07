package share.binary;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.datetime.Date;
import share.datetime.Time;
import share.primitive.Febool;
import share.primitive.Febyte;
import share.primitive.Fedouble;
import share.primitive.Feint;
import share.primitive.Felong;
import share.progressive.Few;
import share.progressive.Lot;

import java.nio.charset.StandardCharsets;

import static share.datetime.Dt.makeDate;
import static share.datetime.Dt.makeTime;
import static share.primitive.Pm.*;
import static share.progressive.Pg.*;


class Aid {

@SuppressWarnings("SpellCheckingInspection")
private static final char[] HEX_STR = "0123456789ABCDEF".toCharArray();

@Contract(pure = true)
static @NotNull String hex(byte by) {
    return new String(new char[]{HEX_STR[(by >> 4) & 0xF], HEX_STR[by & 0xF]});
}

//region Coding
static @NotNull Febyte codeBool(boolean b) {
    Febyte bin = makeFebyte(2);
    feSet(bin, 0, Label.BOOL);
    if (b) {
        feSet(bin, 1, (byte) 1);
    }
    return bin;
}

static @NotNull Febyte codeInt(int n) {
    Febyte bin = makeFebyte(5);
    feSet(bin, 0, Label.INT);
    feCopyInto(Binary.codeInt(n), 0, bin, 1, 4);
    return bin;
}

static @NotNull Febyte codeLong(long n) {
    Febyte bin = makeFebyte(9);
    feSet(bin, 0, Label.LONG);
    feCopyInto(Binary.codeLong(n), 0, bin, 1, 8);
    return bin;
}

static @NotNull Febyte codeDouble(double n) {
    Febyte bin = makeFebyte(9);
    feSet(bin, 0, Label.DOUBLE);
    feCopyInto(Binary.codeDouble(n), 0, bin, 1, 8);
    return bin;
}

static @NotNull Febyte codeChar(char c) {
    Febyte bin = makeFebyte(5);
    feSet(bin, 0, Label.CHAR);
    feCopyInto(Binary.codeInt(c), 0, bin, 1, 4);
    return bin;
}

static @NotNull Febyte codeString(@NotNull String str) {
    Febyte b_str = febyte(str.getBytes(StandardCharsets.UTF_8));
    Febyte bin = makeFebyte(feLength(b_str) + 2);
    feSet(bin, 0, Label.STRING);
    feCopyInto(b_str, 0, bin, 1, feLength(b_str));
    return bin;
}

static @NotNull Febyte codeFebool(Febool bs) {
    if (feLength(bs) == 0) {
        Febyte bin = makeFebyte(5);
        feSet(bin, 0, Label.FEBOOL);
        return bin;
    } else {
        int sz = feLength(bs);
        Febyte bin = makeFebyte(5 + sz / 8 + 1);
        feSet(bin, 0, Label.FEBOOL);
        feCopyInto(Binary.codeInt(sz), 0, bin, 1, 4);
        for (int i = 0; i < sz; i = i + 1) {
            if (feRef(bs, i)) {
                int i_bv = 5 + i / 8;
                byte by = (byte) (feRef(bin, i_bv) | (1 << (i % 8)));
                feSet(bin, i_bv, by);
            }
        }
        return bin;
    }
}

static @NotNull Febyte codeFeint(@NotNull Feint ins) {
    if (feLength(ins) == 0) {
        Febyte bin = makeFebyte(5);
        feSet(bin, 0, Label.FEINT);
        return bin;
    } else {
        int sz = feLength(ins);
        Febyte bin = makeFebyte(5 + sz * 4);
        feSet(bin, 0, Label.FEINT);
        feCopyInto(Binary.codeInt(sz), 0, bin, 1, 4);
        for (int i = 0; i < sz; i = i + 1) {
            Febyte tmp = Binary.codeInt(feRef(ins, i));
            feCopyInto(tmp, 0, bin, 5 + i * 4, 4);
        }
        return bin;
    }
}

static @NotNull Febyte codeFelong(@NotNull Felong ls) {
    if (feLength(ls) == 0) {
        Febyte bin = makeFebyte(5);
        feSet(bin, 0, Label.FELONG);
        return bin;
    } else {
        int sz = feLength(ls);
        Febyte bin = makeFebyte(5 + sz * 8);
        feSet(bin, 0, Label.FELONG);
        feCopyInto(Binary.codeInt(sz), 0, bin, 1, 4);
        for (int i = 0; i < sz; i = i + 1) {
            Febyte tmp = Binary.codeLong(feRef(ls, i));
            feCopyInto(tmp, 0, bin, 5 + i * 8, 8);
        }
        return bin;
    }
}

static @NotNull Febyte codeFedouble(@NotNull Fedouble ds) {
    if (feLength(ds) == 0) {
        Febyte bin = makeFebyte(5);
        feSet(bin, 0, Label.FEDOUBLE);
        return bin;
    } else {
        int sz = feLength(ds);
        Febyte bin = makeFebyte(5 + sz * 8);
        feSet(bin, 0, Label.FEDOUBLE);
        feCopyInto(Binary.codeInt(sz), 0, bin, 1, 4);
        for (int i = 0; i < sz; i = i + 1) {
            Febyte tmp = Binary.codeDouble(feRef(ds, i));
            feCopyInto(tmp, 0, bin, 5 + i * 8, 8);
        }
        return bin;
    }
}

static @NotNull Febyte codeTime(@NotNull Time t) {
    Febyte bin = makeFebyte(17);
    feSet(bin, 0, Label.TIME);
    Febyte b_sec = Binary.codeLong(t.second());
    feCopyInto(b_sec, 0, bin, 1, 8);
    Febyte b_nsec = Binary.codeLong(t.nano());
    feCopyInto(b_nsec, 0, bin, 9, 8);
    return bin;
}

static @NotNull Febyte codeDate(@NotNull Date d) {
    Feint ins = makeFeint(8);
    feSet(ins, 0, d.year());
    feSet(ins, 1, d.month());
    feSet(ins, 2, d.dayOfMonth());
    feSet(ins, 3, d.hour());
    feSet(ins, 4, d.minute());
    feSet(ins, 5, d.second());
    feSet(ins, 6, d.nano());
    feSet(ins, 7, d.offset());
    Febyte bs = codeFeint(ins);
    int sz = feLength(bs);
    Febyte bin = makeFebyte(1 + sz);
    feSet(bin, 0, Label.DATE);
    feCopyInto(bs, 0, bin, 1, sz);
    return bin;
}

// store: lot of Febyte
private static int size(Lot store, int n) {
    if (isNull(store)) {
        return n;
    } else {
        return size(cdr(store), feLength((Febyte) car(store)) + n);
    }
}

// store: lot of Febyte
private static void merge(Lot store, Febyte bin, int pos, byte CONS, byte END) {
    if (isNull(cdr(store))) {
        int sz = feLength((Febyte) car(store));
        feCopyInto((Febyte) car(store), 0, bin, pos, sz);
        feSet(bin, pos + sz, END);
    } else {
        int sz = feLength((Febyte) car(store));
        feCopyInto((Febyte) car(store), 0, bin, pos, sz);
        feSet(bin, pos + sz, CONS);
        merge(cdr(store), bin, pos + sz + 1, CONS, END);
    }
}

static @NotNull Febyte codeLot(Lot datum) {
    if (isNull(datum)) {
        return febyte(Label.LEST_BEGIN, Label.LEST_END);
    } else {
        Lot store = _map(datum);
        int sz = size(store, 0) + length(store) + 1;
        Febyte bin = makeFebyte(sz);
        feSet(bin, 0, Label.LEST_BEGIN);
        merge(store, bin, 1, Label.LEST_CONS, Label.LEST_END);
        return bin;
    }
}

private static Lot _map(Lot lt) {
    if (isNull(lt)) {
        return lot();
    } else {
        Febyte bin = Binary.codeDatum(car(lt));
        return cons(bin, _map(cdr(lt)));
    }
}

// bins: fex of Febyte
private static int size(Few bins) {
    int bound = length(bins);
    int sz = 0;
    for (int i = 0; i < bound; i = i + 1) {
        sz = sz + feLength((Febyte) fewRef(bins, i));
    }
    return sz;
}

// bins: fex of Febyte
private static void merge(Few bins, Febyte bin) {
    int sz = length(bins);
    feCopyInto(Binary.codeInt(sz), 0, bin, 1, 4);
    int pos = 5;
    for (int i = 0; i < sz; i = i + 1) {
        Febyte bs = (Febyte) fewRef(bins, i);
        int len = feLength(bs);
        feCopyInto(bs, 0, bin, pos, len);
        pos = pos + len;
    }
}

static @NotNull Febyte codeFew(Few datum) {
    int sz = length(datum);
    if (sz == 0) {
        Febyte bin = makeFebyte(5);
        feSet(bin, 0, Label.FEX);
        return bin;
    } else {
        Few store = _map(datum);
        int sz_bin = 5 + size(store);
        Febyte bin = makeFebyte(sz_bin);
        feSet(bin, 0, Label.FEX);
        merge(store, bin);
        return bin;
    }
}

// return: fex of Febyte
private static @NotNull Few _map(Few fw) {
    int sz = length(fw);
    Few bins = makeFew(sz, null);
    for (int i = 0; i < sz; i = i + 1) {
        Febyte bin = Binary.codeDatum(fewRef(fw, i));
        fewSet(bins, i, bin);
    }
    return bins;
}
//endregion


//region Decoding Aid
static boolean deBool(byte by) {
    return (by & 1) == 1;
}

static @NotNull String deString(@NotNull Febyte bin) {
    return new String(bin.toRaw(), StandardCharsets.UTF_8);
}

static @NotNull Febool deFebool(Febyte bin, Feint pos) {
    int start = feRef(pos, 0);
    if (feLength(bin) < start + 4) {
        throw new IllegalArgumentException
              (String.format("range [%d %d) is out of febyte range [0 %d)",
                             start, start + 4, feLength(bin)));
    } else {
        int sz = Binary.deInt(bin, start);
        if (sz == 0) {
            feSet(pos, 0, start + 4);
            return makeFebool(0);
        }

        Febool bl = makeFebool(sz);
        for (int i = 0; i < sz; i = i + 1) {
            int i_bin = start + 4 + i / 8;
            int r = feRef(bin, i_bin) & (1 << (i % 8));
            if (r != 0) {
                feSet(bl, i, true);
            }
        }
        feSet(pos, 0, start + 4 + 1 + sz / 8);
        return bl;
    }
}

static @NotNull Feint deFeint(@NotNull Febyte bin, Feint pos) {
    int start = feRef(pos, 0);
    if (feLength(bin) < start + 4) {
        throw new IllegalArgumentException
              (String.format("range [%d %d) is out of febyte range [0 %d)",
                             start, start + 4, feLength(bin)));
    } else {
        int sz = Binary.deInt(bin, start);
        Feint ins = makeFeint(sz);
        int i_bin = start + 4;
        for (int i = 0; i < sz; i = i + 1) {
            int tmp = Binary.deInt(bin, i_bin);
            i_bin = i_bin + 4;
            feSet(ins, i, tmp);
        }
        feSet(pos, 0, start + 4 + sz * 4);
        return ins;
    }
}

static @NotNull Felong deFelong(@NotNull Febyte bin, Feint pos) {
    int start = feRef(pos, 0);
    if (feLength(bin) < start + 4) {
        throw new IllegalArgumentException
              (String.format("range [%d %d) is out of febyte range [0 %d)",
                             start, start + 4, feLength(bin)));
    } else {
        int sz = Binary.deInt(bin, start);
        Felong ls = makeFelong(sz);
        int i_bin = start + 4;
        for (int i = 0; i < sz; i = i + 1) {
            long tmp = Binary.deLong(bin, i_bin);
            i_bin = i_bin + 8;
            feSet(ls, i, tmp);
        }
        feSet(pos, 0, start + 4 + sz * 8);
        return ls;
    }
}

static @NotNull Fedouble deFedouble(@NotNull Febyte bin, Feint pos) {
    int start = feRef(pos, 0);
    if (feLength(bin) < start + 4) {
        throw new IllegalArgumentException
              (String.format("range [%d %d) is out of febyte range [0 %d)",
                             start, start + 4, feLength(bin)));
    } else {
        int sz = Binary.deInt(bin, start);
        Fedouble ds = makeFedouble(sz);
        int i_bin = start + 4;
        for (int i = 0; i < sz; i = i + 1) {
            double tmp = Binary.deDouble(bin, i_bin);
            i_bin = i_bin + 8;
            feSet(ds, i, tmp);
        }
        feSet(pos, 0, start + 4 + sz * 8);
        return ds;
    }
}

static @NotNull Time deTime(Febyte bin, Feint pos) {
    int start = feRef(pos, 0);
    long sec = Binary.deLong(bin, start);
    long nsec = Binary.deLong(bin, start + 8);
    feSet(pos, 0, start + 16);
    return makeTime(sec, nsec);
}

static @NotNull Date deDate(Feint ins) {
    return makeDate
           (feRef(ins, 0), feRef(ins, 1), feRef(ins, 2), feRef(ins, 3), feRef(ins, 4), feRef(ins, 5),
            feRef(ins, 6), feRef(ins, 7));
}
//endregion


//region Decoding
private static Object dePrimitive(Febyte bin, Feint pos) {
    int i = feRef(pos, 0);
    byte label = feRef(bin, i);
    switch (label) {
        case Label.BOOL -> {
            feSet(pos, 0, i + 2);
            byte by = feRef(bin, i + 1);
            return deBool(by);
        }
        case Label.INT -> {
            feSet(pos, 0, i + 5);
            return Binary.deInt(bin, i + 1);
        }
        case Label.LONG -> {
            feSet(pos, 0, i + 9);
            return Binary.deLong(bin, i + 1);
        }
        case Label.DOUBLE -> {
            feSet(pos, 0, i + 9);
            return Binary.deDouble(bin, i + 1);
        }
        case Label.CHAR -> {
            feSet(pos, 0, i + 5);
            return (char) Binary.deInt(bin, i + 1);
        }
        case Label.STRING -> {
            int bound = endingString(bin, i + 1);
            int sz = bound - i - 1;
            feSet(pos, 0, bound + 1);
            Febyte str = makeFebyte(sz);
            feCopyInto(bin, i + 1, str, 0, sz);
            return deString(str);
        }
        case Label.FEBOOL -> {
            feSet(pos, 0, i + 1);
            return deFebool(bin, pos);
        }
        case Label.FEINT -> {
            feSet(pos, 0, i + 1);
            return deFeint(bin, pos);
        }
        case Label.FELONG -> {
            feSet(pos, 0, i + 1);
            return deFelong(bin, pos);
        }
        case Label.FEDOUBLE -> {
            feSet(pos, 0, i + 1);
            return deFedouble(bin, pos);
        }
        default -> throw new IllegalArgumentException
                         (String.format("invalid code.primitive type label %s in position %d",
                                        hex(label), feRef(pos, 0)));
    }
}

private static int endingString(Febyte bin, int start) {
    int i = start;
    while (feRef(bin, i) != 0) {
        i = i + 1;
    }
    return i;
}

private static Object deWrapped(Febyte bin, Feint pos) {
    int i = feRef(pos, 0);
    byte label = feRef(bin, i);
    switch (label) {
        case Label.TIME -> {
            feSet(pos, 0, i + 1);
            return deTime(bin, pos);
        }
        case Label.DATE -> {
            feSet(pos, 0, i + 2);
            Feint data = deFeint(bin, pos);
            return deDate(data);
        }
        case Label.LEST_BEGIN -> {
            if (feRef(bin, i + 1) == Label.LEST_END) {
                feSet(pos, 0, i + 2);
                return lot();
            } else {
                feSet(pos, 0, i + 1);
                Object o = deDatum(bin, pos);
                return cons(o, (Lot) deDatum(bin, pos));
            }
        }
        case Label.LEST_CONS -> {
            feSet(pos, 0, i + 1);
            Object o = deDatum(bin, pos);
            return cons(o, (Lot) deDatum(bin, pos));
        }
        case Label.LEST_END -> {
            feSet(pos, 0, i + 1);
            return lot();
        }
        case Label.FEX -> {
            int sz = Binary.deInt(bin, i + 1);
            Few fw = makeFew(sz, 0);
            feSet(pos, 0, i + 5);
            for (int k = 0; k < sz; k = k + 1) {
                Object o = deDatum(bin, pos);
                fewSet(fw, k, o);
            }
            return fw;
        }
        default -> throw new IllegalArgumentException
                         (String.format("invalid wrapped type label %s in position %d",
                                        hex(label), feRef(pos, 0)));
    }
}

static Object deDatum(Febyte bin, Feint pos) {
    int label = feRef(bin, feRef(pos, 0)) & 0xFF;
    if (0xA0 <= label && label <= 0xAF) {
        return dePrimitive(bin, pos);
    } else if (0xB0 <= label && label <= 0xBF) {
        return deWrapped(bin, pos);
    } else {
        throw new IllegalArgumentException
              (String.format("invalid datum type label %s in position %d",
                             hex((byte) label), feRef(pos, 0)));
    }
}
//endregion
}