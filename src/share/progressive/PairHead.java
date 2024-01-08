package share.progressive;


import static share.utility.Ut.stringOf;


class PairHead extends Pair {

PairHead(Object data, Pair next) {
    super(data, next);
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof PairHead pair) {
        if (data == null &&
            next == null) {
            return pair.data == null &&
                   pair.next == null;
        } else if (data == null) {
            return pair.data == null &&
                   Comparison.equal(next, pair.next);
        } else if (next == null) {
            return pair.next == null &&
                   Comparison.equal(data, pair.data);
        } else {
            return Comparison.equal(data, pair.data) &&
                   Comparison.equal(next, pair.next);
        }
    } else {
        return false;
    }
}

@Override
public String toString() {
    if (next == null) {
        return String.format("(%s)", stringOf(data));
    } else {
        return String.format("(%s %s)", stringOf(data), next);
    }
}
}
