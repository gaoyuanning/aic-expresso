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
package com.sri.ai.expresso.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.sri.ai.expresso.api.Expression;
import com.sri.ai.expresso.api.ExpressionAndContext;
import com.sri.ai.expresso.api.Symbol;
import com.sri.ai.expresso.api.SyntaxTree;
import com.sri.ai.expresso.core.DefaultCompoundSyntaxTree;
import com.sri.ai.expresso.core.DefaultExpressionAndContext;
import com.sri.ai.expresso.core.DefaultSymbol;
import com.sri.ai.grinder.api.RewritingProcess;
import com.sri.ai.grinder.library.Equality;
import com.sri.ai.grinder.library.FunctorConstants;
import com.sri.ai.grinder.library.Variables;
import com.sri.ai.grinder.library.boole.And;
import com.sri.ai.grinder.library.set.intensional.IntensionalSet;
import com.sri.ai.util.Util;
import com.sri.ai.util.base.Equals;
import com.sri.ai.util.base.GetFirstOfPair;
import com.sri.ai.util.base.GetSecondOfPair;
import com.sri.ai.util.base.NotContainedBy;
import com.sri.ai.util.base.Pair;
import com.sri.ai.util.base.SingletonListMaker;
import com.sri.ai.util.collect.FunctionIterator;
import com.sri.ai.util.collect.IntegerIterator;
import com.sri.ai.util.collect.ZipIterator;
import com.sri.ai.util.math.Rational;

/**
 * A class of helper methods for {@link Expression}s.
 * 
 * @author braz
 */
@Beta
public class Expressions {
	public static final Expression EMPTY_LIST      = new DefaultCompoundSyntaxTree("list");
	public static final Symbol     TRUE            = DefaultSymbol.createSymbol("true");
	public static final Symbol     FALSE           = DefaultSymbol.createSymbol("false");
	public static final Expression ZERO            = DefaultSymbol.createSymbol(0);
	public static final Expression ZERO_POINT_FIVE = DefaultSymbol.createSymbol(0.5);
	public static final Expression ONE             = DefaultSymbol.createSymbol(1);
	public static final Expression TWO             = DefaultSymbol.createSymbol(2);
	public static final Expression THREEE          = DefaultSymbol.createSymbol(3);
	//
	private static final SingletonListMaker<Integer> INTEGER_SINGLETON_LIST_MAKER = new SingletonListMaker<Integer>();

	/**
	 * If argument is a "kleene list" application, returns a {@link List} of its arguments;
	 * otherwise, returns a {@link List} containing this argument.
	 */
	public
	static List<Expression> ensureListFromKleeneList(Expression listOrSingleElementOfList) {
		boolean isListName = listOrSingleElementOfList != null && listOrSingleElementOfList.hasFunctor("kleene list");
		return (isListName ? listOrSingleElementOfList.getArguments() : Lists.newArrayList(listOrSingleElementOfList));
	}

	/**
	 * If argument is a "list" application, returns a {@link List} of its arguments;
	 * otherwise, returns a {@link List} containing this argument.
	 */
	public
	static List<Expression> ensureListFromList(Expression listOrSingleElementOfList) {
		boolean isListName = listOrSingleElementOfList != null && listOrSingleElementOfList.hasFunctor("kleene list");
		return (isListName? listOrSingleElementOfList.getArguments() : Lists.newArrayList(listOrSingleElementOfList));
	}

	/**
	 * Returns a "kleene list" application if given list of expressions is not singleton,
	 * and the single element itself otherwise.
	 * This is a inverse operation of {@ #ensureList(Expression)}.
	 */
	public static Expression makeKleeneListIfNeeded(List<Expression> list) {
		if (list.size() == 1) {
			return list.get(0);
		}
		return DefaultCompoundSyntaxTree.make("kleene list", list);
	}

	/**
	 * Returns a "list" application if given list is not singleton,
	 * and the single element itself otherwise.
	 * Wraps objects into expressions.
	 * This is a inverse operation of {@ #ensureList(Expression)}.
	 */
	public static <T> Expression makeListIfNeeded(Collection<T> objects) {
		if (objects.size() == 1 ) {
			return Expressions.wrap(Util.getFirstOrNull(objects));
		}
		return new DefaultCompoundSyntaxTree("list", Expressions.wrap(objects.toArray()).toArray());
	}
	
