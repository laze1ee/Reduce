package share.progressive;


import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


public class Comparison {

static boolean isObjectArrayEqual(Object @NotNull [] arr1, Object @NotNull [] arr2) {
    if (arr1.length == arr2.length) {
        int i = 0;
        while (i < arr1.length &&
               equal(arr1[i], arr2[i])) {
            i = i + 1;
        }
        return i == arr1.length;
    } else {
        return false;
    }
}


public static boolean eq(Object o1, Object o2) {
    if (o1 instanceof Boolean b1 && o2 instanceof Boolean b2) {
        return b1.booleanValue() == b2.booleanValue();
    } else if (o1 instanceof Symbol s1 && o2 instanceof Symbol s2) {
        return s1.id == s2.id;
    } else if (o1 instanceof Lot l1 && o2 instanceof Lot l2) {
        return l1.pair == l2.pair;
    } else if (o1 instanceof Few f1 && o2 instanceof Few f2) {
        return f1.array == f2.array;
    } else if (o1 instanceof Number n1 && o2 instanceof Number n2) {
        if (o1 instanceof Double d1) {
            return d1 == n2.doubleValue();
        } else if (n2 instanceof Double d2) {
            return n1.doubleValue() == d2;
        } else {
            return n1.longValue() == n2.longValue();
        }
    } else {
        return o1 == o2;
    }
}

public static boolean equal(Object o1, Object o2) {
    if (eq(o1, o2)) {
        return true;
    } else if (o1.getClass().isArray() &&
               o2.getClass().isArray()) {
        if (o1 instanceof boolean[] bs1 &&
            o2 instanceof boolean[] bs2) {
            int r = Arrays.compare(bs1, bs2);
            return r == 0;
        } else if (o1 instanceof byte[] bs1 &&
                   o2 instanceof byte[] bs2) {
            int r = Arrays.compare(bs1, bs2);
            return r == 0;
        } else if (o1 instanceof int[] ins1 &&
                   o2 instanceof int[] ins2) {
            int r = Arrays.compare(ins1, ins2);
            return r == 0;
        } else if (o1 instanceof long[] ls1 &&
                   o2 instanceof long[] ls2) {
            int r = Arrays.compare(ls1, ls2);
            return r == 0;
        } else if (o1 instanceof double[] ds1 &&
                   o2 instanceof double[] ds2) {
            int r = Arrays.compare(ds1, ds2);
            return r == 0;
        } else {
            throw new RuntimeException(String.format(Shop.UNSUPPORTED, o1, o2));
        }
    } else {
        return o1.equals(o2);
    }
}


public static boolean less(Object o1, Object o2) {
    if (o1 instanceof Number n1 &&
        o2 instanceof Number n2) {
        if (n1 instanceof Double d1) {
            return d1 < n2.doubleValue();
        } else if (n2 instanceof Double d2) {
            return n1.doubleValue() < d2;
        } else {
            return n1.longValue() < n2.longValue();
        }
    } else if (o1 instanceof String s1 &&
               o2 instanceof String s2) {
        int m = s1.compareTo(s2);
        return m < 0;
    } else {
        throw new RuntimeException(String.format("%s and %s cannot be compared in size", o1, o2));
    }
}


public static boolean greater(Object o1, Object o2) {
    if (o1 instanceof Number n1 &&
        o2 instanceof Number n2) {
        if (n1 instanceof Double d1) {
            return d1 > n2.doubleValue();
        } else if (n2 instanceof Double d2) {
            return n1.doubleValue() > d2;
        } else {
            return n1.longValue() > n2.longValue();
        }
    } else if (o1 instanceof String s1 &&
               o2 instanceof String s2) {
        int m = s1.compareTo(s2);
        return m > 0;
    } else {
        throw new RuntimeException(String.format("%s and %s cannot be compared in size", o1, o2));
    }
}
}
