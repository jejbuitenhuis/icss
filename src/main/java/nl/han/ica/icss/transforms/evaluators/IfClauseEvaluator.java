package nl.han.ica.icss.transforms.evaluators;

import java.util.ArrayList;
import java.util.HashMap;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.BoolLiteral;

public class IfClauseEvaluator extends Evaluator
{
	@Override
	public <T extends ASTNode> ArrayList<ASTNode> evaluate(
		T nodeToEvaluate,
		IHANLinkedList< HashMap<String, Literal> > variableValues
	)
	{ // {{{
		if ( !(nodeToEvaluate instanceof IfClause) )
			throw new RuntimeException( "Expected IfClause, got " + nodeToEvaluate.getClass().getName() );

		IfClause node = (IfClause) nodeToEvaluate;

		this.setVariableValues(variableValues);

		Expression boolConditional = node.conditionalExpression;

		if (boolConditional instanceof VariableReference)
			boolConditional = this.getVariableValue( ( (VariableReference) boolConditional ).name );

		if ( !(boolConditional instanceof BoolLiteral) )
			throw new RuntimeException( "Expected boolean literal, got " + boolConditional.getClass().getName() );

		BoolLiteral conditional = (BoolLiteral) boolConditional;

		ArrayList<ASTNode> ifBody = node.body;
		ArrayList<ASTNode> elseBody = node.elseClause != null
			? node.elseClause.body
			: null;

		return conditional.value ? ifBody : elseBody;
	} // }}}
}