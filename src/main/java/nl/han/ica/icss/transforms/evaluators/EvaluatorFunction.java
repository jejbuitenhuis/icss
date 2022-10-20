package nl.han.ica.icss.transforms.evaluators;

import java.util.ArrayList;

import nl.han.ica.datastructures.IScopeList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;

/**
 * A function which evaluates a `nodeToEvaluate` and returns a new
 * {@link ASTNode} that the `nodeToEvaluate` should be replaced with. The
 * `variableValues` is used to evaluate {@link VariableReference}s when found.
 */
@FunctionalInterface
public interface EvaluatorFunction {
	<T extends ASTNode> ArrayList<ASTNode> evaluate(
		T nodeToEvaluate,
		IScopeList<Literal> variableValues
	);
}
