One often used utility by rewriters is `Substitute`. Its function is to deal with the substitution of one expression for another. For example, we can substitute `X` in `X + 1` by `3`, obtaining the expression `3 + 1`.

`Substitute` can deal with scoping. For example, in substituting `X` by `2` in:

> `{X} union {(on X) f(X)}`,

it produces:

> `{2} union {(on X) f(X)}`

recognizing that the second `X` is not the same as the free one.

It can also deal with substituting function applications in a truth-preserving way. For example, substituting 'f(X)' by `2` in:

> `f(X) + f(Y)`

produces:

> `2 + if Y = X then 2 else f(Y)`

that is, it unifies the expression being substituted with candidate expressions and performs a substitution conditional on that unification.