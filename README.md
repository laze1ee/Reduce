## List and Vector

In the Scheme programming language, various high-level data structures, including stacks, heaps, and
trees, can be easily constructed using list, vector, and other primitive data types. Within my
functional language project, implemented in Java, a specific subproject—Reduce—focuses on the
implementation of list and vector. To avoid naming conflicts, the term "lot" is used for list,
and "few" is used for vector. I have implemented a class `Pair` for list, utilizing the primitive
array type `Object[]` for vector. The code is capable of detecting and correctly printing circular
list and vector. Moreover, it facilitates comparison to ascertain the equivalence of the contents
of two circular structures.

All procedures related to lists and vectors are encapsulated within the
class `share.progressive.Pg`.

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

### Both List and Vector

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

## Wrapped primitive array type

To address the limitations of primitive arrays (e.g., `boolean[]`, `int[]`), which do not directly
extend `Object` and cannot be used as generic type arguments, I have introduced a new data type to
facilitate type-checking. The procedures for `boolean[]` and `int[]` are outlined below, with
similar procedures available for other types.

All procedures related to wrapped primitive array types are contained within the class
`share.primitive.Pm`.

#### Wrap `boolean[]` to `Febool`

|                                `febool(Boolean ...)` ⇒ `Febool` |
|----------------------------------------------------------------:|
|                        `makeFebool(Natural Boolean)` ⇒ `Febool` |
|                                  `feLength(Febool)` ⇒ `Natural` |
|                             `feRef(Febool Natural)` ⇒ `Boolean` |
|                      `feSet(Febool Natural Boolean)` ⇒ `<void>` |
|                             `feFill(Febool Boolean)` ⇒ `<void>` |
|  `feCopyInto(Febool Natural Febool Natural Natural)` ⇒ `<void>` |

#### Wrap `int[]` to `Feint`

|                                `feint(Integer ...)` ⇒ `Feint` |
|--------------------------------------------------------------:|
|                        `makeFeint(Natural Integer)` ⇒ `Feint` |
|                                 `feLength(Feint)` ⇒ `Natural` |
|                                    `feRef(Feint)` ⇒ `Integer` |
|                     `feSet(Feint Natural Integer)` ⇒ `<void>` |
|                            `feFill(Feint Integer)` ⇒ `<void>` |
|  `feCopyInto(FeInt Natural FeInt Natural Natural)` ⇒ `<void>` |