	public static boolean argumentsIntersect(Expression expression1, Expression expression2) {
		return Util.intersect(expression1.getArguments(), expression2.getArguments());
	}
	
	/** A static version of associate when check for associative operator is done, with predicate indicating whether argument of same functor is to be associated. */
	public static Expression associateWhenSureOperatorIsAssociative(Expression expression, Predicate<Expression> isAssociatable) {
		if (expression.numberOfArguments() == 0) {
			return expression;
		}
		List<Expression> resultArguments = new LinkedList<Expression>();
		Expression functor = expression.getFunctor();
		boolean change = false;
		for (Expression argument : expression.getArguments()) {
			if (argument.hasFunctor(functor) &&
					argument.numberOfArguments() > 1 &&
					isAssociatable.apply(argument)) {
				resultArguments.addAll(argument.getArguments());
				change = true;
			}
			else {
				resultArguments.add(argument);
			}
		}
		
		if (change) {
			return DefaultCompoundSyntaxTree.make(functor, resultArguments);
		}
		
		return expression;
	}

	/** A static version of associate when check for associative operator is done, with predicate indicating whether argument of same functor is to be associated. */
	public static Expression associateWhenSureOperatorIsAssociative(Expression expression) {
		Predicate<Expression> alwaysTrue = Predicates.alwaysTrue();
		Expression result = associateWhenSureOperatorIsAssociative(expression, alwaysTrue);
		return result;
	}
	
	/**
	 * Given a symbol assumed to be an identifier,
	 * returns a symbol with a minimum 0 or more prime ("'") characters appended to it
	 * to make it unique, according to a given predicate indicating uniqueness.
	 */
	public static Symbol primedUntilUnique(Symbol symbol, Predicate<Expression> isUnique) {
		while (! isUnique.apply(symbol)) {
			symbol = DefaultSymbol.createSymbol(symbol + "'");
		}
		return symbol;
	}
	
	/**
	 * Given a symbol assumed to be an identifier,
	 * returns a symbol with a minimum 0 or more prime ("'") characters appended to it
	 * to make it unique in a given expression.
	 */
	public static Symbol primedUntilUnique(Symbol symbol, Expression expression, RewritingProcess process) {
		LinkedHashSet<Expression> variables = Variables.get(expression, process.getIsConstantPredicate());
		Predicate<Expression> isUnique = new NotContainedBy<Expression>(variables);
		Symbol result = Expressions.primedUntilUnique(symbol, isUnique);
		return result;
	}

	/**
	 * Given a proposed name for a variable ensure that a valid unique variable
	 * is returned.
	 * 
	 * @param variableName
	 *            a proposed name for the variable.
	 * @param expression
	 *            an expression to check for other variables in order to ensure
	 *            uniqueness.
	 * @param process
	 *            the rewriting process that the variable is being created in.
	 * @return a uniquely named variable in the context of the passed in
	 *         expression.
	 */
	public static Symbol makeUniqueVariable(String variableName,
			Expression expression, RewritingProcess process) {
		// Variables have a leading captial
		if (variableName.length() > 0) {
			String leadingChar = variableName.substring(0, 1);
			if (!leadingChar.equals(leadingChar.toUpperCase())) {
				variableName = leadingChar.toUpperCase()
						+ variableName.substring(1);
			}
		} 
		else {
			variableName = "V";
		}

		return primedUntilUnique(DefaultSymbol.createSymbol(variableName), expression,
				process);
	}
	
	/**
	 * Indicates whether any of <code>expression</code> is a sub-expression of <code>expression</code>.
	 */
	public static boolean containsAnyOf(Expression expression, Collection<Expression> expressions) {
		return Util.thereExists(expressions, new In(expression));
	}

