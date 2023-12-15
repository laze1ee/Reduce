package share.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static share.progressive.Cmp.eq;
import static share.progressive.Pg.*;
import static share.utility.Ut.isBelong;


class Cycle {

private static class $Detect {
    Lot cycle;     // Lot of Lots

    $Detect() {
        cycle = lot();
    }

    Lot detect(Object datum) {
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
    $Detect d = new $Detect();
    return d.detect(datum);
}


private static class $Label {
    final Lot attached_cycle;      // lot of Few
    int count;

    $Label(Lot cycle) {
        attached_cycle = _map(cycle);
        count = -1;
    }

    Object processing(Object datum) {
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
            tmp[i] = processing(arr[i]);
        }
        return tmp;
    }

    @Contract("_ -> new")
    @NotNull Pair _processLot(Pair pair) {
        Few cyc = _find(pair, attached_cycle);
        if (cyc == null) {
            return new PairHead(processing(pair.data), _processCdr(pair.next));
        } else if ((boolean) ref1(cyc)) {
            return new PairCyc(null, null, (int) ref2(cyc));
        } else {
            count = count + 1;
            set1(cyc, true);
            set2(cyc, count);
            return new PairCyc
                   (processing(pair.data), _processCdr(pair.next), (int) ref2(cyc));
        }
    }

    Pair _processCdr(Pair pair) {
        if (pair == null) {
            return null;
        } else {
            Few cyc = _find(pair, attached_cycle);
            if (cyc == null) {
                return new Pair(processing(pair.data), _processCdr(pair.next));
            } else if ((boolean) ref1(cyc)) {
                return new PairCycTail(null, null, (int) ref2(cyc));
            } else {
                count = count + 1;
                set1(cyc, true);
                set2(cyc, count);
                return new PairCycTail
                       (processing(pair.data), _processCdr(pair.next), (int) ref2(cyc));
            }
        }

    }

    static Lot _map(Lot cycle) {
        if (isNull(cycle)) {
            return lot();
        } else {
            // 0 -> cycle pair, 1 -> found, 2 -> count
            return cons(few(car(cycle), false, -1), _map(cdr(cycle)));
        }
    }

    static @Nullable Few _find(Object datum, Lot cycle) {
        if (isNull(cycle)) {
            return null;
        } else if (eq(datum, ref0((Few) car(cycle)))) {
            return (Few) car(cycle);
        } else {
            return _find(datum, cdr(cycle));
        }
    }
}

static Object labeling(Object datum, Lot cycle) {
    $Label label = new $Label(cycle);
    return label.processing(datum);
}

static String stringOf(Object datum) {
    Lot cycle = Cycle.detect(datum);
    Object another;
    if (Pg.isNull(cycle)) {
        another = PgAid.isolate(datum);
        return another.toString();
    } else {
        another = labeling(datum, cycle);
        return String.format("[Warning in print: cycle detected]\n%s", another);
    }
}


// col: lot of Pair
private static boolean _isTailCyc(Pair pair, Lot col) {
    if (pair == null) {
        return false;
    } else if (isBelong(pair.next, col)) {
        return true;
    } else {
        return _isTailCyc(pair.next, cons(pair, col));
    }
}

static boolean isTailCyc(@NotNull Lot lt) {
    return _isTailCyc(lt.pair, lot());
}
}
