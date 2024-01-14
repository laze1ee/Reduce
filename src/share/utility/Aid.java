package share.utility;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.progressive.Few;
import share.progressive.Pg;

import java.lang.reflect.Array;

import static share.progressive.Pg.few;
import static share.utility.Ut.fewIndex;
import static share.utility.Ut.stringOf;


class Aid {

//region String Of
private static final Few char_code = few(0, 7, 8, 9, 0xA, 0xB, 0xC, 0xD, 0x1B, 0x20, 0x7F);
private static final String[] char_name = {"nul", "alarm", "backspace", "tab", "newline", "vtab",
                                           "page", "return", "esc", "space", "delete"};

static String stringOfChar(char c) {
    int i = fewIndex(Pg::eq, (int) c, char_code);
    if (0 <= i) {
        return String.format("#\\%s", char_name[i]);
    } else if (Character.isISOControl(c)) {
        return String.format("#\\u%X", (int) c);
    } else {
        return String.format("#\\%c", c);
    }
}


static @NotNull String consPrimitiveArray(Object arr, int sz) {
    if (sz == 0) {
        return "";
    } else {
        StringBuilder str = new StringBuilder();
        sz = sz - 1;
        for (int i = 0; i < sz; i = i + 1) {
            str.append(stringOf(Array.get(arr, i)));
            str.append(" ");
        }
        str.append(stringOf(Array.get(arr, sz)));
        return str.toString();
    }
}

static String stringOfArray(@NotNull Object array) {
    if (array instanceof boolean[] bs) {
        return String.format("#1(%s)", consPrimitiveArray(bs, bs.length));
    } else if (array instanceof byte[] bs) {
        return String.format("#i8(%s)", consPrimitiveArray(bs, bs.length));
    } else if (array instanceof int[] ins) {
        return String.format("#i32(%s)", consPrimitiveArray(ins, ins.length));
    } else if (array instanceof long[] ls) {
        return String.format("#i64(%s)", consPrimitiveArray(ls, ls.length));
    } else if (array instanceof double[] ds) {
        return String.format("#r64(%s)", consPrimitiveArray(ds, ds.length));
    } else if (array instanceof char[] cs) {
        return String.format("#char(%s)", consPrimitiveArray(cs, cs.length));
    } else {
        throw new RuntimeException(String.format("unsupported array type %s for printing", array));
    }
}


@SuppressWarnings("SpellCheckingInspection")
private static final char[] HEX_STR = "0123456789ABCDEF".toCharArray();

@Contract(pure = true)
static @NotNull String hex(byte by) {
    return new String(new char[]{HEX_STR[(by >> 4) & 0xF], HEX_STR[by & 0xF]});
}
//endregion
}
