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
package com.sri.ai.grinder.library.boole;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import com.google.common.annotations.Beta;
import com.google.common.base.Predicate;
import com.sri.ai.expresso.api.Expression;
import com.sri.ai.expresso.helper.ExpressionIsSymbolOfType;
import com.sri.ai.expresso.helper.Expressions;
import com.sri.ai.grinder.library.CommutativeAssociative;
import com.sri.ai.grinder.library.FunctorConstants;
import com.sri.ai.util.Util;

/**
 * An atomic rewriter of Boolean "or" expressions. Includes related helper methods.
 * 
 * @author braz
 *
 */
@Beta
public class Or extends BooleanCommutativeAssociative {

	public static final Object FUNCTOR = FunctorConstants.OR;
	//
	private final static Expression            neutralElement              = Expressions.makeSymbol(false);
	private final static Expression            absorbingElement            = Expressions.makeSymbol(true);
	private final static Predicate<Expression> isOperableArgumentPredicate = new ExpressionIsSymbolOfType(Boolean.class);

	@Override
	public Object getFunctor() {
		return FunctorConstants.OR;
	}
	
	@Override
	protected Expression getNeutralElement() {
		return neutralElement;
	}
	
	@Override
	protected Expression getAbsorbingElement() {
		return absorbingElement;
	}
	
	@Override
	protected boolean isIdempotent() {
		return true;
	}

	@Override
	protected Predicate<Expression> getIsOperableArgumentSyntaxTreePredicate() {
		return isOperableArgumentPredicate;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object operationOnOperableValues(List<? extends Object> listOfOperableArguments) {
		return Util.or((Collection<Boolean>)listOfOperableArguments);
	}
	
	public static Expression simplify(Expression disjunction) {
		Expression result = disjunction;
		if (disjunction.getArguments().contains(Expressions.TRUE)) {
			result = Expressions.TRUE;
		}
		else {
			LinkedHashSet<Expression> distinctArgumentsNotEqualToFalse = new LinkedHashSet<Expression>();
			Util.collect(disjunction.getArguments(), distinctArgumentsNotEqualToFalse, e -> ! e.equals(Expressions.FALSE));
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

	public static boolean isDisjunction(Expression expression) {
		return expression.hasFunctor(FUNCTOR);
	}	

	/**
	 * Same as {@link CommutativeAssociative#make(Object, List, Object, boolean)},
	 * but not requiring the functor, neutral element, absorbing element and idempotency
	 */
	public static Expression make(Expression... arguments) {
		return make(Arrays.asList(arguments));
	}
	
	/**
	 * Same as {@link CommutativeAssociative#make(Object, List, Object, boolean)},
	 * but not requiring the functor, neutral element, absorbing element and idempotency.
	 */
	public static Expression make(List<Expression> arguments) {
		return CommutativeAssociative.make(FunctorConstants.OR, arguments, Expressions.TRUE, Expressions.FALSE, true);
	}
	
	/**
	 * Same as {@link CommutativeAssociative#make(Iterator<Expression>, Object, Object, Object)},
	 * but not requiring the parameters already determined for or applications.
	 */
	public static Expression make(Iterator<Expression> argumentsIterator) {
		return CommutativeAssociative.make(FunctorConstants.OR, argumentsIterator, Expressions.TRUE, Expressions.FALSE, true);
	}

	/**
	 * If given condition is a disjunction, returns list of disjuncts.
	 * Otherwise, returns singleton list with condition itself.
	 */
	public static List<Expression> getDisjuncts(Expression condition) {
		if (condition.hasFunctor(FunctorConstants.OR)) {
			return condition.getArguments();
		}
		return Util.list(condition);
	}
}
