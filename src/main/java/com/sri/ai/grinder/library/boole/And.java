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

import java.util.ArrayList;
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
 * An atomic rewriter of Boolean "and" expressions. Includes related helper methods.
 * 
 * @author braz
 *
 */
@Beta
public class And extends BooleanCommutativeAssociative {

	public static final  Object FUNCTOR = "and";
	//
	private final static Expression            neutralElement              = Expressions.makeSymbol(true);
	private final static Expression            absorbingElement            = Expressions.makeSymbol(false);
	private final static Predicate<Expression> isOperableArgumentPredicate = new ExpressionIsSymbolOfType(Boolean.class);

	@Override
	public Object getFunctor() {
		return "and";
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
		return Util.and((Collection<Boolean>)listOfOperableArguments);
	}
	
	public static Expression simplify(Expression conjunction) {
		Expression result = conjunction;
		if (conjunction.getArguments().contains(Expressions.FALSE)) {
			result = Expressions.FALSE;
		}
		else {
			LinkedHashSet<Expression> distinctArgumentsNotEqualToTrue = new LinkedHashSet<Expression>();
			Util.collect(conjunction.getArguments(), distinctArgumentsNotEqualToTrue, e -> ! e.equals(Expressions.TRUE));
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

	public static boolean isConjunction(Expression expression) {
		return expression.hasFunctor(FUNCTOR);
	}

	/**
	 * Same as {@link CommutativeAssociative#make(Object, List, Object, boolean)},
	 * but not requiring the functor, neutral element, absorbing element, and idempotency.
	 */
	public static Expression make(List<Expression> arguments) {
		return CommutativeAssociative.make("and", arguments, Expressions.FALSE, Expressions.TRUE, true);
	}

	/**
	 * Same as {@link CommutativeAssociative#make(Object, List, Object, boolean)},
	 * but not requiring the functor and neutral element,
	 * and not requiring a list, using varargs instead.
	 */
	public static Expression make(Expression... arguments) {
		return make(Arrays.asList(arguments));
	}

	/**
	 * Same as {@link CommutativeAssociative#make(Iterator<Expression>, Object, Object, Object)},
	 * but not requiring the parameters already determined for and applications.
	 */
	public static Expression make(Iterator<Expression> argumentsIterator) {
		return CommutativeAssociative.make("and", argumentsIterator, Expressions.FALSE, Expressions.TRUE, true);
	}

	/**
	 * If given condition is a conjunction, returns list of conjuncts.
	 * Otherwise, returns singleton list with condition itself.
	 */
	public static List<Expression> getConjuncts(Expression condition) {
		if (condition.hasFunctor(FunctorConstants.AND)) {
			return condition.getArguments();
		}
		return Util.list(condition);
	}

	public static Expression addConjunct(Expression conjunction, Expression newConjunct) {
		List<Expression> conjuncts = getConjuncts(conjunction);
		List<Expression> newConjuncts = new ArrayList<Expression>(conjuncts.size() + 1);
		newConjuncts.addAll(conjuncts);
		newConjuncts.add(newConjunct);
		Expression result = And.make(newConjuncts);
		return result;
	}
}
