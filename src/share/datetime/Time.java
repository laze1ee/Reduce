package share.datetime;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;


public record Time(long second, int nanosecond) {

public Time(long second, int nanosecond) {
    if (nanosecond <= Aid.NEG_NANO ||
        Aid.POS_NANO <= nanosecond) {
        throw new RuntimeException
              (String.format(Shop.OUT_RANGE_NANO, nanosecond, Aid.NEG_NANO + 1, Aid.POS_NANO - 1));
    }
    if (second > 0 && nanosecond < 0) {
        this.second = second - 1;
        this.nanosecond = Aid.POS_NANO + nanosecond;
    } else if (second < 0 && nanosecond > 0) {
        this.second = second + 1;
        this.nanosecond = nanosecond - Aid.POS_NANO;
    } else {
        this.second = second;
        this.nanosecond = nanosecond;
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
    return String.format("#<time %d.%09d>", second, Math.abs(nanosecond));
}


@Contract(" -> new")
public @NotNull Time neg() {
    return new Time(second * -1, nanosecond * -1);
}

public @NotNull Date toDate(int offset) {
    if (Aid.checkTime(this)) {
        return Aid.timeToDate(this, offset);
    } else {
        throw new IllegalArgumentException(
        String.format("the time %s converting to date is not in range [%d %d]",
                      this, Aid.UTC_MIN, Aid.UTC_MAX));
    }
}


@Contract("_ -> new")
public static @NotNull Time current(TimeType type) {
    if (type == TimeType.UTC) {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        long second = utc.toEpochSecond();
        int nanosecond = utc.getNano();
        return new Time(second, nanosecond);
    } else if (type == TimeType.Monotonic) {
        long stamp = System.nanoTime();
        long second = stamp / Aid.POS_NANO;
        int nanosecond = (int) (stamp % Aid.POS_NANO);
        return new Time(second, nanosecond);
    } else {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long stamp = bean.getCurrentThreadCpuTime();
        long second = stamp / Aid.POS_NANO;
        int nanosecond = (int) (stamp % Aid.POS_NANO);
        return new Time(second, nanosecond);
    }
}

@Contract(" -> new")
public static @NotNull Time current() {
    return current(TimeType.UTC);
}

@Contract("_, _ -> new")
public static @NotNull Time add(@NotNull Time t1, @NotNull Time t2) {
    long second = t1.second + t2.second;
    int nanosecond = t1.nanosecond + t2.nanosecond;
    if (nanosecond >= Aid.POS_NANO) {
        return new Time(second + 1, nanosecond - Aid.POS_NANO);
    } else if (nanosecond <= Aid.NEG_NANO) {
        return new Time(second - 1, nanosecond + Aid.POS_NANO);
    } else {
        return new Time(second, nanosecond);
    }
}
}
