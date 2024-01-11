package share.progressive;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static share.progressive.Comparison.eq;
import static share.progressive.Pg.*;
import static share.utility.Ut.isBelong;


class Cycle {

private static class DetectCycle {
    Lot cycle;     // Lot of Lots

    DetectCycle() {
        cycle = lot();
    }

    Lot process(Object datum) {
        _detect(datum, lot());
        return cycle;
    }

    void _detect(Object datum, Lot col) {
        if (datum instanceof Few fw) {
            _processFex(fw.array, col);
        } else if (datum instanceof Lot lt) {
            if (!isNull(lt)) {
                _processLot(lt.pair, col);
            }
        }
    }

    void _processFex(Object[] array, Lot col) {
        if (isBelong(array, col)) {
            if (!isBelong(array, cycle)) {
                cycle = cons(array, cycle);
            }
        } else {
            col = cons(array, col);
            for (Object obj : array) {
                _detect(obj, col);
            }
        }
    }

    void _processLot(Pair pair, Lot col) {
        if (isBelong(pair, col)) {
            if (!isBelong(pair, cycle)) {
                cycle = cons(pair, cycle);
            }
        } else {
            _processCdr(pair, col);
            _travel(pair, cons(pair, col), lot());
        }
    }

    void _processCdr(Pair pair, Lot col) {
        if (pair != null) {
            col = cons(pair, col);
            if (isBelong(pair.next, col)) {
                if (!isBelong(pair.next, cycle)) {
                    cycle = cons(pair.next, cycle);
                }
            } else {
                _processCdr(pair.next, col);
            }
        }
    }

    void _travel(Pair pair, Lot col, Lot stop) {
        if (pair != null) {
            if (!isBelong(pair.next, stop)) {
                _detect(pair.data, col);
                _travel(pair.next, col, cons(pair, stop));
            }
        }
    }
}

static Lot detect(Object datum) {
    DetectCycle d = new DetectCycle();
    return d.process(datum);
}


private static class LabelCycle {
    final Lot attached_cycle;      // lot of Few
    int count;

    LabelCycle(Lot cycle) {
        attached_cycle = lotMap(LabelCycle::doing, cycle);
        count = -1;
    }

    Object process(Object datum) {
        if (datum instanceof Few fw) {
            return _processFex(fw.array);
        } else if (datum instanceof Lot lt) {
            if (isNull(lt)) {
                return new PairNull();
            } else {
                return _processLot(lt.pair);
            }
        } else {
            return datum;
        }
    }

    @Contract("_ -> new")
    @NotNull Fix _processFex(Object[] array) {
        Few cyc = _find(array, attached_cycle);
        if (cyc != null && (boolean) ref1(cyc)) {
            return new Fixed((int) ref2(cyc));
        } else if (cyc != null) {
            count = count + 1;
            set1(cyc, true);
            set2(cyc, count);
            Object[] tmp = _batchArray(array);
            return new FixCyc(tmp, (int) ref2(cyc));
        } else {
            Object[] tmp = _batchArray(array);
            return new FixNonCyc(tmp);
        }
    }

    Object @NotNull [] _batchArray(Object @NotNull [] arr) {
        int sz = arr.length;
        Object[] tmp = new Object[sz];
        for (int i = 0; i < sz; i = i + 1) {
            tmp[i] = process(arr[i]);
        }
        return tmp;
    }

    @Contract("_ -> new")
    @NotNull Pair _processLot(Pair pair) {
        Few cyc = _find(pair, attached_cycle);
        if (cyc == null) {
            return new PairHead(process(pair.data), _processCdr(pair.next));
        } else if ((boolean) ref1(cyc)) {
            return new PairCyc(null, null, (int) ref2(cyc));
        } else {
            count = count + 1;
            set1(cyc, true);
            set2(cyc, count);
            return new PairCyc
            (process(pair.data), _processCdr(pair.next), (int) ref2(cyc));
        }
    }

    Pair _processCdr(Pair pair) {
        if (pair == null) {
            return null;
        } else {
            Few cyc = _find(pair, attached_cycle);
            if (cyc == null) {
                return new Pair(process(pair.data), _processCdr(pair.next));
            } else if ((boolean) ref1(cyc)) {
                return new PairCycTail(null, null, (int) ref2(cyc));
            } else {
                count = count + 1;
                set1(cyc, true);
                set2(cyc, count);
                return new PairCycTail
                (process(pair.data), _processCdr(pair.next), (int) ref2(cyc));
            }
        }

    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Object doing(Object datum) {
        return few(datum, false, -1);
    }

    @Nullable Few _find(Object datum, Lot cycle) {
        Lot moo = cycle;
        while (!isNull(moo) &&
               !eq(datum, ref0((Few) car(moo)))) {
            moo = cdr(moo);
        }
        if (isNull(moo)) {
            return null;
        } else {
            return (Few) car(moo);
        }
    }
}

static Object label(Object datum, Lot cycle) {
    LabelCycle l = new LabelCycle(cycle);
    return l.process(datum);
}

static String stringOfCycle(Object datum) {
    Lot cycle = Cycle.detect(datum);
    Object another;
    if (Pg.isNull(cycle)) {
        another = Aid.isolate(datum);
        return another.toString();
    } else {
        another = label(datum, cycle);
        return String.format("[Warning in print: cycle detected]\n%s", another);
    }
}


static boolean isTailCircular(@NotNull Lot lt) {
    Pair pair = lt.pair;
    Lot col = lot();
    while (pair != null &&
           !isBelong(pair.next, col)) {
        col = cons(pair, col);
        pair = pair.next;
    }
    return pair != null;
}
}
