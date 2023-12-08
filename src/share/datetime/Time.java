package share.datetime;

public class Time {

private final long sec;
private final long nsec;


Time(long second, long nanosecond) {
    if (0 < second && nanosecond < 0) {
        sec = second - 1;
        nsec = DtAid.NANO_SEC + nanosecond;
    } else if (0 > second && nanosecond > 0) {
        sec = second + 1;
        nsec = nanosecond - DtAid.NANO_SEC;
    } else {
        sec = second;
        nsec = nanosecond;
    }
}

public long second() {
    return sec;
}

public long nano() {
    return nsec;
}

@Override
public String toString() {
    if (sec < 0 || nsec < 0) {
        return String.format("#<time -%d.%09d", sec * -1, nsec * -1);
    } else {
        return String.format("#<time %d.%09d>", sec, nsec);
    }
}
}
