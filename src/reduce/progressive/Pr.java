package reduce.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reduce.datetime.Time;
import reduce.refmethod.Do;
import reduce.refmethod.Has;

import java.util.Arrays;


public class Pr {

//region Constructor
@Contract("_ -> new")
public static @NotNull Symbol symbol(@NotNull String str) {
    return new Symbol(str);
}

@Contract("_ -> new")
public static @NotNull Lot lot(Object @NotNull ... args) {
    return new Lot(args);
}

@Contract("_ -> new")
public static @NotNull Few few(Object @NotNull ... args) {
    return new Few(args);
}

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


//region Predicate
@Contract(pure = true)
public static boolean isNull(@NotNull Lot lt) {
    return lt.pair instanceof PairTail;
}
//endregion


//region Visitor
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

public static Object car(Lot lt) {
    if (isNull(lt)) {
        throw new RuntimeException(Msg.LOT_END);
    } else {
        return ((PairOn) lt.pair).data;
    }
}

@Contract("_ -> new")
public static @NotNull Lot cdr(Lot lt) {
    if (isNull(lt)) {
        throw new RuntimeException(Msg.LOT_END);
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
        while (!isNull(lt)) {
            n = n + 1;
            lt = cdr(lt);
        }
        return n;
    }
}

public static Object lotRef(Lot lt, int index) {
    if (0 <= index && index < length(lt)) {
        int i = index;
        while (0 < i) {
            lt = cdr(lt);
            i = i - 1;
        }
        return car(lt);
    } else {
        throw new RuntimeException(String.format(Msg.LOT_INDEX_OUT, index, lt));
    }
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
//endregion


//region Setter
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

public static void setCar(Lot lt, Object datum) {
    if (isNull(lt)) {
        throw new RuntimeException(Msg.LOT_END);
    } else {
        ((PairOn) lt.pair).data = datum;
    }
}

public static void setCdr(Lot lt1, Lot lt2) {
    if (isNull(lt1)) {
        throw new RuntimeException(Msg.LOT_END);
    } else {
        ((PairOn) lt1.pair).next = lt2.pair;
    }
}
//endregion


//region Connection
@Contract("_, _ -> new")
public static @NotNull Lot cons(Object datum, @NotNull Lot lt) {
    return new Lot(new PairOn(datum, lt.pair));
}

public static Lot append(Lot lt1, Lot lt2) {
    if (lt1.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt1));
    } else {
        return Mate.appending(lt1, lt2);
    }
}
//endregion


//region Copy
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

public static Lot reverse(Lot lt) {
    if (isNull(lt)) {
        return lt;
    } else {
        Pair pair = new PairTail();
        Lot moo = lt;
        while (!isNull(moo)) {
            pair = new PairOn(car(moo), pair);
            moo = cdr(moo);
        }
        return new Lot(pair);
    }
}

public static Lot lotHead(Lot lt, int index) {
    if (lt.isCircularInBreadth() ||
        (0 <= index && index <= length(lt))) {
        return Mate.heading(lt, index);
    } else {
        throw new RuntimeException(String.format(Msg.LOT_INDEX_OUT, index, lt));
    }
}

public static Lot lotTail(Lot lt, int index) {
    if (lt.isCircularInBreadth() ||
        (0 <= index && index <= length(lt))) {
        return Mate.tailing(lt, index);
    } else {
        throw new RuntimeException(String.format(Msg.LOT_INDEX_OUT, index, lt));
    }
}

public static Lot copy(Lot lt) {
    if (isNull(lt)) {
        return lot();
    } else if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        return Mate.copying(lt);
    }
}
//endregion


//region Transformer
public static @NotNull Few lotToFew(Lot lt) {
    if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        Lot moo = lt;
        int n = length(moo);
        Few fw = makeFew(n);
        for (int i = 0; i < n; i = i + 1) {
            fewSet(fw, i, car(moo));
            moo = cdr(moo);
        }
        return fw;
    }
}

@Contract("_ -> new")
public static @NotNull Lot fewToLot(@NotNull Few fw) {
    Pair pair = new PairTail();
    int n = length(fw);
    for (int i = n - 1; i >= 0; i = i - 1) {
        pair = new PairOn(fewRef(fw, i), pair);
    }
    return new Lot(pair);
}

@Contract("_, _ -> new")
public static @NotNull Lot filter(Has pred, Lot lt) {
    if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        Pair pair = new PairTail();
        Lot moo = lt;
        while (!isNull(moo)) {
            if (pred.apply(car(moo))) {
                pair = new PairOn(car(moo), pair);
            }
            moo = cdr(moo);
        }
        return new Lot(pair);
    }
}

@Contract("_, _ -> new")
public static @NotNull Lot map(Do func, Lot lt) {
    if (lt.isCircularInBreadth()) {
        throw new RuntimeException(String.format(Msg.CYC_BREADTH, lt));
    } else {
        Pair pair = new PairTail();
        Lot moo = lt;
        while (!isNull(moo)) {
            Object datum = func.apply(car(moo));
            pair = new PairOn(datum, pair);
            moo = cdr(moo);
        }
        return new Lot(pair);
    }
}

public static @NotNull Few map(Do func, Few fw) {
    int n = length(fw);
    Few moo = makeFew(n);
    for (int i = 0; i < n; i = i + 1) {
        Object datum = func.apply(fewRef(fw, i));
        fewSet(moo, i, datum);
    }
    return moo;
}
//endregion


//region StringOf
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
        return Mate.arrayEqual(o1, o2);
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
    } else if (o1 instanceof String s1 &&
               o2 instanceof String s2) {
        int m = s1.compareTo(s2);
        return m < 0;
    } else if (o1.getClass().isArray() &&
               o2.getClass().isArray()) {
        return Mate.arrayLess(o1, o2);
    } else if (o1 instanceof Time t1 &&
               o2 instanceof Time t2) {
        return Mate.timeLessThan(t1, t2);
    } else {
        throw new RuntimeException(String.format(Msg.UNDEFINED_COMPARE, o1, o2));
    }
}

public static boolean greater(Object o1, Object o2) {
    return less(o2, o1);
}
//endregion
}
