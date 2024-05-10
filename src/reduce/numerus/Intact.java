package reduce.numerus;

import reduce.share.Share;
import reduce.share.BinaryLabel;
import reduce.utility.CheckSum;


public class Intact extends Real {

static final Intact zero = new Intact(Big.zero);

final byte[] data;

public Intact(byte[] data) {
    this.data = data;
}


@Override
public String toString() {
    return Big.stringOf(data);
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Intact in) {
        return Big.equal(data, in.data);
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
public Intact neg() {
    return new Intact(Big.neg(data));
}

@Override
boolean isZero() {
    return Big.equal(data, Big.zero);
}

Float64 toInexact() {
    double d = Big.doubleOf(data);
    return new Float64(d);
}

@Override
public byte[] code() {
    byte[] size = Share.codeSize(data.length);
    byte[] bin = new byte[1 + size.length + data.length];
    bin[0] = BinaryLabel.INTACT;
    System.arraycopy(size, 0, bin, 1, size.length);
    System.arraycopy(data, 0, bin, 1 + size.length, data.length);
    return bin;
}
}
