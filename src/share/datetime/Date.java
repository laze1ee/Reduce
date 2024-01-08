package share.datetime;


public class Date {

private final int year;
private final byte month;
private final byte day_of_month;
private final byte day_of_week;
private final byte hour;
private final byte minute;
private final byte second;
private final int nanosecond;
private final int offset;

public Date(int year, int month, int day_of_month, int hour, int minute, int second,
            int nanosecond, int offset) {
    this.year = year;
    this.month = (byte) month;
    this.day_of_month = (byte) day_of_month;
    this.day_of_week = 8;
    this.hour = (byte) hour;
    this.minute = (byte) minute;
    this.second = (byte) second;
    this.nanosecond = nanosecond;
    this.offset = offset;
}


@Override
public boolean equals(Object datum) {
    if (datum instanceof Date d) {
        return d.year == year &&
               month == d.month &&
               day_of_month == d.day_of_month &&
               hour == d.hour &&
               minute == d.minute &&
               second == d.second &&
               nanosecond == d.nanosecond &&
               offset == d.offset;
    } else {
        return false;
    }
}

@Override
public String toString() {
    return String.format("#<date %s %d-%02d-%02d %s %02d:%02d:%02d>",
                         offset, year, month, day_of_month, day_of_week, hour, minute, second);
}


public int year() {
    return year;
}

public int month() {
    return month;
}

public int dayOfMonth() {
    return day_of_month;
}

public int dayOfWeek() {
    return day_of_month;
}

public int hour() {
    return hour;
}

public int minute() {
    return minute;
}

public int second() {
    return second;
}

public int nanosecond() {
    return nanosecond;
}

public int offset() {
    return offset;
}
}
