package share.progressive;

class PairNull extends Pair {

PairNull() {
    super(null, null);
}

@Override
public boolean equals(Object datum) {
    return datum instanceof PairNull;
}

@Override
public String toString() {
    return "()";
}

}
