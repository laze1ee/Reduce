package share.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.utility.Ut;

import static share.progressive.Pg.*;


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


//region StringOf
static @NotNull String stringOfArray(Object @NotNull [] arr) {
    int sz = arr.length - 1;
    if (sz == -1) {
        return "";
    } else {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < sz; i = i + 1) {
            str.append(stringOf(arr[i]));
            str.append(" ");
        }
        str.append(stringOf(arr[sz]));
        return str.toString();
    }
}

private static final Few char_code =
few(0, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0x1B, 0x20, 0x7F);
private static final Few char_name =
few("nul", "alarm", "backspace", "tab", "newline", "vtab", "page", "return", "esc", "space", "delete");

static String stringOfChar(char c) {
    int i = Ut.fewIndex(Comparison::eq, (int) c, char_code);
    if (0 <= i) {
        return String.format("#\\%s", fewRef(char_name, i));
    } else if (Character.isISOControl(c)) {
        return String.format("#\\u%X", (int) c);
    } else {
        return String.format("#\\%c", c);
    }
}

static Object isolate(Object datum) {
    if (datum instanceof Few fw) {
        return isolateFex(fw.array);
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
private static @NotNull Fix isolateFex(Object @NotNull [] arr) {
    int sz = arr.length;
    Object[] moo = new Object[sz];
    for (int i = 0; i < sz; i = i + 1) {
        moo[i] = isolate(arr[i]);
    }
    return new FixNonCyc(moo);
}

@Contract("_ -> new")
private static @NotNull Pair isolateLot(@NotNull Pair pair) {
    return new PairHead(isolate(pair.data), _processCdr(pair.next));
}

private static Pair _processCdr(Pair pair) {
    if (pair == null) {
        return null;
    } else {
        return new Pair(isolate(pair.data), _processCdr(pair.next));
    }
}
//endregion


//region For Symbol
static final char[] occupant1 = "+-.@".toCharArray();
static final char[] occupant2 = "\"#'(),;[\\]{|}".toCharArray();

static boolean charExist(char c, char @NotNull [] charset) {
    int sz = charset.length;
    int i = 0;
    while (i < sz && c != charset[i]) {
        i = i + 1;
    }
    return i < sz;
}

static boolean isScalar(char c) {
    return c <= 0x1F || Character.isWhitespace(c) || charExist(c, occupant2);
}
//endregion
}
