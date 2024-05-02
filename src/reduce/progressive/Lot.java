package reduce.progressive;

import org.jetbrains.annotations.NotNull;

import static reduce.progressive.Pr.*;


public class Lot {

final Pair pair;

Lot(Pair pair) {
    this.pair = pair;
}

public Lot(Object @NotNull ... args) {
    Pair pair = new PairTail();
    int n = args.length;
    for (int i = n - 1; i >= 0; i = i - 1) {
        pair = new PairOn(args[i], pair);
    }
    this.pair = pair;
}


@Override
public String toString() {
    if (pair instanceof PairTail) {
        return "()";
    } else {
        Lot cycle = Cycle.detect(this);
        if (cycle.isEmpty()) {
            PairOn moo = (PairOn) pair;
            PairHead head = new PairHead(moo.data, Mate.isolate(moo.next));
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
        if (c1.isEmpty() && c2.isEmpty() &&
            length(this) == length(lt)) {
            Pair p1 = Mate.isolate(pair);
            Pair p2 = Mate.isolate(lt.pair);
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

public boolean isEmpty() {
    return pair instanceof PairTail;
}

public boolean isCircularInBreadth() {
    Pair moo_pair = pair;
    Lot col = new Lot(moo_pair);
    while (!(moo_pair instanceof PairTail) &&
           !isBelong(((PairOn) moo_pair).next, col)) {
        PairOn xoo = (PairOn) moo_pair;
        col = cons(xoo.next, col);
        moo_pair = xoo.next;
    }
    return !(moo_pair instanceof PairTail);
}
}
