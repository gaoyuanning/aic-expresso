R\_quantifier\_elimination(F) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/QuantifierElimination.java))
<pre>
inputs: F is any formula.<br>
return: a quantifier-free formula equivalent to F<br>
</pre>
| `if F is "for all x: Y"`<br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(</code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(|</code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(Y)|</code><sub>X</sub><code>, "for all") = |type(x)| )</code><br>
| <code>if F is "there exists x: Y"</code><br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(</code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(|</code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(Y)|</code><sub>X</sub><code>, "there exists") &gt; 0)</code><br>
| <code>if F is "not G"</code><br>
|.... <code>return </code><a href='RewriterQuantifierElimination.md'>R_quantifier_elimination</a><code>(</code><a href='RewriterMoveNotIn.md'>R_move_not_in</a><code>(not G))</code><br>
| <code>if F is G =&gt; H</code><br>
|.... <code>return </code><a href='RewriterQuantifierElimination.md'>R_quantifier_elimination</a><code>(</code><a href='RewriterTopSimplifyDisjunction.md'>R_top_simplify_disjunction</a><code>(not G or H))</code><br>
| <code>if F is G &lt;=&gt; H</code><br>
|.... <code>return </code><a href='RewriterQuantifierElimination.md'>R_quantifier_elimination</a><code>(</code><a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>((not G or H) and (G or not H)))</code><br>
| <code>if F is a conjunction F</code><sub>1</sub><code> and ... and F</code><sub>n</sub><br>
|.... <code>F' &lt;- True</code><br>
|.... <code>i &lt;- 1</code><br>
|.... <code>while F' is not "False" and i &lt;= n</code><br>
|........ <code>G</code><sub>i</sub><code> &lt;- </code><a href='RewriterQuantifierElimination.md'>R_quantifier_elimination</a><code>(F</code><sub>i</sub><code>)</code><br>
|........ <code>F' &lt;- </code><a href='FunctionAddConjunctAndTopSimplify.md'>add_conjunct_and_top_simplify</a><code>(G</code><sub>i</sub><code>, F')</code><br>
|........ <code>i &lt;- i + 1</code><br>
|.... <code>return F'</code><br>
| <code>if F is a disjunction F</code><sub>1</sub><code> or ... or F</code><sub>n</sub><br>
|.... <code>F' &lt;- False</code><br>
|.... <code>i &lt;- 1</code><br>
|.... <code>while F' is not "True" and i &lt;= n</code><br>
|........ <code>G</code><sub>i</sub><code> &lt;- </code><a href='RewriterQuantifierElimination.md'>R_quantifier_elimination</a><code>(F</code><sub>i</sub><code>)</code><br>
|........ <code>F' &lt;- </code><a href='FunctionAddDisjunctAndTopSimplify.md'>add_disjunct_and_top_simplify</a><code>(G</code><sub>i</sub><code>, F')</code><br>
|........ <code>i &lt;- i + 1</code><br>
|.... <code>return F'</code><br>
| <code>return F</code><br>