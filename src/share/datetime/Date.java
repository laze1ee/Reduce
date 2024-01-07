package share.datetime;


import share.primitive.Feint;

import static share.primitive.Pm.*;


public class Date {

final Feint arr;


Date() {
    arr = makeFeint(9);
}

Date(int year, int month, int day_of_month, int hour, int minute, int second, int nano,
     int offset) {
    arr = makeFeint(9);
    feSet(arr, 0, year);
    feSet(arr, 1, month);
    feSet(arr, 2, day_of_month);
    feSet(arr, 3, Aid.dayWeek(year, month, day_of_month));
    feSet(arr, 4, hour);
    feSet(arr, 5, minute);
    feSet(arr, 6, second);
    feSet(arr, 7, nano);
    feSet(arr, 8, offset);
}

public int year() {
    return feRef(arr, 0);
}

public int month() {
    return feRef(arr, 1);
}

public int dayOfMonth() {
    return feRef(arr, 2);
}

public int dayOfWeek() {
    return feRef(arr, 3);
}

public int hour() {
    return feRef(arr, 4);
}

public int minute() {
    return feRef(arr, 5);
}

public int second() {
    return feRef(arr, 6);
}

public int nano() {
    return feRef(arr, 7);
}

public int offset() {
    return feRef(arr, 8);
}

@Override
public String toString() {
    return String.format("#<date %s %d-%02d-%02d %s %02d:%02d:%02d>",
                         Aid.stringOffset(feRef(arr, 8)), feRef(arr, 0), feRef(arr, 1), feRef(arr, 2),
                         Aid.stringWeek(feRef(arr, 3)), feRef(arr, 4), feRef(arr, 5), feRef(arr, 6));
}
}
