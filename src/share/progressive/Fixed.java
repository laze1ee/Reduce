package share.progressive;

class Fixed extends Fix {

final int count;

Fixed(int count) {
    this.count = count;
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Fixed fd) {
        return count == fd.count;
    } else {
        return false;
    }
}

@Override
public String toString() {
    return String.format("#%d#", count);
}
}
