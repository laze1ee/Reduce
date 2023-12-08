package share.primitive;

public class Feint {

final int[] data;

Feint(int... args) {
    data = args;
}

@Override
@SuppressWarnings("Duplicates")
public String toString() {
    if (data.length == 0) {
        return "#i32()";
    } else {
        StringBuilder b_str = new StringBuilder("#i32(");
        int sz = data.length - 1;
        int i;
        for (i = 0; i < sz; i = i + 1) {
            b_str.append(data[i]);
            b_str.append(" ");
        }
        b_str.append(data[i]);
        b_str.append(")");
        return b_str.toString();
    }
}
}
