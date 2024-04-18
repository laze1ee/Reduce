package reduce.utility;

import org.jetbrains.annotations.NotNull;
import reduce.progressive.Lot;
import reduce.refmethod.Has;

import static reduce.progressive.Pr.*;


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


public static void insert(@NotNull RBTree tree, Object key, Object value) {
    Lot path = RBTreeAid.pathOf(tree.root, key);
    RBNode node = (RBNode) car(path);
    if (node.isEmpty()) {
        node.key = key;
        node.value = value;
        node.color = true;
        node.left = new RBNode();
        node.right = new RBNode();
        RBTreeAid.InsertFixer fixer = new RBTreeAid.InsertFixer(tree, path);
        fixer.process();
    } else {
        throw new RuntimeException(String.format(Msg.SAME_KEY, key, tree));
    }
}

public static boolean isPresent(@NotNull RBTree tree, Object key) {
    Lot path = RBTreeAid.pathOf(tree.root, key);
    RBNode node = (RBNode) car(path);
    return !node.isEmpty();
}

public static Object ref(@NotNull RBTree tree, Object key) {
    Lot path = RBTreeAid.pathOf(tree.root, key);
    RBNode node = (RBNode) car(path);
    if (node.isEmpty()) {
        throw new RuntimeException(String.format(Msg.NOT_PRESENT, key, tree));
    } else {
        return node.value;
    }
}

public static void set(@NotNull RBTree tree, Object key, Object new_value) {
    Lot path = RBTreeAid.pathOf(tree.root, key);
    RBNode node = (RBNode) car(path);
    if (node.isEmpty()) {
        throw new RuntimeException(String.format(Msg.NOT_PRESENT, key, tree));
    } else {
        node.value = new_value;
    }
}

public static void delete(@NotNull RBTree tree, Object key) {
    if (tree.isEmpty()) {
        throw new RuntimeException(Msg.EMPTY_TREE);
    } else {
        RBTreeAid.delete(tree, key);
    }
}

public Object minimum() {
    Lot path = RBTreeAid.minimum(root, lot());
    if (isNull(path)) {
        throw new RuntimeException(Msg.EMPTY_TREE);
    } else {
        RBNode node = (RBNode) car(path);
        return node.key;
    }
}

public Object maximum() {
    Lot path = RBTreeAid.maximum(root, lot());
    if (isNull(path)) {
        throw new RuntimeException(Msg.EMPTY_TREE);
    } else {
        RBNode node = (RBNode) car(path);
        return node.key;
    }
}

public static Lot travel(@NotNull RBTree tree) {
    RBTreeAid.Travel f = new RBTreeAid.Travel();
    return f.process(tree.root);
}

public static Lot filter(Has pred, @NotNull RBTree tree) {
    RBTreeAid.Filter f = new RBTreeAid.Filter(pred);
    return f.process(tree.root);
}
}
