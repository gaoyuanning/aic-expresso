# Overview #

Java programmers can use the _expresso_ and _brewer_ packages to represent and construct expressions, and then use them any way they like, as any other type of Java objects. One possible way of using them is in a [Rewriting System](http://en.wikipedia.org/wiki/Rewriting), in which expressions (and parts of expressions) are iteratively rewritten until no further rewriting is possible (not unlike the way people sometimes work out expressions in informal mathematics). The _grinder_ pacakge provides support for this kind of use.

The way grinder operates is simple. A rewriter can be seen as a function mapping each expression into another, possibly itself (this latter case is interpreted as the rewriter "not applying" to that expression). Grinder takes one rewriter and one expression, and applies the rewriter to the expression, returning the result.

Naturally, it is more common to successively apply several rewriters to an expression. This can be done by using an "aggregation" rewriter called `RewriteOnce`. This rewriter is based on a sorted list of other rewriters. From the first one on, it tries to apply each rewriter to the expression and sub-expressions of the expression, top-down and depth-first. If a rewriter succeeds, `RewriteOnce` substitutes the result for the original rewritten sub-expression, and returns the overall, new expression with the rewritten sub-expression.

`RewriteOnce`, as the name indicates, does a single rewriting. If we wish to keep applying a list of rewriters to an expression until no more rewritings are possible, we can use a rewriter called `ExhaustiveRewriter`. An exhaustive rewriter takes some other rewriter (typically a `RewriteOnce` rewriter containing a list of rewriters) and keeps applying it until no more rewritings are done. Similarly, `TotalRewriter` exhaustively rewrites an expression until no more rewriting is possible but instead takes a list of rewriters to apply (this is essentially an optimized version of the `ExhaustiveRewriter` and `RewriteOnce` rewrite mechanism, as it reduces the overhead needed in between each individual rewrite call).

The most common way of using grinder is, thus, having a list of rewriters able to perform a task in some domain of interest, and using them in conjunction with `RewriteOnce` and `ExhaustiveRewriter` or `TotalRewriter`. Typically, the "most useful" rewriters will be at the top of this list, since they will be attempted first. One such rewriter is, for example, one that replaces `0 * Something`, where `Something` is some potentially complex expression, by `0`, since that eliminates a potentially complex sub-expression at a very low cost. Note that applying rewriters top-down makes sense here too. If we did it bottom-up, then we would try to apply rewriters inside `Something` before "noticing" that it can simply be eliminated. Another way of looking at this is that the top-down ordering lends a "lazy evaluation" aspect to the rewriting.

There is a large, and ever growing, library of rewriters contained in grinder, for dealing with basic mathematics constructs such as numbers, booleans and sets. The rewriters are the part of grinder which actually implements the semantics of expressions. They work in groups, and sometimes communicate with one another during processing. Often, a rewriter uses, as a pre-condition, the fact that some other rewriter must have attempted a rewriting first (that is, is placed higher up in the list of rewriters). For example, rewriters computing the cardinality of sets assume that other rewriters normalizing set condition formulas are applied earlier and that, therefore, those conditions must already be normalized when expressions get to them. Right now, the user must make sure the required rewriters are present and placed before the ones requiring them; a planned future feature is having a rewriter "requires" declaration that automatically does that. Other relationships between rewriters will become clearer as we explain the main groups of rewriters and supporting utilities:

  * [Arithmetic](GrinderArithmetic.md)
  * [Sets](GrinderSets.md)
  * [Logic](GrinderLogic.md)
  * [Model Counting](ModelCountingOverview.md)
  * [Substituting Expressions](GrinderSubstitute.md)

### Rewriting Process ###
Rewriters usually operate in groups, forming a process we call _rewriting process_. A `RewritingProcess` is an interface representing such entities. It provides methods for determining the top expression being processed, a means to find one of its rewriters (sometimes rewriters need to find each other and communicate; see Modules), as well as a means to notify all rewriters that the process is about to start.

A rewriter is not associated with any specific process, however, since it can be part of several. Therefore, it does not have a method or field indicating "its process". Instead, the rewriter receives the process in question as an argument of its rewriting method. It can then access the process's properties as needed for that particular rewriting operation.

### Modules ###
Rewriters often rely on properties of expressions in order to modify them. The rewriter utility `Substitute`, for example, needs to know that `{(on X) f(X)}` scopes `X` in its sub-expressions. It would be simple to hard-code this fact in the system, but if a user wants to introduce some other type of expression that also scopes variables (for example, an "at least" quantifier such as `at least 20 values of X in SomeSet: odd(X)`), then `Substitute` would not know about that fact.

Instead, we use the notion of _modules_. A module object exposes an interface for a particular type of property. The module `ScopedVariables`, for instance, has methods indicating what are the scoping variables in any given expression such as the example above. When a user introduces a new function scoping variables, he or she writes a class implementing the `ScopedVariables.Provider` interface and _registers_ it with the module when a rewriting process is initiated. This way, the user does not have to modify `ScopedVariables`, but simply writes a provider, which is like a plug-in containing specific semantic knowledge about a type of expression. In fact, providers are used for all scoping functions in the system, for e.g. intensional sets, which could have been written by any user.

There are other modules for other types of properties as well. For example, it is sometimes useful to know that a function is injective, because if `f` is injective then we know that `f(X)=f(a)` is equivalent to `X=a`. The module `InjectiveModule` takes care of that, and can have its own providers registered for particular types of functions introduced by a user.

Other current modules are `MutuallyExclusiveCoDomainsModule` and `ExpressionKnowledgeModule`.

**Note:** currently, modules are implemented as rewriters (that usually don't perform any rewriting and therefore also implement the `NoOpRewriter` interface), but this will eventually not be true. The reason this was done so was because there was a need for rewriters to find a module upon the beginning of a rewriting process, so they can register specific providers with it. Since there was already a `RewritingProcess` method for finding rewriters, modules were created as rewriters so they can be found in the same way. However, this requires modules to have a rewriting method, which should not be necessary. At some point, we will abstract the notion of "something that can be found in a rewriting process" that will be extended by both rewriters and modules.


### A further note on differences between Symbol Trees and Expressions ###
Symbol Trees and Expressions behave in very similar manners for the most common types of expressions: symbol and function applications. More specific forms show more differences. For example, in [probabilistic reasoning](http://code.google.com/p/aic-praise/), we use the notation `[if sick(X) then 0.1 else 0.9]` to denote a factor. This is a symbol tree with `[ . ]` in its root, with a child equal to the syntax tree of `if sick(X) then 0.1 else 0.9`. However, only `X` is defined as a subexpression of this expression; the rest of it acts as an atomic syntactic indicator of the factor being represented. This means that the if-then-else sub-expression is not rewritten or evaluated.

The way Grinder decides which parts are actually sub-expressions of an expression is by using the module `ExpressionKnowledgeModule`. The providers registered with this module know which expressions are special in this regard, and indicate their sub-expressions. This allows users to create their own special expressions with specific sub-expressions. Expression that do not have such a provider for them are assumed to be function applications, with all subtrees being subexpressions as well. This is the mechanism by which expressions of types different from function applications and symbols (that is, sets and quantified expressions) are implemented.

### More Complex Rewriters ###
Initially we expected Grinder and the exhaustive rewriting of expressions to be the sole execution mechanism needed. The idea was to provide a list of needed rewriters and let them do their magic until a fully simplified equivalent expression is returned in a desired form.

This idea has proven naive because it is hard to control where the computation is going when more than one rewriter is available. This has led to the introduction of _hierarchical rewriters_, rewriters that take one expression and (by often invoking other rewriters) return an equivalent expression completely rewritten to satisfy a certain desired form. In other words, these rewriters do the "whole job" as opposed to the _atomic_ rewriters used by exhaustive rewriting, which typically take a small step and step back, letting other rewriters have a chance.

An example of a hierarchical rewriter is the one called Cardinality, which takes a logical formaula and rewrites it to its answer. That is of course a complex process, that the rewriter does all by itself by invoking successively simpler rewriters.

The advantage of hierarchical rewriters is that their execution is guided by a very thought-out algorithm that does the job in an efficient manner. The disadvantage is that they are not as generic as atomic rewriters and need to be thought out. That said, we still use exhaustive rewriting by atomic rewriters at certain points, particularly the simplification of expressions.



