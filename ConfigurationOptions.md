Each package in aic-expresso has its own default [configuration](http://code.google.com/p/aic-util/wiki/Configuration) settings, which can be overridden at run time.

# Grinder #
The [Grinder Configuration](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/GrinderConfiguration.java) options are as follows:

Note: The following options must be set before any [Trace](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/helper/Trace.java)  or [Justification](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/helper/Justification.java) calls are made in the code as they apply to the underlying [logging](http://logback.qos.ch/) system.

|Name|Default|Description|
|:---|:------|:----------|
| trace.level                                               | trace  | Enabled by default, set to 'off' to disable globally.  |
| grinder.trace.in.and.out.of.atomic.rewriter.enabled       | false  | Disabled by default, set to 'true' to let atomic rewriters generate call information (i.e. input and output).  |
| grinder.trace.in.and.out.of.hierarchical.rewriter.enabled | true   | Enabled by default, set to 'false' to stop hierarchical rewriters from generating call information (i.e. input and output).  |
| justification.level                                       | trace  | Enabled by default, set to 'off' to disable globally. |
| grinder.display.tree.util.ui                              | true   | Relevant appenders are configured in the logback-test.xml to output trace and justification information to a GUI during testing.  |
| grinder.wait.until.ui.closed.enabled                      | false  | If the Tree Util UI is enabled, keep it open even when testing is complete. Useful to turn on if investigating a particular test case and you want to inspect the trace our justification output generated once the test has completed. |

|Name|Default|Description|
|:---|:------|:----------|
| grinder.cardinalilty.assume.domains.always.large | false | Used to determine if the [cardinality](http://code.google.com/p/aic-expresso/wiki/RewriterCard) computation logic makes the assumption that the domain size of all logical variables is always greater than the number of constants present in the model.  |
| grinder.complete.simplify.use.sat.solver | true  | Determine if [R\_complete\_normalize](http://code.google.com/p/aic-expresso/wiki/RewriterCompleteSimplify) should rely on an underlying SAT Solver for its tautology and contradiction testing. |
| grinder.default.sat.solver.class | `SAT4JSolver.class.getName()` | The default SAT Solver class to instantiate. |
| grinder.replace.variable.by.constant.it.is.bound.to | true  | Used by the [Incomplete Linear Implied Certainty](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/IncompleteLinearImpliedCertainty.java) atomic rewriter (used in [R\_normalize](http://code.google.com/p/aic-expresso/wiki/RewriterNormalize)) to determine if it should replace logical variables with the value it is bound to in a particular execution context (e.g. `if X = ann then hello(X) else goodbye()`, would change `hello(X)` to `hello(ann)`).|
| grinder.rewrite.dead.ends.cache.maximum.size | 100   | The maximum size allowed for a rewrite dead ends cache. Used by [Total Rewriter](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/core/TotalRewriter.java), which is basis for [R\_normalize](http://code.google.com/p/aic-expresso/wiki/RewriterNormalize)). |
| grinder.rewrite.dead.ends.cache.garbage.collection.period | No Collection | The period, in number of entries, to run garbage collection on the dead ends cache (defaults to no collection, i.e. -1). |
| grinder.rewriting.process.cache.maximum.size | 100   | The maximum size allowed for a Rewriting Processes rewrite cache. This is used to reuse the output of previous rewrites. |
| grinder.rewriting.process.cache.garbage.collection.period | No Collection | The period, in number of entries, to run garbage collection on the rewriting process's cache (defaults to no collection, i.e. -1). |
| grinder.rewriting.filter.out.logging.by.rewriters.named | Empty String  | Filter out the trace and justification output for the rewriters listed in a colon separated String of named rewriters. Note: Often set in the appropriate aic-smf configuration file (e.g. aic-smf-configu-test.xml or aic-smf-configu.xml).  |
| grinder.demo.ui.default.look.and.feel | Nimbus | The Java Swing default look and feel to set Demo GUI applications to. |

# Expresso #
The [Expresso Configuration](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/expresso/ExpressoConfiguration.java) options are as follows:

|Name|Default|Description|
|:---|:------|:----------|
|expresso.display.numeric.precision.for.symbols|9      | The dot relative precision to which numeric Symbols should be displayed (e.g. toString() representation). The actual precision is calculated as max(length of integer portion, configured precision). This ensures no loss of precision in the presentation of the integer part of numeric Symbols. |
|expresso.display.scientific.greater.n.integer.places|40     | A threshold for whether or not scientific notation should be used in the display of numeric symbols with a fractional part. |
|expresso.display.scientific.after.n.decimal.places|40     | A threshold for whether or not scientific notation should be used in the display of numeric symbols with a fractional part. |
|expresso.use.global.symbol.table|true   | Whether or not a global symbol table should be used. |
|expresso.global.symbol.table.maximum.size|No Maximum Size| The maximum size allowed for the global symbol table (defaults to no maximum, i.e. -1). Setting to 0 is the same as having no global symbol table. |
|expresso.global.symbol.table.cache.numerics|false  |Whether or not numeric symbols should be cached in the global symbol table.|
|expresso.syntax.to.string.thread.cache.timeout|60     | The amount of time, in seconds, that a cache for maintaining instances of syntax to string functions on a per-thread basis is to maintain its entries. |