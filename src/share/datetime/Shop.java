package share.datetime;


class Shop {

// Time
static final String OUT_SECOND = "%s is out of range [0 +inf.0)";
static final String OUT_NANO = "%s is out of range [0 999_999_999]";

// Date
static final String OUT_YEAR = "year %d is out of range [0 2000000000]";
static final String OUT_MONTH = "month %d is out of range [1 12]";
static final String OUT_DAY = "day of month %d-%02d-%02d is invalid";
static final String OUT_HOUR = "hour %d is out of range [0 23]";
static final String OUT_MINUTE = "minute %d is out of range [0 59]";
static final String OUT_SEC = "second %d is out of range [0 59]";
static final String OUT_OFFSET = "zone offset %d is out of range [-64800 64800]";
}
