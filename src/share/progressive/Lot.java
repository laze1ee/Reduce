package share.progressive;


import org.jetbrains.annotations.NotNull;


public class Lot {

final Pair pair;

Lot(Pair pair) {
    this.pair = pair;
}

Lot(Object @NotNull [] args) {
    if (args.length == 0) {
        pair = null;
    } else {
        pair = Aid.initLot(args);
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Lot lt) {
        Lot c1 = Cycle.detect(this);
        Lot c2 = Cycle.detect(lt);
        if (Pg.isNull(c1) && Pg.isNull(c2)) {
            Object o1 = Aid.isolate(this);
            Object o2 = Aid.isolate(lt);
            return o1.equals(o2);
        } else if (Pg.length(c1) == Pg.length(c2)) {
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
public String toString() {
    return Cycle.stringOfCycle(this);
}
}