is\_tautology(F) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/IsTautology.java))
<pre>
inputs: F is a formula.<br>
return: whether F is a tautology(F)or not.<br>
</pre>
| `let x`<sub>1</sub>`, ..., x`<sub>n</sub>` be the free variables in F`<br>
| <code>return whether </code><a href='RewriterCompleteNormalize.md'>R_complete_normalize</a><code>( for all x</code><sub>1</sub><code> : ... for all x</code><sub>n</sub><code> : F )) is "True"</code><br>