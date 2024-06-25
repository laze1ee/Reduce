package reduce.utility;

import org.jetbrains.annotations.NotNull;
import reduce.progressive.Lot;
import reduce.refmethod.Has;

import static reduce.progressive.Pr.car;


/**
 * Red Black Tree Algorithm
 */
public class RBTree {

RBNode root;

public RBTree() {
    root = new RBNode();
}


@Override
public String toString() {
    return String.format("(Red-Black-Tree %s)", root);
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof RBTree tree) {
        return root.equals(tree.root);
    } else {
        return false;
    }
}


public boolean isEmpty() {
    return root.isEmpty();
}

/***
 * @return if inserting succeeded, return true else false.
 */
public static boolean insert(@NotNull RBTree tree, Object key, @NotNull Object value) {
    Lot path = RBTreeMate.pathOf(tree.root, key);
    RBNode node = (RBNode) car(path);
    if (node.isEmpty()) {
        node.key = key;
        node.value = value;
        node.color = true;
        node.left = new RBNode();
        node.right = new RBNode();
        RBTreeMate.InsertFixer fixer = new RBTreeMate.InsertFixer(tree, path);
        fixer.process();
        return true;
    } else {
        return false;
    }
}

public static boolean isPresent(@NotNull RBTree tree, Object key) {
    Lot path = RBTreeMate.pathOf(tree.root, key);
    RBNode node = (RBNode) car(path);
    return !node.isEmpty();
}

public static Object ref(@NotNull RBTree tree, Object key) {
    Lot path = RBTreeMate.pathOf(tree.root, key);
    RBNode node = (RBNode) car(path);
    if (node.isEmpty()) {
        throw new RuntimeException(String.format(Msg.NOT_PRESENT, key, tree));
    } else {
        return node.value;
    }
}

public static void set(@NotNull RBTree tree, Object key, Object new_value) {
    Lot path = RBTreeMate.pathOf(tree.root, key);
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
    Lot path = RBTreeMate.minimum(root, new Lot());
    if (path.isEmpty()) {
        throw new RuntimeException(Msg.EMPTY_TREE);
    } else {
        RBNode node = (RBNode) car(path);
        return node.key;
    }
}

public Object maximum() {
    Lot path = RBTreeMate.maximum(root, new Lot());
    if (path.isEmpty()) {
        throw new RuntimeException(Msg.EMPTY_TREE);
    } else {
        RBNode node = (RBNode) car(path);
        return node.key;
    }
}

public static Lot travel(@NotNull RBTree tree) {
    RBTreeMate.Travel f = new RBTreeMate.Travel();
    return f.process(tree.root);
}

public static Lot filter(Has pred, @NotNull RBTree tree) {
    RBTreeMate.Filter f = new RBTreeMate.Filter(pred);
    return f.process(tree.root);
}
}
