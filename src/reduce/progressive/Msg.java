package reduce.progressive;


class Msg {

// Symbol
static final String INVALID_STRING = "string %s is invalid";
static final String JACKPOT =
"congratulation! you encounter the same checksum with fletcher-32 but different strings %s and %s";

// Lot and Few
static final String LOT_EMPTY = "list () is empty";
static final String CYC_BREADTH = "%s is circular in breadth";
static final String FEW_INDEX_OUT = "index %s is out of range for few %s";
static final String LOT_INDEX_OUT = "index %s is out of range for lot %s";

// Comparison
static final String UNSUPPORTED_COMPARE = "unsupported array %s and %s for comparing";
static final String UNDEFINED_COMPARE = "undefined type %s and %s for comparing in size";

// Binary
static final String UNSUPPORTED = "unsupported data tye %s for coding";
static final String UNMATCHED = "unmatched label code %s for decoding";
}
