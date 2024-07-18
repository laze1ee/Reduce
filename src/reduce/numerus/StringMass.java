package reduce.numerus;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import reduce.share.Share;


class StringMass {

static boolean isHex(char c) {
    return ('0' <= c && c <= '9') || ('A' <= c && c <= 'F') || ('a' <= c && c <= 'f');
}

//region Predicate
static boolean isBin(char c) {
    return c == '0' || c == '1';
}

static boolean isDigit(char c) {
    return '0' <= c && c <= '9';
}


static int rangePrefixFloat(CertainChar f, @NotNull String str, int start, String prefix) {
    int sz = str.length();
    if (start + 5 > sz) {
        return start;
    } else {
        String pre = str.substring(start, start + 2);
        pre = pre.toLowerCase();
        if (pre.equals(prefix)) {
            int bound = rangeFloat(f, str, start + 2);
            if (bound == start + 2) {
                return start;
            } else {
                return bound;
            }
        } else {
            return start;
        }
    }
}

static int rangeFloat(CertainChar f, @NotNull String str, int start) {
    int sz = str.length();
    if (start + 3 > sz) {
        return start;
    } else {
        int i = start;
        if (str.charAt(i) == '+' ||
            str.charAt(i) == '-') {
            i += 1;
        }
        if (f.apply(str.charAt(i))) {
            do {
                i += 1;
            } while (i < sz && f.apply(str.charAt(i)));
            if (i + 1 < sz &&
                str.charAt(i) == '.' &&
                f.apply(str.charAt(i + 1))) {
                i += 2;
                while (i < sz && f.apply(str.charAt(i))) {
                    i += 1;
                }
                return i;
            } else {
                return start;
            }
        } else {
            return start;
        }
    }
}

static int rangePrefixInteger(CertainChar f, @NotNull String str, int start, String prefix) {
    int sz = str.length();
    if (start + 3 > sz) {
        return start;
    } else {
        String pre = str.substring(start, start + 2);
        pre = pre.toLowerCase();
        if (pre.equals(prefix)) {
            int bound = rangeInteger(f, str, start + 2);
            if (bound == start + 2) {
                return start;
            } else {
                return bound;
            }
        } else {
            return start;
        }
    }
}

static int rangeInteger(CertainChar f, @NotNull String str, int start) {
    int sz = str.length();
    int i = start;
    if (str.charAt(i) == '+' ||
        str.charAt(i) == '-') {
        i += 1;
    }
    if (i < sz &&
        f.apply(str.charAt(i))) {
        do {
            i += 1;
        } while (i < sz && f.apply(str.charAt(i)));
        return i;
    } else {
        return start;
    }
}

static int rangeFraction(@NotNull String str, int start) {
    int sz = str.length();
    int pos;
    if (start == 0) {
        pos = str.indexOf('/');
    } else {
        pos = str.substring(start)
                 .indexOf('/');
        pos += start;
    }
    if (pos == -1 ||
        pos == start || pos == sz - 1) {
        return start;
    } else {
        int bound = rangeVariousInteger(str, start);
        if (bound != pos) {
            return start;
        }
        bound = rangeVariousInteger(str, pos + 1);
        if (bound > pos + 1) {
            return bound;
        } else {
            return start;
        }
    }
}

static int rangeVariousInteger(String str, int start) {
    int bound;

    bound = rangeVariousPrefixInteger(str, start);
    if (bound > start) {
        return bound;
    }
    bound = rangeInteger(StringMass::isDigit, str, start);
    return Math.max(bound, start);
}


static @NotNull Integer rangeVariousPrefixInteger(String str, int start) {
    int bound;
    bound = rangePrefixInteger(StringMass::isBin, str, start, Msg.BIN_PREFIX);
    if (bound > start) {
        return bound;
    }
    bound = rangePrefixInteger(StringMass::isDigit, str, start, Msg.DEC_PREFIX);
    if (bound > start) {
        return bound;
    }
    bound = rangePrefixInteger(StringMass::isHex, str, start, Msg.HEX_PREFIX);
    return bound;
}

static int rangeReal(String str, int start) {
    int bound;

    bound = rangeFraction(str, start);
    if (bound > start) {
        return bound;
    }
    bound = rangePrefixFloat(StringMass::isBin, str, start, Msg.BIN_PREFIX);
    if (bound > start) {
        return bound;
    }
    bound = rangePrefixFloat(StringMass::isDigit, str, start, Msg.DEC_PREFIX);
    if (bound > start) {
        return bound;
    }
    bound = rangePrefixFloat(StringMass::isHex, str, start, Msg.HEX_PREFIX);
    if (bound > start) {
        return bound;
    }
    bound = rangeVariousPrefixInteger(str, start);
    if (bound > start) {
        return bound;
    }
    bound = rangeFloat(StringMass::isDigit, str, start);
    if (bound > start) {
        return bound;
    }
    bound = rangeInteger(StringMass::isDigit, str, start);
    if (bound > start) {
        return bound;
    }

    bound = start + 6;
    if (bound > str.length()) {
        return start;
    } else if (str.charAt(start) == '+' ||
               str.charAt(start) == '-') {
        String special = str.substring(start + 1, bound);
        special = special.toLowerCase();
        if (special.equals("inf.0") ||
            special.equals("nan.0")) {
            return bound;
        } else {
            return start;
        }
    } else {
        return start;
    }
}

static int rangeComplex(@NotNull String str) {
    int sz = str.length();
    if (str.charAt(sz - 1) == 'i') {
        int bound = rangeReal(str, 0);
        if (bound == 0) {
            return 0;
        } else {
            bound = rangeReal(str, bound);
            if (bound == sz - 1) {
                return sz;
            } else {
                return 0;
            }
        }
    } else {
        return 0;
    }
}
//endregion


//region Parsing
static @NotNull String trimFloat(@NotNull String str, int bound) {
    StringBuilder builder = new StringBuilder();

    int dot = str.indexOf('.');
    builder.append(trimInteger(str, dot));

    int i = bound - 1;
    while (str.charAt(i) == '0') {
        i -= 1;
    }
    if (i == dot) {
        i += 1;
    }

    builder.append(str, dot, i + 1);
    return builder.toString();
}

static @NotNull String trimInteger(@NotNull String str, int bound) {
    StringBuilder builder = new StringBuilder();

    int i = 0;
    if (str.charAt(i) == '#') {
        i += 2;
    }
    if (str.charAt(i) == '+' ||
        str.charAt(i) == '-') {
        builder.append(str.charAt(i));
        i += 1;
    }
    while (i < bound && str.charAt(i) == '0') {
        i += 1;
    }

    if (i == bound) {
        builder.append('0');
    } else {
        builder.append(str, i, bound);
    }
    return builder.toString();
}

static @NotNull Float64 parsePrefixFloat(@NotNull String str, int shift) {
    int i = 0;
    boolean neg = str.charAt(i) == '-';
    if (neg ||
        str.charAt(i) == '+') {
        i += 1;
    }

    byte[] fra = Big.zero;
    while (str.charAt(i) != '.') {
        byte n = Share.valueOfChar(str.charAt(i));
        fra = Big.add(Big.shiftLeft(fra, shift), new byte[]{n});
        i += 1;
    }
    int e1 = Big.farLeftBit(fra) - 1;

    int sz = str.length();
    i += 1;
    int e2 = 0;
    while (i < sz) {
        byte n = Share.valueOfChar(str.charAt(i));
        fra = Big.add(Big.shiftLeft(fra, shift), new byte[]{n});
        e2 -= shift;
        i += 1;
    }
    int exponent;
    if (e1 >= 0) {
        exponent = e1 + 1023;
    } else {
        e2 = e2 + Big.farLeftBit(fra) - 1;
        exponent = e2 + 1023;
    }

    return formFloat(neg, exponent, fra);
}

@Contract("_, _, _ -> new")
static @NotNull Float64 formFloat(boolean neg, int exponent, byte[] fra) {
    if (Big.equal(fra, Big.zero)) {
        byte[] bits = new byte[8];
        if (neg) {
            bits[0] = (byte) 0x80;
        }
        double d = Double.longBitsToDouble(Share.binaryToInteger(bits, 0, 8, false));
        return new Float64(d);
    } else {
        int offset = 53 - Big.farLeftBit(fra);
        byte[] ooo = fra;
        if (offset > 0) {
            ooo = Big.shiftLeft(ooo, offset);
        } else if (offset < 0) {
            ooo = Big.shiftRight(ooo, offset * -1);
        }
        ooo[0] = (byte) (ooo[0] & 0xF);

        byte[] bits = new byte[8];
        System.arraycopy(ooo, 0, bits, 1, 7);
        bits[1] = (byte) (bits[1] | (exponent << 4));
        bits[0] = (byte) (exponent >> 4);
        if (neg) {
            bits[0] = (byte) (bits[0] | 0x80);
        }
        double d = Double.longBitsToDouble(Share.binaryToInteger(bits, 0, 8, false));
        return new Float64(d);
    }
}

@SuppressWarnings("DuplicatedCode")
@Contract("_, _ -> new")
static @NotNull Intact parsePrefixInteger(@NotNull String str, int shift) {
    int i = 0;
    boolean neg = str.charAt(i) == '-';
    if (neg ||
        str.charAt(i) == '+') {
        i += 1;
    }

    int sz = str.length();
    byte[] inta = Big.zero;
    while (i < sz) {
        byte n = Share.valueOfChar(str.charAt(i));
        inta = Big.add(Big.shiftLeft(inta, shift), new byte[]{n});
        i += 1;
    }

    if (neg) {
        return new Intact(Big.neg(inta));
    } else {
        return new Intact(inta);
    }
}

@Contract("_ -> new")
static @NotNull Float64 parseFloat(String str) {
    double d = Double.parseDouble(str);
    return new Float64(d);
}

@SuppressWarnings("DuplicatedCode")
@Contract("_ -> new")
static @NotNull Intact parseInteger(@NotNull String str) {
    int i = 0;
    boolean neg = str.charAt(i) == '-';
    if (neg ||
        str.charAt(i) == '+') {
        i += 1;
    }

    int sz = str.length();
    byte[] inta = Big.zero;
    while (i < sz) {
        byte n = Share.valueOfChar(str.charAt(i));
        inta = Big.add(Big.mul(inta, Big.ten), new byte[]{n});
        i += 1;
    }

    if (neg) {
        return new Intact(Big.neg(inta));
    } else {
        return new Intact(inta);
    }
}

static @NotNull Real parseFraction(@NotNull String str) {
    int i = str.indexOf('/');
    String str_num = str.substring(0, i);
    String str_den = str.substring(i + 1);

    Intact num = parseVariousInteger(str_num);
    Intact den = parseVariousInteger(str_den);
    if (Arithmetic.realValueLessThan(den, Intact.zero)) {
        num = num.neg();
        den = den.neg();
    }
    Fraction fr = new Fraction(num.data, den.data);
    return fr.simplify();
}

static Intact parseVariousInteger(String str) {
    int bound;

    bound = rangePrefixInteger(StringMass::isBin, str, 0, Msg.BIN_PREFIX);
    if (bound > 0) {
        String ooo = trimInteger(str, bound);
        return parsePrefixInteger(ooo, 1);
    }
    bound = rangePrefixInteger(StringMass::isDigit, str, 0, Msg.DEC_PREFIX);
    if (bound > 0) {
        String ooo = trimInteger(str, bound);
        return parseInteger(ooo);
    }
    bound = rangePrefixInteger(StringMass::isHex, str, 0, Msg.HEX_PREFIX);
    if (bound > 0) {
        String ooo = trimInteger(str, bound);
        return parsePrefixInteger(ooo, 4);
    } else {
        bound = rangeInteger(StringMass::isDigit, str, 0);
        String ooo = trimInteger(str, bound);
        return parseInteger(ooo);
    }
}

static Real parseReal(@NotNull String str) {
    int sz = str.length();
    int bound;

    bound = rangeFraction(str, 0);
    if (bound == sz) {
        return parseFraction(str);
    }
    bound = rangePrefixFloat(StringMass::isBin, str, 0, Msg.BIN_PREFIX);
    if (bound == sz) {
        String ooo = trimFloat(str, bound);
        return parsePrefixFloat(ooo, 1);
    }
    bound = rangePrefixFloat(StringMass::isDigit, str, 0, Msg.DEC_PREFIX);
    if (bound == sz) {
        String ooo = trimFloat(str, bound);
        return parseFloat(ooo);
    }
    bound = rangePrefixFloat(StringMass::isHex, str, 0, Msg.HEX_PREFIX);
    if (bound == sz) {
        String ooo = trimFloat(str, bound);
        return parsePrefixFloat(ooo, 4);
    }
    bound = rangePrefixInteger(StringMass::isBin, str, 0, Msg.BIN_PREFIX);
    if (bound == sz) {
        String ooo = trimInteger(str, bound);
        return parsePrefixInteger(ooo, 1);
    }
    bound = rangePrefixInteger(StringMass::isDigit, str, 0, Msg.DEC_PREFIX);
    if (bound == sz) {
        String ooo = trimInteger(str, bound);
        return parseInteger(ooo);
    }
    bound = rangePrefixInteger(StringMass::isHex, str, 0, Msg.HEX_PREFIX);
    if (bound == sz) {
        String ooo = trimInteger(str, bound);
        return parsePrefixInteger(ooo, 4);
    }
    bound = rangeFloat(StringMass::isDigit, str, 0);
    if (bound == sz) {
        String ooo = trimFloat(str, bound);
        return parseFloat(ooo);
    }
    bound = rangeInteger(StringMass::isDigit, str, 0);
    if (bound == sz) {
        String ooo = trimInteger(str, bound);
        return parseInteger(ooo);
    }

    if (str.equals(Msg.POS_INF)) {
        return Float64.POS_INF;
    } else if (str.equals(Msg.NEG_INF)) {
        return Float64.NEG_INF;
    } else {
        return Float64.NAN;
    }
}

@Contract("_ -> new")
static @NotNull Complex parseComplex(String str) {
    int bound = rangeReal(str, 0);
    String str_real = str.substring(0, bound);
    String str_img = str.substring(bound, str.length() - 1);

    Real real = parseReal(str_real);
    Real img = parseReal(str_img);
    return new Complex(real, img);
}
//endregion
}
