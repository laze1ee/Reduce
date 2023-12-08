package share.primitive;

public class Febyte {

final byte[] data;

Febyte(byte... args) {
    data = args;
}

public byte[] toRaw() {
    return data;
}

@SuppressWarnings("Duplicates")
public String toIntString() {
    if (data.length == 0) {
        return "#i8()";
    } else {
        StringBuilder str = new StringBuilder("#i8(");
        int sz = data.length - 1;
        int i;
        for (i = 0; i < sz; i = i + 1) {
            str.append(data[i]);
            str.append(" ");
        }
        str.append(data[i]);
        str.append(")");
        return str.toString();
    }
}


@Override
public String toString() {
    if (data.length == 0) {
        return "#u8()";
    } else {
        StringBuilder str = new StringBuilder("#u8(");
        int sz = data.length - 1;
        int i;
        for (i = 0; i < sz; i = i + 1) {
            str.append(PmAid._hex(data[i]));
            str.append(" ");
        }
        str.append(PmAid._hex(data[i]));
        str.append(")");
        return str.toString();
    }
}
}
