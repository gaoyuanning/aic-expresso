This library manipulates _extensional_ and _intensional_ set definitions.

### Extensional ###
Extensional definitions simply explicitly enumerate the elements of a set, for example:

> `{1,2,3}`

### Intensional ###
Intensional definitions list a tuple of _indices_ and their domains, a condition on those indices, and a function application on idices (the set's _head_). Indices satisfying the condition are used by the function application to compute a member of thee set. For example:

> `{ (on X in {1,2,3,4}) X + 1 | X != 3}`

is an intensional definition of the extensional set `{2, 3, 5}`. The index domain can be any expression evaluating to a set, including other intensional set definitions, which is of course why intensional definitions are more powerful than extensional ones. Here is an example of an intensional definition using multiple indices:

> `{ (on X in {1,2,3,4}, Y in {a,b,c}) f(X,Y) | X != 3 and Y != b}`

The examples above illustrate the actual syntax defined by `CommonGrammar` for set definitions. This syntax is identical to the one used in informal mathematics taught in primary school, but for the exception that indices are explicitly indicated, so that free variables can be used, thereby allowing something like:

> `{(on X in {1,2,3}) X + Y | X != 2}`

to be properly distinguished from:

> `{(on X in {1,2,3}, Y in {5,6}) X + Y | X != 2}`

The syntax makes the set condition optional (when it is just assumed `true`). If an index, say, `X`, does not include a domain, it is assumed to be `type(X)`. Therefore, `{(on X) f(X)}` is equivalent to `{(on X in type(X)) f(X) | true}`.

Indices are not restricted to (variable) symbols; we can also use function applications (see [Quantification of Function Applications](QuantificationOfFunctionApplications.md) for more details). For example, it is valid to write:

> `{(on f(2) in {1,2,3}) f(2) + f(1)}`

which iterates over possible values for `f(2)`, without affecting `f(1)`. We can even write:

> `{(on f(X) in {1,2,3}) f(X) + f(1)}`

where `X` is a free variable. In this example, if `X` has value `2`, then we have the same set as:

> `{(on f(2) in {1,2,3}) f(2) + f(1)}`

If `X` is `1`, however, the set is:

> `{(on f(1) in {1,2,3}) f(1) + f(1)}`

in which case, of course, both occurrences of `f(1)` are affected and, as one might imagine, this is the same as simply:

> `{(on f(1) in {1,2,3}) 2*f(1)}`

Finally, we explain a more sophisticated construct that allows even the _index_ of an intensional set definition to be _evaluated_ on the fly. The examples above use indices known in advance: `X` and `Y`. There are situations in which the index to be used is determined "at runtime", from the evaluation of an expression. We indicate this by prefixing the index by `value of`, as in:

> `{(on value of Index in {1,2,3}) X + Y}`

where `Index` is an expression-valued variable, that is, its interpretation maps to value that is an expression itself. Suppose `Index` has value `<X>`, that is, it represents the symbol `X` itself (angle brackets indicate a quoted expression). Then the set definition is the same as:

> `{(on value of <X> in {1,2,3}) X + Y}`

the operator `value of`, when applied to an expression value, "plugs" that expression it its own place, so we get:

> `{(on X in {1,2,3}) X + Y}`

As a further example, consider:

> `{(on value of {<X>,<Y>} - {<Y>} in {1,2,3}) X + Y}`

which, after the set difference is evaluated, is found to be equivalent to:

> `{(on X in {1,2,3}) X + Y}`

as well.

### Multisets ###
Multiset definitions are denoted by the use of double curly brackets (`{{ }}`) instead of single ones (`{ }`), for both intensional and extensional definitions.