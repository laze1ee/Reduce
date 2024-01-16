package share.refmethod;

@FunctionalInterface
public interface Eqv {

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
boolean apply(Object o1, Object o2);
}
