package nl.han.ica.icss.checker.checkers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.types.ExpressionType;

public class OperationChecker implements CheckerFunction
{
	private static final ArrayList<ExpressionType> DISALLOWED_TYPES = new ArrayList<>( Arrays.asList(
		ExpressionType.COLOR,
		ExpressionType.UNDEFINED
	) );

	@Override
	public <T extends ASTNode> void check(
		T nodeToCheck,
		IHANLinkedList< HashMap<String, ExpressionType> > variableTypes
	)
	{ // {{{
		if ( !(nodeToCheck instanceof Operation) )
			throw new RuntimeException( "Expected operation, got " + nodeToCheck.getClass().getName() );

		Operation node = (Operation) nodeToCheck;
		Expression left = node.lhs;
		Expression right = node.rhs;

		ExpressionType typeLeft = ExpressionType.fromExpression(left, variableTypes);

		// Check if left and right expressions aren't invalid types {{{

		// if left or right are an Operation, they will get checked later,
		// because we walk recursively through the AST
		if ( !(left instanceof Operation) )
		{
			if ( DISALLOWED_TYPES.contains(typeLeft) )
			{
				String error = String.format(
					"Cannot use type '%s' in '%s'",
					typeLeft,
					node.getClass().getSimpleName()
				);

				node.setError(error);

				return; // only one error per node allowed
			}
		}

		ExpressionType typeRight = ExpressionType.fromExpression(right, variableTypes);

		if ( !node.hasError() && !(right instanceof Operation) )
		{
			if ( DISALLOWED_TYPES.contains(typeRight) )
			{
				String error = String.format(
					"Cannot use type '%s' in '%s'",
					typeRight,
					node.getClass().getSimpleName()
				);

				node.setError(error);

				return; // only one error per node allowed
			}
		}
		// }}}

		// Check if left and right expressions are the same ExpressionTypes {{{

		// If one of the expressions is a scalar, it doesn't matter what the
		// other type is, because `scalar * something` is always allowed
		if (typeLeft == ExpressionType.SCALAR) return;
		if (typeRight == ExpressionType.SCALAR) return;

		if (typeLeft != typeRight)
		{
			String error = String.format(
				"Types in operation must be the same, but got '%s' and '%s'",
				typeLeft,
				typeRight
			);

			node.setError(error);

			return; // only one error per node allowed
		}
		// }}}
	} // }}}
}
