package share.progressive;


import share.utility.RBTree;


public class Symbol {

static final Few rbTree = RBTree.initialize();
static int count = 0;

final String name;
final int id;


Symbol(String name) {
    this.name = name;
    if (RBTree.isPresent(rbTree, this.name)) {
        id = (int) RBTree.ref(rbTree, this.name);
    } else {
        id = count;
        RBTree.insert(rbTree, this.name, id);
        count = count + 1;
    }
}


@Override
public String toString() {
    return name;
}


@Override
public boolean equals(Object obj) {
    if (obj instanceof Symbol sym) {
        return this.id == sym.id;
    } else {
        return false;
    }
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
