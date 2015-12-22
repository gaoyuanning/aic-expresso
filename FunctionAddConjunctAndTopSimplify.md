add\_conjunct\_and\_top\_simplify(F, F<sub>1</sub> and ... and F<sub>n</sub>) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/AddConjunctAndTopSimplify.java))
<pre>
inputs: F and each Fi is any formula.<br>
return: a top-simplified equivalent of F1 and ... and Fn and F in linear time.<br>
</pre>
| `if F is False`<br>
|.... <code>return False</code><br>
| <code>if </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F</code><sub>i</sub><code>, F), for some i</code><br>
|.... <code>return F</code><sub>1</sub><code> and ... and F</code><sub>n</sub><br>
| <code>if </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F, not F</code><sub>i</sub><code>) or </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F</code><sub>i</sub><code>, not F) for some i</code><br>
|.... <code>return False</code><br>
| <code>C &lt;- remove from F</code><sub>1</sub><code> and ... and F</code><sub>n</sub><code> all conjuncts F</code><sub>i</sub><code> such that </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F, F</code><sub>i</sub><code>)</code><br>
| <code>return C and F</code><br>