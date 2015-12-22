  * [Introduction](Introduction.md)
  * [Expresso (Representation)](ExpressoOverview.md)
  * [Grinder (Rewriting System)](GrinderOverview.md)
    * [Arithmetic](GrinderArithmetic.md)
    * [Sets](GrinderSets.md)
    * [Logic](GrinderLogic.md)
      * [Quantification of Function Applications](QuantificationOfFunctionApplications.md)
    * [Model Counting](ModelCountingOverview.md)
      * Rewriters
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
        * [R\_simplify(F)](RewriterSimplify.md)
        * [R\_complete\_normalize(F)](RewriterCompleteNormalize.md)
        * [R\_complete\_simplify(F)](RewriterCompleteSimplify.md)
      * Supporting Functions
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
    * [Substituting Expressions](GrinderSubstitute.md)
  * [Configuration Options](ConfigurationOptions.md)
  * For Developers/Contributors
    * [Getting Started](GettingStarted.md)
    * [Coding Conventions](CodingConventions.md)
    * [Creating a Release](CreatingARelease.md)
    * [Deploy Demo](DeployingDemoApp.md)
  * [Road Map](RoadMap.md)
  * [Releases](ReleaseHistory.md)
    * [Release 1.0.1](Release_1_0_1.md)
  * [Acknowledgements](Acknowledgements.md)