	/**
	 * Given a collection of expressions <code>c</code>,
	 * returns a map with the an entry for each expression <code>e</code> in <code>c</code>,
	 * defined as follows:
	 * <code>(key, value)</code>, if <code>e</code> is a binary application of <code>functor</code> on <code>(key, value)</code>,
	 * and <code>(e, defaultValueGivenKey.evaluate(e))</code> otherwise.
	 * This was originally motivated by the problem of mapping collections such as <code>X in Integer, Y, Z in Real</code>
	 * to something like <code>X -> Integer, Y -> type(Y), Z -> Real</code>.
	 */
	public static LinkedHashMap<Expression, Expression> getRelationalMap(Collection<Expression> expressions, Expression functor, Function<Expression, Expression> defaultValueGivenKey) {
		LinkedHashMap<Expression, Expression> result = new LinkedHashMap<Expression, Expression>();
		for (Expression expression : expressions) {
			Expression key;
			Expression value;
			if (expression.hasFunctor(functor)) {
				key = expression.get(0);
				value = expression.get(1);
			}
			else {
				key   = expression;
				value = defaultValueGivenKey.apply(key);
			}
			result.put(key, value);
		}
		return result;
	}

	/** Indicates whether an expression is a Symbol representing a numeric constant. */
	public static boolean isNumber(Expression expression) {
		return expression instanceof Symbol && ((Symbol)expression).getValue() instanceof Number;
	}
	
	/** Assumes the expression is a Symbol and returns its value as Number. */
	public static Number asNumber(Expression expression) {
		return (Number) ((Symbol) expression).getValue();
	}

	/** Gets an object and returns it if it is an expression, or a {@link DefaultSymbol} containing it as value. */
	public static SyntaxTree wrap(Object object) {
		if (object == null || object instanceof SyntaxTree) {
			return (SyntaxTree) object;
		}
		return DefaultSymbol.createSymbol(object);
	}

	/** The array version of {@link #wrap(Object)}. */
	public static List<SyntaxTree> wrap(Object[] array) {
		LinkedList<SyntaxTree> result = new LinkedList<SyntaxTree>();
		for (int i = 0; i!= array.length; i++) {
			SyntaxTree wrap = wrap(array[i]);
			result.add(wrap);
		}
		return result;
	}
	
	/** A version of {@link #wrap(Object)} getting an iterator and returning a list. */
	public static List<SyntaxTree> wrap(Iterator<Object> iterator) {
		List<SyntaxTree> result = Util.listFrom(new FunctionIterator<Object, SyntaxTree>(iterator, WRAPPER));
		return result;
	}

	/** A {@link Function} version of {@link #wrap(Object)}. */
	public static final Function<Object, SyntaxTree> WRAPPER = new Function<Object, SyntaxTree>() {
		@Override
		public SyntaxTree apply(Object object) {
			return wrap(object);
		}
	};
	
	/**
	 * Given an expression, a path, and a sub-expression,
	 * returns the expression with its path-sub-expression replaced by the given sub-expression.
	 * The path is a list of indices indicating a path in the expression tree.
	 * If there are no indices to be followed (path is empty), the sub-expression itself is returned.
	 * The method assumes the path describes an existing path-sub-expression.
	 */
	public static Expression replaceAtPath(Expression expression, List<Integer> path, Expression subExpression) {
		return replaceAtPath(expression, path, 0, subExpression);
	}

	/**
	 * Given an expression, a path, a position i in the path, and a sub-expression,
	 * returns the expression with its path-i-sub-expression replaced by the given sub-expression.
	 * The path is a list of indices indicating a path in the expression tree.
	 * The path-i-sub-expression is the expression obtained by following the path from the position i on.
	 * If there are no indices to be followed (i is equal to the path's length), the sub-expression is returned.
	 * The method assumes the path describes an existing path-i-sub-expression.
	 */
	private static Expression replaceAtPath(Expression expression, List<Integer> path, int i, Expression subExpression) {
		if (i != path.size()) {
			int index = path.get(i);
			Expression subExpressionAtI = replaceAtPath(expression.getSyntaxTree().getSubTree(index), path, i + 1, subExpression);
			 // does need to be sub tree
			Expression result = expression.getSyntaxTree().setImmediateSubTree(index, subExpressionAtI);
			return result;
		}
		return subExpression;
	}

	public static boolean isSubExpressionOf(Expression searched, Expression expression) {
		boolean result = Util.thereExists(new SubExpressionsDepthFirstIterator(expression), new Equals<Expression>(searched));
		return result;
	}

