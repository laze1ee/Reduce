package share.progressive;

import static share.progressive.Pr.isNull;
import static share.progressive.Pr.length;


public class Lot {

final Pair pair;

Lot(Pair pair) {
    this.pair = pair;
}


@Override
public String toString() {
    if (pair instanceof PairTail) {
        return "()";
    } else {
        Lot cycle = Cycle.detect(this);
        if (isNull(cycle)) {
            PairOn moo = (PairOn) pair;
            PairHead head = new PairHead(moo.data, Aid.isolate(moo.next));
            return head.toString();
        } else {
            Object cyc_pair = Cycle.label(this, cycle);
            return cyc_pair.toString();
        }
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Lot lt) {
        Lot c1 = Cycle.detect(this);
        Lot c2 = Cycle.detect(lt);
        if (isNull(c1) && isNull(c2) &&
            length(this) == length(lt)) {
            Pair p1 = Aid.isolate(pair);
            Pair p2 = Aid.isolate(lt.pair);
            return p1.equals(p2);
        } else if (!isNull(c1) && !isNull(c2)) {
            Object o1 = Cycle.label(this, c1);
            Object o2 = Cycle.label(lt, c2);
            return o1.equals(o2);
        } else {
            return false;
        }
    } else {
        return false;
    }
}
}
