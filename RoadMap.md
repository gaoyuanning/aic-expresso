# Road Map for AIC Expresso #

This page details the development road map for AIC Expresso. It works as a to-do list and gives users an idea of what features are to come, and where AIC Expresso is going.

## Incremental rewriting ##

If we have just resolved a large expression E and now need to solve a new large expression E' equal to E but for a few parts, we would want Expresso to be smart about it and only recompute parts that really need to be recomputed. This already happens with rewriting caching, which remembers what expressions have been rewritten to in the past. However, say E is an application of `+` to one hundred arguments: `1 + 2 + ... + 100`. If E' is `1 + 2 + ... + 99 + 400`, caching will not help because E' is different from E (it _would_ work if the summation was represented as the nesting of lots of _binary_ `+`, because then most of the sub-expressions would have their result cached).

Incremental rewriting is a capability in which Expresso can be provided with incremental versions of rewriters (provided by the user or, in the case of basic operators like `+`, being in a library) so that it would be efficient even in cases like the above.

## Scalability ##

Since Expresso is in early development, there is quite a bit of optimization that can be done on it. We also plan to achieve greater scalability by enabling it to work concurrently and on a cluster.The code has been written since a very early stage with this in mind and most of what is needed is already in there, but we need to test and tune this.

## Unification-based caching ##

Caching is an essential part of symbolic evaluation because typically the same expression is found several times during the solving of a problem.

However, simple lookup of an expression does not yield the best possible caching. For example, if an expression `f(X,3)` has been reduced to, say, `X + 1`, we could in principle deduce that `f(2,3)` can be reduced to `2 + 1`, while not being able to say much about `f(X,4)`. What is needed for this is a _unification_-based caching mechanism that stores expressions with free variables and identifies cases unifying with them.

## Cleaning of code regarding distinction between expressions and syntax trees ##

There was an error in the initial design of Expresso and, as a result, the distinction between expressions and their syntax trees is currently confusing. Cleaning that up will make the library easier to use.