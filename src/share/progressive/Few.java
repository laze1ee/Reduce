package share.progressive;

import static share.progressive.Pr.isNull;


public class Few {

final Object[] array;

Few(Object[] array) {
    this.array = array;
}

@Override
public String toString() {
    Lot cyc_data = Cycle.detect(this);
    if (isNull(cyc_data)) {
        return String.format("#(%s)", Aid.consArray(array, array.length));
    } else {
        Object cycle = Cycle.label(this, cyc_data);
        return cycle.toString();
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Few fw) {
        Lot c1 = Cycle.detect(this);
        Lot c2 = Cycle.detect(fw);
        if (isNull(c1) && isNull(c2) &&
            array.length == fw.array.length) {
            return Aid.objectArrEqual(array, fw.array);
        } else if (!isNull(c1) && !isNull(c2) &&
                   array.length == fw.array.length) {
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
}
