package share.datetime;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.primitive.Feint;

import static share.datetime.Dt.makeTime;
import static share.primitive.Pm.*;


class Aid {

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
static @NotNull Time negTime(@NotNull Time t) {
    return makeTime(t.second() * -1, t.nano() * -1);
}

static String stringOffset(int offset) {
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
static @NotNull String stringWeek(int day) {
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

static @NotNull Date timeDate(@NotNull Time t, int offset) {
    long secs = t.second() + offset + COMPLEMENT;
    long days = secs / SEC_DAY;
    long secs_day = secs % SEC_DAY;

    Feint d1 = Aid.trackDate(days);
    Feint d2 = Aid.trackTime(secs_day);

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

static int dayWeek(int year, int month, int day) {
    long days = sumDays(year, month, day);
    return trackDayWeek(days);
}

private static boolean isLeapYear(int year) {
    if (year % 4 == 0) {
        return year % 100 != 0 || year % 400 == 0;
    } else {
        return false;
    }
}

static @NotNull Feint trackDate(long days) {
    Feint degree = makeFeint(4);

    int days_400y = _400Years(degree, days);
    int days_100y = _100Years(degree, days_400y);
    int days_4y = _4Years(degree, days_100y);
    int days_1y = _1Year(degree, days_4y);

    int year = trackYear(degree);
    Feint tmp = trackMonthAndDay(days_1y + 1, isLeapYear(year));
    int month = feRef(tmp, 0);
    int day_month = feRef(tmp, 1);
    int day_week = trackDayWeek(days);
    return feint(year, month, day_month, day_week);
}

private static int _400Years(Feint degree, long days) {
    feSet(degree, 0, (int) (days / DAYS_400Y));
    return (int) (days % DAYS_400Y);
}

private static int _100Years(Feint degree, int days) {
    if (days <= DAYS_100Y + 1) {
        feSet(degree, 1, 0);
        return days;
    } else {
        int _days = days - 1;
        feSet(degree, 1, (int) (_days / DAYS_100Y));
        return (int) (_days % DAYS_100Y);
    }
}

private static int _4Years(Feint degree, int days) {
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

private static int _1Year(Feint degree, int days) {
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

private static int trackYear(Feint degree) {
    return feRef(degree, 0) * 400 + feRef(degree, 1) * 100 + feRef(degree, 2) * 4 + feRef(degree, 3);
}

private static @NotNull Feint trackMonthAndDay(int days, boolean leap) {
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

private static int trackDayWeek(long days) {
    long in_week = (days % 7 + START_DAY_OF_WEEK) % 7;
    return (int) (in_week + 1);
}

@Contract(pure = true)
static @NotNull Feint trackTime(long sec) {
    int hour = (int) (sec / 3600);
    int minute = (int) (sec % 3600 / 60);
    int second = (int) (sec % 3600 % 60);
    return feint(hour, minute, second);
}

static long sumDays(int year, int month, int day) {
    Feint degree = makeFeint(4);
    int y = year;
    feSet(degree, 0, y / 400);
    y = y % 400;
    feSet(degree, 1, y / 100);
    y = y % 100;
    feSet(degree, 2, y / 4);
    y = y % 4;
    feSet(degree, 3, y);

    long days = sumDaysYears(degree, year);
    days = days + sumDaysYear(year, month, day);
    return days;
}

private static long sumDaysYears(Feint degree, int year) {
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
        if (isLeapYear(year - feRef(degree, 3))) {
            days = days + feRef(degree, 3) * DAYS_Y + 1;
        } else {
            days = days + feRef(degree, 3) * DAYS_Y;
        }
    }
    return days;
}

private static int sumDaysYear(int year, int month, int day) {
    int i;
    if (isLeapYear(year)) {
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

static boolean checkDayMonth(int year, int month, int day_month) {
    int day;
    if (isLeapYear(year)) {
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

static void checkBoundary(Date d) {
    Time t = Dt.dateToTime(d);
    Date utc = timeDate(t, 0);
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
