worstCaseNumberOfDisjuncts(F, sign) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/WorstCaseNumberOfDisjuncts.java))
<pre>
inputs: F is a formula.<br>
sign is "true" or "false".<br>
return: a worst-case estimate of the number of disjuncts of the DNF<br>
equivalent to formula F, if sign is true, or to not F, if sign is false.<br>
</pre>
| `if F is not F'`<br>
|.... <code>return </code><a href='FunctionWorstCaseNumberOfDisjunctsWithSign.md'>worstCaseNumberOfDisjuncts</a><code>(F', not sign)</code><br>
|<br>
| <code>if F is F</code><sub>1</sub><code> and ... and F</code><sub>n</sub><br>
|.... <code>if sign</code><br>
|........ <code>return prod_i </code><a href='FunctionWorstCaseNumberOfDisjunctsWithSign.md'>worstCaseNumberOfDisjuncts</a><code>(F</code><sub>i</sub><code>, sign)</code><br>
|.... <code>else</code> // sign is negative, this will be a disjunction in the DNF.<br>
|........ <code>return sum_i</code> <a href='FunctionWorstCaseNumberOfDisjunctsWithSign.md'>worstCaseNumberOfDisjuncts</a><code>(F</code><sub>i</sub><code>, sign)</code><br>
|<br>
| <code>if F is F</code><sub>1</sub><code> or ... or F</code><sub>n</sub><br>
|.... <code>if sign</code><br>
|........ <code>return sum_i </code><a href='FunctionWorstCaseNumberOfDisjunctsWithSign.md'>worstCaseNumberOfDisjuncts</a><code>(F</code><sub>i</sub><code>, sign)</code><br>
|.... <code>else</code> // sign is negative, this will be a conjunction in the DNF.<br>
|........ <code>return prod_i </code><a href='FunctionWorstCaseNumberOfDisjunctsWithSign.md'>worstCaseNumberOfDisjuncts</a><code>(F</code><sub>i</sub><code>, sign)</code><br>
|<br>
| <code>if F is there exists X : F'</code><br>
|.... <code>return </code><a href='FunctionWorstCaseNumberOfDisjunctsWithSign.md'>worstCaseNumberOfDisjuncts</a><code>(F', sign)</code><br>
|<br>
| <code>if F is for all X : F'</code><br>
|.... <code>return </code><a href='FunctionWorstCaseNumberOfDisjunctsWithSign.md'>worstCaseNumberOfDisjuncts</a><code>(F', sign)</code><br>
|<br>
| // if the expression is not the application of a boolean connective as above,<br>
| // we assume an atom:<br>
| <code>return 1</code><br>