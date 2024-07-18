package reduce.numerus;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reduce.progressive.Few;

import static reduce.progressive.Pr.*;


class Arithmetic {

@Contract("_, _ -> new")
private static @NotNull Real fractionAdd(@NotNull Fraction fr1, @NotNull Fraction fr2) {
    if (Big.equal(fr1.denominator, fr2.denominator)) {
        Fraction fr = new Fraction(Big.add(fr1.numerator, fr2.numerator),
                                   fr1.denominator);
        return fr.simplify();
    } else {
        byte[] num = Big.add(Big.mul(fr1.numerator, fr2.denominator),
                             Big.mul(fr2.numerator, fr1.denominator));
        byte[] den = Big.mul(fr1.denominator, fr2.denominator);
        Fraction fr = new Fraction(num, den);
        return fr.simplify();
    }
}

static boolean realValueEqual(Real r1, Real r2) {
    if (r1 instanceof Intact i1) {
        if (r2 instanceof Intact i2) {
            return Big.equal(i1.data, i2.data);
        } else if (r2 instanceof Float64 fl &&
                   Double.isFinite(fl.data)) {
            return realValueEqual(i1, fl.toExact());
        } else {
            return false;
        }
    } else if (r1 instanceof Fraction fr1) {
        if (r2 instanceof Fraction fr2) {
            return Big.equal(fr1.numerator, fr2.numerator) &&
                   Big.equal(fr1.denominator, fr2.denominator);
        } else if (r2 instanceof Float64 fl &&
                   Double.isFinite(fl.data)) {
            return realValueEqual(fr1, fl.toExact());
        } else {
            return false;
        }
    } else {
        Float64 fl1 = (Float64) r1;
        if (r2 instanceof Float64 fl2) {
            return fl1.data == fl2.data;
        } else {
            return realValueEqual(r2, fl1);
        }
    }
}

static boolean realValueLessThan(Real r1, Real r2) {
    if (r1 instanceof Intact in1) {
        if (r2 instanceof Intact in2) {
            return Big.less(in1.data, in2.data);
        } else if (r2 instanceof Fraction fr) {
            Few answer = Big.div(fr.numerator, fr.denominator);
            byte[] q = (byte[]) ref0(answer);
            byte[] r = (byte[]) ref1(answer);
            if (Big.equal(in1.data, q)) {
                return Big.less(Big.zero, r);
            } else {
                return Big.less(in1.data, q);
            }
        } else if (r2 instanceof Float64 fl &&
                   Double.isFinite(fl.data)) {
            return realValueLessThan(r1, fl.toExact());
        } else {
            return r2 == Float64.POS_INF;
        }
    } else if (r1 instanceof Fraction fr1) {
        if (r2 instanceof Intact) {
            return !realValueLessThan(r2, r1);
        } else if (r2 instanceof Fraction fr2) {
            if (Big.equal(fr1.denominator, fr2.denominator)) {
                return Big.less(fr1.numerator, fr2.numerator);
            } else {
                byte[] num1 = Big.mul(fr1.numerator, fr2.denominator);
                byte[] num2 = Big.mul(fr2.numerator, fr1.denominator);
                return Big.less(num1, num2);
            }
        } else if (r2 instanceof Float64 fl &&
                   Double.isFinite(fl.data)) {
            return realValueLessThan(r1, fl.toExact());
        } else {
            return r2 == Float64.POS_INF;
        }
    } else {
        Float64 fl1 = (Float64) r1;
        if (r2 instanceof Intact || r2 instanceof Fraction) {
            if (Double.isFinite(fl1.data)) {
                return realValueLessThan(fl1.toExact(), r2);
            } else {
                return fl1 == Float64.NEG_INF;
            }
        } else {
            Float64 fl2 = (Float64) r2;
            return fl1.data < fl2.data;
        }
    }
}


static Real realAdd(Real r1, Real r2) {
    if (r1 instanceof Intact i1) {
        if (r2 instanceof Intact i2) {
            return new Intact(Big.add(i1.data, i2.data));
        } else if (r2 instanceof Fraction fr) {
            byte[] sum = Big.add(Big.mul(i1.data, fr.denominator),
                                 fr.numerator);
            return new Fraction(sum, fr.denominator);
        } else {
            double d = Big.doubleOf(i1.data);
            Float64 fl = (Float64) r2;
            return new Float64(d + fl.data);
        }
    } else if (r1 instanceof Fraction fr1) {
        if (r2 instanceof Fraction fr2) {
            return fractionAdd(fr1, fr2);
        } else if (r2 instanceof Float64 fl) {
            double num = Big.doubleOf(fr1.numerator);
            double den = Big.doubleOf(fr1.denominator);
            return new Float64(num / den + fl.data);
        } else {
            return realAdd(r2, r1);
        }
    } else {
        Float64 fl1 = (Float64) r1;
        if (r2 instanceof Float64 fl2) {
            return new Float64(fl1.data + fl2.data);
        } else {
            return realAdd(r2, r1);
        }
    }
}

static Real realMul(Real r1, Real r2) {
    if (r1 instanceof Intact in1) {
        if (r2 instanceof Intact in2) {
            return new Intact(Big.mul(in1.data, in2.data));
        } else if (r2 instanceof Fraction fr) {
            Fraction fr2 = new Fraction(Big.mul(in1.data, fr.numerator),
                                        fr.denominator);
            return fr2.simplify();
        } else {
            double d = Big.doubleOf(in1.data);
            Float64 fl = (Float64) r2;
            return new Float64(d * fl.data);
        }
    } else if (r1 instanceof Fraction fr1) {
        if (r2 instanceof Fraction fr2) {
            Fraction fr3 = new Fraction(Big.mul(fr1.numerator, fr2.numerator),
                                        Big.mul(fr1.denominator, fr2.denominator));
            return fr3.simplify();
        } else if (r2 instanceof Float64 fl) {
            double num = Big.doubleOf(fr1.numerator);
            double den = Big.doubleOf(fr1.denominator);
            return new Float64(num / den * fl.data);
        } else {
            return realMul(r2, r1);
        }
    } else {
        Float64 fl1 = (Float64) r1;
        if (r2 instanceof Float64 fl2) {
            return new Float64(fl1.data * fl2.data);
        } else {
            return realMul(r2, r1);
        }
    }
}

static Real realDiv(Real r1, Real r2) {
    if (r1 instanceof Intact in1) {
        if (r2 instanceof Intact in2) {
            byte[] num = in1.data;
            byte[] den = in2.data;
            if (Big.less(den, Big.zero)) {
                num = Big.neg(num);
                den = Big.neg(den);
            }
            Fraction fr = new Fraction(num, den);
            return fr.simplify();
        } else if (r2 instanceof Fraction fr) {
            Fraction fr2 = fr.inverse();
            return realMul(r1, fr2);
        } else {
            double d = Big.doubleOf(in1.data);
            Float64 fl = (Float64) r2;
            return new Float64(d / fl.data);
        }
    } else if (r1 instanceof Fraction fr1) {
        if (r2 instanceof Intact in) {
            Fraction fr2 = new Fraction(Big.one, in.data);
            return realMul(fr1, fr2);
        } else if (r2 instanceof Fraction fr2) {
            return realMul(fr1, fr2.inverse());
        } else {
            double num = Big.doubleOf(fr1.numerator);
            double den = Big.doubleOf(fr1.denominator);
            Float64 fl = (Float64) r2;
            return new Float64(num / den / fl.data);
        }
    } else {
        Float64 fl1 = (Float64) r1;
        if (r2 instanceof Intact in) {
            double d = Big.doubleOf(in.data);
            return new Float64(fl1.data / d);
        } else if (r2 instanceof Fraction fr) {
            double num = Big.doubleOf(fr.numerator);
            double den = Big.doubleOf(fr.denominator);
            return new Float64(fl1.data * den / num);
        } else {
            Float64 fl2 = (Float64) r2;
            return new Float64(fl1.data / fl2.data);
        }
    }
}


static Numerus complexAdd(@NotNull Complex co1, @NotNull Complex co2) {
    Real real = realAdd(co1.real, co2.real);
    Real img = realAdd(co1.imaginary, co2.imaginary);
    if (img.isZero()) {
        return real;
    } else {
        return new Complex(real, img);
    }
}

static @NotNull Numerus complexMul(@NotNull Complex co1, @NotNull Complex co2) {
    Real real = realAdd(realMul(co1.real, co2.real),
                        realMul(co1.imaginary, co2.imaginary).neg());
    Real img = realAdd(realMul(co1.real, co2.imaginary),
                       realMul(co2.real, co1.imaginary));
    if (img.isZero()) {
        return real;
    } else {
        return new Complex(real, img);
    }
}

static @NotNull Numerus complexDiv(@NotNull Complex co1, @NotNull Complex co2) {
    Real den = realAdd(realMul(co2.real, co2.real),
                       realMul(co2.imaginary, co2.imaginary));
    Real real = realAdd(realMul(co1.real, co2.real),
                        realMul(co1.imaginary, co2.imaginary));
    Real img = realAdd(realMul(co1.imaginary, co2.real),
                       realMul(co1.real, co2.imaginary).neg());
    return new Complex(realDiv(real, den), realDiv(img, den));
}
}
