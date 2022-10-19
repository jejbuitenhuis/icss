package nl.han.ica.icss.transforms.evaluators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.VariableAssignment;

public class VariableAssignmentEvaluator extends Evaluator
{
	@Override
	public <T extends ASTNode> ArrayList<ASTNode> evaluate(
		T nodeToEvaluate,
		IHANLinkedList< HashMap<String, Literal> > variableValues
	)
	{ // {{{
		if ( !(nodeToEvaluate instanceof VariableAssignment) )
			throw new RuntimeException( "Expected VariableAssignment, got " + nodeToEvaluate.getClass().getName() );

		VariableAssignment node = (VariableAssignment) nodeToEvaluate;

		String name = node.name.name;
		Expression value = node.expression;

		if ( !(value instanceof Literal) )
			throw new RuntimeException( "Expected Literal, got " + value.getClass().getName() );

		variableValues.getFirst()
			.put( name, (Literal) value );

		return null;
	} // }}}
}
