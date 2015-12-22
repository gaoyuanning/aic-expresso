R\_sum\_over\_one\_variable(sum`_`{x: Cx} S) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/SumOverOneVariable.java))
<pre>
inputs: Cx is a formula in boolean logic, constraining the values of the summation index x.<br>
S is an Easily-Summable expression with respect to x, that is:<br>
if S is not an if-then-else expression, then x does not occur in S.<br>
if S is an expression of the form 'if Q then E1 else E2'<br>
where E1 and E2 are Easily-Summable with respect to x.<br>
return: a counting solution.<br>
</pre>
| `Cases for input:`<br>
| <code>S is "if F then S1 else S2"</code><br>
|.... <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(</code><a href='RewriterSumOverOneVariable.md'>R_sum_over_one_variable</a><code>(sum_{x:Cx and F} S1) + </code><a href='RewriterSumOverOneVariable.md'>R_sum_over_one_variable</a><code>(sum_{x:Cx and not F} S2)))</code><br>
| <code>S is a numeric expression</code><br>
|.... <code>if S is 0</code><br>
|........ <code>return 0</code><br>
|.... <code>else</code><br>
|........ <code>return </code><a href='RewriterNormalize.md'>R_normalize</a><code>(</code><a href='RewriterCard.md'>R_card</a><code>(|Cx|</code><sub>{x}</sub><code>) * S)</code>