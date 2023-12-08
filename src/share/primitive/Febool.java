package share.primitive;

public class Febool {

final boolean[] data;

Febool(boolean... args) {
    data = args;
}

@Override
public String toString() {
    if (data.length == 0) {
        return "#1()";
    } else {
        StringBuilder b_str = new StringBuilder("#1(");
        int sz = data.length - 1;
        int i;
        for (i = 0; i < sz; i = i + 1) {
            b_str.append(PmAid._display_bool(data[i]));
            b_str.append(" ");
        }
        b_str.append(PmAid._display_bool(data[i]));
        b_str.append(")");
        return b_str.toString();
    }
}
}
