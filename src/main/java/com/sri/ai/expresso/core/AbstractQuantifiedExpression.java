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
package com.sri.ai.expresso.core;

import static com.sri.ai.grinder.library.indexexpression.IndexExpressions.replaceArgument;
import static com.sri.ai.grinder.library.indexexpression.IndexExpressions.replaceOrAddType;
import static com.sri.ai.util.Util.castOrThrowError;

import java.util.List;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.sri.ai.expresso.api.Expression;
import com.sri.ai.expresso.api.ExpressionAndContext;
import com.sri.ai.expresso.api.QuantifiedExpression;
import com.sri.ai.expresso.api.SubExpressionAddress;
import com.sri.ai.grinder.core.AbstractExpression;
import com.sri.ai.grinder.library.indexexpression.IndexExpressions;

/**
 * An abstract implementation of a {@link QuantifiedExpression}.
 * 
 * @author braz
 */
@SuppressWarnings("serial")
@Beta
public abstract class AbstractQuantifiedExpression extends AbstractExpression implements QuantifiedExpression {

	private List<Expression> indexExpressions;
	
	public AbstractQuantifiedExpression(List<Expression> indexExpressions) {
		this.indexExpressions = indexExpressions;
	}
	
	@Override
	public List<Expression> getIndexExpressions() {
		return indexExpressions;
	}

	@Override
	public abstract QuantifiedExpression setIndexExpressions(List<Expression> newIndexExpressions);

	/**
	 * A convenience method (for extensions' benefit) generating the {@link ExpressionAndContext} objects with respect to the index expressions.
	 * Extensions of {@link AbstractQuantifiedExpression} will still need to generate the {@link ExpressionAndContext} objects
	 * referring to other sub-expressions.
	 */
	protected List<ExpressionAndContext> makeIndexExpressionSubExpressionsAndContext(List<ExpressionAndContext> result) {
		
		for (int indexExpressionIndex = 0; indexExpressionIndex != getIndexExpressions().size(); indexExpressionIndex++) {
			Expression indexExpression = getIndexExpressions().get(indexExpressionIndex);
			Expression index = IndexExpressions.getIndex(indexExpression);
			for (int argumentIndex = 0; argumentIndex != index.numberOfArguments(); argumentIndex++) {
				ExpressionAndContext expressionAndContext = makeAddressForIndexArgument(indexExpressionIndex, index, argumentIndex);
				result.add(expressionAndContext);
			}
			Expression type = IndexExpressions.getType(indexExpression);
			if (type != null) {
				ExpressionAndContext expressionAndContext = makeAddressForIndexType(indexExpressionIndex, type);
				result.add(expressionAndContext);
			}
		}
		
		return result;
	}

	private ExpressionAndContext makeAddressForIndexType(int indexExpressionIndex, Expression type) {
		SubExpressionAddress address = new IndexExpressionTypeSubExpressionAddress(indexExpressionIndex);
		ExpressionAndContext expressionAndContext = new DefaultExpressionAndContext(type, address);
		return expressionAndContext;
	}

	private ExpressionAndContext makeAddressForIndexArgument(int indexExpressionIndex, Expression index, int argumentIndex) {
		SubExpressionAddress address = new IndexExpressionArgumentSubExpressionAddress(indexExpressionIndex, argumentIndex);
		ExpressionAndContext expressionAndContext = new DefaultExpressionAndContext(index.get(argumentIndex), address);
		return expressionAndContext;
	}

	protected static class IndexExpressionSubExpressionAddress implements SubExpressionAddress {
		
		private int indexExpressionIndex;
		Function<Expression, Function<Expression, Expression>> functionFromNewSubExpressionToSubExpressionReplacementFunction;
		
		public IndexExpressionSubExpressionAddress(
				int indexExpressionIndex, Function<Expression, Function<Expression, Expression>> functionFromNewSubExpressionToSubExpressionReplacementFunction) {
			super();
			this.indexExpressionIndex = indexExpressionIndex;
			this.functionFromNewSubExpressionToSubExpressionReplacementFunction = functionFromNewSubExpressionToSubExpressionReplacementFunction;
		}

		@Override
		public Expression replace(Expression expression, Expression newSubExpression) {
			AbstractQuantifiedExpression quantifiedExpression = castOrThrowError(AbstractQuantifiedExpression.class, expression, "Attempt to use " + IndexExpressionTypeSubExpressionAddress.class.getSimpleName() + " to replace sub-expression " + newSubExpression + " in %s, but the latter should have been an instance of %s but is instead an instance of %s");
			Function<Expression, Expression> subExpressionReplacementFunction = functionFromNewSubExpressionToSubExpressionReplacementFunction.apply(newSubExpression);
			Expression result = quantifiedExpression.replaceIndexExpression(indexExpressionIndex, subExpressionReplacementFunction);
			return result;
		}
	}

	/**
	 * A sub-expression address referring to an argument of a function-application index expression.
	 * <p>
	 * For example, <code>new IndexExpressionArgumentSubExpressionAddress(1, 2)</code>
	 * would refer to sub-expression <code>g(c, d)</code>
	 * in
	 * <p>
	 * <code>there exists X in Things, f(a, b, g(c, d)) in boolean, Z : ... </code>
	 * <p>
	 * because, in the 2nd (that is, with index 1) index above (<code>f(a, b, g(c, d))</code>),
	 * the 3rd argument (that is, with index 2) is <code>g(c, d)</code>.
	 * <p>
	 * This class is left abstract because the implementation of
	 * {@link SubExpressionAddress#replace(Expression, Expression)}
	 * will depend on the particular implementation of {@link AbstractQuantifiedExpression}.
	 * 
	 * @author braz
	 *
	 */
	protected static class IndexExpressionArgumentSubExpressionAddress extends IndexExpressionSubExpressionAddress {
		public IndexExpressionArgumentSubExpressionAddress(int indexExpressionIndex, int argumentIndex) {
			super(indexExpressionIndex, newArgument -> (indexExpression -> replaceArgument(indexExpression, argumentIndex, newArgument)));
		}
	}

	/**
	 * Similar to {@link #IndexExpressionArgumentSubExpressionAddress},
	 * but referring to the type of an index expression.
	 * <p>
	 * For example, <code>new IndexExpressionTypeSubExpressionAddress(1)</code>
	 * would refer to sub-expression <code>boolean</code>
	 * in
	 * <p>
	 * <code>there exists X in Things, f(a, b, g(c, d)) in boolean, Z : ... </code>
	 * <p>
	 * because, in the 2nd (that is, with index 1) index expression above (<code>f(a, b, g(c, d)) in boolean</code>),
	 * the type is <code>boolean</code>.
	 * <p>
	 * This class is left abstract because the implementation of
	 * {@link SubExpressionAddress#replace(Expression, Expression)}
	 * will depend on the particular implementation of {@link AbstractQuantifiedExpression}.
	 *
	 * @author braz
	 *
	 */
	protected static class IndexExpressionTypeSubExpressionAddress extends IndexExpressionSubExpressionAddress {
		public IndexExpressionTypeSubExpressionAddress(int indexExpressionIndex) {
			super(indexExpressionIndex, newType -> (indexExpression -> replaceOrAddType(indexExpression, newType)));
		}
	}

	@Override
	public Expression getFunctor() {
		return null;
	}

	@Override
	public Expression set(int i, Expression newIthArgument) {
		return null;
	}
}