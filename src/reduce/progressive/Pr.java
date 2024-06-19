package reduce.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reduce.datetime.Date;
import reduce.datetime.Time;
import reduce.numerus.*;
import reduce.refmethod.Do;
import reduce.refmethod.Eqv;
import reduce.refmethod.Has;

import java.util.Arrays;
import java.util.Random;


public class Pr {

//region Constructor
public static @NotNull Few makeFew(int amount, Object value) {
    Few fw = new Few(new Object[amount]);
    for (int i = 0; i < amount; i = i + 1) {
        fewSet(fw, i, value);
    }
    return fw;
}

public static @NotNull Few makeFew(int amount) {
    return makeFew(amount, 0);
}
//endregion


//region Visitor
public static Object car(@NotNull Lot lt) {
    if (lt.isEmpty()) {
        throw new RuntimeException(Msg.LOT_EMPTY);
    } else {
        return ((PairOn) lt.pair).data;
    }
}

@Contract("_ -> new")
public static @NotNull Lot cdr(@NotNull Lot lt) {
    if (lt.isEmpty()) {
        throw new RuntimeException(Msg.LOT_EMPTY);
    } else {
        return new Lot(((PairOn) lt.pair).next);
    }
}

public static Object caar(Lot lt) {
    return car((Lot) car(lt));
}

@Contract("_ -> new")
public static @NotNull Lot cddr(Lot lt) {
    return cdr(cdr(lt));
}

public static Object cadr(Lot lt) {
    return car(cdr(lt));
}

public static int length(@NotNull Lot lt) {
    if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        int n = 0;
        while (!lt.isEmpty()) {
            n = n + 1;
            lt = cdr(lt);
        }
        return n;
    }
}

public static Object lotRef(Lot lt, int index) {
    Lot ooo = lt;
    int i = index;
    while (!ooo.isEmpty()) {
        if (i == 0) {
            return car(ooo);
        }
        ooo = cdr(ooo);
        i -= 1;
    }
    throw new RuntimeException(String.format(Msg.LOT_INDEX_OUT, index, lt));
}

public static Object first(Lot lt) {
    return lotRef(lt, 0);
}

public static Object second(Lot lt) {
    return lotRef(lt, 1);
}

public static Object third(Lot lt) {
    return lotRef(lt, 2);
}

public static Object fourth(Lot lt) {
    return lotRef(lt, 3);
}


public static Object fewRef(Few fw, int index) {
    if (0 <= index && index < length(fw)) {
        return fw.array[index];
    } else {
        throw new RuntimeException(String.format(Msg.FEW_INDEX_OUT, index, fw));
    }
}

public static Object ref0(Few fw) {
    return fewRef(fw, 0);
}

public static Object ref1(Few fw) {
    return fewRef(fw, 1);
}

public static Object ref2(Few fw) {
    return fewRef(fw, 2);
}

public static Object ref3(Few fw) {
    return fewRef(fw, 3);
}

public static Object ref4(Few fw) {
    return fewRef(fw, 4);
}

public static Object ref5(Few fw) {
    return fewRef(fw, 5);
}

@Contract(pure = true)
public static int length(@NotNull Few fw) {
    return fw.array.length;
}
//endregion


//region Setter
public static void setCar(@NotNull Lot lt, Object datum) {
    if (lt.isEmpty()) {
        throw new RuntimeException(Msg.LOT_EMPTY);
    } else {
        ((PairOn) lt.pair).data = datum;
    }
}

public static void setCdr(@NotNull Lot lt1, Lot lt2) {
    if (lt1.isEmpty()) {
        throw new RuntimeException(Msg.LOT_EMPTY);
    } else {
        ((PairOn) lt1.pair).next = lt2.pair;
    }
}

public static void fewSet(Few fw, int index, Object value) {
    if (0 <= index && index < length(fw)) {
        fw.array[index] = value;
    } else {
        throw new RuntimeException(String.format(Msg.FEW_INDEX_OUT, index, fw));
    }
}

public static void set0(Few fw, Object datum) {
    fewSet(fw, 0, datum);
}

public static void set1(Few fw, Object datum) {
    fewSet(fw, 1, datum);
}

public static void set2(Few fw, Object datum) {
    fewSet(fw, 2, datum);
}

