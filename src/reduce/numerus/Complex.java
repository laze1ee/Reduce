package reduce.numerus;

import reduce.share.BinaryLabel;
import reduce.utility.CheckSum;


public class Complex extends Numerus {

final Real real;
final Real imaginary;

public Complex(Real real, Real imaginary) {
    this.real = real;
    this.imaginary = imaginary;
}


@Override
public String toString() {
    boolean neg = Arithmetic.realLess(imaginary, Intact.zero);
    if (neg ||
        imaginary == Float64.POS_INF ||
        imaginary == Float64.NEG_INF ||
        imaginary == Float64.NAN) {
        return String.format("%s%si", real, imaginary);
    } else {
        return String.format("%s+%si", real, imaginary);
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Complex co) {
        return real.equals(co.real) &&
               imaginary.equals(co.imaginary);
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
public Complex neg() {
    return new Complex(real.neg(), imaginary.neg());
}

@Override
public byte[] code() {
    byte[] bs1 = real.code();
    byte[] bs2 = imaginary.code();
    byte[] bin = new byte[1 + bs1.length + bs2.length];
    bin[0] = BinaryLabel.COMPLEX;
    System.arraycopy(bs1, 0, bin, 1, bs1.length);
    System.arraycopy(bs2, 0, bin, 1 + bs1.length, bs2.length);
    return bin;
}
}
