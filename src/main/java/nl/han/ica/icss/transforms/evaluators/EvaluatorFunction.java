package nl.han.ica.icss.transforms.evaluators;

import java.util.ArrayList;

import nl.han.ica.datastructures.IScopeList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.transforms.Evaluator;

/**
 * A function which evaluates a `nodeToEvaluate` and returns a List of
 * {@link ASTNode}s that the `nodeToEvaluate` should be replaced with. If the
 * returned value is `null`, the `nodeToEvaluate` shouldn't be replaced. The
 * `variableValues` is used to evaluate {@link VariableReference}s when found.
 */
@FunctionalInterface
public interface EvaluatorFunction {
	<T extends ASTNode> ArrayList<ASTNode> evaluate(
		T nodeToEvaluate,
		IScopeList<Literal> variableValues,
		Evaluator evaluator
	);
}