public static void set3(Few fw, Object datum) {
    fewSet(fw, 3, datum);
}

public static void set4(Few fw, Object datum) {
    fewSet(fw, 4, datum);
}

public static void set5(Few fw, Object datum) {
    fewSet(fw, 5, datum);
}

public static void fewFill(@NotNull Few fw, Object datum) {
    Arrays.fill(fw.array, datum);
}
//endregion


//region Connection
@Contract("_, _ -> new")
public static @NotNull Lot cons(Object datum, @NotNull Lot lt) {
    return new Lot(new PairOn(datum, lt.pair));
}

public static Lot append(@NotNull Lot lt1, Lot lt2) {
    if (lt1.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt1));
    } else {
        Lot ooo = reverse(lt1);
        Lot eee = lt2;
        while (!ooo.isEmpty()) {
            eee = cons(car(ooo), eee);
            ooo = cdr(ooo);
        }
        return eee;
    }
}
//endregion


//region Copy
public static @NotNull Lot reverse(@NotNull Lot lt) {
    if (lt.isEmpty()) {
        return lt;
    } else if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        Pair pair = new PairTail();
        while (!lt.isEmpty()) {
            pair = new PairOn(car(lt), pair);
            lt = cdr(lt);
        }
        return new Lot(pair);
    }
}

public static @NotNull Lot lotHead(@NotNull Lot lt, int index) {
    if (lt.isCircularInBreadth() ||
        (0 <= index && index <= length(lt))) {
        if (index == 0) {
            return new Lot();
        } else {
            PairOn head = new PairOn(car(lt), new PairTail());
            PairOn ooo = head;
            Lot eee = cdr(lt);
            for (int i = index - 1; i > 0; i -= 1) {
                ooo.next = new PairOn(car(eee), new PairTail());
                ooo = (PairOn) ooo.next;
                eee = cdr(eee);
            }
            return new Lot(head);
        }
    } else {
        throw new RuntimeException(String.format(Msg.LOT_INDEX_OUT, index, lt));
    }
}

public static Lot lotTail(@NotNull Lot lt, int index) {
    if (lt.isCircularInBreadth() ||
        (0 <= index && index <= length(lt))) {
        for (int i = index; i > 0; i -= 1) {
            lt = cdr(lt);
        }
        return lt;
    } else {
        throw new RuntimeException(String.format(Msg.LOT_INDEX_OUT, index, lt));
    }
}

@Contract("_ -> new")
public static @NotNull Lot copy(@NotNull Lot lt) {
    if (lt.isEmpty()) {
        return new Lot();
    } else if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        PairOn head = new PairOn(car(lt), new PairTail());
        PairOn ooo = head;
        Lot eee = cdr(lt);
        while (!eee.isEmpty()) {
            ooo.next = new PairOn(car(eee), new PairTail());
            ooo = (PairOn) ooo.next;
            eee = cdr(eee);
        }
        return new Lot(head);
    }
}

public static void fewCopyInto(@NotNull Few src, int src_pos, @NotNull Few dest,
                               int dest_pos, int amount) {
    System.arraycopy(src.array, src_pos, dest.array, dest_pos, amount);
}

public static @NotNull Few copy(@NotNull Few fw) {
    int n = length(fw);
    Few moo = makeFew(n, 0);
    fewCopyInto(fw, 0, moo, 0, n);
    return moo;
}
//endregion


//region Transformer
public static @NotNull Few lotToFew(@NotNull Lot lt) {
    if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        int n = length(lt);
        Few fw = makeFew(n, 0);
        for (int i = 0; i < n; i += 1) {
            fewSet(fw, i, car(lt));
            lt = cdr(lt);
        }
        return fw;
    }
}

@Contract("_ -> new")
public static @NotNull Lot fewToLot(@NotNull Few fw) {
    Pair pair = new PairTail();
    int n = length(fw);
    for (int i = n - 1; i >= 0; i -= 1) {
        pair = new PairOn(fewRef(fw, i), pair);
    }
    return new Lot(pair);
}

@Contract("_, _ -> new")
public static @NotNull Lot filter(Has pred, @NotNull Lot lt) {
    if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        Pair pair = new PairTail();
        while (!lt.isEmpty()) {
            if (pred.apply(car(lt))) {
                pair = new PairOn(car(lt), pair);
            }
            lt = cdr(lt);
        }
        return reverse(new Lot(pair));
    }
}

