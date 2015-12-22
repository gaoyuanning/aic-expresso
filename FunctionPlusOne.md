plusOne(N) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/CardinalityExtensionalSet.java))
<pre>
inputs: N is a counting solution.<br>
return: a counting solution equivalent to N + 1.<br>
</pre>
| `If N is a number`<br>
|.... <code>return the number N + 1</code><br>
| <code>if N is if C then N1 else N2</code><br>
|.... <code>return if N then </code><a href='FunctionPlusOne.md'>plusOne</a><code>(N1) else </code><a href='FunctionPlusOne.md'>plusOne</a><code>(N2)</code><br>