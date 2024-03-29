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
package com.sri.ai.grinder.core;

import java.util.Iterator;

import com.google.common.annotations.Beta;
import com.google.common.base.Predicate;
import com.sri.ai.expresso.api.Expression;
import com.sri.ai.expresso.core.AbstractModuleNoOpRewriter;
import com.sri.ai.expresso.helper.SubExpressionsDepthFirstIterator;
import com.sri.ai.grinder.api.Module;
import com.sri.ai.grinder.api.RewritingProcess;
import com.sri.ai.util.Util;

/**
 * An open interpretation expression is one whose interpretation depends on the
 * interpretation of expressions not present in itself. An example of this is,
 * in the application of probabilistic reasoning, the function "value" on random
 * variables, which maps to the current interpretation of the logical term
 * representing that random variable's value. I(value(X_i)) is I(x_i), and in
 * fact I(value) is a function defined in terms of I for other expressions.
 * 
 * The open interpretation module allows providers to specify whether certain
 * expressions depend on other expressions even if the latter are not explicit
 * sub-expressions of the former.
 * 
 * @author braz
 */
@Beta
public class OpenInterpretationModule extends AbstractModuleNoOpRewriter {

	public static interface OpenInterpretationModuleProvider extends Module.Provider {
		/**
		 * Indicates whether an expression has an open interpretation in relation to another expression
		 * (that is, whether its interpretation depends on the interpretation of that other expression).
		 */
		boolean isOpenInterpretationExpressionWithRespectTo(Expression expression, Expression anotherExpression, RewritingProcess process);
	}

	/**
	 * Registers a {@link Provider} in the {@link OpenInterpretationModule} module of the given process,
	 * or throw an error if there is not one.
	 */
	public static void register(Provider provider, RewritingProcess process) throws Error {
		register(OpenInterpretationModule.class, provider, process);
	}

	public boolean isOpenInterpretationExpressionWithRespectTo(Expression expression, Expression anotherExpression, RewritingProcess process) {
		boolean result;
		for (Provider provider : providers.keySet()) {
			OpenInterpretationModuleProvider openInterpretationModuleProvider = (OpenInterpretationModuleProvider) provider;
			result = openInterpretationModuleProvider.isOpenInterpretationExpressionWithRespectTo(expression, anotherExpression, process);
			if (result) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds OpenInterpretationModule from process and run {@link #hasSubExpressionWithOpenInterpretationWithRespectTo(Expression, Expression, RewritingProcess)}.
	 */
	public static boolean findModuleAndIndicateOpenInterpretationWithRespectTo(Expression expression, Expression anotherExpression, RewritingProcess process) {
		OpenInterpretationModule openInterpretationModule =
			(OpenInterpretationModule) process.findModule(OpenInterpretationModule.class);
		if (openInterpretationModule == null) {
			return false;
		}
		boolean result = openInterpretationModule.hasSubExpressionWithOpenInterpretationWithRespectTo(expression, anotherExpression, process);
		return result;
	}

	public boolean hasSubExpressionWithOpenInterpretationWithRespectTo(Expression expression, Expression anotherExpression, RewritingProcess process) {
		Iterator<Expression> iterator = new SubExpressionsDepthFirstIterator(expression);
		boolean result = Util.thereExists(iterator, new IsOpenInterpretationExpression(anotherExpression, process));
		return result;
	}

	public class IsOpenInterpretationExpression implements Predicate<Expression> {

		private Expression variable;
		private RewritingProcess process;

		public IsOpenInterpretationExpression(Expression variable, RewritingProcess process) {
			this.variable = variable;
			this.process = process;
		}

		@Override
		public boolean apply(Expression expression) {
			boolean result = isOpenInterpretationExpressionWithRespectTo(expression, variable, process);
			return result;
		}
	}
}
