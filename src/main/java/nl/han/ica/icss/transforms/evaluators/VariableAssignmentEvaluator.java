package nl.han.ica.icss.transforms.evaluators;

import java.util.ArrayList;

import nl.han.ica.datastructures.IScopeList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableAssignment;

public class VariableAssignmentEvaluator implements EvaluatorFunction
{
	@Override
	public <T extends ASTNode> ArrayList<ASTNode> evaluate(
		T nodeToEvaluate,
		IScopeList<Literal> variableValues
	)
	{ // {{{
		if ( !(nodeToEvaluate instanceof VariableAssignment) )
			throw new RuntimeException( "Expected VariableAssignment, got " + nodeToEvaluate.getClass().getName() );

		VariableAssignment node = (VariableAssignment) nodeToEvaluate;

		String name = node.name.name;
		Expression value = node.expression;

		if (value instanceof Operation)
		{
			EvaluatorFunction function = new OperationEvaluator();

			ASTNode result = function.evaluate(value, variableValues)
				.get(0);

			if ( !(result instanceof Expression) )
				throw new RuntimeException( "Expected expression, got " + result.getClass().getName() );

			value = (Expression) result;
		}

		if ( !(value instanceof Literal) )
			throw new RuntimeException( "Expected Literal, got " + value.getClass().getName() );

		variableValues.set( name, (Literal) value );

		return null;
	} // }}}
}
