package share.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.progressive.Lot;
import share.refmethod.Has;

import static share.progressive.Pr.*;


@SuppressWarnings("SuspiciousNameCombination")
class RBTreeAid {

@Contract(pure = true)
static @NotNull String stringOfColor(boolean color) {
    if (color) {
        return "red";
    } else {
        return "black";
    }
}


static @NotNull Lot pathOf(RBNode node, Object key) {
    Lot path = lot();
    RBNode moo = node;
    while (!moo.isEmpty()) {
        if (less(key, moo.key)) {
            path = cons(moo, path);
            moo = moo.left;
        } else if (greater(key, moo.key)) {
            path = cons(moo, path);
            moo = moo.right;
        } else {
            return cons(moo, path);
        }
    }
    return cons(moo, path);
}

static boolean isLeftOf(Object node1, @NotNull RBNode node2) {
    return eq(node1, node2.left);
}

static boolean isRightOf(Object node1, @NotNull RBNode node2) {
    return eq(node1, node2.right);
}

static void leftRotate(RBTree tree, Lot path) {
    RBNode x = (RBNode) car(path);
    RBNode y = x.right;
    if (y.isEmpty()) {
        throw new RuntimeException(String.format("right branch is empty in node %s", x));
    }
    RBNode b = y.left;

    y.left = x;
    x.right = b;

    if (isNull(cdr(path))) {
        tree.root = y;
    } else {
        RBNode p = (RBNode) cadr(path);
        if (isLeftOf(x, p)) {
            p.left = y;
        } else {
            p.right = y;
        }
    }
}

static void rightRotate(RBTree tree, Lot path) {
    RBNode x = (RBNode) car(path);
    RBNode y = x.left;
    if (y.isEmpty()) {
        throw new RuntimeException(String.format("left branch is empty in node %s", x));
    }
    RBNode b = y.right;

    y.right = x;
    x.left = b;

    if (isNull(cdr(path))) {
        tree.root = y;
    } else {
        RBNode p = (RBNode) cadr(path);
        if (isLeftOf(x, p)) {
            p.left = y;
        } else {
            p.right = y;
        }
    }
}


static Lot minimum(RBNode node, Lot path) {
    RBNode moo = node;
    while (!moo.isEmpty()) {
        path = cons(moo, path);
        moo = moo.left;
    }
    return path;
}

static Lot maximum(RBNode node, Lot path) {
    RBNode moo = node;
    while (!moo.isEmpty()) {
        path = cons(moo, path);
        moo = moo.right;
    }
    return path;
}


record InsertFixer(RBTree tree, Lot path) {

    void process() {
        _aid(path);
        tree.root.color = false;
    }

