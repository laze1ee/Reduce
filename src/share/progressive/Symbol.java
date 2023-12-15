package share.progressive;

import share.utility.Ut;


public class Symbol {

static Few rbTree = Ut.emptyRBTree();
static int count = 0;

final String name;
int id;


Symbol(String name) {
    this.name = name;
    if (Ut.isKeyExistInRBTree(rbTree, this.name)) {
        id = (int) Ut.refRBTree(rbTree, this.name);
    } else {
        id = count;
        Ut.insertRBTree(rbTree, this.name, id);
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
        if (Character.isDigit(c) || PgAid.charExist(c, PgAid.occupant1) || PgAid.isScalar(c)) {
            str.append(String.format("\\u%X;", (int) c));
        } else {
            str.append(c);
        }
        for (int i = 1; i < sz; i = i + 1) {
            c = name.charAt(i);
            if (PgAid.isScalar(c)) {
                str.append(String.format("\\u%X;", (int) c));
            } else {
                str.append(c);
            }
        }
        return str.toString();
    }
}
}
