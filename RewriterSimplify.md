R`_`simplify(F) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/Simplify.java))
<pre>
inputs: F is an expression.<br>
return: a simplification of F if possible, otherwise F if already in simplest form.<br>
</pre>
| // Exhaustively simplifies the given expression until no further simplification is possible.<br>
| // Note: It will not necessarily use full satisfiability testing as part of its simplification logic.<br>