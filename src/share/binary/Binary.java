package share.binary;

import share.datetime.Date;
import share.datetime.Time;
import share.progressive.Few;
import share.progressive.Lot;


public class Binary {

public static byte[] codeDatum(Object datum) {
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

public static Object decodeDatum(byte[] bin) {
    Aid.Decoding de = new Aid.Decoding(bin);
    return de.process();
}
}
