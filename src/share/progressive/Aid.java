package share.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.datetime.Time;

import java.lang.reflect.Array;
import java.util.Arrays;

import static share.progressive.Pr.*;
import static share.utility.Ut.fewIndex;


class Aid {

//region Lot
static Lot appending(Lot lt1, Lot lt2) {
    if (isNull(lt1)) {
        return lt2;
    } else {
        return cons(car(lt1), appending(cdr(lt1), lt2));
    }
}

static Lot heading(Lot lt, int index) {
    if (index == 0) {
        return lot();
    } else {
        return cons(car(lt), heading(cdr(lt), index - 1));
    }
}

static Lot tailing(Lot lt, int index) {
    if (index == 0) {
        return lt;
    } else {
        return tailing(cdr(lt), index - 1);
    }
}

static Lot copying(Lot lt) {
    if (isNull(lt)) {
        return lot();
    } else {
        return cons(car(lt), copying(cdr(lt)));
    }
}

@Contract("null -> new")
static @NotNull Pair isolate(Pair pair) {
    if (pair instanceof PairTail) {
        return new PairTail();
    } else {
        PairUse use = (PairUse) pair;
        return new PairCons(use.data, isolate(use.next));
    }
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


//region Comparison
static boolean numberEq(Number n1, Number n2) {
    if (n1 instanceof Integer i1 &&
        n2 instanceof Integer i2) {
        return (int) i1 == i2;
    } else if (n1 instanceof Long l1 &&
               n2 instanceof Long l2) {
        return (long) l1 == l2;
    } else if (n1 instanceof Double d1 &&
               n2 instanceof Double d2) {
        return (double) d1 == d2;
    } else {
        return false;
    }
}

static boolean numberLess(Number n1, Number n2) {
    if (n1 instanceof Integer i1 &&
        n2 instanceof Integer i2) {
        return (int) i1 < i2;
    } else if (n1 instanceof Long l1 &&
               n2 instanceof Long l2) {
        return (long) l1 < l2;
    } else if (n1 instanceof Double d1 &&
               n2 instanceof Double d2) {
        return (double) d1 < d2;
    } else {
        return false;
    }
}

static boolean arrayEqual(Object arr1, Object arr2) {
    if (arr1 instanceof boolean[] bs1 &&
        arr2 instanceof boolean[] bs2) {
        int r = Arrays.compare(bs1, bs2);
        return r == 0;
    } else if (arr1 instanceof byte[] bs1 &&
               arr2 instanceof byte[] bs2) {
        int r = Arrays.compare(bs1, bs2);
        return r == 0;
    } else if (arr1 instanceof int[] ins1 &&
               arr2 instanceof int[] ins2) {
        int r = Arrays.compare(ins1, ins2);
        return r == 0;
    } else if (arr1 instanceof long[] ls1 &&
               arr2 instanceof long[] ls2) {
        int r = Arrays.compare(ls1, ls2);
        return r == 0;
    } else if (arr1 instanceof double[] ds1 &&
               arr2 instanceof double[] ds2) {
        int r = Arrays.compare(ds1, ds2);
        return r == 0;
    } else {
        throw new RuntimeException(String.format(Shop.UNSUPPORTED_COMPARE, arr1, arr2));
    }
}

static boolean objectArrEqual(Object @NotNull [] arr1, Object @NotNull [] arr2) {
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

static boolean arrayLess(Object arr1, Object arr2) {
    if (arr1 instanceof boolean[] bs1 &&
        arr2 instanceof boolean[] bs2) {
        int r = Arrays.compare(bs1, bs2);
        return r < 0;
    } else if (arr1 instanceof byte[] bs1 &&
               arr2 instanceof byte[] bs2) {
        int r = Arrays.compare(bs1, bs2);
        return r < 0;
    } else if (arr1 instanceof int[] ins1 &&
               arr2 instanceof int[] ins2) {
        int r = Arrays.compare(ins1, ins2);
        return r < 0;
    } else if (arr1 instanceof long[] ls1 &&
               arr2 instanceof long[] ls2) {
        int r = Arrays.compare(ls1, ls2);
        return r < 0;
    } else if (arr1 instanceof double[] ds1 &&
               arr2 instanceof double[] ds2) {
        int r = Arrays.compare(ds1, ds2);
        return r < 0;
    } else {
        throw new RuntimeException(String.format(Shop.UNSUPPORTED_COMPARE, arr1, arr2));
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


//region String Of
private static final Few char_code = few(0, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0x1B, 0x20, 0x7F);
private static final String[] char_name = {"nul", "alarm", "backspace", "tab", "newline", "vtab",
                                           "page", "return", "esc", "space", "delete"};
@SuppressWarnings("SpellCheckingInspection")
private static final char[] HEX_STR = "0123456789ABCDEF".toCharArray();

static String stringOfChar(char c) {
    int i = fewIndex(Pr::eq, (int) c, char_code);
    if (0 <= i) {
        return String.format("#\\%s", char_name[i]);
    } else if (Character.isISOControl(c)) {
        return String.format("#\\u%X", (int) c);
    } else {
        return String.format("#\\%c", c);
    }
}

static String stringOfArray(@NotNull Object array) {
    if (array instanceof byte[] bs) {
        return String.format("#i8(%s)", consArray(bs, bs.length));
    } else if (array instanceof int[] ins) {
        return String.format("#i32(%s)", consArray(ins, ins.length));
    } else if (array instanceof long[] ls) {
        return String.format("#i64(%s)", consArray(ls, ls.length));
    } else if (array instanceof double[] ds) {
        return String.format("#r64(%s)", consArray(ds, ds.length));
    } else if (array instanceof char[] cs) {
        return String.format("#char(%s)", consArray(cs, cs.length));
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
}
