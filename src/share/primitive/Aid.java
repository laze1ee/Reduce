package share.primitive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;


class Aid {

@SuppressWarnings("SpellCheckingInspection")
private static final char[] HEX_STR = "0123456789ABCDEF".toCharArray();

@Contract(pure = true)
static @NotNull String hex(byte by) {
    return new String(new char[]{HEX_STR[(by >> 4) & 0xF], HEX_STR[by & 0xF]});
}


static String stringOf(Object datum) {
    if (datum instanceof Boolean b) {
        if (b) {
            return "#t";
        } else {
            return "#f";
        }
    } else if (datum instanceof Byte b) {
        return String.format("%s", hex(b));
    } else {
        return datum.toString();
    }
}

static @NotNull String display(Object arr, int sz) {
    StringBuilder str = new StringBuilder();
    int i = 0;
    while (i < sz - 1) {
        str.append(stringOf(Array.get(arr, i)));
        str.append(" ");
        i = i + 1;
    }
    str.append(stringOf(Array.get(arr, i)));
    return str.toString();
}

static @NotNull String errDisplay(Object arr) {
    StringBuilder str = new StringBuilder();
    for (int i = 0; i < 5; i = i + 1) {
        Object datum = Array.get(arr, i);
        str.append(stringOf(datum));
        str.append(" ");
    }
    return str.toString();
}
}
