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
package com.sri.ai.grinder.plaindpll.util;

import com.sri.ai.expresso.api.Expression;
import com.sri.ai.grinder.api.RewritingProcess;
import com.sri.ai.grinder.library.boole.And;
import com.sri.ai.grinder.library.controlflow.IfThenElse;
import com.sri.ai.grinder.plaindpll.api.ConstraintTheory;
import com.sri.ai.grinder.plaindpll.core.SGDPLLT;


/**
 * A collection of utility methods for post-processing {@link SGDPLLT} solutions.
 * 
 * @author braz
 *
 */
public class SolutionPostProcessing {

	public static Expression fromSolutionToShorterExpression(Expression solution, ConstraintTheory theory, RewritingProcess process) {
		Expression result = solution;
		if (IfThenElse.isIfThenElse(solution)) {
			result = simplifySolutionIfBranchesAreEqualModuloSplitter(result, theory, process);
			
			Expression splitter   = IfThenElse.condition(result);
			Expression thenBranch = fromSolutionToShorterExpression(IfThenElse.thenBranch(result), theory, process);
			Expression elseBranch = fromSolutionToShorterExpression(IfThenElse.elseBranch(result), theory, process);
			
			// if C then if C' then A else B else B  --->   if C and C' then A else B
			if (IfThenElse.isIfThenElse(thenBranch) && IfThenElse.elseBranch(thenBranch).equals(elseBranch)) {
				Expression conditionsConjunction = And.make(splitter, IfThenElse.condition(thenBranch));
				result = IfThenElse.make(conditionsConjunction, IfThenElse.thenBranch(thenBranch), elseBranch);
			}
			else {
				result = IfThenElse.makeIfDistinctFrom(result, splitter, thenBranch, elseBranch);
			}
			
			result = IfThenElse.simplify(result);
		}
		return result;
	}

	public static Expression simplifySolutionIfBranchesAreEqualModuloSplitter(Expression solution, ConstraintTheory theory, RewritingProcess process) {
		Expression result = solution;
		if (IfThenElse.isIfThenElse(solution)) {
			Expression splitter   = IfThenElse.condition(solution);
			Expression thenBranch = IfThenElse.thenBranch(solution);
			Expression elseBranch = IfThenElse.elseBranch(solution);
			if (equalModuloSplitter(splitter, thenBranch, elseBranch, theory, process)) {
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
	 * @param theoryWithEquality TODO
	 * @param process TODO
	 * @return whether the two solutions are equal modulo the splitter equality
	 */
	public static boolean equalModuloSplitter(Expression splitter, Expression solution1, Expression solution2, ConstraintTheory theory, RewritingProcess process) {
		boolean result = solution1.equals(solution2);
		if ( ! result) {
			Expression solution2UnderSplitter = DPLLUtil.applySplitterToSolution(true, splitter, solution2, theory, process);
			result = solution1.equals(solution2UnderSplitter);
		}
		return result;
	}
}