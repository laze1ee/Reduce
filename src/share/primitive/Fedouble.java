package share.primitive;


public class Fedouble {

final double[] arr;

Fedouble(double... args) {
    arr = args;
}


@Override
public String toString() {
    return String.format("#r64(%s)", Aid.display(arr, arr.length));
}

public String forErr() {
    if (arr.length <= 5) {
        return String.format("%s", this);
    } else {
        return String.format("#r64(%s...)", Aid.errDisplay(arr));
    }
}
}
