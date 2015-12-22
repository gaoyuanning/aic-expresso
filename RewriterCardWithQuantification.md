R\_card( | F |<sub>X</sub>, quantification ) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/Cardinality.java))
<pre>
inputs: F is any top-simplified formula.<br>
quantification is "none", "for all", or "there exists".<br>
return: a basic expression equivalent to | F |_X, if quantification is "none".<br>
If quantification is "for all", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not ||X||, it may be any value but ||X||.<br>
If quantification is "there exists", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not 0, it may be any value but 0.<br>
</pre>
| `if F is True`<br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(||X||)</code><br>
|<br>
| <code>if F is False</code><br>
|.... <code>return 0</code><br>
|<br>
| <code>if x does not occur in F for any x in X</code><br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(if F then ||X|| else 0)</code><br>
|<br>
| <code>if F is a conjunction // including F being a literal or a multi-equality</code><br>
|.... <code>return </code><a href='RewriterCardConjunction.md'>R_card_conjunction</a><code>(|F|</code><sub>X</sub><code>, quantification)</code><br>
|<br>
| <code>if F is disjunction</code><br>
|.... <code>return </code><a href='RewriterCardDisjunction.md'>R_card_disjunction</a><code>(| F |</code><sub>X</sub><code>, quantification)</code><br>
|<br>
| <code>if F is "not G"</code><br>
|.... <code>F' &lt;- </code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(</code><a href='RewriterMoveNotIn.md'>R_move_not_in</a><code>(F))</code><br>
|.... <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(| F' |</code><sub>X</sub><code>, quantification)</code><br>
|<br>
| <code>if F is an implication of the form G =&gt; H</code><br>
|.... <code>return </code><a href='RewriterCardImplication.md'>R_card_implication</a><code>( | F |</code><sub>X</sub><code>, quantification)</code><br>
|<br>
| <code>if F is an equivalence of the form G &lt;=&gt; H</code><br>
|.... <code>return </code><a href='RewriterCardEquivalence.md'>R_card_equivalence</a><code>( | F |</code><sub>X</sub><code>, quantification)</code><br>
|<br>
| <code>if F is Q y : G</code><br>
|.... <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>( | </code><a href='RewriterTopQuantifierElimination.md'>R_top_quantifier_elimination</a><code>(Q y : G) |</code><sub>X</sub><code>, quantification)</code>