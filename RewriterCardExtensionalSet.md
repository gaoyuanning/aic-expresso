R\_cardExtensionalSet( | {t<sub>1</sub>,...,t<sub>n</sub>} | ) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/CardinalityExtensionalSet.java))
<pre>
inputs: Each ti is an expression.<br>
return: a basic expression equivalent to | {t1,...,tn} |.<br>
</pre>
| `if n = 0` // empty set<br>
|.... <code>return 0</code><br>
| // cardinality of the set expression without the first element expression<br>
| <code>N &lt;- </code><a href='RewriterCardExtensionalSet.md'>R_cardExtensionalSet</a><code>( | {t</code><sub>2</sub><code>,...,t</code><sub>k</sub><code>} | )</code><br>
| <code>Irrelevant &lt;- </code><a href='RewriterNormalize.md'>R_normalize</a><code>(t</code><sub>1</sub><code> = t</code><sub>2</sub><code> or ... or t</code><sub>1</sub><code> = t</code><sub>n</sub><code>)</code><br>
| <code>return if Irrelevant then N else </code><a href='FunctionPlusOne.md'>plusOne</a><code>(N)</code><br>