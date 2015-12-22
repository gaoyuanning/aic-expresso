R\_complete\_normalize(F) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/CompleteNormalize.java))
<pre>
inputs: F is an expression.<br>
return: a normalization of E, that is, an equivalent expression with all if-then-elses externalized.<br>
The expression is also simplified per R_complete_simplify, which includes being completely free of unreachable sub-expressions.<br>
</pre>