package reduce.progressive;

import org.jetbrains.annotations.NotNull;
import reduce.utility.RBTree;
import reduce.utility.Ut;

import java.nio.charset.StandardCharsets;

import static reduce.progressive.Pr.stringOf;


public class Symbol {

private static final RBTree catalog = new RBTree();

final int identifier;

public Symbol(@NotNull String str) {
    if (str.isEmpty() ||
        str.isBlank()) {
        throw new RuntimeException(String.format(Msg.INVALID_STRING, stringOf(str)));
    } else {
        int checksum = Ut.fletcher32(str.getBytes(StandardCharsets.UTF_8));
        if (RBTree.isPresent(Symbol.catalog, checksum)) {
            String str2 = (String) RBTree.ref(Symbol.catalog, checksum);
            if (!str.equals(str2)) {
                throw new RuntimeException(String.format(Msg.JACKPOT, str, str2));
            }
        } else {
            RBTree.insert(Symbol.catalog, checksum, str);
        }
        identifier = checksum;
    }
}


@Override
public String toString() {
    return (String) RBTree.ref(catalog, identifier);
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Symbol sym) {
        return this.identifier == sym.identifier;
    } else {
        return false;
    }
}


public int getIdentifier() {
    return identifier;
}

public String display() {
    String name = toString();
    if (name.isEmpty()) {
        return "||";
    } else {
        StringBuilder str = new StringBuilder();
        int sz = name.length();
        char c = name.charAt(0);
        if (Character.isDigit(c) || Mate.isCharPresent(c, Mate.occupant1) || Mate.isScalar(c)) {
            str.append(String.format("\\u%X;", (int) c));
        } else {
            str.append(c);
        }
        for (int i = 1; i < sz; i = i + 1) {
            c = name.charAt(i);
            if (Mate.isScalar(c)) {
                str.append(String.format("\\u%X;", (int) c));
            } else {
                str.append(c);
            }
        }
        return str.toString();
    }
}
}
