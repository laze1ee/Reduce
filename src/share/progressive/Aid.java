package share.progressive;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.datetime.Time;

import static share.progressive.Pg.isNull;
import static share.utility.Ut.stringOf;


class Aid {

static @NotNull Pair initLot(Object @NotNull [] args) {
    Pair pair = new Pair(args[0], null);
    Pair moo = pair;
    int n = args.length;
    for (int i = 1; i < n; i = i + 1) {
        moo.next = new Pair(args[i], null);
        moo = moo.next;
    }
    return pair;
}


static @NotNull String consObjectArray(Object @NotNull [] arr) {
    int n = arr.length;
    if (n == 0) {
        return "";
    } else {
        StringBuilder str = new StringBuilder();
        n = n - 1;
        for (int i = 0; i < n; i = i + 1) {
            str.append(stringOf(arr[i]));
            str.append(" ");
        }
        str.append(stringOf(arr[n]));
        return str.toString();
    }
}


static Object isolate(Object datum) {
    if (datum instanceof Few fw) {
        return isolateFew(fw.array);
    } else if (datum instanceof Lot lt) {
        if (isNull(lt)) {
            return new PairNull();
        } else {
            return isolateLot(lt.pair);
        }
    } else {
        return datum;
    }
}

@Contract("_ -> new")
private static @NotNull Fix isolateFew(Object @NotNull [] arr) {
    int sz = arr.length;
    Object[] moo = new Object[sz];
    for (int i = 0; i < sz; i = i + 1) {
        moo[i] = isolate(arr[i]);
    }
    return new FixNonCyc(moo);
}

@Contract("_ -> new")
private static @NotNull Pair isolateLot(@NotNull Pair pair) {
    return new PairHead(isolate(pair.data), processCdr(pair.next));
}

private static Pair processCdr(Pair pair) {
    if (pair == null) {
        return null;
    } else {
        return new Pair(isolate(pair.data), processCdr(pair.next));
    }
}


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


static boolean isObjectArrayEqual(Object @NotNull [] arr1, Object @NotNull [] arr2) {
    if (arr1.length == arr2.length) {
        int i = 0;
        while (i < arr1.length &&
               Pg.equal(arr1[i], arr2[i])) {
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
}
