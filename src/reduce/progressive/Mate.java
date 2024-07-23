package reduce.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reduce.datetime.Time;

import java.lang.reflect.Array;

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
static boolean less(Object o1, Object o2) {
    return (int) o1 < (int) o2;
}

static boolean greater(Object o1, Object o2) {
    return (int) o1 > (int) o2;
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
}
