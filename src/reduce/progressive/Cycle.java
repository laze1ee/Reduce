package reduce.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static reduce.progressive.Pr.*;


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

    private void _entry(Object datum) {
        if (datum instanceof Few fw) {
            _collectArray(fw.array);
        } else if (datum instanceof Lot lt) {
            _collectPair(lt.pair);
        }
    }

    private void _collectArray(Object[] arr) {
        if (isBelong(arr, collector)) {
            if (!isBelong(arr, cycle)) {
                cycle = cons(arr, cycle);
            }
        } else {
            collector = cons(arr, collector);
            for (Object datum : arr) {
                _entry(datum);
            }
        }
    }

    private void _collectPair(Pair pair) {
        if (!(pair instanceof PairTail)) {
            if (isBelong(pair, collector)) {
                if (!isBelong(pair, cycle)) {
                    cycle = cons(pair, cycle);
                }
            } else {
                collector = cons(pair, collector);
                PairOn moo = (PairOn) pair;
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
            return _processArray(fw.array);
        } else if (datum instanceof Lot lt) {
            return _processPair(lt.pair);
        } else {
            return datum;
        }
    }

    @Contract("_ -> new")
    private @NotNull Fer _processArray(Object[] arr) {
        Few cyc = _find(arr);
        if (cyc != null &&
            (boolean) ref1(cyc)) {
            return new FewMark((int) ref2(cyc));
        } else if (cyc != null) {
            set1(cyc, true);
            set2(cyc, count);
            count = count + 1;
            Object[] moo = _batchArray(arr);
            return new FewCyc(moo, (int) ref2(cyc));
        } else {
            Object[] moo = _batchArray(arr);
            return new Few(moo);
        }
    }

    private Object @NotNull [] _batchArray(Object @NotNull [] arr) {
        int n = arr.length;
        Object[] moo = new Object[n];
        for (int i = 0; i < n; i = i + 1) {
            moo[i] = process(arr[i]);
        }
        return moo;
    }

    @Contract("_ -> new")
    private @NotNull Pair _processPair(Pair pair) {
        Few cyc = _find(pair);
        if (cyc != null &&
            ((boolean) ref1(cyc))) {
            return new PairMark((int) ref2(cyc));
        } else if (cyc != null) {
            set1(cyc, true);
            set2(cyc, count);
            count = count + 1;
            PairOn moo = (PairOn) pair;
            return new PairCyc(process(moo.data), _processNext(moo.next), (int) ref2(cyc));
        } else {
            PairOn moo = (PairOn) pair;
            return new PairHead(process(moo.data), _processNext(moo.next));
        }
    }

    private @NotNull Pair _processNext(Pair pair) {
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
                PairOn moo = (PairOn) pair;
                return new PairCyc(process(moo.data), _processNext(moo.next), (int) ref2(cyc));
            } else {
                PairOn row = (PairOn) pair;
                return new PairCons(process(row.data), _processNext(row.next));
            }
        }
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Object attach(Object datum) {
        return few(datum, false, -1);
    }

    private @Nullable Few _find(Object datum) {
        Lot moo = attached_cycle;
        while (!moo.isEmpty() &&
               !eq(datum, ref0((Few) car(moo)))) {
            moo = cdr(moo);
        }
        if (moo.isEmpty()) {
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
}
