package share.progressive;


import static share.utility.Ut.stringOf;


class PairCyc extends Pair {

final int count;

PairCyc(Object data, Pair next, int count) {
    super(data, next);
    this.count = count;
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof PairCyc pair) {
        if (data == null && next == null) {
            return pair.data == null &&
                   pair.next == null &&
                   count == pair.count;
        } else if (data == null) {
            return pair.data == null &&
                   count == pair.count &&
                   next.equals(pair.next);
        } else if (next == null) {
            return pair.next == null &&
                   count == pair.count &&
                   data.equals(pair.data);
        } else {
            return count == pair.count &&
                   data.equals(pair.data) &&
                   next.equals(pair.next);
        }
    } else {
        return false;
    }
}

@Override
public String toString() {
    if (next == null) {
        return String.format("#%d#", count);
    } else {
        return String.format("#%d=(%s %s)", count, stringOf(data), next);
    }
}
}