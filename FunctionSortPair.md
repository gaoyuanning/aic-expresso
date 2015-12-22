sort\_pair(F1, F2) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/SortPair.java))
<pre>
inputs: F1 and F2 are formulas.<br>
return: a tuple with the cheapest formula in the first position.<br>
</pre>
| `(F', k) <- `[pick\_cheapest](FunctionPickCheapest.md)`( (F1, 1), (F2, 2) )`<br>
| <code>if k = 1</code><br>
|.... <code>return (F1, F2)</code><br>
| <code>return (F2, F1)</code><br>