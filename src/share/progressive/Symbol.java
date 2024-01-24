package share.progressive;

import share.utility.RBTree;


public class Symbol {

private static final RBTree tree = new RBTree();
private static int count = 1;

final String name;
final int identifier;


Symbol(String name) {
    this.name = name;
    if (RBTree.isPresent(tree, this.name)) {
        identifier = (int) RBTree.ref(tree, this.name);
    } else {
        identifier = count;
        RBTree.insert(tree, this.name, identifier);
        count = count + 1;
    }
}


@Override
public String toString() {
    return name;
}


@Override
public boolean equals(Object datum) {
    if (datum instanceof Symbol sym) {
        return this.identifier == sym.identifier;
    } else {
        return false;
    }
}


public int getId() {
    return identifier;
}

public String display() {
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
