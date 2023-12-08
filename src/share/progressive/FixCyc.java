package share.progressive;

class FixCyc extends Fix {

final Object[] array;
final int count;

FixCyc(Object[] array, int count) {
    this.array = array;
    this.count = count;
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof FixCyc fc) {
        return count == fc.count &&
               Cmp.isArraysEq(array, fc.array);
    } else {
        return false;
    }
}

@Override
public String toString() {
    return String.format("#%d=#(%s)", count, PgAid.stringOfArray(array));
}
}
