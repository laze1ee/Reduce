package share.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static share.progressive.Pr.*;
import static share.utility.Ut.isBelong;


class Cycle {

private static class DetectCycle {

    Lot collector;
    Lot cycle;

    DetectCycle() {
        collector = lot();
        cycle = lot();
    }

    Lot process(Object datum) {
        _entry(datum);
        return cycle;
    }

    void _entry(Object datum) {
        if (datum instanceof Few fw) {
            _collectFew(fw);
        } else if (datum instanceof Lot lt) {
            _collectPair(lt.pair);
        }
    }

    void _collectFew(Few fw) {
        if (isBelong(fw, collector)) {
            if (!isBelong(fw, cycle)) {
                cycle = cons(fw, cycle);
            }
        } else {
            collector = cons(fw, collector);
            int n = fw.array.length;
            for (int i = 0; i < n; i = i + 1) {
                _entry(fewRef(fw, i));
            }
        }
    }

    void _collectPair(Pair pair) {
        if (!(pair instanceof PairTail)) {
            if (isBelong(pair, collector)) {
                if (!isBelong(pair, cycle)) {
                    cycle = cons(pair, cycle);
                }
            } else {
                collector = cons(pair, collector);
                PairUse moo = (PairUse) pair;
                _entry(moo.data);
                _collectPair(moo.next);
            }
        }
    }
}

static Lot detect(Object datum) {
    DetectCycle d = new DetectCycle();
    return d.process(datum);
}


private static class LabelCycle {

    final Lot attached_cycle;
    int count;

    LabelCycle(Lot cycle) {
        attached_cycle = map(LabelCycle::attach, cycle);
        count = 0;
    }

    Object process(Object datum) {
        if (datum instanceof Few fw) {
            return _processFew(fw);
        } else if (datum instanceof Lot lt) {
            return _processPair(lt.pair);
        } else {
            return datum;
        }
    }

    @Contract("_ -> new")
    @NotNull Object _processFew(Few fw) {
        Few cyc = _find(fw);
        if (cyc != null &&
            (boolean) ref1(cyc)) {
            return new FewMark((int) ref2(cyc));
        } else if (cyc != null) {
            set1(cyc, true);
            set2(cyc, count);
            count = count + 1;
            Object[] arr = _batchArray(fw.array);
            return new FewCyc(arr, (int) ref2(cyc));
        } else {
            Object[] arr = _batchArray(fw.array);
            return new Few(arr);
        }
    }

    Object @NotNull [] _batchArray(Object @NotNull [] arr) {
        int n = arr.length;
        Object[] moo = new Object[n];
        for (int i = 0; i < n; i = i + 1) {
            moo[i] = process(arr[i]);
        }
        return moo;
    }

    @Contract("_ -> new")
    @NotNull Pair _processPair(Pair pair) {
        Few cyc = _find(pair);
        if (cyc != null &&
            ((boolean) ref1(cyc))) {
            return new PairMark((int) ref2(cyc));
        } else if (cyc != null) {
            set1(cyc, true);
            set2(cyc, count);
            count = count + 1;
            PairUse moo = (PairUse) pair;
            return new PairCyc(process(moo.data), _processNext(moo.next), (int) ref2(cyc));
        } else {
            PairUse moo = (PairUse) pair;
            return new PairHead(process(moo.data), _processNext(moo.next));
        }
    }

    @NotNull Pair _processNext(Pair pair) {
        if (pair instanceof PairTail) {
            return new PairTail();
        } else {
            Few cyc = _find(pair);
            if (cyc != null &&
                ((boolean) ref1(cyc))) {
                return new PairMark((int) ref2(cyc));
            } else if (cyc != null) {
                set1(cyc, true);
                set2(cyc, count);
                count = count + 1;
                PairUse moo = (PairUse) pair;
                return new PairCyc(process(moo.data), _processNext(moo.next), (int) ref2(cyc));
            } else {
                PairUse row = (PairUse) pair;
                return new PairCons(process(row.data), _processNext(row.next));
            }
        }
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Object attach(Object datum) {
        return few(datum, false, -1);
    }

    @Nullable Few _find(Object datum) {
        Lot moo = attached_cycle;
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


static boolean isTailCircular(@NotNull Lot lt) {
    Pair pair = lt.pair;
    Lot col = lot(pair);
    while (!(pair instanceof PairTail) &&
           !isBelong(((PairUse) pair).next, col)) {
        PairUse moo = (PairUse) pair;
        col = cons(moo.next, col);
        pair = moo.next;
    }
    return !(pair instanceof PairTail);
}
}
