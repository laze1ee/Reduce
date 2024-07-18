package reduce.numerus;

import reduce.share.Share;
import reduce.share.BinaryLabel;
import reduce.utility.CheckSum;


public class Float64 extends Real {

public static final Float64 POS_INF = new Float64(Double.POSITIVE_INFINITY);
public static final Float64 NEG_INF = new Float64(Double.NEGATIVE_INFINITY);
public static final Float64 NAN = new Float64(Double.NaN);

final double data;

public Float64(double data) {
    this.data = data;
}


@Override
public String toString() {
    if (Double.isFinite(data)) {
        return String.format("%s", data);
    } else if (data == POS_INF.data) {
        return Msg.POS_INF;
    } else if (data == NEG_INF.data) {
        return Msg.NEG_INF;
    } else {
        return Msg.NAN;
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Float64 fl) {
        return data == fl.data;
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
public Float64 neg() {
    return new Float64(data * -1);
}

@Override
boolean isZero() {
    return data == 0;
}

public boolean isFinite() {
    return Double.isFinite(data);
}

public boolean isInfinite() {
    return Double.isInfinite(data);
}

public boolean isNan() {
    return Double.isNaN(data);
}

public Real toExact() {
    if (data == 0) {
        return Intact.zero;
    } else {
        boolean neg = data < 0;
        byte[] fl = Share.integerToBinary(Double.doubleToLongBits(data), 8, false);
        int exponent = (fl[0] & 0x7F) * 16 + ((fl[1] & 0xF0) >>> 4) - 1023;

        fl[0] = 0;
        fl[1] = (byte) (fl[1] & 0xF);
        fl[1] = (byte) (fl[1] | 0x10);
        fl = Big.shiftRight(fl, Big.nearLeftBit(fl) - 1);
        int n = Big.farLeftBit(fl);
        byte[] den = Big.shiftLeft(Big.one, n - 1);

        if (exponent > 0) {
            fl = Big.shiftLeft(fl, exponent);
        } else if (exponent < 0) {
            den = Big.shiftLeft(den, Math.abs(exponent));
        }

        if (neg) {
            return new Fraction(fl, den).simplify().neg();
        } else {
            return new Fraction(fl, den).simplify();
        }
    }
}

@Override
public byte[] code() {
    long bits = Double.doubleToLongBits(data);
    byte[] ooo = Share.integerToBinary(bits, 8, false);
    byte[] bin = new byte[1 + ooo.length];
    bin[0] = BinaryLabel.FLOAT64;
    System.arraycopy(ooo, 0, bin, 1, ooo.length);
    return bin;
}
}
