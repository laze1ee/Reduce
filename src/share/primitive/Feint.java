package share.primitive;

public class Feint {

final int[] arr;

Feint(int... args) {
    arr = args;
}


@Override
public String toString() {
    return String.format("#i32(%s)", Aid.display(arr, arr.length));
}

public String forErr() {
    if (arr.length <= 5) {
        return String.format("%s", this);
    } else {
        return String.format("#i32(%s...)", Aid.errDisplay(arr));
    }
}
}
