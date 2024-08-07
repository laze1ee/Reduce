package reduce.progressive;

import static reduce.progressive.Pr.equal;
import static reduce.progressive.Pr.stringOf;


class PairCyc extends PairOn {

final int count;

PairCyc(Object data, Pair next, int count) {
    super(data, next);
    this.count = count;
}

@Override
public String toString() {
    if (next instanceof PairEnd) {
        return String.format("#%s=(%s)", count, stringOf(data));
    } else if (next instanceof PairCyc ||
               next instanceof PairMark) {
        return String.format("#%s=(%s . %s)", count, stringOf(data), next);
    } else {
        return String.format("#%s=(%s %s)", count, stringOf(data), next);
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof PairCyc moo) {
        return count == moo.count &&
               equal(data, moo.data) &&
               next.equals(moo.next);
    } else {
        return false;
    }
}
}
