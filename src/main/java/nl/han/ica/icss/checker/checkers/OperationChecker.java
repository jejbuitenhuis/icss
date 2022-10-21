package nl.han.ica.icss.checker.checkers;

import java.util.ArrayList;
import java.util.Arrays;
import nl.han.ica.datastructures.IScopeList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
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
		IScopeList<ExpressionType> variableTypes
	)
	{ // {{{
		if ( !(nodeToCheck instanceof Operation) )
			throw new RuntimeException( "Expected operation, got " + nodeToCheck.getClass().getName() );

		Operation node = (Operation) nodeToCheck;
		Expression left = node.lhs;
		Expression right = node.rhs;

		// Check if left and right expressions aren't invalid types {{{
		ExpressionType typeLeft = ExpressionType.fromExpression(left, variableTypes);

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

		// only the MultiplyOperation allows for Scalars
		if (node instanceof MultiplyOperation)
		{
			// one of the sides is a Scalar, so it doesn't matter what the
			// other side is
			if (
				typeLeft == ExpressionType.SCALAR
				|| typeRight == ExpressionType.SCALAR
			)
				return;

			// only non-scalars are left, which means the MultiplyOperation is
			// never allowed (e.g. % * %, px * px, % * px, etc)
			node.setError( String.format(
				"Cannot use type '%s' in combination with type '%s' in '%s'",
				typeLeft,
				typeRight,
				node.getClass().getSimpleName()
			) );

			return; // only one error per node allowed
		}

		// left and right need to be of the same type
		if (typeLeft != typeRight)
		{
			node.setError( String.format(
				"Cannot use type '%s' in combination with type '%s' in '%s'",
				typeLeft,
				typeRight,
				node.getClass().getSimpleName()
			) );

			return; // only one error per node allowed
		}
		// }}}
	} // }}}
}
