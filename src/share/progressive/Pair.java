package share.progressive;


import static share.utility.Ut.stringOf;


class Pair {

Object data;
Pair next;

Pair(Object data, Pair next) {
    this.data = data;
    this.next = next;
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Pair pair) {
        if (data == null &&
            next == null) {
            return pair.data == null &&
                   pair.next == null;
        } else if (data == null) {
            return pair.data == null &&
                   Pg.equal(next, pair.next);
        } else if (next == null) {
            return pair.next == null &&
                   Pg.equal(data, pair.data);
        } else {
            return Pg.equal(data, pair.data) &&
                   Pg.equal(next, pair.next);
        }
    } else {
        return false;
    }
}

@Override
public String toString() {
    if (next == null) {
        return String.format("%s", stringOf(data));
    } else {
        return String.format("%s %s", stringOf(data), next);
    }
}
}