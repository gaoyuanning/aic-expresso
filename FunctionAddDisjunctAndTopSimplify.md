add\_disjunct\_and\_top\_simplify(F, F<sub>1</sub> or ... or F<sub>n</sub>) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/AddDisjunctAndTopSimplify.java))
<pre>
inputs: F and each Fi is any formula.<br>
return: a top-simplified equivalent of F1 or ... or Fn and F in linear time.<br>
</pre>
| `if F is True`<br>
|.... <code>return True</code><br>
| <code>if </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F, F</code><sub>i</sub><code>), for some i</code><br>
|.... <code>return F</code><sub>1</sub><code> or ... or F</code><sub>n</sub><br>
| <code>if </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(not F, F</code><sub>i</sub><code>) or </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(not F</code><sub>i</sub><code>, F) for some i</code><br>
|.... <code>return True</code><br>
| <code>D &lt;- remove from F</code><sub>1</sub><code> or ... or F</code><sub>n</sub><code> all disjuncts F</code><sub>i</sub><code> such that </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(F</code><sub>i</sub><code>, F)</code><br>
| <code>return D or F</code><br>