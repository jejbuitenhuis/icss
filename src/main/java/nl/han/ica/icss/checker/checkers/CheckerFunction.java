package nl.han.ica.icss.checker.checkers;

import java.util.HashMap;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.types.ExpressionType;

/**
 * A function which checks a `nodeToCheck` and sets an error on that node
 * accordingly. This means that the state of the provided node can change (an
 * error can be set). The `variableTypes` is used to evaluate
 * {@link VariableReference}s when found.
 */
@FunctionalInterface
public interface CheckerFunction {
	<T extends ASTNode> void check(
		T nodeToCheck,
		final IHANLinkedList< HashMap<String, ExpressionType> > variableTypes
	);
}
