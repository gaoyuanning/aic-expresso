pick\_cheapest( Candidates ) - ([implementation](http://code.google.com/p/aic-expresso/source/browse/trunk/src/main/java/com/sri/ai/grinder/library/equality/cardinality/direct/core/PickCheapest.java))<br>
| // cost of an expression is its size times the number of disjunctive operators in it<br>
| // or, => and <=> are disjunctive operators<br>
| // implementation note: when picking the smallest F<sub>i</sub>, down-recurse all candidates in parallel<br>
| // keeping the lower bound (accumulated cost so far) for each of them. Stop when you have the<br>
| // final cost of a candidate and it is cheaper than the lower bounds of all others. This will avoid<br>
| // having to recurse down all of them till the end.<br>