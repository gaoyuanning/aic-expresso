R\_card\_conjunction\_of\_disequalities(| F |<sub>X</sub>, quantification) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/CardinalityConjunctionOfDisequalities.java))
<pre>
inputs: F is a conjunction of disequalities. Every disequality in F contains at least one index variable in X.<br>
"quantification" is either "there exists", "for all", or "none".<br>
return: a basic expression equivalent to | F |_X, if quantification is "none".<br>
If quantification is "for all", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not ||X||, it may be any value but ||X||.<br>
If quantification is "there exists", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not 0, it may be any value but 0.<br>
</pre>
| if X = {x}:<br>
|.... <code>if quantification is "for all" and ( ASSUME_DOMAIN_ALWAYS_LARGE or |type(x)| &gt; 0 )</code><br>
|........ <code>return 0</code><br>
|.... <code>if quantification is "there exists" and ( ASSUME_DOMAIN_ALWAYS_LARGE or |type(x)| &gt; k )</code><br>
|........ <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>( |type(x)| )</code><br>
|.... <code>if quantification is "none"</code><br>
|........ <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>( |type(x)| - </code><a href='RewriterCardExtensionalSet.md'>R_cardExtensionalSet</a><code>(|{t</code><sub>1</sub><code>,...,t</code><sub>k</sub><code>}|) )</code><br>
| <code>if X = {x</code><sub>1</sub><code>, ..., </code>x<sub>n</sub><code>}:</code><br>
|.... <code>return </code><a href='RewriterSumOverOneVariable.md'>R_sum_over_one_variable</a><code>(sum_{x1:True} </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>( | F |</code><sub>{x2, ..., xn}</sub><code>, quantification))</code><br>