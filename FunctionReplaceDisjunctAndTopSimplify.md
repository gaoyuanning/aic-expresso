replace\_disjunct\_and\_top\_simplify(F, i, F<sub>1</sub> or ... or F<sub>n</sub>) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/ReplaceDisjunctAndTopSimplify.java))
<pre>
This function is intended for simplifying disjunctions obtained from replacing a given disjunct<br>
by another in a disjunction that has already been checked for obvious simplifications.<br>
inputs: F and each Fi is any formula.<br>
i is an integer in {1,..., n}<br>
return: a possibly simplified expression equivalent to<br>
F1 or ... or Fi-1 or F or Fi+1 or ... or Fn, in linear time.<br>
</pre>
| `if F is True`<br>
|.... <code>return True</code><br>
| <code>if </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F, F</code><sub>j</sub><code>), for some j != i</code><br>
|.... <code>return F</code><sub>1</sub><code> or ... or F</code><sub>i-1</sub><code> or F</code><sub>i+1</sub><code> or ... or F</code><sub>n</sub><br>
| <code>if </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(not F, F</code><sub>j</sub><code>) or </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(not F</code><sub>j</sub><code>, F) for some j != i</code><br>
|.... <code>return True</code><br>
| <code>D &lt;- F</code><sub>1</sub><code> or ... or F</code><sub>i-1</sub><code> or F or F</code><sub>i+1</sub><code> or ... or F</code><sub>n</sub><br>
| <code>D &lt;- remove from D all disjuncts F</code><sub>j</sub><code>, j != i, such that </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F</code><sub>j</sub><code>, F)</code><br>
| <code>return D</code><br>