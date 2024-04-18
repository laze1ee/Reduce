package reduce.binary;

import org.jetbrains.annotations.NotNull;
import reduce.datetime.Date;
import reduce.datetime.Time;
import reduce.progressive.Few;
import reduce.progressive.Lot;


public class Binary {

public static byte @NotNull [] shortToBinary(short n, boolean little_endian) {
    return Mate.integerToBinary(n, 2, little_endian);
}

public static byte @NotNull [] intToBinary(int n, boolean little_endian) {
    return Mate.integerToBinary(n, 4, little_endian);
}

public static byte @NotNull [] longToBinary(long n, boolean little_endian) {
    return Mate.integerToBinary(n, 8, little_endian);
}

public static short binaryToShort(byte[] bin, int start, boolean little_endian) {
    return (short) Mate.binaryToInteger(bin, start, start + 2, little_endian);
}

public static int binaryToInt(byte[] bin, int start, boolean little_endian) {
    return (int) Mate.binaryToInteger(bin, start, start + 4, little_endian);
}

public static long binaryToLong(byte[] bin, int start, boolean little_endian) {
    return Mate.binaryToInteger(bin, start, start + 8, little_endian);
}

public static byte[] codingDatum(Object datum) {
    if (datum instanceof Boolean b) {
        return Mate.codeBoolean(b);
    } else if (datum instanceof Integer i) {
        return Mate.codeInt(i);
    } else if (datum instanceof Long l) {
        return Mate.codeLong(l);
    } else if (datum instanceof Double d) {
        return Mate.codeDouble(d);
    } else if (datum instanceof Character c) {
        return Mate.codeChar(c);
    } else if (datum instanceof String s) {
        return Mate.codeString(s);

    } else if (datum instanceof int[] ins) {
        return Mate.codeIntArray(ins);
    } else if (datum instanceof long[] ls) {
        return Mate.codeLongArray(ls);
    } else if (datum instanceof double[] ds) {
        return Mate.codeDoubleArray(ds);

    } else if (datum instanceof Lot lt) {
        return Mate.codeLot(lt);
    } else if (datum instanceof Few fw) {
        return Mate.codeFew(fw);

    } else if (datum instanceof Time t) {
        return Mate.codeTime(t);
    } else if (datum instanceof Date d) {
        return Mate.codeDate(d);
    } else {
        throw new RuntimeException(String.format(Msg.UNSUPPORTED, datum));
    }
}

public static Object decodingDatum(byte[] bin) {
    Mate.Decoding de = new Mate.Decoding(bin);
    return de.process();
}
}
