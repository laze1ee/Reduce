package share.utility;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import share.progressive.Few;
import share.progressive.Lot;
import share.refmethod.Eqv;

import java.util.Random;

import static share.progressive.Cmp.eq;
import static share.progressive.Pg.*;


public class Ut {

private static final char[] CHARS_SET =
"_-ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

public static @NotNull String randomString(int length) {
    Random r = new Random();
    StringBuilder s = new StringBuilder();
    for (int i = 0; i < length; i = i + 1) {
        int index = r.nextInt(CHARS_SET.length);
        char c = CHARS_SET[index];
        s.append(c);
    }
    return s.toString();
}


//region Lot
public static boolean isBelong(Object o, Lot lt) {
    if (isNull(lt)) {
        return false;
    } else if (eq(o, car(lt))) {
        return true;
    } else {
        return isBelong(o, cdr(lt));
    }
}

public static boolean isBelong(Eqv pred, Object o, Lot lt) {
    if (isNull(lt)) {
        return false;
    } else if (pred.apply(o, car(lt))) {
        return true;
    } else {
        return isBelong(pred, o, cdr(lt));
    }
}

public static Lot noDup(Lot lt) {
    if (isNull(lt)) {
        return lot();
    } else if (isBelong(car(lt), cdr(lt))) {
        return noDup(cdr(lt));
    } else {
        return cons(car(lt), noDup(cdr(lt)));
    }
}

public static Lot remove(Object o, Lot lt) {
    if (isNull(lt)) {
        return lot();
    } else if (eq(o, car(lt))) {
        return cdr(lt);
    } else {
        return cons(car(lt), remove(o, cdr(lt)));
    }
}
//endregion


//region Few
public static int atFew(Eqv pred, Object o, Few fw) {
    int sz = length(fw);
    for (int i = 0; i < sz; i = i + 1) {
        if (pred.apply(o, fewRef(fw, i))) {
            return i;
        }
    }
    return -1;
}
//endregion


//region Red-Black-Tree
@Contract(" -> new")
public static @NotNull Few emptyRBTree() {
    return few(RBTree.emptyNode());
}

public static boolean isEmptyRBTree(Few rbTree) {
    if (length(rbTree) == 1 && ref0(rbTree) instanceof Few node) {
        return RBTree.isEmptyNode(node);
    } else {
        return false;
    }
}

public static boolean isRBTree(Few rbTree) {
    if (length(rbTree) == 1 && ref0(rbTree) instanceof Few node) {
        return RBTree.isNode(node);
    } else {
        return false;
    }
}

public static void insertRBTree(Few rbTree, Object key, Object value) {
    RBTree.$Finder moo = new RBTree.$Finder(key, (Few) ref0(rbTree));
    Lot path = moo.process();
    Few node = (Few) car(path);
    if (RBTree.isEmptyNode(node)) {
        RBTree.setKey(node, key);
        RBTree.setValue(node, value);
        RBTree.setColor(node, RBTree.RED);
        RBTree.setLeft(node, RBTree.emptyNode());
        RBTree.setRight(node, RBTree.emptyNode());
        RBTree.$InsertFixer fixer = new RBTree.$InsertFixer(rbTree, path);
        fixer.process();
    } else {
        throw new RuntimeException
              (String.format("insert same key %s for tree %s", key, stringOfRBTree(rbTree)));
    }
}

public static boolean isKeyExistInRBTree(Few rbTree, Object key) {
    RBTree.$Finder moo = new RBTree.$Finder(key, (Few) ref0(rbTree));
    Lot path = moo.process();
    return !RBTree.isEmptyNode((Few) car(path));
}

public static Object refRBTree(Few rbTree, Object key) {
    RBTree.$Finder moo = new RBTree.$Finder(key, (Few) ref0(rbTree));
    Lot path = moo.process();
    Few node = (Few) car(path);
    if (RBTree.isEmptyNode(node)) {
        throw new RuntimeException
              (String.format("key %s is not existed in tree %s", key, stringOfRBTree(rbTree)));
    } else {
        return RBTree.getValue(node);
    }
}

public static void setRBTree(Few rbTree, Object key, Object value) {
    RBTree.$Finder moo = new RBTree.$Finder(key, (Few) ref0(rbTree));
    Lot path = moo.process();
    Few node = (Few) car(path);
    if (RBTree.isEmptyNode(node)) {
        throw new RuntimeException
              (String.format("key %s is not existed in tree %s", key, stringOfRBTree(rbTree)));
    } else {
        RBTree.setValue(node, value);
    }
}

public static void deleteRBTree(Object key, Few tree) {
    if (isEmptyRBTree(tree)) {
        throw new RuntimeException("empty tree");
    } else {
        RBTree.delete(key, tree);
    }
}

public static Object minOfRBTree(Few tree) {
    Lot path = RBTree.minimum((Few) ref0(tree), lot());
    if (isNull(path)) {
        throw new RuntimeException("empty tree");
    } else {
        return RBTree.getValue((Few) car(path));
    }
}

public static Object maxOfRBTree(Few tree) {
    Lot path = RBTree.maximum((Few) ref0(tree), lot());
    if (isNull(path)) {
        throw new RuntimeException("empty tree");
    } else {
        return RBTree.getValue((Few) car(path));
    }
}

public static String stringOfRBTree(Few tree) {
    return String.format("<RBTree %s>", RBTree.stringOfNode((Few) ref0(tree)));
}
//endregion
}