@Contract("_, _ -> new")
public static @NotNull Lot map(Do func, @NotNull Lot lt) {
    if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        Pair pair = new PairTail();
        while (!lt.isEmpty()) {
            Object datum = func.apply(car(lt));
            pair = new PairOn(datum, pair);
            lt = cdr(lt);
        }
        return reverse(new Lot(pair));
    }
}

public static @NotNull Few map(Do func, Few fw) {
    int n = length(fw);
    Few ooo = makeFew(n, 0);
    for (int i = 0; i < n; i = i + 1) {
        Object datum = func.apply(fewRef(fw, i));
        fewSet(ooo, i, datum);
    }
    return ooo;
}
//endregion


//region StringOf
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

public static String stringOf(Object datum) {
    if (datum == null) {
        return "#<null>";
    } else if (datum instanceof Boolean b) {
        if (b) {
            return "#t";
        } else {
            return "#f";
        }
    } else if (datum instanceof Character c) {
        return Mate.stringOfChar(c);
    } else if (datum instanceof String str) {
        return Mate.originalString(str);
    } else if (datum.getClass().isArray()) {
        return Mate.stringOfArray(datum);
    } else {
        return datum.toString();
    }
}

public static @NotNull String hexOfBytes(byte @NotNull [] bs) {
    int n = bs.length;
    if (n == 0) {
        return "#u8()";
    } else {
        StringBuilder str = new StringBuilder();
        str.append("#u8(");
        n = n - 1;
        for (int i = 0; i < n; i = i + 1) {
            str.append(Mate.stringOfHex(bs[i]));
            str.append(" ");
        }
        str.append(Mate.stringOfHex(bs[n]));
        str.append(")");
        return str.toString();
    }
}
//endregion


//region Comparison
public static boolean eq(Object o1, Object o2) {
    if (o1 == o2) {
        return true;
    } else if (o1 instanceof Boolean b1 &&
               o2 instanceof Boolean b2) {
        return (b1 && b2) || !(b1 || b2);
    } else if (o1 instanceof Number n1 &&
               o2 instanceof Number n2) {
        return Mate.numberEq(n1, n2);
    } else if (o1 instanceof Numerus n1 &&
               o2 instanceof Numerus n2) {
        return Numerus.eq(n1, n2);
    } else if (o1 instanceof Character c1 &&
               o2 instanceof Character c2) {
        return ((char) c1) == c2;
    } else if (o1 instanceof Symbol sym1 &&
               o2 instanceof Symbol sym2) {
        return sym1.identifier == sym2.identifier;
    } else if (o1 instanceof Lot lt1 &&
               o2 instanceof Lot lt2) {
        return lt1.pair == lt2.pair;
    } else if (o1 instanceof Few fw1 &&
               o2 instanceof Few fw2) {
        return fw1.array == fw2.array;
    } else {
        return false;
    }
}

public static boolean equal(Object o1, Object o2) {
    if (eq(o1, o2)) {
        return true;
    } else if (o1.getClass().isArray() &&
               o2.getClass().isArray()) {
        if (o1 instanceof boolean[] bs1 &&
            o2 instanceof boolean[] bs2) {
            int r = Arrays.compare(bs1, bs2);
            return r == 0;
        } else if (o1 instanceof byte[] bs1 &&
                   o2 instanceof byte[] bs2) {
            int r = Arrays.compare(bs1, bs2);
            return r == 0;
        } else if (o1 instanceof int[] ins1 &&
                   o2 instanceof int[] ins2) {
            int r = Arrays.compare(ins1, ins2);
            return r == 0;
        } else if (o1 instanceof long[] ls1 &&
                   o2 instanceof long[] ls2) {
            int r = Arrays.compare(ls1, ls2);
            return r == 0;
        } else if (o1 instanceof double[] ds1 &&
                   o2 instanceof double[] ds2) {
            int r = Arrays.compare(ds1, ds2);
            return r == 0;
        } else {
            return false;
        }
    } else {
        return o1.equals(o2);
    }
}

