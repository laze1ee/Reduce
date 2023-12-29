package share.utility;

import org.jetbrains.annotations.NotNull;
import share.progressive.Few;
import share.progressive.Lot;
import share.refmethod.Eqv;

import java.util.Random;

import static share.progressive.Cmp.eq;
import static share.progressive.Pg.*;


public class Ut {

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
public static boolean isBelong(Object o, Lot lt) {
    if (isNull(lt)) {
        return false;
    } else if (eq(o, car(lt))) {
        return true;
    } else {
        return isBelong(o, cdr(lt));
    }
}

public static boolean isBelong(Eqv pred, Object o, Lot lt) {
    if (isNull(lt)) {
        return false;
    } else if (pred.apply(o, car(lt))) {
        return true;
    } else {
        return isBelong(pred, o, cdr(lt));
    }
}

public static Lot removeDup(Lot lt) {
    if (isNull(lt)) {
        return lot();
    } else if (isBelong(car(lt), cdr(lt))) {
        return removeDup(cdr(lt));
    } else {
        return cons(car(lt), removeDup(cdr(lt)));
    }
}

public static Lot remove(Object o, Lot lt) {
    if (isNull(lt)) {
        return lot();
    } else if (eq(o, car(lt))) {
        return cdr(lt);
    } else {
        return cons(car(lt), remove(o, cdr(lt)));
    }
}
//endregion


//region Few
public static int fewIndex(Eqv pred, Object o, Few fw) {
    int sz = length(fw);
    int i = 0;
    while (i < sz &&
           !pred.apply(o, fewRef(fw, i))) {
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