	/**
	 * Tests whether the functor of a given (possibly null) expression equals a given object.
	 */
	public static boolean hasFunctor(Expression expression, Object functor) {
		boolean result = expression != null && expression.hasFunctor(functor);
		return result;
	}

	public static Expression addExpressionToArgumentsOfFunctionApplication(Expression expression, Object newArgument) {
		ArrayList<Expression> newArguments = new ArrayList<Expression>(expression.getArguments());
		newArguments.add(wrap(newArgument));
		return DefaultCompoundSyntaxTree.make(expression.getFunctor(), newArguments);
	}

	public static Expression make(Object functor, Object... arguments) {
		if (arguments.length != 0) {
			if (arguments[0] instanceof Expression[]) {
				return make(functor, (Object[]) arguments[0]); // the cast avoids arguments[0] being wrapper in a new 1-element array
			}
			if (arguments[0] instanceof Collection) {
				return make(functor, ((Collection)arguments[0]).toArray());
			}
		}
	
		List<Expression> argumentExpressions = new LinkedList<Expression>();
		Iterator<Object> argumentIterator = Arrays.asList(arguments).iterator();
		while (argumentIterator.hasNext()) {
			Object object = argumentIterator.next();
			Expression argument = wrap(object);
			argumentExpressions.add(argument);
		}
		Expression functorExpression = wrap(functor);
		Expression result = DefaultCompoundSyntaxTree.make(functorExpression, argumentExpressions);
		return result;
	}

	public static Expression apply(Object functor, Object... arguments) {
		return make(functor, arguments);
	}

	public static List<Expression> getSyntaxTreesOfSubExpressions(Expression syntaxTree) {
		Expression expression = syntaxTree;
		List<Expression> result =
			getSyntaxTreesOfExpressions(expression.getImmediateSubExpressionsIterator());
		return result;
	}

	/**
	 * Returns a list of the syntax trees coming from an iterator of expressions.
	 */
	public static List<Expression> getSyntaxTreesOfExpressions(Iterator<Expression> expressions) {
		List<Expression> result = Util.listFrom(
				new FunctionIterator<Expression, Expression>(
						expressions,
						Expression.GET_SYNTAX_TREE));
		return result;
	}

	/**
	 * Returns a list of the syntax trees coming from a list of expressions.
	 */
	public static List<Expression> getSyntaxTreesOfExpressions(List<Expression> expressions) {
		List<Expression> result = Util.listFrom(
				new FunctionIterator<Expression, Expression>(
						expressions,
						Expression.GET_SYNTAX_TREE));
		return result;
	}
	
	/**
	 * Indicates whether this expression is a symbol, which is the same thing as a function application with no arguments.
	 */
	public static boolean isSymbol(Expression expression) {
		return expression.numberOfArguments() == 0;
	}
	
	public static boolean isSymbolOrFunctionApplication(Expression expression) {
		boolean result =
			expression.getSyntacticFormType().equals("Function application") ||
			expression.getSyntacticFormType().equals("Symbol");
		return result;
	}

	public static boolean isFunctionApplicationWithArguments(Expression expression) {
		boolean result =
			expression.getSyntacticFormType().equals("Function application") &&
			expression.numberOfArguments() > 0;
		return result;
	}
	
	/**
	 * Returns the value of the expression if it is a symbol expression (per {@link {{@link #isSymbol(Expression)},
	 * or <null> otherwise.
	 */
	public static Object value(Expression expression) {
		return expression.getValue();
	}
	
	public static final Function GET_FUNCTOR_OR_SYMBOL = new Function() {

		@Override
		public Object apply(Object object) {
			Expression expression = (Expression) object;
			return expression.getFunctorOrSymbol();
		}
	};
	
	/**
	 * Returns a copy of an expressions with its arguments replaced by new arguments,
	 * with same expression returned if the new arguments turn out to be the same
	 * objects as the current arguments.
	 */
	public static Expression replaceArguments(Expression expression, List<Expression> newArguments) {
		Iterator<Expression> newArgumentsIterator = newArguments.iterator();
		for (int i = 0; i != expression.numberOfArguments(); i++) {
			Expression newIthArgument = newArgumentsIterator.next();
			expression.set(i, newIthArgument);
		}
		return expression;
	}

