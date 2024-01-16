package share.binary;

class Label {

static final byte BOOLEAN = (byte) 0x80;
static final byte INT = (byte) 0x81;
static final byte LONG = (byte) 0x82;
static final byte DOUBLE = (byte) 0x83;
static final byte CHAR = (byte) 0x84;
static final byte STRING = (byte) 0x85;

static final byte BOOLEAN_ARR = (byte) 0x87;
static final byte INT_ARR = (byte) 0x88;
static final byte LONG_ARR = (byte) 0x89;
static final byte DOUBLE_ARR = (byte) 0x8A;

static final byte LOT_BEGIN = (byte) 0x8C;
static final byte LOT_CONS = (byte) 0x8D;
static final byte LOT_END = (byte) 0x8E;
static final byte FEW = (byte) 0x8F;

static final byte TIME = (byte) 0x91;
static final byte DATE = (byte) 0x92;
}
