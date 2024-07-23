package reduce.utility;

import org.jetbrains.annotations.NotNull;
import reduce.progressive.Few;
import reduce.progressive.Lot;
import reduce.refmethod.Has2;
import reduce.refmethod.Has1;

import static reduce.progressive.Pr.*;


/**
 * Red Black Tree Algorithm
 */
public class RBTree {

final Few data;

public RBTree(Has2 less, Has2 greater) {
    data = new Few(less, greater, new RBNode());
}


@Override
public String toString() {
    return String.format("(Red-Black-Tree %s)", ref2(data));
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof RBTree tree) {
        return this.root().equals(tree.root());
    } else {
        return false;
    }
}

Has2 less() {
    return (Has2) ref0(data);
}

Has2 greater() {
    return (Has2) ref1(data);
}

RBNode root() {
    return (RBNode) ref2(data);
}

void setRoot(RBNode node) {
    set2(data, node);
}

public boolean isEmpty() {
    return root().isEmpty();
}

/***
 * @return if inserting succeeded, return true else false.
 */
public static boolean insert(@NotNull RBTree tree, Object key, @NotNull Object value) {
    Lot path = RBTreeMate.pathOf(tree, key);
    RBNode node = (RBNode) car(path);
    if (node.isEmpty()) {
        node.key = key;
        node.value = value;
        node.color = true;
        node.left = new RBNode();
        node.right = new RBNode();
        RBTreeMate.InsertFixing fixer = new RBTreeMate.InsertFixing(tree, path);
        fixer.process();
        return true;
    } else {
        return false;
    }
}

public static boolean isPresent(@NotNull RBTree tree, Object key) {
    Lot path = RBTreeMate.pathOf(tree, key);
    RBNode node = (RBNode) car(path);
    return !node.isEmpty();
}

public static Object ref(@NotNull RBTree tree, Object key) {
    Lot path = RBTreeMate.pathOf(tree, key);
    RBNode node = (RBNode) car(path);
    if (node.isEmpty()) {
        throw new RuntimeException(String.format(Msg.NOT_PRESENT, key, tree));
    } else {
        return node.value;
    }
}

public static void set(@NotNull RBTree tree, Object key, Object new_value) {
    Lot path = RBTreeMate.pathOf(tree, key);
    RBNode node = (RBNode) car(path);
    if (node.isEmpty()) {
        throw new RuntimeException(String.format(Msg.NOT_PRESENT, key, tree));
    } else {
        node.value = new_value;
    }
}

/***
 * @return if deleting succeeded, return true else false.
 */
public static boolean delete(@NotNull RBTree tree, Object key) {
    if (tree.isEmpty()) {
        return false;
    } else {
        return RBTreeMate.delete(tree, key);
    }
}

public Object minimum() {
    Lot path = RBTreeMate.minimum(this.root(), new Lot());
    if (path.isEmpty()) {
        throw new RuntimeException(Msg.EMPTY_TREE);
    } else {
        RBNode node = (RBNode) car(path);
        return node.key;
    }
}

public Object maximum() {
    Lot path = RBTreeMate.maximum(this.root(), new Lot());
    if (path.isEmpty()) {
        throw new RuntimeException(Msg.EMPTY_TREE);
    } else {
        RBNode node = (RBNode) car(path);
        return node.key;
    }
}

public static Lot travel(@NotNull RBTree tree) {
    RBTreeMate.Traveling f = new RBTreeMate.Traveling();
    return f.process(tree.root());
}

public static Lot filter(Has1 pred, @NotNull RBTree tree) {
    RBTreeMate.Filter f = new RBTreeMate.Filter(pred);
    return f.process(tree.root());
}
}
