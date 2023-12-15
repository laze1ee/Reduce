package share.progressive;

public class Few {

final Object[] array;

Few(Object[] array) {
    this.array = array;
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Few fw) {
        Lot c1 = Cycle.detect(this);
        Lot c2 = Cycle.detect(fw);
        if (Pg.isNull(c1) && Pg.isNull(c2)) {
            Object o1 = PgAid.isolate(this);
            Object o2 = PgAid.isolate(fw);
            return o1.equals(o2);
        } else if (Pg.length(c1) == Pg.length(c2)) {
            Object o1 = Cycle.labeling(this, c1);
            Object o2 = Cycle.labeling(fw, c2);
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
