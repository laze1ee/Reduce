package share.progressive;

public class Lot {

final Pair pair;

Lot(Pair pair) {
    this.pair = pair;
}

Lot(Object[] args) {
    if (args.length == 0) {
        pair = null;
    } else {
        pair = new Pair(args[0], null);
        PgAid.initLot(1, pair, args);
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Lot lt) {
        Lot c1 = Cycle.detect(this);
        Lot c2 = Cycle.detect(lt);
        if (Pg.isNull(c1) && Pg.isNull(c2)) {
            Object o1 = PgAid.isolate(this);
            Object o2 = PgAid.isolate(lt);
            return o1.equals(o2);
        } else if (Pg.length(c1) == Pg.length(c2)) {
            Object o1 = Cycle.labeling(this, c1);
            Object o2 = Cycle.labeling(lt, c2);
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
    return Cycle.stringOf(this);
}
}