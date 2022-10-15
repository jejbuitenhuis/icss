package nl.han.ica.icss.checker.checkers;

import java.util.HashMap;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.types.ExpressionType;

public class IfClauseChecker implements CheckerFunction
{
	private static final ExpressionType ALLOWED_TYPE = ExpressionType.BOOL;

	@Override
	public <T extends ASTNode> void check(
		T nodeToCheck,
		IHANLinkedList< HashMap<String, ExpressionType> > variableTypes
	)
	{ // {{{
		if ( !(nodeToCheck instanceof IfClause) )
			throw new RuntimeException( "Expected if-clause, got " + nodeToCheck.getClass().getName() );

		IfClause node = (IfClause) nodeToCheck;
		ExpressionType conditionalType = ExpressionType.fromExpression(
			node.conditionalExpression,
			variableTypes
		);

		if (conditionalType != ALLOWED_TYPE)
		{
			String error = String.format(
				"Unexpected type '%s' in if-clause, expected type '%s'",
				conditionalType,
				ALLOWED_TYPE
			);

			node.setError(error);

			return; // only one error allowed per node
		}
	} // }}}
}
