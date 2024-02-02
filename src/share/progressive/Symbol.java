package share.progressive;

import share.utility.RBTree;


public class Symbol {

static RBTree catalog = new RBTree();

final int identifier;


Symbol(int identifier) {
    this.identifier = identifier;
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
        if (Character.isDigit(c) || Aid.isCharPresent(c, Aid.occupant1) || Aid.isScalar(c)) {
            str.append(String.format("\\u%X;", (int) c));
        } else {
            str.append(c);
        }
        for (int i = 1; i < sz; i = i + 1) {
            c = name.charAt(i);
            if (Aid.isScalar(c)) {
                str.append(String.format("\\u%X;", (int) c));
            } else {
                str.append(c);
            }
        }
        return str.toString();
    }
}
}