	public static boolean isBooleanOperatorApplication(Expression condition) {
		boolean result = condition.getFunctor() != null &&
				FunctorConstants.BOOLEAN_FUNCTORS.contains(condition.getFunctor().toString());
		return result;
	}

	/**
	 * This method decomposes an expression into a pair of conjuncts,
	 * the first of them a formula on equalities (or disequalities) on atomic symbols,
	 * (ideally the largest possible such formula),
	 * and the second of them another formula,
	 * such that their conjunction is equivalent to the original condition.
	 */
	public static Pair<Expression, Expression> separateEqualityFormulasOnAtomicSymbolsFromRest(
			Expression expression, RewritingProcess process) {
		
		// deal with conjunctions first because we can separate their conjuncts into equality formulas and non-equality formulas. 
		if (expression.hasFunctor("and")) {
			List<Expression> subExpressions = Util.listFrom(expression.getImmediateSubExpressionsIterator());
			subExpressions.remove(0); // we don't process the functor.
			List<Pair<Expression, Expression>> splits = Util.mapIntoList(subExpressions, new SeparateEqualityFormulasOnAtomicSymbolsFromRest(process));
			List<Expression> constraintParts    = Util.mapIntoList(splits, new GetFirstOfPair<Pair<Expression, Expression>, Expression>());
			List<Expression> nonConstraintParts = Util.mapIntoList(splits, new GetSecondOfPair<Pair<Expression, Expression>, Expression>());
	
			Pair<Expression, Expression> trivialResult =
				Expressions.checkForTrivialResult(expression, constraintParts, nonConstraintParts, process);
			if (trivialResult != null) {
				return trivialResult;
			}
	
			Expression equalityFormulaOnAtomicSymbols    = And.make(constraintParts);
			Expression nonEqualityFormulaOnAtomicSymbols = And.make(nonConstraintParts);
			Pair<Expression, Expression> result =
				new Pair<Expression, Expression>(
						equalityFormulaOnAtomicSymbols, nonEqualityFormulaOnAtomicSymbols);
			return result;
		}
		else if (isEqualityFormulaOnAtomicSymbols(expression)) {
			Pair<Expression, Expression> result =
				new Pair<Expression, Expression>(expression, Expressions.TRUE);
			return result;
		}
		else {
			return new Pair<Expression, Expression>(TRUE, expression); // we don't know how to separate them, so we don't.
		}
	}
	
	final private static class SeparateEqualityFormulasOnAtomicSymbolsFromRest implements Function<Expression, Pair<Expression, Expression>> {
		private RewritingProcess process;

		public SeparateEqualityFormulasOnAtomicSymbolsFromRest(RewritingProcess process) {
			super();
			this.process = process;
		}

		@Override
		public Pair<Expression, Expression> apply(Expression expression) {
			return separateEqualityFormulasOnAtomicSymbolsFromRest(expression, process);
		}
	};

	public static Pair<Expression, Expression> checkForTrivialResult(
			Expression condition, List<Expression> equalityFormulaOnAtomicSymbols,
			List<Expression> nonEqualityFormulasOnAtomicSymbols,
			RewritingProcess process) {
		
		Pair<Expression, Expression> trivialResult = null;
		Equals<Expression> equalsTrue = new Equals<Expression>(TRUE);
		boolean allAreConstraintParts = Util.forAll(nonEqualityFormulasOnAtomicSymbols, equalsTrue);
		if (allAreConstraintParts) {
			trivialResult = new Pair<Expression, Expression>(condition, TRUE);
		}
		boolean allAreNonConstraintParts = Util.forAll(equalityFormulaOnAtomicSymbols, equalsTrue);
		if (allAreNonConstraintParts) {
			trivialResult = new Pair<Expression, Expression>(TRUE, condition);
		}
		return trivialResult;
	}

	public static boolean isEqualityFormulaOnAtomicSymbols(Expression expression) {
		 if (expression instanceof Symbol) {
			 return false;
		 }
		 if (expression.hasFunctor("=") || expression.hasFunctor("!=") || expression.hasFunctor("is")) {
			 return true;
		 }
		 if (FunctorConstants.BOOLEAN_FUNCTORS.contains(expression.getFunctor().toString())) {
			 boolean result = Util.forAll(expression.getArguments(), isEqualityFormulaOnAtomicSymbols);
			 return result;
		 }
		 return false;
	}
	
