package share.utility;

import org.jetbrains.annotations.NotNull;
import share.progressive.Few;
import share.progressive.Lot;
import share.refmethod.Eqv;

import java.util.Random;

import static share.progressive.Pr.*;


public class Ut {

//region String Of
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
            str.append(Aid.hex(bs[i]));
            str.append(" ");
        }
        str.append(Aid.hex(bs[n]));
        str.append(")");
        return str.toString();
    }
}
//endregion


//region Lot
public static boolean isBelong(Object datum, Lot lt) {
    Lot moo = lt;
    while (!isNull(moo) &&
           !eq(datum, car(moo))) {
        moo = cdr(moo);
    }
    return !isNull(moo);
}

public static boolean isBelong(Eqv pred, Object datum, Lot lt) {
    Lot moo = lt;
    while (!isNull(moo) &&
           !pred.apply(datum, car(moo))) {
        moo = cdr(moo);
    }
    return !isNull(moo);
}

public static Lot removeDup(Lot lt) {
    Lot moo = lt;
    Lot xoo = lot();
    while (!isNull(moo)) {
        if (!isBelong(car(moo), cdr(moo))) {
            xoo = cons(car(moo), xoo);
        }
        moo = cdr(moo);
    }
    return reverse(xoo);
}
//endregion


//region Few
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
}
