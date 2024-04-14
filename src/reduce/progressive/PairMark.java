package reduce.progressive;

class PairMark extends Pair {

final int count;

PairMark(int count) {
    this.count = count;
}
@Override
public String toString() {
    return String.format("#%s#", count);
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof PairMark moo) {
        return count == moo.count;
    } else {
        return false;
    }
}
}
