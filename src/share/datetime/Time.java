package share.datetime;


public record Time(long second, int nanosecond) {

public Time {
    if (second < 0) {
        throw new RuntimeException(String.format(Shop.OUT_SECOND, second));
    } else if (nanosecond < 0 ||
               1000_000_000 <= nanosecond) {
        throw new RuntimeException(String.format(Shop.OUT_NANO, nanosecond));
    }
}


@Override
public boolean equals(Object datum) {
    if (datum instanceof Time t) {
        return second == t.second &&
               nanosecond == t.nanosecond;
    } else {
        return false;
    }
}

@Override
public String toString() {
    return String.format("#<time %d.%09d>", second, nanosecond);
}


}
