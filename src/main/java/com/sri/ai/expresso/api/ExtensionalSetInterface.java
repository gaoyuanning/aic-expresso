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
package com.sri.ai.expresso.api;

import java.util.List;

import com.google.common.annotations.Beta;
import com.sri.ai.grinder.library.set.extensional.ExtensionalSet;

/**
 * An {@link Expression} that represents an extensionally defined set.
 * <p>
 * Currently named {@link ExtensionalSetInterface} in order not to be confused with {@link ExtensionalSet},
 * which will be phased out in time.
 * 
 * @author braz
 */
@Beta
public interface ExtensionalSetInterface extends Expression {
	
	/**
	 * Indicates whether the set is a uniset.
	 */
	public boolean isUniSet();
	
	/**
	 * Indicates whether the set is a multiset.
	 */
	public boolean isMultiSet();
	
	/**
	 * Returns the expressions describing the elements in the set.
	 * The method is named {@link #getElementsDefinitions()} instead of simply <code>getElements</code>
	 * to stress the distinctions about the sub-expressions defining the elements of set, and the elements themselves.
	 * For example, the extensionally defined set expression <code>{ X, X, a, a }</code> has <i>four</i> element definitions,
	 * but has two or perhaps even only one element (depending on whether the value of <code>X</code> is <code>a</code>).
	 */
	default public List<Expression> getElementsDefinitions() {
		return getSubExpressions();
	}

	/**
	 * Returns the i-th element definition.
	 */
	default public Expression getElementDefinition(int i) {
		return getSubExpressions().get(i);
	}

	/**
	 * Returns a new instance equivalent to this one but for the i-th element definition, which is replaced by the given one.
	 */
	public Expression setElementDefinition(int i, Expression newIthElementDefinition);
}
