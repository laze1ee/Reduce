package reduce.numerus;

import org.jetbrains.annotations.NotNull;
import reduce.progressive.Few;
import reduce.share.Share;

import static reduce.progressive.Pr.*;


class Big {

static final byte[] zero = new byte[]{0};
static final byte[] one = new byte[]{1};
static final byte[] ten = new byte[]{10};


static byte @NotNull [] add(byte @NotNull [] m, byte @NotNull [] n) {
    if (m.length > n.length) {
        n = complement(n, m.length);
    } else if (m.length < n.length) {
        m = complement(m, n.length);
    }

    byte[] sum = new byte[m.length + 1];
    int carry = 0;
    for (int i = m.length - 1; i >= 1; i -= 1) {
        carry = (m[i] & 0xFF) + (n[i] & 0xFF) + carry;
        sum[i + 1] = (byte) carry;
        carry = carry >> 8;
    }
    carry = m[0] + n[0] + carry;
    sum[1] = (byte) carry;
    sum[0] = (byte) (carry >> 8);

    return Share.trim(sum);
}

private static byte @NotNull [] complement(byte @NotNull [] big, int length) {
    byte[] ooo = new byte[length];
    int i = length - 1;
    for (int j = big.length - 1; j >= 0; i -= 1, j -= 1) {
        ooo[i] = big[j];
    }

    if (big[0] < 0) {
        for (; i >= 0; i -= 1) {
            ooo[i] = -1;
        }
    }
    return ooo;
}

static byte @NotNull [] neg(byte @NotNull [] big) {
    byte[] b = new byte[big.length];
    for (int i = 0; i < big.length; i += 1) {
        b[i] = (byte) ~big[i];
    }
    return add(b, one);
}

private static byte @NotNull [] abs(byte @NotNull [] big) {
    if (big[0] < 0) {
        return neg(big);
    } else {
        return big;
    }
}

static byte @NotNull [] mul(byte @NotNull [] m, byte[] n) {
    byte[] a = abs(m);
    byte[] b = abs(n);

    int sz = a.length + b.length;
    byte[] product = new byte[sz];
    for (int i = b.length - 1; i >= 0; i -= 1) {
        byte[] ooo = new byte[sz];
        int carry = 0;
        for (int j = a.length - 1; j >= 0; j -= 1) {
            carry = (a[j] & 0xFF) * (b[i] & 0xFF) + carry;
            ooo[i + j + 1] = (byte) carry;
            carry = carry >> 8;
        }
        ooo[i] = (byte) carry;
        carry = 0;
        for (int j = sz - 1; j >= 0; j -= 1) {
            carry = (product[j] & 0xff) + (ooo[j] & 0xFF) + carry;
            product[j] = (byte) carry;
            carry = carry >> 8;
        }
    }
    product = Share.trim(product);

    if ((m[0] >= 0 && n[0] >= 0) ||
        (m[0] < 0 && n[0] < 0)) {
        return product;
    } else {
        return neg(product);
    }
}

static int farLeftBit(byte @NotNull [] big) {
    int i = 0;
    while (i < big.length &&
           big[i] == 0) {
        i += 1;
    }

    if (i == big.length) {
        return 0;
    } else {
        int far_left = 0;
        byte n = big[i];
        while (n != 0) {
            n = (byte) ((n & 0xFF) >>> 1);
            far_left += 1;
        }
        return far_left + 8 * (big.length - 1 - i);
    }
}

static int nearLeftBit(byte @NotNull [] big) {
    int i = big.length - 1;
    while (i > -1 &&
           big[i] == 0) {
        i -= 1;
    }

    if (i == -1) {
        return 0;
    } else {
        int near_left = 1;
        int n = big[i] & 0xFF;
        while ((n & 1) == 0) {
            n = n >> 1;
            near_left += 1;
        }
        return near_left + 8 * (big.length - 1 - i);
    }
}

private static byte @NotNull [] shiftR(byte @NotNull [] big, int shift) {
    byte[] ooo = new byte[big.length];
    int mask = (1 << shift) - 1;
    int front_half = 0;
    for (int i = 0; i < big.length; i += 1) {
        ooo[i] = (byte) (ooo[i] | front_half);
        int back_half = (big[i] & 0xFF) >>> shift;
        ooo[i] = (byte) (ooo[i] | back_half);
        front_half = (big[i] & mask) << (8 - shift);
    }
    if (big[0] < 0) {
        ooo[0] = (byte) (ooo[0] | (mask << (8 - shift)));
    }

    return Share.trim(ooo);
}

static byte[] shiftRight(byte @NotNull [] big, int shift) {
    int m = shift / 8;
    int n = shift % 8;

    if (big.length <= m || equal(big, zero)) {
        return zero;
    } else {
        byte[] ooo = new byte[big.length - m];
        System.arraycopy(big, 0, ooo, 0, big.length - m);

        if (n == 0) {
            return ooo;
        } else {
            return shiftR(ooo, n);
        }
    }
}

static byte @NotNull [] shiftRightUnsigned(byte @NotNull [] big, int shift) {
    if (shift <= 0 || equal(big, zero)) {
        return big;
    } else if (big[0] < 0) {
        byte[] moo = new byte[big.length + 1];
        System.arraycopy(big, 0, moo, 1, big.length);
        return shiftRight(moo, shift);
    } else {
        return shiftRight(big, shift);
    }
}

private static byte @NotNull [] shiftL(byte @NotNull [] big, int shift) {
    byte[] ooo = new byte[big.length + 1];
    int mask = ((1 << shift) - 1) << (8 - shift);
    int back_half = 0;
    for (int i = big.length - 1; i >= 0; i -= 1) {
        ooo[i + 1] = (byte) (ooo[i + 1] | back_half);
        int front_half = (big[i] & 0xFF) << shift;
        ooo[i + 1] = (byte) (ooo[i + 1] | front_half);
        back_half = (big[i] & mask) >>> (8 - shift);
    }
    ooo[0] = (byte) (ooo[0] | back_half);

    if (big[0] < 0) {
        ooo[0] = (byte) (ooo[0] | mask);
        return ooo;
    } else {
        return Share.trim(ooo);
    }
}

static byte @NotNull [] shiftLeft(byte @NotNull [] big, int shift) {
    if (equal(big, zero)) {
        return zero;
    } else {
        int m = shift / 8;
        int n = shift % 8;

        byte[] ooo = new byte[big.length + m];
        System.arraycopy(big, 0, ooo, 0, big.length);

        if (n == 0) {
            return ooo;
        } else {
            return shiftL(ooo, n);
        }
    }
}

static boolean equal(byte @NotNull [] m, byte @NotNull [] n) {
    if (m.length == n.length) {
        int i = 0;
        while (i < m.length && m[i] == n[i]) {
            i += 1;
        }
        return i == m.length;
    } else {
        return false;
    }
}

static boolean less(byte @NotNull [] m, byte @NotNull [] n) {
    byte[] a;
    byte[] b;
    if (m.length < n.length) {
        a = complement(m, n.length);
        b = n;
    } else if (m.length > n.length) {
        a = m;
        b = complement(n, m.length);
    } else {
        a = m;
        b = n;
    }

    if (a.length == 1) {
        return a[0] < b[0];
    } else if (a[0] < b[0]) {
        return true;
    } else if (a[0] > b[0]) {
        return false;
    } else {
        int i = 1;
        while (i < a.length - 1 && a[i] == b[i]) {
            i += 1;
        }
        return (a[i] & 0xFF) < (b[i] & 0xFF);
    }
}

static @NotNull Few div(byte @NotNull [] m, byte[] n) {
    byte[] a = abs(m);
    byte[] b = abs(n);

    byte[] divisor = shiftLeft(b, farLeftBit(a) - farLeftBit(b));
    byte[] quotient = zero;
    byte[] remainder = a;
    while (true) {
        if (less(remainder, b)) {
            quotient = shiftLeft(quotient, farLeftBit(divisor) - farLeftBit(b));
            break;
        } else if (less(remainder, divisor)) {
            quotient = shiftLeft(quotient, 1);
            divisor = shiftRight(divisor, 1);
        } else {
            quotient = shiftLeft(quotient, 1);
            quotient = add(quotient, one);
            remainder = add(remainder, neg(divisor));
            if (!less(remainder, b)) {
                divisor = shiftRight(divisor, 1);
            }
        }
    }

    if (m[0] >= 0 && n[0] >= 0) {
        return new Few(quotient, remainder);
    } else if (m[0] < 0 && n[0] < 0) {
        return new Few(quotient, neg(remainder));
    } else if (m[0] < 0) {
        return new Few(neg(quotient), neg(remainder));
    } else {
        return new Few(neg(quotient), remainder);
    }
}

static byte[] quotient(byte[] m, byte[] n) {
    Few answer = div(m, n);
    return (byte[]) ref0(answer);
}

static byte[] remainder(byte[] m, byte[] n) {
    Few answer = div(m, n);
    return (byte[]) ref1(answer);
}

static byte[] gcd(byte @NotNull [] m, byte[] n) {
    byte[] a = abs(m);
    byte[] b = abs(n);

    while (!equal(b, zero)) {
        byte[] r = remainder(a, b);
        a = b;
        b = r;
    }
    return a;
}

static double doubleOf(byte @NotNull [] big) {
    boolean neg = big[0] < 0;
    if (neg) {
        big = neg(big);
    }

    double d = 0;
    for (byte b : big) {
        d = d * 256 + (b & 0xFF);
    }
    if (neg) {
        return d * -1;
    } else {
        return d;
    }
}

static int intOf(byte @NotNull [] big) {
    boolean neg = big[0] < 0;
    if (neg) {
        big = neg(big);
    }

    int n = 0;
    for (byte b : big) {
        n = n * 256 + (b & 0xFF);
    }
    if (neg) {
        return n * -1;
    } else {
        return n;
    }
}

static @NotNull String stringOf(byte[] big) {
    byte[] quotient = big;
    byte[] remainder;
    if (quotient[0] < 0) {
        quotient = neg(quotient);
    }

    Few fw = div(quotient, ten);
    quotient = (byte[]) ref0(fw);
    remainder = (byte[]) ref1(fw);
    StringBuilder builder = new StringBuilder();
    builder.insert(0, remainder[0]);
    while (!equal(quotient, zero)) {
        fw = div(quotient, ten);
        quotient = (byte[]) ref0(fw);
        remainder = (byte[]) ref1(fw);
        builder.insert(0, remainder[0]);
    }

    if (big[0] < 0) {
        builder.insert(0, '-');
    }
    return builder.toString();
}
}
