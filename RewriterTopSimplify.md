R\_top\_simplify(F) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/TopSimplify.java))<br>
| <code>if F is a conjunction</code><br>
|.... <code>F &lt;- </code><a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>(F)</code><br>
| <code>if F is a disjunction</code><br>
|.... <code>F &lt;- </code><a href='RewriterTopSimplifyDisjunction.md'>R_top_simplify_disjunction</a><code>(F)</code><br>
| <code>return F</code><br>