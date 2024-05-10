package reduce.share;

public class BinaryLabel {

public static final byte BOOLEAN_FALSE = (byte) 0x80;
public static final byte BOOLEAN_TRUE = (byte) 0x81;
public static final byte INT = (byte) 0x82;
public static final byte LONG = (byte) 0x83;
public static final byte DOUBLE = (byte) 0x84;
public static final byte CHAR = (byte) 0x85;
public static final byte STRING = (byte) 0x86;

public static final byte INTS = (byte) 0x90;
public static final byte LONGS = (byte) 0x91;
public static final byte DOUBLES = (byte) 0x92;

public static final byte INTACT = (byte) 0xA0;
public static final byte FRACTION = (byte) 0xA1;
public static final byte FLOAT64 = (byte) 0xA2;
public static final byte COMPLEX = (byte) 0xA3;

public static final byte SYMBOL = (byte) 0xB0;
public static final byte LOT_BEGIN = (byte) 0xB1;
public static final byte LOT_CONS = (byte) 0xB2;
public static final byte LOT_END = (byte) 0xB3;
public static final byte FEW = (byte) 0xB4;

public static final byte TIME = (byte) 0xC0;
public static final byte DATE = (byte) 0xC1;
}
