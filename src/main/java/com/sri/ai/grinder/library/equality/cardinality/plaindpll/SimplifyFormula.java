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
package com.sri.ai.grinder.library.equality.cardinality.plaindpll;

import static com.sri.ai.util.Util.mapIntoList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import com.google.common.annotations.Beta;
import com.sri.ai.expresso.api.Expression;
import com.sri.ai.expresso.helper.Expressions;
import com.sri.ai.grinder.api.RewritingProcess;
import com.sri.ai.grinder.library.Disequality;
import com.sri.ai.grinder.library.Equality;
import com.sri.ai.grinder.library.FunctorConstants;
import com.sri.ai.grinder.library.boole.And;
import com.sri.ai.grinder.library.boole.Equivalence;
import com.sri.ai.grinder.library.boole.ForAll;
import com.sri.ai.grinder.library.boole.Implication;
import com.sri.ai.grinder.library.boole.Or;
import com.sri.ai.grinder.library.boole.ThereExists;
import com.sri.ai.grinder.library.controlflow.IfThenElse;
import com.sri.ai.grinder.library.equality.formula.FormulaToDNF;
import com.sri.ai.util.Util;
import com.sri.ai.util.base.Equals;
import com.sri.ai.util.base.Not;

@Beta
/** 
 * A class for plain, 
 * non-rewriter simplification of equality boolean formulas.
 */
public class SimplifyFormula {

	public static Expression simplify(Expression formula, RewritingProcess process) {
		formula = topSimplify(formula, process);
		if (formula.getSyntacticFormType().equals("Function application")) {
			ArrayList<Expression> simplifiedArguments =
					Util.mapIntoArrayList(formula.getArguments(), e -> simplify(e, process));
			formula = Expressions.apply(formula.getFunctor(), simplifiedArguments);
			formula = topSimplify(formula, process);
		}
		return formula;
	}

	/**
	 * Simplifies the top expression until it cannot be simplified anymore.
	 * Always returns either a symbol or a function application (quantified formulas have their top quantifiers eliminated).
	 * @param formula
	 * @param process
	 * @return
	 */
	public static Expression topSimplify(Expression formula, RewritingProcess process) {
		
		Expression previous;
		do {
			previous = formula;
			
			if (formula.hasFunctor(FunctorConstants.EQUALITY)) {
				formula = Equality.checkForTrivialEqualityCases(formula, process);
			}
			else if (formula.hasFunctor(FunctorConstants.DISEQUALITY)) {
				formula = Disequality.checkForTrivialDisequalityCases(formula, process);
			}
			else if (formula.hasFunctor(FunctorConstants.AND)) {
				formula = eliminateBooleanConstantsInConjunction(formula);
			}
			else if (formula.hasFunctor(FunctorConstants.OR)) {
				formula = eliminateBooleanConstantsInDisjunction(formula);
			}
			else if (formula.hasFunctor(FunctorConstants.NOT)) {
				formula = eliminateBooleanConstantsInNegation(formula);
			}
			else if (formula.hasFunctor(FunctorConstants.IF_THEN_ELSE)) {
				formula = IfThenElse.simplify(formula);
			}
			else if (formula.hasFunctor(FunctorConstants.EQUIVALENCE)) {
				formula = Equivalence.simplify(formula);
			}
			else if (formula.hasFunctor(FunctorConstants.IMPLICATION)) {
				formula = Implication.simplify(formula);
			}
			else if (formula.getSyntacticFormType().equals(ForAll.SYNTACTIC_FORM_TYPE)) {
				formula = (new PlainTautologicalityDPLL()).rewrite(formula, process);
			}
			else if (formula.getSyntacticFormType().equals(ThereExists.SYNTACTIC_FORM_TYPE)) {
				formula = (new PlainSatisfiabilityDPLL()).rewrite(formula, process);
			}
		} while (formula != previous);
		
		return formula;
	}

