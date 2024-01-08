package share.progressive;


import static share.utility.Ut.stringOf;


class PairCycTail extends PairCyc {

PairCycTail(Object data, Pair next, int count) {
    super(data, next, count);
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof PairCycTail pair) {
        if (data == null &&
            next == null) {
            return pair.data == null &&
                   pair.next == null &&
                   count == pair.count;
        } else if (data == null) {
            return pair.data == null &&
                   count == pair.count &&
                   Comparison.equal(next, pair.next);
        } else if (next == null) {
            return pair.next == null &&
                   count == pair.count &&
                   Comparison.equal(data, pair.data);
        } else {
            return count == pair.count &&
                   Comparison.equal(data, pair.data) &&
                   Comparison.equal(next, pair.next);
        }
    } else {
        return false;
    }
}

@Override
public String toString() {
    if (next == null) {
        return String.format(". #%d#", count);
    } else {
        return String.format(". #%d=(%s %s)", count, stringOf(data), next);
    }
}
}
