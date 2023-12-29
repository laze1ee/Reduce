package share.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.progressive.Few;
import share.progressive.Lot;

import static share.progressive.Cmp.*;
import static share.progressive.Pg.*;


class RBTreeAid {

static boolean RED = true;
static boolean BLACK = false;


//region constructor
@Contract(value = " -> new", pure = true)
static @NotNull Few emptyNode() {
    return few(false, 0, false, few(), few());
}
//endregion


//region visitor
static Object getKey(Few node) {
    return ref0(node);
}

static Object getValue(Few node) {
    return ref1(node);
}

static boolean getColor(Few node) {
    return (boolean) ref2(node);
}

static Few getLeft(Few node) {
    return (Few) ref3(node);
}

static Few getRight(Few node) {
    return (Few) ref4(node);
}

static class $Finder {
    Object key;
    Few node;

    $Finder(Object key, Few node) {
        this.key = key;
        this.node = node;
    }

    Lot process() {
        return _aid(node, lot());
    }

    Lot _aid(Few node, Lot path) {
        if (isEmptyNode(node)) {
            return cons(node, path);
        } else if (less(key, getKey(node))) {
            return _aid(getLeft(node), cons(node, path));
        } else if (greater(key, getKey(node))) {
            return _aid(getRight(node), cons(node, path));
        } else {
            return cons(node, path);
        }
    }
}

static Lot minimum(Few node, Lot path) {
    if (isEmptyNode(node)) {
        return path;
    } else {
        Few left = getLeft(node);
        return minimum(left, cons(node, path));
    }
}

static Lot maximum(Few node, Lot path) {
    if (isEmptyNode(node)) {
        return path;
    } else {
        Few right = getRight(node);
        return maximum(right, cons(node, path));
    }
}

static String stringOfNode(Few node) {
    if (isEmptyNode(node)) {
        return "nil";
    } else {
        return
        String.format
              ("(%s %s %s %s %s)", getKey(node), getValue(node), _colorToString(getColor(node)),
              stringOfNode(getLeft(node)), stringOfNode(getRight(node)));
    }
}

@Contract(pure = true)
private static @NotNull String _colorToString(boolean color) {
    if (color) {
        return "red";
    } else {
        return "black";
    }
}
//endregion


//region predictor
static boolean isEmptyNode(Few node) {
    return 5 == length(node) &&
           getKey(node) instanceof Boolean &&
           isBlack(getColor(node)) &&
           0 == length(getLeft(node)) &&
           0 == length(getRight(node));
}

static boolean isNode(Few node) {
    if (isEmptyNode(node)) {
        return true;
    } else {
        return isNode(getLeft(node)) &&
               isNode(getRight(node));
    }
}

static boolean isRed(boolean color) {
    return color;
}

static boolean isBlack(boolean color) {
    return !color;
}

static boolean isLeft(Few node, Object branch) {
    return eq(getLeft(node), branch);
}

static boolean isRight(Few node, Object branch) {
    return eq(getRight(node), branch);
}
//endregion


//region  setter
static void setKey(Few node, Object key) {
    set0(node, key);
}

static void setValue(Few node, Object value) {
    set1(node, value);
}

static void setColor(Few node, boolean color) {
    set2(node, color);
}

static void setLeft(Few node, Few branch) {
    set3(node, branch);
}

static void setRight(Few node, Few branch) {
    set4(node, branch);
}

static void leftRotate(Few tree, Lot path) {
    Few x = (Few) car(path);
    Few y = getRight(x);
    if (isEmptyNode(y)) {
        throw new RuntimeException(String.format("right branch is empty in %s", x));
    }
    Few b = getLeft(y);

    setLeft(y, x);
    setRight(x, b);

    if (isNull(cdr(path))) {
        set0(tree, y);
    } else {
        Few p = (Few) cadr(path);
        if (isLeft(p, x)) {
            setLeft(p, y);
        } else {
            setRight(p, y);
        }
    }
}

static void rightRotate(Few tree, Lot path) {
    Few y = (Few) car(path);
    Few x = getLeft(y);
    if (isEmptyNode(x)) {
        throw new RuntimeException(String.format("left branch is empty in %s", y));
    }
    Few b = getRight(x);

    setRight(x, y);
    setLeft(y, b);

    if (isNull(cdr(path))) {
        set0(tree, x);
    } else {
        Few p = (Few) cadr(path);
        if (isRight(p, y)) {
            setRight(p, x);
        } else {
            setLeft(p, x);
        }
    }
}

static class $InsertFixer {
    Few tree;
    Lot path;

    $InsertFixer(Few tree, Lot path) {
        this.tree = tree;
        this.path = path;
    }

    void process() {
        _aid(path);
        setColor((Few) ref0(tree), BLACK);
    }

