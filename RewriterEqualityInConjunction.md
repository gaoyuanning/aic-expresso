R\_equality\_in\_conjunction(| x<sub>i</sub> = t and Phi |<sub>X</sub>, quantification) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/EqualityInConjunction.java))
<pre>
inputs: Phi is a formula, x_i is one of the index variables in X, t is a variable or a constant.<br>
"quantification" is either "there exists", "for all", or "none".<br>
return: a basic expression equivalent to | F |_X, if quantification is "none".<br>
If quantification is "for all", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not ||X||, it may be any value but ||X||.<br>
If quantification is "there exists", returns a counting-solution, the leaves<br>
of which may differ from the exact one in the following way:<br>
when the exact one is not 0, it may be any value but 0.<br>
</pre>
| `if t is the same expression as x`<sub>i</sub><br>
|.... <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(| Phi |</code><sub>X</sub><code>, quantification)</code><br>
| else<br>
|.... <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>(| </code><a href='RewriterNormalize.md'>R_normalize</a><code>(Phi[x</code><sub>i</sub><code> / t]) |</code><sub>X\{xi}</sub><code>, quantification)</code><br>