	public static Expression eliminateBooleanConstantsInConjunction(Expression conjunction) {
		Expression result = conjunction;
		if (conjunction.getArguments().contains(Expressions.FALSE)) {
			result = Expressions.FALSE;
		}
		else {
			Not<Expression> notEqualToTrue = Not.make(Equals.make(Expressions.TRUE));
			LinkedHashSet<Expression> distinctArgumentsNotEqualToTrue = new LinkedHashSet<Expression>();
			Util.collect(conjunction.getArguments(), distinctArgumentsNotEqualToTrue, notEqualToTrue);
			if (distinctArgumentsNotEqualToTrue.size() != conjunction.getArguments().size()) {
				if (distinctArgumentsNotEqualToTrue.size() == 0) {
					result = Expressions.TRUE;
				}
				else if (distinctArgumentsNotEqualToTrue.size() == 1) {
					result = Util.getFirst(distinctArgumentsNotEqualToTrue);
				}
				else if (distinctArgumentsNotEqualToTrue.size() != conjunction.numberOfArguments()) {
					result = Expressions.apply(FunctorConstants.AND, distinctArgumentsNotEqualToTrue);
				}
			}
		}
		return result;
	}

	public static Expression eliminateBooleanConstantsInDisjunction(Expression disjunction) {
		Expression result = disjunction;
		if (disjunction.getArguments().contains(Expressions.TRUE)) {
			result = Expressions.TRUE;
		}
		else {
			Not<Expression> notEqualToFalse = Not.make(Equals.make(Expressions.FALSE));
			LinkedHashSet<Expression> distinctArgumentsNotEqualToFalse = new LinkedHashSet<Expression>();
			Util.collect(disjunction.getArguments(), distinctArgumentsNotEqualToFalse, notEqualToFalse);
			if (distinctArgumentsNotEqualToFalse.size() != disjunction.getArguments().size()) {
				if (distinctArgumentsNotEqualToFalse.size() == 0) {
					result = Expressions.FALSE;
				}
				else if (distinctArgumentsNotEqualToFalse.size() == 1) {
					result = Util.getFirst(distinctArgumentsNotEqualToFalse);
				}
				else if (distinctArgumentsNotEqualToFalse.size() != disjunction.numberOfArguments()) {
					result = Expressions.apply(FunctorConstants.OR, distinctArgumentsNotEqualToFalse);
				}
			}
		}
		return result;
	}

	public static Expression eliminateBooleanConstantsInNegation(Expression formula) {
		Expression result;
		if (formula.get(0).equals(Expressions.TRUE)) {
			result = Expressions.FALSE;
		}
		else if (formula.get(0).equals(Expressions.FALSE)) {
			result = Expressions.TRUE;
		}
		else if (formula.get(0).hasFunctor(FunctorConstants.NOT)) {
			result = formula.get(0).get(0);
		}
		else {
			result = formula;
		}
		return result;
	}

	public static Expression fromSolutionToShorterExpression(Expression solution, RewritingProcess process) {
		Expression result = solution;
		if (IfThenElse.isIfThenElse(solution)) {
			Expression splitter = IfThenElse.getCondition(solution);
			Expression thenBranch = fromSolutionToShorterExpression(IfThenElse.getThenBranch(solution), process);
			Expression elseBranch = fromSolutionToShorterExpression(IfThenElse.getElseBranch(solution), process);
			
			// if C then if C' then A else B else B  --->   if C and C' then A else B
			if (IfThenElse.isIfThenElse(thenBranch) && IfThenElse.getElseBranch(thenBranch).equals(elseBranch)) {
				Expression conditionsConjunction = And.make(splitter, IfThenElse.getCondition(thenBranch));
				result = IfThenElse.make(conditionsConjunction, IfThenElse.getThenBranch(thenBranch), elseBranch);
			}
			else {
				result = IfThenElse.makeIfDistinctFrom(solution, splitter, thenBranch, elseBranch);
			}
			
			result = simplifySolutionIfBranchesAreEqualModuloSplitter(result, process);
			
			result = IfThenElse.simplify(result);
		}
		return result;
	}

	public static Expression simplifySolutionIfBranchesAreEqualModuloSplitter(Expression solution, RewritingProcess process) {
		Expression result = solution;
		if (IfThenElse.isIfThenElse(solution)) {
			Expression splitter   = IfThenElse.getCondition(solution);
			Expression thenBranch = IfThenElse.getThenBranch(solution);
			Expression elseBranch = IfThenElse.getElseBranch(solution);
			if (equalModuloSplitter(splitter, thenBranch, elseBranch, process)) {
				result = elseBranch;
			}
		}
		return result;
	}
	
