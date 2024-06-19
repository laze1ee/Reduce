package reduce.progressive;

import org.jetbrains.annotations.NotNull;
import reduce.utility.CheckSum;

import static reduce.progressive.Pr.code;


public class Few extends Fer {

final Object[] array;

public Few(Object @NotNull ... args) {
    this.array = args;
}


@Override
public String toString() {
    Lot cycle = Cycle.detect(this);
    if (cycle.isEmpty()) {
        return String.format("#(%s)", Mate.consArray(array, array.length));
    } else {
        Object ooo = Cycle.label(this, cycle);
        return ooo.toString();
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Few fw) {
        Lot c1 = Cycle.detect(this);
        Lot c2 = Cycle.detect(fw);
        if (c1.isEmpty() && c2.isEmpty() &&
            array.length == fw.array.length) {
            return Mate.objectArrayEqual(array, fw.array);
        } else if (!c1.isEmpty() && !c2.isEmpty() &&
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

@Override
public int hashCode() {
    byte[] bin = code(this);
    return CheckSum.fletcher32(bin);
}

public Object[] toRaw() {
    return array;
}
}
