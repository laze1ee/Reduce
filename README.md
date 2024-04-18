### Intro

In the Scheme programming language, various high-level data structures, including stacks, heaps, and
trees, can be easily constructed via using list or vector contains primitive data types. List and 
vector on the basis of ensuring the concision of the data type, also have some high-level features.

Within my functional language project, implemented in Java, a subproject Reduce that focuses on the
implementation of list and vector. To avoid naming conflicts, the term "lot" is used for list,
and "few" is used for vector. I made some classes `Pair` for list, utilizing the primitive
array type `Object[]` for vector. 

The code is capable of detecting and correctly printing circular list and vector. Moreover, it
facilitates comparison to ascertain the equivalence of the contents of two circular structures.

All procedures related to lists and vectors are encapsulated within the
class `reduce.progressive.Pr`.

> Symbolic representations will be utilized for illustrative purposes.
> * `Type-name` denotes a specific type with capitalized initials; for example, Natural represents a
    natural number.
> * `Any` represents any data type.
> * `...` following a type name indicates that there can be any number of arguments of this type.
> * `⇒` returns.
> * `<void>` signifies no return value.

### List

|                                    Scheme |                             Java |
|------------------------------------------:|---------------------------------:|
|                 `(list Any ...)` ⇒ `List` |           `lot(Any ...)` ⇒ `Lot` |
|                `(null? List)` ⇒ `Boolean` |        `isNull(Lot)` ⇒ `Boolean` |
|               `(length List)` ⇒ `Natural` |        `length(Lot)` ⇒ `Natural` |
|                      `(car Pair)` ⇒ `Any` |               `car(Lot)` ⇒ `Any` |
|                      `(cdr Pair)` ⇒ `Any` |              `cdr(Lot)` ⇒ `List` |
|         `(list-ref List Natural)` ⇒ `Any` |   `lotRef(Lot, Natural)` ⇒ `Any` |
|          `(set-car! List Any)` ⇒ `<void>` |    `setCar(Lot, Any)` ⇒ `<void>` |
|          `(set-cdr! Pair Any)` ⇒ `<void>` |    `setCdr(Lot, Lot)` ⇒ `<void>` |
|                 `(cons Any Any)` ⇒ `Pair` |         `cons(Any, Lot)` ⇒ `Lot` |
|             `(append List Pair)` ⇒ `Pair` |       `append(Lot, Lot)` ⇒ `Lot` |
|                 `(reverse List)` ⇒ `List` |           `reverse(Lot)` ⇒ `Lot` |
|       `(list-head List Natural)` ⇒ `List` |  `lotHead(Lot, Natural)` ⇒ `Lot` |
|       `(list-tail List Natural)` ⇒ `List` |  `lotTail(Lot, Natural)` ⇒ `Lot` |
|               `(list-copy List)` ⇒ `List` |              `copy(Lot)` ⇒ `Lot` |
|  `(map Procedure List List ...)` ⇒ `List` |         `lotMap(Do Lot)` ⇒ `Lot` |
|        `(filter Procedure List)` ⇒ `List` |        `filter(Has Lot)` ⇒ `Lot` |

### Vector

|                                            Scheme |                                    Java |
|--------------------------------------------------:|----------------------------------------:|
|                     `(vector Any ...)` ⇒ `Vector` |                  `few(Any ...)` ⇒ `Few` |
|            `(make-vector Natural Any)` ⇒ `Vector` |         `makefew(Natural, Any)` ⇒ `Few` |
|              `(vector-length Vector)` ⇒ `Natural` |               `length(Few)` ⇒ `Natural` |
|             `(vector-ref Vector Natural)` ⇒ `Any` |          `fewRef(Few, Natural)` ⇒ `Any` |
|     `(vector-set! Vector Natural Any)` ⇒ `<void>` |  `fewSet(Few, Natural, Any)` ⇒ `<void>` |
|                 `(vector-copy Vector)` ⇒ `Vector` |                     `copy(Few)` ⇒ `Few` |
|  `(vector-map Proc Vector Vector ...)` ⇒ `Vector` |                `fewMap(Do Few)` ⇒ `Few` |

### Others

|                            Scheme |                     Java |
|----------------------------------:|-------------------------:|
|  `(list->vector List)` ⇒ `Vector` |  `lotTofew(Lot)` ⇒ `Few` |
|  `(vector->list Vector)` ⇒ `List` |  `fewToLot(Few)` ⇒ `Lot` |

Additionally, I've listed some procedures that deviate from the Scheme convention. The Java versions
are as follows:

* `caar(Lot)` ⇒ `Any`
* `cadr(Lot)` ⇒ `Any`
* `cdar(Lot)` ⇒ `Lot`
* `cddr(Lot)` ⇒ `Lot`
