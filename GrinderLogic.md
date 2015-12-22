A collection of Rewriters is included that can be applied to first order logic formulas.

### Logical Connectives ###
  * `not` (negation) e.g.s:
```
not true  -> false
not false -> true
not not X -> X
```
  * `and` (conjunction) e.g.s:
```
true and false -> false
true and X     -> X
and()          -> true
```
  * `or` (disjunction) e.g.s:
```
true or false -> true
false or X    -> X
or()          -> false
```
  * `=>` (implies) e.g.s:
```
X => Y -> not(X) or Y
```
  * `<=>` (equivalence) e.g.s:
```
X <=> Y -> (X and Y) or (not(X) and not(Y))
```

### Quantification ###
In addition, support for the standard quantification in first order logic:
  * `there exists . : .` (existential) : `there exists X : X != a`
  * `for all . : .` (universal): `for all X : X = a or X = b or X = c`

**Note:** Quantification of more than one variable is achieved by nesting the quantifiers, for example:

`for all X : for all Y : X != Y`

In standard first order logic, quantification can typically be done on variables only. We allow [function applications to be quantified as well](QuantificationOfFunctionApplications.md).