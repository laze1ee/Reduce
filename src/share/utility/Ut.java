package share.utility;

import org.jetbrains.annotations.NotNull;
import share.progressive.Few;
import share.progressive.Lot;
import share.refmethod.Eqv;

import java.util.Random;

import static share.progressive.Comparison.eq;
import static share.progressive.Pg.*;


public class Ut {

@SuppressWarnings("SpellCheckingInspection")
private static final char[] CHARS_SET =
"_-ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

public static @NotNull String randomString(int length) {
    Random r = new Random();
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < length; i = i + 1) {
        int index = r.nextInt(CHARS_SET.length);
        char c = CHARS_SET[index];
        s.append(c);
    }
    return s.toString();
}


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
