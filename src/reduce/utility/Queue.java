package reduce.utility;

import org.jetbrains.annotations.NotNull;
import reduce.progressive.Few;
import reduce.progressive.Lot;

import static reduce.progressive.Pr.*;


public class Queue {

// #(dequeue-lot enqueue-lot)
final Few pipe;

public Queue(Object @NotNull ... args) {
    Few moo = new Few(args);
    Lot xoo = fewToLot(moo);
    pipe = new Few(xoo, new Lot());
}

@Override
public String toString() {
    return String.format("(queue %s %s)", ref0(pipe), ref1(pipe));
}

@Override
public boolean equals(Object datum) {
    if (datum instanceof Queue queue) {
        return ref0(pipe).equals(ref0(queue.pipe)) &&
               ref1(pipe).equals(ref1(queue.pipe));
    } else {
        return false;
    }
}


public boolean isEmpty() {
    return ((Lot) ref0(pipe)).isEmpty() &&
           ((Lot) ref1(pipe)).isEmpty();
}

public static void en(@NotNull Queue queue, Object datum) {
    Lot en_lot = (Lot) ref1(queue.pipe);
    en_lot = cons(datum, en_lot);
    set1(queue.pipe, en_lot);
}

public static Object de(@NotNull Queue queue) {
    if (queue.isEmpty()) {
        throw new RuntimeException(Msg.EMPTY_QUEUE);
    } else if (((Lot) ref0(queue.pipe)).isEmpty()) {
        Lot en_lot = (Lot) ref1(queue.pipe);
        en_lot = reverse(en_lot);
        set0(queue.pipe, cdr(en_lot));
        set1(queue.pipe, new Lot());
        return car(en_lot);
    } else {
        Lot de_lot = (Lot) ref0(queue.pipe);
        set0(queue.pipe, cdr(de_lot));
        return car(de_lot);
    }
}
}
