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


class BinaryAid {

private static final char[] HEX_STR = "0123456789ABCDEF".toCharArray();

@Contract(pure = true)
static @NotNull String _hex(byte by) {
    return new String(new char[]{HEX_STR[(by >> 4) & 0xF], HEX_STR[by & 0xF]});
}

//region Coding
static @NotNull Febyte _code_bool(boolean b) {
    Febyte bin = makeFebyte(2);
    feSet(bin, 0, BinaryLabel.BOOL);
    if (b) {
        feSet(bin, 1, (byte) 1);
    }
    return bin;
}

static @NotNull Febyte _code_int(int n) {
    Febyte bin = makeFebyte(5);
    feSet(bin, 0, BinaryLabel.INT);
    feCopyInto(Binary.codeInt(n), 0, bin, 1, 4);
    return bin;
}

static @NotNull Febyte _code_long(long n) {
    Febyte bin = makeFebyte(9);
    feSet(bin, 0, BinaryLabel.LONG);
    feCopyInto(Binary.codeLong(n), 0, bin, 1, 8);
    return bin;
}

static @NotNull Febyte _code_double(double n) {
    Febyte bin = makeFebyte(9);
    feSet(bin, 0, BinaryLabel.DOUBLE);
    feCopyInto(Binary.codeDouble(n), 0, bin, 1, 8);
    return bin;
}

static @NotNull Febyte _code_char(char c) {
    Febyte bin = makeFebyte(5);
    feSet(bin, 0, BinaryLabel.CHAR);
    feCopyInto(Binary.codeInt(c), 0, bin, 1, 4);
    return bin;
}

static @NotNull Febyte _code_string(@NotNull String str) {
    Febyte b_str = febyte(str.getBytes(StandardCharsets.UTF_8));
    Febyte bin = makeFebyte(feLength(b_str) + 2);
    feSet(bin, 0, BinaryLabel.STRING);
    feCopyInto(b_str, 0, bin, 1, feLength(b_str));
    return bin;
}

static @NotNull Febyte _code_febool(Febool bs) {
    if (feLength(bs) == 0) {
        Febyte bin = makeFebyte(5);
        feSet(bin, 0, BinaryLabel.FEBOOL);
        return bin;
    } else {
        int sz = feLength(bs);
        Febyte bin = makeFebyte(5 + sz / 8 + 1);
        feSet(bin, 0, BinaryLabel.FEBOOL);
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

static @NotNull Febyte _code_feint(@NotNull Feint ins) {
    if (feLength(ins) == 0) {
        Febyte bin = makeFebyte(5);
        feSet(bin, 0, BinaryLabel.FEINT);
        return bin;
    } else {
        int sz = feLength(ins);
        Febyte bin = makeFebyte(5 + sz * 4);
        feSet(bin, 0, BinaryLabel.FEINT);
        feCopyInto(Binary.codeInt(sz), 0, bin, 1, 4);
        for (int i = 0; i < sz; i = i + 1) {
            Febyte tmp = Binary.codeInt(feRef(ins, i));
            feCopyInto(tmp, 0, bin, 5 + i * 4, 4);
        }
        return bin;
    }
}

static @NotNull Febyte _code_felong(@NotNull Felong ls) {
    if (feLength(ls) == 0) {
        Febyte bin = makeFebyte(5);
        feSet(bin, 0, BinaryLabel.FELONG);
        return bin;
    } else {
        int sz = feLength(ls);
        Febyte bin = makeFebyte(5 + sz * 8);
        feSet(bin, 0, BinaryLabel.FELONG);
        feCopyInto(Binary.codeInt(sz), 0, bin, 1, 4);
        for (int i = 0; i < sz; i = i + 1) {
            Febyte tmp = Binary.codeLong(feRef(ls, i));
            feCopyInto(tmp, 0, bin, 5 + i * 8, 8);
        }
        return bin;
    }
}

static @NotNull Febyte _code_fedouble(@NotNull Fedouble ds) {
    if (feLength(ds) == 0) {
        Febyte bin = makeFebyte(5);
        feSet(bin, 0, BinaryLabel.FEDOUBLE);
        return bin;
    } else {
        int sz = feLength(ds);
        Febyte bin = makeFebyte(5 + sz * 8);
        feSet(bin, 0, BinaryLabel.FEDOUBLE);
        feCopyInto(Binary.codeInt(sz), 0, bin, 1, 4);
        for (int i = 0; i < sz; i = i + 1) {
            Febyte tmp = Binary.codeDouble(feRef(ds, i));
            feCopyInto(tmp, 0, bin, 5 + i * 8, 8);
        }
        return bin;
    }
}

static @NotNull Febyte _code_time(@NotNull Time t) {
    Febyte bin = makeFebyte(17);
    feSet(bin, 0, BinaryLabel.TIME);
    Febyte b_sec = Binary.codeLong(t.second());
    feCopyInto(b_sec, 0, bin, 1, 8);
    Febyte b_nsec = Binary.codeLong(t.nano());
    feCopyInto(b_nsec, 0, bin, 9, 8);
    return bin;
}

static @NotNull Febyte _code_date(@NotNull Date d) {
    Feint ins = makeFeint(8);
    feSet(ins, 0, d.year());
    feSet(ins, 1, d.month());
    feSet(ins, 2, d.dayOfMonth());
    feSet(ins, 3, d.hour());
    feSet(ins, 4, d.minute());
    feSet(ins, 5, d.second());
    feSet(ins, 6, d.nano());
    feSet(ins, 7, d.offset());
    Febyte bs = _code_feint(ins);
    int sz = feLength(bs);
    Febyte bin = makeFebyte(1 + sz);
    feSet(bin, 0, BinaryLabel.DATE);
    feCopyInto(bs, 0, bin, 1, sz);
    return bin;
}

// store: lot of Febyte
private static int _size(Lot store, int n) {
    if (isNull(store)) {
        return n;
    } else {
        return _size(cdr(store), feLength((Febyte) car(store)) + n);
    }
}

// store: lot of Febyte
private static void _merge(Lot store, Febyte bin, int pos, byte CONS, byte END) {
    if (isNull(cdr(store))) {
        int sz = feLength((Febyte) car(store));
        feCopyInto((Febyte) car(store), 0, bin, pos, sz);
        feSet(bin, pos + sz, END);
    } else {
        int sz = feLength((Febyte) car(store));
        feCopyInto((Febyte) car(store), 0, bin, pos, sz);
        feSet(bin, pos + sz, CONS);
        _merge(cdr(store), bin, pos + sz + 1, CONS, END);
    }
}

static @NotNull Febyte _code_lot(Lot datum) {
    if (isNull(datum)) {
        return febyte(BinaryLabel.LEST_BEGIN, BinaryLabel.LEST_END);
    } else {
        Lot store = _map(datum);
        int sz = _size(store, 0) + length(store) + 1;
        Febyte bin = makeFebyte(sz);
        feSet(bin, 0, BinaryLabel.LEST_BEGIN);
        _merge(store, bin, 1, BinaryLabel.LEST_CONS, BinaryLabel.LEST_END);
        return bin;
    }
}

private static Lot _map(Lot lo) {
    if (isNull(lo)) {
        return lot();
    } else {
        Febyte bin = Binary.codeDatum(car(lo));
        return cons(bin, _map(cdr(lo)));
    }
}

// bins: fex of Febyte
private static int _size(Few bins) {
    int bound = length(bins);
    int sz = 0;
    for (int i = 0; i < bound; i = i + 1) {
        sz = sz + feLength((Febyte) fewRef(bins, i));
    }
    return sz;
}

// bins: fex of Febyte
private static void _merge(Few bins, Febyte bin) {
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

static @NotNull Febyte _code_fex(Few datum) {
    int sz = length(datum);
    if (sz == 0) {
        Febyte bin = makeFebyte(5);
        feSet(bin, 0, BinaryLabel.FEX);
        return bin;
    } else {
        Few store = _map(datum);
        int sz_bin = 5 + _size(store);
        Febyte bin = makeFebyte(sz_bin);
        feSet(bin, 0, BinaryLabel.FEX);
        _merge(store, bin);
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
static boolean _de_bool(byte by) {
    return (by & 1) == 1;
}

static @NotNull String _de_string(@NotNull Febyte bin) {
    return new String(bin.toRaw(), StandardCharsets.UTF_8);
}

static @NotNull Febool _de_febool(Febyte bin, Feint pos) {
    int start = feRef(pos, 0) ;
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

static @NotNull Feint _de_feint(@NotNull Febyte bin, Feint pos) {
    int start = feRef(pos, 0) ;
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

static @NotNull Felong _de_felong(@NotNull Febyte bin, Feint pos) {
    int start = feRef(pos, 0) ;
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

static @NotNull Fedouble _de_fedouble(@NotNull Febyte bin, Feint pos) {
    int start = feRef(pos, 0) ;
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

static @NotNull Time _de_time(Febyte bin, Feint pos) {
    int start = feRef(pos, 0) ;
    long sec = Binary.deLong(bin, start);
    long nsec = Binary.deLong(bin, start + 8);
    feSet(pos, 0, start + 16);
    return makeTime(sec, nsec);
}

static @NotNull Date _de_date(Feint ins) {
    return makeDate
           (feRef(ins, 0), feRef(ins, 1), feRef(ins, 2), feRef(ins, 3), feRef(ins, 4), feRef(ins, 5),
           feRef(ins, 6), feRef(ins, 7));
}
//endregion


//region Decoding
private static Object _de_primitive(Febyte bin, Feint pos) {
    int i = feRef(pos, 0) ;
    byte label = feRef(bin, i);
    switch (label) {
        case BinaryLabel.BOOL -> {
            feSet(pos, 0, i + 2);
            byte by = feRef(bin, i + 1);
            return _de_bool(by);
        }
        case BinaryLabel.INT -> {
            feSet(pos, 0, i + 5);
            return Binary.deInt(bin, i + 1);
        }
        case BinaryLabel.LONG -> {
            feSet(pos, 0, i + 9);
            return Binary.deLong(bin, i + 1);
        }
        case BinaryLabel.DOUBLE -> {
            feSet(pos, 0, i + 9);
            return Binary.deDouble(bin, i + 1);
        }
        case BinaryLabel.CHAR -> {
            feSet(pos, 0, i + 5);
            return (char) Binary.deInt(bin, i + 1);
        }
        case BinaryLabel.STRING -> {
            int bound = _ending_string(bin, i + 1);
            int sz = bound - i - 1;
            feSet(pos, 0, bound + 1);
            Febyte str = makeFebyte(sz);
            feCopyInto(bin, i + 1, str, 0, sz);
            return _de_string(str);
        }
        case BinaryLabel.FEBOOL -> {
            feSet(pos, 0, i + 1);
            return _de_febool(bin, pos);
        }
        case BinaryLabel.FEINT -> {
            feSet(pos, 0, i + 1);
            return _de_feint(bin, pos);
        }
        case BinaryLabel.FELONG -> {
            feSet(pos, 0, i + 1);
            return _de_felong(bin, pos);
        }
        case BinaryLabel.FEDOUBLE -> {
            feSet(pos, 0, i + 1);
            return _de_fedouble(bin, pos);
        }
        default -> throw new IllegalArgumentException
                         (String.format("invalid primitive type label %s in position %d",
                         _hex(label), feRef(pos, 0) ));
    }
}

private static int _ending_string(Febyte bin, int start) {
    int i = start;
    while (feRef(bin, i) != 0) {
        i = i + 1;
    }
    return i;
}

private static Object _de_wrapped(Febyte bin, Feint pos) {
    int i = feRef(pos, 0) ;
    byte label = feRef(bin, i);
    switch (label) {
        case BinaryLabel.TIME -> {
            feSet(pos, 0, i + 1);
            return _de_time(bin, pos);
        }
        case BinaryLabel.DATE -> {
            feSet(pos, 0, i + 2);
            Feint data = _de_feint(bin, pos);
            return _de_date(data);
        }
        case BinaryLabel.LEST_BEGIN -> {
            if (feRef(bin, i + 1) == BinaryLabel.LEST_END) {
                feSet(pos, 0, i + 2);
                return lot();
            } else {
                feSet(pos, 0, i + 1);
                Object o = _de_datum(bin, pos);
                return cons(o, (Lot) _de_datum(bin, pos));
            }
        }
        case BinaryLabel.LEST_CONS -> {
            feSet(pos, 0, i + 1);
            Object o = _de_datum(bin, pos);
            return cons(o, (Lot) _de_datum(bin, pos));
        }
        case BinaryLabel.LEST_END -> {
            feSet(pos, 0, i + 1);
            return lot();
        }
        case BinaryLabel.FEX -> {
            int sz = Binary.deInt(bin, i + 1);
            Few fw = makeFew(sz, 0);
            feSet(pos, 0, i + 5);
            for (int k = 0; k < sz; k = k + 1) {
                Object o = _de_datum(bin, pos);
                fewSet(fw, k, o);
            }
            return fw;
        }
        default -> throw new IllegalArgumentException
                         (String.format("invalid wrapped type label %s in position %d",
                         _hex(label), feRef(pos, 0) ));
    }
}

static Object _de_datum(Febyte bin, Feint pos) {
    int label = feRef(bin, feRef(pos, 0) ) & 0xFF;
    if (0xA0 <= label && label <= 0xAF) {
        return _de_primitive(bin, pos);
    } else if (0xB0 <= label && label <= 0xBF) {
        return _de_wrapped(bin, pos);
    } else {
        throw new IllegalArgumentException
              (String.format("invalid datum type label %s in position %d",
              _hex((byte) label), feRef(pos, 0) ));
    }
}
//endregion
}