	private static Predicate<Expression> isEqualityFormulaOnAtomicSymbols = new Predicate<Expression>() {
		@Override
		public boolean apply(Expression expression) {
			return isEqualityFormulaOnAtomicSymbols(expression);
		}
	};
	
	/**
	 * Replaces all numeric symbols in expressions by  a rounded value according to a precision (a number of significant digits to be kept). 
	 */
	public static Expression roundToAGivenPrecision(Expression expression, final int precision) {
		Function<SyntaxTree, SyntaxTree> rounder = new Function<SyntaxTree, SyntaxTree>() {
			
			@Override
			public SyntaxTree apply(SyntaxTree subSyntaxTree) {
				SyntaxTree result = Expressions.round(subSyntaxTree, precision);
				return result;
			}
		};
		
		Expression result = ((SyntaxTree) expression).replaceSubTreesAllOccurrences(rounder);
		return result;
	}

	/**
	 * Takes a syntax tree and, if it is numeric symbol,
	 * replaces all numeric symbols in expressions by  a rounded value according to a precision (a number of significant digits to be kept); 
	 * otherwise, return the symbol itself.
	 */
	public  static SyntaxTree round(SyntaxTree syntaxTree, int precision) {
		if (syntaxTree instanceof Symbol && Expressions.isNumber(syntaxTree)) {
			Rational value = syntaxTree.rationalValue();
			String rounded = "";
			if (value.isInteger()) {
				rounded = value.toString();
			} 
			else {
				rounded = value.toStringDotRelative(precision);
			}
			return DefaultSymbol.createSymbol(rounded);
		}
		return syntaxTree;
	}

	public
	static Iterator<ExpressionAndContext> getSubExpressionsAndContextsIteratorFromImmediateSubTrees(
			Expression expression, RewritingProcess process) {
		if (expression == null) {
			List<ExpressionAndContext> emptyList = Collections.emptyList();
			return emptyList.iterator();
		}
	
		Iterator<? extends Expression> subTreesIterator = expression.getSyntaxTree().getImmediateSubTreesIterator();
		// does need to be sub tree
		FunctionIterator<Integer, List<Integer>> pathsIterator = Expressions.makeSingleIndexPathsIteratorFromTo(0, expression.getSyntaxTree().numberOfImmediateSubTrees());
		
		List<Expression> emptyList = Collections.emptyList();
		Iterator<ExpressionAndContext> result = Expressions.makeIteratorOfSubExpressionsAndContextsFromIteratorsOnSubTreesAndPathsWithGivenQuantifiedVariables(
				subTreesIterator, pathsIterator, emptyList /* no quantified variables */, process);
	
		return result;
	}

	public
	static Iterator<ExpressionAndContext> getSubExpressionsAndContextsIteratorFromImmediateSubTreesIncludingRootOne(
			Expression expression, RewritingProcess process) {
		if (expression == null) {
			List<ExpressionAndContext> emptyList = Collections.emptyList();
			return emptyList.iterator();
		}
	
		Iterator<? extends Expression> subTreesIterator = expression.getSyntaxTree().getImmediateSubTreesIncludingRootOneIterator();
		// does need to be sub tree
		FunctionIterator<Integer, List<Integer>> pathsIterator = Expressions.makeSingleIndexPathsIterator(expression);
		
		List<Expression> emptyList = Collections.emptyList();
		Iterator<ExpressionAndContext> result = Expressions.makeIteratorOfSubExpressionsAndContextsFromIteratorsOnSubTreesAndPathsWithGivenQuantifiedVariables(
				subTreesIterator, pathsIterator, emptyList /* no quantified variables */, process);
	
		return result;
	}

