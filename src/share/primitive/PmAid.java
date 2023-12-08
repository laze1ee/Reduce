package share.primitive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


class PmAid {

@Contract(pure = true)
static @NotNull String _display_bool(boolean b) {
    if (b) {
        return "#t";
    } else {
        return "#f";
    }
}

private static final char[] HEX_STR = "0123456789ABCDEF".toCharArray();

@Contract(pure = true)
static @NotNull String _hex(byte by) {
    return new String(new char[]{HEX_STR[(by >> 4) & 0xF], HEX_STR[by & 0xF]});
}



}