	/**
	 * Decides whether two solutions are equal modulo the splitter's equality.
	 * This is used to simplify
	 * <code>if X = a then a else X</code> to <code>X</code>,
	 * by providing the check that the then branch <code>a</code> is a special case of the else branch <code>X</code>
	 * and can therefore be replaced by the else branch <code>X</code>,
	 * after which the entire expression is replaced by <code>X</code>
	 * @param splitter
	 * @param solution1
	 * @param solution2
	 * @param process TODO
	 * @return whether the two solutions are equal modulo the splitter equality
	 */
	public static boolean equalModuloSplitter(Expression splitter, Expression solution1, Expression solution2, RewritingProcess process) {
		boolean result = solution1.equals(solution2);
		if ( ! result) {
			Expression solution2UnderSplitter = completeSimplifySolutionGivenEquality(solution2, splitter, process);
			result = solution1.equals(solution2UnderSplitter);
		}
		return result;
	}
	
	/**
	 * Applies an equality between two terms to a formula by replacing the first one by the second everywhere and simplifying the result,
	 * which is incomplete, that is, may contain tautological or contradictory conditions given their context.
	 */
	protected static Expression simplifyGivenEquality(Expression formula, Expression equalityOfTwoTerms, RewritingProcess process) {
		Expression term1 = equalityOfTwoTerms.get(0);
		Expression term2 = equalityOfTwoTerms.get(1);
		Expression result = formula.replaceAllOccurrences(term1, term2, process);
		result = simplify(result, process);
		return result;
	}

	/**
	 * Applies an disequality (which may, if convenient, be represented as the corresponding equality -- only the arguments are used)
	 * between two terms to a formula by simplifying each condition in it individually and simplifying the result,
	 * which is incomplete, that is, may contain tautological or contradictory conditions given their context.
	 */
	protected static Expression simplifyGivenDisequality(Expression formula, Expression disequality, RewritingProcess process) {
		Expression term1 = disequality.get(0);
		Expression term2 = disequality.get(1);
		Expression result = formula.replaceAllOccurrences(new SimplifyAtomGivenDisequality(term1, term2), process);
		result = simplify(result, process);
		return result;
	}

	/**
	 * Given a solution (a conditional expression where conditions are always two-argument equalities)
	 * that is complete (that is, no condition can be equivalently replaced by true or false)
	 * and an equality,
	 * finds an equivalent solution that is complete even under a context in which the given equality is true.
	 * It also guarantees that the first argument of the equality (which must be a variable) does not occur in the returned solution.
	 */
	protected static Expression completeSimplifySolutionGivenEquality(Expression solution, Expression equalityOfTwoTerms, RewritingProcess process) {
		// Some notes about the development of this and the next method are at the bottom of the file.
		// They discuss the main ideas, but the implementation still turned out a little different from them.
		
		Expression result = solution;
		
		if (Equality.isEquality(solution) || Disequality.isDisequality(solution)) {
			result = SimplifyFormula.simplifyGivenEquality(solution, equalityOfTwoTerms, process);
		}
		else if (IfThenElse.isIfThenElse(solution)) {
	
			Expression condition  = IfThenElse.getCondition (solution);
			Expression thenBranch = IfThenElse.getThenBranch(solution);
			Expression elseBranch = IfThenElse.getElseBranch(solution);
	
			Expression newCondition = simplifyGivenEquality(condition, equalityOfTwoTerms, process);
			
			if (newCondition.equals(Expressions.TRUE)) {
				result = completeSimplifySolutionGivenEquality(thenBranch, equalityOfTwoTerms, process);
			}
			else if (newCondition.equals(Expressions.FALSE)) {
				result = completeSimplifySolutionGivenEquality(elseBranch, equalityOfTwoTerms, process);
			}
			else {
				Expression newThenBranch = completeSimplifySolutionGivenEquality(thenBranch, equalityOfTwoTerms, process);
				Expression newElseBranch = completeSimplifySolutionGivenEquality(elseBranch, equalityOfTwoTerms, process);
				
				// solutions conditions must always have a variable as first argument
				newCondition = Equality.makeSureFirstArgumentIsNotAConstant(newCondition, process);

				if (newCondition != condition) {
					newThenBranch = completeSimplifySolutionGivenEquality(newThenBranch, newCondition, process);
					// It is important to realize why this second transformation on the then branch
					// does not invalidate the guarantees given by the first one,
					// as well as why individual completeness for equalityOfTwoTerms and newCondition
					// imply completeness with respect to their *conjunction*.
					// The guarantees of the first complete simplification given equalityOfTwoTerms are not lost because,
					// if they were, there would be a condition that could be replaced by true or false given equalityOfTwoTerms.
					// This however would require the first variable in equalityOfTwoTerms to be present, and it is not
					// because it was eliminated by the first complete simplification and it does not get re-introduced by the second one.
					// The completeness with respect to the conjunction comes from the fact that the only possible facts implied
					// by a conjunction of equalities that could simplify a condition while these individual equalities could not,
					// would be a consequence of them, and the only consequences of them are transitive consequences.
					// For example, X = Z can be simplified by the conjunction (X = Y and Y = Z), even though it cannot be simplified
					// by either X = Y or by Y = Z, but it can be simplified by X = Z which is a transitive consequence of the two.
					// However, such transitive consequences are explicitly produced by replacing the first argument of an equality
					// during the simplification. Using X = Y to replace all Y by X will replace Y in Y = Z and produce a new condition X = Z,
					// which represents the transitive consequence explicitly and which will simplify whatever conditions depend on it.
					//
					// Another approach is to consider every possible type of condition configuration.
					// It is more detailed and takes more work to implement, but it would save some unnecessary substitutions.
					// A schema of these substitutions is described in the file SimplifyFormulacompleteSimplifySolutionGivenEqualitySubstitutionSchemas.jpg
					// stored in the same directory as this file.
					newElseBranch = completeSimplifySolutionGivenEqualityNegation(newElseBranch, newCondition, process);
				}
			
				result = IfThenElse.makeIfDistinctFrom(solution, newCondition, newThenBranch, newElseBranch, false /* no simplification to condition */);
			}
		}
		
		return result;
	}

