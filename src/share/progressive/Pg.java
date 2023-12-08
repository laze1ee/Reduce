package share.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.refmethod.Do;
import share.refmethod.Has;

import java.util.Arrays;


public class Pg {

public static String stringOf(Object datum) {
    if (datum instanceof Boolean) {
        if ((boolean) datum) {
            return "#t";
        } else {
            return "#f";
        }
    } else if (datum instanceof Character) {
        return PgAid.stringOfChar((char) datum);
    } else if (datum instanceof String) {
        return String.format("\"%s\"", datum);
    } else {
        return datum.toString();
    }
}


//region Constructor
@Contract("_ -> new")
public static @NotNull Symbol symbol(@NotNull String str) {
    return new Symbol(str);
}

@Contract("_ -> new")
public static @NotNull Lot lot(@NotNull Object... args) {
    return new Lot(args);
}

@Contract(value = "_ -> new", pure = true)
public static @NotNull Few few(@NotNull Object... args) {
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
public static boolean isNull(@NotNull Lot lo) {
    return lo.pair == null;
}
//endregion


//region Visitor
public static int length(@NotNull Lot lo) {
    if (Cycle.isTailCyc(lo)) {
        throw new IllegalArgumentException
              (String.format("error: lot is circular\n%s", lo));
    } else {
        int n = 0;
        while (!isNull(lo)) {
            n = n + 1;
            lo = cdr(lo);
        }
        return n;
    }
}

@Contract(pure = true)
public static int length(@NotNull Few fw) {
    return fw.array.length;
}

public static Object car(@NotNull Lot lo) {
    if (isNull(lo)) {
        throw new IllegalArgumentException("() is not a pair");
    } else {
        return lo.pair.data;
    }
}

public static Object caar(Lot lo) {
    Lot tmp = (Lot) car(lo);
    if (isNull(tmp)) {
        throw new IllegalArgumentException("() is not a pair");
    } else {
        return tmp.pair.data;
    }
}

@Contract("_ -> new")
public static @NotNull Lot cdr(@NotNull Lot lo) {
    if (isNull(lo)) {
        throw new IllegalArgumentException("() is not a pair");
    } else {
        return new Lot(lo.pair.next);
    }
}

public static Object cadr(Lot lo) {
    return car(cdr(lo));
}

@Contract("_ -> new")
public static @NotNull Lot cdar(Lot lo) {
    Lot tmp = (Lot) car(lo);
    if (isNull(lo)) {
        throw new IllegalArgumentException("() is not a pair");
    } else {
        return new Lot(tmp.pair.next);
    }
}

@Contract("_ -> new")
public static @NotNull Lot cddr(Lot lo) {
    return cdr(cdr(lo));
}

public static Object lotRef(@NotNull Lot lo, int index) {
    if (0 <= index && index < length(lo)) {
        while (0 < index) {
            lo = cdr(lo);
            index = index - 1;
        }
        return car(lo);
    } else {
        throw new IndexOutOfBoundsException
              (String.format("index %d is out of range for lot %s", index, lo));
    }
}

public static Object fewRef(Few fw, int index) {
    if (0 <= index && index < length(fw)) {
        return fw.array[index];
    } else {
        throw new IndexOutOfBoundsException
              (String.format("index %d is out of range for fex[%d %d)", index, 0, length(fw)));
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
//endregion


//region Setter
public static void setCar(@NotNull Lot lo, @NotNull Object value) {
    if (isNull(lo)) {
        throw new IllegalArgumentException("() is not a pair");
    } else {
        lo.pair.data = value;
    }
}

public static void setCdr(@NotNull Lot lo, @NotNull Lot value) {
    if (isNull(lo)) {
        throw new IllegalArgumentException("() is not a pair");
    } else {
        lo.pair.next = value.pair;
    }
}

public static void fewSet(Few fw, int index, Object value) {
    if (0 <= index && index < length(fw)) {
        fw.array[index] = value;
    } else {
        throw new IndexOutOfBoundsException
              (String.format("index %d is out of range for fex[%d %d)", index, 0, length(fw)));
    }
}

public static void set0(Few fw, Object value) {
    fewSet(fw, 0, value);
}

public static void set1(Few fw, Object value) {
    fewSet(fw, 1, value);
}

public static void set2(Few fw, Object value) {
    fewSet(fw, 2, value);
}

public static void set3(Few fw, Object value) {
    fewSet(fw, 3, value);
}

public static void set4(Few fw, Object value) {
    fewSet(fw, 4, value);
}

public static void set5(Few fw, Object value) {
    fewSet(fw, 5, value);
}

public static void fill(@NotNull Few fw, Object value) {
    Arrays.fill(fw.array, value);
}
//endregion


//region Connection
public static @NotNull Lot cons(@NotNull Object obj, @NotNull Lot lo) {
    Pair pair = new Pair(obj, lo.pair);
    return new Lot(pair);
}

public static Lot consMore(Lot l1, Lot l2) {
    if (isNull(l1)) {
        return l2;
    } else {
        return consMore(cdr(l1), cons(car(l1), l2));
    }
}

public static Lot append(@NotNull Lot l1, @NotNull Lot l2) {
    if (isNull(l1)) {
        return l2;
    } else {
        return cons(car(l1), append(cdr(l1), l2));
    }
}
//endregion


//region Copy
public static @NotNull Lot reverse(@NotNull Lot lo) {
    if (isNull(lo)) {
        return lo;
    } else if (Cycle.isTailCyc(lo)) {
        throw new IllegalArgumentException
              (String.format("error: lot is circular\n%s", lo));
    } else {
        Pair moo = new Pair(lo.pair.data, null);
        lo = cdr(lo);
        while (!isNull(lo)) {
            moo = new Pair(lo.pair.data, moo);
            lo = cdr(lo);
        }
        return new Lot(moo);
    }
}

public static Lot lotHead(@NotNull Lot lo, int index) {
    if (Cycle.isTailCyc(lo) || 0 <= index && index <= length(lo)) {
        return PgAid._head(lo, index);
    } else {
        throw new IndexOutOfBoundsException
              (String.format("index %d is out of range for lot %s", index, lo));
    }
}

public static Lot lotTail(@NotNull Lot lo, int index) {
    if (Cycle.isTailCyc(lo) || 0 <= index && index <= length(lo)) {
        return PgAid._tail(lo, index);
    } else {
        throw new IndexOutOfBoundsException
              (String.format("index %d is out of range for lot %s", index, lo));
    }
}

public static Lot copy(@NotNull Lot lo) {
    if (isNull(lo)) {
        return lot();
    } else {
        return cons(car(lo), copy(cdr(lo)));
    }
}

public static void fewCopyInto(@NotNull Few src, int src_pos, @NotNull Few dest,
                               int dest_pos, int amount) {
    System.arraycopy(src.array, src_pos, dest.array, dest_pos, amount);
}

public static @NotNull Few copy(@NotNull Few fw) {
    int sz = length(fw);
    Few moo = makeFew(sz);
    fewCopyInto(fw, 0, moo, 0, sz);
    return moo;
}
//endregion


//region Transformer
public static @NotNull Few lotToFew(@NotNull Lot lo) {
    if (Cycle.isTailCyc(lo)) {
        throw new IllegalArgumentException
              (String.format("error: lot is circular\n%s", lo));
    } else {
        int sz = length(lo);
        Few fw = makeFew(sz);
        for (int i = 0; i < sz; i = i + 1) {
            fewSet(fw, i, car(lo));
            lo = cdr(lo);
        }
        return fw;
    }
}

public static @NotNull Lot fewToLot(@NotNull Few fw) {
    int sz = length(fw);
    if (0 == sz) {
        return lot();
    } else {
        Pair moo = new Pair(ref0(fw), null);
        PgAid.initLot(1, moo, fw.array);
        return new Lot(moo);
    }
}
//endregion


//region Traversal
public static Lot filter(Has pred, Lot lo) {
    if (isNull(lo)) {
        return lot();
    } else if (pred.apply(car(lo))) {
        return cons(car(lo), filter(pred, cdr(lo)));
    } else {
        return filter(pred, cdr(lo));
    }
}

public static Lot lotMap(Do func, Lot lo) {
    if (isNull(lo)) {
        return lot();
    } else {
        Object o = func.apply(car(lo));
        return cons(o, lotMap(func, cdr(lo)));
    }
}

public static @NotNull Few fewMap(Do func, Few fw) {
    int sz = length(fw);
    Few rs = makeFew(sz);
    for (int i = 0; i < sz; i = i + 1) {
        Object r = func.apply(fewRef(fw, i));
        fewSet(rs, i, r);
    }
    return rs;
}
//endregion
}
