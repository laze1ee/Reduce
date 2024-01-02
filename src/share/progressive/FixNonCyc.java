package share.progressive;

class FixNonCyc extends Fix {

final Object[] array;

FixNonCyc(Object[] array) {
    this.array = array;
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof FixNonCyc fn) {
        return Comparison.isArraysEq(array, fn.array);
    } else {
        return false;
    }
}

@Override
public String toString() {
    return String.format("#(%s)", Aid.stringOfArray(array));
}
}
