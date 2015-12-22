replace\_conjunct\_and\_top\_simplify(F, i, F<sub>1</sub> and ... and F<sub>n</sub>) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/ReplaceConjunctAndTopSimplify.java))
<pre>
This function is intended for simplifying conjunctions obtained from replacing a given conjunct<br>
by another in a conjunction that has already been checked for obvious simplifications.<br>
inputs: F and each Fi is any formula.<br>
i is an integer in {1,..., n}<br>
return: a possibly simplified expression equivalent to<br>
F1 and ... and Fi-1 and F and Fi+1 and ... and Fn, in linear time.<br>
</pre>
| `if F is False`<br>
|.... <code>return False</code><br>
| <code>if </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F</code><sub>j</sub><code>, F), for some j != i</code><br>
|.... <code>return F</code><sub>1</sub><code> and ... and F</code><sub>i-1</sub><code> and F</code><sub>i+1</sub><code> and ... and F</code><sub>n</sub><br>
| <code>if </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F, not F</code><sub>j</sub><code>) or </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F</code><sub>j</sub><code>, not F) for some j != i</code><br>
|.... <code>return False</code><br>
| <code>C &lt;- F</code><sub>1</sub><code> and ... and F</code><sub>i-1</sub><code> and F and F</code><sub>i+1</sub><code> and ... and F</code><sub>n</sub><br>
| <code>C &lt;- remove from C all conjuncts F</code><sub>j</sub><code>, j != i, such that </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F, F</code><sub>j</sub><code>)</code><br>
| <code>return C</code><br>