R\_card(| F |_{x<sub>1</sub>, ..., x<sub>n</sub>}_) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/Cardinality.java))
<pre>
inputs: F a first-order formula on equalities,<br>
i.e. F is a boolean combination of equalities and disequalities of variables and constants.<br>
F may have quantifiers.<br>
return: a basic expression representing |F|.<br>
</pre>
| `F <- `[R\_top\_simplify](RewriterTopSimplify.md)`(F) // this is the only function that does not assume its input is top-simplified`<br>
|<br>
| <code>if n = 0</code><br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(if F then 1 else 0)</code><br>
|<br>
| <code>if n &gt; 0</code><br>
|.... <code>if </code><a href='FunctionNegationHasLessNumberOfDisjuncts.md'>negationHasLessNumberOfDisjuncts</a><code>(F):</code><br>
|........ <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(||X|| - </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>( | not F |</code><sub>X</sub>,<code> "none" ))</code><br>
|.... <code>else</code><br>
|........ <code>return </code><a href='RewriterCardWithQuantification.md'>R_card</a><code>( | F |</code><sub>X</sub><code>, "none")</code>