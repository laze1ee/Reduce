package reduce.progressive;

class PairEnd extends Pair {

@Override
public boolean equals(Object datum) {
    return datum instanceof PairEnd;
}
}
