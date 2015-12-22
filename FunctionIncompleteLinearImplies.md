incomplete\_linear\_implies(G, H) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/IncompleteLinearImplies.java))
<pre>
Runs in linear time on the size of G and H.<br>
inputs: G and H are formulas.<br>
return: whether G is known to imply H according the methods used by this function<br>
(a return value of "false" does not mean that G does not imply H,<br>
it merely means that this function does not know that it does).<br>
</pre>
| `if G and H are the same expression`<br>
|........ <code>or</code><br>
|.... <code>G is "t</code><sub>1</sub><code> = t</code><sub>2</sub><code>" or "not (t</code><sub>1</sub><code> != t</code><sub>2</sub><code>)" and</code><br>
|.... <code>H is "t</code><sub>2</sub><code> = t</code><sub>1</sub><code>" or "not (t</code><sub>2</sub><code> != t</code><sub>1</sub><code>)"</code><br>
|........ <code>or</code><br>
|.... <code>G is "t</code><sub>1</sub><code> = c</code><sub>1</sub><code>" or G is "c</code><sub>1</sub><code> = t</code><sub>1</sub><code>" with c</code><sub>1</sub><code> a constant,</code><br>
|.... <code>and H is "not t</code><sub>1</sub><code> = c</code><sub>2</sub><code>" or H is "not c</code><sub>2</sub><code> = t</code><sub>1</sub><code>" or H is "t</code><sub>1</sub><code> != c</code><sub>2</sub><code>" or H is "c</code><sub>2</sub><code> != t</code><sub>1</sub><code>"</code><br>
|.... <code>with c</code><sub>2</sub><code> a constant distinct from c</code><sub>1</sub><br>
|........ <code>or</code><br>
|.... <code>G is t</code><sub>1</sub><code> != t</code><sub>2</sub><code> or "not (t</code><sub>1</sub><code> = t</code><sub>2</sub><code>)" and H is "t</code><sub>2</sub><code> != t</code><sub>1</sub><code>" or "not (t</code><sub>2</sub><code> = t</code><sub>1</sub><code>)"</code><br>
|........ <code>return true</code><br>
| <code>return false</code><br>