	/**
	 * Makes an iterator of sub-expressions and their contexts built from two iterators,
	 * one on the expression and another on their paths.
	 * The quantified variables of the sub expressions will be the one provided as argument.
	 */
	public
	static Iterator<ExpressionAndContext> makeIteratorOfSubExpressionsAndContextsFromIteratorsOnSubTreesAndPathsWithGivenQuantifiedVariables(
			Iterator<? extends Expression> expressionsIterator, 
			FunctionIterator<Integer, List<Integer>> pathsIterator, 
			Collection<Expression> quantifiedVariables, 
			RewritingProcess process) {
		
		Iterator<ExpressionAndContext> result =
			new FunctionIterator<List<Object>, ExpressionAndContext>(
					new ZipIterator(expressionsIterator, pathsIterator),
					new DefaultExpressionAndContext.MakerFromExpressionAndPathList(quantifiedVariables));
	
		return result;
	}

	public static FunctionIterator<Integer, List<Integer>> makeSingleIndexPathsIterator(Expression expression) {
		int start = -1;
		int end = expression.getSyntaxTree().numberOfImmediateSubTreesIncludingRootOneIterator() - 1;
		FunctionIterator<Integer, List<Integer>> result = Expressions.makeSingleIndexPathsIteratorFromTo(start, end);
		return result;
	}

	/**
	 * Makes iterator over paths with a single index, starting from <code>start</code> to <code>end</code> exclusive.
	 */
	public static FunctionIterator<Integer, List<Integer>> makeSingleIndexPathsIteratorFromTo(int start, int end) {
		IntegerIterator integerIterator = new IntegerIterator(start, end);
		FunctionIterator<Integer, List<Integer>> result = new FunctionIterator<Integer, List<Integer>>(integerIterator, INTEGER_SINGLETON_LIST_MAKER);
		return result;
	}

	/**
	 * Takes a formula and, if it is a conjunction, returns the list of conjuncts and,
	 * if it is not, returns the formula itself (thus considering it a 1-conjunct conjunction).
	 */
	public static List<Expression> takeFormulaAsConjunctionAndReturnConjuncts(Expression expression) {
		if (And.isConjunction(expression)) {
			return expression.getArguments();
		}
		else {
			return Util.list(expression);
		}
	}

	/**
	 * Returns a list of applications of a given functor to the corresponding elements in two lists
	 * (two elements are correspondent if they have the same indices).
	 */
	public static List<Expression> makePairwiseApplications(Object functor, List<Expression> list1, List<Expression> list2) {
		if (list1.size() != list2.size()) {
			throw new Error("Expressions.makePairwiseApplications(Object, List<Expression>, List<Expression>) expects two lists of same size.");
		}
		List<Expression> result = new LinkedList<Expression>();
		for (int i = 0; i != list1.size(); i++) {
			result.add(Expressions.make(functor, list1.get(i), list2.get(i)));
		}
		return result;
	}

	/**
	 * A small class for gathering the information regarding an index bound to some value in a given condition
	 * @author braz
	 */
	public static class BoundIndexInformation {
		/** The bound index */
		public Expression index;
		
		/** One of the values the index is bound to. */
		public Expression value;
		
		/** The index expressions minus the one on the bound index. */
		public List<Expression> indexExpressionsWithoutBoundIndex;
	}

	/**
	 * Returns a {@link BoundIndexInformation} object for the first index (among those defined in indexExpressions)
	 * bound in a given formula.
	 */
	public static BoundIndexInformation getBoundIndexInformation(Expression formula, List<Expression> indexExpressions) {
		BoundIndexInformation result = null;
		for (Expression conjunct : And.getConjuncts(formula)) {
			if (Equality.isEquality(conjunct)) {
				Collection<Expression> indexOrNothing = Util.list();
				Collection<Expression> remaining = Util.list();
		
				Util.collectFirstN(
						conjunct.getArguments(),
						1,
						new IntensionalSet.IsIndexIn(indexExpressions),
						indexOrNothing,
						remaining);
				
				if (indexOrNothing.size() == 1) {
					result = new BoundIndexInformation();
					result.index = Util.getFirst(indexOrNothing);
					result.value = Util.getFirst(remaining);
					result.indexExpressionsWithoutBoundIndex =
							Util.removeNonDestructively(
									indexExpressions, new IntensionalSet.IsIndexExpressionOnIndex(result.index));
					break;
				}
			}
		}
		return result;
	}
}
