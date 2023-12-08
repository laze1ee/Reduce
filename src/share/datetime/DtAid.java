package share.datetime;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.primitive.Feint;

import static share.datetime.Dt.makeTime;
import static share.primitive.Pm.*;


class DtAid {

static final long NANO_SEC = 1_000_000_000L;

private static final long DAYS_Y = 365;
private static final long DAYS_4Y = 1461;
private static final long DAYS_100Y = 36524;
private static final long DAYS_400Y = 146097;

static final long SEC_DAY = 86400;
static final long COMPLEMENT = 62167219200L;

private static final Feint INC_DAYS = feint(
0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365,
0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366);

private static final int START_DAY_OF_WEEK = 5;


@Contract(value = "_ -> new", pure = true)
static @NotNull Time _neg_time(@NotNull Time t) {
    return makeTime(t.second() * -1, t.nano() * -1);
}

static String _string_offset(int offset) {
    int n = offset;
    String sign;
    if (n == 0) {
        sign = "";
    } else if (n < 0) {
        sign = "-";
        n = n * -1;
    } else {
        sign = "+";
    }
    int hour = n / 3600;
    int minute = (n % 3600) % 60;
    return String.format("%s%02d:%02d", sign, hour, minute);
}

@Contract(pure = true)
static @NotNull String _string_week(int day) {
    if (day == 1) {
        return "Monday";
    } else if (day == 2) {
        return "Tuesday";
    } else if (day == 3) {
        return "Wednesday";
    } else if (day == 4) {
        return "Thursday";
    } else if (day == 5) {
        return "Friday";
    } else if (day == 6) {
        return "Saturday";
    } else {
        return "Sunday";
    }
}

static @NotNull Date _time_date(@NotNull Time t, int offset) {
    long secs = t.second() + offset + COMPLEMENT;
    long days = secs / SEC_DAY;
    long secs_day = secs % SEC_DAY;

    Feint d1 = DtAid._track_date(days);
    Feint d2 = DtAid._track_time(secs_day);

    Date date = new Date();
    feSet(date.arr, 0, feRef(d1, 0));
    feSet(date.arr, 1, feRef(d1, 1));
    feSet(date.arr, 2, feRef(d1, 2));
    feSet(date.arr, 3, feRef(d1, 3));
    feSet(date.arr, 4, feRef(d2, 0));
    feSet(date.arr, 5, feRef(d2, 1));
    feSet(date.arr, 6, feRef(d2, 2));
    feSet(date.arr, 7, (int) t.nano());
    feSet(date.arr, 8, offset);
    return date;
}

static int _day_week(int year, int month, int day) {
    long days = _sum_days(year, month, day);
    return _track_day_week(days);
}

private static boolean _is_leap_year(int year) {
    if (year % 4 == 0) {
        return year % 100 != 0 || year % 400 == 0;
    } else {
        return false;
    }
}

static @NotNull Feint _track_date(long days) {
    Feint degree = makeFeint(4);

    int days_400y = _400years(degree, days);
    int days_100y = _100years(degree, days_400y);
    int days_4y = _4years(degree, days_100y);
    int days_1y = _1year(degree, days_4y);

    int year = _track_year(degree);
    Feint tmp = _track_month_and_day(days_1y + 1, _is_leap_year(year));
    int month = feRef(tmp, 0);
    int day_month = feRef(tmp, 1);
    int day_week = _track_day_week(days);
    return feint(year, month, day_month, day_week);
}

private static int _400years(Feint degree, long days) {
    feSet(degree, 0, (int) (days / DAYS_400Y));
    return (int) (days % DAYS_400Y);
}

private static int _100years(Feint degree, int days) {
    if (days <= DAYS_100Y + 1) {
        feSet(degree, 1, 0);
        return days;
    } else {
        int _days = days - 1;
        feSet(degree, 1, (int) (_days / DAYS_100Y));
        return (int) (_days % DAYS_100Y);
    }
}

private static int _4years(Feint degree, int days) {
    if (feRef(degree, 1) == 0) {
        feSet(degree, 2, (int) (days / DAYS_4Y));
        return (int) (days % DAYS_4Y);
    } else if (days <= DAYS_4Y - 1) {
        feSet(degree, 2, 0);
        return days;
    } else {
        int _days = days + 1;
        feSet(degree, 2, (int) (_days / DAYS_4Y));
        return (int) (_days % DAYS_4Y);
    }
}

private static int _1year(Feint degree, int days) {
    if (feRef(degree, 1) > 0 && feRef(degree, 2) == 0) {
        feSet(degree, 3, days / 365);
        return days % 365;
    } else if (days == 366) {
        feSet(degree, 3, 1);
        return 0;
    } else if (days < 366) {
        feSet(degree, 3, 0);
        return days;
    } else {
        int _days = days - 1;
        feSet(degree, 3, _days / 365);
        return _days % 365;
    }
}

private static int _track_year(Feint degree) {
    return feRef(degree, 0) * 400 + feRef(degree, 1) * 100 + feRef(degree, 2) * 4 + feRef(degree, 3);
}

private static @NotNull Feint _track_month_and_day(int days, boolean leap) {
    int i;
    if (leap) {
        i = 1;
    } else {
        i = 0;
    }
    for (int j = 1; j < 13; j = j + 1) {
        if (days <= feRef(INC_DAYS, i * 13 + j)) {
            int day_month = days - feRef(INC_DAYS, i * 13 + j - 1);
            return feint(j, day_month);
        }
    }
    throw new IllegalArgumentException(String.format("days %d is invalid", days));
}

private static int _track_day_week(long days) {
    long in_week = (days % 7 + START_DAY_OF_WEEK) % 7;
    return (int) (in_week + 1);
}

@Contract(pure = true)
static @NotNull Feint _track_time(long sec) {
    int hour = (int) (sec / 3600);
    int minute = (int) (sec % 3600 / 60);
    int second = (int) (sec % 3600 % 60);
    return feint(hour, minute, second);
}

static long _sum_days(int year, int month, int day) {
    Feint degree = makeFeint(4);
    int y = year;
    feSet(degree, 0, y / 400);
    y = y % 400;
    feSet(degree, 1, y / 100);
    y = y % 100;
    feSet(degree, 2, y / 4);
    y = y % 4;
    feSet(degree, 3, y);

    long days = _sum_days_years(degree, year);
    days = days + _sum_days_year(year, month, day);
    return days;
}

private static long _sum_days_years(Feint degree, int year) {
    long days = feRef(degree, 0) * DAYS_400Y;

    if (feRef(degree, 1) > 0) {
        days = days + feRef(degree, 1) * DAYS_100Y + 1;
    }

    if (feRef(degree, 2) > 0) {
        if (feRef(degree, 1) == 0) {
            days = days + feRef(degree, 2) * DAYS_4Y;
        } else {
            days = days + feRef(degree, 2) * DAYS_4Y - 1;
        }
    }

    if (feRef(degree, 3) > 0) {
        if (_is_leap_year(year - feRef(degree, 3))) {
            days = days + feRef(degree, 3) * DAYS_Y + 1;
        } else {
            days = days + feRef(degree, 3) * DAYS_Y;
        }
    }
    return days;
}

private static int _sum_days_year(int year, int month, int day) {
    int i;
    if (_is_leap_year(year)) {
        i = 1;
    } else {
        i = 0;
    }
    int days = feRef(INC_DAYS, i * 13 + month - 1);
    return days + day - 1;
}

private static final Feint DAY_YEAR = feint(
0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31,
0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31);

static boolean _check_day_month(int year, int month, int day_month) {
    int day;
    if (_is_leap_year(year)) {
        day = feRef(DAY_YEAR, month);
    } else {
        day = feRef(DAY_YEAR, 13 + month);
    }
    return day_month <= day;
}

static final long UTC_MIN = -62167219200L;
static final long UTC_MAX = 63113841832780800L;

static boolean _check_time(@NotNull Time t) {
    return COMPLEMENT * -1 <= t.second() && t.second() <= UTC_MAX;
}

static void _check_boundary(Date d) {
    Time t = Dt.dateToTime(d);
    Date utc = _time_date(t, 0);
    int year = utc.year();
    int month = utc.month();
    int day_of_month = utc.dayOfMonth();
    int hour = utc.hour();
    int minute = utc.minute();
    int second = utc.second();
    int nano = utc.nano();
    if (2000000000 == year) {
        if (month != 1 || day_of_month != 1 || hour != 0 || minute != 0 || second != 0 || nano != 0) {
            throw new IllegalArgumentException("overflow boundary");
        }
    }
}
}
