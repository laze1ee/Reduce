package share.progressive;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.datetime.Time;
import share.refmethod.Do;
import share.refmethod.Has;

import java.util.Arrays;


public class Pg {

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
public static boolean isNull(@NotNull Lot lt) {
    return lt.pair == null;
}
//endregion


//region Visitor
public static int length(@NotNull Lot lt) {
    if (Cycle.isTailCircular(lt)) {
        throw new RuntimeException(String.format(Shop.CIRCULAR, lt));
    } else {
        int n = 0;
        while (!isNull(lt)) {
            n = n + 1;
            lt = cdr(lt);
        }
        return n;
    }
}

public static int length(@NotNull Few fw) {
    return fw.array.length;
}

public static Object car(@NotNull Lot lt) {
    if (isNull(lt)) {
        throw new RuntimeException(Shop.LOT_NULL);
    } else {
        return lt.pair.data;
    }
}

public static Object caar(Lot lt) {
    Lot tmp = (Lot) car(lt);
    if (isNull(tmp)) {
        throw new RuntimeException(Shop.LOT_NULL);
    } else {
        return tmp.pair.data;
    }
}

@Contract("_ -> new")
public static @NotNull Lot cdr(@NotNull Lot lt) {
    if (isNull(lt)) {
        throw new RuntimeException(Shop.LOT_NULL);
    } else {
        return new Lot(lt.pair.next);
    }
}

public static Object cadr(Lot lt) {
    return car(cdr(lt));
}

@Contract("_ -> new")
public static @NotNull Lot cdar(Lot lt) {
    Lot tmp = (Lot) car(lt);
    if (isNull(lt)) {
        throw new RuntimeException(Shop.LOT_NULL);
    } else {
        return new Lot(tmp.pair.next);
    }
}

@Contract("_ -> new")
public static @NotNull Lot cddr(Lot lt) {
    return cdr(cdr(lt));
}

public static Object lotRef(@NotNull Lot lt, int index) {
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
//endregion


//region Setter
public static void setCar(@NotNull Lot lt, @NotNull Object datum) {
    if (isNull(lt)) {
        throw new RuntimeException(Shop.LOT_NULL);
    } else {
        lt.pair.data = datum;
    }
}

public static void setCdr(@NotNull Lot lt, @NotNull Lot datum) {
    if (isNull(lt)) {
        throw new RuntimeException(Shop.LOT_NULL);
    } else {
        lt.pair.next = datum.pair;
    }
}

public static void fewSet(Few fw, int index, Object datum) {
    if (0 <= index && index < length(fw)) {
        fw.array[index] = datum;
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

public static void fill(@NotNull Few fw, Object datum) {
    Arrays.fill(fw.array, datum);
}
//endregion


//region Connection
public static @NotNull Lot cons(@NotNull Object datum, @NotNull Lot lt) {
    Pair pair = new Pair(datum, lt.pair);
    return new Lot(pair);
}

public static Lot consMore(Lot lt1, Lot lt2) {
    Lot moo = lt1;
    Lot xoo = lt2;
    while (!isNull(moo)) {
        xoo = cons(car(moo), xoo);
        moo = cdr(moo);
    }
    return xoo;
}

public static Lot append(@NotNull Lot lt1, @NotNull Lot lt2) {
    Lot moo = reverse(lt1);
    return consMore(moo, lt2);
}
//endregion


//region Copy
public static Lot reverse(Lot lt) {
    if (isNull(lt)) {
        return lt;
    } else if (Cycle.isTailCircular(lt)) {
        throw new RuntimeException(String.format(Shop.CIRCULAR, lt));
    } else {
        Pair pair = new Pair(lt.pair.data, null);
        Lot moo = cdr(lt);
        while (!isNull(moo)) {
            pair = new Pair(moo.pair.data, pair);
            moo = cdr(moo);
        }
        return new Lot(pair);
    }
}

public static Lot lotHead(Lot lt, int amount) {
    if (Cycle.isTailCircular(lt) ||
        0 <= amount && amount <= length(lt)) {
        Lot moo = lt;
        Lot xoo = lot();
        for (int i = amount; 0 < i; i = i - 1) {
            xoo = cons(car(moo), xoo);
            moo = cdr(moo);
        }
        return reverse(xoo);
    } else {
        throw new RuntimeException(String.format(Shop.LOT_INDEX_OUT, amount, lt));
    }
}

public static Lot lotTail(Lot lt, int amount) {
    if (Cycle.isTailCircular(lt) ||
        0 <= amount && amount <= length(lt)) {
        Lot moo = lt;
        for (int i = amount; 0 < i; i = i - 1) {
            moo = cdr(moo);
        }
        return moo;
    } else {
        throw new RuntimeException(String.format(Shop.LOT_INDEX_OUT, amount, lt));
    }
}

public static Lot copy(Lot lt) {
    if (isNull(lt)) {
        return lt;
    } else if (Cycle.isTailCircular(lt)) {
        throw new RuntimeException(String.format(Shop.CIRCULAR, lt));
    } else {
        Pair pair = new Pair(lt.pair.data, null);
        Pair bair = pair;
        Lot moo = cdr(lt);
        while (!isNull(moo)) {
            bair.next = new Pair(car(moo), null);
            bair = bair.next;
            moo = cdr(moo);
        }
        return new Lot(pair);
    }
}

public static void fewCopyInto(@NotNull Few src, int src_pos, @NotNull Few dest,
                               int dest_pos, int amount) {
    System.arraycopy(src.array, src_pos, dest.array, dest_pos, amount);
}

public static @NotNull Few copy(@NotNull Few fw) {
    int sz = length(fw);
    Few moo = makeFew(sz, 0);
    fewCopyInto(fw, 0, moo, 0, sz);
    return moo;
}
//endregion


//region Transformer
public static @NotNull Few lotToFew(@NotNull Lot lt) {
    if (Cycle.isTailCircular(lt)) {
        throw new RuntimeException(String.format("%s is circular", lt));
    } else {
        Lot moo = lt;
        int sz = length(moo);
        Few fw = makeFew(sz);
        for (int i = 0; i < sz; i = i + 1) {
            fewSet(fw, i, car(moo));
            moo = cdr(moo);
        }
        return fw;
    }
}

public static @NotNull Lot fewToLot(@NotNull Few fw) {
    Pair pair = Aid.initLot(fw.array);
    return new Lot(pair);
}
//endregion


//region Traversal
public static Lot filter(Has pred, Lot lt) {
    Lot moo = lt;
    Lot xoo = lot();
    while (!isNull(moo)) {
        if (pred.apply(car(moo))) {
            xoo = cons(car(moo), xoo);
        }
        moo = cdr(moo);
    }
    return reverse(xoo);
}

public static Lot lotMap(Do func, Lot lt) {
    Lot moo = lt;
    Lot xoo = lot();
    while (!isNull(moo)) {
        Object datum = func.apply(car(moo));
        xoo = cons(datum, xoo);
        moo = cdr(moo);
    }
    return reverse(xoo);
}

public static @NotNull Few fewMap(Do func, Few fw) {
    int sz = length(fw);
    Few moo = makeFew(sz);
    for (int i = 0; i < sz; i = i + 1) {
        Object datum = func.apply(fewRef(fw, i));
        fewSet(moo, i, datum);
    }
    return moo;
}
//endregion


//region Comparison
public static boolean eq(Object o1, Object o2) {
    if (o1 == o2) {
        return true;
    } else if (o1 instanceof Boolean b1 &&
               o2 instanceof Boolean b2) {
        return (b1 && b2) ||
               !(b1 || b2);
    } else if (o1 instanceof Number &&
               o2 instanceof Number) {
        if (o1 instanceof Integer i1 &&
            o2 instanceof Integer i2) {
            return ((int) i1) == i2;
        } else if (o1 instanceof Long l1 &&
                   o2 instanceof Long l2) {
            return ((long) l1) == l2;
        } else if (o1 instanceof Double d1 &&
                   o2 instanceof Double d2) {
            return ((double) d1) == d2;
        } else {
            return false;
        }
    } else if (o1 instanceof Character c1 &&
               o2 instanceof Character c2) {
        return ((char) c1) == c2;
    } else if (o1 instanceof Symbol sym1 &&
               o2 instanceof Symbol sym2) {
        return sym1.id == sym2.id;
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
            throw new RuntimeException(String.format(Shop.UNSUPPORTED, o1, o2));
        }
    } else {
        return o1.equals(o2);
    }
}

public static boolean numberEq(Number n1, Number n2) {
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
        return sym1.id < sym2.id;
    } else if (o1 instanceof Number n1 &&
               o2 instanceof Number n2) {
        if (n1 instanceof Double ||
            n2 instanceof Double) {
            return n1.doubleValue() < n2.doubleValue();
        } else {
            return n1.longValue() < n2.longValue();
        }
    } else if (o1 instanceof String s1 &&
               o2 instanceof String s2) {
        int m = s1.compareTo(s2);
        return m < 0;
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
            throw new RuntimeException(String.format(Shop.UNSUPPORTED, o1, o2));
        }
    } else if (o1 instanceof Time t1 &&
               o2 instanceof Time t2) {
        return Aid.timeLessThan(t1, t2);
    } else {
        throw new RuntimeException(String.format("%s and %s cannot be compared in size", o1, o2));
    }
}

public static boolean greater(Object o1, Object o2) {
    if (o1 instanceof Symbol sym1 &&
        o2 instanceof Symbol sym2) {
        return sym1.id > sym2.id;
    } else if (o1 instanceof Number n1 &&
               o2 instanceof Number n2) {
        if (n1 instanceof Double ||
            n2 instanceof Double) {
            return n1.doubleValue() > n2.doubleValue();
        } else {
            return n1.longValue() > n2.longValue();
        }
    } else if (o1 instanceof String s1 &&
               o2 instanceof String s2) {
        int m = s1.compareTo(s2);
        return m > 0;
    } else if (o1.getClass().isArray() &&
               o2.getClass().isArray()) {
        if (o1 instanceof boolean[] bs1 &&
            o2 instanceof boolean[] bs2) {
            int r = Arrays.compare(bs1, bs2);
            return r > 0;
        } else if (o1 instanceof byte[] bs1 &&
                   o2 instanceof byte[] bs2) {
            int r = Arrays.compare(bs1, bs2);
            return r > 0;
        } else if (o1 instanceof int[] ins1 &&
                   o2 instanceof int[] ins2) {
            int r = Arrays.compare(ins1, ins2);
            return r > 0;
        } else if (o1 instanceof long[] ls1 &&
                   o2 instanceof long[] ls2) {
            int r = Arrays.compare(ls1, ls2);
            return r > 0;
        } else if (o1 instanceof double[] ds1 &&
                   o2 instanceof double[] ds2) {
            int r = Arrays.compare(ds1, ds2);
            return r > 0;
        } else {
            throw new RuntimeException(String.format(Shop.UNSUPPORTED, o1, o2));
        }
    } else if (o1 instanceof Time t1 &&
               o2 instanceof Time t2) {
        return Aid.timeLessThan(t2, t1);
    } else {
        throw new RuntimeException(String.format("%s and %s cannot be compared in size", o1, o2));
    }
}
//endregion
}
