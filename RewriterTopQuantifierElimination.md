R\_top\_quantifier\_elimination(Qx F) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/TopQuantifierElimination.java))
<pre>
inputs: Q is a quantifier ("there exists", or "for all") over x, F is a boolean formula on equalities.<br>
return: an equivalent formula to Qx F by eliminating the quantification Qx.<br>
</pre>
| `F <- `[R\_top\_simplify](RewriterTopSimplify.md)`(F)`<br>
| <code>if Q is "for all"</code><br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(</code><a href='RewriterCard.md'>R_card</a><code>(|F|</code><sub>x</sub><code>, Q) = |type(x)| )</code><br>
| <code>if Q is "there exists"</code><br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(</code><a href='RewriterCard.md'>R_card</a><code>(|F|</code><sub>x</sub><code>, Q) &gt; 0)</code><br>