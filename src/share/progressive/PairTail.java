package share.progressive;

class PairTail extends Pair {

@Override
public boolean equals(Object datum) {
    return datum instanceof PairTail;
}
}
