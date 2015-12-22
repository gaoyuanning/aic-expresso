# Overview #
A library of rewriters had been developed for performing model counting (and quantifier elimination) in a quantified, function-free equality logic, where equality is the only predicate (free variables allowed). For details see:

  * Braz, Saadati, Bui, O'Reilly: [Lifted Arbitrary Constraint Solving for Lifted Probabilistic Inference](http://aic-expresso.googlecode.com/svn/wiki/docs/LiftedArbitraryConstraintSolvingForLiftedProbabilisticInference.pdf). In: [2nd International Workshop on Statistical Relational AI](http://tsi.wfubmc.edu/labs/strait/StaRAI/starai.html). (2012)

## Pseudo-Code for Computing the Cardinallity of a Set ##
Let `L` be a language with no function symbols and a single predicate representing equality, but it may have distinct constant symbols. Let `F` be a quantifier formula in `L`. Let
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="|F|\_{x\_1, ..., x\_n}" height="25"/>

denote the cardinality of the set:

> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="{(x1, ..., xn) | F}\_{x\_1, ..., x\_n}" height="25"/>

We shall define a series of rewriters to compute:

> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="|F|\_{x\_1, ..., x\_n}" height="25"/>

We need the following definition for our process.

Definition:  An expression `E` is Easily Summable `(ES)` with respect to variables x<sub>1</sub>,...,x<sub>n</sub> iff it has one of the following forms:
  * `E` is some constant `c`.
  * `E` is a variable other than x<sub>1</sub>,...,x<sub>n</sub>
  * `E` is of the form "if C then t<sub>1</sub> else t<sub>2</sub>" where t<sub>1</sub> and t<sub>2</sub> are `ES` with respect to x<sub>1</sub>,...,x<sub>n</sub> and `C` is a quantifier-free formaula in `L`.
  * `E` is of the form f(t<sub>1</sub>, ..., t<sub>n</sub>) where t<sub>1</sub>,...,t<sub>n</sub> are `ES` with respect to x<sub>1</sub>,...,x<sub>n</sub>.

For a set of index variables `X`, `||X||` denotes the project of the cardinalities of domain sizes of index variables in `X`. For instance, if `X = {x, y}`, then
> <wiki:gadget url="http://mathml-gadget.googlecode.com/svn/trunk/mathml-gadget.xml" border="0" up\_content="||X|| = |type(X)| xx |type(Y)|" height="25"/>


### List of Cardinality Rewriters ###
  * [R\_card(| F |\_{x1, ..., xn})](RewriterCard.md)
  * [R\_card( | F |\_X, quantification )](RewriterCardWithQuantification.md)
  * [R\_sum\_over\_one\_variable(sum\_{x: Cx} S)](RewriterSumOverOneVariable.md)
  * [R\_card\_conjunction( | F |\_X, quantification )](RewriterCardConjunction.md)
  * [R\_equality\_in\_conjunction(| x\_i = t and Phi |\_X, quantification)](RewriterEqualityInConjunction.md)
  * [R\_card\_conjunction\_of\_disequalities(| F |\_X, quantification)](RewriterCardConjunctionOfDisequalities.md)
  * [R\_card\_disjunction(| F |\_X, quantification)](RewriterCardDisjunction.md)
  * [R\_card\_implication(| F |\_X, quantification)](RewriterCardImplication.md)
  * [R\_card\_equivalence(| F |\_X, quantification)](RewriterCardEquivalence.md)
  * [R\_top\_simplify(F)](RewriterTopSimplify.md)
  * [R\_top\_simplify\_disjunction(F1 or ... or Fn)](RewriterTopSimplifyDisjunction.md)
  * [R\_top\_simplify\_conjunction(F1 and ... and Fn)](RewriterTopSimplifyConjunction.md)
  * [R\_move\_not\_in(F)](RewriterMoveNotIn.md)
  * [R\_top\_quantifier\_elimination(Qx F)](RewriterTopQuantifierElimination.md)
  * [R\_quantifier\_elimination(F)](RewriterQuantifierElimination.md)
  * [R\_cardExtensionalSet( | {t1,...,tn} | )](RewriterCardExtensionalSet.md)
  * [R\_normalize(F)](RewriterNormalize.md)

### List of Cardinality Support Functions ###
  * [negationHasLessNumberOfDisjuncts(F)](FunctionNegationHasLessNumberOfDisjuncts.md)
  * [pick\_cheapest( Candidates )](FunctionPickCheapest.md)
  * [sort\_pair(F1, F2)](FunctionSortPair.md)
  * [replace\_conjunct\_and\_top\_simplify(F, i, F1 and ... and Fn)](FunctionReplaceConjunctAndTopSimplify.md)
  * [add\_conjunct\_and\_top\_simplify(F, F1 and ... and Fn)](FunctionAddConjunctAndTopSimplify.md)
  * [replace\_disjunct\_and\_top\_simplify(F, i, F1 or ... or Fn)](FunctionReplaceDisjunctAndTopSimplify.md)
  * [add\_disjunct\_and\_top\_simplify(F, F1 or ... or Fn)](FunctionAddDisjunctAndTopSimplify.md)
  * [is\_contradiction(F)](FunctionIsContradiction.md)
  * [is\_tautology(F)](FunctionIsTautology.md)
  * [incomplete\_linear\_implies(G, H)](FunctionIncompleteLinearImplies.md)
  * [plusOne(N)](FunctionPlusOne.md)
  * [worstCaseNumberOfDisjuncts(F)](FunctionWorstCaseNumberOfDisjuncts.md)
  * [worstCaseNumberOfDisjuncts(F, sign)](FunctionWorstCaseNumberOfDisjunctsWithSign.md)