	/**
	 * Given a solution (a conditional expression where conditions are always two-argument equalities)
	 * that is complete (that is, no condition can be equivalently replaced by true or false)
	 * and an equality of two terms,
	 * finds an equivalent solution that is complete even under a context in which the given equality is false.
	 */
	protected static Expression completeSimplifySolutionGivenEqualityNegation(Expression solution, Expression equalityOfTwoTerms, RewritingProcess process) {
		Expression result = solution;
		
		if (Equality.isEquality(solution) || Disequality.isDisequality(solution)) {
			result = SimplifyFormula.simplifyGivenDisequality(solution, equalityOfTwoTerms, process);
		}
		else if (IfThenElse.isIfThenElse(solution)) {
	
			Expression condition  = IfThenElse.getCondition(solution);
			Expression thenBranch = IfThenElse.getThenBranch(solution);
			Expression elseBranch = IfThenElse.getElseBranch(solution);
	
			Expression newCondition = simplifyGivenDisequality(condition, equalityOfTwoTerms, process);
			
			if (newCondition.equals(Expressions.TRUE)) {
				result = completeSimplifySolutionGivenEqualityNegation(thenBranch, equalityOfTwoTerms, process);
			}
			else if (newCondition.equals(Expressions.FALSE)) {
				result = completeSimplifySolutionGivenEqualityNegation(elseBranch, equalityOfTwoTerms, process);
			}
			else {
				// Note that, at this point, newCondition is the same as condition because
				// the only simplification that a disequality performs when applied to a condition is to transform it to either true or false,
				// and this has already been tested to not have occurred.
				
				Expression equalityTheNegationOfWhichWillBeAppliedToThenBranch;
				if (Equality.isEquality(condition)) {
					equalityTheNegationOfWhichWillBeAppliedToThenBranch = adaptEqualityNegationToEqualityCondition(equalityOfTwoTerms, condition, process);
				}
				else {
					equalityTheNegationOfWhichWillBeAppliedToThenBranch = equalityOfTwoTerms;
				}

				Expression newThenBranch = thenBranch;
				if ( ! equalityTheNegationOfWhichWillBeAppliedToThenBranch.equals(Expressions.FALSE)) {
					       newThenBranch = completeSimplifySolutionGivenEqualityNegation(thenBranch, equalityTheNegationOfWhichWillBeAppliedToThenBranch, process);
				}
				Expression newElseBranch = completeSimplifySolutionGivenEqualityNegation(elseBranch, equalityOfTwoTerms,                                  process);
				
				result = IfThenElse.makeIfDistinctFrom(solution, newCondition, newThenBranch, newElseBranch, false /* no simplification to condition */);
			}
		}
		
		return result;
	}

