R\_top\_simplify\_conjunction(F1 and ... and Fn) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/TopSimplifyConjunction.java))
<pre>
inputs: Each Fi is any formula.<br>
return: simplification of top level of conjunction.<br>
</pre>
| `T <- empty tuple` // T will contain conjuncts of F not equal to True or False<br>
| <code>for all i</code><br>
|.... <code>if F</code><sub>i</sub><code> is False or Alpha != Alpha</code><br>
|........ <code>return False</code><br>
|.... <code>if F</code><sub>i</sub><code> is not "True" and F</code><sub>i</sub><code> is not Alpha = Alpha</code><br>
|........ <code>add F</code><sub>i</sub><code> to T</code><br>
|<br>
| <code>T' &lt;- empty tuple</code> // T' will contain the elements of T that are needed in the final conjunction<br>
| <code>irrelevant &lt;- empty set</code><br>
| <code>for all i in 1,..., |T|</code><br>
|.... <code>if T</code><sub>i</sub><code> is in irrelevant</code><br>
|........ <code>continue to next i</code><br>
|.... <code>i_is_irrelevant &lt;- false</code><br>
|.... <code>for all j in i + 1, ..., |T|</code><br>
|........ <code>if </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(T</code><sub>j</sub><code>, T</code><sub>i</sub><code>)</code><br>
|............ <code>i_is_irrelevant &lt;- true</code><br>
|............ <code>continue to next i</code><br>
|........ <code>if </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(T</code><sub>i</sub><code>, T</code><sub>j</sub><code>)</code><br>
|............ <code>irrelevant &lt;- irrelevant union { T</code><sub>j</sub><code> }</code><br>
|........ <code>else if </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(T</code><sub>i</sub><code>, not T</code><sub>j</sub><code>) or </code><a href='FunctionIncompleteLinearImplies.md'>incomplete_linear_implies</a><code>(T</code><sub>j</sub><code>, not T</code><sub>i</sub><code>)</code><br>
|............ <code>return False</code> // conjunction is contradiction<br>
|.... <code>if not i_is_irrelevant</code><br>
|........ <code>add T</code><sub>i</sub><code> to T'</code><br>
|<br>
| <code>return conjunction on elements of T'</code><br>