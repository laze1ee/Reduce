package reduce.datetime;

class Shop {

// Time
static final String OUT_RANGE_NANO = "\nnanosecond %s is out of range [%s %s]";

// Date
static final String OUT_YEAR = "\nyear %d is out of range [0 2000000000]";
static final String OUT_MONTH = "\nmonth %d is out of range [1 12]";
static final String OUT_DAY = "\nday of month %d-%02d-%02d is invalid";
static final String OUT_HOUR = "\nhour %d is out of range [0 23]";
static final String OUT_MINUTE = "\nminute %d is out of range [0 59]";
static final String OUT_SEC = "\nsecond %d is out of range [0 59]";
static final String OUT_NANO = "\nnanosecond %s is out of range [0 999999999]";
static final String OUT_OFFSET = "\nzone offset %d is out of range [-64800 64800]";
}
