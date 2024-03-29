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
package com.sri.ai.grinder.library.number;

import static com.sri.ai.expresso.helper.Expressions.isNumber;
import static com.sri.ai.expresso.helper.Expressions.makeSymbol;
import static com.sri.ai.util.Util.greaterThanOrEqualTo;

import java.util.LinkedHashSet;

import com.google.common.annotations.Beta;
import com.sri.ai.expresso.api.Expression;
import com.sri.ai.expresso.helper.Expressions;
import com.sri.ai.grinder.library.BinaryOperator;
import com.sri.ai.grinder.library.FunctorConstants;
import com.sri.ai.util.Util;

/**
 * Implements a rewriter for the "greater than or equal to" operation.
 * 
 * @author braz
 */
@Beta
public class GreaterThanOrEqualTo extends BinaryOperator {

	public GreaterThanOrEqualTo() {
		this.functors = new LinkedHashSet<Expression>();
		this.functors.add(Expressions.makeSymbol(">="));
		//
		this.firstType  = Number.class;
		this.secondType = Number.class;
	}
	
	@Override
	protected Object operation(Expression expression1, Expression expression2) {
		return Util.greaterThanOrEqualTo(expression1.rationalValue(), expression2.rationalValue());
	}
	
	/**
	 * Receives an application of {@link FunctorConstants.GREATER_THAN_OR_EQUAL_TO} and evaluates it if possible.
	 * @param greaterThanOrEqualToApplication
	 * @return
	 */
	public static Expression simplify(Expression greaterThanOrEqualToApplication) {
		Expression result;
		if (isNumber(greaterThanOrEqualToApplication.get(0)) && isNumber(greaterThanOrEqualToApplication.get(1))) {
			result = makeSymbol(greaterThanOrEqualTo(greaterThanOrEqualToApplication.get(0).rationalValue(), greaterThanOrEqualToApplication.get(1).rationalValue()));
		}
		else {
			result = greaterThanOrEqualToApplication;
		}
		return result;
	}
}
