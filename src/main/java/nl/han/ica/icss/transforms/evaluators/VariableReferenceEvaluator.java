package nl.han.ica.icss.transforms.evaluators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.VariableReference;

public class VariableReferenceEvaluator extends Evaluator
{
	@Override
	public <T extends ASTNode> ArrayList<ASTNode> evaluate(
		T nodeToEvaluate,
		IHANLinkedList< HashMap<String, Literal> > variableValues
	)
	{ // {{{
		if ( !(nodeToEvaluate instanceof VariableReference) )
			throw new RuntimeException( "Expected VariableReference, got " + nodeToEvaluate.getClass().getName() );

		VariableReference node = (VariableReference) nodeToEvaluate;

		this.setVariableValues(variableValues);

		return new ArrayList<>( List.of( this.getVariableValue(node.name) ) );
	} // }}}
}