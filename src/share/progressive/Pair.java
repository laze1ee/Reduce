package share.progressive;

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
        if (data == null && next == null) {
            return pair.data == null &&
                   pair.next == null;
        } else if (data == null) {
            return pair.data == null &&
                   next.equals(pair.next);
        } else if (next == null) {
            return pair.next == null &&
                   data.equals(pair.data);
        } else {
            return data.equals(pair.data) &&
                   next.equals(pair.next);
        }
    } else {
        return false;
    }
}

@Override
public String toString() {
    if (next == null) {
        return String.format("%s", Pg.stringOf(data));
    } else {
        return String.format("%s %s", Pg.stringOf(data), next);
    }
}
}