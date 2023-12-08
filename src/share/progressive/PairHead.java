package share.progressive;

class PairHead extends Pair {

PairHead(Object data, Pair next) {
    super(data, next);
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof PairHead pair) {
        if (data == null && next == null) {
            return pair.data == null && pair.next == null;
        } else if (data == null) {
            return next.equals(pair.next);
        } else if (next == null) {
            return data.equals(pair.data);
        } else {
            return data.equals(pair.data) && next.equals(pair.next);
        }
    } else {
        return false;
    }
}

@Override
public String toString() {
    if (next == null) {
        return String.format("(%s)", Pg.stringOf(data));
    } else {
        return String.format("(%s %s)", Pg.stringOf(data), next);
    }
}

}
