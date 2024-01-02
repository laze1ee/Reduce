package share.binary;

class Label {

static final byte BOOL = (byte) 0xA0;
static final byte INT = (byte) 0xA1;
static final byte LONG = (byte) 0xA2;
static final byte DOUBLE = (byte) 0xA3;
static final byte CHAR = (byte) 0xA4;
static final byte STRING = (byte) 0xA5;

static final byte FEBOOL = (byte) 0xA6;
static final byte FEINT = (byte) 0xA7;
static final byte FELONG = (byte) 0xA8;
static final byte FEDOUBLE = (byte) 0xA9;


static final byte TIME = (byte) 0xB0;
static final byte DATE = (byte) 0xB1;


static final byte LEST_BEGIN = (byte) 0xB8;
static final byte LEST_CONS = (byte) 0xB9;
static final byte LEST_END = (byte) 0xBA;
static final byte FEX = (byte) 0xBC;
}
