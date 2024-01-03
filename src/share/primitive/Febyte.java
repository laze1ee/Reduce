package share.primitive;

public class Febyte {

final byte[] arr;

Febyte(byte... args) {
    arr = args;
}

public byte[] toRaw() {
    return arr;
}


public String toIntString() {
    if (arr.length == 0) {
        return "#i8()";
    } else {
        StringBuilder str = new StringBuilder("#i8(");
        int sz = arr.length;
        int i;
        for (i = 0; i < sz - 1; i = i + 1) {
            str.append(arr[i]);
            str.append(" ");
        }
        str.append(arr[i]);
        str.append(")");
        return str.toString();
    }
}

@Override
public String toString() {
    return String.format("#u8(%s)", Aid.display(arr, arr.length));
}

public String forErr() {
    if (arr.length <= 5) {
        return String.format("%s", this);
    } else {
        return String.format("#u8(%s...)", Aid.errDisplay(arr));
    }
}
}
