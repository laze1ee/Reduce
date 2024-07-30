package reduce.progressive;

import org.jetbrains.annotations.NotNull;
import reduce.utility.Binary;
import reduce.utility.CheckSum;

import static reduce.progressive.Pr.*;


public class Lot {

final Pair pair;

Lot(Pair pair) {
    this.pair = pair;
}

public Lot(Object @NotNull ... args) {
    Pair pair = new PairEnd();
    int n = args.length;
    for (int i = n - 1; i >= 0; i -= 1) {
        pair = new PairOn(args[i], pair);
    }
    this.pair = pair;
}


@Override
public String toString() {
    if (pair instanceof PairEnd) {
        return "()";
    } else {
        Lot cycle = Cycle.detect(this);
        if (cycle.isEmpty()) {
            PairOn ooo = (PairOn) pair;
            PairHead head = new PairHead(ooo.data, Mate.toPairCons(ooo.next));
            return head.toString();
        } else {
            Object ooo = Cycle.label(this, cycle);
            return ooo.toString();
        }
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Lot lt) {
        Lot c1 = Cycle.detect(this);
        Lot c2 = Cycle.detect(lt);
        if (c1.isEmpty() && c2.isEmpty() &&
            length(this) == length(lt)) {
            Pair p1 = Mate.toPairCons(pair);
            Pair p2 = Mate.toPairCons(lt.pair);
            return p1.equals(p2);
        } else if (!c1.isEmpty() && !c2.isEmpty()) {
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

@Override
public int hashCode() {
    byte[] bin = Binary.encode(this);
    return CheckSum.fletcher32(bin);
}

public boolean isEmpty() {
    return pair instanceof PairEnd;
}

public boolean isCircularInBreadth() {
    Pair ooo = pair;
    Lot col = new Lot();
    col = cons(pair, col);
    while (!(ooo instanceof PairEnd)) {
        PairOn eee = (PairOn) ooo;
        if (Mate.isBelong(eee.next, col)) {
            return true;
        }
        col = cons(eee.next, col);
        ooo = eee.next;
    }
    return false;
}
}
