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
package com.sri.ai.grinder.plaindpll.api;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Predicate;
import com.sri.ai.expresso.api.Expression;
import com.sri.ai.grinder.api.Rewriter;
import com.sri.ai.grinder.api.RewritingProcess;

/**
 * The interface for any rewriting solving symbolic generalized summation problems.
 * 
 * @author braz
 *
 */
public interface Solver extends Rewriter {

	/** Solves a problem encoded in the given expression. */
	Expression rewriteAfterBookkeeping(Expression expression, RewritingProcess process);

	/** Makes a default, appropriate rewriting process for use with this solver as a rewriter. */
	RewritingProcess makeRewritingProcess(Expression expression);

	/**
	 * Returns the summation (or the provided semiring additive operation) of an expression
	 * over the provided set of indices under given non-null constraint.
	 */
	Expression solve(Expression expression, Collection<Expression> indices, Constraint constraint, RewritingProcess process);
	
	/**
	 * Returns the summation (or the provided semiring additive operation) of an expression over the provided set of indices.
	 */
	Expression solve(Expression expression, Collection<Expression> indices, RewritingProcess process);

	/**
	 * Convenience substitute for {@link #solve(Expression, Collection, RewritingProcess)} that takes care of constructing the RewritingProcess.
	 */
	Expression solve(
			Expression expression, Collection<Expression> indices,
			Map<String, String> mapFromVariableNameToTypeName, Map<String, String> mapFromTypeNameToSizeString);

	/**
	 * Convenience substitute for {@link #solve(Expression, Collection, RewritingProcess)} that takes care of constructing the RewritingProcess.
	 */
	Expression solve(
			Expression expression, Collection<Expression> indices,
			Map<String, String> mapFromVariableNameToTypeName, Map<String, String> mapFromTypeNameToSizeString,
			Predicate<Expression> isUniquelyNamedConstantPredicate);

	void interrupt();
	
	void setDebug(boolean b);
}