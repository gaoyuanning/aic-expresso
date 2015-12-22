R\_card\_equivalence(| F |<sub>X</sub>, quantification) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/CardinalityEquivalence.java))
<pre>
inputs: F is an equivalence of the form "G <=> H".<br>
"quantification" is either "there exists", "for all", or "none".<br>
return: a basic expression equivalent to | F |_X, if quantification is "none".<br>
If quantification is "for all", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not ||X||, it may be any value but ||X||.<br>
If quantification is "there exists", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not 0, it may be any value but 0.<br>
</pre>
| `G <- `[R\_top\_simplify](RewriterTopSimplify.md)`(G)`<br>
| <code>H &lt;- </code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(H)</code><br>
| <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a>(| <a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>(G and H) |</code><sub>X</sub><code>, quantification) +</code><br>
|.... <a href='RewriterCardWithQuantification.md'>R_card</a><code>(| </code><a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>(not G and not H) |</code><sub>X</sub><code>, quantification)</code><br>