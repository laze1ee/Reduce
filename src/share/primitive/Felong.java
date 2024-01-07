package share.primitive;


public class Felong {

final long[] arr;

Felong(long... args) {
    arr = args;
}


@Override
public String toString() {
    return String.format("#i64(%s)", Aid.display(arr, arr.length));
}

public String forErr() {
    if (arr.length <= 5) {
        return String.format("%s", this);
    } else {
        return String.format("#i64(%s...)", Aid.errDisplay(arr));
    }
}
}
