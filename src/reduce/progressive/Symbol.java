package reduce.progressive;

import org.jetbrains.annotations.NotNull;
import reduce.utility.RBTree;
import reduce.utility.CheckSum;

import java.nio.charset.StandardCharsets;

import static reduce.progressive.Pr.stringOf;


public class Symbol {

private static final RBTree tree = new RBTree(Mate::less, Mate::greater);

final int identifier;

public Symbol(@NotNull String str) {
    if (str.isEmpty() ||
        str.isBlank()) {
        throw new RuntimeException(String.format(Msg.INVALID_STRING, stringOf(str)));
    } else {
        int checksum = CheckSum.fletcher32(str.getBytes(StandardCharsets.UTF_8));
        if (RBTree.isPresent(Symbol.tree, checksum)) {
            String str2 = (String) RBTree.ref(Symbol.tree, checksum);
            if (!str.equals(str2)) {
                throw new RuntimeException(String.format(Msg.JACKPOT, str, str2));
            }
        } else {
            RBTree.insert(Symbol.tree, checksum, str);
        }
        identifier = checksum;
    }
}


@Override
public String toString() {
    String str = (String) RBTree.ref(tree, identifier);
    if (str.isEmpty()) {
        return "||";
    } else {
        StringBuilder builder = new StringBuilder();
        int sz = str.length();
        char c = str.charAt(0);
        if (Character.isDigit(c) || Mate.isScalar(c)) {
            builder.append(String.format("\\u%X;", (int) c));
        } else {
            builder.append(c);
        }
        for (int i = 1; i < sz; i = i + 1) {
            c = str.charAt(i);
            if (Mate.isScalar(c)) {
                builder.append(String.format("\\u%X;", (int) c));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Symbol sym) {
        return this.identifier == sym.identifier;
    } else {
        return false;
    }
}

@Override
public int hashCode() {
    return identifier;
}

public String toRaw() {
    return (String) RBTree.ref(tree, identifier);
}
}
