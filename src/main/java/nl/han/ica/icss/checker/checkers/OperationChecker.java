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

		// if left or right are an Operation, they will get checked later,
		// because we walk recursively through the AST
		if ( !(left instanceof Operation) )
		{
			ExpressionType typeLeft = ExpressionType.fromExpression(left, variableTypes);

			if ( DISALLOWED_TYPES.contains(typeLeft) )
			{
				String error = String.format(
					"Cannot use type '%s' in '%s'",
					typeLeft,
					node.getClass().getSimpleName()
				);

				node.setError(error);
			}
		}

		if ( !node.hasError() && !(right instanceof Operation) )
		{
			ExpressionType typeRight = ExpressionType.fromExpression(right, variableTypes);

			if ( DISALLOWED_TYPES.contains(typeRight) )
			{
				String error = String.format(
					"Cannot use type '%s' in '%s'",
					typeRight,
					node.getClass().getSimpleName()
				);

				node.setError(error);
			}
		}
	} // }}}
}