public static boolean less(Object o1, Object o2) {
    if (o1 instanceof Symbol sym1 &&
        o2 instanceof Symbol sym2) {
        return sym1.identifier < sym2.identifier;
    } else if (o1 instanceof Number n1 &&
               o2 instanceof Number n2) {
        return Mate.numberLess(n1, n2);
    } else if (o1 instanceof Real r1 &&
               o2 instanceof Real r2) {
        return Numerus.valueLess(r1, r2);
    } else if (o1 instanceof String s1 &&
               o2 instanceof String s2) {
        int m = s1.compareTo(s2);
        return m < 0;
    } else if (o1 instanceof Time t1 &&
               o2 instanceof Time t2) {
        return Mate.timeLessThan(t1, t2);
    } else if (o1.getClass().isArray() &&
               o2.getClass().isArray()) {
        if (o1 instanceof boolean[] bs1 &&
            o2 instanceof boolean[] bs2) {
            int r = Arrays.compare(bs1, bs2);
            return r < 0;
        } else if (o1 instanceof byte[] bs1 &&
                   o2 instanceof byte[] bs2) {
            int r = Arrays.compare(bs1, bs2);
            return r < 0;
        } else if (o1 instanceof int[] ins1 &&
                   o2 instanceof int[] ins2) {
            int r = Arrays.compare(ins1, ins2);
            return r < 0;
        } else if (o1 instanceof long[] ls1 &&
                   o2 instanceof long[] ls2) {
            int r = Arrays.compare(ls1, ls2);
            return r < 0;
        } else if (o1 instanceof double[] ds1 &&
                   o2 instanceof double[] ds2) {
            int r = Arrays.compare(ds1, ds2);
            return r < 0;
        } else {
            throw new RuntimeException(String.format(Msg.UNDEFINED_ARR_COMPARE,
                                                     stringOf(o1), stringOf(o2)));
        }
    } else {
        throw new RuntimeException(String.format(Msg.UNDEFINED_COMPARE, o1, o2));
    }
}

public static boolean greater(Object o1, Object o2) {
    return less(o2, o1);
}
//endregion


//region Non-Core Function
public static boolean isBelong(Object datum, @NotNull Lot lt) {
    if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        return Mate.isBelong(datum, lt);
    }
}

public static Lot remove(Object datum, @NotNull Lot lt) {
    if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        return Mate.remove(datum, lt);
    }
}

public static @NotNull Lot removeAll(Object datum, @NotNull Lot lt) {
    if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        return Mate.removeAll(datum, lt);
    }
}

public static @NotNull Lot removeDup(@NotNull Lot lt) {
    if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        return Mate.removeDup(lt);
    }
}

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


//region Binary
public static byte[] code(Object datum) {
    if (datum instanceof Boolean b) {
        return Mate.codeBoolean(b);
    } else if (datum instanceof Integer in) {
        return Mate.codeInt(in);
    } else if (datum instanceof Long l) {
        return Mate.codeLong(l);
    } else if (datum instanceof Double d) {
        return Mate.codeDouble(d);
    } else if (datum instanceof Character c) {
        return Mate.codeChar(c);
    } else if (datum instanceof String str) {
        return Mate.codeString(str);

    } else if (datum instanceof int[] ins) {
        return Mate.codeInts(ins);
    } else if (datum instanceof long[] ls) {
        return Mate.codeLongs(ls);
    } else if (datum instanceof double[] ds) {
        return Mate.codeDoubles(ds);

    } else if (datum instanceof Intact in) {
        return in.code();
    } else if (datum instanceof Fraction fr) {
        return fr.code();
    } else if (datum instanceof Float64 fl) {
        return fl.code();
    } else if (datum instanceof Complex cx) {
        return cx.code();

    } else if (datum instanceof Symbol sym) {
        return Mate.codeSymbol(sym);
    } else if (datum instanceof Lot lt) {
        return Mate.codeLot(lt);
    } else if (datum instanceof Few fw) {
        return Mate.codeFew(fw);

    } else if (datum instanceof Time t) {
        return Mate.codeTime(t);
    } else if (datum instanceof Date d) {
        return Mate.codeDate(d);
    } else {
        throw new RuntimeException(String.format(Msg.UNSUPPORTED, datum));
    }
}

public static Object decode(byte[] bin) {
    Mate.Decoding de = new Mate.Decoding(bin);
    return de.process();
}
//endregion
}
