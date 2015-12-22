R\_card\_disjunction(| F |<sub>X</sub>, quantification) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/CardinalityDisjunction.java))
<pre>
inputs: F is a disjunction.<br>
"quantification" is either "there exists", "for all", or "none".<br>
return: a basic expression equivalent to | F |_X, if quantification is "none".<br>
If quantification is "for all", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not ||X||, it may be any value but ||X||.<br>
If quantification is "there exists", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not 0, it may be any value but 0.<br>
</pre>
| `if F is a disjunction which can be partitioned into a few subproblems`<br>
| <code>such that F can be partitioned into D, D</code><sub>1</sub><code>, and D</code><sub>2</sub><code>; where I</code><sub>1</sub><code> and I</code><sub>2</sub><code> are</code><br>
| <code>two partitions of the indices in X, and the index variables in D</code><sub>1</sub><code> and D</code><sub>2</sub><br>
| <code>are I</code><sub>1</sub><code> and I</code><sub>2</sub><code> respectively, and D is independent of X. Either D or D</code><sub>2</sub><code> may be empty.</code><br>
|.... <code>D</code><sub>1</sub><code> = </code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(D</code><sub>1</sub><code>)</code><br>
|.... <code>if D</code><sub>2</sub><code> is empty:</code><br>
|........ <code>R &lt;- </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(| D</code><sub>1</sub><code> |</code><sub>X</sub><code>, quantification)</code><br>
|.... <code>else</code><br>
|........ <code>D</code><sub>2</sub><code> = </code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(D</code><sub>2</sub><code>)</code><br>
|........ <code>R &lt;- </code><a href='RewriterNormalize.md'>R_normalize</a><code>(</code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(|D</code><sub>1</sub><code>|</code><sub>I1</sub><code>, quantification)*||I</code><sub>2</sub><code>|| +</code><br>
|............ <a href='RewriterCardWithQuantification.md'>R_card</a><code>(|D</code><sub>2</sub><code>|</code><sub>I2</sub><code>, quantification)*||I</code><sub>1</sub><code>|| -</code><br>
|............ <a href='RewriterCardWithQuantification.md'>R_card</a><code>(|D</code><sub>1</sub><code>|</code><sub>I1</sub><code>, quantification)* </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(|D</code><sub>2</sub><code>|</code><sub>I2</sub><code>, quantification))</code><br>
|.... <code>if D is empty:</code><br>
|........ <code>return R</code><br>
|.... <code>else</code><br>
|........ <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(if D then ||X|| else R)</code><br>
|<br>
| // Assume F is of the form F1 or F2<br>
| // | F1 or F2 |<sub>X</sub> = | F1 |<sub>X</sub> + | F2 |<sub>X</sub> - | F1 and F2 |<sub>X</sub><br>
| // but we do not compute it like that, see below for optimized version.<br>
|<br>
| <code>(F1, F2) &lt;- split_disjuncts_on_X(F) as follows:</code><br>
| <code>if F1 contains all the disjuncts independent of X (not empty)</code><br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(if F1 then ||X|| else R_card(|F2|</code><sub>X</sub><code>, quantification))</code><br>
|<br>
| <code>otherwise (if all disjuncts of F have some index variable occurring in them):</code><br>
| <code>F1 &lt;- first disjunct in F</code><br>
| <code>F2 &lt;- remaining disjuncts in F</code><br>
|<br>
| <code>F1 &lt;- </code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(F1)</code><br>
| <code>F2 &lt;- </code><a href='RewriterTopSimplify.md'>R_top_simplify</a><code>(F2)</code><br>
|<br>
| <code>if quantification is "for all"</code><br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(</code><br>
|........ <code>if </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(| </code><a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>(not F1 and not F2) |</code><sub>X</sub><code>, "there exists") &gt; 0</code><br>
|........ <code>then 0 else ||X||)</code><br>
|<br>
| <code>(F1, F2) &lt;- </code><a href='FunctionSortPair.md'>sort_pair</a><code>(F1, F2)</code><br>
|<br>
| <code>N1 &lt;- </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(| F1 |</code><sub>X</sub><code>, quantification)</code><br>
| <code>if N1 = 0</code><br>
|.... <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(| F2 |</code><sub>X</sub><code>, quantification)</code> // | F1 and F2 |<sub>X</sub> is 0<br>
| <code>if N1 = ||X||</code><br>
|.... <code>return ||X||</code> // | F2 |<sub>X</sub> = | F1 and F2 |<sub>X</sub> and cancel out<br>
|<br>
| <code>N2 &lt;- </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(| F2 |</code><sub>X</sub><code>, quantification)</code><br>
| <code>if N2 = 0</code><br>
|.... <code>return N1</code> // | F1 and F2 |<sub>X</sub> is 0<br>
| <code>if N2 = ||X||</code><br>
|.... <code>return ||X||</code> // N1 = | F1 |<sub>X</sub> = | F1 and F2 |<sub>X</sub> and cancel out<br>
|<br>
| <code>if quantification is "there exists"</code><br>
|.... // there is no need to compute N3, since it is enough that there is x in for either F1 or F2<br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(if N1 &gt; 0 or N2 &gt; 0 then ||X|| else 0)</code><br>
|<br>
| // quantification is guaranteed to be "none" because otherwise it would have returned by now<br>
| <code>N3 &lt;- </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>( | </code><a href='RewriterTopSimplifyConjunction.md'>R_top_simplify_conjunction</a><code>(F1 and F2) |</code><sub>X</sub><code>, "none" )</code><br>
| <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(N1 + N2 - N3)</code><br>
| // possible further optimization since F1 and F2 are already top-simplified<br>