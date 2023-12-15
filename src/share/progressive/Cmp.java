package share.progressive;

import org.jetbrains.annotations.NotNull;


public class Cmp {

public static boolean less(Object o1, Object o2) {
    if (o1 instanceof Number n1 && o2 instanceof Number n2) {
        if (n1 instanceof Double d1) {
            return d1 < n2.doubleValue();
        } else if (n2 instanceof Double d2) {
            return n1.doubleValue() < d2;
        } else {
            return n1.longValue() < n2.longValue();
        }
    } else if (o1 instanceof String s1 && o2 instanceof String s2) {
        int m = s1.compareTo(s2);
        return m < 0;
    } else {
        throw new RuntimeException(String.format("%s and %s cannot be compared in size", o1, o2));
    }
}


public static boolean greater(Object o1, Object o2) {
    if (o1 instanceof Number n1 && o2 instanceof Number n2) {
        if (n1 instanceof Double d1) {
            return d1 > n2.doubleValue();
        } else if (n2 instanceof Double d2) {
            return n1.doubleValue() > d2;
        } else {
            return n1.longValue() > n2.longValue();
        }
    } else if (o1 instanceof String s1 && o2 instanceof String s2) {
        int m = s1.compareTo(s2);
        return m > 0;
    } else {
        throw new RuntimeException(String.format("%s and %s cannot be compared in size", o1, o2));
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
    return eq(o1, o2) || o1.equals(o2);
}


static boolean isArraysEq(Object @NotNull [] arr1, Object @NotNull [] arr2) {
    if (arr1.length == arr2.length) {
        int i = 0;
        while (i < arr1.length && arr1[i].equals(arr2[i])) {
            i = i + 1;
        }
        return i == arr1.length;
    } else {
        return false;
    }
}
}