	public static Expression adaptEqualityNegationToEqualityCondition(Expression equalityOfTwoTerms, Expression condition, RewritingProcess process) {
		Expression equalityTheNegationOfWhichWillBeAppliedToThenBranch;
		Expression disequality = Disequality.make(equalityOfTwoTerms.get(0), equalityOfTwoTerms.get(1));
		Expression disequalityToBeAppliedToThenBranch = simplifyGivenEquality(disequality, condition, process);

		assert ! disequalityToBeAppliedToThenBranch.equals(Expressions.FALSE): "Disequality cannot be rendered false by condition without newCondition having been computed as false earlier on";
		
		if (disequalityToBeAppliedToThenBranch.equals(Expressions.TRUE)) {
			equalityTheNegationOfWhichWillBeAppliedToThenBranch = Expressions.FALSE;
		}
		else {
			equalityTheNegationOfWhichWillBeAppliedToThenBranch = Equality.make(disequalityToBeAppliedToThenBranch.get(0), disequalityToBeAppliedToThenBranch.get(1));
		}
		// We need to simplify the disequality with the condition because the first variable in the condition is not present in the then branch;
		// therefore, if the disequality is about that first variable, it will be ineffectual as-is and needs to be replaced
		// by an disequality that is translated by the term used instead inside the then branch.
		// For example, if disequality is X != a and the condition is X = Y,
		// then the then branch does not contain X, but Y instead which represents X.
		// We must then apply the disequality Y != a.
		return equalityTheNegationOfWhichWillBeAppliedToThenBranch;
	}

	public static Expression simplifyUnderFormula(Expression expression, Expression constraint, RewritingProcess process) {
		Expression dnf = FormulaToDNF.convertToDNF(constraint, process);
		List<Expression> conjunctiveClauses = Or.getDisjuncts(dnf);
		List<Expression> versions =
				mapIntoList(
						conjunctiveClauses,
						conjunctiveClause -> simplifyGivenConjunctiveClause(expression, conjunctiveClause, process));
		Expression result = Or.make(versions);
		return result;
	}

	public static Expression simplifyGivenConjunctiveClause(Expression expression, Expression conjunctiveClause, RewritingProcess process) {
		List<Expression> literals = And.getConjuncts(conjunctiveClause);
		List<Expression> versions =
				mapIntoList(
						literals,
						literal -> {
							Expression splitter = SymbolEqualityConstraint.makeSplitterIfPossible(literal, Collections.emptyList(), process);
							return simplifyGivenEquality(expression, splitter, process);	
						});
		Expression result = And.make(versions);
		return result;
	}
}

