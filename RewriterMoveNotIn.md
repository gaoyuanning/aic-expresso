R\_move\_not\_in(F) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/MoveNotIn.java))
<pre>
inputs: F is a negation.<br>
return: a formula that is not a negation and is equivalent to "F"<br>
</pre>
| `Cases for input:`<br>
| <code>F is "not FALSE"</code><br>
|.... <code>return TRUE</code><br>
| <code>F is "not TRUE"</code><br>
|.... <code>return FALSE</code><br>
| <code>F is "not (t</code><sub>1</sub><code> = t</code><sub>2</sub><code> = ... = t</code><sub>n</sub><code>)</code>"<br>
|.... <code>return t</code><sub>1</sub><code> != t</code><sub>2</sub><code> or ... or t</code><sub>{n-1}</sub><code> != t</code><sub>n</sub><br>
| <code>F is "not x != t"</code><br>
|.... <code>return x = t</code><br>
| <code>F is "not not G"</code><br>
|.... <code>G &lt;- </code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(G)</code><br>
|.... <code>return G</code><br>
| <code>F is "not (G</code><sub>1</sub><code> and ... and G</code><sub>n</sub><code>)"</code><br>
|.... <code>(G</code><sub>1</sub><code> and ... and G</code><sub>n</sub><code>) &lt;- </code><a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>(G</code><sub>1</sub><code> and ... and G</code><sub>n</sub><code>)</code><br>
|.... <code>return not G</code><sub>1</sub><code> or ... or not G</code><sub>n</sub><br>
| <code>F is "not (G</code><sub>1</sub><code> or ... or G</code><sub>n</sub><code>)"</code><br>
|.... <code>(G</code><sub>1</sub><code> or ... or G</code><sub>n</sub><code>) &lt;- </code><a href='RewriterTopSimplifyDisjunction.md'>R_top_simplify_disjunction</a><code>(G</code><sub>1</sub><code> or ... or G</code><sub>n</sub><code>)</code><br>
|.... <code>return not G</code><sub>1</sub><code> and ... and not G</code><sub>n</sub><br>
| <code>F is "not (G =&gt; H)"</code><br>
|.... <code>return </code><a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>(G and not H)</code><br>
| <code>F is "not (G &lt;=&gt; H)"</code><br>
|... <code>return </code><a href='RewriterTopSimplifyDisjunction.md'>R_top_simplify_disjunction</a><code>(</code><br>
|....... <a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>(G and not H)</code><br>
|....... <code>or</code><br>
|....... <a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>(not G and H))</code><br>
| <code>F is "not (Qx G)" for Q a quantifier</code><br>
|.... <code>return Q'x not G</code><br>
|........ <code>where Q' is the opposite quantifier of Q</code><br>