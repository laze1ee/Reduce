package share.datetime;


public class Time {

private final long second;
private final int nanosecond;

public Time(long second, int nanosecond) {
    if (second < 0) {
        throw new RuntimeException(String.format(Shop.INVALID_SEC, second));
    } else if (nanosecond < 0 ||
               1000_000_000 <= nanosecond) {
        throw new RuntimeException(String.format(Shop.INVALID_NANO, nanosecond));
    }
    this.second = second;
    this.nanosecond = nanosecond;
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


public long second() {
    return second;
}

public int nanosecond() {
    return nanosecond;
}
}
