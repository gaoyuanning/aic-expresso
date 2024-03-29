/*
 * Copyright (c) 2013, SRI International
 * All rights reserved.
 * Licensed under the The BSD 3-Clause License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 * 
 * http://opensource.org/licenses/BSD-3-Clause
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the aic-expresso nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.sri.ai.grinder.library.equality.cardinality.direct.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.annotations.Beta;
import com.sri.ai.expresso.api.Expression;
import com.sri.ai.grinder.api.Rewriter;
import com.sri.ai.grinder.api.RewritingProcess;
import com.sri.ai.grinder.core.KindAttribute;
import com.sri.ai.grinder.core.OpenInterpretationModule;
import com.sri.ai.grinder.core.TotalRewriter;
import com.sri.ai.grinder.helper.Justification;
import com.sri.ai.grinder.helper.RewriterLoggingNamedRewriterFilter;
import com.sri.ai.grinder.library.AbsorbingElement;
import com.sri.ai.grinder.library.Associative;
import com.sri.ai.grinder.library.Disequality;
import com.sri.ai.grinder.library.Equality;
import com.sri.ai.grinder.library.FunctorConstants;
import com.sri.ai.grinder.library.PlainSubstitution;
import com.sri.ai.grinder.library.SyntacticFunctionsSubExpressionsProvider;
import com.sri.ai.grinder.library.boole.And;
import com.sri.ai.grinder.library.boole.ContradictoryConjuncts;
import com.sri.ai.grinder.library.boole.Equivalence;
import com.sri.ai.grinder.library.boole.Implication;
import com.sri.ai.grinder.library.boole.Not;
import com.sri.ai.grinder.library.boole.Or;
import com.sri.ai.grinder.library.controlflow.IfThenElse;
import com.sri.ai.grinder.library.controlflow.IfThenElseBranchesAreBooleanConstants;
import com.sri.ai.grinder.library.controlflow.IfThenElseConditionIsTrueInThenBranchAndFalseInElseBranch;
import com.sri.ai.grinder.library.controlflow.IfThenElseIrrelevantCondition;
import com.sri.ai.grinder.library.controlflow.IfThenElseSubExpressionsAndImposedConditionsProvider;
import com.sri.ai.grinder.library.controlflow.ImposedConditionsModule;
import com.sri.ai.grinder.library.controlflow.NormalizeDisequalities;
import com.sri.ai.grinder.library.controlflow.NormalizeEqualities;
import com.sri.ai.grinder.library.equality.CheapDisequalityModule;
import com.sri.ai.grinder.library.equality.NotOnDisequality;
import com.sri.ai.grinder.library.equality.NotOnEquality;
import com.sri.ai.grinder.library.equality.cardinality.direct.AbstractCardinalityRewriter;
import com.sri.ai.grinder.library.equality.cardinality.direct.CardinalityRewriter;
import com.sri.ai.grinder.library.number.GreaterThan;
import com.sri.ai.grinder.library.set.intensional.IntensionalSetWithFalseConditionIsEmptySet;

/**
 * Successively and exhaustively applies simplifications to a formula expression,
 * that is, one formed of boolean formula operators plus if-then-else.
 * 
 * @author braz
 *
 */
@Beta
public class FormulaSimplify extends AbstractCardinalityRewriter {
	private Rewriter rRootRewriter = null;
	
	public FormulaSimplify() {
		super();
	}
	
	@Override
	public String getName() {
		return CardinalityRewriter.R_formula_simplify;
	}
	
	public Rewriter getRootRewriter() {
		// Lazy initialize so that required supporting classes
		// can be setup an configured as necessary.
		if (rRootRewriter == null) {
			TotalRewriter rootRewriter = new TotalRewriter(getName()+" Total Rewriter", getAtomicRewriters());
			RewriterLoggingNamedRewriterFilter rewriterFilter = new RewriterLoggingNamedRewriterFilter();
			if (rewriterFilter.isRewriterFiltered(getName())) {
				rootRewriter.setOuterTraceEnabled(false);
			} 
			else {
				rootRewriter.setOuterTraceEnabled(true);
			}
			
			rRootRewriter = rootRewriter;
			this.updateChildRewriter(null, rRootRewriter);
		}
		return rRootRewriter;
	}
	
	@Override
	public Expression rewriteAfterBookkeeping(Expression expression, RewritingProcess process) {
		Justification.beginEqualityStep("basic simplifications");
		Expression result = getRootRewriter().rewrite(expression, process);
		Justification.endEqualityStep(result);
		return result;
	}
	
	//
	// PROTECTED METHODS
	//
	protected List<Rewriter> getAtomicRewriters() {
		ArrayList<Rewriter> result = new ArrayList<Rewriter>(
				Arrays.asList(new Rewriter[] {
						new PlainSubstitution(),
						new CardinalityOfType(),
						new IfThenElse(),

						new Equality(),
						new Disequality(),
						new NormalizeEqualities(),
						new NormalizeDisequalities(),
						new NotOnEquality(),
						new NotOnDisequality(), 
						new GreaterThan(), 

						new And(),
						new Or(),      
						new Not(), 
						new ContradictoryConjuncts(),
						new Implication(), 
						new Equivalence(), 
						new AbsorbingElement(
								"and", "false"),
						new AbsorbingElement(
								"or", "true"),
						new Associative("and"),
						new Associative("or"),
						
						// new FromConditionalFormulaToFormula(), // commented out because it potentially expands expression
						new FromConditionalFormulaWithConstantBooleanBranchToFormula(),
						// new DeMorgans(), // commenting out because it makes some stress tests much slower.
						// new, cheap simplifiers to be used instead of full ImpliedCertainty
						new IncompleteTopImpliedCertainty(),
						new TrivialForAllCases(),
						new TrivialThereExistsCases(),
						new ConjunctsHoldTrueForEachOther(),
						
						//
						// Support for: Quantifier Elimination
						// e.g.:
						// there exists X : a = X             -> true
						// there exists X: (X = a) => (X = b) -> true
						// there exists X: X != a and X = Y   -> Y != a
						new QuantifierEliminationWrapper(KindAttribute.VALUE_FOR_ALL),
						new QuantifierEliminationWrapper(KindAttribute.VALUE_THERE_EXISTS),
						new QuantifierEliminationWrapper(FunctorConstants.NOT),
						new QuantifierEliminationWrapper(FunctorConstants.IMPLICATION),
						new QuantifierEliminationWrapper(FunctorConstants.EQUIVALENCE),
						new QuantifierEliminationWrapper(FunctorConstants.AND),
						new QuantifierEliminationWrapper(FunctorConstants.OR),
										
						new IfThenElseIrrelevantCondition(),
						// new DisequalityToEqualityInIfThenElseCondition(),
						new IfThenElseBranchesAreBooleanConstants(),
						new IfThenElseConditionIsTrueInThenBranchAndFalseInElseBranch(),
						
						// only modules from here on: they don't actually
						// rewrite anything, so why test them sooner than
						// needed?
						new ImposedConditionsModule(),
						new CheapDisequalityModule(),
						new IfThenElseSubExpressionsAndImposedConditionsProvider(),
						new IntensionalSetWithFalseConditionIsEmptySet(), // Note: This is just a provider for scoped variables and not a rewriter.
						new SyntacticFunctionsSubExpressionsProvider("type"),
						new OpenInterpretationModule() }));
		
		return result;
	}
}