/*
 * Some notes written while developing the complete simplification methods.
 * 
 * Theorem: Given a condition C and a solution S,
the simplification S[C] does not contain tautological or contradictory conditions.

C is of the form X = T.
If S is a leaf, the theorem holds trivially.
If S is "if C1 then S1 else S2"
    if C1 is X = T1
        S[C] is if T = T1 then S1[C] else S2[C]
        if T = T1 gets simplified to true or false, theorem holds
        Otherwise, we need to prove that S1[C] is complete
           under X = T and T = T1, and S2[C] is complete
           under X = T and T != T1.
        S1[C]: X does not occur in S1, only T1 and possibly T
        Example: S1 is (T = T1), then S1[C] is S1,
        so S[C] is if T = T1 then T = T1 then S2[C], not complete!

Counter example then is
C is X = T
S is if X = T1 then T = T1 else S2
S[C] thus is if T = T1 then T = T1 else S2[C], not complete.

Create model counting test for this externalization of the sum of two solutions

(if X = T then 1 else 0) + (if X = T1 then if T = T1 then 1 else 0 else 0)

In model counting, the above can arise from a splitting on an atom, say, Y = a, that works as a selector:

(Y = a and X = T) or (Y != a and X = T1 and T = T1)

Fix:
create an algorithm for applying condition that takes heed of new created conditions. Still need to prove that applying one condition at a time is enough.

Special case in which we don't need completeness (because, say, we are within DPLL so all conditions will be split anyway) still possibly desirable.
Can improve anyway by simplifying during condition application. It could be that simplifying to completeness right away is more efficient anyway.
At first I thought that perhaps that simple application is complete when inside DPLL anyway, but that is not true, counter-example above could occur.

Candidate equality application algorithm:
Let C be an equality X = T
Let S be a complete solution.

if S is a constant
    return S
else if S is "if C1 then S1 else S2"
    C1' <- atom-apply C to C1
    if C1' is true
        S <- apply C to S1
    else if C1' false
        S <- apply C to S2
    else if C1' is not C1
        S1 <- apply C to S1
        S2 <- apply C to S2
        S1 <- apply C1' to S1
        S2 <- apply C1' to S2
        S <- if C1' then S1 else S2

    return S
    
Post-conditions:
- the result is equivalent to S in the C-space. In other words,
for every model M that satisfies C, S and the result evaluate to the same value in M.

- Every condition in the result that is not 'true' or 'false' is both satisfied by some model in its own context,
and falsified by some other model in its own context,
including the top context being the models of C.

It seems hard to prove it because Si could be non-trivial for C, meaning it is true and false for different models of C,
and the same for C1', but still not be both true and false for different models of the *intersection* of C and C1'.
Perhaps this never happens for equality theory but we would need to take the theory into account to prove that.

Another approach is to represent the entire context as a graph of variables and disequalities and go down the formula applying it.
The graph is a consolidated representation of context and makes it easy to decide whether any atom is implied or not.
Then we have the following algorithm:

Joint application algorithm:
*** NEW *** C is a context graph
Let S be a complete solution.

if S is a constant
    return S
else if S is "if C1 then S1 else S2"
    if C1 is true in C
        S <- apply C to S1     // 10/2014: do we really need to apply C to S1 if S1 was already complete wrt C1?
    else if C1 is false in C
        S <- apply C to S2
    else
        CC1 <- apply C1 to constraint C // CC1 is constraint graph
        S1 <- apply CC1 to S1
        CnotC1 <- apply not C1 to constraint C
        S2 <- apply CnotC1 to S2
        S <- if C1 then S1 else S2

    return S

This is better but it rebuilds a lot of the context graph that had already been solved when S was built.
Can we leverage the fact that we know S is already solved and complete?

Let us consider case by case.

C is X = Y, C1 is Z = W
    can we bypass C1 because it is unaffected by C? What if we have Y = W and X = Z in the else clause of C1 (where Z != W)? Then that should be false, but bypassing does not pick it. We need the information that Z != W. However bypassing is safe in the then clause because Z is not present in it (the fact that Z = W is implicit everywhere).

C is X = Y, C1 is Y = W
    apply X = W to then clause
    apply to else clause keeping X != W.

C is X = Y, C1 is X = W
    if Y is W, return S1
    if Y cannot be W, return S2
    otherwise, need to apply Y = W to then clause
    apply to else clause keeping X != W.

C is X = Y, C1 is Z = Y
    bypass then clause, apply to else clause

C is X = Y, C1 is Z = X
    bypass then clause, apply to else clause

As we can see, we still need to keep the constraint graph for the else clause but can do a little better with the then clause.

What if C is a disequality?

C is X != Y, C1 is Z = W
    bypass C to then clause
    apply to else clause keeping C1 negation

C is X != Y, C1 is Y = W
    apply X != W to then clause
    apply to else clause keeping C1 negation.

C is X != Y, C1 is X = W
    if Y is W, return S2, because X = W <=> X = Y <=> false
    if Y cannot be W, return S because X != Y is irrelevant
    otherwise, need to apply W != Y to then clause
    apply to else clause keeping C1 negation.

C is X != Y, C1 is Z = Y
    apply X != Z to then clause
    apply to else clause keeping C1 negation.

C is X != Y, C1 is Z = X
    apply Z != Y to then clause
    apply to else clause keeping C1 negation.

Clearly this is a lot more efficient than the joint application shown above.
Besides, the joint application requires a constraint map that keeps equalities as well, which we don't have so far
(and which would be less efficient as well).
So this is the way to go.
This depends on the equalities being such that the first term does not appear in the then clause!

*/