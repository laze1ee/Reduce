package share.primitive;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


public class Pm {

//region Constructor
@Contract(value = "_ -> new", pure = true)
public static @NotNull Febool febool(boolean... args) {
    return new Febool(args);
}

@Contract(value = "_ -> new", pure = true)
public static @NotNull Febyte febyte(byte... args) {
    return new Febyte(args);
}

@Contract(value = "_ -> new", pure = true)
public static @NotNull Feint feint(int... args) {
    return new Feint(args);
}

@Contract(value = "_ -> new", pure = true)
public static @NotNull Felong felong(long... args) {
    return new Felong(args);
}

@Contract(value = "_ -> new", pure = true)
public static @NotNull Fedouble fedouble(double... args) {
    return new Fedouble(args);
}

public static @NotNull Febool makeFebool(int amount, boolean value) {
    Febool bs = new Febool(new boolean[amount]);
    for (int i = 0; i < amount; i = i + 1) {
        feSet(bs, i, value);
    }
    return bs;
}

@Contract(value = "_ -> new", pure = true)
public static @NotNull Febool makeFebool(int amount) {
    return new Febool(new boolean[amount]);
}

public static @NotNull Febyte makeFebyte(int amount, byte value) {
    Febyte bs = new Febyte(new byte[amount]);
    for (int i = 0; i < amount; i = i + 1) {
        feSet(bs, i, value);
    }
    return bs;
}

@Contract(value = "_ -> new", pure = true)
public static @NotNull Febyte makeFebyte(int amount) {
    return new Febyte(new byte[amount]);
}

public static @NotNull Feint makeFeint(int amount, int value) {
    Feint ins = new Feint(new int[amount]);
    for (int i = 0; i < amount; i = i + 1) {
        feSet(ins, i, value);
    }
    return ins;
}

@Contract(value = "_ -> new", pure = true)
public static @NotNull Feint makeFeint(int amount) {
    return new Feint(new int[amount]);
}

public static @NotNull Felong makeFelong(int amount, long value) {
    Felong ls = new Felong(new long[amount]);
    for (int i = 0; i < amount; i = i + 1) {
        feSet(ls, i, value);
    }
    return ls;
}

@Contract(value = "_ -> new", pure = true)
public static @NotNull Felong makeFelong(int amount) {
    return new Felong(new long[amount]);
}

public static @NotNull Fedouble makeFedouble(int amount, double value) {
    Fedouble ds = new Fedouble(new double[amount]);
    for (int i = 0; i < amount; i = i + 1) {
        feSet(ds, i, value);
    }
    return ds;
}

@Contract(value = "_ -> new", pure = true)
public static @NotNull Fedouble makeFedouble(int amount) {
    return new Fedouble(new double[amount]);
}
//endregion


//region Visitor
public static int feLength(@NotNull Febool bs) {
    return bs.data.length;
}

public static int feLength(@NotNull Febyte bs) {
    return bs.data.length;
}

public static int feLength(@NotNull Feint ins) {
    return ins.data.length;
}

public static int feLength(@NotNull Felong ls) {
    return ls.data.length;
}

public static int feLength(@NotNull Fedouble ds) {
    return ds.data.length;
}

public static boolean feRef(Febool bs, int index) {
    if (0 <= index && index < feLength(bs)) {
        return bs.data[index];
    } else {
        throw new ArrayIndexOutOfBoundsException
              (String.format("index %d is out of range for febool[%d %d)", index, 0, feLength(bs)));
    }
}

public static byte feRef(Febyte bs, int index) {
    if (0 <= index && index < feLength(bs)) {
        return bs.data[index];
    } else {
        throw new ArrayIndexOutOfBoundsException
              (String.format("index %d is out of range for febyte[%d %d)", index, 0, feLength(bs)));
    }
}

public static int feRef(Feint ins, int index) {
    if (0 <= index && index < feLength(ins)) {
        return ins.data[index];
    } else {
        throw new ArrayIndexOutOfBoundsException
              (String.format("index %d is out of range for feint[%d %d)", index, 0, feLength(ins)));
    }
}

public static long feRef(Felong ls, int index) {
    if (0 <= index && index < feLength(ls)) {
        return ls.data[index];
    } else {
        throw new ArrayIndexOutOfBoundsException
              (String.format("index %d is out of range for felong[%d %d)", index, 0, feLength(ls)));
    }
}

public static double feRef(Fedouble ds, int index) {
    if (0 <= index && index < feLength(ds)) {
        return ds.data[index];
    } else {
        throw new ArrayIndexOutOfBoundsException
              (String.format("index %d is out of range for fedouble[%d %d)", index, 0, feLength(ds)));
    }
}
//endregion


//region Setter
public static void feSet(@NotNull Febool bs, int index, boolean value) {
    if (0 <= index && index < feLength(bs)) {
        bs.data[index] = value;
    } else {
        throw new ArrayIndexOutOfBoundsException
              (String.format("index %d is out of range for febool[%d %d)", index, 0, feLength(bs)));
    }
}

public static void feSet(@NotNull Febyte bs, int index, byte value) {
    if (0 <= index && index < feLength(bs)) {
        bs.data[index] = value;
    } else {
        throw new ArrayIndexOutOfBoundsException
              (String.format("index %d is out of range for febyte[%d %d)", index, 0, feLength(bs)));
    }
}

public static void feSet(@NotNull Feint ins, int index, int value) {
    if (0 <= index && index < feLength(ins)) {
        ins.data[index] = value;
    } else {
        throw new ArrayIndexOutOfBoundsException
              (String.format("index %d is out of range for feint[%d %d)", index, 0, feLength(ins)));
    }
}

public static void feSet(@NotNull Felong ls, int index, long value) {
    if (0 <= index && index < feLength(ls)) {
        ls.data[index] = value;
    } else {
        throw new ArrayIndexOutOfBoundsException
              (String.format("index %d is out of range for felong[%d %d)", index, 0, feLength(ls)));
    }
}

public static void feSet(@NotNull Fedouble ds, int index, double value) {
    if (0 <= index && index < feLength(ds)) {
        ds.data[index] = value;
    } else {
        throw new ArrayIndexOutOfBoundsException
              (String.format("index %d is out of range for fedouble[%d %d)", index, 0, feLength(ds)));
    }
}

public static void feFill(@NotNull Febool bs, boolean value) {
    Arrays.fill(bs.data, value);
}

public static void feFill(@NotNull Febyte bs, byte value) {
    Arrays.fill(bs.data, value);
}

public static void feFill(@NotNull Feint ins, int value) {
    Arrays.fill(ins.data, value);
}

public static void feFill(@NotNull Felong ls, long value) {
    Arrays.fill(ls.data, value);
}

public static void feFill(@NotNull Fedouble ds, double value) {
    Arrays.fill(ds.data, value);
}
//endregion


//region Copy
public static void feCopyInto(@NotNull Febool src, int src_pos, @NotNull Febool dest,
                              int dest_pos, int amount) {
    System.arraycopy(src.data, src_pos, dest.data, dest_pos, amount);
}

public static void feCopyInto(@NotNull Febyte src, int src_pos, @NotNull Febyte dest,
                              int dest_pos, int amount) {
    System.arraycopy(src.data, src_pos, dest.data, dest_pos, amount);
}

public static void feCopyInto(@NotNull Feint src, int src_pos, @NotNull Feint dest,
                              int dest_pos, int amount) {
    System.arraycopy(src.data, src_pos, dest.data, dest_pos, amount);
}

public static void feCopyInto(@NotNull Felong src, int src_pos, @NotNull Felong dest,
                              int dest_pos, int amount) {
    System.arraycopy(src.data, src_pos, dest.data, dest_pos, amount);
}

public static void feCopyInto(@NotNull Fedouble src, int src_pos, @NotNull Fedouble dest,
                              int dest_pos, int amount) {
    System.arraycopy(src.data, src_pos, dest.data, dest_pos, amount);
}
//endregion

}
