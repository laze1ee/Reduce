package share.datetime;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import share.primitive.Pm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;


public class Dt {

@Contract(value = "_, _ -> new", pure = true)
public static @NotNull Time makeTime(
long sec,
@Range(from = -999999999L, to = 999999999L) long nsec) {
    return new Time(sec, nsec);
}

public static @NotNull Time currentTime(TimeType type) {
    if (type == TimeType.UTC) {
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        long sec = utc.toEpochSecond();
        long nsec = utc.getNano();
        return makeTime(sec, nsec);
    } else if (type == TimeType.Monotonic) {
        long stamp = System.nanoTime();
        long sec = stamp / Aid.NANO_SEC;
        long nsec = stamp % Aid.NANO_SEC;
        return makeTime(sec, nsec);
    } else {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long stamp = bean.getCurrentThreadCpuTime();
        long sec = stamp / Aid.NANO_SEC;
        long nsec = stamp % Aid.NANO_SEC;
        return makeTime(sec, nsec);
    }
}

/**
 * @return a time object that applied {@link TimeType#UTC} by default.
 */
public static @NotNull Time currentTime() {
    return currentTime(TimeType.UTC);
}

public static boolean timeEqual(@NotNull Time t1, @NotNull Time t2) {
    if (t1.equals(t2)) {
        return true;
    } else {
        return t1.second() == t2.second() && t1.nano() == t2.nano();
    }
}

public static boolean timeLess(@NotNull Time t1, @NotNull Time t2) {
    if (t1.second() < t2.second()) {
        return true;
    } else if (t1.second() == t2.second()) {
        return t1.nano() < t2.nano();
    } else {
        return false;
    }
}

public static boolean timeGreat(@NotNull Time t1, @NotNull Time t2) {
    if (t1.second() > t2.second()) {
        return true;
    } else if (t1.second() == t2.second()) {
        return t1.nano() > t2.nano();
    } else {
        return false;
    }
}

@Contract("_ -> new")
public static @NotNull Time copyTime(@NotNull Time t) {
    return makeTime(t.second(), t.nano());
}

@Contract("_, _ -> new")
public static @NotNull Time timeAdd(@NotNull Time t1, @NotNull Time t2) {
    long sec = t1.second() + t2.second();
    long nsec = t1.nano() + t2.nano();
    if (nsec >= Aid.NANO_SEC) {
        return makeTime(sec + 1, nsec - Aid.NANO_SEC);
    } else if (nsec <= Aid.NANO_SEC * -1) {
        return makeTime(sec - 1, nsec + Aid.NANO_SEC);
    } else {
        return makeTime(sec, nsec);
    }
}

@Contract("_, _ -> new")
public static @NotNull Time timeSub(@NotNull Time t1, @NotNull Time t2) {
    return timeAdd(t1, Aid.negTime(t2));
}

/**
 * the date range from <b>{@code 0-01-01 00:00:00}</b> to <b>{@code 2000000000-01-01 00:00:00}</b>.
 */
@Contract("_, _, _, _, _, _, _, _ -> new")
public static @NotNull Date makeDate(int year, int month, int day_of_month, int hour, int minute,
                                     int second, int nanosecond, int offset) {
    Date date = new Date(year, month, day_of_month, hour, minute, second, nanosecond, offset);

    if (year < 0 || 2000000000 < year) {
        throw new IllegalArgumentException(
        String.format("year %d is out of range [0 2000000000]", year));
    }
    if (month < 1 || 12 < month) {
        throw new IllegalArgumentException(
        String.format("month %d is out of range [1 12]", month));
    }
    if (!Aid.checkDayMonth(year, month, day_of_month)) {
        throw new IllegalArgumentException(
        String.format("day %d-%02d-%02d is invalid", year, month, day_of_month));
    }
    if (hour < 0 || 23 < hour) {
        throw new IllegalArgumentException(
        String.format("hour %d is out of range [0 23]", hour));
    }
    if (minute < 0 || 59 < minute) {
        throw new IllegalArgumentException(
        String.format("minute %d is out of range [0 59]", minute));
    }
    if (second < 0 || 59 < second) {
        throw new IllegalArgumentException(
        String.format("second %d is out of range [0 59]", second));
    }
    if (nanosecond < 0 || 999999999 < nanosecond) {
        throw new IllegalArgumentException(
        String.format("nanosecond %d is out of range [0 999999999]", nanosecond));
    }
    if (offset < -64800 || 64800 < offset) {
        throw new IllegalArgumentException(
        String.format("zone offset %d is out of range [-64800 64800]", offset));
    }

    Aid.checkBoundary(date);

    return date;
}

/**
 * the date range from <b>{@code 0-01-01 00:00:00}</b> to <b>{@code 2000000000-01-01 00:00:00}</b>.
 *
 * @return a date object that applied current time-zone offset by default.
 */
public static @NotNull Date makeDate(int year, int month, int day_of_month, int hour, int minute,
                                     int second, int nanosecond) {
    int offset = OffsetTime.now().getOffset().getTotalSeconds();
    return new Date(year, month, day_of_month, hour, minute, second, nanosecond, offset);
}

/**
 * @param offset represents the time-zone offset in seconds east of UTC, its range -64800 to +64800.
 * @return a date object representing the current date.
 */
public static @NotNull Date currentDate(int offset) {
    String id = ZoneOffset.ofTotalSeconds(offset).getId();
    ZonedDateTime zone = ZonedDateTime.now(ZoneId.of(id));
    Date date = new Date();
    Pm.feSet(date.arr, 0, zone.getYear());
    Pm.feSet(date.arr, 1, zone.getMonth().getValue());
    Pm.feSet(date.arr, 2, zone.getDayOfMonth());
    Pm.feSet(date.arr, 3, zone.getDayOfWeek().getValue());
    Pm.feSet(date.arr, 4, zone.getHour());
    Pm.feSet(date.arr, 5, zone.getMinute());
    Pm.feSet(date.arr, 6, zone.getSecond());
    Pm.feSet(date.arr, 7, zone.getNano());
    Pm.feSet(date.arr, 8, offset);
    return date;
}

/**
 * @return a date object representing the current date, using current time-zone by default.
 */
public static @NotNull Date currentDate() {
    return currentDate(OffsetTime.now().getOffset().getTotalSeconds());
}

public static @NotNull Date timeToDate(@NotNull Time t, int offset) {
    if (Aid._check_time(t)) {
        return Aid.timeDate(t, offset);
    } else {
        throw new IllegalArgumentException(
        String.format("the time converted to date, must be in range [%d %d]",
        Aid.UTC_MIN, Aid.UTC_MAX));
    }
}

public static @NotNull Date timeToDate(@NotNull Time t) {
    int offset = OffsetTime.now().getOffset().getTotalSeconds();
    return timeToDate(t, offset);
}

public static @NotNull Time dateToTime(@NotNull Date d) {
    long days = Aid.sumDays(d.year(), d.month(), d.dayOfMonth());
    long secs_day = d.hour() * 3600L + d.minute() * 60L + d.second();
    long secs = days * 24 * 3600 + secs_day - d.offset() - Aid.COMPLEMENT;
    return makeTime(secs, d.nano());
}
}
