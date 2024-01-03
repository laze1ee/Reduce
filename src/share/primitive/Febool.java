package share.primitive;


public class Febool {

final boolean[] arr;

Febool(boolean... args) {
    arr = args;
}

@Override
public String toString() {
    return String.format("#1(%s)", Aid.display(arr, arr.length));
}

public String forErr() {
    if (arr.length <= 5) {
        return String.format("%s", this);
    } else {
        return String.format("#1(%s...)", Aid.errDisplay(arr));
    }
}
}
