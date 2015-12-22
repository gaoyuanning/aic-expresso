R\_card\_conjunction( | F |<sub>X</sub>, quantification ) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/CardinalityConjunction.java))
<pre>
inputs: F is False, True, a literal, a multi-equality, or a conjunction.<br>
"quantification" is either "there exists", "for all", or "none".<br>
return: a basic expression equivalent to | F |_X, if quantification is "none".<br>
If quantification is "for all", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not ||X||, it may be any value but ||X||.<br>
If quantification is "there exists", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not 0, it may be any value but 0.<br>
</pre>
| `if F is a conjunction which can be partitioned into two or more independent sub problems`<br>
| // i.e. there is a partition {I<sub>1</sub>, ..., I<sub>k</sub>} of indices such that there is a partition {C<sub>1</sub>, ..., C<sub>k</sub>}<br>
| // of the conjuncts of F where indices in I<sub>j</sub> occur in C<sub>j</sub> only, for every j:<br>
|.... <code>if I</code><sub>1</sub><code> is empty:</code><br>
|........ <code>return if C</code><sub>1</sub><code> then </code><a href='RewriterNormalize.md'>R_normalize</a><code>(</code><br>
|............ <a href='RewriterCardWithQuantification.md'>R_card</a><code>(|</code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(C</code><sub>2</sub><code>)|</code><sub>I2</sub><code>, quantification)  * ... *</code><br>
|............ <a href='RewriterCardWithQuantification.md'>R_card</a><code>(|</code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(C</code><sub>k</sub><code>)|</code><sub>Ik</sub><code>, quantification) ) else 0</code><br>
|.... <code>else</code><br>
|........ <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(</code><br>
|............ <a href='RewriterCardWithQuantification.md'>R_card</a><code>(|</code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(C</code><sub>1</sub><code>)|</code><sub>I1</sub><code>, quantification)  * ... *</code> <br>
|............ <a href='RewriterCardWithQuantification.md'>R_card</a><code>(|</code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(C</code><sub>k</sub><code>)|</code><sub>Ik</sub><code>, quantification) )</code><br>
|<br>
| <code>if F is True or empty conjunction</code><br>
|.... <code>return ||X||</code><br>
|<br>
| <code>if F is False</code><br>
|.... <code>return 0</code><br>
|<br>
| <code>if F is F1 and F2 where F1 is a formula independent of all x's in X</code><br>
|.... // note that F1 is the conjunction of all conjuncts of F that are X-independent, or F itself if it is X-free<br>
|....<code>F2 = </code><a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>(F2)</code><br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>( if F1 then </code><a href='RewriterCardConjunction.md'>R_card_conjunction</a><code>(| F2 |</code><sub>X</sub><code>, quantification) else 0 )</code><br>
|<br>
| <code>if F is a conjunction of the form 'x = t and Phi', where x is one of the index variables in X</code><br>
|.... <code>return </code><a href='RewriterEqualityInConjunction.md'>R_equality_in_conjunction</a><code>(| F |</code><sub>X</sub><code>, quantification)</code><br>
|<br>
| <code>if F is x1 != t1 and ... xn != tk</code> // F is a conjunction of disequalities<br>
|.... <code>return </code><a href='RewriterCardConjunctionOfDisequalities.md'>R_card_conjunction_of_disequalities</a><code>(| F |</code><sub>X</sub><code>, quantification)</code><br>
|<br>
| <code>Candidates &lt;- { (Fi, i) : Fi satisfies one of the cases below }</code><br>
| <code>(Fi, i) &lt;- </code><a href='FunctionPickCheapest.md'>pick_cheapest</a><code>( Candidates )</code><br>
|<br>
|<code>if Fi is "not G"</code><br>
|.... <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(|</code><a href='FunctionReplaceConjunctAndTopSimplify.md'>replace_conjunct_and_top_simplify</a><code>(</code><a href='RewriterMoveNotIn.md'>R_move_not_in</a><code>(Fi), i, F)|</code><sub>X</sub><code>, quantification )</code><br>
|<br>
| <code>if Fi is (nested) conjunction (G1 and ... and Gk)</code><br>
|.... <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(| </code><a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>(F1 and ... Fi-1 and G1 and ... and Gk and Fi+1 and ... and Fn) |</code><sub>X</sub><code>, quantification )</code><br>
|<br>
| <code>if Fi is G1 =&gt; G2</code>
|.... <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(</code><br>
|........ <code>| </code><a href='RewriterTopSimplifyDisjunction.md'>R_top_simplify_disjunction</a><code>(</code><a href='FunctionReplaceConjunctAndTopSimplify.md'>replace_conjunct_and_top_simplify</a><code>((not G1), i, F)</code><br>
|........ <code>or</code><br>
|........ <a href='FunctionReplaceConjunctAndTopSimplify.md'>replace_conjunct_and_top_simplify</a><code>(G2, i, F)) |</code><sub>X</sub><code>, quantification )</code><br>
|<br>
| <code>if Fi is G1 &lt;=&gt; G2</code><br>
|.... <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(</code><br>
|........ <code>| </code><a href='FunctionReplaceConjunctAndTopSimplify.md'>replace_conjunct_and_top_simplify</a><code>(G1 and G2), i, F) |</code><sub>X</sub><code>, quantification)</code><br>
|........ <code>+ </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(</code><br>
|........ <code>| </code><a href='FunctionReplaceConjunctAndTopSimplify.md'>replace_conjunct_and_top_simplify</a><code>(not G1 and not G2), i, F) |</code><sub>X</sub><code>, quantification)</code><br>
|<br>
| <code>if Fi is (F1 or F2)</code><br>
|.... <code>return</code> <a href='RewriterCardWithQuantification.md'>R_card</a><code>(</code><br>
|........ <code>| </code><a href='RewriterTopSimplifyDisjunction.md'>R_top_simplify_disjunction</a><code>(</code><a href='FunctionReplaceConjunctAndTopSimplify.md'>replace_conjunct_and_top_simplify</a><code>(F1, i, F)</code><br>
|........ <code>or</code><br>
|........ <a href='FunctionReplaceConjunctAndTopSimplify.md'>replace_conjunct_and_top_simplify</a><code>(F2, i, F)) |</code><sub>X</sub><code>, quantification )</code><br>
|<br>
| <code>if Fi is Q y : G</code><br>
|.... <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(| </code><a href='FunctionReplaceConjunctAndTopSimplify.md'>replace_conjunct_and_top_simplify</a><code>(</code><a href='RewriterTopQuantifierElimination.md'>R_top_quantifier_elimination</a><code>(Fi), i, F) |</code><sub>X</sub><code>, quantification )</code><br>