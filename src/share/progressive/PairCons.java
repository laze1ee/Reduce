package share.progressive;

import static share.progressive.Pr.equal;
import static share.progressive.Pr.stringOf;


class PairCons extends PairUse {

PairCons(Object data, Pair next) {
    super(data, next);
}

@Override
public String toString() {
    if (next instanceof PairTail) {
        return String.format("%s", stringOf(data));
    } else if (next instanceof PairCyc ||
               next instanceof PairMark) {
        return String.format("%s . %s", stringOf(data), next);
    } else {
        return String.format("%s %s", stringOf(data), next);
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof PairCons moo) {
        return equal(data, moo.data) &&
               next.equals(moo.next);
    } else {
        return false;
    }
}
}
