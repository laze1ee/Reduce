package reduce.utility;

import org.jetbrains.annotations.NotNull;
import reduce.progressive.Lot;
import reduce.refmethod.Has;

import static reduce.progressive.Pr.*;


@SuppressWarnings("SuspiciousNameCombination")
class RBTreeMate {

static @NotNull Lot pathOf(@NotNull RBNode node, Object key) {
    Lot path = new Lot();
    while (!node.isEmpty()) {
        if (less(key, node.key)) {
            path = cons(node, path);
            node = node.left;
        } else if (greater(key, node.key)) {
            path = cons(node, path);
            node = node.right;
        } else {
            return cons(node, path);
        }
    }
    return cons(node, path);
}

private static boolean isLeftOf(Object node1, @NotNull RBNode node2) {
    return eq(node1, node2.left);
}

private static boolean isRightOf(Object node1, @NotNull RBNode node2) {
    return eq(node1, node2.right);
}

private static void leftRotate(RBTree tree, Lot path) {
    RBNode x = (RBNode) car(path);
    RBNode y = x.right;
    RBNode b = y.left;

    y.left = x;
    x.right = b;

    if (cdr(path).isEmpty()) {
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

private static void rightRotate(RBTree tree, Lot path) {
    RBNode x = (RBNode) car(path);
    RBNode y = x.left;
    RBNode b = y.right;

    y.right = x;
    x.left = b;

    if (cdr(path).isEmpty()) {
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


static Lot minimum(@NotNull RBNode node, Lot path) {
    while (!node.isEmpty()) {
        path = cons(node, path);
        node = node.left;
    }
    return path;
}

static Lot maximum(@NotNull RBNode node, Lot path) {
    while (!node.isEmpty()) {
        path = cons(node, path);
        node = node.right;
    }
    return path;
}


record InsertFixing(RBTree tree, Lot path) {

    void process() {
        _job(path);
        tree.root.color = false;
    }

    private void _job(Lot path) {
        if (2 < length(path) && ((RBNode) get1(path)).isRed()) {
            RBNode p = (RBNode) get1(path);
            RBNode pp = (RBNode) get2(path);
            if (isLeftOf(p, pp)) {
                RBNode u = pp.right;
                if (u.isRed()) {
                    p.color = false;
                    u.color = false;
                    pp.color = true;
                    _job(cddr(path));
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
                    _job(cddr(path));
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


private static void transplant(RBTree tree, Lot path, RBNode node) {
    if (1 == length(path)) {
        tree.root = node;
    } else {
        RBNode p = (RBNode) get1(path);
        if (isLeftOf(car(path), p)) {
            p.left = node;
        } else {
            p.right = node;
        }
    }
}

static boolean delete(@NotNull RBTree tree, Object key) {
    Lot path = pathOf(tree.root, key);
    RBNode deleted = (RBNode) car(path);
    if (deleted.isEmpty()) {
        return false;
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
            Lot min_path = minimum(deleted.right, new Lot());
            RBNode alternate = (RBNode) car(min_path);
            color = alternate.color;
            x = alternate.right;
            if (!isRightOf(alternate, deleted)) {
                transplant(tree, min_path, x);
                alternate.right = deleted.right;
            }
            transplant(tree, path, alternate);
            alternate.left = deleted.left;
            alternate.color = deleted.color;
            path = append(cons(x, cdr(min_path)), cons(alternate, cdr(path)));
        }
        if (!color) {       // is the deleted color black?
            DeleteFixing fixer = new DeleteFixing(tree, path);
            fixer.process();
        }
        return true;
    }
}

private static class DeleteFixing {

    final RBTree tree;
    final Lot path;
    RBNode x;

    DeleteFixing(RBTree tree, Lot path) {
        this.tree = tree;
        this.path = path;
    }

    void process() {
        x = (RBNode) car(path);
        _job(cdr(path));
        x.color = false;
    }

    private void _job(@NotNull Lot path) {
        if (!path.isEmpty() && x.isBlack()) {
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
                    _job(cdr(path));
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
                    _job(cdr(path));
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


static class Traveling {

    Lot col;

    Traveling() {
        col = new Lot();
    }

    Lot process(RBNode node) {
        _job(node);
        return col;
    }

    private void _job(@NotNull RBNode node) {
        if (!node.isEmpty()) {
            _job(node.right);
            col = cons(node.key, col);
            _job(node.left);
        }
    }
}

static class Filter {

    Lot col;
    final Has pred;

    Filter(Has pred) {
        col = new Lot();
        this.pred = pred;
    }

    Lot process(RBNode node) {
        _aid(node);
        return col;
    }

    private void _aid(@NotNull RBNode node) {
        if (!node.isEmpty()) {
            _aid(node.right);
            if (pred.apply(node.value)) {
                col = cons(node.key, col);
            }
            _aid(node.left);
        }
    }
}
}
