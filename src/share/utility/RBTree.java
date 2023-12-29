package share.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.progressive.Few;
import share.progressive.Lot;

import static share.progressive.Pg.*;


public class RBTree {

@Contract(" -> new")
public static @NotNull Few initialize() {
    return few(RBTreeAid.emptyNode());
}

public static boolean isEmpty(Few tree) {
    if (length(tree) == 1 && ref0(tree) instanceof Few node) {
        return RBTreeAid.isEmptyNode(node);
    } else {
        return false;
    }
}

public static boolean isRBTree(Few tree) {
    if (length(tree) == 1 && ref0(tree) instanceof Few node) {
        return RBTreeAid.isNode(node);
    } else {
        return false;
    }
}

public static void insert(Few tree, Object key, Object value) {
    RBTreeAid.$Finder moo = new RBTreeAid.$Finder(key, (Few) ref0(tree));
    Lot path = moo.process();
    Few node = (Few) car(path);
    if (RBTreeAid.isEmptyNode(node)) {
        RBTreeAid.setKey(node, key);
        RBTreeAid.setValue(node, value);
        RBTreeAid.setColor(node, RBTreeAid.RED);
        RBTreeAid.setLeft(node, RBTreeAid.emptyNode());
        RBTreeAid.setRight(node, RBTreeAid.emptyNode());
        RBTreeAid.$InsertFixer fixer = new RBTreeAid.$InsertFixer(tree, path);
        fixer.process();
    } else {
        throw new RuntimeException
              (String.format("insert same key %s for tree %s", key, stringOf(tree)));
    }
}

public static boolean isPresent(Few tree, Object key) {
    RBTreeAid.$Finder moo = new RBTreeAid.$Finder(key, (Few) ref0(tree));
    Lot path = moo.process();
    return !RBTreeAid.isEmptyNode((Few) car(path));
}

public static Object ref(Few tree, Object key) {
    RBTreeAid.$Finder moo = new RBTreeAid.$Finder(key, (Few) ref0(tree));
    Lot path = moo.process();
    Few node = (Few) car(path);
    if (RBTreeAid.isEmptyNode(node)) {
        throw new RuntimeException
              (String.format("key %s is not present in tree %s", key, stringOf(tree)));
    } else {
        return RBTreeAid.getValue(node);
    }
}

public static void set(Few tree, Object key, Object value) {
    RBTreeAid.$Finder moo = new RBTreeAid.$Finder(key, (Few) ref0(tree));
    Lot path = moo.process();
    Few node = (Few) car(path);
    if (RBTreeAid.isEmptyNode(node)) {
        throw new RuntimeException
              (String.format("key %s is not present in tree %s", key, stringOf(tree)));
    } else {
        RBTreeAid.setValue(node, value);
    }
}

public static void delete(Object key, Few tree) {
    if (isEmpty(tree)) {
        throw new RuntimeException("empty tree");
    } else {
        RBTreeAid.delete(key, tree);
    }
}

public static Object minOf(Few tree) {
    Lot path = RBTreeAid.minimum((Few) ref0(tree), lot());
    if (isNull(path)) {
        throw new RuntimeException("empty tree");
    } else {
        return RBTreeAid.getValue((Few) car(path));
    }
}

public static Object maxOf(Few tree) {
    Lot path = RBTreeAid.maximum((Few) ref0(tree), lot());
    if (isNull(path)) {
        throw new RuntimeException("empty tree");
    } else {
        return RBTreeAid.getValue((Few) car(path));
    }
}

public static String stringOf(Few tree) {
    return String.format("<RBTree %s>", RBTreeAid.stringOfNode((Few) ref0(tree)));
}
}
