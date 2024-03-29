package share.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.datetime.Time;
import share.refmethod.Do;
import share.refmethod.Has;
import share.utility.RBTree;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static share.utility.Ut.fletcher32;


public class Pr {

//region Constructor
@Contract("_ -> new")
public static @NotNull Symbol symbol(@NotNull String str) {
    if (str.isEmpty() ||
        str.isBlank()) {
        throw new RuntimeException(String.format(Shop.INVALID_STRING, stringOf(str)));
    } else {
        int checksum = fletcher32(str.getBytes(StandardCharsets.UTF_8));
        if (RBTree.isPresent(Symbol.catalog, checksum)) {
            String str2 = (String) RBTree.ref(Symbol.catalog, checksum);
            if (!str.equals(str2)) {
                throw new RuntimeException(String.format(Shop.JACKPOT, str, str2));
            }
        } else {
            RBTree.insert(Symbol.catalog, checksum, str);
        }
        return new Symbol(checksum);
    }
}

@Contract(value = "_ -> new", pure = true)
public static @NotNull Few few(@NotNull Object @NotNull ... args) {
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

@Contract("_ -> new")
public static @NotNull Lot lot(@NotNull Object @NotNull ... args) {
    Pair pair = new PairTail();
    int n = args.length;
    for (int i = n - 1; i >= 0; i = i - 1) {
        pair = new PairOn(args[i], pair);
    }
    return new Lot(pair);
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
        throw new RuntimeException(String.format(Shop.FEW_INDEX_OUT, index, fw));
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
        throw new RuntimeException(Shop.LOT_END);
    } else {
        return ((PairOn) lt.pair).data;
    }
}

@Contract("_ -> new")
public static @NotNull Lot cdr(Lot lt) {
    if (isNull(lt)) {
        throw new RuntimeException(Shop.LOT_END);
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

public static int length(Lot lt) {
    if (Cycle.isTailCircular(lt)) {
        throw new RuntimeException(String.format(Shop.TAIL_CIRCULAR, lt));
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
        throw new RuntimeException(String.format(Shop.LOT_INDEX_OUT, index, lt));
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
        throw new RuntimeException(String.format(Shop.FEW_INDEX_OUT, index, fw));
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
        throw new RuntimeException(Shop.LOT_END);
    } else {
        ((PairOn) lt.pair).data = datum;
    }
}

public static void setCdr(Lot lt1, Lot lt2) {
    if (isNull(lt1)) {
        throw new RuntimeException(Shop.LOT_END);
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
    if (Cycle.isTailCircular(lt1)) {
        throw new RuntimeException(String.format(Shop.TAIL_CIRCULAR, lt1));
    } else {
        return Aid.appending(lt1, lt2);
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
    if (Cycle.isTailCircular(lt) ||
        (0 <= index && index <= length(lt))) {
        return Aid.heading(lt, index);
    } else {
        throw new RuntimeException(String.format(Shop.LOT_INDEX_OUT, index, lt));
    }
}

public static Lot lotTail(Lot lt, int index) {
    if (Cycle.isTailCircular(lt) ||
        (0 <= index && index <= length(lt))) {
        return Aid.tailing(lt, index);
    } else {
        throw new RuntimeException(String.format(Shop.LOT_INDEX_OUT, index, lt));
    }
}

public static Lot copy(Lot lt) {
    if (isNull(lt)) {
        return lot();
    } else if (Cycle.isTailCircular(lt)) {
        throw new RuntimeException(String.format(Shop.TAIL_CIRCULAR, lt));
    } else {
        return Aid.copying(lt);
    }
}
//endregion


//region Transformer
public static @NotNull Few lotToFew(Lot lt) {
    if (Cycle.isTailCircular(lt)) {
        throw new RuntimeException(String.format(Shop.TAIL_CIRCULAR, lt));
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
    if (Cycle.isTailCircular(lt)) {
        throw new RuntimeException(String.format(Shop.TAIL_CIRCULAR, lt));
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
    if (Cycle.isTailCircular(lt)) {
        throw new RuntimeException(String.format(Shop.TAIL_CIRCULAR, lt));
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
    if (datum instanceof Boolean b) {
        if (b) {
            return "#t";
        } else {
            return "#f";
        }
    } else if (datum instanceof Character c) {
        return Aid.stringOfChar(c);
    } else if (datum instanceof String str) {
        return String.format("\"%s\"", str);
    } else if (datum.getClass().isArray()) {
        return Aid.stringOfArray(datum);
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
            str.append(Aid.stringOfHex(bs[i]));
            str.append(" ");
        }
        str.append(Aid.stringOfHex(bs[n]));
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
        return Aid.numberEq(n1, n2);
    } else if (o1 instanceof Character c1 &&
               o2 instanceof Character c2) {
        return ((char) c1) == c2;
    } else if (o1 instanceof Symbol sym1 &&
               o2 instanceof Symbol sym2) {
        return sym1.identifier == sym2.identifier;
    } else if (o1 instanceof Few fw1 &&
               o2 instanceof Few fw2) {
        return fw1.array == fw2.array;
    } else if (o1 instanceof Lot lt1 &&
               o2 instanceof Lot lt2) {
        return lt1.pair == lt2.pair;
    } else {
        return false;
    }
}

public static boolean equal(Object o1, Object o2) {
    if (eq(o1, o2)) {
        return true;
    } else if (o1.getClass().isArray() &&
               o2.getClass().isArray()) {
        return Aid.arrayEqual(o1, o2);
    } else {
        return o1.equals(o2);
    }
}

public static boolean numberValueEq(Number n1, Number n2) {
    if (n1 instanceof Double ||
        n2 instanceof Double) {
        return n1.doubleValue() == n2.doubleValue();
    } else {
        return n1.longValue() == n2.longValue();
    }
}

public static boolean less(Object o1, Object o2) {
    if (o1 instanceof Symbol sym1 &&
        o2 instanceof Symbol sym2) {
        return sym1.identifier < sym2.identifier;
    } else if (o1 instanceof Number n1 &&
               o2 instanceof Number n2) {
        return Aid.numberLess(n1, n2);
    } else if (o1 instanceof String s1 &&
               o2 instanceof String s2) {
        int m = s1.compareTo(s2);
        return m < 0;
    } else if (o1.getClass().isArray() &&
               o2.getClass().isArray()) {
        return Aid.arrayLess(o1, o2);
    } else if (o1 instanceof Time t1 &&
               o2 instanceof Time t2) {
        return Aid.timeLessThan(t1, t2);
    } else {
        throw new RuntimeException(String.format(Shop.UNDEFINED_COMPARE, o1, o2));
    }
}

public static boolean numberValueLess(Number n1, Number n2) {
    if (n1 instanceof Double ||
        n2 instanceof Double) {
        return n1.doubleValue() < n2.doubleValue();
    } else {
        return n1.longValue() < n2.longValue();
    }
}

public static boolean greater(Object o1, Object o2) {
    return less(o2, o1);
}
//endregion
}