    void _aid(Lot path) {
        if (2 < length(path) && isRed(getColor((Few) lotRef(path, 1)))) {
            Few p = (Few) lotRef(path, 1);
            Few pp = (Few) lotRef(path, 2);
            if (isLeft(pp, p)) {
                Few u = getRight(pp);
                if (isRed(getColor(u))) {
                    setColor(p, BLACK);
                    setColor(u, BLACK);
                    setColor(pp, RED);
                    _aid(cddr(path));
                } else {
                    if (isRight(p, car(path))) {
                        leftRotate(tree, cdr(path));
                        p = (Few) car(path);
                    }
                    rightRotate(tree, cddr(path));
                    setColor(p, BLACK);
                    setColor(pp, RED);
                }
            } else {
                Few u = getLeft(pp);
                if (isRed(getColor(u))) {
                    setColor(p, BLACK);
                    setColor(u, BLACK);
                    setColor(pp, RED);
                    _aid(cddr(path));
                } else {
                    if (isLeft(p, car(path))) {
                        rightRotate(tree, cdr(path));
                        p = (Few) car(path);
                    }
                    leftRotate(tree, cddr(path));
                    setColor(p, BLACK);
                    setColor(pp, RED);
                }
            }
        }
    }
}

static void transplant(Few tree, Lot path, Few node) {
    if (1 == length(path)) {
        set0(tree, node);
    } else {
        Few p = (Few) lotRef(path, 1);
        if (isLeft(p, car(path))) {
            setLeft(p, node);
        } else {
            setRight(p, node);
        }
    }
}

static void delete(Object key, Few tree) {
    $Finder moo = new $Finder(key, (Few) ref0(tree));
    Lot path = moo.process();
    Few deleted = (Few) car(path);
    if (isEmptyNode(deleted)) {
        throw new RuntimeException
              (String.format("key %s is not existed in tree %s", key, tree));
    } else {
        boolean color = getColor(deleted);
        Few x;
        if (isEmptyNode(getLeft(deleted))) {
            x = getRight(deleted);
            transplant(tree, path, x);
            path = cons(x, cdr(path));
        } else if (isEmptyNode(getRight(deleted))) {
            x = getLeft(deleted);
            transplant(tree, path, x);
            path = cons(x, cdr(path));
        } else {
            Lot path2 = minimum(getRight(deleted), lot());
            Few alternate = (Few) car(path2);
            color = getColor(alternate);
            x = getRight(alternate);
            if (!isRight(deleted, alternate)) {
                transplant(tree, path2, x);
                setRight(alternate, getRight(deleted));
            }
            transplant(tree, path, alternate);
            setLeft(alternate, getLeft(deleted));
            setColor(alternate, getColor(deleted));
            path = append(cons(x, cdr(path2)), cons(alternate, cdr(path)));
        }
        if (isBlack(color)) {
            $DeleteFixer fixer = new $DeleteFixer(tree, path);
            fixer.process();
        }
    }
}

static class $DeleteFixer {
    Few tree;
    Lot path;
    Few x;

    $DeleteFixer(Few tree, Lot path) {
        this.tree = tree;
        this.path = path;
    }

    void process() {
        x = (Few) car(path);
        _aid(cdr(path));
        setColor(x, BLACK);
    }

    void _aid(Lot path) {
        if (!isNull(path) && isBlack(getColor(x))) {
            Few p = (Few) car(path);
            if (isLeft(p, x)) {
                Few s = getRight(p);
                if (isRed(getColor(s))) {
                    leftRotate(tree, path);
                    setColor(s, BLACK);
                    setColor(p, RED);
                    path = cons(p, cons(s, cdr(path)));
                    s = getRight(p);
                }
                if (isBlack(getColor(getLeft(s))) && isBlack(getColor(getRight(s)))) {
                    setColor(s, RED);
                    x = p;
                    _aid(cdr(path));
                } else {
                    if (isBlack(getColor(getRight(s)))) {
                        rightRotate(tree, cons(s, path));
                        setColor(s, RED);
                        setColor(getRight(p), BLACK);
                        s = getRight(p);
                    }
                    leftRotate(tree, path);
                    setColor(s, getColor(p));
                    setColor(p, BLACK);
                    setColor(getRight(s), BLACK);
                }
            } else {
                Few s = getLeft(p);
                if (isRed(getColor(s))) {
                    rightRotate(tree, path);
                    setColor(s, BLACK);
                    setColor(p, RED);
                    path = cons(p, cons(s, cdr(path)));
                    s = getLeft(p);
                }
                if (isBlack(getColor(getRight(s))) && isBlack(getColor(getLeft(s)))) {
                    setColor(s, RED);
                    x = p;
                    _aid(cdr(path));
                } else {
                    if (isBlack(getColor(getLeft(s)))) {
                        leftRotate(tree, cons(s, path));
                        setColor(s, RED);
                        setColor(getLeft(p), BLACK);
                        s = getLeft(p);
                    }
                    rightRotate(tree, path);
                    setColor(s, getColor(p));
                    setColor(p, BLACK);
                    setColor(getLeft(s), BLACK);
                }
            }
        }
    }
}
//endregion

}
