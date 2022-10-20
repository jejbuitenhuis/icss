package nl.han.ica.icss.transforms.evaluators;

import java.util.ArrayList;
import java.util.List;

import nl.han.ica.datastructures.IScopeList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.VariableReference;

public class VariableReferenceEvaluator implements EvaluatorFunction
{
	@Override
	public <T extends ASTNode> ArrayList<ASTNode> evaluate(
		T nodeToEvaluate,
		IScopeList<Literal> variableValues
	)
	{ // {{{
		if ( !(nodeToEvaluate instanceof VariableReference) )
			throw new RuntimeException( "Expected VariableReference, got " + nodeToEvaluate.getClass().getName() );

		VariableReference node = (VariableReference) nodeToEvaluate;

		return new ArrayList<>( List.of( variableValues.get(node.name) ) );
	} // }}}
}
