package reduce.numerus;

import reduce.share.BinaryLabel;
import reduce.utility.CheckSum;


public class Fraction extends Real {

final byte[] numerator;
final byte[] denominator;

public Fraction(byte[] numerator, byte[] denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
}


@Override
public String toString() {
    return String.format("%s/%s", Big.stringOf(numerator), Big.stringOf(denominator));
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Fraction fr) {
        return Big.equal(numerator, fr.numerator) &&
               Big.equal(denominator, fr.denominator);
    } else {
        return false;
    }
}

@Override
public int hashCode() {
    byte[] bin = code();
    return CheckSum.fletcher32(bin);
}

@Override
public Fraction neg() {
    return new Fraction(Big.neg(numerator), denominator);
}

@Override
boolean isZero() {
    return false;
}

Float64 toInexact() {
    double num = Big.doubleOf(numerator);
    double den = Big.doubleOf(denominator);
    return new Float64(num / den);
}

Real simplify() {
    if (Big.equal(numerator, Big.zero)) {
        return Intact.zero;
    } else if (Big.equal(denominator, Big.one)) {
        return new Intact(numerator);
    } else {
        byte[] divisor = Big.gcd(numerator, denominator);
        if (Big.equal(divisor, denominator)) {
            return new Intact(Big.quotient(numerator, divisor));
        } else if (Big.equal(divisor, Big.one)) {
            return this;
        } else {
            byte[] num = Big.quotient(numerator, divisor);
            byte[] den = Big.quotient(denominator, divisor);
            return new Fraction(num, den);
        }
    }
}

Fraction inverse() {
    if (Big.less(numerator, Big.zero)) {
        byte[] num = Big.neg(denominator);
        byte[] den = Big.neg(numerator);
        return new Fraction(num, den);
    } else {
        return new Fraction(denominator, numerator);
    }
}

@Override
public byte[] code() {
    byte[] num = new Intact(numerator).code();
    byte[] den = new Intact(denominator).code();
    byte[] bin = new byte[1 + num.length + den.length - 2];
    bin[0] = BinaryLabel.FRACTION;
    System.arraycopy(num, 1, bin, 1, num.length - 1);
    System.arraycopy(den, 1, bin, num.length, den.length - 1);
    return bin;
}
}
