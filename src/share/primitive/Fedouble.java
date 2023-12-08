package share.primitive;

public class Fedouble {

final double[] data;

Fedouble(double... args) {
    data = args;
}

@Override
@SuppressWarnings("Duplicates")
public String toString() {
    if (data.length == 0) {
        return "#r64()";
    } else {
        StringBuilder b_str = new StringBuilder("#r64(");
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
