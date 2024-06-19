package reduce.numerus;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reduce.share.Share;


public abstract class Numerus {

public abstract Numerus neg();

public abstract byte[] code();


public static boolean isNumerus(@NotNull String str) {
    int sz = str.length();

    int bound = StringMass.rangeComplex(str);
    if (bound == sz) {
        return true;
    } else {
        bound = StringMass.rangeReal(str, 0);
        return bound == sz;
    }
}

public static Numerus parseNumerus(@NotNull String str) {
    int bound = StringMass.rangeComplex(str);
    if (bound == str.length()) {
        return StringMass.parseComplex(str);
    } else {
        return StringMass.parseReal(str);
    }
}


public static boolean isExact(Object datum) {
    if (datum instanceof Intact || datum instanceof Fraction) {
        return true;
    } else if (datum instanceof Complex co) {
        return isExact(co.real) && isExact(co.imaginary);
    } else {
        return false;
    }
}

public static boolean isInexact(Object datum) {
    if (datum instanceof Float64) {
        return true;
    } else if (datum instanceof Complex co) {
        return isInexact(co.real) || isInexact(co.imaginary);
    } else {
        return false;
    }
}

public static boolean isInteger(Object datum) {
    if (datum instanceof Intact) {
        return true;
    } else if (datum instanceof Float64 fl) {
        return fl.isInteger();
    } else {
        return false;
    }
}

public static Numerus exactToInexact(Numerus num) {
    if (num instanceof Intact in) {
        return in.toInexact();
    } else if (num instanceof Fraction fr) {
        return fr.toInexact();
    } else if (num instanceof Complex co) {
        Real re = (Real) exactToInexact(co.real);
        Real img = (Real) exactToInexact(co.imaginary);
        return new Complex(re, img);
    } else {
        return num;
    }
}

public static Object inexactToExact(Numerus num) {
    if (num instanceof Float64 fl) {
        if (fl.isFinite()) {
            return fl.toExact();
        } else {
            return String.format(Msg.NO_EXACT_REP, fl);
        }
    } else if (num instanceof Complex co) {
        Object re = inexactToExact(co.real);
        if (re instanceof String) {
            return re;
        }
        Object img = inexactToExact(co.imaginary);
        if (img instanceof String) {
            return img;
        }
        return new Complex((Real) re, (Real) img);
    } else {
        return num;
    }
}

@Contract("_ -> new")
public static @NotNull Intact intToIntact(int n) {
    byte[] big = Share.integerToBinary(n, 4, false);
    return new Intact(Share.trim(big));
}

public static int intactToInt(@NotNull Intact in) {
    return Big.intOf(in.data);
}


public static boolean eq(Numerus n1, Numerus n2) {
    if (n1 instanceof Intact in1 &&
        n2 instanceof Intact in2) {
        return Big.equal(in1.data, in2.data);
    } else if (n1 instanceof Float64 fl1 &&
               n2 instanceof Float64 fl2) {
        return fl1.data == fl2.data;
    } else {
        return false;
    }
}

public static boolean valueEq(Numerus n1, Numerus n2) {
    if (n1 instanceof Real r1 &&
        n2 instanceof Real r2) {
        return Arithmetic.realEq(r1, r2);
    } else if (n1 instanceof Complex co1 &&
               n2 instanceof Complex co2) {
        return Arithmetic.realEq(co1.real, co2.real) &&
               Arithmetic.realEq(co1.imaginary, co2.imaginary);
    } else {
        return false;
    }
}

public static boolean valueLess(Real r1, Real r2) {
    return Arithmetic.realLess(r1, r2);
}


public static Numerus add(Numerus n1, Numerus n2) {
    if (n1 instanceof Real r1 &&
        n2 instanceof Real r2) {
        return Arithmetic.realAdd(r1, r2);
    } else if (n1 instanceof Complex co1 &&
               n2 instanceof Complex co2) {
        return Arithmetic.complexAdd(co1, co2);
    } else if (n1 instanceof Real re &&
               n2 instanceof Complex co) {
        Real real = Arithmetic.realAdd(re, co.real);
        return new Complex(real, co.imaginary);
    } else {
        return add(n2, n1);
    }
}

public static Numerus mul(Numerus n1, Numerus n2) {
    if (n1 instanceof Real r1 &&
        n2 instanceof Real r2) {
        return Arithmetic.realMul(r1, r2);
    } else if (n1 instanceof Complex co1 &&
               n2 instanceof Complex co2) {
        return Arithmetic.complexMul(co1, co2);
    } else if (n1 instanceof Real re &&
               n2 instanceof Complex co) {
        Real real = Arithmetic.realMul(re, co.real);
        Real img = Arithmetic.realMul(re, co.imaginary);
        return new Complex(real, img);
    } else {
        return mul(n2, n1);
    }
}

public static Numerus div(Numerus n1, Numerus n2) {
    if (n1 instanceof Real r1 &&
        n2 instanceof Real r2) {
        return Arithmetic.realDiv(r1, r2);
    } else if (n1 instanceof Complex co1 &&
               n2 instanceof Complex co2) {
        return Arithmetic.complexDiv(co1, co2);
    } else if (n1 instanceof Real re &&
               n2 instanceof Complex co) {
        if (re.isZero()) {
            return Intact.zero;
        } else {
            Real den = Arithmetic.realAdd(Arithmetic.realMul(co.real, co.real),
                                          Arithmetic.realMul(co.imaginary, co.imaginary));
            Real real = Arithmetic.realMul(re, co.real);
            Real img = Arithmetic.realMul(re, co.imaginary);
            return new Complex(Arithmetic.realDiv(real, den),
                               Arithmetic.realDiv(img.neg(), den));
        }
    } else if (n1 instanceof Complex co &&
               n2 instanceof Real re) {
        Real real = Arithmetic.realDiv(co.real, re);
        Real img = Arithmetic.realDiv(co.imaginary, re);
        return new Complex(real, img);
    } else {
        // redundant clause
        return Intact.zero;
    }
}

public static @NotNull Real quotient(Real r1, Real r2) {
    if (r1 instanceof Intact in1 &&
        r2 instanceof Intact in2) {
        byte[] q = Big.quotient(in1.data, in2.data);
        return new Intact(q);
    } else if (r1 instanceof Float64 fl &&
               r2 instanceof Intact in) {
        Intact in1 = (Intact) fl.toExact();
        byte[] q = Big.quotient(in1.data, in.data);
        return new Float64(Big.doubleOf(q));
    } else if (r1 instanceof Intact in &&
               r2 instanceof Float64 fl) {
        Intact in2 = (Intact) fl.toExact();
        byte[] q = Big.quotient(in.data, in2.data);
        return new Float64(Big.doubleOf(q));
    } else if (r1 instanceof Float64 fl1 &&
               r2 instanceof Float64 fl2) {
        Intact in1 = (Intact) fl1.toExact();
        Intact in2 = (Intact) fl2.toExact();
        byte[] q = Big.quotient(in1.data, in2.data);
        return new Float64(Big.doubleOf(q));
    } else {
        // redundant clause
        return Intact.zero;
    }
}

public static @NotNull Real remainder(Real r1, Real r2) {
    if (r1 instanceof Intact i1 &&
        r2 instanceof Intact i2) {
        byte[] q = Big.remainder(i1.data, i2.data);
        return new Intact(q);
    } else if (r1 instanceof Float64 fl &&
               r2 instanceof Intact in) {
        Intact in1 = (Intact) fl.toExact();
        byte[] q = Big.remainder(in1.data, in.data);
        return new Float64(Big.doubleOf(q));
    } else if (r1 instanceof Intact in &&
               r2 instanceof Float64 fl) {
        Intact in2 = (Intact) fl.toExact();
        byte[] q = Big.remainder(in.data, in2.data);
        return new Float64(Big.doubleOf(q));
    } else if (r1 instanceof Float64 fl1 &&
               r2 instanceof Float64 fl2) {
        Intact in1 = (Intact) fl1.toExact();
        Intact in2 = (Intact) fl2.toExact();
        byte[] q = Big.remainder(in1.data, in2.data);
        return new Float64(Big.doubleOf(q));
    } else {
        // redundant clause
        return Intact.zero;
    }
}

public static Intact shift(Intact in, @NotNull Intact shift) {
    int i = Big.intOf(shift.data);
    if (0 < i) {
        byte[] big = Big.shiftLeft(in.data, i);
        return new Intact(big);
    } else if (i < 0) {
        byte[] big = Big.shiftRight(in.data, i * -1);
        return new Intact(big);
    } else {
        return in;
    }
}

public static @NotNull Intact shiftRightUnsigned(@NotNull Intact in, @NotNull Intact shift) {
    int i = Big.intOf(shift.data);
    byte[] big = Big.shiftRightUnsigned(in.data, i);
    return new Intact(big);
}
}
