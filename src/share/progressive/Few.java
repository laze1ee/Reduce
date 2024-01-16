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
        if (Pr.isNull(c1) && Pr.isNull(c2)) {
            Object o1 = Aid.isolate(this);
            Object o2 = Aid.isolate(fw);
            return o1.equals(o2);
        } else if (Pr.length(c1) == Pr.length(c2)) {
            Object o1 = Cycle.label(this, c1);
            Object o2 = Cycle.label(fw, c2);
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