    void _aid(Lot path) {
        if (2 < length(path) && ((RBNode) cadr(path)).isRed()) {
            RBNode p = (RBNode) lotRef(path, 1);
            RBNode pp = (RBNode) lotRef(path, 2);
            if (isLeftOf(p, pp)) {
                RBNode u = pp.right;
                if (u.isRed()) {
                    p.color = false;
                    u.color = false;
                    pp.color = true;
                    _aid(cddr(path));
                } else {
                    if (isRightOf(car(path), p)) {
                        leftRotate(tree, cdr(path));
                        p = (RBNode) car(path);
                    }
                    rightRotate(tree, cddr(path));
                    p.color = false;
                    pp.color = true;
                }
            } else {
                RBNode u = pp.left;
                if (u.isRed()) {
                    p.color = false;
                    u.color = false;
                    pp.color = true;
                    _aid(cddr(path));
                } else {
                    if (isLeftOf(car(path), p)) {
                        rightRotate(tree, cdr(path));
                        p = (RBNode) car(path);
                    }
                    leftRotate(tree, cddr(path));
                    p.color = false;
                    pp.color = true;
                }
            }
        }
    }
}


static void transplant(RBTree tree, Lot path, RBNode node) {
    if (1 == length(path)) {
        tree.root = node;
    } else {
        RBNode p = (RBNode) lotRef(path, 1);
        if (isLeftOf(car(path), p)) {
            p.left = node;
        } else {
            p.right = node;
        }
    }
}

static void delete(@NotNull RBTree tree, Object key) {
    Lot path = pathOf(tree.root, key);
    RBNode deleted = (RBNode) car(path);
    if (deleted.isEmpty()) {
        throw new RuntimeException(String.format("key %s is not present in tree %s", key, tree));
    } else {
        boolean color = deleted.color;
        RBNode x;
        if (deleted.left.isEmpty()) {
            x = deleted.right;
            transplant(tree, path, x);
            path = cons(x, cdr(path));
        } else if (deleted.right.isEmpty()) {
            x = deleted.left;
            transplant(tree, path, x);
            path = cons(x, cdr(path));
        } else {
            Lot path2 = minimum(deleted.right, lot());
            RBNode alternate = (RBNode) car(path2);
            color = alternate.color;
            x = alternate.right;
            if (!isRightOf(alternate, deleted)) {
                transplant(tree, path2, x);
                alternate.right = deleted.right;
            }
            transplant(tree, path, alternate);
            alternate.left = deleted.left;
            alternate.color = deleted.color;
            path = append(cons(x, cdr(path2)), cons(alternate, cdr(path)));
        }
        if (!color) {       // is the deleted color black?
            DeleteFixer fixer = new DeleteFixer(tree, path);
            fixer.process();
        }
    }
}

static class DeleteFixer {

    final RBTree tree;
    final Lot path;
    RBNode x;

    DeleteFixer(RBTree tree, Lot path) {
        this.tree = tree;
        this.path = path;
    }

    void process() {
        x = (RBNode) car(path);
        _aid(cdr(path));
        x.color = false;
    }

    void _aid(Lot path) {
        if (!isNull(path) && x.isBlack()) {
            RBNode p = (RBNode) car(path);
            if (isLeftOf(x, p)) {
                RBNode s = p.right;
                if (s.isRed()) {
                    leftRotate(tree, path);
                    s.color = false;
                    p.color = true;
                    path = cons(p, cons(s, cdr(path)));
                    s = p.right;
                }
                if (s.left.isBlack() && s.right.isBlack()) {
                    s.color = true;
                    x = p;
                    _aid(cdr(path));
                } else {
                    if (s.right.isBlack()) {
                        rightRotate(tree, cons(s, path));
                        s.color = true;
                        p.right.color = false;
                        s = p.right;
                    }
                    leftRotate(tree, path);
                    s.color = p.color;
                    p.color = false;
                    s.right.color = false;
                }
            } else {
                RBNode s = p.left;
                if (s.isRed()) {
                    rightRotate(tree, path);
                    s.color = false;
                    p.color = true;
                    path = cons(p, cons(s, cdr(path)));
                    s = p.left;
                }
                if (s.left.isBlack() && s.right.isBlack()) {
                    s.color = true;
                    x = p;
                    _aid(cdr(path));
                } else {
                    if (s.left.isBlack()) {
                        leftRotate(tree, cons(s, path));
                        s.color = true;
                        p.left.color = false;
                        s = p.left;
                    }
                    rightRotate(tree, path);
                    s.color = p.color;
                    p.color = false;
                    s.left.color = false;
                }
            }
        }
    }
}


static class Travel {

    Lot col;

    Travel() {
        col = lot();
    }

    Lot process(RBNode node) {
        _aid(node);
        return col;
    }

    void _aid(@NotNull RBNode node) {
        if (!node.isEmpty()) {
            _aid(node.right);
            col = cons(node.value, col);
            _aid(node.left);
        }
    }
}

static class Filter {

    Lot col;
    final Has pred;

    Filter(Has pred) {
        col = lot();
        this.pred = pred;
    }

    Lot process(RBNode node) {
        _aid(node);
        return col;
    }

    void _aid(@NotNull RBNode node) {
        if (!node.isEmpty()) {
            _aid(node.right);
            if (pred.apply(node.value)) {
                col = cons(node.value, col);
            }
            _aid(node.left);
        }
    }
}
}
