R`_`complete`_`simplify(E) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/CompleteSimplify.java))
<pre>
inputs: E is an expression.<br>
return: a simplification of E if possible, otherwise E if already in simplest form.<br>
</pre>
| // Exhaustively simplifies the given expression until no further simplification is possible.<br>
| // Note: Uses full satisfiability testing (unlike <a href='RewriterNormalize.md'>R_normalize</a>) as part of its simplification logic.<br>