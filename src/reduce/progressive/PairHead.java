package reduce.progressive;

import static reduce.progressive.Pr.equal;
import static reduce.progressive.Pr.stringOf;


class PairHead extends PairCons {

PairHead(Object data, Pair next) {
    super(data, next);
}

@Override
public String toString() {
    if (next instanceof PairEnd) {
        return String.format("(%s)", stringOf(data));
    } else if (next instanceof PairCyc ||
               next instanceof PairMark) {
        return String.format("(%s . %s)", stringOf(data), next);
    } else {
        return String.format("(%s %s)", stringOf(data), next);
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof PairHead moo) {
        return equal(data, moo.data) &&
               next.equals(moo.next);
    } else {
        return false;
    }
}
}
