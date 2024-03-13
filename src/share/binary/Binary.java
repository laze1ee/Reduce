package share.binary;

import org.jetbrains.annotations.NotNull;
import share.datetime.Date;
import share.datetime.Time;
import share.progressive.Few;
import share.progressive.Lot;


public class Binary {

public static byte @NotNull [] shortToBinary(short n, boolean little_endian) {
    return Aid.integerToBinary(n, 2, little_endian);
}

public static byte @NotNull [] intToBinary(int n, boolean little_endian) {
    return Aid.integerToBinary(n, 4, little_endian);
}

public static byte @NotNull [] longToBinary(long n, boolean little_endian) {
    return Aid.integerToBinary(n, 8, little_endian);
}

public static short binaryToShort(byte[] bin, int start, boolean little_endian) {
    return (short) Aid.binaryToInteger(bin, start, start + 2, little_endian);
}

public static int binaryToInt(byte[] bin, int start, boolean little_endian) {
    return (int) Aid.binaryToInteger(bin, start, start + 4, little_endian);
}

public static long binaryToLong(byte[] bin, int start, boolean little_endian) {
    return Aid.binaryToInteger(bin, start, start + 8, little_endian);
}

public static byte[] codingDatum(Object datum) {
    if (datum instanceof Boolean b) {
        return Aid.codeBoolean(b);
    } else if (datum instanceof Integer i) {
        return Aid.codeInt(i);
    } else if (datum instanceof Long l) {
        return Aid.codeLong(l);
    } else if (datum instanceof Double d) {
        return Aid.codeDouble(d);
    } else if (datum instanceof Character c) {
        return Aid.codeChar(c);
    } else if (datum instanceof String s) {
        return Aid.codeString(s);

    } else if (datum instanceof int[] ins) {
        return Aid.codeIntArray(ins);
    } else if (datum instanceof long[] ls) {
        return Aid.codeLongArray(ls);
    } else if (datum instanceof double[] ds) {
        return Aid.codeDoubleArray(ds);

    } else if (datum instanceof Lot lt) {
        return Aid.codeLot(lt);
    } else if (datum instanceof Few fw) {
        return Aid.codeFew(fw);

    } else if (datum instanceof Time t) {
        return Aid.codeTime(t);
    } else if (datum instanceof Date d) {
        return Aid.codeDate(d);
    } else {
        throw new RuntimeException(String.format(Shop.UNSUPPORTED, datum));
    }
}

public static Object decodingDatum(byte[] bin) {
    Aid.Decoding de = new Aid.Decoding(bin);
    return de.process();
}
}
