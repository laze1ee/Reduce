package share.utility;


import static share.progressive.Pg.eq;
import static share.progressive.Pg.equal;
import static share.utility.Ut.stringOf;


class RBNode {

Object key;
Object value;
boolean color;
RBNode left;
RBNode right;


RBNode() {
    key = null;
    value = null;
    color = false;
    left = null;
    right = null;
}


@Override
public boolean equals(Object datum) {
    if (datum instanceof RBNode RBNode) {
        return equal(key, RBNode.key) &&
               equal(value, RBNode.value) &&
               eq(color, RBNode.color) &&
               left.equals(RBNode.left) &&
               right.equals(RBNode.right);
    } else {
        return false;
    }
}

@Override
public String toString() {
    if (this.isEmpty()) {
        return "nil";
    } else {
        return String.format("(%s %s %s %s %s)", stringOf(key), stringOf(value),
                             RBTreeAid.stringOfColor(color), left, right);
    }
}


public boolean isRed() {
    return color;
}

public boolean isBlack() {
    return !color;
}

public boolean isEmpty() {
    return key == null &&
           value == null &&
           !color &&
           left == null &&
           right == null;
}
}
