package reduce.progressive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static reduce.progressive.Pr.*;


class Cycle {

private static class Detecting {

    final Object datum;
    Lot collector;
    Lot cycle;

    Detecting(Object datum) {
        this.datum = datum;
        this.collector = new Lot();
        this.cycle = new Lot();
    }

    Lot process() {
        _job(datum);
        return cycle;
    }

    private void _job(Object datum) {
        if (datum instanceof Few fw) {
            _collectArray(fw.array);
        } else if (datum instanceof Lot lt) {
            _collectPair(lt.pair);
        }
    }

    private void _collectArray(Object[] arr) {
        if (Mate.isBelong(arr, collector)) {
            if (!Mate.isBelong(arr, cycle)) {
                cycle = cons(arr, cycle);
            }
        } else {
            collector = cons(arr, collector);
            for (Object datum : arr) {
                _job(datum);
            }
        }
    }

    private void _collectPair(Pair pair) {
        if (!(pair instanceof PairTail)) {
            if (Mate.isBelong(pair, collector)) {
                if (!Mate.isBelong(pair, cycle)) {
                    cycle = cons(pair, cycle);
                }
            } else {
                collector = cons(pair, collector);
                PairOn ooo = (PairOn) pair;
                _job(ooo.data);
                _collectPair(ooo.next);
            }
        }
    }
}

static Lot detect(Object datum) {
    Detecting inst = new Detecting(datum);
    return inst.process();
}


private static class Labeling {

    final Object datum;
    final Lot attached_cycle;
    int count;

    Labeling(Object datum, Lot cycle) {
        this.datum = datum;
        this.attached_cycle = map(Labeling::attach, cycle);
        this.count = 0;
    }

    Object process() {
        return _job(datum);
    }

    Object _job(Object datum) {
        if (datum instanceof Few fw) {
            return _jobArray(fw.array);
        } else if (datum instanceof Lot lt) {
            return _jobPair(lt.pair);
        } else {
            return datum;
        }
    }

    @Contract("_ -> new")
    private @NotNull Fer _jobArray(Object[] arr) {
        Few cyc = _find(arr);
        if (cyc != null &&
            (boolean) ref1(cyc)) {
            return new FewMark((int) ref2(cyc));
        } else if (cyc != null) {
            set1(cyc, true);
            set2(cyc, count);
            count = count + 1;
            Object[] ooo = _batchArray(arr);
            return new FewCyc(ooo, (int) ref2(cyc));
        } else {
            Object[] ooo = _batchArray(arr);
            return new Few(ooo);
        }
    }

    private Object @NotNull [] _batchArray(Object @NotNull [] arr) {
        int n = arr.length;
        Object[] ooo = new Object[n];
        for (int i = 0; i < n; i = i + 1) {
            ooo[i] = _job(arr[i]);
        }
        return ooo;
    }

    @Contract("_ -> new")
    private @NotNull Pair _jobPair(Pair pair) {
        Few cyc = _find(pair);
        if (cyc != null &&
            ((boolean) ref1(cyc))) {
            return new PairMark((int) ref2(cyc));
        } else if (cyc != null) {
            set1(cyc, true);
            set2(cyc, count);
            count = count + 1;
            PairOn ooo = (PairOn) pair;
            return new PairCyc(_job(ooo.data), _jobNext(ooo.next), (int) ref2(cyc));
        } else {
            PairOn ooo = (PairOn) pair;
            return new PairHead(_job(ooo.data), _jobNext(ooo.next));
        }
    }

    private @NotNull Pair _jobNext(Pair pair) {
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
                PairOn ooo = (PairOn) pair;
                return new PairCyc(_job(ooo.data), _jobNext(ooo.next), (int) ref2(cyc));
            } else {
                PairOn row = (PairOn) pair;
                return new PairCons(_job(row.data), _jobNext(row.next));
            }
        }
    }

    @Contract(value = "_ -> new", pure = true)
    static @NotNull Object attach(Object datum) {
        return new Few(datum, false, -1);
    }

    private @Nullable Few _find(Object datum) {
        Lot ooo = attached_cycle;
        while (!ooo.isEmpty() &&
               !eq(datum, ref0((Few) car(ooo)))) {
            ooo = cdr(ooo);
        }
        if (ooo.isEmpty()) {
            return null;
        } else {
            return (Few) car(ooo);
        }
    }
}

static Object label(Object datum, Lot cycle) {
    Labeling inst = new Labeling(datum, cycle);
    return inst.process();
}
}
